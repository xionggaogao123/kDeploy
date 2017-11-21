package com.lk.kDeploy;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lk.kDeploy.websocket.WebSocketOutputStream;

public class ConsoleOutputStream extends OutputStream {
	private static Logger LOG = LoggerFactory.getLogger(WebSocketOutputStream.class);

	private StringBuilder stringBuilder = new StringBuilder();
	
	@Override
	public void write(int b) throws IOException {
		
		try {
            synchronized (this) {
            	stringBuilder.append((char) b);
                if (b == '\n' && stringBuilder.length() > 512) {
                	flush();
                }
            }
        } catch (Exception e) {
        	LOG.error("推送命令执行日志消息失败", e);
        }
	}

	@Override
	public void flush() throws UnsupportedEncodingException {
		String echo = stringBuilder.toString();
		stringBuilder.setLength(0);
		
		LOG.info("common: {}", echo);
		LOG.info("UTF-8: {}", new String(echo.getBytes(), "UTF-8"));
		LOG.info("GBK: {}", new String(echo.getBytes(), "GBK"));
		LOG.info("ISO-8859-1: {}", new String(echo.getBytes(), "ISO-8859-1"));
		
		LOG.info("======================================");
		
		LOG.info("GBK -> UTF-8: {}", new String(echo.getBytes("GBK"), "UTF-8"));
		LOG.info("GBK -> GBK: {}", new String(echo.getBytes("GBK"), "GBK"));
		LOG.info("GBK -> ISO-8859-1: {}", new String(echo.getBytes("GBK"), "ISO-8859-1"));
		
		LOG.info("======================================");
		
		LOG.info("ISO -> UTF-8: {}", new String(echo.getBytes("ISO-8859-1"), "UTF-8"));
		LOG.info("ISO -> GBK: {}", new String(echo.getBytes("ISO-8859-1"), "GBK"));
		LOG.info("ISO -> ISO-8859-1: {}", new String(echo.getBytes("ISO-8859-1"), "ISO-8859-1"));
	}
}
