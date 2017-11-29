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
    /**
     * 缺少分页参数
     */
    int PAGE_PARAM_BLANK_ERROR = 103;
    /**
     * 找不到对象
     */
    int OBJECT_NOT_FOUND = 104;
    
    /*
     * 用户模块
     */
    /**
     * 用户名或密码不正确
     */
    int USERNAME_OR_PASSWORD_ERROR = 201;
    
    /*
     * 项目模块
     */
    /**
     * 命令执行错误
     */
    int EXECUTE_COMMAND_ERROR = 301;
    /**
     * 项目源码目录创建失败
     */
    int PROJECT_SOURCE_PATH_MAKE_ERROR = 302;
    
}
