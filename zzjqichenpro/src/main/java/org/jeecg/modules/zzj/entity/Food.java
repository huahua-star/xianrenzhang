package org.jeecg.modules.zzj.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: mini吧
 * @Author: jeecg-boot
 * @Date:   2019-09-19
 * @Version: V1.0
 */
@Data
@TableName("qc_food")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_food对象", description="mini吧")
public class Food {
    
	/**mini吧*/
    @ApiModelProperty(value = "mini吧")
	private java.lang.Integer id;
	//商品id
	String tyId;
	/**价格*/
	@Excel(name = "价格", width = 15)
    @ApiModelProperty(value = "价格")
	private java.math.BigDecimal price;
	//房间号
	String roomnum;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
	private java.lang.String type;
	//商品数量
	Integer tyconut;
	//订单号
	String orderid;
	//状态
	Integer staus;













}
