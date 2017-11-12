package com.lk.kDeploy.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lk.kDeploy.base.annotion.AnonymousAccess;
import com.lk.kDeploy.base.dto.RequestDTO;
import com.lk.kDeploy.base.dto.ResponceDTO;
import com.lk.kDeploy.config.KdeployConfig;
import com.lk.kDeploy.constants.Constants;
import com.lk.kDeploy.constants.ReturnCode;
import com.lk.kDeploy.util.RespBuildUtil;

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
	private KdeployConfig kdeployConfig;
	
	@PostMapping("/login")
	@AnonymousAccess
	public ResponceDTO login(@RequestBody RequestDTO reqDto, HttpServletRequest request) {
		String username = reqDto.getStringParam("username");
		String password = reqDto.getStringParam("password");
		
		String realPassword = kdeployConfig.getPassword();
		if (!kdeployConfig.getUsername().equals(username) || !DigestUtils.sha512Hex(realPassword).equals(password)) {
			return RespBuildUtil.error(ReturnCode.USERNAME_OR_PASSWORD_ERROR);
		}
		
		request.getSession().setAttribute(Constants.SESSION_LOGIN_USER, username);
		return RespBuildUtil.success();
	}
	
	@PostMapping("/logout")
	@AnonymousAccess
	public ResponceDTO logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return RespBuildUtil.success();
	}
	
}
