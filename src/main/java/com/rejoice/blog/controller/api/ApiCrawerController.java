package com.rejoice.blog.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rejoice.blog.common.bean.Result;
import com.rejoice.blog.entity.CrawerProgress;
import com.rejoice.blog.service.CrawerProgressService;

@RestController
@RequestMapping("/api/token/crawer")
public class ApiCrawerController {

	@Autowired
	private CrawerProgressService crawerProgressService;
	
	@PostMapping("/progress")
	public Result<Void> saveProgress(CrawerProgress crawerProgress) {
		crawerProgressService.saveSelective(crawerProgress);
		return Result.success(null);
	}
	
	@GetMapping("/progress")
	public Result<CrawerProgress> getProgress(CrawerProgress crawerProgress) {
		crawerProgress = crawerProgressService.queryOne(crawerProgress);
		return Result.success(crawerProgress);
	}
}
