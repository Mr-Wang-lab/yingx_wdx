package com.baizhi.wdx.service;

import com.baizhi.wdx.entity.Category;

import java.util.HashMap;

public interface CategoryService {
    //查询一级类别
    HashMap<String,Object> queryByOnePage(Integer page,Integer rows);

    //查询一级类别对应的二级类别
    HashMap<String,Object> queryByTwoPage(Integer page,Integer rows,String parentId);

    HashMap<String,Object> delete(Category category);

    void add(Category category);

    void update(Category category);
}
