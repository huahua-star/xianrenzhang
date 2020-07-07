package org.jeecg.modules.zzj.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;
import org.jeecg.modules.zzj.mapper.KaiLaiOrderMapper;
import org.jeecg.modules.zzj.service.IKaiLaiOrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 调用的mapper 均为 调用 凯莱给出的 存储过程
 */
@Service
public class IKaiLaiOrderServiceImpl extends ServiceImpl<KaiLaiOrderMapper, KaiLaiOrder> implements IKaiLaiOrderService {
    //切换数据源  为multi-kailai
    //生成预订单
    @DS("multi-kailai")
    @Override
    public int generateOrder(Map<String, String> map) {
        return baseMapper.generateOrder(map);
    }
    //通过手机号获取订单信息
    @DS("multi-kailai")
    @Override
    public List<KaiLaiOrder> getOrderByPhone(String phone) {
        return baseMapper.getOrderByPhone(phone);
    }
    //通过订单好查询订单信息
    @DS("multi-kailai")
    @Override
    public KaiLaiOrder getOrderByOrderId(String orderId) {
        return baseMapper.getOrderByOrderId(orderId);
    }
    //根据订单号撤销预订单
    @DS("multi-kailai")
    @Override
    public int cancleOrder(String orderId) {
        return baseMapper.cancleOrder(orderId);
    }
    //修改 订单状态为已入住状态 checkIn
    @DS("multi-kailai")
    @Override
    public int checkInOrder(String orderId) {
        return baseMapper.checkInOrder(orderId);
    }
    //修改 订单状态为退房（离店） 状态 checkOut
    @DS("multi-kailai")
    @Override
    public int checkOutOrder(String orderId) {
        return baseMapper.checkOutOrder(orderId);
    }
    //通过订单id  修改 订单信息  添加入住人
    @DS("multi-kailai")
    @Override
    public int updateProfileByRid(Map<String, String> map) {
        return baseMapper.updateProfileByRid(map);
    }
    @DS("multi-kailai")
    @Override
    public int createAccompany(Map<String, String> map) {
        return baseMapper.createAccompany(map);
    }
    @DS("multi-kailai")
    @Override
    public List<KaiLaiOrder> searchResvByIdcard(String idNumber) {
        return baseMapper.searchResvByIdcard(idNumber);
    }
    @DS("multi-kailai")
    @Override
    public List<KaiLaiOrder> searchByRoomNo(String roomNo) {
        return baseMapper.searchByRoomNo(roomNo);
    }
    @DS("multi-kailai")
    @Override
    public int goonCheckIn(Map<String, String> map) {
        return baseMapper.goonCheckIn(map);
    }
    @DS("multi-kailai")
    @Override
    public int isCanGoonCheckIn(Map<String, String> map) {
        return baseMapper.isCanGoonCheckIn(map);
    }
    @DS("multi-kailai")
    @Override
    public List<KaiLaiOrder> searchResvByName(Map<String, String> map) {
        return baseMapper.searchResvByName(map);
    }
    @DS("multi-kailai")
    @Override
    public int updateProfileByNameId(Map<String, String> map) {
        return baseMapper.updateProfileByNameId(map);
    }
    @DS("multi-kailai")
    @Override
    public int updateResvPayment(Map<String, String> map) {
        return baseMapper.updateResvPayment(map);
    }
    @DS("multi-kailai")
    @Override
    public KaiLaiOrder getOrderByCRSId(String orderId) {
        return baseMapper.getOrderByCRSId(orderId);
    }
    @DS("multi-kailai")
    @Override
    public int updateResvMessage(Map<String, String> map) {
        return baseMapper.updateResvMessage(map);
    }
    @DS("multi-kailai")
    @Override
    public String searchTotalAmount(String orderId) {
        return baseMapper.searchTotalAmount(orderId);
    }
    @DS("multi-kailai")
    @Override
    public List<KaiLaiOrder> searchResvByLFName(Map<String, String> map) {
        return baseMapper.searchResvByLFName(map);
    }
    @DS("multi-kailai")
    @Override
    public List<KaiLaiOrder> searchResvByBlockByID(String blockId) {
        return baseMapper.searchResvByBlockByID(blockId);
    }
    @DS("multi-kailai")
    @Override
    public int createResvAndBlockID(Map<String, String> map) {
        return baseMapper.createResvAndBlockID(map);
    }
    @DS("multi-kailai")
    @Override
    public String getRoomNo(String orderId) {
        return baseMapper.getRoomNo(orderId);
    }

}
