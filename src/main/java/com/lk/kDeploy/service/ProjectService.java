package com.lk.kDeploy.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lk.kDeploy.constants.ReturnCode;
import com.lk.kDeploy.entity.Project;
import com.lk.kDeploy.exception.ServiceException;
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

	public void deploy(Project project) {
		String name = project.getName();
		String sourcePath = project.getProjectSourcePath();
		
		File projectPath = new File(sourcePath + name);

		try {
			FileUtils.forceMkdir(projectPath);
		} catch (IOException e) {
			throw new ServiceException(ReturnCode.EXECUTE_COMMAND_ERROR);
		}
		
//		FilenameUtils.
	}
	
}
