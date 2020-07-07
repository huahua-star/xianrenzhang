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
 * @Description: 发卡及写卡档案表
 * @Author: jeecg-boot
 * @Date:   2019-11-02
 * @Version: V1.0
 */
@Data
@TableName("qc_wrrecord")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_wrrecord对象", description="发卡及写卡档案表")
public class Wrrecord {
    
	/**主键*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**成功0失败1*/
	@Excel(name = "成功0失败1", width = 15)
    @ApiModelProperty(value = "成功0失败1")
	private java.lang.Integer recordType;
	/**idCardNum*/
	@Excel(name = "idCardNum", width = 15)
    @ApiModelProperty(value = "idCardNum")
	private java.lang.String idCardNum;
	/**name*/
	@Excel(name = "name", width = 15)
    @ApiModelProperty(value = "name")
	private java.lang.String name;
	/**reserveId*/
	@Excel(name = "reserveId", width = 15)
    @ApiModelProperty(value = "reserveId")
	private java.lang.String reserveId;
	/**roomNum*/
	@Excel(name = "roomNum", width = 15)
    @ApiModelProperty(value = "roomNum")
	private java.lang.String roomNum;
}
