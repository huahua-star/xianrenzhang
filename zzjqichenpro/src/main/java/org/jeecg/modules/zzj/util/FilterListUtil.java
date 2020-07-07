package org.jeecg.modules.zzj.util;

import com.alibaba.fastjson.JSON;
import com.alipay.api.domain.KbdishInfo;
import io.netty.util.internal.StringUtil;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;
import org.jeecg.modules.zzj.entity.KaiLaiRoom;
import org.jeecg.modules.zzj.util.Card.SetResultUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FilterListUtil {

    public static List<KaiLaiOrder> filterList(List<KaiLaiOrder> list) throws ParseException {
        List<KaiLaiOrder> resultList=new ArrayList<>();
        if (list.size()>0){
            for(KaiLaiOrder k : list){
                Date nowDate=new Date();
                Date checkInDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(k.getActualCheckInDate());
                Date checkOutDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(k.getActualCheckOutDate());
                if (checkInDate.before(nowDate)&&nowDate.before(checkOutDate)){
                    k.setIsFlag("1");
                }else{
                    k.setIsFlag("0");
                }
                if (!k.getResvStatus().equalsIgnoreCase("CHECKOUT")&&!k.getResvStatus().equalsIgnoreCase("CANCEL")&&!k.getResvStatus().equalsIgnoreCase("NOSHOW")){
                    resultList.add(k);
                }
            }
        }
        return resultList;
    }
    //人数小于等于1的订单
    public static List<KaiLaiOrder> filterListNumber(List<KaiLaiOrder> list) throws ParseException {
        List<KaiLaiOrder> resultList=new ArrayList<>();
        if (list.size()>0) {
            for (KaiLaiOrder kaiLaiOrder : list){
                if (StringUtil.isNullOrEmpty(kaiLaiOrder.getAccompanyId())) {
                    resultList.add(kaiLaiOrder);
                }
            }
        }
        return resultList;
    }
    //未入住的订单
    public static List<KaiLaiOrder> filterListNumberZero(List<KaiLaiOrder> list) throws ParseException {
        List<KaiLaiOrder> resultList=new ArrayList<>();
        if (list.size()>0) {
            for (KaiLaiOrder kaiLaiOrder : list){
                if (StringUtil.isNullOrEmpty(kaiLaiOrder.getAccompanyId())&& "0".equals(kaiLaiOrder.getNameId())) {
                    resultList.add(kaiLaiOrder);
                }
            }
        }
        return resultList;
    }
    //人数等于1的订单
    public static List<KaiLaiOrder> filterListNumberOne(List<KaiLaiOrder> list) throws ParseException {
        List<KaiLaiOrder> resultList=new ArrayList<>();
        if (list.size()>0) {
            for (KaiLaiOrder kaiLaiOrder : list){
                if (StringUtil.isNullOrEmpty(kaiLaiOrder.getAccompanyId())&&!"0".equals(kaiLaiOrder.getNameId())) {
                    resultList.add(kaiLaiOrder);
                }
            }
        }
        return resultList;
    }

    public static List<KaiLaiOrder> fiterCheckStateChinese(List<KaiLaiOrder> list){
        Map<String,String> map=new HashMap<>();
        map.put("CHECKIN","已入住");
        map.put("DUEIN","预抵");
        List<KaiLaiOrder> resultList=new ArrayList<>();
        if (list.size()>0){
            for (KaiLaiOrder kaiLaiOrder : list){
                kaiLaiOrder.setCheckInState(map.get(kaiLaiOrder.getResvStatus()));
                resultList.add(kaiLaiOrder);
            }
        }
        return resultList;
    }

    public static String conversion(String conversion){
        Map<String,String> map=new HashMap<>();
        map.put("ALIPAY","0");
        map.put("WXPAY","1");
        map.put("YLPAY","2");
        return map.get(conversion);
    }


}
