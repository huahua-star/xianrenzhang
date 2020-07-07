package org.jeecg.modules.zzj.service.impl;

import org.jeecg.modules.zzj.entity.Food;
import org.jeecg.modules.zzj.mapper.FoodMapper;
import org.jeecg.modules.zzj.service.IFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;

/**
 * @Description: mini吧
 * @Author: jeecg-boot
 * @Date:   2019-09-19
 * @Version: V1.0
 */
@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements IFoodService {

    @Autowired
    private FoodMapper foodMapper;

    @Override
    public List<Food> getList(String tionnumber) {
        return foodMapper.selectList(tionnumber);
    }
}
