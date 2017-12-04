package com.lk.kDeploy.service;

import java.util.List;

import com.lk.kDeploy.base.vo.ProjectListVO;
import com.lk.kDeploy.entity.Project;

public interface ProjectCommandService {
	
	/**
	 * 初始化项目命令
	 * @param id
	 */
	void initialize(String id);

	/**
	 * 通过项目id获取项目状态
	 * @param id
	 * @return 状态。0 未部署, 1 启动中, 2 已停止 
	 */
	Integer getStatus(String id);

	/**
	 * 批量设置项目状态
	 * @param resList
	 */
	void setStatus(List<ProjectListVO> resList);

	/**
	 * 拉取项目并推送命令回显
	 * @param project
	 * @param username
	 */
	void gitpull(Project project, String username);

	/**
	 * 打包部署项目
	 * @param project
	 * @param username
	 */
	void deploy(Project project, String username);

}