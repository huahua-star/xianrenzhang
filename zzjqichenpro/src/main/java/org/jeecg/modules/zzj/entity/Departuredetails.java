package org.jeecg.modules.zzj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @Description: 自助机离店明细
 * @Author: jeecg-boot
 * @Date: 2019-12-23
 * @Version: V1.0
 */
@Data
@TableName("qc_departuredetail")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "qc_departuredetail对象", description = "自助机离店明细")
public class Departuredetails {

    /**
     * 姓名
     */
    @Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    String chinesename;
    /**
     * 到店日期
     */
    @Excel(name = "到店日期", width = 15)
    @ApiModelProperty(value = "到店日期")
    String reach;
    /**
     * 离店日期
     */
    @Excel(name = "离店日期", width = 15)
    @ApiModelProperty(value = "离店日期")
    String tui;
    /**
     * 房間號
     */
    @Excel(name = "房間號", width = 15)
    @ApiModelProperty(value = "房間號")
    private String roomkey;
    /**
     * 房间数
     */
    @Excel(name = "房间数", width = 15)
    @ApiModelProperty(value = "房间数")
    private String roomcounter;
    /**
     * 间夜数
     */
    @Excel(name = "间夜数", width = 15)
    @ApiModelProperty(value = "间夜数")
    private String nights;
    /**
     * 成人数
     */
    @Excel(name = "成人数", width = 15)
    @ApiModelProperty(value = "成人数")
    private String adults;
    /**
     * 房间数
     */
    @Excel(name = "房间数", width = 15)
    @ApiModelProperty(value = "房间数")
    private String roomtype;
    /**
     * 房间原价
     */
    @Excel(name = "房间原价", width = 15)
    @ApiModelProperty(value = "房间原价")
    private String ratenotdiscount;
    /**
     * 包价
     */
    @Excel(name = "包价", width = 15)
    @ApiModelProperty(value = "包价")
    private String pkgpay;
    /**
     * 余额
     */
    @Excel(name = "余额", width = 15)
    @ApiModelProperty(value = "余额")
    private String balance;
    /**
     * 市场代码
     */
    @Excel(name = "市场代码", width = 15)
    @ApiModelProperty(value = "市场代码")
    private String marketcode;
    /**
     * 大于0团队
     */
    @Excel(name = "大于0团队", width = 15)
    @ApiModelProperty(value = "大于0团队")
    private String blockid;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String message;
    /**
     * 预定状态
     */
    @Excel(name = "预定状态", width = 15)
    @ApiModelProperty(value = "预定状态")
    private String resvstatus;
    /**
     * 陪同的姓名
     */
    @Excel(name = "陪同的姓名", width = 15)
    @ApiModelProperty(value = "陪同的姓名")
    private String altname;
}
