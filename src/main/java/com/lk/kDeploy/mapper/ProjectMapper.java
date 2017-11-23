package com.lk.kDeploy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lk.kDeploy.base.BaseMapper;
import com.lk.kDeploy.entity.Project;

public interface ProjectMapper extends BaseMapper<Project> {

	@Select("<script>SELECT * FROM project <where><if test='name != null'>name like '%${name}%'</if></where> LIMIT #{from}, #{pageSize}</script>")
	public List<Project> pageList(@Param("name") String name, @Param("from") Integer from, @Param("pageSize") Integer pageSize);

	@Select("<script>SELECT COUNT(1) FROM project <where><if test='name != null'>name like '%${name}%'</if></where></script>")
	public int count(@Param("name") String name);
}
