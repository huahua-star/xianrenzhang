package org.jeecg.modules.zzj.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.zzj.entity.Klreservationname;
import org.jeecg.modules.zzj.mapper.KlreservationnameMapper;
import org.jeecg.modules.zzj.service.IKlreservationnameService;
import org.springframework.stereotype.Service;

/**
 * @Description: 预订单号
 * @Author: jeecg-boot
 * @Date:   2019-10-28
 * @Version: V1.0
 */
@Service
@DS("multi-kailai")
public class KlreservationnameServiceImpl extends ServiceImpl<KlreservationnameMapper, Klreservationname> implements IKlreservationnameService {

}
