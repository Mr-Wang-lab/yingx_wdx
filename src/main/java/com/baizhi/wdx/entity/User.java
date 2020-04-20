package com.baizhi.wdx.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Table(name = "yx_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName ="yingx",type="user")
public class User implements Serializable {
    @Id
    @Excel(name = "Id")
    private String id;
    @Field(type = FieldType.Text,analyzer = "ik_max_wold",searchAnalyzer = "ik_max_wold")
    @Excel(name = "名字",width = 30,height = 30)
    private String username;
    @Excel(name = "性别",width = 30,height = 30)
    private String sex;
    @Excel(name = "城市",width = 30,height = 30)
    private String city;
    @Excel(name = "电话",width = 30,height = 30)
    private String phone;
    @Column(name = "head_img")
    @Excel(name = "头像",type =1,width = 30,height = 30)
    private String headImg;
    @Excel(name = "签名",width = 30,height = 30)
    private String sign;
    @Excel(name = "微信",width = 30,height = 30)
    private String wechat;
    @Excel(name = "状态",width = 30,height = 30)
    private String status;
    @Column(name = "create_date")
    @Excel(name = "创建日期",format = "yyyy-MM-dd",width = 30,height = 30)
    private Date createDate;

}