package com.lk.kDeploy.exception;

/**
 * 业务运行时异常
 *
 * @author: lk
 * @since: 2017年11月7日
 */
public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5604111511825282042L;

	private int errorCode;
	
	public ServiceException() {}
	
	public ServiceException(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
