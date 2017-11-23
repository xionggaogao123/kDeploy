package com.lk.kDeploy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 通用基础配置
 *
 * @author: lk
 * @since: 2017年11月7日
 */
@Component
public class CommonConfig {

	@Value("${kdeploy.username}")
	private String username;
	
	@Value("${kdeploy.password}")
	private String password;
	
	@Value("${kdeploy.websocket.host}")
	private String websocketHost;
	
	@Value("${kdeploy.websocket.port}")
	private Integer websocketPort;
	
	@Value("${kdeploy.command.system-charset}")
	private String commandSystemCharset;
	
	@Value("${kdeploy.command.timeout}")
	private Integer commandTimeout;

	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getWebsocketHost() {
		return websocketHost;
	}
	public Integer getWebsocketPort() {
		return websocketPort;
	}
	public String getCommandSystemCharset() {
		return commandSystemCharset;
	}
	public long getCommandTimeout() {
		return commandTimeout;
	}
}
