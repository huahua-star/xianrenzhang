package org.jeecg.modules.zzj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.zzj.entity.DueToSource;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;

import java.util.List;

public interface IDueToSourceService extends IService<DueToSource> {
    List<DueToSource> searchSourceCode();
}
