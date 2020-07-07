package org.jeecg.modules.zzj.Thread;

import TTCEPackage.K7X0Util;
import org.springframework.beans.factory.annotation.Value;

public class RecipientCard extends Thread  {
    @Value("${sdk.ComHandle}")
    private Integer comHandle;

    public RecipientCard(){
    }
    public void run() {
        try {
            Thread.currentThread().sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("线程等待异常");
        }
        System.out.println("开始收卡");
        K7X0Util.regain();
    }
}
