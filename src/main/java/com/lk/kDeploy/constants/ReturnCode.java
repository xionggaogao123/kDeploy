package com.lk.kDeploy.constants;

public interface ReturnCode {

    /**
     * 成功
     */
    int SUCCESS = 0;
    /**
     * 失败
     */
    int FAIL = 1;
    /**
     * session过期，请重新登录
     */
    int SESSION_TIMEOUT = 100;
    /**
     * 缺少必填参数
     */
    int REQUIRED_BLANK_ERROR = 101;
    /**
     * 参数为空
     */
    int PARAMS_BLANK_ERROR = 102;
}
