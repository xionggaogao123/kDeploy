package com.lk.kDeploy.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lk.kDeploy.constants.ReturnCode;
import com.lk.kDeploy.entity.Project;
import com.lk.kDeploy.exception.ServiceException;
import com.lk.kDeploy.mapper.ProjectMapper;

/**
 * 项目操作业务层
 *
 * @author: lk
 * @since: 2017年11月6日
 */
@Service
public class ProjectService {
	protected static final Logger LOG = LoggerFactory.getLogger(ProjectService.class);
	
	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private CommandService commandService;
	
	public List<Project> pageList(String name, Integer from, Integer pageSize) {
		return projectMapper.pageList(name, from, pageSize);
	}

	public int count(String name) {
		return projectMapper.count(name);
	}

	public Project getById(String id) {
		Project java = new Project();
		java.setId(id);
		return projectMapper.selectOne(java);
	}
	
	public void save(Project project) {
		projectMapper.insert(project);
	}
	
	public void update(Project project) {
		projectMapper.updateByPrimaryKey(project);
	}
	
	public void delete(String id) {
		projectMapper.deleteByPrimaryKey(id);
	}
	
	/**
	 * 拉取项目并推送命令回显
	 * @param project
	 * @param username
	 */
	public void gitpull(Project project, String username) {
		String name = project.getName();
		String sourcePath = project.getProjectSourcePath();
		String projectPath = sourcePath + name;
		
		File projectDir = new File(projectPath);
		if (!projectDir.exists()) {
			LOG.info("项目源码不存在，克隆源码。projectName: {}", name);
			gitClone(project, username, false);
			return;
		}
		
		File projectGitDir = FileUtils.getFile(projectDir, ".get");
		if (!projectGitDir.exists() || !projectGitDir.isDirectory()) {
			LOG.info("项目源码地址没有git相关文件，暴力克隆源码。projectName: {}", name);
			gitClone(project, username, true);
			return;
		}
		
		File gitConfig = FileUtils.getFile(projectGitDir, "config");
		String gitUrl = getGitUrl(gitConfig);
		if (project.getGitUrl().equals(gitUrl)) {
			LOG.info("项目源码被被别的git项目占用，暴力克隆源码。projectName: {}", name);
			gitClone(project, username, true);
		}
		
		String branch = project.getBranch();
		if (StringUtils.isNotBlank(branch)) {
			LOG.info("项目配置有分支属性。branch: {}", branch);
			
			String nowBranch = getBranch(project);
			LOG.info("项目当前项目分支。nowBranch: {}", nowBranch);
			if (!branch.equals(nowBranch)) {
				String command = String.format("cd %s & git checkout %s", projectPath, branch);
				commandService.executeAndPushLog(username, command);
			}
		}
		
		String command = String.format("cd %s & git pull", projectPath);
		commandService.executeAndPushLog(username, command);
	}

	public void deploy(Project project, String username) {
		String name = project.getName();
		String sourcePath = project.getProjectSourcePath();
		
		// TODO
	}

	/**
	 * 从远程仓库克隆代码
	 * @param project
	 * @param username
	 * @param force
	 */
	private void gitClone(Project project, String username, boolean force) {
		String name = project.getName();
		String sourcePath = project.getProjectSourcePath();
		String projectPath = sourcePath + name;
		
		File projectDir = new File(projectPath);
		
		try {
			FileUtils.forceMkdir(projectDir);
		} catch (IOException e) {
			LOG.info("创建源码路径失败。projectPath: {}", projectPath);
			throw new ServiceException(ReturnCode.EXECUTE_COMMAND_ERROR);
		}
		
		if (force) {
			try {
				LOG.info("清空源码目录中的文件。projectDir: {}", projectDir.getAbsolutePath());
				FileUtils.cleanDirectory(projectDir);
			} catch (IOException e) {
				LOG.info("清空目录报错。dir: {}", projectDir.getPath());
			}
		}
		
		String gitUrl = project.getGitUrl();
		String branch = project.getBranch();
		
		String command = String.format("cd %s & git clone %s", sourcePath, gitUrl);
		if (StringUtils.isNotBlank(branch)) {
			command += String.format(" & git checkout %s", branch);
		}
		commandService.executeAndPushLog(username, command);
	}
	
	private String getGitUrl(File gitConfig) {
		String configStr = null;
		try {
			configStr = FileUtils.readFileToString(gitConfig, "UTF-8");
		} catch (IOException e) {
			LOG.error("读取git项目配置文件报错", e);
			throw new ServiceException();
		}
		
		Pattern pattern = Pattern.compile("url = (\\S+)");
		Matcher matcher = pattern.matcher(configStr);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
	
	private String getBranch(Project project) {
		String name = project.getName();
		String sourcePath = project.getProjectSourcePath();
		String projectPath = sourcePath + name;
		
		String command = String.format("cd %s & git branch", projectPath);
		String branchInfo = commandService.execute(command);
		
		Pattern pattern = Pattern.compile("* (\\S+)");
		Matcher matcher = pattern.matcher(branchInfo);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

}
