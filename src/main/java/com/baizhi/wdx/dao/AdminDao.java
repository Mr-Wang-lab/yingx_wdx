package com.baizhi.wdx.dao;

import com.baizhi.wdx.entity.Admin;

public interface AdminDao {
    Admin queryByUsername(String username);
}
