package org.jeecg.modules.zzj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.zzj.entity.*;

import java.util.List;
import java.util.Map;

public interface IklrecordService extends IService<Klrecord> {


    List<Klbill> queryklbill(String roomkey);

    List<Klrecord> querylist(String roomkey);

    boolean updateresrowld(String roomkey);

    List<Checkupaccounts> querythrow(String resrowid);

    int insertentry(Map<String, Object> map);

    int insercurtains(Map<String, Object> hashMap);

    List<Relatedpdf> querypdf1(String roomkey);

    List<Departuredetail> querylists(String roomkey);

    List<Object> Queryentrycode();

    List<Object> Querytheentrycode();

}
