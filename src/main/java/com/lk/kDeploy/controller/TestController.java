package com.lk.kDeploy.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lk.kDeploy.base.dto.RequestDTO;
import com.lk.kDeploy.base.dto.ResponseDTO;
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
	
	@PostMapping("/execute2")
	public ResponseDTO execute2(@RequestBody RequestDTO req) throws ServiceException, IOException {
		String command = req.getStringParam("command");
		String res = exec(command);
		LOG.info(res);
		
		Map<String, Object> map = new HashMap<>();
		map.put("log", res);
		return RespBuildUtil.success(map);
	}
	public String exec(String cmd) throws IOException {
		CommandLine cmdLine = CommandLine.parse(cmd);
		
		DefaultExecutor executor = new DefaultExecutor();
		// 防止抛出异常
		executor.setExitValues(null);

		// 命令执行的超时时间
		ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 1000);
		executor.setWatchdog(watchdog);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
		executor.setStreamHandler(streamHandler);

		executor.execute(cmdLine);
		
		return outputStream.toString();
	}
	
}
