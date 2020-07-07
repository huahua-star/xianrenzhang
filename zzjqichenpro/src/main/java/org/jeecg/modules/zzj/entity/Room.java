package org.jeecg.modules.zzj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 房间roomController
 * @Author: jeecg-boot
 * @Date:   2019-09-16
 * @Version: V1.0
 */
@Data
@TableName("qc_room")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_room对象", description="房间roomController")
public class Room {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**房间号*/
	@Excel(name = "房间号", width = 15)
    @ApiModelProperty(value = "房间号")
	private java.lang.String roomnum;
	/**房间类型表id 外键*/
	@Excel(name = "房间类型表id 外键", width = 15)
    @ApiModelProperty(value = "房间类型表id 外键")
	private java.lang.Integer typeid;
	/**房间面积*/
	@Excel(name = "房间面积", width = 15)
    @ApiModelProperty(value = "房间面积")
	private java.lang.String area;
	/**房间卡号*/
	@Excel(name = "房间卡号", width = 15)
    @ApiModelProperty(value = "房间卡号")
	private java.lang.String card;
	/**超时时间*/
	@Excel(name = "超时时间", width = 15)
    @ApiModelProperty(value = "超时时间")
	private java.lang.String timeout;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15)
    @ApiModelProperty(value = "创建时间")
	private java.lang.String createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private java.lang.String updateBy;
	/**修改时间*/
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	//房间秘钥
	String roomkey;

}
