package com.lk.kDeploy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.lk.kDeploy.websocket.WebSocketServer;

/**
 * 监听容器初始化/刷新事件
 *
 * @author: lk
 * @since: 2017年11月13日
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	protected static Logger LOGGER = LoggerFactory.getLogger(ApplicationStartup.class);

	@Autowired
	private CommonConfig commonConfig;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		String listenHostName = commonConfig.getWebsocketHost();
		int port = commonConfig.getWebsocketPort();
		WebSocketServer.start(listenHostName, port);
	}
}
