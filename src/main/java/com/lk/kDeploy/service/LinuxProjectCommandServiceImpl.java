package com.lk.kDeploy.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lk.kDeploy.base.vo.ProjectListVO;
import com.lk.kDeploy.constants.Constants;
import com.lk.kDeploy.constants.ProjectOperationConst;
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
	private CommandService commandService;
	
	/**
	 * https://segmentfault.com/a/1190000009275813
	 */
	@Override
	public void initialize(Project project) {
		LOG.info("初始化shell文件。projectName: {}", project.getName());
		try {
			InputStream stream = getClass().getClassLoader().getResourceAsStream(Constants.SHELL_TMPL_PROJECT_FILE);
			String shellStr = IOUtils.toString(stream, "utf-8");
			
			shellStr = shellStr.replace("{{id}}", project.getId());
			shellStr = shellStr.replace("{{name}}", project.getName());
			shellStr = shellStr.replace("{{gitUrl}}", project.getGitUrl());
			shellStr = shellStr.replace("{{branch}}", null == project.getBranch() ? "" : project.getBranch());
			shellStr = shellStr.replace("{{projectSourcePath}}", project.getProjectSourcePath());
			shellStr = shellStr.replace("{{projectDeployPath}}", project.getProjectDeployPath());
			shellStr = shellStr.replace("{{packageName}}", project.getPackageName());
			shellStr = shellStr.replace("{{deploySubModule}}", null == project.getDeploySubModule() ? "" : project.getDeploySubModule());
			
			shellStr = shellStr.replaceAll("\\\\r\\\\n", "\n"); // 替换掉windows的换行符
			
			File shell = getShellFile(project.getName());
			if (shell.exists()) {
				LOG.info("删除旧shell文件。file: {}", shell.getAbsolutePath());
				FileUtils.deleteQuietly(shell);
			}
			FileUtils.writeStringToFile(shell, shellStr, "utf-8");
		} catch (IOException e) {
			LOG.error("初始化项目shell文件失败", e);
			throw new ServiceException(ReturnCode.PROJECT_CREATE_SHELL_ERROR);
		}
	}

	@Override
	public void uninitialize(Project project) {
		String name = project.getName();
		
		File shell = getShellFile(name);
		LOG.info("删除旧shell文件。file: {}", shell.getAbsolutePath());
		FileUtils.deleteQuietly(shell);
		
		String projectSourceDir = project.getProjectSourcePath() + name;
		String projectDeployDir = project.getProjectDeployPath() + name;
		try {
			FileUtils.deleteDirectory(new File(projectSourceDir));
		} catch (IOException e) {
			LOG.error("删除源码文件夹失败, projectName: {}", name, e);
		}
		try {
			FileUtils.deleteDirectory(new File(projectDeployDir));
		} catch (IOException e) {
			LOG.error("删除部署文件夹失败, projectName: {}", name, e);
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

	@Override
	public void gitpull(Project project, String username) {
		String name = project.getName();
		String sourcePath = project.getProjectSourcePath();
		
		File projectDir = new File(sourcePath + name);
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
			return;
		}
		
		String branch = project.getBranch();
		if (StringUtils.isNotBlank(branch)) {
			LOG.info("项目配置有分支属性。branch: {}", branch);
			
			String nowBranch = getProjectBranch(project);
			LOG.info("项目当前项目分支。nowBranch: {}", nowBranch);
			if (!branch.equals(nowBranch)) {
				LOG.info("切换分支。projectName: {}, branch: {}", name, branch);
				execProjOpsAndPush(username, project, ProjectOperationConst.GIT_CHECKOUT);
			}
		}
		
		execProjOpsAndPush(username, project, ProjectOperationConst.GIT_PULL);
	}
	
	@Override
	public void checkout(Project project, String username) {
		execProjOpsAndPush(username, project, ProjectOperationConst.GIT_CHECKOUT);
	}

	@Override
	public void deploy(Project project, String username) {
		String name = project.getName();
		String deployPath = project.getProjectDeployPath() + name;
		
		File deployDir = new File(deployPath);
		
		try {
			FileUtils.forceMkdir(deployDir);
		} catch (IOException e) {
			LOG.info("创建部署文件夹失败。deployPath: {}", deployPath);
			throw new ServiceException(ReturnCode.PROJECT_DEPLOY_PATH_MAKE_ERROR);
		}
		
		execProjOpsAndPush(username, project, ProjectOperationConst.DEPLOY);
	}
	
	@Override
	public void startup(Project project, String username) {
		execProjOpsAndPush(username, project, ProjectOperationConst.STARTUP);
	}

	@Override
	public void shutdown(Project project, String username) {
		execProjOpsAndPush(username, project, ProjectOperationConst.SHUTDOWN);
	}
	
	/**
	 * 从远程仓库克隆代码
	 * @param project
	 * @param username
	 * @param force
	 */
	private void gitClone(Project project, String username, boolean force) {
		String name = project.getName();
		String sourcePath = project.getProjectSourcePath() + name;
		
		File sourceDir = new File(sourcePath);
		
		try {
			FileUtils.forceMkdir(sourceDir);
		} catch (IOException e) {
			LOG.info("创建源码目录失败。projectPath: {}", sourcePath);
			throw new ServiceException(ReturnCode.PROJECT_SOURCE_PATH_MAKE_ERROR);
		}
		
		if (force) {
			try {
				LOG.info("清空源码目录中的文件。projectDir: {}", sourceDir.getAbsolutePath());
				FileUtils.cleanDirectory(sourceDir);
			} catch (IOException e) {
				LOG.info("清空目录报错。dir: {}", sourceDir.getPath());
			}
		}
		
		execProjOpsAndPush(username, project, ProjectOperationConst.GIT_CLONE);
		
		String branch = project.getBranch();
		if (StringUtils.isNotBlank(branch)) {
			LOG.info("切换分支。projectName: {}, branch: {}", name, branch);
			execProjOpsAndPush(username, project, ProjectOperationConst.GIT_CHECKOUT);
		}
	}

	private void execProjOpsAndPush(String username, Project project, String operation) {
		File shellFile = getShellFileStrong(project);
		String command = String.format("sh %s %s", shellFile.getAbsolutePath(), operation);
		commandService.executeAndPushLog(username, command);
	}
	private String execProjOps(Project project, String operation) {
		File shellFile = getShellFileStrong(project);
		String command = String.format("sh %s %s", shellFile.getAbsolutePath(), operation);
		return commandService.execute(command);
	}
	
	/**
	 * 获取shell文件
	 * @param projectName
	 * @return
	 */
	private File getShellFile(String projectName) {
		String fileName = projectName + ".sh";
		return new File(Constants.SHELL_PATH_DIR + fileName);
	}
	
	/**
	 * 获取shell文件，若未初始化则初始化一次
	 * @param project
	 * @return
	 */
	private File getShellFileStrong(Project project) {
		File shellFile = getShellFile(project.getName());
		if (shellFile.exists()) {
			return shellFile;
		}
		
		initialize(project);
		shellFile = getShellFile(project.getName());
		if (shellFile.exists()) {
			return shellFile;
		}
		
		throw new ServiceException(ReturnCode.PROJECT_CREATE_SHELL_ERROR);
	}
	
	/**
	 * 通过配置文件获取远程git仓库地址
	 * @param gitConfig
	 * @return
	 */
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
	
	/**
	 * 获取项目当前的分支名
	 * @param project
	 * @return
	 */
	private String getProjectBranch(Project project) {
		String branchInfo = execProjOps(project, ProjectOperationConst.GIT_BRANCH);
		
		Pattern pattern = Pattern.compile("* (\\S+)");
		Matcher matcher = pattern.matcher(branchInfo);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
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
			
			String id = getProjectIdByProcessStr(processStr);
			if (StringUtils.isNotBlank(id) && id.length() == 36) {
				set.add(id);
			}
		}
		return set;
	}
	/**
	 * 
	 * @param processStr 例子：dev      22690     1  0 15:49 ?        00:00:43 java -jar a2d44bbf_8c80_4dcf_8a1c_7441753f9f67-quickpay-app-start-1.0.0-SNAPSHOT.jar --spring.config.location=application.yml
	 * @return
	 */
	private String getProjectIdByProcessStr(String processStr) {
		Pattern pattern = Pattern.compile("\\s([a-z0-9_]+)-\\S+(\\.jar|\\.war)\\s");
		Matcher matcher = pattern.matcher(processStr);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

}
