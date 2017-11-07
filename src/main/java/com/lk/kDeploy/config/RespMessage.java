package com.lk.kDeploy.config;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 
 *
 * @author: lk
 * @since: 2017年10月1日
 */
@Configuration
@ConfigurationProperties
@PropertySource(value = "classpath:resp_message.properties", ignoreResourceNotFound = true)
public class RespMessage {
	public Map<String, String> respMsg;

	public Map<String, String> getrespMsg() {
		return respMsg;
	}

	public void setrespMsg(Map<String, String> map) {
		this.respMsg = map;
	}

	public String get(int key) {
		if (!respMsg.containsKey(key + "")) return "";
		try {
			return new String(respMsg.get(key + "").getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
}
