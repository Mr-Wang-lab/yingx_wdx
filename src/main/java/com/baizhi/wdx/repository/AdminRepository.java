package com.baizhi.wdx.repository;

import com.baizhi.wdx.entity.Admin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AdminRepository extends ElasticsearchRepository<Admin,String> {



}
