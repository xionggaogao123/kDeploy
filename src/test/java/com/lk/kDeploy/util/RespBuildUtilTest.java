package com.lk.kDeploy.util;

import org.apache.commons.codec.digest.DigestUtils;
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
	
	@Test
	public void sha512() {
		System.out.println(DigestUtils.sha512Hex("123123"));
		//263fec58861449aacc1c328a4aff64aff4c62df4a2d50b3f207fa89b6e242c9aa778e7a8baeffef85b6ca6d2e7dc16ff0a760d59c13c238f6bcdc32f8ce9cc62
	}

}
