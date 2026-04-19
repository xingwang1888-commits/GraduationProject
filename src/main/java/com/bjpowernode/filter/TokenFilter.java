package com.bjpowernode.filter;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import com.bjpowernode.constant.Constant;
import com.bjpowernode.entity.TUser;
import com.bjpowernode.result.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        //登录接口，不需要验证token（因为登录时，还没有生成token）
        String requestUri = request.getRequestURI(); //   /user/login
        if (requestUri.equals("/api/login")) { //如果是登录请求，我们不需要验证token
            //直接放行，不需要验证token
            filterChain.doFilter(request, response);
        } else {
            String token = request.getHeader("Authorization"); //从请求头中获取token的值
            if (!StringUtils.hasText(token)) { //前面有个 “非”
                Result result = Result.builder().code(901).msg("请求Token为空").build();
                response.getWriter().write(JSONUtil.toJsonStr(result));
            } else {
                boolean verify = false; //验证的初始值是false，false表示验证未通过
                try {
                    //验证通过了，则verify = true
                    verify = JWTUtil.verify(token, Constant.SECRET.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!verify) { //前面有个 “非”
                    Result result = Result.builder().code(902).msg("请求Token不合法").build();
                    response.getWriter().write(JSONUtil.toJsonStr(result));
                } else {
                    JSONObject payloads = JWTUtil.parseToken(token).getPayloads();
                    String userJSON = payloads.get("user", String.class);
                    TUser tUser = JSONUtil.toBean(userJSON, TUser.class);
                    Integer userId = tUser.getId();
                    //拿redis的token

                    String redisToken = (String) redisTemplate.opsForValue().get(Constant.REDIS_TOKEN_KEY + userId);
                    if (!token.equals(redisToken)) { //前面有个 “非”
                        Result result = Result.builder().code(903).msg("请求Token错误").build();
                        response.getWriter().write(JSONUtil.toJsonStr(result));
                    } else {
                        //token验证通过了，
                        //要在spring security的上下文中放置一个认证对象，这样的话，spring security在执行后续的Filter的时候，才知道这个人是登录了的
                        UsernamePasswordAuthenticationToken authenticationToken
                                = new UsernamePasswordAuthenticationToken(tUser, null, tUser.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                        //放行，让代码继续去执行下一个Filter
                        filterChain.doFilter(request, response);
                    }
                }
            }
        }
    }
}
