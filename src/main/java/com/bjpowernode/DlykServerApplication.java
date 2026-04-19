package com.bjpowernode;

import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@MapperScan(value = {"com.bjpowernode.mapper"})
@SpringBootApplication
public class DlykServerApplication implements CommandLineRunner {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DlykServerApplication.class, args);
    }

    /**
     * 该run在springboot项目启动的时候会执行一次（就只执行一次）
     *
     * 在上面的main方法执行之后，就会执行该run方法
     *
     * 所以在项目中，经常用来对项目做一些初始化的工作
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args)  {
        redisTemplate.setKeySerializer(RedisSerializer.string()); //redis的key采用string进行序列化
        redisTemplate.setValueSerializer(RedisSerializer.json()); //如果放入redis的时候是一个对象，那么建议采用json序列化
        redisTemplate.setHashKeySerializer(RedisSerializer.string()); //redis的hashKey采用string进行序列化
        redisTemplate.setHashValueSerializer(RedisSerializer.string()); //redis的hashValue采用string进行序列化
    }
}
