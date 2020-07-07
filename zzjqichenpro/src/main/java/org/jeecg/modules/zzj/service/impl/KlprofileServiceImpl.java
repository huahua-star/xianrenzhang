package org.jeecg.modules.zzj.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.zzj.entity.Klprofile;
import org.jeecg.modules.zzj.entity.group;
import org.jeecg.modules.zzj.mapper.KlprofileMapper;
import org.jeecg.modules.zzj.service.IKlprofileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 客户档案表
 * @Author: jeecg-boot
 * @Date:   2019-10-28
 * @Version: V1.0
 */
@Service
@DS("multi-kailai")
public class KlprofileServiceImpl extends ServiceImpl<KlprofileMapper, Klprofile> implements IKlprofileService {

    @Autowired
    private KlprofileMapper klprofileMapper;

    @Override
    public List<group> queryteam(String nameid) {
        return klprofileMapper.queryteam(nameid);

    }

}
