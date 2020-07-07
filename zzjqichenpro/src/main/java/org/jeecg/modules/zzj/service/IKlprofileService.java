package org.jeecg.modules.zzj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.zzj.entity.Klprofile;
import org.jeecg.modules.zzj.entity.group;

import java.util.List;

/**
 * @Description: 客户档案表
 * @Author: jeecg-boot
 * @Date:   2019-10-28
 * @Version: V1.0
 */
public interface IKlprofileService extends IService<Klprofile> {

    List<group> queryteam(String nameid);
}
