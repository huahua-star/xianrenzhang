package org.jeecg.modules.zzj.service;

import org.jeecg.modules.zzj.entity.Food;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: mini吧
 * @Author: jeecg-boot
 * @Date:   2019-09-19
 * @Version: V1.0
 */
public interface IFoodService extends IService<Food> {

    List<Food> getList(String tionnumber);


}
