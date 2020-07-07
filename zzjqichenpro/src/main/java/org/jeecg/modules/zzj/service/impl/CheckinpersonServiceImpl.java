package org.jeecg.modules.zzj.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.zzj.entity.Checkinperson;
import org.jeecg.modules.zzj.mapper.CheckinpersonMapper;
import org.jeecg.modules.zzj.service.ICheckinpersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 交易记录
 * @Author: jeecg-boot
 * @Date:   2019-11-02
 * @Version: V1.0
 */
@Service
public class CheckinpersonServiceImpl extends ServiceImpl<CheckinpersonMapper, Checkinperson> implements ICheckinpersonService {

    @Autowired
    private CheckinpersonMapper checkinpersonMapper;

}
