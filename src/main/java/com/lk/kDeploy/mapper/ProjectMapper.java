package com.lk.kDeploy.mapper;

import org.apache.ibatis.annotations.Select;

import com.lk.kDeploy.base.BaseMapper;
import com.lk.kDeploy.entity.Project;

public interface ProjectMapper extends BaseMapper<Project> {

	@Select("SELECT COUNT(1) FROM project")
	public int count();
}
