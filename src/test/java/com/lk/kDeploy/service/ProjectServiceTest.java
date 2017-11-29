package com.lk.kDeploy.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ProjectServiceTest {

	@Test
	public void forceMkDir() throws IOException {
		FileUtils.forceMkdir(new File("C:/Users/ThinkPad/Desktop/abc/bcd/lk"));
	}

}
