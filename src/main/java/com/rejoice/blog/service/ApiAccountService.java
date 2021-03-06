package com.rejoice.blog.service;

import org.springframework.stereotype.Service;

import com.rejoice.blog.common.constant.PlatformEnum;
import com.rejoice.blog.entity.ApiAccount;

@Service
public class ApiAccountService extends BaseService<ApiAccount> {

	public ApiAccount getJianshuAccount() {
		//get jianshu account
		ApiAccount jianshuCons = new ApiAccount();
		jianshuCons.setUsername("18719353314");
		jianshuCons.setPlatform(PlatformEnum.JIANSHU);
		ApiAccount jianshuAccount = this.queryOne(jianshuCons);
		return jianshuAccount;
	}
	
	public ApiAccount getOschinaAccount() {
		//get oschina account
		ApiAccount oschinaCons = new ApiAccount();
		oschinaCons.setUsername("18719353314");
		oschinaCons.setPlatform(PlatformEnum.OSCHINA);
		ApiAccount oschinaAccount = this.queryOne(oschinaCons);
		return oschinaAccount;
	}
	
	public ApiAccount getChengTongAccount() {
		//get ct account
		ApiAccount ctCons = new ApiAccount();
		ctCons.setPlatform(PlatformEnum.CHENG_TONG);
		ApiAccount ctAccount = this.queryOne(ctCons);
		return ctAccount;
	}
}
