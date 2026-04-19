package com.bjpowernode.util;

import cn.hutool.json.JSONUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bjpowernode.entity.TUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JavaJwtUtil {

    //密钥不能让别人知道，这个密钥放在服务器上
    public static final String secret = "0S/12dSd0=;2Sfdjkgh3OPYs";


    //1、怎么生成jwt这个字符串？
    public static String createToken(String userJson) {
        //组装头数据
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        //链式编程
        return JWT.create()
                //头部
                .withHeader(header)

                //负载
                .withClaim("user", userJson)
                .withClaim("phone", "13725121212")
                .withClaim("email", "13725121212@139.com")
                .withClaim("birthDay", new Date())

                //签名
                .sign(Algorithm.HMAC256(secret));
    }


    //2、怎么验证jwt有没有被篡改？
    public static Boolean verifyToken(String token) {
        try {
            // 使用密钥创建一个jwt验证对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            //使用验证器对象验证JWT，如果验证没有抛出异常，说明验证通过，反之就是没有通过
            jwtVerifier.verify(token);
            //如果验证没有抛出异常，返回true表示验证通过
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    //3、怎么解析出jwt里面的负载数据？
    public static String parseToken(String token) {
        try {
            // 使用秘钥创建一个jwt验证器对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            //验证JWT，得到一个解码后的jwt对象
            DecodedJWT decodedJWT = jwtVerifier.verify(token);

            //通过解码后的jwt对象获取负载的数据
            Claim user = decodedJWT.getClaim("user");
            System.out.println(user.asString());

            Claim phone = decodedJWT.getClaim("phone");
            System.out.println(phone.asString());

            Claim email = decodedJWT.getClaim("email");
            System.out.println(email.asString());

            Claim birthDay = decodedJWT.getClaim("birthDay");
            System.out.println(birthDay.asDate());

            return user.asString();
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
