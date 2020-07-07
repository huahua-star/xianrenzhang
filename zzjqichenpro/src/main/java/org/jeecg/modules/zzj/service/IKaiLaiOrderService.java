package org.jeecg.modules.zzj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.zzj.entity.DueToSource;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;

import java.util.List;
import java.util.Map;

public interface IKaiLaiOrderService  extends IService<KaiLaiOrder> {
    int generateOrder(Map<String, String> map);
    List<KaiLaiOrder> getOrderByPhone(String phone);
    KaiLaiOrder getOrderByOrderId(String orderId);
    int cancleOrder(String orderId);
    int checkInOrder(String orderId);
    int checkOutOrder(String orderId);
    int updateProfileByRid(Map<String, String> map);
    int createAccompany(Map<String, String> map);
    List<KaiLaiOrder> searchResvByIdcard(String idNumber);
    List<KaiLaiOrder> searchByRoomNo(String roomNo);
    int goonCheckIn(Map<String, String> map);
    int isCanGoonCheckIn(Map<String, String> map);
    List<KaiLaiOrder> searchResvByName(Map<String, String> map);
    int updateProfileByNameId(Map<String, String> map);
    int updateResvPayment(Map<String, String> map);
    KaiLaiOrder getOrderByCRSId(String orderId);
    int updateResvMessage(Map<String, String> map);
    String searchTotalAmount(String  orderId);
    List<KaiLaiOrder> searchResvByLFName(Map<String, String> map);
    List<KaiLaiOrder> searchResvByBlockByID(String blockId);
    int createResvAndBlockID(Map<String,String> map);
    String getRoomNo(String  orderId);
}
