package com.lk.kDeploy.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lk.kDeploy.base.dto.RequestDTO;
import com.lk.kDeploy.base.dto.ResponceDTO;
import com.lk.kDeploy.base.vo.ProjectSimpleVO;
import com.lk.kDeploy.constants.ReturnCode;
import com.lk.kDeploy.entity.Project;
import com.lk.kDeploy.service.ProjectService;
import com.lk.kDeploy.util.RespBuildUtil;

/**
 * 
 *
 * @author: lk
 * @since: 2017年11月7日
 */
@RestController
@RequestMapping("/api/project")
public class ProjectController {
	protected static final Logger LOG = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private ProjectService projectService;
	
	@PostMapping("/pageList")
	public ResponceDTO pageList(@RequestBody RequestDTO reqDto) {
		Integer page = reqDto.getPage();
		Integer pageSize = reqDto.getPageSize();
		if (null == page || null == pageSize) {
			LOG.info("缺少分页参数");
			return RespBuildUtil.error(ReturnCode.PAGE_PARAM_BLANK_ERROR);
		}
		
		String name = reqDto.getStringParam("name");
		List<Project> list = projectService.pageList(name, reqDto.getSelectFrom(), pageSize);
		int total = projectService.count(name);
		
		List<ProjectSimpleVO> resList = list.stream().map(ProjectSimpleVO::new).collect(Collectors.toList());
		return RespBuildUtil.success(resList, page, pageSize, total);
	}
	
}
