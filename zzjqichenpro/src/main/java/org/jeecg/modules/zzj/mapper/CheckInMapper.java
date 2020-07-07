package org.jeecg.modules.zzj.mapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.zzj.entity.CheckIn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.sql.Wrapper;
import java.util.List;

/**
 * @Description: 入住功能
 * @Author: jeecg-boot
 * @Date:   2019-09-16
 * @Version: V1.0
 */
public interface CheckInMapper extends BaseMapper<CheckIn> {
    @Select("select * from qc_checkin")
    public List<CheckIn> getAll();

    public List<CheckIn> xmlgetAll();
}
