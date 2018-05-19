package com.rejoice.blog.security;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rejoice.blog.entity.UserInfo;
import com.rejoice.blog.service.UserInfoService;

@Service
public class MyAppUserDetailsService implements UserDetailsService {
	@Autowired
	private UserInfoService userInfoService;
	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		UserInfo cons = new UserInfo();
		cons.setUserName(userName);
		UserInfo loginUser = userInfoService.queryOne(cons);
		if(loginUser == null) {
			throw new RuntimeException("用户名不匹配");
		}
		GrantedAuthority authority = new SimpleGrantedAuthority(loginUser.getRole());
		UserDetails userDetails = (UserDetails)new User(loginUser.getUserName(),
				loginUser.getPassword(), Arrays.asList(authority));
		return userDetails;
	}
	
	
}