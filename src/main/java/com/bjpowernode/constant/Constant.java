package com.bjpowernode.constant;

/**
 * 常量类
 *
 */
public class Constant {

    //JWT的密钥
    public static final String SECRET = "0S/12d4845Whd2h3OPYs0q2";

    //权限标识符命名：  [项目名:]模块名:功能名
    //redis的key的命名规范：   项目名:模块名:功能名[:唯一业务参数]
    public static final String REDIS_TOKEN_KEY = "security:user:login";
}
