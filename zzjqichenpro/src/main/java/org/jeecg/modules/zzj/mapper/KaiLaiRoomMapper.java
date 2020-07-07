package org.jeecg.modules.zzj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.zzj.entity.KaiLaiRoom;


import java.util.List;
import java.util.Map;

public interface KaiLaiRoomMapper extends BaseMapper<KaiLaiRoom> {
    List<KaiLaiRoom> SelectKaiLaiRoom(Map<String, String> map);
    KaiLaiRoom SelectRoomNameByType(@Param("type") String type);
    List<KaiLaiRoom> HelpSearch(Map<String, String> map);
}
