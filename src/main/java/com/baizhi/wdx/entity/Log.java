package com.baizhi.wdx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Table(name ="yx_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log implements Serializable {
    private String id;

    private String adminname;
//"yyyy-MM-dd"
    @DateTimeFormat()
    private Date data;

    private String operation;

    private String status;


}