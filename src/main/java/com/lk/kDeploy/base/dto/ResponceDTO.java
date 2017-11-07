package com.lk.kDeploy.base.dto;

import java.util.List;

/**
 * 请求参数
 *
 * @author: lk
 * @since: 2017年10月1日
 */
public class ResponceDTO {

	private int returnCode;
	private String returnMsg;
	
	private Object data;
	
	private Integer page;
	private Integer pageSize;
	private Integer totalPage;
	private List<? extends Object> dataList;
	public int getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
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
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public List<? extends Object> getDataList() {
		return dataList;
	}
	public void setDataList(List<? extends Object> dataList) {
		this.dataList = dataList;
	}
}
