package org.jeecg.modules.zzj.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.zzj.service.IklrecordService;
import org.jeecg.modules.zzj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

/**
 * 抛帐
 * 银联 支付宝 微信 城市挂账
 */
@Slf4j
@Api(tags = "抛帐")
@RestController
@RequestMapping("/zzj/ThebillaccountController")
public class ThebillaccountController {

    @Autowired
    private IklrecordService iklrecordService;

    /**
     * @param resrowid 预订单号
     * @param roomkey  房间号
     * @param Trncode  账项代码  0支付宝 , 1微信 , 2银联 , 3城市挂账
     * @param amount   结账金额
     * @return
     */
    @ApiOperation(value = "结账后抛帐")
    @RequestMapping(value = "/Pcurtain", method = RequestMethod.GET)
    public R Pcurtain(@RequestParam(name = "resrowid", required = true) String resrowid,
                      @RequestParam(name = "roomkey", required = true) String roomkey,
                      @RequestParam(name = "Trncode", required = true) Integer Trncode,
                      @RequestParam(name = "amount", required = true) String amount) {
        if (StringUtils.isEmpty(resrowid) & StringUtils.isEmpty(roomkey) & StringUtils.isEmpty(amount)) {
            return R.error("输入需要参数!");
        }
        if (Trncode.equals(0)) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("resrowid", resrowid);
            hashMap.put("roomkey", roomkey);
            hashMap.put("Trncode", 9140);
            hashMap.put("amount", amount);
            int ids = iklrecordService.insercurtains(hashMap);
        }
        if (Trncode.equals(1)) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("resrowid", resrowid);
            hashMap.put("roomkey", roomkey);
            hashMap.put("Trncode", 9130);
            hashMap.put("amount", amount);
            int ids = iklrecordService.insercurtains(hashMap);
        }
        if (Trncode.equals(2)) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("resrowid", resrowid);
            hashMap.put("roomkey", roomkey);
            hashMap.put("Trncode", 9120);
            hashMap.put("amount", amount);
            int ids = iklrecordService.insercurtains(hashMap);
        }
        if (Trncode.equals(3)) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("resrowid", resrowid);
            hashMap.put("roomkey", roomkey);
            hashMap.put("Trncode", 9030);
            hashMap.put("amount", amount);
            int ids = iklrecordService.insercurtains(hashMap);
        }
        return R.ok();
    }
}
