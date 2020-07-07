package org.jeecg.modules.zzj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("PreAuthorization")
@ApiModel(value="PreAuthorization对象", description="预授权信息对象")
public class PreAuthorization {
    /*订单号*/
    private String resvRowId;
    /*金额*/
    private String amount;
    /*信用卡号*/
    private String creditCard;
    /*预授权号*/
    private String pad;
    /*押金号*/
    private String depositNo;
    /*持卡人*/
    private String creditCardHolder;
    /*备注信息*/
    private String creditCardNote;
    //有效期
    private String creditCardExp;
}
