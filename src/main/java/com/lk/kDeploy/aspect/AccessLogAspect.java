package com.lk.kDeploy.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lk.kDeploy.base.annotion.AnonymousAccess;
import com.lk.kDeploy.base.annotion.ValidateRequired;
import com.lk.kDeploy.base.dto.RequestDTO;
import com.lk.kDeploy.base.dto.ResponseDTO;
import com.lk.kDeploy.constants.Constants;
import com.lk.kDeploy.constants.ReturnCode;
import com.lk.kDeploy.exception.ServiceException;
import com.lk.kDeploy.util.JsonUtils;
import com.lk.kDeploy.util.RespBuildUtil;

/**
 * api请求aop
 * 
 * <ol>
 * 	<li>打印请求日志</li>
 * 	<li>登录拦截</li>
 * 	<li>必填参数校验</li>
 * </ol>
 *
 * @author: lk
 * @since: 2017年10月1日
 */
@Aspect
@Component
public class AccessLogAspect {
	private final Logger LOG = LoggerFactory.getLogger(AccessLogAspect.class);

//	private final Logger access = LoggerFactory.getLogger("scIrond.access");

	@Pointcut("execution(public * com.lk.kDeploy.controller.*.*(..))")
	public void accessLog() {
	}

	public boolean isMatches(String className, String reg) {
		return className.matches(reg);
	}

	@Around("accessLog()")
	public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			
			MethodSignature ms = (MethodSignature) pjp.getSignature();
	        Method method = ms.getMethod();

			RequestDTO reqDto = null;
			Object[] args = pjp.getArgs();
			for (Object obj : args) {
				if (obj instanceof RequestDTO) {
					reqDto = (RequestDTO) obj;
					break;
				}
			}
			
			String uri = request.getRequestURI();
			if (null == reqDto) {
				LOG.info("访问接口, uri: {}, request: null", uri);
			} else {
				LOG.info("访问接口, uri: {}, request: {}", uri, JsonUtils.toJson(reqDto));
			}
			
			validateAccess(method, request);
			if (null != reqDto) validateRequired(method, reqDto.getParams());
			
			Object result = pjp.proceed();
			LOG.info("接口返回, uri: {}, response: {}", uri, JsonUtils.toJson(result));
			
			return result;
		} catch (ServiceException e) {
			ResponseDTO errorResp = RespBuildUtil.error(e.getErrorCode());
			LOG.info("接口返回, errorResp: {}", JsonUtils.toJson(errorResp));
			return errorResp;
		}
	}

	/**
	 * 校验访问权限
	 * @param method
	 * @param request
	 * @throws ServiceException 
	 */
	private void validateAccess(Method method, HttpServletRequest request) throws ServiceException {
		// 匿名访问时，不需要获取openid			        
		AnonymousAccess anonymousAccess = AnnotationUtils.findAnnotation(method, AnonymousAccess.class);
		if (anonymousAccess == null && !isLogin(request)) {
			throw new ServiceException(ReturnCode.SESSION_TIMEOUT);
		}
	}

	/**
	 * 校验必填参数
	 * @param method
	 * @param params
	 * @throws ServiceException 
	 */
	private void validateRequired(Method method, Map<String, Object> params) throws ServiceException {
		ValidateRequired validateRequired = AnnotationUtils.findAnnotation(method, ValidateRequired.class);
		if (null == validateRequired) {
			return;
		}
		
		if (null == params) {
			LOG.info("params参数为空");
			throw new ServiceException(ReturnCode.PARAMS_BLANK_ERROR);
		}
		
		String[] requireds = validateRequired.value();
		
		List<String> miss = new ArrayList<>();
		for (String required : requireds) {
			Object value = params.get(required);
			if (null == value || StringUtils.isBlank(value.toString())) {
				miss.add(required);
			}
		}
		
		if (miss.size() > 0) {
			LOG.info("缺少必填参数：{}", StringUtils.join(miss, ", "));
			throw new ServiceException(ReturnCode.REQUIRED_BLANK_ERROR);
		}
	}

	private boolean isLogin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (null == session.getAttribute(Constants.SESSION_LOGIN_USER)) {
			return false;
		}
		return true;
	}
}
