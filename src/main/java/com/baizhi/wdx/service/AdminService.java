package com.baizhi.wdx.service;

import com.baizhi.wdx.entity.Admin;

import java.util.HashMap;

public interface AdminService {
    HashMap<String, Object> login(Admin admin, String enCode);
}
