package com.rejoice.blog.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rejoice.blog.common.bean.Result;
import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.concurrent.VolitateVars;
import com.rejoice.blog.entity.PdfBook;
import com.rejoice.blog.service.PdfBookService;

@RestController
@RequestMapping("/pdf-book")
public class PdfBookController extends BaseController<PdfBook, PdfBookService> {

	@PostMapping("/batch-import")
	public Result<Void> batchImport(@RequestBody Map<String,String> books){
		this.getService().batchImport(books.get("books"));
		return Result.success(null);
	}
	
	@PostMapping("/batch-post")
	public Result<Void> batchPost(){
		if(!StringUtils.equalsAnyIgnoreCase(Constant.TRUE
				, VolitateVars.POST_JIANSHU_BATCH_LOCK
				, VolitateVars.POST_SYSTEM_BATCH_LOCK)) {
			this.getService().batchPost();
			return Result.success(null); 
		}
		return Result.paramError("上次批量发表还在进行中，请稍候重试！");
		
	}
}
