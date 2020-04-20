package com.baizhi.wdx;


import com.baizhi.wdx.entity.Admin;
import com.baizhi.wdx.repository.AdminRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GitTests<pvsm> {


   @Resource
    AdminRepository adminRepository;

   @Test
   public void testqueryss() {


   }
   @Test
   public void testquerys() {
      int a =10;
      int b = 30;
      int c = a+b;
      

   }

}
