package com.rejoice.blog.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.rejoice.blog.common.exception.InternalServerException;
import com.rejoice.blog.properties.QiniuProperties;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QiniuService {

	@Autowired
	private QiniuProperties qiniuProperties;
	
	public String upload(InputStream inputStream, String fileName) {
		String[] fileNames = fileName.split("\\.");
		fileName += fileNames[0] + "_" + System.currentTimeMillis() + "." + fileNames[1];
		try {
			// 构造一个带指定Zone对象的配置类
			Configuration cfg = new Configuration(Zone.zone0());
			//...其他参数参考类注释
			UploadManager uploadManager = new UploadManager(cfg);
			//...生成上传凭证，然后准备上传
			//默认不指定key的情况下，以文件内容的hash值作为文件名
			Auth auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
			String upToken = auth.uploadToken(qiniuProperties.getBucket());
			Response response = uploadManager.put(inputStream, fileName, upToken, null, null);
			// 解析上传成功的结果
			DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			return qiniuProperties.getHost()+"/"+putRet.key;
		} catch (Exception e) {
			log.error("上传文件失败:", e.getMessage());
			throw new InternalServerException(e);
		}
	}
}
