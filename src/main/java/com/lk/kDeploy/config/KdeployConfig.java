package com.lk.kDeploy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 *
 * @author: lk
 * @since: 2017年11月7日
 */
@Component
public class KdeployConfig {

	@Value("${kdeploy.username}")
	private String username;
	
	@Value("${kdeploy.password}")
	private String password;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
