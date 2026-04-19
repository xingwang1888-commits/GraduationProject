package com.gdpu.util;

import com.gdpu.entity.TUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginInfoUtil {

    /**
     * 获取当前登录人的信息
     *
     * @return
     */
    public static TUser getCurrentLoginUser() {
        return (TUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
