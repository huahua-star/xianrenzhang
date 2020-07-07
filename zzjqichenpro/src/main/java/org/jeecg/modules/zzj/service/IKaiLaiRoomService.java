package org.jeecg.modules.zzj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.zzj.entity.KaiLaiRoom;

import java.util.List;
import java.util.Map;

public interface IKaiLaiRoomService extends IService<KaiLaiRoom> {
    List<KaiLaiRoom> SelectKaiLaiRoom(Map<String, String> map);
    KaiLaiRoom SelectRoomNameByType(String type);
    List<KaiLaiRoom> HelpSearch(Map<String, String> map);
}
