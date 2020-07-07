package org.jeecg.modules.zzj.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 写卡信息
 * @Author: jeecg-boot
 * @Date:   2019-10-02
 * @Version: V1.0
 */
@Data
@TableName("qc_sendrecord")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_sendrecord对象", description="写卡记录")
public class Sendrecord {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	String id;
	/**入住客户*/
	@Excel(name = "入住客户", width = 15)
    @ApiModelProperty(value = "入住客户")
	private java.lang.String name;
	/**身份证*/
	@Excel(name = "身份证", width = 15)
    @ApiModelProperty(value = "身份证")
	private java.lang.String idnumber;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
	private java.lang.String phonenum;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15)
	@ApiModelProperty(value = "创建时间")
	private Date createtime;
	/**房间唯一标识*/
	@Excel(name = "房间唯一标识", width = 15)
    @ApiModelProperty(value = "房间唯一标识")
	private java.lang.String outside;
	/**房间id*/
	@Excel(name = "房间id", width = 15)
    @ApiModelProperty(value = "房间id")
	private java.lang.Integer roomnumid;
	/**默认0 0失败 1成功*/
	@Excel(name = "默认0 0失败 1成功", width = 15)
    @ApiModelProperty(value = "默认0 0失败 1成功")
	private java.lang.Integer sendstatus;
	/**写卡唯一标示*/
	@Excel(name = "写卡唯一标示", width = 15)
    @ApiModelProperty(value = "写卡唯一标示")
	private java.lang.String cardnum;
	/**有效的*/
	@Excel(name = "有效的", width = 15)
    @ApiModelProperty(value = "有效的")
	private java.lang.String idval;
}
