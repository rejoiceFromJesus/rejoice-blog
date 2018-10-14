package com.rejoice.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rejoice.blog.entity.ApiAccount;
import com.rejoice.blog.service.ApiAccountService;

@RestController
@RequestMapping("/api-account")
public class ApiAccountController extends BaseController<ApiAccount, ApiAccountService> {

}
