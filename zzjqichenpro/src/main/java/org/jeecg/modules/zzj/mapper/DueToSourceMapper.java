package org.jeecg.modules.zzj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.zzj.entity.DueToSource;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;

import java.util.List;

public interface DueToSourceMapper extends BaseMapper<DueToSource> {
    List<DueToSource> searchSourceCode();
}
