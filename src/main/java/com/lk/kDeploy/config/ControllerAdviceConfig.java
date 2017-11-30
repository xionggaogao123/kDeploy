package com.lk.kDeploy.config;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lk.kDeploy.base.dto.ResponseDTO;
import com.lk.kDeploy.constants.ReturnCode;
import com.lk.kDeploy.util.RespBuildUtil;

@ControllerAdvice
public class ControllerAdviceConfig {

	private final static Logger LOG = LoggerFactory.getLogger(ControllerAdviceConfig.class);

    @ExceptionHandler(Exception.class)
    public @ResponseBody ResponseDTO jsonErrorHandler(HttpServletRequest request, Exception e) {
        LOG.error("有异常！请联系管理员", e);
        return RespBuildUtil.error(ReturnCode.FAIL);
    }
}
