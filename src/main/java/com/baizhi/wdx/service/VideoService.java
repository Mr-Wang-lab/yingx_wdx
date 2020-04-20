package com.baizhi.wdx.service;


import com.baizhi.wdx.entity.Video;
import com.baizhi.wdx.vo.VideoVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public interface VideoService {

    //检索
    List<Video> querySearch(String content);

    List<Video> querySearchs(String content);

    HashMap<String, Object> queryByPage(Integer page, Integer rows);

    String add(Video video);

    void update(Video video);

    void uploadVdieo(MultipartFile path, String id, HttpServletRequest request);

    void uploadVdieos(MultipartFile path, String id, HttpServletRequest request);

    HashMap<String, Object> delete(Video video);

    List<VideoVo> queryByReleaseTime();

}
