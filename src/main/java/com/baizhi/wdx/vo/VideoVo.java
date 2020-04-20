package com.baizhi.wdx.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoVo {

    private String id;
    private String videoTitle;
    private String description;
    private String path;
    private String cover;
    private Date uploadTime;
    private Integer likeCount;

    private String cateName;

    private String headImg;
}
