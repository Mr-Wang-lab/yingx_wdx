package com.baizhi.wdx.service;

import com.baizhi.wdx.entity.City;
import com.baizhi.wdx.entity.User;
import com.baizhi.wdx.vo.CityInfoVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface UserService {
    /**
     *
     * @param page
     * @param rows
     * @return
     */
    List<CityInfoVo> userMap();

    HashMap<String,Object> queryByuserCount();

    HashMap<String, Object> queryByPage(Integer page, Integer rows);

    String add(User user);

    void uploadUser(MultipartFile headImg, String id, HttpServletRequest request);

    void update(User user);

    ArrayList<User> queryAll();
    void del(User user);

    void uploadUserAliyun(MultipartFile headImg, String id, HttpServletRequest request);

    void uploadUserAliyuns(MultipartFile headImg, String id, HttpServletRequest request);
}