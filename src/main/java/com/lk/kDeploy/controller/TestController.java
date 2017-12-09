package com.lk.kDeploy.controller;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lk.kDeploy.base.annotion.AnonymousAccess;
import com.lk.kDeploy.base.dto.RequestDTO;
import com.lk.kDeploy.base.dto.ResponseDTO;
import com.lk.kDeploy.constants.Constants;
import com.lk.kDeploy.exception.ServiceException;
import com.lk.kDeploy.service.CommandService;
import com.lk.kDeploy.util.RespBuildUtil;

/**
 * 测试action
 *
 * @author: lk
 * @since: 2017年11月7日
 */
@RestController
@RequestMapping("/api/test")
public class TestController {
	private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private CommandService commandService;
	
	@PostMapping("/executeAndPush")
	public ResponseDTO executeAndPush(@RequestBody RequestDTO req) throws ServiceException {
		String command = req.getStringParam("command");
		commandService.executeAndPushLog("admin", command);
		return RespBuildUtil.success();
	}
	
	@PostMapping("/execute")
	public ResponseDTO execute(@RequestBody RequestDTO req) throws ServiceException {
		String command = req.getStringParam("command");
		String res = commandService.execute(command);
		LOG.info(res);
		
		Map<String, Object> map = new HashMap<>();
		map.put("log", res);
		return RespBuildUtil.success(map);
	}
	
	@AnonymousAccess
	@PostMapping("/resource")
	public ResponseDTO resource(@RequestBody RequestDTO req) throws Exception {
		InputStream stream = getClass().getClassLoader().getResourceAsStream(Constants.SHELL_TMPL_PROJECT_FILE);
		String shellStr = IOUtils.toString(stream, "utf-8");
		String source = shellStr;
		
		shellStr = shellStr.replace("{{id}}", "a2d44bbf_8c80_4dcf_8a1c_7441753f9f67");
		shellStr = shellStr.replace("{{name}}", "springStart");
		shellStr = shellStr.replace("{{gitUrl}}", "git@120.77.209.205:repositories/springStart.git");
		shellStr = shellStr.replace("{{branch}}", "");
		shellStr = shellStr.replace("{{projectSourcePath}}", "/home/dev/kdeployDir/");
		shellStr = shellStr.replace("{{projectDeployPath}}", "/home/dev/kdeployApplDir/");
		shellStr = shellStr.replace("{{packageName}}", "sprintStart-0.0.1-SNAPSHOT.jar");
		shellStr = shellStr.replace("{{deploySubModule}}", "");
		
		Map<String, Object> map = new HashMap<>();
		map.put("shellStr", shellStr);
		map.put("shellStr2", source);
		return RespBuildUtil.success(map);
	}
}
