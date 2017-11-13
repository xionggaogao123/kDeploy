package com.lk.kDeploy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import com.lk.kDeploy.websocket.WebSocketServer;

/**
 * 监听容器关闭事件
 *
 * @author: lk
 * @since: 2017年11月13日
 */
@Component
public class ApplicationShutdown implements ApplicationListener<ContextClosedEvent> {
	protected static Logger LOGGER = LoggerFactory.getLogger(ApplicationShutdown.class);

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		WebSocketServer.shutdown();
	}
}
