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
 * @Description: 早餐表
 * @Author: jeecg-boot
 * @Date:   2019-11-03
 * @Version: V1.0
 */
@Data
@TableName("qc_breakfast")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_breakfast对象", description="早餐表")
public class breakfast {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**入住人姓名*/
	@Excel(name = "入住人姓名", width = 15)
    @ApiModelProperty(value = "入住人姓名")
	private java.lang.String hotelname;
	/**对应的字段*/
	@Excel(name = "对应的字段", width = 15)
    @ApiModelProperty(value = "对应的字段")
	private java.lang.String brkfast;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
	private java.util.Date starttime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
	private java.util.Date endtime;
	/**对应楼层*/
	@Excel(name = "对应楼层", width = 15)
    @ApiModelProperty(value = "对应楼层")
	private java.lang.String hotelfloor;
}
