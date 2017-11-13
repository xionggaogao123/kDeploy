package com.lk.kDeploy.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.lk.kDeploy.base.dto.SocketMsgDTO;
import com.lk.kDeploy.util.JsonUtils;

/**
 * WebSocket服务
 *
 * @author: lk
 * @since: 2017年11月10日
 */
public class WebSocketServer {
	private static Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

	private static WebSocketServer myServer = null;
	
	private static SocketIOServer socketIOServer = null;

	private WebSocketServer() {}
	
	public static WebSocketServer start(String listenHostName, int port) {
		if (null != myServer) {
			return myServer;
		}
		myServer = new WebSocketServer();
		LOGGER.info("开启WebSocket服务...");

		Configuration config = new Configuration();
		config.setHostname(listenHostName);
		config.setPort(port);

		socketIOServer = new SocketIOServer(config);
		socketIOServer.start();
		
		LOGGER.info("WebSocket服务启动成功。监听地址: {}:{}", listenHostName, port);
		
		initEventListener();
		return myServer;
	}

	public static void shutdown() {
		LOGGER.info("关闭WebSocket服务");
		socketIOServer.stop();
	}
	
	/**
	 * 增加WebSocket监听事件
	 * @param eventName
	 * @param eventAction
	 */
	public static void addEventListener(String eventName, EventAction eventAction) {
		socketIOServer.addEventListener(eventName, SocketMsgDTO.class, (SocketIOClient client, SocketMsgDTO socketMsg, AckRequest ackRequest) -> eventAction.onData(client, socketMsg));
	}
	
	public static void pushMsg(String eventName, SocketIOClient client, SocketMsgDTO socketMsg) {
		client.sendEvent(eventName, new AckCallback<String>(String.class) {
			@Override
			public void onSuccess(String result) {
				LOGGER.info("消息发送成功。消息: {}, result: {}", JsonUtils.toJson(socketMsg), result);
			}
		}, socketMsg);
	}
	
	private static void initEventListener() {
		WebSocketServer.addEventListener("regist", (client, socketMsg) -> {
			LOGGER.info("socketMsg: {}", JsonUtils.toJson(socketMsg));
			
			new Thread(() -> {
				try {
					Thread.sleep(1000 * 3);
					
					WebSocketServer.pushMsg("chat", client, socketMsg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		});
	}
}
