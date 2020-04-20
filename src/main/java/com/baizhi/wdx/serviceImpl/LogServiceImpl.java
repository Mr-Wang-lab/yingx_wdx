package com.baizhi.wdx.serviceImpl;

import com.baizhi.wdx.annotation.AddCache;
import com.baizhi.wdx.dao.LogMapper;
import com.baizhi.wdx.entity.Log;
import com.baizhi.wdx.entity.UserExample;
import com.baizhi.wdx.service.LogService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class LogServiceImpl implements LogService {
    @Resource
    LogMapper logMapper;

    @AddCache
    @Override
    public HashMap<String, Object> queryByPage(Integer page, Integer rows) {
        System.out.println("page::++"+page);

        HashMap<String, Object> map = new HashMap<>();

        //封装数据
        //总条数   records
        UserExample example = new UserExample();
        Integer records = logMapper.selectCountByExample(example);
        map.put("records", records);
        Integer records1 = (Integer) map.get("records");
        System.out.println("records1;;;"+records1);
        //总页数   total   总条数/每页展示条数  是否有余数
        Integer total = records % rows == 0 ? records / rows : records / rows + 1;
        map.put("total", total);

        //当前页   page
        map.put("page", page);

        //数据     rows
        //参数  忽略条数,获取几条
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<Log> logs = logMapper.selectByRowBounds(new Log(), rowBounds);
        map.put("rows", logs);

    //    Integer rows1 = (Integer) map.get("rows");
        System.out.println("rows1: "+map.get("rows"));
        return map;
    }
}
