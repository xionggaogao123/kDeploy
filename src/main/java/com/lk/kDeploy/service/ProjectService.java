package com.lk.kDeploy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lk.kDeploy.entity.Project;
import com.lk.kDeploy.mapper.ProjectMapper;

/**
 * 项目操作业务层
 *
 * @author: lk
 * @since: 2017年11月6日
 */
@Service
public class ProjectService {

	@Autowired
	private ProjectMapper projectMapper;
	
	public List<Project> pageList(String name, Integer from, Integer pageSize) {
		return projectMapper.pageList(name, from, pageSize);
	}

	public int count(String name) {
		Project java = new Project();
		java.setName(name);
		return projectMapper.selectCount(java);
	}
}
