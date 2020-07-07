package org.jeecg.modules.zzj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.zzj.entity.DueToSource;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;

import java.util.List;
import java.util.Map;

public interface KaiLaiOrderMapper  extends BaseMapper<KaiLaiOrder> {
    int generateOrder(Map<String, String> map);

    List<KaiLaiOrder> getOrderByPhone(@Param("phone") String phone);

    KaiLaiOrder getOrderByOrderId(@Param("orderId") String orderId);

    int cancleOrder(@Param("orderId") String orderId);

    int checkInOrder(@Param("orderId") String orderId);

    int checkOutOrder(@Param("orderId") String orderId);

    int updateProfileByRid(Map<String, String> map);

    int createAccompany(Map<String, String> map);

    List<KaiLaiOrder> searchResvByIdcard(@Param("idNumber") String idNumber);

    List<KaiLaiOrder> searchByRoomNo(@Param("roomNo") String roomNo);

    int goonCheckIn(Map<String, String> map);

    int isCanGoonCheckIn(Map<String, String> map);

    List<KaiLaiOrder> searchResvByName(Map<String, String> map);

    int updateProfileByNameId(Map<String, String> map);

    int updateResvPayment(Map<String, String> map);

    KaiLaiOrder getOrderByCRSId(@Param("orderId") String  orderId);

    int updateResvMessage(Map<String, String> map);

    String searchTotalAmount(@Param("orderId") String  orderId);

    List<KaiLaiOrder> searchResvByLFName(Map<String, String> map);

    List<KaiLaiOrder> searchResvByBlockByID(@Param("blockId") String  blockId);

    int createResvAndBlockID(Map<String,String> map);

    String getRoomNo(@Param("orderId") String  orderId);
}
