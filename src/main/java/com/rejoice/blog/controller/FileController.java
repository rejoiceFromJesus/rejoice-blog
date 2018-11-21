package com.rejoice.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rejoice.blog.bean.vo.LayuiEditUploadImgVo;
import com.rejoice.blog.common.bean.Result;
import com.rejoice.blog.service.UploadService;

@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	UploadService uploadService;

	@PostMapping("/upload")
	public Result<LayuiEditUploadImgVo> uploadMultipleFiles(@RequestParam("file") MultipartFile[] files) {
		String httpPath = uploadService.uploadImg(files[0]);
		LayuiEditUploadImgVo vo = new LayuiEditUploadImgVo();
		vo.setSrc(httpPath);
		vo.setTitle(files[0].getOriginalFilename());
		return Result.success(vo);
	}
}
