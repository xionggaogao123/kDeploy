package com.lk.kDeploy;

import java.io.IOException;
import java.io.OutputStream;

public class ConsoleOutputStream extends OutputStream {

	private StringBuilder stringBuilder = new StringBuilder();
	
	@Override
	public void write(int b) throws IOException {
		
		try {
            synchronized (this) {
            	stringBuilder.append((char) b);
                if ((b == '\n')) {
                	System.out.println(stringBuilder.toString());
                	stringBuilder.setLength(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
