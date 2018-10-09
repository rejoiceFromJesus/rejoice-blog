package com.rejoice.blog.service;

import org.springframework.stereotype.Service;

import com.rejoice.blog.entity.ApiAccount;

@Service
public class ApiAccountService extends BaseService<ApiAccount> {

	public ApiAccount getJianshuAccount() {
		//get jianshu account
		ApiAccount jianshuCons = new ApiAccount();
		jianshuCons.setUsername("18719353314");
		jianshuCons.setPlatform("简书");
		ApiAccount jianshuAccount = this.queryOne(jianshuCons);
		return jianshuAccount;
	}
}
