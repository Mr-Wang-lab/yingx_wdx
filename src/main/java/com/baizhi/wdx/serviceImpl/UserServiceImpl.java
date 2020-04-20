package com.baizhi.wdx.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baizhi.wdx.annotation.AddCache;
import com.baizhi.wdx.annotation.AddLog;
import com.baizhi.wdx.annotation.DelCache;
import com.baizhi.wdx.dao.CityMapper;
import com.baizhi.wdx.dao.UserMapper;
import com.baizhi.wdx.entity.City;
import com.baizhi.wdx.entity.Month;
import com.baizhi.wdx.entity.User;
import com.baizhi.wdx.entity.UserExample;
import com.baizhi.wdx.service.UserService;
import com.baizhi.wdx.util.AliyunOssUtil;
import com.baizhi.wdx.vo.CityInfoVo;
import com.google.common.collect.Lists;
import io.goeasy.GoEasy;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;
    @Resource
    CityMapper cityMapper;

    @Override
    public List<CityInfoVo> userMap() {
        List<CityInfoVo> list = Lists.newArrayList();

        List<City> listBoy = cityMapper.queryBoyAndCity();
        CityInfoVo cityInfoVo = new CityInfoVo();
        cityInfoVo.setTitle("男");
        cityInfoVo.setCitys(listBoy);
        list.add(cityInfoVo);
        List<City> listGrily = cityMapper.queryGrilAndCity();
        CityInfoVo cityInfoVo1 = new CityInfoVo();
        cityInfoVo1.setTitle("女");
        cityInfoVo1.setCitys(listGrily);
        list.add(cityInfoVo1);

        String content = JSON.toJSONString(list);

        //配置发送消息的必要配置  参数：regionHost,服务器地址,自己的appKey
        GoEasy goEasy = new GoEasy( "http://rest-hangzhou.goeasy.io", "BC-49c4bb3ed91945448c35358477615835");

        //配置发送消息  参数:管道名称（自定义）,发送内容
        goEasy.publish("186-yingxChanne", content);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;
    }
    @AddCache
    @Override
    public HashMap<String, Object> queryByuserCount() {

        HashMap<String, Object> map = new HashMap<>();
        List<Month> monthBoy = cityMapper.queryBoyCount();
        List<String> months = Lists.newArrayList();
        List<String> boys = Lists.newArrayList();
        List<String> girls = Lists.newArrayList();
        for (Month month : monthBoy) {
            months.add(month.getMonth());
            if (month.getSex().equals("男")){
                boys.add(month.getValue());
            }else {
                girls.add(month.getValue());
            }
        }
        List newList = months.stream().distinct().collect(Collectors.toList());
        map.put("month", newList);
        map.put("girl", girls);
        map.put("boy", boys);

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
        return map;

    }
    @AddCache
    @Override
    public HashMap<String, Object> queryByPage(Integer page, Integer rows) {

        HashMap<String, Object> map = new HashMap<>();

        //封装数据
        //总条数   records
        UserExample example = new UserExample();
        Integer records = userMapper.selectCountByExample(example);
        map.put("records", records);
        //总页数   total   总条数/每页展示条数  是否有余数
        Integer total = records % rows == 0 ? records / rows : records / rows + 1;
        map.put("total", total);
        //当前页   page
        map.put("page", page);
        //数据     rows
        //参数  忽略条数,获取几条
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<User> users = userMapper.selectByRowBounds(new User(), rowBounds);
        map.put("rows", users);

        return map;
    }

    @AddLog("添加用户")
    @DelCache
    @Override
    public String add(User user) {
        String uid = UUID.randomUUID().toString();
        user.setId(uid);
        user.setStatus("1");
        user.setCreateDate(new Date());
        System.out.println(user + "useraddserviceimpl");
        userMapper.insert(user);
        this.queryByuserCount();
        this.userMap();
        return uid;
    }

    @Override
    public void uploadUser(MultipartFile headImg, String id, HttpServletRequest request) {
        //1.根据相对路径获取绝对路径
        String realPath = request.getSession().getServletContext().getRealPath("/upload/photo");

        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        //2获取文件名
        String filename = headImg.getOriginalFilename();

        String newName = new Date().getTime() + "-" + filename;

        try {
            //3.文件上传
            headImg.transferTo(new File(realPath, newName));

            System.out.println(realPath + ".........realpath   newName" + newName);
            //4.图片修改
            //修改的条件
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(id);

            User user = new User();
            user.setHeadImg(newName); //设置修改的结果

            //修改
            userMapper.updateByExampleSelective(user, example);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @AddLog("修改用户")
    @DelCache
    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public ArrayList<User> queryAll() {
        UserExample example = new UserExample();
        ArrayList<User> users = (ArrayList<User>) userMapper.selectByExample(example);
        return users;
    }

    @AddLog("删除用户")
    @DelCache
    @Override
    public void del(User user) {
        userMapper.delete(user);

    }

    @Override
    public void uploadUserAliyun(MultipartFile headImg, String id, HttpServletRequest request) {

        //将文件转为byte数组
        byte[] bytes = null;
        try {
            bytes = headImg.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //获取文件名
        String filename = headImg.getOriginalFilename();
        String newName = new Date().getTime() + "-" + filename;


        //1.文件上传至阿里云

        // Endpoint以北京为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4FuYtJia246XD7SK55nj";
        String accessKeySecret = "K1yNZh215gNbaXSVdhBM3c8LNYcS61";
        String bucket = "yingx-186wdx";   //存储空间名
        String fileName = newName;  //指定上传文件名  可以指定上传目录

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传Byte数组。
        ossClient.putObject(bucket, fileName, new ByteArrayInputStream(bytes));

        // 关闭OSSClient。
        ossClient.shutdown();


        //2.图片信息的修改
        //修改的条件
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(id);

        User user = new User();
        user.setHeadImg("https://yingx-186wdx.oss-cn-beijing.aliyuncs.com/" + newName);  //设置修改的结果   网络路径
        //https://yingx-186.oss-cn-beijing.aliyuncs.com/1585641490828-9.jpg

        //修改
        userMapper.updateByExampleSelective(user, example);


    }

    @Override
    public void uploadUserAliyuns(MultipartFile headImg, String id, HttpServletRequest request) {
        //获取文件名
        String filename = headImg.getOriginalFilename();
        String newName = new Date().getTime() + "-" + filename;

        //1.文件上传至阿里云
        AliyunOssUtil.uploadFileBytes("yingx-186wdx", headImg, newName);

        //截取视频第一帧
        //上传封面

        //2.图片信息的修改
        //修改的条件
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(id);

        User user = new User();
        user.setHeadImg("https://yingx-186wdx.oss-cn-beijing.aliyuncs.com/" + newName);  //设置修改的结果   网络路径
        //https://yingx-186wdx.oss-cn-beijing.aliyuncs.com/1585641490828-9.jpg

        //修改
        userMapper.updateByExampleSelective(user, example);

    }
}
