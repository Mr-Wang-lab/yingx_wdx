package com.baizhi.wdx.dao;

import com.baizhi.wdx.entity.City;
import com.baizhi.wdx.entity.Month;
import com.baizhi.wdx.entity.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CityMapper extends Mapper<User> {


    List<City> queryBoyAndCity();
    List<City> queryGrilAndCity();
    List<Month> queryBoyCount();
    List<Month> queryGirlCount();


}