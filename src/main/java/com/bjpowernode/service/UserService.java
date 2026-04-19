package com.bjpowernode.service;

import org.springframework.security.core.userdetails.UserDetailsService;

//我们的处理登录的service接口，需要继承spring security框架的UserDetailsService接口
public interface UserService extends UserDetailsService {

}
