package org.jeecg.modules.zzj.controller;

import com.alibaba.fastjson.JSON;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.scene.layout.BackgroundImage;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.zzj.entity.KaiLaiRoom;
import org.jeecg.modules.zzj.entity.PreAuthorization;
import org.jeecg.modules.zzj.service.IPreAuthorizationService;
import org.jeecg.modules.zzj.util.Card.SetResultUtil;
import org.jeecg.modules.zzj.util.CheckMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags="推送抓取凯莱数据库中预授权相关功能")
@RestController
@RequestMapping("/zzj/kailai/pre")
public class PreAuthorizationController {
    @Autowired
    private IPreAuthorizationService iPreAuthorizationService;

    /**
     * 抓取凯莱预授权信息
     * @param
     * @return
     */
    @AutoLog(value = "抓取凯莱预授权信息")
    @ApiOperation(value="抓取凯莱预授权信息-searchResvCardAmount", notes="抓取凯莱预授权信息-searchResvCardAmount")
    @GetMapping(value = "/searchResvCardAmount")
    public Result<Object> searchResvCardAmount(String orderId) throws Exception {
        Result<Object> result = new Result<Object>();
        if (StringUtil.isNullOrEmpty(orderId)){
            String message="缺少参数";
            SetResultUtil.setLackParamResult(result,message);
        }
        // 查询凯莱的预授权信息
        List<PreAuthorization> list=iPreAuthorizationService.searchResvCardAmount(orderId);
        if (null!=list&&list.size()>0){
            String returnString= JSON.toJSONString(list);
            SetResultUtil.setSuccessResult(result,"成功查询",returnString);
        }else{
            SetResultUtil.setSuccessResult(result,"无该订单的预授权信息");
        }
        return result;
    }

    /**
     * 推送凯莱预授权信息
     * @param
     * @return
     */
    @AutoLog(value = "推送凯莱预授权信息")
    @ApiOperation(value="推送凯莱预授权信息-insertCardAmount", notes="推送凯莱预授权信息-insertCardAmount")
    @GetMapping(value = "/insertCardAmount")
    public Result<Object> insertCardAmount(String orderId,String amount,String creditCard,String pad,String depositNo,
                                           String creditCardHolder,String creditCardNote,String creditCardExp) throws Exception {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("orderId",orderId);
        map.put("amount",amount);
        map.put("creditCard",creditCard);
        map.put("pad",pad);
        map.put("depositNo",depositNo);
        map.put("creditCardHolder",creditCardHolder);
        map.put("creditCardNote",creditCardNote);
        map.put("creditCardExp",creditCardExp);
        String message="";
        if (CheckMapUtil.checkMap(map)){
            message="缺少参数";
            SetResultUtil.setLackParamResult(result,message);
        }
        //返回值为1 是正确
        int returnKey=iPreAuthorizationService.insertCardAmount(map);
        if (returnKey==1){
            SetResultUtil.setSuccessResult(result,"成功推送预授权信息");
        }else{
            SetResultUtil.setErrorMsgResult(result,"推送失败");
        }
        return result;
    }

    /**
     * 解除凯莱预授权信息--可部分结算
     * @param
     * @return
     */
    @AutoLog(value = "解除预授权信息")
    @ApiOperation(value="解除预授权信息-cancleCardAmount", notes="解除凯莱预授权信息-cancleCardAmount")
    @GetMapping(value = "/cancleCardAmount")
    public Result<Object> cancleCardAmount(String orderId,String amount) throws Exception {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("orderId",orderId);
        map.put("amount",amount);
        String message="";
        if (CheckMapUtil.checkMap(map)){
            message="缺少参数";
            SetResultUtil.setLackParamResult(result,message);
        }
        //返回值为1 是正确
        int returnKey=iPreAuthorizationService.cancleCardAmount(map);
        if (returnKey==1){
            SetResultUtil.setSuccessResult(result,"成功结算预授权信息");
        }else{
            SetResultUtil.setErrorMsgResult(result,"结算失败");
        }
        return result;
    }
    /**
     * 抓取预授权总金额
     *
     * @param
     * @return
     */
    @AutoLog(value = "抓取预授权总金额")
    @ApiOperation(value="抓取预授权总金额", notes="抓取预授权总金额")
    @GetMapping(value = "/getAllPreAmout")
    public Result<Object> getAllPreAmout(String orderId) throws Exception {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("orderId",orderId);
        String message="";
        if (CheckMapUtil.checkMap(map)){
            message="缺少参数";
            SetResultUtil.setLackParamResult(result,message);
        }
        List<PreAuthorization> list=iPreAuthorizationService.searchResvCardAmount(orderId);
        if (null!=list&&list.size()>0){
            BigDecimal allAmout=new BigDecimal("0.00");
            for(PreAuthorization preAuthorization : list){
                allAmout=allAmout.add(new BigDecimal(preAuthorization.getAmount()));
            }
            SetResultUtil.setSuccessResult(result,"成功查询",allAmout);
        }else{
            SetResultUtil.setSuccessResult(result,"无该订单的预授权信息");
        }
        return result;
    }

}
