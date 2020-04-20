package com.baizhi.wdx.controller;

import com.baizhi.wdx.entity.Video;
import com.baizhi.wdx.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping("video")
public class VideoController {
    @Resource
    VideoService videoService;


    //分页查询视频
    @ResponseBody
    @RequestMapping("queryByPage")
    public HashMap<String,Object> queryBypage(Integer page,Integer rows){
        System.out.println("VIDEO  +++CONTROLLER");

        HashMap<String, Object> map = videoService.queryByPage(page, rows);

        return map;
    }

    //添加视频
    @ResponseBody
    @RequestMapping("edit")
    public Object edit(Video video, String oper) {

        if (oper.equals("add")) {

            String id = videoService.add(video);
            return id;
        }

        if (oper.equals("edit")) {
            videoService.update(video);
        }

        if (oper.equals("del")) {
            HashMap<String, Object> map = videoService.delete(video);
            return map;
        }
        return "";
    }

    @ResponseBody
    @RequestMapping("uploadVdieo")
    public void uploadVdieo(MultipartFile path, String id, HttpServletRequest request) {
        videoService.uploadVdieos(path, id, request);
    }
    @ResponseBody
    @RequestMapping("querySearch")
    public List<Video> querySearch(String content) {
        List<Video> videos = videoService.querySearchs(content);
        return videos;
    }
}
