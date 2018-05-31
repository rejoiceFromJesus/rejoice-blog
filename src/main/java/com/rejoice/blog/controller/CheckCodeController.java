package com.rejoice.blog.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.common.util.ImageCode;

/**
 *
 * CheckCodeController
 * 
 * @author rejoice 948870341@qq.com
 * @date 2017年6月8日 下午5:38:42
 * 
 * @version 1.0.0
 *
 */
@RestController
@RequestMapping("/checkCode")
public class CheckCodeController {

	
	@GetMapping
	public String imagecode(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    OutputStream os = response.getOutputStream();
	    Map<String,Object> map = ImageCode.getImageCode(60, 20, os);
	    request.getSession().setAttribute(Constant.SESSION_VALIDATE_CODE, map.get("strEnsure").toString().toLowerCase());
	    request.getSession().setAttribute("codeTime",new Date().getTime());
	    try {
	        ImageIO.write((BufferedImage) map.get("image"), "JPEG", os);
	    } catch (IOException e) {
	        return "";
	    }
	    return null;
	}
	
	@GetMapping(value="{checkCode}")
	public Integer checkcode(@PathVariable("checkCode") String checkCode,HttpServletRequest request, HttpSession session) throws Exception {
	    Object cko = session.getAttribute(Constant.SESSION_VALIDATE_CODE) ; //验证码对象
	    if(cko == null){
	        request.setAttribute("errorMsg", "验证码已失效，请重新输入！");
	        return 3;
	    }
	    String captcha = cko.toString();
	    Date now = new Date();
	    Long codeTime = Long.valueOf(session.getAttribute("codeTime")+"");
	    if (StringUtils.isEmpty(checkCode) || captcha == null || (now.getTime()-codeTime)/1000/60>5) {
	        //验证码有效时长为5分钟
	        request.setAttribute("errorMsg", "验证码已失效，请重新输入！");
	        return 3;
	    }else if( !(checkCode.equalsIgnoreCase(captcha))) {
	        request.setAttribute("errorMsg", "验证码错误！");
	        return 2;
	    }  else {
	        //session.removeAttribute("simpleCaptcha");
	        return 1;
	    }
	}
	
	

}
