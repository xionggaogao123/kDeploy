package com.lk.kDeploy.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lk.kDeploy.base.dto.SocketMsgDTO;
import com.lk.kDeploy.config.CommonConfig;
import com.lk.kDeploy.constants.Constants;
import com.lk.kDeploy.websocket.WebSocketServer;

/**
 * 
 *
 * @author: lk
 * @since: 2017年11月18日
 */
@Service
public class CommandService {
	private static final Logger LOG = LoggerFactory.getLogger(CommandService.class);

	@Autowired
	private CommonConfig commonConfig;
	
	public void executeAndPushLog(String username, String command) throws ExecuteException, IOException, InterruptedException {
		
		final ExecuteWatchdog watchdog = new ExecuteWatchdog(commonConfig.getCommandTimeout() * 60 * 1000);
		final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		final CommandLine cmdLine = CommandLine.parse(command);
		
		DefaultExecutor executor = new DefaultExecutor();
		executor.setWatchdog(watchdog);
		executor.setExitValues(null);
		
		PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(outputStream);
		
		executor.setStreamHandler(new PumpStreamHandler(outputStream, outputStream));
		
		InputStreamReader inStreamReader = new InputStreamReader(pis, commonConfig.getCommandSystemCharset());
		BufferedReader br = new BufferedReader(inStreamReader);
        
        LOG.info("执行命令。username: {}, command： {}", username, command);
		executor.execute(cmdLine, resultHandler);
        
        String line = null;
        while ((line = br.readLine()) != null || !resultHandler.hasResult()) {
        	push(line + "\n", username);
        }
        pis.close();
        
        Thread.sleep(500); // 休息一下让命令执行结束
		
//		LOG.info("--> Watchdog is watching ? " + watchdog.isWatching());
//		LOG.info("--> Watchdog should have killed the process : " + watchdog.killedProcess());
//		LOG.info("--> wait result is : " + resultHandler.hasResult());
//		LOG.info("--> exit value is : " + resultHandler.getExitValue());
//		LOG.info("--> exception is : " + resultHandler.getException());
		
        LOG.info("执行命令结束。username: {}, command： {}", username, command);
	}

	private void push(String msg, String username) {
		SocketMsgDTO socketMsg = new SocketMsgDTO();
    	socketMsg.putParam("log", msg);
		WebSocketServer.pushMsg(Constants.SOCKET_EVENT_COMMAND_ECHO, username, socketMsg);
	}

}
