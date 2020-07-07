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
 * @Description: 发卡记录表
 * @Author: jeecg-boot
 * @Date:   2019-09-16
 * @Version: V1.0
 */
@Data
@TableName("qc_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_record对象", description="发卡记录表")
public class Record {
    
	/**表id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "表id")
	private java.lang.String id;
	/**房间号*/
	@Excel(name = "房间号", width = 15)
    @ApiModelProperty(value = "房间号")
	private java.lang.String roomnum;
	/**状态 0失败 1成功*/
	@Excel(name = "状态 0失败 1成功", width = 15)
    @ApiModelProperty(value = "状态 0失败 1成功")
	private java.lang.String state;
	/**pms 订单号 */
	@Excel(name = "pms订单号", width = 15)
	@ApiModelProperty(value = "pms订单号")
	private java.lang.String orderid;

	/*数量*/
	@Excel(name = "数量", width = 15)
	@ApiModelProperty(value = "数量")
	private java.lang.String number;

	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**评论时间*/
	@Excel(name = "评论时间", width = 15)
    @ApiModelProperty(value = "评论时间")
	private java.lang.String createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private java.lang.String updateBy;
	/**修改时间时间*/
	@Excel(name = "修改时间时间", width = 15)
    @ApiModelProperty(value = "修改时间时间")
	private java.lang.String updateTime;
}
