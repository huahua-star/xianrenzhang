package org.jeecg.modules.zzj.mapper;

import java.util.List;
import org.jeecg.modules.zzj.entity.Food;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: mini吧
 * @Author: jeecg-boot
 * @Date:   2019-09-19
 * @Version: V1.0
 */
public interface FoodMapper extends BaseMapper<Food> {

    List<Food> selectList(String tionnumber);
}
