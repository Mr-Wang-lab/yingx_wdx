package com.baizhi.wdx.controller;


import com.baizhi.wdx.service.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;

@Controller
@RequestMapping("Log")
public class LogController {


    @Resource
    LogService logService;
    @ResponseBody
    @RequestMapping("queryByPage")
    public HashMap<String, Object> queryBypage(Integer page,Integer rows){
        HashMap<String, Object> map = logService.queryByPage(page, rows);
        return map;
    }


}
