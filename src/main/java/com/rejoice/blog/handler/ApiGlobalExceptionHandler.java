package com.rejoice.blog.handler;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.rejoice.blog.common.bean.ResponseVo;
/**
* @ClassName: GlobalDefaultExceptionHandler 
* @Description: 全局异常处理
* @author rejoice  948870341@qq.com 
* @date 2016年9月27日 下午5:07:48 
*
 */
//@ControllerAdvice(basePackageClasses={ApiCompanyController.class})
class ApiGlobalExceptionHandler {
  public static final String DEFAULT_ERROR_VIEW = "error";
  private Logger logger = LoggerFactory.getLogger(ApiGlobalExceptionHandler.class);
  
  @ExceptionHandler(value = Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ResponseVo defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
	  logger.error("api global exception handler, exception occurs:",e);
	  return ResponseVo.systemError("system error");
  }
  
}