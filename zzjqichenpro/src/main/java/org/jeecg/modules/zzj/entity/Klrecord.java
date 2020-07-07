package org.jeecg.modules.zzj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.sql.Date;


//用户基本信息实体类
@TableName("qc_userinfo")
@Data
public class Klrecord {
    //中文姓名
    private String chinesename;
    //性别
    private String gender;
    //国籍
    private String nationality;
    //出生日期
    private Date birth;
    //证件种类
    private String identity;
    //签证有效期
    private Date validity;
    //证件号码(身份证号码)
    private String idnumber;
    //到店日期
    private Date reach;
    //离店日期
    private Date out;
    //房间号
    private String roomkey;
    //房费
    //private String roomrate;
    //手机号
    //private String phone;


}
