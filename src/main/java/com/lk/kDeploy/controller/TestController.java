package com.lk.kDeploy.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lk.kDeploy.base.annotion.AnonymousAccess;
import com.lk.kDeploy.base.annotion.ValidateRequired;
import com.lk.kDeploy.base.dto.RequestDTO;
import com.lk.kDeploy.base.dto.ResponseDTO;
import com.lk.kDeploy.entity.Project;
import com.lk.kDeploy.exception.ServiceException;
import com.lk.kDeploy.service.CommandService;
import com.lk.kDeploy.util.JsonUtils;
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
		String res = commandService.execute("cmd /c dir");
		LOG.info(res);
		
		Map<String, Object> map = new HashMap<>();
		map.put("log", res);
		return RespBuildUtil.success();
	}
	
}
