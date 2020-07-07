package org.jeecg.modules.zzj.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.zzj.entity.KaiLaiRoom;
import org.jeecg.modules.zzj.mapper.KaiLaiRoomMapper;
import org.jeecg.modules.zzj.service.IKaiLaiRoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@DS("multi-kailai")
@Service
public class IKaiLaiRoomServiceImpl extends ServiceImpl<KaiLaiRoomMapper, KaiLaiRoom> implements IKaiLaiRoomService {

    @Override
    public List<KaiLaiRoom> SelectKaiLaiRoom(Map<String,String> map) {
        return baseMapper.SelectKaiLaiRoom(map);
    }

    @Override
    public KaiLaiRoom SelectRoomNameByType(String type) {
        return baseMapper.SelectRoomNameByType(type);
    }

    @Override
    public List<KaiLaiRoom> HelpSearch(Map<String, String> map) {
        return baseMapper.HelpSearch(map);
    }
}
