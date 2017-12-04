package com.lk.kDeploy;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.Charset;
import java.util.function.Consumer;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lk.kDeploy.constants.ReturnCode;
import com.lk.kDeploy.exception.ServiceException;
import com.lk.kDeploy.util.ExecutorUtil;

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
	
	/**
	 * 输入
	 * @throws ExecuteException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testCmdInput() throws ExecuteException, IOException, InterruptedException {
		exec("C:/Program Files/MySQL/MySQL Server 5.7/bin/mysql.exe -u root -p", 60000, "GBK", System.out::println);
	}
	public void exec(String command, long timeout, String charset, Consumer<String> echoConsumer) {
		LOG.info("执行命令。command: {}", command);
		
		Executor executor = getDefaultExecutor(timeout);
		
		final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		final CommandLine cmdLine = CommandLine.parse(command);
		
		try (PipedOutputStream outputStream = new PipedOutputStream()) {
			try (PipedInputStream pis = new PipedInputStream(outputStream, 2048)) {
				
				
				String input = "111222\r\n";
				executor.setStreamHandler(new PumpStreamHandler(outputStream, outputStream, new ByteArrayInputStream(input .getBytes())));
				
				InputStreamReader inStreamReader = new InputStreamReader(pis, charset);
				BufferedReader br = new BufferedReader(inStreamReader);
				
				executor.execute(cmdLine, resultHandler);
				
				String line = null;
				while ((line = br.readLine()) != null) {
					LOG.debug("回显: {}", line);
					echoConsumer.accept(line + "\n");
				}
			} catch (IOException e) {
				throw e;
			}
		} catch (IOException e) {
			LOG.error("执行命令报错", e);
			throw new ServiceException(ReturnCode.EXECUTE_COMMAND_ERROR);
		}
		
        try {
        	// 休息一下让命令执行结束
			Thread.sleep(500);
		} catch (InterruptedException e) {
			LOG.error("不会发生", e);
		}
		
//		LOG.info("--> Watchdog is watching ? " + watchdog.isWatching());
//		LOG.info("--> Watchdog should have killed the process : " + watchdog.killedProcess());
//		LOG.info("--> wait result is : " + resultHandler.hasResult());
//		LOG.info("--> exit value is : " + resultHandler.getExitValue());
//		LOG.info("--> exception is : " + resultHandler.getException());
		
        LOG.info("执行命令结束。command: {}", command);
	}
	private static Executor getDefaultExecutor(long timeout) {
		Executor executor = new DefaultExecutor();
		
		ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
		executor.setWatchdog(watchdog);
		executor.setExitValues(null);
		return executor;
	}
	
}
