package org.jeecg.modules.zzj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.zzj.entity.PayMent;

import java.util.List;

public interface PayMentMapper extends BaseMapper<PayMent> {
    List<PayMent> searchPayment();
}
