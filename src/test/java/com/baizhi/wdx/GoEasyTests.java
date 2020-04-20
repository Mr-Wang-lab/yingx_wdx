package com.baizhi.wdx;

import com.alibaba.fastjson.JSON;
import com.baizhi.wdx.dao.VideoMapper;
import com.baizhi.wdx.po.VideoPo;
import io.goeasy.GoEasy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class GoEasyTests {



    @Test
    public void testqueryss(){

        //配置发送消息的必要配置  参数：regionHost,服务器地址,自己的appKey
        GoEasy goEasy = new GoEasy( "http://rest-hangzhou.goeasy.io", "BC-0057789df92c41168811cf621c262470");

        //配置发送消息  参数:管道名称（自定义）,发送内容
        goEasy.publish("186-yingxChannel", "Hello, 186 GoEasy!");
    }

    @Test
    public void testGoEasyUser(){

        //添加用户

        for (int i = 0; i < 10; i++) {

            Random random = new Random();
            //获取随机数  参数i：50  0<=i<50
            //int i = random.nextInt(50);

            HashMap<String, Object> map = new HashMap<>();

            //根据月份 性别 查询数量   //查询用户信息
            map.put("month", Arrays.asList("1月","2月","3月","4月","5月","6月"));
            map.put("boys", Arrays.asList(random.nextInt(500),random.nextInt(500),random.nextInt(500),random.nextInt(500),random.nextInt(500),random.nextInt(200)));
            map.put("girls", Arrays.asList(random.nextInt(500),random.nextInt(500),random.nextInt(500),random.nextInt(500),random.nextInt(500),random.nextInt(500)));

            //将对象转为json格式字符串
            String content = JSON.toJSONString(map);

            //配置发送消息的必要配置  参数：regionHost,服务器地址,自己的appKey
            GoEasy goEasy = new GoEasy( "http://rest-hangzhou.goeasy.io", "BC-49c4bb3ed91945448c35358477615835");

            //配置发送消息  参数:管道名称（自定义）,发送内容
            goEasy.publish("186-yingxChannel", content);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }



}
