package com.rejoice.blog.viewresolver;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import com.rejoice.blog.view.ExcelView;

/**
 * Created by aboullaite on 2017-02-24.
 */
public class ExcelViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
    	if(s.endsWith(".xls")){
    		ExcelView view = new ExcelView();
    		return view;
    		
    	}
    	return null;
    }
}
