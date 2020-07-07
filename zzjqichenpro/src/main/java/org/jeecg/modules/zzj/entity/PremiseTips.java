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
 * @Description: 抛帐提醒
 * @Author: jeecg-boot
 * @Date:   2019-11-06
 * @Version: V1.0
 */
@Data
@TableName("qc_premise_tips")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_premise_tips对象", description="抛帐提醒")
public class PremiseTips {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**预定号*/
	@Excel(name = "预定号", width = 15)
    @ApiModelProperty(value = "预定号")
	private java.lang.String reservationNumber;
	/**房间号码*/
	@Excel(name = "房间号码", width = 15)
    @ApiModelProperty(value = "房间号码")
	private java.lang.String roomNumber;
	/**抛帐信息*/
	@Excel(name = "抛帐信息", width = 15)
    @ApiModelProperty(value = "抛帐信息")
	private java.lang.String premiseTips;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**记录状态*/
	@Excel(name = "记录状态", width = 15)
    @ApiModelProperty(value = "记录状态")
	private java.lang.Integer status;
}
