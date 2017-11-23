package com.lk.kDeploy.websocket;

import java.io.IOException;
import java.io.PipedOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyPipedOutputStream extends PipedOutputStream {
	private static Logger LOG = LoggerFactory.getLogger(MyPipedOutputStream.class);

	public void write(byte b[], int off, int len) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		for (byte c : b) {
			stringBuilder.append((char) c);
		}
        	
    	LOG.info("回显：{}", stringBuilder.toString());
		super.write(b, off, len);
    }
}
