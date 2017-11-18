package com.lk.kDeploy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApacheExecTest {
	protected static Logger LOG = LoggerFactory.getLogger(ApacheExecTest.class);

	@Test
	public void testCRUD() throws ExecuteException, IOException {
//		LOG.info(exec("cmd /c echo hello world! let us start... & d: & cd java/workspace-sts-3.8.3.RELEASE/springStart & dir & mvn clean install -U -Dmaven.test.skip=true"));
		LOG.info(exec("ping www.baidu.com"));
	}
	
	public static String exec(String cmd) throws IOException {
		CommandLine cmdLine = CommandLine.parse(cmd);
		
		DefaultExecutor executor = new DefaultExecutor();
		// 防止抛出异常
		executor.setExitValues(null);

		// 命令执行的超时时间
		ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 1000);
		executor.setWatchdog(watchdog);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
		executor.setStreamHandler(streamHandler);

		executor.execute(cmdLine);
		
		return outputStream.toString();
	}
	
	@Test
	public void testCmd() throws ExecuteException, IOException, InterruptedException {
		final CommandLine cmdLine = CommandLine.parse("ping www.baidu.com -t");
		final ExecuteWatchdog watchdog = new ExecuteWatchdog(Integer.MAX_VALUE);
		final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();

		executor.setWatchdog(watchdog);
		executor.execute(cmdLine, resultHandler);
		
		Thread.sleep(3000);//等进程执行一会，再终止它
		LOG.info("--> Watchdog is watching ? " + watchdog.isWatching());
		watchdog.destroyProcess();//终止进程
		LOG.info("--> destroyProcess done.");
		LOG.info("--> Watchdog is watching ? " + watchdog.isWatching());
		LOG.info("--> Watchdog should have killed the process : " + watchdog.killedProcess());
		LOG.info("--> wait result is : " + resultHandler.hasResult());
		
		Thread.sleep(600); // 等待进程完全停止
		LOG.info("--> exit value is : " + resultHandler.getExitValue());
		LOG.info("--> exception is : " + resultHandler.getException());

		LOG.info("\n3 second =================");
		
		resultHandler.waitFor(3000);//等待3秒。下面加上上面的几个System.out，看看进程状态是什么。
		LOG.info("--> Watchdog is watching ? " + watchdog.isWatching());
		LOG.info("--> destroyProcess done.");
		LOG.info("--> Watchdog is watching ? " + watchdog.isWatching());
		LOG.info("--> Watchdog should have killed the process : " + watchdog.killedProcess());
		LOG.info("--> wait result is : " + resultHandler.hasResult());
		LOG.info("--> exit value is : " + resultHandler.getExitValue());
		LOG.info("--> exception is : " + resultHandler.getException());
	}
	
	@Test
	public void testCmd2() throws ExecuteException, IOException, InterruptedException {
		String cmdStr = "ping www.baidu.com -t";
		final CommandLine cmdLine = CommandLine.parse(cmdStr);
		DefaultExecutor executor = new DefaultExecutor();
		
		LOG.info("start...");
		int exitValue = executor.execute(cmdLine);
		LOG.info("end");
		
		
		LOG.info("" + exitValue);
	}
	
	@Test
	public void testCmd3() throws ExecuteException, IOException, InterruptedException {
		final ExecuteWatchdog watchdog = new ExecuteWatchdog(4000);

		String cmdStr = "ping www.baidu.com -t";
		final CommandLine cmdLine = CommandLine.parse(cmdStr);
		DefaultExecutor executor = new DefaultExecutor();
		executor.setWatchdog(watchdog);
		executor.setExitValues(null);
		
		LOG.info("start...");
		int exitValue = executor.execute(cmdLine);
		LOG.info("end");
		
		
		LOG.info("" + exitValue);
	}
	
	@Test
	public void testCmd4() throws ExecuteException, IOException, InterruptedException {
		final ExecuteWatchdog watchdog = new ExecuteWatchdog(Integer.MAX_VALUE);
		final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		
		String cmdStr = "ping www.baidu.com -t";
		final CommandLine cmdLine = CommandLine.parse(cmdStr);
		DefaultExecutor executor = new DefaultExecutor();
		executor.setWatchdog(watchdog);
		executor.setExitValues(null);
		
		LOG.info("start...");
		executor.execute(cmdLine, resultHandler);
		
		Thread.sleep(1000 * 5);
		LOG.info("wait...");
//		resultHandler.waitFor(1000 * 5);
		resultHandler.wait(1000 * 5);
		LOG.info("wait end...");
		
		Thread.sleep(1000 * 20);
		LOG.info("end");
	}
	
	@Test
	public void testCmd5() throws ExecuteException, IOException, InterruptedException {
		final ExecuteWatchdog watchdog = new ExecuteWatchdog(Integer.MAX_VALUE);
		final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		
		String cmdStr = "ping www.baidu.com -t";
		final CommandLine cmdLine = CommandLine.parse(cmdStr);
		DefaultExecutor executor = new DefaultExecutor();
		executor.setWatchdog(watchdog);
		executor.setExitValues(null);
		
		OutputStream outputStream = new ConsoleOutputStream();
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		executor.setStreamHandler(new PumpStreamHandler(outputStream, outputStream));
		
		LOG.info("start...");
		executor.execute(cmdLine, resultHandler);
		
//		Thread.sleep(1000 * 10);
		LOG.info("end");
	}
	
	/**
	 * 无限循环判断命令执行完毕
	 * @throws ExecuteException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testCmd6() throws ExecuteException, IOException, InterruptedException {
		final CommandLine cmdLine = CommandLine.parse("ping www.baidu.com");
		final ExecuteWatchdog watchdog = new ExecuteWatchdog(Integer.MAX_VALUE);
		final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();

		executor.setWatchdog(watchdog);
		executor.execute(cmdLine, resultHandler);
		
		while (!resultHandler.hasResult()) {
			LOG.info("嘿");
			Thread.sleep(800);
		}
		
		LOG.info("--> Watchdog is watching ? " + watchdog.isWatching());
		LOG.info("--> Watchdog should have killed the process : " + watchdog.killedProcess());
		LOG.info("--> wait result is : " + resultHandler.hasResult());
		LOG.info("--> exit value is : " + resultHandler.getExitValue());
		LOG.info("--> exception is : " + resultHandler.getException());
	}
	
}
