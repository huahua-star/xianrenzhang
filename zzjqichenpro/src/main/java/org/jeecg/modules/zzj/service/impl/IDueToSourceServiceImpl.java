package org.jeecg.modules.zzj.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.zzj.entity.DueToSource;
import org.jeecg.modules.zzj.mapper.DueToSourceMapper;
import org.jeecg.modules.zzj.service.IDueToSourceService;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public class IDueToSourceServiceImpl extends ServiceImpl<DueToSourceMapper, DueToSource> implements IDueToSourceService {

    @DS("multi-kailai")
    @Override
    public List<DueToSource> searchSourceCode() {
        return baseMapper.searchSourceCode();
    }
}
