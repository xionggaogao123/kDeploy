package com.lk.kDeploy.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lk.kDeploy.base.annotion.AnonymousAccess;
import com.lk.kDeploy.base.dto.ResponceDTO;
import com.lk.kDeploy.entity.Project;
import com.lk.kDeploy.mapper.ProjectMapper;
import com.lk.kDeploy.service.ProjectService;
import com.lk.kDeploy.util.JsonUtils;
import com.lk.kDeploy.util.RespBuildUtil;

/**
 * 
 *
 * @author: lk
 * @since: 2017年11月7日
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
	private static final Logger LOG = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ProjectMapper projectMapper;
	
	@GetMapping
	@AnonymousAccess
    public ResponceDTO count() {
        LOG.info("project count: {}", projectMapper.count());
        return RespBuildUtil.success();
    }
	
	@GetMapping("/findOne")
	@AnonymousAccess
	public ResponceDTO findOne() {
		Project java = new Project();
		java.setId("12345678");
		Project project = projectMapper.selectOne(java);
		
		LOG.info("fingOne: {}", JsonUtils.toJson(project));
		return RespBuildUtil.success(project);
	}
	
	@GetMapping("/findAll")
	@AnonymousAccess
	public ResponceDTO findAll() {
		Project java = new Project();
		java.setId("12345678");
		List<Project> list = projectMapper.selectAll();
		
		LOG.info("findAll: {}", JsonUtils.toJson(list));
		return RespBuildUtil.success(list);
	}
	
	@GetMapping("/insert")
	@AnonymousAccess
	public ResponceDTO insert() {
		Project java = new Project();
		java.setId("12345679");
		java.setName("demo2");
		java.setGitUrl("http://124.com");
		java.setProjectSourcePath("/ProjectSourcePath");
		java.setProjectDeployPath("/ProjectDeployPath");
		java.setPackageName("demo2.war");
		java.setCreateAt(LocalDateTime.now());
		int row = projectMapper.insert(java);
		
		LOG.info("insert res: {}", row);
		return RespBuildUtil.success();
	}
	
}
