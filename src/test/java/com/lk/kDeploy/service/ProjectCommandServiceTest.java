package com.lk.kDeploy.service;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.lk.kDeploy.util.UUIDUtil;

public class ProjectCommandServiceTest {

	@Test
	public void test() {
		String echo = "dev      19112     1  0 Nov27 ?        00:01:27 java -jar smart-repayment-start-1.0.0-Fianl.jar --spring.config.location=application.yml\ndev      19564     1  0 Nov27 ?        00:01:08 java -jar villton-push-1.0.0-SNAPSHOT.jar --spring.config.location=application.yml\ndev      22436     1  0 15:18 ?        00:00:40 java -jar quickpay-starter-1.0.0-Fianl.jar --spring.config.location=application.yml\ndev      22690     1  0 15:49 ?        00:00:43 java -jar quickpay-app-start-1.0.0-SNAPSHOT.jar --spring.config.location=application.yml\ndev      24126 24104  0 22:04 pts/3    00:00:00 grep --color=auto java\ndev      24879     1  0 Nov15 ?        00:13:17 java -jar pay-gateway-starter-1.0.0-Fianl.jar --spring.config.location=application.yml\n";
		
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
