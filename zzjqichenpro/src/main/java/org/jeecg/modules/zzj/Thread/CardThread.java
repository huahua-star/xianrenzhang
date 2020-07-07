package org.jeecg.modules.zzj.Thread;

import TTCEPackage.K7X0Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class CardThread extends Thread {

    @Value("${sdk.ComHandle}")
    private Integer comHandle;

    public CardThread(){
    }
    public void run() {
        log.info("进入发卡方法,6秒后将自动发卡");
        try {
            Thread.currentThread().sleep(6000);
        } catch (InterruptedException e) {
            System.out.println("线程等待异常");
        }
        System.out.println("开始发卡");
        K7X0Util.sendCardToTake(comHandle);
    }
}
