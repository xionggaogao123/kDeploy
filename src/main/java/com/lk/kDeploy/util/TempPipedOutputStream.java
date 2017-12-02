package com.lk.kDeploy.util;

import java.io.IOException;
import java.io.PipedOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TempPipedOutputStream extends PipedOutputStream {
	protected Logger LOG = LoggerFactory.getLogger(TempPipedOutputStream.class);

	@Override
	public void write(int b) throws IOException {
		LOG.info("写入管道: {}", (char) b);
		super.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		LOG.info("批量写入管道: {}", b);
		super.write(b, off, len);
	}

	
}
