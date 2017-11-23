package com.lk.kDeploy.websocket;

import java.io.IOException;
import java.io.PipedOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyPipedOutputStream extends PipedOutputStream {
	private static Logger LOG = LoggerFactory.getLogger(MyPipedOutputStream.class);

	private StringBuilder stringBuilder = new StringBuilder();
	
	@Override
	public void write(int b)  throws IOException {
		stringBuilder.append((char) b);
        if (b == '\n') {
        	
        	LOG.info("回显：{}", stringBuilder.toString());
        	stringBuilder.setLength(0);
        }
        super.write(b);
    }
}
