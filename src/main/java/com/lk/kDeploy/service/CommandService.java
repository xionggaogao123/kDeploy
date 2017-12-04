package com.lk.kDeploy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lk.kDeploy.base.dto.SocketMsgDTO;
import com.lk.kDeploy.config.CommonConfig;
import com.lk.kDeploy.constants.Constants;
import com.lk.kDeploy.util.ExecutorUtil;
import com.lk.kDeploy.websocket.WebSocketServer;

/**
 * 项目命令业务层
 * linux版本
 *
 * @author: lk
 * @since: 2017年11月28日
 */
@Service
public class CommandService {

	private static long MILLISECONDS_PER_MINUTE = 60 * 1000;
	
	@Autowired
	private CommonConfig commonConfig;
	
	public String execute(String command) {
		long timeout = commonConfig.getCommandTimeout() * MILLISECONDS_PER_MINUTE;
		String charset = commonConfig.getCommandSystemCharset();
		return ExecutorUtil.exec(command, timeout, charset);
	}
	
	public void executeAndPushLog(String username, String command) {
		long timeout = commonConfig.getCommandTimeout() * MILLISECONDS_PER_MINUTE;
		String charset = commonConfig.getCommandSystemCharset();
		
		ExecutorUtil.exec(command, timeout, charset, log -> {
			push(log, username);
		});
	}

	private void push(String msg, String username) {
		SocketMsgDTO socketMsg = new SocketMsgDTO();
    	socketMsg.putParam("log", msg);
		WebSocketServer.pushMsg(Constants.SOCKET_EVENT_COMMAND_ECHO, username, socketMsg);
	}
}
