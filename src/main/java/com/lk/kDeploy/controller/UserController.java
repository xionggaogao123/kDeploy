package com.lk.kDeploy.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
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
import com.lk.kDeploy.config.CommonConfig;
import com.lk.kDeploy.constants.Constants;
import com.lk.kDeploy.constants.ReturnCode;
import com.lk.kDeploy.util.RespBuildUtil;
import com.lk.kDeploy.websocket.WebSocketClientPool;

/**
 * 用户模块接口
 *
 * @author: lk
 * @since: 2017年11月7日
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
	protected static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private CommonConfig commonConfig;
	
	@PostMapping("/login")
	@AnonymousAccess
	@ValidateRequired({"username", "password"})
	public ResponseDTO login(@RequestBody RequestDTO reqDto, HttpServletRequest request) {
		String username = reqDto.getStringParam("username");
		String password = reqDto.getStringParam("password");
		
		String realPassword = commonConfig.getPassword();
		if (!commonConfig.getUsername().equals(username) || !DigestUtils.sha512Hex(realPassword).equals(password)) {
			return RespBuildUtil.error(ReturnCode.USERNAME_OR_PASSWORD_ERROR);
		}
		
		request.getSession().setAttribute(Constants.SESSION_LOGIN_USER, username);
		
//		String token = UUIDUtil.getId(); 开发
		String token = "888";
		WebSocketClientPool.preRegistration(token, username);
		
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("token", token);
		return RespBuildUtil.success(resMap);
	}
	
	@GetMapping("/session")
	public ResponseDTO getSession(HttpServletRequest request) {
		Object username = request.getSession().getAttribute(Constants.SESSION_LOGIN_USER);
		if (null == username) {
			return RespBuildUtil.error(ReturnCode.SESSION_TIMEOUT);
		}
		
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("username", username);
		return RespBuildUtil.success(resMap);
	}
	
	@PostMapping("/logout")
	@AnonymousAccess
	public ResponseDTO logout(HttpServletRequest request) {
		
		String username = (String) request.getSession().getAttribute(Constants.SESSION_LOGIN_USER);
		WebSocketClientPool.unregistration(username );
		
		request.getSession().invalidate();
		return RespBuildUtil.success();
	}
	
}
