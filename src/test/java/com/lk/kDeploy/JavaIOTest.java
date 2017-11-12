package com.lk.kDeploy;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaIOTest {
	protected static Logger LOG = LoggerFactory.getLogger(JavaIOTest.class);

	@Test
	public void inputOutput() throws IOException {
		ByteArrayOutputStream bOutput = new ByteArrayOutputStream(12);
		while (bOutput.size() != 10) {
			// 获取用户输入
			bOutput.write(System.in.read());
		}
		byte b[] = bOutput.toByteArray();
		System.out.println("Print the content");
		for (int x = 0; x < b.length; x++) {
			// 打印字符
			System.out.print((char) b[x] + "   ");
		}
		System.out.println("   ");
		int c;
		ByteArrayInputStream bInput = new ByteArrayInputStream(b);
		System.out.println("Converting characters to Upper case ");
		for (int y = 0; y < 1; y++) {
			while ((c = bInput.read()) != -1) {
				System.out.println(Character.toUpperCase((char) c));
			}
			bInput.reset();
		}
	}
	
	@Test
	public void inputOutput2() throws IOException {
	    // 使用 System.in 创建 BufferedReader 
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    System.out.println("Enter lines of text.");
	    System.out.println("Enter 'end' to quit.");
	    // 读取字符
	    @SuppressWarnings("resource")
		OutputStream outputStream = new ConsoleOutputStream();
	    
	    int read;
	    do {
	    	read = br.read();
			outputStream.write(read);
	    } while(read != 'q');
	}
	
	@Test
	public void charToString() {
		System.out.println((char) 80);
	}
}
