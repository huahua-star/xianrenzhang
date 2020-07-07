package org.jeecg.modules.zzj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.zzj.entity.PreAuthorization;

import java.util.List;
import java.util.Map;

public interface PreAuthorizationMapper extends BaseMapper<PreAuthorization> {
    List<PreAuthorization> searchResvCardAmount(@Param("orderId")String orderId);
    int insertCardAmount(Map<String,String> map);
    int cancleCardAmount(Map<String,String> map);
    int payBillByCardAmount(Map<String,String> map);
}
