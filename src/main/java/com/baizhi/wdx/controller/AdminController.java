package com.baizhi.wdx.controller;
import com.baizhi.wdx.dao.AdminDao;
import com.baizhi.wdx.entity.Admin;
import com.baizhi.wdx.service.AdminService;
import com.baizhi.wdx.serviceImpl.AdminServiceImpl;
import com.baizhi.wdx.util.ImageCodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Resource
    AdminService adminService;

    @RequestMapping("getImgCode")
    @ResponseBody
    public void getImgCode(HttpServletRequest request , HttpServletResponse response){
        //根据ImageCodeUtil获取随机字符
        String code = ImageCodeUtil.getSecurityCode();
        System.out.println(code+"--------验证码");
            //存储随机字符
      request.getSession().setAttribute("imageCode", code);
        //生成图片
        BufferedImage image = ImageCodeUtil.createImage(code);
        //响应页面
        try {
            ImageIO.write(image,"png",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("login")
    @ResponseBody
    public   HashMap<String, Object> login(Admin admin,String enCode) {

        System.out.println(admin + enCode);
        //调用登陆业务方法
        HashMap<String, Object> map = adminService.login(admin, enCode);
        return map;
    }
    @RequestMapping("logout")
    public String logout(HttpServletRequest request ){
        HttpSession session = request.getSession();
        session.removeAttribute("admin");
        return  "redirect:/login/login.jsp";
    }
}
