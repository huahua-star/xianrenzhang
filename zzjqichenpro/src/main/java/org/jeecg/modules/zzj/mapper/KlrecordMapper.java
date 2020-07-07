package org.jeecg.modules.zzj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.zzj.entity.*;

import java.util.List;
import java.util.Map;


public interface KlrecordMapper extends BaseMapper<Klrecord> {


    List<Klbill> querylist(String roomkey);

    List<Klrecord> queryklrecord(String roomkey);

    boolean updates(String roomkey);

    List<Checkupaccounts> querythrow(String resrowid);

    int insertentry(Map<String, Object> map);

    int insercurtains(Map<String, Object> hashMap);

    List<Relatedpdf> querypdf1(String roomkey);

    List<Departuredetail> querylists(String roomkey);

    List<Object> Queryentrycode();

    List<Object> Querytheentrycode();
}
