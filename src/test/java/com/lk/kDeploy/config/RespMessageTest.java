package com.lk.kDeploy.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RespMessageTest {

	@Autowired
	private RespMessage respMessage;
	
	@Test
	public void test() {
		System.out.println(respMessage.get(100));
	}

}
