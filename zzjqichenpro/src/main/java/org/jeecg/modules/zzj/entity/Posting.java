package org.jeecg.modules.zzj.entity;

import lombok.Data;
//入账
@Data
public class Posting {

    private String resrowid;//预定号
    private String roomkey;//房间号
    private String TransactionCode;//账项代码
    private String quantity;//数量
    private String unitprice;//单价
    private String subcode;//subcode计算公式：预定号+房间号+时分秒
    private String username;//客人姓名


}
