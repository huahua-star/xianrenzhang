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
 * @Description: 房间类型
 * @Author: jeecg-boot
 * @Date:   2019-09-16
 * @Version: V1.0
 */
@Data
@TableName("qc_roomtype")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_roomtype对象", description="房间类型")
public class RoomType {
    
	/**表id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "表id")
	private java.lang.String id;
	/**房间类型名称*/
	@Excel(name = "房间类型名称", width = 15)
    @ApiModelProperty(value = "房间类型名称")
	private java.lang.String name;
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
	/**状态 0 删除 1 正常*/
	@Excel(name = "状态 0 删除 1 正常", width = 15)
    @ApiModelProperty(value = "状态 0 删除 1 正常")
	private java.lang.String state;
}
