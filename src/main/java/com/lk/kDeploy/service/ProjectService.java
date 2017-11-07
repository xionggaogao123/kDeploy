package com.lk.kDeploy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public int count() {
		return projectMapper.count();
	}
}
