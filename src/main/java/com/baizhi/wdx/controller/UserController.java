package com.baizhi.wdx.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baizhi.wdx.entity.City;
import com.baizhi.wdx.entity.User;
import com.baizhi.wdx.service.UserService;
import com.baizhi.wdx.util.AliyunSendPhoneUtil;
import com.baizhi.wdx.vo.CityInfoVo;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {

    @Resource
    UserService userService;

    private static final String MESSAGE = "操作成功";

    @RequestMapping("queryByPage")
    @ResponseBody
    public HashMap<String, Object> queryByPage(Integer page, Integer rows) {

         HashMap<String, Object> map = userService.queryByPage(page, rows);


        return map;
    }

    @RequestMapping("edit")
    @ResponseBody
    public String edit(User user, String oper) {

        String uid = null;
        if (oper.equals("add")) {
            System.out.println(user + "::---adddddddcontro");
            uid = userService.add(user);
        }
        if (oper.equals("edit")) {
            userService.update(user);
        }
        if (oper.equals("del")) {
            userService.del(user);
        }
        return uid;
    }


    //文件上传
    @RequestMapping("uploadUser")
    public void uploadUser(MultipartFile headImg, String id, HttpServletRequest request) {
        //userService.uploadUser(headImg,id,request);  上传到本地
        userService.uploadUserAliyuns(headImg, id, request);  //上传到阿里云
    }

    //验证码
    @RequestMapping("sendPhoneCode")
    @ResponseBody
    public String sendPhoneCode(String phone) {

        long time = new Date().getTime();
        System.out.println(phone + time + "  :  dianhuahaoma ");

        String random = AliyunSendPhoneUtil.getRandom(6);
        //存储验证码

        String massage = AliyunSendPhoneUtil.sendCode(phone, random);

        System.out.println(massage + " massagecontroller");
        return massage;
    }

    @RequestMapping("sendExcel")
    @ResponseBody
    public String sendExcel() {

        System.out.println("sendExcel++++++++++++++++++++");
        try {
            ArrayList<User> usersList = userService.queryAll();
            ExportParams exportParams = new ExportParams("用户信息表", "信息表一");
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, User.class, usersList);
            workbook.write(new FileOutputStream(new File("D://186.xls")));
            workbook.close();
            return MESSAGE;
        } catch (IOException e) {
            e.printStackTrace();
            return "保存失败";
        }

    }
    @RequestMapping("userMap")
    @ResponseBody
    public List<CityInfoVo>  userMap(){

        System.out.println("senEXCLE");

        return userService.userMap();
    }

    @RequestMapping("queryByuserCount")
    @ResponseBody
    public HashMap<String, Object> queryByuserCount(){
        HashMap<String, Object> map = userService.queryByuserCount();
        return map;
    }

}
