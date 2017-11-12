package com.lk.kDeploy.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

/**
 * WebSocket服务
 *
 * @author: lk
 * @since: 2017年11月10日
 */
public class WebSocketServer {
	private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);

	private static WebSocketServer myServer = null;
	
	private static SocketIOServer socketIOServer = null;

	private WebSocketServer() {}
	
	public static WebSocketServer start(String listenHostName, int port) {
		if (null != myServer) {
			return myServer;
		}
		myServer = new WebSocketServer();
		log.info("开启WebSocket服务...");

		Configuration config = new Configuration();
		config.setHostname(listenHostName);
		config.setPort(port);

		socketIOServer = new SocketIOServer(config);
		socketIOServer.start();
		
		log.info("WebSocket服务启动成功。监听地址: {}:{}", listenHostName, port);
		
		return myServer;
	}

	public static void shutdown() {
		log.info("关闭WebSocket服务");
		socketIOServer.stop();
	}
	
	public void addEventListener() {
//		socketIOServer.addEventListener("connectin", Map.class, new DataListener<Map<>() {
//
//			@Override
//			public void onData(final SocketIOClient client, Map data, final AckRequest ackRequest) {
//				log.info("connectin in");
//			}
//		});
	}
}
