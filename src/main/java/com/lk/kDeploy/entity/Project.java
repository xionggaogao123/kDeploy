package com.lk.kDeploy.entity;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 项目实体类
 *
 * @author: lk
 * @since: 2017年11月6日
 */
@Table(name = "project")
public class Project extends BaseEntity {

	public static final int NONE_DEPLOYED = 0;
	public static final int STATUS_STARTED = 1;
	public static final int STATUS_STOPPED = 2;
	
	private String name; // 名称 使用git仓库中的项目名
	private String description; // 描述
	private String gitUrl; // 远程git仓库地址
	private String branch; // 分支名
	private String projectSourcePath; // 项目源码路径
	private String projectDeployPath; // 项目发布路径
	private String packageName; // 包名 完整包名，如：demo.jar
	private String deploySubModule; // 发布子模块名称 发布的子模块文件夹名
	private Integer deployWay; // 发布方法 1: 直接运行; 2: 拷贝到容器
	private String customStart; // 自定义启动命令
	private String webContainerId; // 容器id
	
	@Transient
	private Integer status; // 状态。0 未部署, 1 启动中, 2 已停止
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getGitUrl() {
		return gitUrl;
	}
	public void setGitUrl(String gitUrl) {
		this.gitUrl = gitUrl;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getProjectSourcePath() {
		return projectSourcePath;
	}
	public void setProjectSourcePath(String projectSourcePath) {
		this.projectSourcePath = projectSourcePath;
	}
	public String getProjectDeployPath() {
		return projectDeployPath;
	}
	public void setProjectDeployPath(String projectDeployPath) {
		this.projectDeployPath = projectDeployPath;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getDeploySubModule() {
		return deploySubModule;
	}
	public void setDeploySubModule(String deploySubModule) {
		this.deploySubModule = deploySubModule;
	}
	public Integer getDeployWay() {
		return deployWay;
	}
	public void setDeployWay(Integer deployWay) {
		this.deployWay = deployWay;
	}
	public String getCustomStart() {
		return customStart;
	}
	public void setCustomStart(String customStart) {
		this.customStart = customStart;
	}
	public String getWebContainerId() {
		return webContainerId;
	}
	public void setWebContainerId(String webContainerId) {
		this.webContainerId = webContainerId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
