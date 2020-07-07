package org.jeecg.modules.zzj.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.zzj.entity.klroom;
import org.jeecg.modules.zzj.mapper.klroomMapper;
import org.jeecg.modules.zzj.service.IklroomService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 凯莱酒店房间
 * @Author: jeecg-boot
 * @Date:   2019-10-25
 * @Version: V1.0
 */
@Service
@DS("multi-kailai")
public class klroomServiceImpl extends ServiceImpl<klroomMapper, klroom> implements IklroomService {

}
