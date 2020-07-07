package org.jeecg.modules.zzj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.zzj.entity.ProFile;

import java.util.Map;

public interface IProFileService extends IService<ProFile> {
    ProFile searchProfileByNameID(Map<String,String> map);
}
