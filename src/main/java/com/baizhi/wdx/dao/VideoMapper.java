package com.baizhi.wdx.dao;

import com.baizhi.wdx.entity.Video;
import com.baizhi.wdx.entity.VideoExample;
import java.util.List;

import com.baizhi.wdx.po.VideoPo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface VideoMapper extends Mapper<Video> {

    List<VideoPo>  queryByReleaseTime();
}