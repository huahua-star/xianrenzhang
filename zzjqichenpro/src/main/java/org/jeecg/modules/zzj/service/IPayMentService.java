package org.jeecg.modules.zzj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.zzj.entity.PayMent;

import java.util.List;

public interface IPayMentService  extends IService<PayMent> {
    List<PayMent> searchPayment();
}
