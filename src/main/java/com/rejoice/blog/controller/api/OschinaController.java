package com.rejoice.blog.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rejoice.blog.service.ApiAccountService;

@RequestMapping("/api/oschina")
@RestController
public class OschinaController {
  
	@Autowired
	ApiAccountService apiAccountService;
	
	@GetMapping("/oauth/callback")
	public Map<String,Object> oauthCallback(@RequestParam String code) {
		Map<String,Object> map = new HashMap<>();
		map.put("code", code);
		return map;
	}
}
