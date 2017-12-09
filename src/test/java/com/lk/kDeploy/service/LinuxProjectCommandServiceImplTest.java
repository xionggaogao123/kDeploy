package com.lk.kDeploy.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.lk.kDeploy.util.UUIDUtil;

public class LinuxProjectCommandServiceImplTest {

	@Test
	public void test() {
		String echo = "dev      22690     1  0 15:49 ?        00:00:43 java -jar a2d44bbf_8c80_4dcf_8a1c_7441753f9f67-quickpay-app-start-1.0.0-SNAPSHOT.jar --spring.config.location=application.yml";
		
		String[] split = echo.split("\n");
		for (String processStr : split) {
			if (StringUtils.isNotBlank(processStr)) {
				System.out.println(processStr);
				System.out.println(getPackageName(processStr));
				System.out.println();
			}
		}
	}
	private String getPackageName(String processStr) {
		Pattern pattern = Pattern.compile("\\s([a-z0-9_]+)-\\S+(\\.jar|\\.war)\\s");
		Matcher matcher = pattern.matcher(processStr);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
	
	@Test
	public void pattern() {
		String str = "dev      22690     1  0 15:49 ?        00:00:43 java -jar quickpay-app-start-1.0.0-SNAPSHOT.jar --spring.config.location=application.yml";
		Pattern pattern = Pattern.compile("\\s([a-z0-9_]+)-\\S+(\\.jar|\\.war)\\s");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			System.out.println(matcher.group(1));
		}
	}
	
	@Test
	public void uuidLength() {
		int i = 0;
		while (i++ < 50) {
			String id = UUIDUtil.getId();
			System.out.println(id.length());
		}
	}

}
