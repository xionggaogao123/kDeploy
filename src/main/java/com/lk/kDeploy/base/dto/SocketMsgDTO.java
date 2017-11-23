package com.lk.kDeploy.base.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * socket消息
 *
 * @author: lk
 * @since: 2017年11月12日
 */
public class SocketMsgDTO {

	private String type;
	private Map<String, Object> params;
	
	public void putParam(String key, Object value) {
		if (null == params) {
			params = new HashMap<>();
		}
		params.put(key, value);
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
