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
 * @Description: 交易记录
 * @Author: jeecg-boot
 * @Date:   2019-11-02
 * @Version: V1.0
 */
@Data
@TableName("qc_transaction_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_transaction_record对象", description="交易记录")
public class TransactionRecord {
    
	/**主键*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**金额*/
	@Excel(name = "金额", width = 15)
    @ApiModelProperty(value = "金额")
    String money;
	/**支付宝订单号*/
	@Excel(name = "支付宝订单号", width = 15)
    @ApiModelProperty(value = "支付宝订单号")
	private java.lang.String outTradeNo;
	/**op系统订单号*/
	@Excel(name = "op系统订单号", width = 15)
    @ApiModelProperty(value = "op系统订单号")
	private java.lang.String reservationNumber;
	/**房间号*/
	@Excel(name = "房间号", width = 15)
    @ApiModelProperty(value = "房间号")
	private java.lang.String roomNum;
	/**交易类型*/
	@Excel(name = "交易类型", width = 15)
    @ApiModelProperty(value = "交易类型")
	private java.lang.String reservationType;
	/**状态(0:成功,1:失败)*/
	@Excel(name = "状态(0:成功,1:失败)", width = 15)
    @ApiModelProperty(value = "状态(0:成功,1:失败)")
	private java.lang.Integer status;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**是否是预授权 0普通支付 1预授权支付*/
	@Excel(name = "是否是预授权 0普通支付 1预授权支付", width = 15)
    @ApiModelProperty(value = "是否是预授权 0普通支付 1预授权支付")
	private java.lang.Integer asAuthorize;
	/**实际支付金额*/
	@Excel(name = "实际支付金额", width = 15)
    @ApiModelProperty(value = "实际支付金额")
	private java.math.BigDecimal actualMoney;
	/**预授权状态1 完成 2 待支付 0撤销*/
	@Excel(name = "预授权状态1 完成 2 待支付 0撤销", width = 15)
    @ApiModelProperty(value = "预授权状态1 完成 2 待支付 0撤销")
	private java.lang.Integer revokeStatus;
	/**结算时间或者撤销时间*/
	@Excel(name = "结算时间或者撤销时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结算时间或者撤销时间")
	private java.util.Date completeTime;
	/**导出*/
	@Excel(name = "导出", width = 15)
    @ApiModelProperty(value = "导出")
	private java.lang.Integer exports;
}
