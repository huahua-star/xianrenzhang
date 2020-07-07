package org.jeecg.modules.zzj.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.zzj.entity.ProFile;
import org.jeecg.modules.zzj.mapper.ProFileMapper;
import org.jeecg.modules.zzj.service.IProFileService;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
@DS("multi-kailai")
public class IProFileServiceImpl extends ServiceImpl<ProFileMapper, ProFile> implements IProFileService {
    @Override
    public ProFile searchProfileByNameID(Map<String, String> map) {
        return baseMapper.searchProfileByNameID(map);
    }
}
