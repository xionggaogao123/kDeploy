package com.lk.kDeploy.base.dto;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 请求参数
 *
 * @author: lk
 * @since: 2017年10月1日
 */
public class RequestDTO {

	// 第几页
	private Integer page;

	// 每页显示多少行
	private Integer pageSize;

	// 查询的参数
	private Map<String, Object> params;

	@JSONField(serialize = false)
	public String getStringParam(String key) {
		return getParam(key, String.class);
	}
	
	@JSONField(serialize = false)
	public Integer getIntegerParam(String key) {
		return getParam(key, Integer.class);
	}
	
	@SuppressWarnings("unchecked")
	@JSONField(serialize = false)
	private <T> T getParam(String key, Class<T> clazz) {
		if (params == null) {
			return null;
		}

		Object obj = params.get(key);
		if (obj == null) {
			return null;
		}

		if (obj instanceof JSONObject) {
			return JSON.toJavaObject((JSONObject) obj, clazz);
		}

		return (T) obj;
	}
	
	@JSONField(serialize = false)
	public Integer getSelectFrom() {
		return pageSize * (page - 1);
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
