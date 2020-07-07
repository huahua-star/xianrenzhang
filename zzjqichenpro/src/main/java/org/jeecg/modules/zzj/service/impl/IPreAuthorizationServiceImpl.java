package org.jeecg.modules.zzj.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.zzj.entity.PreAuthorization;
import org.jeecg.modules.zzj.mapper.PreAuthorizationMapper;
import org.jeecg.modules.zzj.service.IKaiLaiRoomService;
import org.jeecg.modules.zzj.service.IPreAuthorizationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@DS("multi-kailai")
@Service
public class IPreAuthorizationServiceImpl extends ServiceImpl<PreAuthorizationMapper, PreAuthorization> implements IPreAuthorizationService {
    @Override
    public List<PreAuthorization> searchResvCardAmount(String orderId) {
        return baseMapper.searchResvCardAmount(orderId);
    }

    @Override
    public int insertCardAmount(Map<String, String> map) {
        return baseMapper.insertCardAmount(map);
    }

    @Override
    public int cancleCardAmount(Map<String, String> map) {
        return baseMapper.cancleCardAmount(map);
    }

    @Override
    public int payBillByCardAmount(Map<String, String> map) {
        return baseMapper.payBillByCardAmount(map);
    }
}
