package com.baizhi.wdx;
import com.baizhi.wdx.dao.AdminDao;
import com.baizhi.wdx.dao.CityMapper;
import com.baizhi.wdx.dao.UserMapper;
import com.baizhi.wdx.entity.Admin;
import com.baizhi.wdx.entity.City;
import com.baizhi.wdx.entity.User;
import com.baizhi.wdx.entity.UserExample;
import com.baizhi.wdx.service.UserService;
import com.baizhi.wdx.vo.CityInfoVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YingxWdxApplicationTests {

    @Resource
    UserMapper userMapper;
    @Resource
    AdminDao adminDao;
    @Resource
    CityMapper cityMapper;

    @Resource
    UserService userService;
    @Test
   public void contextLoads() {
        Admin admin = adminDao.queryByUsername("admin");
        System.out.println(admin);
    }

    @Test
    public void contextLoads3() {
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo("2");
       /* List<User> users = userMapper.selectByExample(example);
        users.forEach(user -> System.out.println(user));*/
        User user = new User();

        user.setUsername("duduu");
        user.setStatus("2");
        userMapper.updateByExample(user,example);
    }
    @Test
    public void contextLoads4() {
        List<User> users = userMapper.selectAll();
        users.forEach(user -> System.out.println(user));
    }

    @Test
    public void contextLoads5() {
        List<City> cities = cityMapper.queryBoyAndCity();
        for (City city : cities) {
            System.out.println(city);
        }
    }
    @Test
    public void contextLoads11() {
        List<CityInfoVo> cityInfoVos = userService.userMap();
        for (CityInfoVo cityInfoVo : cityInfoVos) {
            System.out.println(cityInfoVo);
        }
    }

}
