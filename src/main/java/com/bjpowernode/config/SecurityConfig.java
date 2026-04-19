package com.bjpowernode.config;

import com.bjpowernode.filter.TokenFilter;
import com.bjpowernode.handler.AppLogoutSuccessHandler;
import com.bjpowernode.handler.AppAccessDeniedHandler;
import com.bjpowernode.handler.AppAuthenticationFailureHandler;
import com.bjpowernode.handler.AppAuthenticationSuccessHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableMethodSecurity //开启方法上的注解权限检查
@Configuration //配置spring的容器，类似spring.xml文件一样
public class SecurityConfig {

    @Resource
    private AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;

    @Resource
    private AppAuthenticationFailureHandler appAuthenticationFailureHandler;

    @Resource
    private AppLogoutSuccessHandler appLogoutSuccessHandler;

    @Resource
    private TokenFilter tokenFilter;

    @Resource
    private AppAccessDeniedHandler appAccessDeniedHandler;


    @Bean  //配置一个spring的bean， bean的id就是方法名，bean的class就是方法的返回类型
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //配置跨域
    public CorsConfigurationSource configurationSource() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        //跨域配置
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("*")); //允许任何来源，http://localhost:10492/
        corsConfiguration.setAllowedMethods(Arrays.asList("*")); //允许任何请求方法，post、get、put、delete
        corsConfiguration.setAllowedHeaders(Arrays.asList("*")); //允许任何的请求头 (jwt)

        //注册跨域配置
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration); //  /api/user,  /api/user/12082
        return urlBasedCorsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfigurationSource configurationSource) throws Exception { //httpSecurity是方法参数注入Bean

        return httpSecurity
                //配置我们自己的登录页
                .formLogin( (formLogin) -> {
                    // 框架默认接收登录提交请求的地址是 /login，但是我们把它给弄丢了，需要捡回来
                    formLogin.loginProcessingUrl("/api/login") //登录的账号密码往哪个地址提交
                            .usernameParameter("loginAct") //指定登录账号的参数名，（不指定默认是username）
                            .passwordParameter("loginPwd") //指定登录密码的参数名，（不指定默认是password）
                            .successHandler(appAuthenticationSuccessHandler) //登录成功后执行该handler
                            .failureHandler(appAuthenticationFailureHandler);// 登录失败后执行该handler
                })

                .logout((logout) -> {
                    logout.logoutUrl("/api/logout") //退出请求提交到哪个地址
                            .logoutSuccessHandler(appLogoutSuccessHandler); //退出成功后执行该handler
                })

                //把所有接口都会进行登录状态检查的默认行为，再加回来
                .authorizeHttpRequests( (authorizeHttpRequests) -> {
                    authorizeHttpRequests
                            .anyRequest().authenticated(); //除了上面的特殊情况外，其他任何对后端接口的请求，都需要认证（登录）后才能访问
                })

                .csrf( (csrf) -> {
                    //禁用csrf跨站请求伪造，禁用之后，肯定就不安全了，有csrf网络攻击的风险，后续加入jwt是可以防御的
                    csrf.disable();
                })

                .cors( (cors) -> { //允许前端跨域访问
                    cors.configurationSource(configurationSource);
                })

                .sessionManagement((sessionManagement) -> {
                    //session创建策略 (无session状态)
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })

                //在登录Filter后面加入我们的token验证的Filter
                .addFilterBefore(tokenFilter, LogoutFilter.class)

                .exceptionHandling((exceptionHandling) -> {
                    exceptionHandling.accessDeniedHandler(appAccessDeniedHandler); //没有权限的时候，执行该handler
                })

                .build();
    }
}