package org.jeecg.modules.zzj.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 退房时打印该入住人的信息
 * @Author: jeecg-boot
 * @Date:   2019-11-02
 * @Version: V1.0
 */
@Data
@TableName("qc_check_in_person")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_check_in_person对象", description="交易记录")
public class Checkinperson {
    
	/**主键*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**入住人姓名*/
	@Excel(name = "入住人姓名", width = 15)
    @ApiModelProperty(value = "入住人姓名")
	private java.lang.String personName;
	/**入住人联系方式*/
	@Excel(name = "入住人联系方式", width = 15)
    @ApiModelProperty(value = "入住人联系方式")
	private java.lang.String personPhone;
	/**入住人身份证号*/
	@Excel(name = "入住人身份证号", width = 15)
    @ApiModelProperty(value = "入住人身份证号")
	private java.lang.String personIdCard;
	/**opera订单号*/
	@Excel(name = "opera订单号", width = 15)
    @ApiModelProperty(value = "opera订单号")
	private java.lang.String reservationNumber;
	/**opera系统房间号*/
	@Excel(name = "opera系统房间号", width = 15)
    @ApiModelProperty(value = "opera系统房间号")
	private java.lang.String operaNum;
	/**民族*/
	@Excel(name = "民族", width = 15)
    @ApiModelProperty(value = "民族")
	private java.lang.String personNation;
	/**陪同人数*/
	@Excel(name = "陪同人数", width = 15)
    @ApiModelProperty(value = "陪同人数")
	private java.lang.Integer personEscort;
	/**入住时间*/
	@Excel(name = "入住时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "入住时间")
	private java.util.Date checkInTime;
	/**离店时间*/
	@Excel(name = "离店时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "离店时间")
	private java.util.Date leaveTime;
	/**预订人0 同随人1*/
	@Excel(name = "预订人0 同随人1", width = 15)
    @ApiModelProperty(value = "预订人0 同随人1")
	private java.lang.Integer reserveType;
	/**客户号*/
	@Excel(name = "客户号", width = 15)
    @ApiModelProperty(value = "客户号")
	private java.lang.String customerNum;
	/**确认序列号*/
	@Excel(name = "确认序列号", width = 15)
    @ApiModelProperty(value = "确认序列号")
	private java.lang.String sequenceNum;
	/**LATA号*/
	@Excel(name = "LATA号", width = 15)
    @ApiModelProperty(value = "LATA号")
	private java.lang.String lataNum;
	/**称呼*/
	@Excel(name = "称呼", width = 15)
    @ApiModelProperty(value = "称呼")
	private java.lang.String call;
	/**邮箱*/
	@Excel(name = "邮箱", width = 15)
    @ApiModelProperty(value = "邮箱")
	private java.lang.String mail;
	/**地址*/
	@Excel(name = "地址", width = 15)
    @ApiModelProperty(value = "地址")
	private java.lang.String address;
	/**0男1女3未知*/
	@Excel(name = "0男1女3未知", width = 15)
    @ApiModelProperty(value = "0男1女3未知")
	private java.lang.Integer sex;
	/**预住天数*/
	@Excel(name = "预住天数", width = 15)
    @ApiModelProperty(value = "预住天数")
	private java.lang.String checkInDays;
	/**出生日期*/
	@Excel(name = "出生日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出生日期")
	private java.util.Date birthday;
	/**证件类型*/
	@Excel(name = "证件类型", width = 15)
    @ApiModelProperty(value = "证件类型")
	private java.lang.String documentType;
	/**卡的房间号*/
	@Excel(name = "卡的房间号", width = 15)
    @ApiModelProperty(value = "卡的房间号")
	private java.lang.String cardNum;
	/**导出*/
	@Excel(name = "导出", width = 15)
    @ApiModelProperty(value = "导出")
	private java.lang.Integer exports;
	/**查房 0 是未 查房 1 已查房*/
	@Excel(name = "查房 0 是未 查房 1 已查房", width = 15)
    @ApiModelProperty(value = "查房 0 是未 查房 1 已查房")
	private java.lang.Integer checkRoom;
	/**预授权状态1 完成 2 待支付 0撤销*/
	@Excel(name = "预授权状态1 完成 2 待支付 0撤销", width = 15)
    @ApiModelProperty(value = "预授权状态1 完成 2 待支付 0撤销")
	private java.lang.Integer preStatus;
	/**pdf名称*/
	@Excel(name = "pdf名称", width = 15)
    @ApiModelProperty(value = "pdf名称")
	private java.lang.String pdfUrl;
}
