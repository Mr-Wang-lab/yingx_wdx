package com.baizhi.wdx;


import com.alibaba.fastjson.JSON;
import io.goeasy.GoEasy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Set;




 @RunWith(SpringRunner.class)
 @SpringBootTest
public class RedisTests {


    @Resource
    RedisTemplate redisTemplate;

    @Test
    public void testqueryss(){
      /*  Set keys = redisTemplate.keys("*");
        for (Object key : keys) {
            System.out.println(key);
        }*/
       // redisTemplate.opsForValue().set("naem","xiaohei");

        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object name = valueOperations.get("*");
        System.out.println(name);

    }

    @Test
    public void testGoEasyUser(){


    }



}
