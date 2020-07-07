package org.jeecg.modules.zzj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.zzj.entity.Klprofile;
import org.jeecg.modules.zzj.entity.group;

import java.util.List;

/**
 * @Description: 客户档案表
 * @Author: jeecg-boot
 * @Date:   2019-10-28
 * @Version: V1.0
 */
public interface KlprofileMapper extends BaseMapper<Klprofile> {


    List<group> queryteam(String nameid);
}
