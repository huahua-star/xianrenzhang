package org.jeecg.modules.zzj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class Departuredetail {
    String roomkey;//房间号
    String roomcounter;//房间数
    String nights;//间夜数
    String adults;//成人数
    String roomtype;//房间数
    String ratenotdiscount;//房间原价
    String pkgpay;//包价
    String balance;//余额
    String marketCode;//市场代码
    String blockid;//大于0团队
    String message;//备注
    String resvstatus;//预定状态
    String altname;//陪同的姓名

}
