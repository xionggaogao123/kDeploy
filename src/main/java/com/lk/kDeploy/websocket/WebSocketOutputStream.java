package com.lk.kDeploy.websocket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lk.kDeploy.base.dto.SocketMsgDTO;
import com.lk.kDeploy.constants.Constants;

public class WebSocketOutputStream extends OutputStream {
	private static Logger LOG = LoggerFactory.getLogger(WebSocketOutputStream.class);

	private StringBuilder stringBuilder = new StringBuilder();
	
	private String username;
	private SocketMsgDTO socketMsg;
	
	public WebSocketOutputStream(String username, String projectId) {
		this.username = username;
		
		Map<String, Object> params = new HashMap<>();
    	params.put("id", projectId);
    	
    	this.socketMsg = new SocketMsgDTO();
		socketMsg.setParams(params);
	}
	
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
		LOG.info("UTF-8: {}", new String(stringBuilder.toString().getBytes(), "UTF-8"));
		LOG.info("GBK: {}", new String(stringBuilder.toString().getBytes(), "GBK"));
		LOG.info("ISO-8859-1: {}", new String(stringBuilder.toString().getBytes(), "ISO-8859-1"));
		
		String log = new String(stringBuilder.toString().getBytes(), "UTF-8");
    	stringBuilder.setLength(0);
    	
    	this.socketMsg.getParams().put("log", log);
		WebSocketServer.pushMsg(Constants.SOCKET_EVENT_COMMAND_ECHO, this.username, this.socketMsg);
	}

}
