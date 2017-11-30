package com.lk.kDeploy.websocket;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.SocketIOClient;

/**
 * 
 *
 * @author: lk
 * @since: 2017年11月15日
 */
public final class WebSocketClientPool {
	private static Logger LOG = LoggerFactory.getLogger(WebSocketClientPool.class);

	private static final int TOKEN_TIME_OUT = 10; // 分钟
	private static final int CLIENT_TIME_OUT = 50; // 分钟
	
	/**
	 * 保存预注册token的map</br>
	 * 登录时创建token并预注册，客户端发送socket消息根据token注册客户端</br>
	 * key: token</br>
	 * value: username
	 */
	private static ConcurrentMap<String, String> PRE_REGISTRATION_TOKEN_MAP = new ConcurrentHashMap<>();
	/**
	 * key: token
	 */
	private static ConcurrentMap<String, LocalDateTime> PRE_REGISTRATION_TOKEN_EXPIRE_TIME_MAP = new ConcurrentHashMap<>();
	
	/**
	 * 保存预注册token的map</br>
	 * key: username</br>
	 * value: websocket客户端
	 */
	private static ConcurrentMap<String, SocketIOClient> USER_CLIENT_MAP = new ConcurrentHashMap<>();
	/**
	 * key: username
	 */
	private static ConcurrentMap<String, LocalDateTime> USER_CLIENT_EXPIRE_TIME_MAP = new ConcurrentHashMap<>();
	
	/**
	 * 预注册WebSocket Client
	 * @param token
	 * @param username
	 */
	public static void preRegistration(String token, String username) {
		LOG.info("预注册WebSocket Client。token: {}", token);
		
		synchronized (PRE_REGISTRATION_TOKEN_MAP) {
			PRE_REGISTRATION_TOKEN_MAP.put(token, username);
			PRE_REGISTRATION_TOKEN_EXPIRE_TIME_MAP.put(token, LocalDateTime.now().plusMinutes(TOKEN_TIME_OUT));
		}
	}
	
	/**
	 * 注册WebSocket Client
	 * @param token
	 * @param username
	 */
	public static void registration(String token, SocketIOClient client) {
		LOG.info("注册WebSocket Client。token: {}", token);
		
		synchronized (USER_CLIENT_MAP) {
			LocalDateTime now = LocalDateTime.now();
			cleanExpired(now);
			
			String username = PRE_REGISTRATION_TOKEN_MAP.get(token);
			if (null == username) {
				LOG.info("token过期或没有预注册。token: {}", token);
				return;
			}
			
			USER_CLIENT_MAP.put(username, client);
			USER_CLIENT_EXPIRE_TIME_MAP.put(username, now.plusMinutes(CLIENT_TIME_OUT));
		}
	}

	/**
	 * 注销client
	 * @param username
	 */
	public static void unregistration(String username) {
		LOG.info("注销client。username: {}", username);
		
		USER_CLIENT_MAP.remove(username);
		USER_CLIENT_EXPIRE_TIME_MAP.remove(username);
	}
	
	/**
	 * 获取client
	 * @param username
	 * @return
	 */
	public static SocketIOClient getClient(String username) {
		LocalDateTime now = LocalDateTime.now();
		cleanExpired(now);
		SocketIOClient client = USER_CLIENT_MAP.get(username);
		if (null == client) {
			return null;
		}
		
		USER_CLIENT_EXPIRE_TIME_MAP.put(username, now.plusMinutes(CLIENT_TIME_OUT));
		return client;
	}
	
	private static void cleanExpired(LocalDateTime now) {
		PRE_REGISTRATION_TOKEN_EXPIRE_TIME_MAP.forEach((k, v) -> {
			if (now.isAfter(v)) {
				PRE_REGISTRATION_TOKEN_EXPIRE_TIME_MAP.remove(k);
				PRE_REGISTRATION_TOKEN_MAP.remove(k);
			}
		});
		USER_CLIENT_EXPIRE_TIME_MAP.forEach((k, v) -> {
			if (now.isAfter(v)) {
				USER_CLIENT_EXPIRE_TIME_MAP.remove(k);
				USER_CLIENT_MAP.remove(k);
			}
		});
	}
}
