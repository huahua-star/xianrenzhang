package org.jeecg.modules.zzj.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.zzj.entity.PayMent;
import org.jeecg.modules.zzj.mapper.PayMentMapper;
import org.jeecg.modules.zzj.service.IPayMentService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@DS("multi-kailai")
public class PayMentServiceImpl extends ServiceImpl<PayMentMapper, PayMent> implements IPayMentService {

    @Override
    public List<PayMent> searchPayment() {
        return baseMapper.searchPayment();
    }
}
