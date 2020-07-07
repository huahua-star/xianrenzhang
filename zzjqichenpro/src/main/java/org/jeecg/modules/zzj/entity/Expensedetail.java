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
 * @Description: 退房客户消费明细
 * @Author: jeecg-boot
 * @Date:   2019-11-18
 * @Version: V1.0
 */
@Data
@TableName("qc_expensedetail")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_expensedetail对象", description="退房客户消费明细")
public class Expensedetail {
    
	/**预定单号*/
	@Excel(name = "预定单号", width = 15)
    @ApiModelProperty(value = "预定单号")
	private String resrowld;
	/**交易日期*/
	@Excel(name = "交易日期", width = 15)
    @ApiModelProperty(value = "交易日期")
	private String transactionDate;
	/**消费金额*/
	@Excel(name = "消费金额", width = 15)
    @ApiModelProperty(value = "消费金额")
     String price;
	/**消费字段分类*/
	@Excel(name = "消费字段分类", width = 15)
    @ApiModelProperty(value = "消费字段分类")
	private String transactioncode;
	/**消费描述*/
	@Excel(name = "消费描述", width = 15)
    @ApiModelProperty(value = "消费描述")
	private String trndescription;
}
