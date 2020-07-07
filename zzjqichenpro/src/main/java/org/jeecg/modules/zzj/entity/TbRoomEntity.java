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
 * @Description: 房间费用
 * @Author: jeecg-boot
 * @Date:   2019-11-16
 * @Version: V1.0
 */
@Data
@TableName("qc_tb_room")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_tb_room对象", description="房间费用")
public class TbRoomEntity {
    
	/**主键*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**房间名称*/
	@Excel(name = "房间名称", width = 15)
    @ApiModelProperty(value = "房间名称")
	private java.lang.String roomName;
	/**面积(单位平米，保留小数两位)*/
	@Excel(name = "面积(单位平米，保留小数两位)", width = 15)
    @ApiModelProperty(value = "面积(单位平米，保留小数两位)")
	private java.lang.Double roomArea;
	/**最小楼层*/
	@Excel(name = "最小楼层", width = 15)
    @ApiModelProperty(value = "最小楼层")
	private java.lang.Integer minFloor;
	/**最大楼层*/
	@Excel(name = "最大楼层", width = 15)
    @ApiModelProperty(value = "最大楼层")
	private java.lang.Integer maxFloor;
	/**床型主键*/
	@Excel(name = "床型主键", width = 15)
    @ApiModelProperty(value = "床型主键")
	private java.lang.String bedTypeId;
	/**设施简介*/
	@Excel(name = "设施简介", width = 15)
    @ApiModelProperty(value = "设施简介")
	private java.lang.String facilityInfo;
	/**洗手间设备简介*/
	@Excel(name = "洗手间设备简介", width = 15)
    @ApiModelProperty(value = "洗手间设备简介")
	private java.lang.String wcInfo;
	/**是否含早餐(0:否,1:是)*/
	@Excel(name = "是否含早餐(0:否,1:是)", width = 15)
    @ApiModelProperty(value = "是否含早餐(0:否,1:是)")
	private java.lang.Integer isBreakfast;
	/**是否可取消(0:否,1:是)*/
	@Excel(name = "是否可取消(0:否,1:是)", width = 15)
    @ApiModelProperty(value = "是否可取消(0:否,1:是)")
	private java.lang.Integer isCancel;
	/**是否有床(0:否,1:是)*/
	@Excel(name = "是否有床(0:否,1:是)", width = 15)
    @ApiModelProperty(value = "是否有床(0:否,1:是)")
	private java.lang.Integer isWindow;
	/**是否吸烟(0:否,1:是)*/
	@Excel(name = "是否吸烟(0:否,1:是)", width = 15)
    @ApiModelProperty(value = "是否吸烟(0:否,1:是)")
	private java.lang.Integer isSmoke;
	/**房间的方向(0:南,1:北)*/
	@Excel(name = "房间的方向(0:南,1:北)", width = 15)
    @ApiModelProperty(value = "房间的方向(0:南,1:北)")
	private java.lang.Integer roomOrientation;
	/**最大人数*/
	@Excel(name = "最大人数", width = 15)
    @ApiModelProperty(value = "最大人数")
	private java.lang.Integer maxPeople;
	/**房型id*/
	@Excel(name = "房型id", width = 15)
    @ApiModelProperty(value = "房型id")
	private java.lang.String roomTypeId;
	/**房间费用(天)*/
	@Excel(name = "房间费用(天)", width = 15)
    @ApiModelProperty(value = "房间费用(天)")
	private java.math.BigDecimal money;
	/**房间号(房间标识)*/
	@Excel(name = "房间号(房间标识)", width = 15)
    @ApiModelProperty(value = "房间号(房间标识)")
	private java.lang.String roomNum;
	/**房间秘钥*/
	@Excel(name = "房间秘钥", width = 15)
    @ApiModelProperty(value = "房间秘钥")
	private java.lang.String roomKey;
	/**押金*/
	@Excel(name = "押金", width = 15)
    @ApiModelProperty(value = "押金")
	private java.math.BigDecimal deposit;
	/**起始时间(HH:MM:SS)*/
    @ApiModelProperty(value = "起始时间(HH:MM:SS)")
	private java.util.Date startTime;
	/**结束时间(HH:MM:SS)*/
    @ApiModelProperty(value = "结束时间(HH:MM:SS)")
	private java.util.Date endTime;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**状态(0关闭,1开启)*/
	@Excel(name = "状态(0关闭,1开启)", width = 15)
    @ApiModelProperty(value = "状态(0关闭,1开启)")
	private java.lang.Integer status;
	/**卡的房间号*/
	@Excel(name = "卡的房间号", width = 15)
    @ApiModelProperty(value = "卡的房间号")
	private java.lang.String cardNum;
}
