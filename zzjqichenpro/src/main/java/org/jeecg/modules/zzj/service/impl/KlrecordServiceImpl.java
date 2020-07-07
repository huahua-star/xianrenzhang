package org.jeecg.modules.zzj.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.zzj.entity.*;
import org.jeecg.modules.zzj.mapper.KlrecordMapper;
import org.jeecg.modules.zzj.service.IklrecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Repository
@Service
@DS("multi-kailai")
public class KlrecordServiceImpl extends ServiceImpl<KlrecordMapper, Klrecord> implements IklrecordService {

    @Autowired
    private KlrecordMapper klrecordMapper;


    @Override
    public List<Klbill> queryklbill(String roomkey) {
       List<Klbill>list= klrecordMapper.querylist(roomkey);
        return list;
    }

    @Override
    public List<Klrecord> querylist(String roomkey) {
        List<Klrecord> list1=klrecordMapper.queryklrecord(roomkey);
        return list1;
    }

    @Override
    public boolean updateresrowld(String roomkey) {
        return  klrecordMapper.updates(roomkey);
    }

    @Override
    public List<Checkupaccounts> querythrow(String resrowid) {
        return klrecordMapper.querythrow(resrowid);
    }

    @Override
    public int insertentry(Map<String, Object> map) {
        return klrecordMapper.insertentry(map);
    }

    @Override
    public int insercurtains(Map<String, Object> hashMap) {
        return klrecordMapper.insercurtains(hashMap);
    }

    @Override
    public List<Relatedpdf> querypdf1(String roomkey) {

        return klrecordMapper.querypdf1(roomkey);
    }

    @Override
    public List<Departuredetail> querylists(String roomkey) {
        return  klrecordMapper.querylists(roomkey);
    }

    @Override
    public List<Object> Queryentrycode() {
        return klrecordMapper.Queryentrycode();
    }

    @Override
    public List<Object> Querytheentrycode() {
        return  klrecordMapper.Querytheentrycode();
    }


}
