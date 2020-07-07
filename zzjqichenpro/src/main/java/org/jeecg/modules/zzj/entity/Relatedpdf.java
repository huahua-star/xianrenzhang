package org.jeecg.modules.zzj.entity;


import lombok.Data;

/**
 * 账单pdf有关
 */
@Data
public class Relatedpdf {

    private String name;//客人姓名
    private String roomkey;//房间号码
    private String reach;//到店日期
    private String out;//离店日期


}
