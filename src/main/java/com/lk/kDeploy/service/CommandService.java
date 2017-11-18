package com.lk.kDeploy.service;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lk.kDeploy.ConsoleOutputStream;
import com.lk.kDeploy.controller.TestController;

/**
 * 
 *
 * @author: lk
 * @since: 2017年11月18日
 */
public class CommandService {
	private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

	@Test
	public void testt() throws ExecuteException, IOException, InterruptedException {
		execute("123", "ping www.baidu.com");
	}
	
	public void execute(String username, String command) throws ExecuteException, IOException, InterruptedException {
		
		final ExecuteWatchdog watchdog = new ExecuteWatchdog(Integer.MAX_VALUE);
		final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		final CommandLine cmdLine = CommandLine.parse(command);
		
		DefaultExecutor executor = new DefaultExecutor();
		executor.setWatchdog(watchdog);
		executor.setExitValues(null);
		
		OutputStream outputStream = new ConsoleOutputStream();
		executor.setStreamHandler(new PumpStreamHandler(outputStream, outputStream));
		
		LOG.info("start...");
		executor.execute(cmdLine, resultHandler);
		
		while (!resultHandler.hasResult()) {
			LOG.info("---还没好");
			Thread.sleep(800);
		}
		
		LOG.info("--> Watchdog is watching ? " + watchdog.isWatching());
		LOG.info("--> Watchdog should have killed the process : " + watchdog.killedProcess());
		LOG.info("--> wait result is : " + resultHandler.hasResult());
		LOG.info("--> exit value is : " + resultHandler.getExitValue());
		LOG.info("--> exception is : " + resultHandler.getException());
		
		LOG.info("end");
	}
}
