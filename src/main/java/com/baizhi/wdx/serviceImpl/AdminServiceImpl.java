package com.baizhi.wdx.serviceImpl;

import com.baizhi.wdx.dao.AdminDao;
import com.baizhi.wdx.entity.Admin;
import com.baizhi.wdx.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    @Resource
    HttpSession session;
    @Resource
    AdminDao adminDao;
    @Override
    public   HashMap<String, Object> login(Admin admin, String enCode) {

        HashMap<String, Object> map = new HashMap<>();
        //1.获取存储的验证码
        String code = (String) session.getAttribute("imageCode");
        //2.验证验证码
        if(enCode.equals(code)){
            //3.验证用户
            //查询用户
            Admin admin1 = adminDao.queryByUsername(admin.getUsername());
            if(admin1 !=null){
                //4.验证密码
                if(admin.getPassword().equals(admin1.getPassword())){
                    session.setAttribute("admin",admin1);
                    System.out.println("admin1---------:"+ admin1);
                    map.put("status","200");
                }else{
                    map.put("status","400");
                    map.put("message","密码错误");
                }

            }else{
                map.put("status","400");
                map.put("message","用户不存在");
            }


        }else{
            map.put("status","400");
            map.put ("message","验证码错误");
        }

        return map;
    }


}
