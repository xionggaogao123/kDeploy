package com.lk.kDeploy.controller;

import java.io.IOException;

import org.apache.commons.exec.ExecuteException;
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
	
	@PostMapping
	@AnonymousAccess
	@ValidateRequired({"name", "type"})
    public ResponseDTO checkUser(@RequestBody RequestDTO req) {
        LOG.info("收到参数：{}", JsonUtils.toJson(req));
        
        Project project = new Project();
        project.setId("998");
        return RespBuildUtil.success(project);
    }
	
	@GetMapping
	@AnonymousAccess
	public ResponseDTO test() {
		Project project = new Project();
		project.setId("998");
		return RespBuildUtil.success(project);
	}
	
	
	@PostMapping("/ping")
	public ResponseDTO testPing(@RequestBody RequestDTO req) throws ExecuteException, IOException, InterruptedException {
		String command = req.getStringParam("command");
		commandService.executeAndPushLog("admin", command);
		return RespBuildUtil.success();
	}
	
}
