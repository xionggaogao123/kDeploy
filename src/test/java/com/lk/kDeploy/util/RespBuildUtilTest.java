package com.lk.kDeploy.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RespBuildUtilTest {

	@Test
	public void test() {
		System.out.println(JsonUtils.toJson(RespBuildUtil.success()));
	}

}
