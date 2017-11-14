package com.lk.kDeploy.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lk.kDeploy.base.annotion.AnonymousAccess;
import com.lk.kDeploy.base.dto.RequestDTO;
import com.lk.kDeploy.base.dto.ResponseDTO;
import com.lk.kDeploy.config.CommonConfig;
import com.lk.kDeploy.util.RespBuildUtil;

/**
 * 基础模块接口
 *
 * @author: lk
 * @since: 2017年11月14日
 */
@RestController
@RequestMapping("/api/common")
public class CommonController {
	protected static final Logger LOG = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private CommonConfig commonConfig;
	
	@PostMapping("/getConfig")
	@AnonymousAccess
	public ResponseDTO login(@RequestBody RequestDTO reqDto, HttpServletRequest request) {
		Integer websocketPort = commonConfig.getWebsocketPort();
		
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("websocketPort", websocketPort);
		return RespBuildUtil.success(resMap);
	}
	
}
