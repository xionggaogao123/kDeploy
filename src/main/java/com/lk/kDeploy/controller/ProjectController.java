package com.lk.kDeploy.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lk.kDeploy.base.annotion.ValidateRequired;
import com.lk.kDeploy.base.dto.RequestDTO;
import com.lk.kDeploy.base.dto.ResponseDTO;
import com.lk.kDeploy.base.vo.ProjectListVO;
import com.lk.kDeploy.constants.Constants;
import com.lk.kDeploy.constants.ReturnCode;
import com.lk.kDeploy.entity.Project;
import com.lk.kDeploy.exception.ServiceException;
import com.lk.kDeploy.service.ProjectCommandService;
import com.lk.kDeploy.service.ProjectService;
import com.lk.kDeploy.util.JsonUtils;
import com.lk.kDeploy.util.RespBuildUtil;
import com.lk.kDeploy.util.UUIDUtil;

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
	
	@Autowired
	private ProjectCommandService projectCommandService;
	
	@PostMapping("/pageList")
	public ResponseDTO pageList(@RequestBody RequestDTO reqDto) {
		Integer page = reqDto.getPage();
		Integer pageSize = reqDto.getPageSize();
		if (null == page || null == pageSize) {
			LOG.info("缺少分页参数");
			throw new ServiceException(ReturnCode.PAGE_PARAM_BLANK_ERROR);
		}
		
		String name = reqDto.getStringParam("name");
		List<Project> list = projectService.pageList(name, reqDto.getSelectFrom(), pageSize);
		int total = projectService.count(name);
		
		List<ProjectListVO> resList = list.stream().map(ProjectListVO::new).collect(Collectors.toList());
		projectCommandService.setStatus(resList);
		
		return RespBuildUtil.success(resList, page, pageSize, total);
	}
	
	@PostMapping("/{id}/get")
	public ResponseDTO get(@PathVariable("id") String id) {
		Project project = getExistingProject(id);
		
		project.setStatus(projectCommandService.getStatus(project.getId()));
		return RespBuildUtil.success(project);
	}
	
	@PostMapping("/add")
	@ValidateRequired({"name", "gitUrl", "projectSourcePath", "projectDeployPath", "packageName", "deployWay"})
	public ResponseDTO add(@RequestBody RequestDTO reqDto) {
		Project project = JsonUtils.map(JsonUtils.toJson(reqDto.getParams()), Project.class);
		
		String id = UUIDUtil.getId();
		project.setId(id);
		project.setCreateAt(LocalDateTime.now());
		projectService.save(project);
		
		projectCommandService.initialize(project);
		return RespBuildUtil.success();
	}

	@PostMapping("/{id}/update")
	public ResponseDTO update(@RequestBody RequestDTO reqDto, @PathVariable("id") String id) {
		Project project = getExistingProject(id);
		
		if (reqDto.hasParam("name")) project.setName(reqDto.getStringParam("name"));
		if (reqDto.hasParam("description")) project.setDescription(reqDto.getStringParam("description"));
		if (reqDto.hasParam("gitUrl")) project.setGitUrl(reqDto.getStringParam("gitUrl"));
		if (reqDto.hasParam("branch")) project.setBranch(reqDto.getStringParam("branch"));
		if (reqDto.hasParam("projectSourcePath")) project.setProjectSourcePath(reqDto.getStringParam("projectSourcePath"));
		if (reqDto.hasParam("projectDeployPath")) project.setProjectDeployPath(reqDto.getStringParam("projectDeployPath"));
		if (reqDto.hasParam("packageName")) project.setPackageName(reqDto.getStringParam("packageName"));
		if (reqDto.hasParam("deploySubModule")) project.setDeploySubModule(reqDto.getStringParam("deploySubModule"));
		if (reqDto.hasParam("deployWay")) project.setDeployWay(reqDto.getIntegerParam("deployWay"));
		if (reqDto.hasParam("customStart")) project.setCustomStart(reqDto.getStringParam("customStart"));
		if (reqDto.hasParam("webContainerId")) project.setWebContainerId(reqDto.getStringParam("webContainerId"));
		projectService.update(project);
		
		projectCommandService.initialize(project);
		return RespBuildUtil.success();
	}
	
	@PostMapping("/{id}/delete")
	public ResponseDTO delete(@PathVariable("id") String id) {
		if (StringUtils.isBlank(id)) {
			throw new ServiceException(ReturnCode.REQUIRED_BLANK_ERROR);
		}
		
		Project project = projectService.getById(id);
		projectService.delete(id);
		projectCommandService.uninitialize(project);
		return RespBuildUtil.success();
	}
	
	/**
	 * 初始化项目
	 * @param id
	 * @return
	 */
	@PostMapping("/{id}/initialize")
	public ResponseDTO initialize(@PathVariable("id") String id) {
		Project project = getExistingProject(id);
		
		projectCommandService.initialize(project);
		return RespBuildUtil.success();
	}
	
	/**
	 * 拉取项目
	 * @param id
	 * @param request
	 * @return
	 */
	@PostMapping("/{id}/gitpull")
	public ResponseDTO gitpull(@PathVariable("id") String id, HttpServletRequest request) {
		Project project = getExistingProject(id);
		
		String username = (String) request.getSession().getAttribute(Constants.SESSION_LOGIN_USER);
		
		projectCommandService.gitpull(project, username);
		return RespBuildUtil.success();
	}
	
	/**
	 * 项目切换分支
	 * @param id
	 * @param request
	 * @return
	 */
	@PostMapping("/{id}/checkout")
	@ValidateRequired({"branch"})
	public ResponseDTO checkout(@RequestBody RequestDTO reqDto, @PathVariable("id") String id, HttpServletRequest request) {
		Project project = getExistingProject(id);
		
		String username = (String) request.getSession().getAttribute(Constants.SESSION_LOGIN_USER);
		
		String branch = reqDto.getStringParam("branch");
		project.setBranch(branch);
		projectService.update(project);
		projectCommandService.initialize(project);
		
		projectCommandService.checkout(project, username);
		projectCommandService.deploy(project, username);
		return RespBuildUtil.success();
	}
	
	/**
	 * 部署项目
	 * @param id
	 * @param request
	 * @return
	 */
	@PostMapping("/{id}/deploy")
	public ResponseDTO deploy(@PathVariable("id") String id, HttpServletRequest request) {
		Project project = getExistingProject(id);
		
		String username = (String) request.getSession().getAttribute(Constants.SESSION_LOGIN_USER);
		
		projectCommandService.deploy(project, username);
		return RespBuildUtil.success();
	}
	
	/**
	 * 部署项目
	 * @param id
	 * @param request
	 * @return
	 */
	@PostMapping("/{id}/startup")
	public ResponseDTO startup(@PathVariable("id") String id, HttpServletRequest request) {
		Project project = getExistingProject(id);
		
		String username = (String) request.getSession().getAttribute(Constants.SESSION_LOGIN_USER);
		
		projectCommandService.startup(project, username);
		return RespBuildUtil.success();
	}
	
	/**
	 * 部署项目
	 * @param id
	 * @param request
	 * @return
	 */
	@PostMapping("/{id}/shutdown")
	public ResponseDTO shutdown(@PathVariable("id") String id, HttpServletRequest request) {
		Project project = getExistingProject(id);
		
		String username = (String) request.getSession().getAttribute(Constants.SESSION_LOGIN_USER);
		
		projectCommandService.shutdown(project, username);
		return RespBuildUtil.success();
	}
	
	private Project getExistingProject(String id) {
		if (StringUtils.isBlank(id)) {
			throw new ServiceException(ReturnCode.REQUIRED_BLANK_ERROR);
		}
		
		Project project = projectService.getById(id);
		if (null == project) {
			throw new ServiceException(ReturnCode.OBJECT_NOT_FOUND);
		}
		return project;
	}
}
