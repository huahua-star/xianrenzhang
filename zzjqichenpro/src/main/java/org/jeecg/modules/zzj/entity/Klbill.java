package org.jeecg.modules.zzj.entity;

import lombok.Data;
import java.sql.Date;

@Data
public class Klbill {

    //预订单号
    private String resrowld;

    //具体那天的房费房费
    private Date transactionDate;

    //总消费  BigDecimal
    private String price;

    // 5100的是迷你吧消费
    private String transactioncode;

    //消费描述
    private String trndescription;



}
