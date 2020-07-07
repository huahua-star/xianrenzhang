package org.jeecg.modules.zzj.entity;

import java.io.Serializable;
import java.util.Date;
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
 * @Description: 离店信息
 * @Author: jeecg-boot
 * @Date:   2019-11-03
 * @Version: V1.0
 */
@Data
@TableName("qc_hotel_tips")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_hotel_tips对象", description="离店信息")
public class HotelTips {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**房间号*/
	@Excel(name = "房间号", width = 15)
    @ApiModelProperty(value = "房间号")
	private java.lang.String roomNum;
	/**查房状态（0未查1已查）*/
	@Excel(name = "查房状态（0未查1已查）", width = 15)
    @ApiModelProperty(value = "查房状态（0未查1已查）")
	private java.lang.Integer status;
	/**离店时间 */
	@Excel(name = "离店时间 ", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "离店时间 ")
	private java.util.Date nowTime;
	/**是否开发票(0未开 1开了)*/
	@Excel(name = "是否开发票(0未开 1开了)", width = 15)
    @ApiModelProperty(value = "是否开发票(0未开 1开了)")
	private java.lang.Integer whetherOpen;
	/**电子发票订单号*/
	@Excel(name = "电子发票订单号", width = 15)
    @ApiModelProperty(value = "电子发票订单号")
	private java.lang.String invoiceId;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
	private java.lang.String userName;
	/**入住时间*/
	@Excel(name = "入住时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "入住时间")
	private java.util.Date inTime;
	/**金额*/
	@Excel(name = "金额", width = 15)
    @ApiModelProperty(value = "金额")
	private java.lang.String money;
	/**是否有minibar消费*/
	@Excel(name = "是否有minibar消费", width = 15)
    @ApiModelProperty(value = "是否有minibar消费")
	private java.lang.String minibarType;
	/**reservationNumber*/
	@Excel(name = "reservationNumber", width = 15)
    @ApiModelProperty(value = "reservationNumber")
	private java.lang.String reservationNumber;
}
