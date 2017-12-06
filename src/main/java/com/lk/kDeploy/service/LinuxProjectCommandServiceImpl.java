package com.lk.kDeploy.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.lk.kDeploy.base.vo.ProjectListVO;
import com.lk.kDeploy.constants.Constants;
import com.lk.kDeploy.constants.ReturnCode;
import com.lk.kDeploy.entity.Project;
import com.lk.kDeploy.exception.ServiceException;

/**
 * 项目命令业务层
 * linux版本
 *
 * @author: lk
 * @since: 2017年11月28日
 */
@Service
public class LinuxProjectCommandServiceImpl implements ProjectCommandService {
	protected static final Logger LOG = LoggerFactory.getLogger(LinuxProjectCommandServiceImpl.class);
	
	@Autowired
	private ProjectService projectService;

	@Autowired
	private CommandService commandService;
	
	@Override
	public void initialize(Project project) {
		try {
			File shell = new ClassPathResource("shellTemp/project.sh").getFile();
			String shellStr = FileUtils.readFileToString(shell, "utf-8");
			
			shellStr.replace("{{id}}", project.getId());
			shellStr.replace("{{name}}", project.getName());
			shellStr.replace("{{gitUrl}}", project.getGitUrl());
			shellStr.replace("{{branch}}", project.getBranch());
			shellStr.replace("{{projectSourcePath}}", project.getProjectSourcePath());
			shellStr.replace("{{projectDeployPath}}", project.getProjectDeployPath());
			shellStr.replace("{{packageName}}", project.getPackageName());
			shellStr.replace("{{deploySubModule}}", project.getDeploySubModule());
			
			String fileName = project.getName() + ".sh";
			FileUtils.writeStringToFile(new File(Constants.SHELL_PATH + fileName), shellStr, "utf-8");
		} catch (IOException e) {
			LOG.error("初始化项目shell文件失败", e);
			throw new ServiceException(ReturnCode.PROJECT_SOURCE_PATH_MAKE_ERROR);
		}
	}
	
	@Override
	public Integer getStatus(String id) {
		if (getStartedProjectIds().contains(id)) {
			return 1;
		}
		return 0;
	}

	@Override
	public void setStatus(List<ProjectListVO> resList) {
		Set<String> startedSet = getStartedProjectIds();
		
		resList.forEach(project -> {
			if (startedSet.contains(project.getId())) {
				project.setStatus(1);
				return;
			}
			project.setStatus(0);
		});
	}

	/**
	 * 获取存在线程的项目id集合
	 * @return
	 */
	private Set<String> getStartedProjectIds() {
		String echo = commandService.execute("ps -ef | grep java");
		
		String[] split = echo.split("\n");
		Set<String> set = new HashSet<>();
		for (String processStr : split) {
			if (StringUtils.isBlank(processStr)) {
				continue;
			}
			
			String id = getProjectId(processStr);
			if (StringUtils.isNotBlank(id) && id.length() == 36) {
				set.add(id);
			}
		}
		return set;
	}
	private String getProjectId(String processStr) {
		Pattern pattern = Pattern.compile("\\s([a-z0-9_]+)-\\S+(\\.jar|\\.war)\\s");
		Matcher matcher = pattern.matcher(processStr);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
	
	@Override
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

	@Override
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
