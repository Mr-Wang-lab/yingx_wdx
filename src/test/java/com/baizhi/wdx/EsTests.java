package com.baizhi.wdx;


import com.baizhi.wdx.entity.Admin;
import com.baizhi.wdx.repository.AdminRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
public class EsTests<pvsm> {


   @Resource
    AdminRepository adminRepository;

   @Test
   public void testqueryss() {

       Admin admin = new Admin("root","张美鹃","789789798");

       adminRepository.save(admin);

   }
    @Test
    public void query() {






    }  
}
