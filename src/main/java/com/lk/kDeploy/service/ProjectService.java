package com.lk.kDeploy.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lk.kDeploy.entity.Project;
import com.lk.kDeploy.mapper.ProjectMapper;

/**
 * 项目数据库操作业务层
 *
 * @author: lk
 * @since: 2017年11月6日
 */
@Service
public class ProjectService {
	protected static final Logger LOG = LoggerFactory.getLogger(ProjectService.class);
	
	@Autowired
	private ProjectMapper projectMapper;
	
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
	
}
