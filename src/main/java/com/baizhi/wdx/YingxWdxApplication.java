package com.baizhi.wdx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.baizhi.wdx.dao")
//@MapperScan("com.baizhi.wdx.dao")

@SpringBootApplication
public class YingxWdxApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(YingxWdxApplication.class, args);
    }

}
