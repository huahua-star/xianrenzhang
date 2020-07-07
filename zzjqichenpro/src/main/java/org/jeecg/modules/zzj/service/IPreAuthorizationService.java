package org.jeecg.modules.zzj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.zzj.entity.PreAuthorization;

import java.util.List;
import java.util.Map;

public interface IPreAuthorizationService extends IService<PreAuthorization> {
    List<PreAuthorization> searchResvCardAmount(String orderId);
    int insertCardAmount(Map<String,String> map);
    int cancleCardAmount(Map<String,String> map);
    int payBillByCardAmount(Map<String,String> map);
}
