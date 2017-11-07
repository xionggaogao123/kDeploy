package com.lk.kDeploy.entity;

import java.time.LocalDateTime;

import javax.persistence.Id;

/**
 * 基础实体类
 *
 * @author: lk
 * @since: 2017年11月6日
 */
public class BaseEntity {

	@Id
	private String id;
	private LocalDateTime createAt;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LocalDateTime getCreateAt() {
		return createAt;
	}
	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
	}
}
