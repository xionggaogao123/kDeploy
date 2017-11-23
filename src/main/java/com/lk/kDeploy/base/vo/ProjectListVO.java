package com.lk.kDeploy.base.vo;

import com.lk.kDeploy.entity.Project;

/**
 * 
 *
 * @author: lk
 * @since: 2017年11月7日
 */
public class ProjectListVO {

	private String id;
	private String name; // 名称 使用git仓库中的项目名
	private String description; // 描述
	private Integer status; // 状态。0 未部署, 1 启动中, 2 已停止
	
	public ProjectListVO() {}
	public ProjectListVO(Project java) {
		this.id = java.getId();
		this.name = java.getName();
		this.description = java.getDescription();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
