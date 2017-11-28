package com.lk.kDeploy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.function.Consumer;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lk.kDeploy.constants.ReturnCode;
import com.lk.kDeploy.exception.ServiceException;

/**
 * 
 *
 * @author: lk
 * @since: 2017年11月28日
 */
public class ExecutorUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorUtil.class);

	/**
	 * 执行命令
	 * @param command 命令
	 * @param timeout 执行超时时间
	 * @param charset 回显字符串编码
	 * @return 返回回显字符串
	 */
	public static String exec(String command, long timeout, String charset) {
		LOGGER.info("执行命令。command： {}", command);
		
		Executor executor = getDefaultExecutor(timeout);
		
		final CommandLine cmdLine = CommandLine.parse(command);

		PipedOutputStream outputStream = new PipedOutputStream();
		try (PipedInputStream pis = new PipedInputStream(outputStream)) {
			
			executor.setStreamHandler(new PumpStreamHandler(outputStream, outputStream));
			executor.execute(cmdLine);
			
			return IOUtils.toString(pis, charset);
		} catch (IOException e) {
			LOGGER.error("执行命令报错", e);
			throw new ServiceException(ReturnCode.EXECUTE_COMMAND_ERROR);
		}
	}
	
	/**
	 * 执行命令
	 * @param command 命令
	 * @param timeout 执行超时时间
	 * @param charset 回显字符串编码
	 * @param echoConsumer 处理一行回显的方法
	 */
	public static void exec(String command, long timeout, String charset, Consumer<String> echoConsumer) {
		LOGGER.info("执行命令。command： {}", command);
		
		Executor executor = getDefaultExecutor(timeout);
		
		final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		final CommandLine cmdLine = CommandLine.parse(command);
		
		PipedOutputStream outputStream = new PipedOutputStream();
		try (PipedInputStream pis = new PipedInputStream(outputStream)) {
			
			executor.setStreamHandler(new PumpStreamHandler(outputStream, outputStream));
			
			InputStreamReader inStreamReader = new InputStreamReader(pis, charset);
			BufferedReader br = new BufferedReader(inStreamReader);
			
			executor.execute(cmdLine, resultHandler);
			
			String line = null;
			while ((line = br.readLine()) != null) {
				echoConsumer.accept(line + "\n");
			}
		} catch (IOException e) {
			LOGGER.error("执行命令报错", e);
			throw new ServiceException(ReturnCode.EXECUTE_COMMAND_ERROR);
		}
		
        try {
        	// 休息一下让命令执行结束
			Thread.sleep(500);
		} catch (InterruptedException e) {
			LOGGER.error("不会发生", e);
		}
		
//		LOG.info("--> Watchdog is watching ? " + watchdog.isWatching());
//		LOG.info("--> Watchdog should have killed the process : " + watchdog.killedProcess());
//		LOG.info("--> wait result is : " + resultHandler.hasResult());
//		LOG.info("--> exit value is : " + resultHandler.getExitValue());
//		LOG.info("--> exception is : " + resultHandler.getException());
		
        LOGGER.info("执行命令结束。command： {}", command);
	}

	private static Executor getDefaultExecutor(long timeout) {
		Executor executor = new DefaultExecutor();
		
		ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
		executor.setWatchdog(watchdog);
		executor.setExitValues(null);
		return executor;
	}
}
