package com.lk.kDeploy.util;

import java.util.UUID;

/**
 * 
 *
 * @author: lk
 * @since: 2017年11月15日
 */
public class UUIDUtil {
	
	public static String getId() {
		return UUID.randomUUID().toString();
	}
	
	public static String getId(String prefix) {
		return prefix + getId();
	}
}
