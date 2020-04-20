package com.baizhi.wdx.vo;

import com.baizhi.wdx.entity.City;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityInfoVo {

    private String title;

    private List<City> citys;
}
