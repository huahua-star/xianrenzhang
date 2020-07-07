package org.jeecg.modules.zzj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.zzj.common.ReturnCode;
import org.jeecg.modules.zzj.common.ReturnMessage;
import org.jeecg.modules.zzj.common.umsips;
import org.jeecg.modules.zzj.entity.TblTxnp;
import org.jeecg.modules.zzj.service.ITblTxnpService;
import org.jeecg.modules.zzj.util.CheckMapUtil;
import org.jeecg.modules.zzj.util.Pay.PreOccupancyUtil;
import org.jeecg.modules.zzj.util.Card.SetResultUtil;
import org.jeecg.modules.zzj.util.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Api(tags="支付相关功能")
@RestController
@RequestMapping("/zzj/pay")
public class PayController {
    @Autowired
    private ITblTxnpService tblTxnpService;
    static int iRet;
    static char[] strMemo = new char[1024];

    public final static String nonce_str = UuidUtils.getUUID();

    @Value("${qrDir}")
    private String qrDir;
    @Value("${alipay.apikey}")
    private String apikey;


    @AutoLog(value = "获取二维码")
    @ApiOperation(value="获取二维码", notes="获取二维码")
    @RequestMapping(value = "/getQrImage")
    public void getQrImage(String filePath,HttpServletResponse response) {
        log.info("进入getQrImage()方法filePath:{}", filePath);
        response.setContentType("image/png");
        try {
            FileCopyUtils.copy(new FileInputStream(filePath), response.getOutputStream());
            /*log.info("根据filePath获取到二维码图片");
            FileInputStream inputStream = new FileInputStream(filePath);
            int i = inputStream.available();
            byte[] buff = new byte[i];
            inputStream.read(buff);

            OutputStream out = response.getOutputStream();
            out.write(buff);
            System.out.println(new String(buff));
            // 关闭响应输出流
            out.close();
            inputStream.close();*/
        } catch (FileNotFoundException e) {
            log.error("getQrImage()方法出现异常:{}", e.getMessage());
        } catch (IOException e) {
            log.error("getQrImage()方法出现异常:{}", e.getMessage());
        } catch (Exception e) {
            log.error("getQrImage()方法出现异常:{}", e.getMessage());
        }
    }
    @AutoLog(value = "删除二维码")
    @ApiOperation(value="删除二维码", notes="删除二维码")
    @RequestMapping(value = "/deleteQrImage")
    public void deleteQrImage(String filePath) {
        log.info("deleteQrImage()方法filePath:{}", filePath);
        try {
            log.info("根据filePath删除文件");
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        } catch (Exception e) {
            log.error("deleteQrImage()方法出现异常:{}", e.getMessage());
        }
    }
    /**
     * 预授权支付二维码
     *
     * @param payType
     * @param amount
     * @param roomNum
     * @param reservationNumber
     * @return
     */
    @AutoLog(value = "让用户预授权支付二维码")
    @ApiOperation(value="让用户预授权支付二维码", notes="预授权支付二维码")
    @RequestMapping(value = "/empowerpay")
    public Result<TblTxnp> empowerpay(HttpServletResponse response, Integer payType, String amount, String roomNum, String reservationNumber) {

        System.out.println("预订单号:"+reservationNumber);
        System.out.println("支付金额:"+amount);
        System.out.println("支付类型:"+payType); //payType 0 支付宝 1微信
        System.out.println("房间号:"+roomNum);

        Result<TblTxnp> result = new Result<TblTxnp>();
        log.info("empowerpay()方法payType:amount:{}roomNum:{}reservationNumber:{}", payType, amount, roomNum, reservationNumber);
        try {
            String outTradeNo = UUID.randomUUID().toString().replace("-", "");
            TblTxnp tbl = new TblTxnp();
            log.info("判断金额是否为零");
            if (amount.equals("0") || amount == "0") {
                log.info("empowerpay()方法执行结束return:{}", ReturnCode.postSuccess, ReturnMessage.moneyZeroError);
                result.setCode(ReturnCode.postSuccess);
                result.setMessage(ReturnMessage.moneyZeroError);
                return result;
            }
            //预授权Util
            PreOccupancyUtil empower = new PreOccupancyUtil();
            log.info("金额不为空，生成二维码(生成成功！)");
            Map qrcode = null;
            qrcode = empower.qrcode(payType,amount,qrDir,apikey,nonce_str);
            System.out.println(qrcode+"----------------------11111----------------");
            //}
            //tbl.setId(UuidUtils.getUUID());
            tbl.setPreamount(new BigDecimal(amount));
            tbl.setOrderid(outTradeNo);//自己生产的订单号
            tbl.setPreOrderid(reservationNumber);
            tbl.setPaymethod(payType.toString());
            log.info("empowerpay()方法执行结束return:{}",ReturnCode.postSuccess);
            /* Map resultMap = new HashMap();
            resultMap.put("outTradeNo", outTradeNo);
            resultMap.put("filePath", "C:/qrImage/qr-20190529154229714.png");
            return R.ok(resultMap);*/
            
            if(qrcode!=null){
                //支付宝返回的订单号
                tbl.setOrderid((String) qrcode.get("outTradeNo"));
                tbl.setRoomnum(roomNum);
                tbl.setState("1");
                tbl.setPaytype("1");//预授权支付
                tbl.setAmount(new BigDecimal("0"));
                tblTxnpService.save(tbl);
                result.setCode(ReturnCode.postSuccess);
                result.setMessage(ReturnMessage.success);
                log.info("empowerpay()方法执行结束return:{}",result);
                result.setSuccess(true);
                result.setMap(qrcode);
                return result;
            }
            result.setCode(ReturnCode.parameterError);
            result.setMessage(ReturnMessage.logicError);
            log.error("empowerpay()方法执行结束return:{}",result);
            result.setSuccess(false);
            return result;
        } catch (Exception e) {
            log.error("empowerpay()方法出现异常:{}", e.getMessage());
            result.setCode(ReturnCode.parameterError);
            result.setMessage(ReturnMessage.logicError);
            result.setSuccess(false);
            return result;
        }
    }


    @AutoLog(value = "预授权支付状态轮询")
    @ApiOperation(value="预授权支付状态轮询", notes="预授权支付状态轮询")
    @RequestMapping(value = "/query_order")
    public Result<TblTxnp> query_order(HttpServletResponse response,Integer payType, String outTradeNo, String roomNum) {
        log.info("进入query_order()方法payType:{}outTradeNo:{}", payType, outTradeNo);
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            PreOccupancyUtil empower = new PreOccupancyUtil();
            log.info("查询支付状态");
            TblTxnp tbl = tblTxnpService.getOne(new QueryWrapper<TblTxnp>().eq("orderid",outTradeNo));
            String qrcode = empower.query_order(payType,outTradeNo,apikey,nonce_str);
            JSONObject jsonObj = JSON.parseObject(qrcode);
            String message = jsonObj.get("message").toString();
            System.out.println("message:"+message);
            if(message == "SUCCESS" || message.equals("SUCCESS")) {
                String paystatus = jsonObj.get("paystatus").toString();
                System.out.println("jsonObj:"+jsonObj);
                //paystatus 1支付成功 0待付款 2付款失败
                if(paystatus == "1" ||paystatus.equals("1")){
                    tbl.setState("2");
                    tbl.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                }else if(paystatus == "0" ||paystatus.equals("0")){
                    return SetResultUtil.setErrorMsgResult(result,ReturnMessage.forPay);
                }else{
                    return SetResultUtil.setErrorMsgResult(result,ReturnMessage.payFalse);
                }
            }else{
                return SetResultUtil.setErrorMsgResult(result,ReturnMessage.payFalse);
            }

            //}
            //正式环境删除for 直接解开 上面的就可用
            /*for (int i = 0; i < personList.size(); i++) {
                personList.get(i).setStatus(0);
                personList.get(i).setCompleteTime(new Date());
            }
             System.out.println(personList);
             */

            log.info("检测到支付成功修改支付状态");
            tblTxnpService.updateById(tbl);
            return SetResultUtil.setSuccessResult(result,ReturnMessage.success,tbl);
        } catch (Exception e) {
            log.error("query_order()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result);
        }
    }

    @AutoLog(value = "预授权微信条码支付状态轮询")
    @ApiOperation(value="预授权支付状态轮询", notes="预授权支付状态轮询")
    @RequestMapping(value = "/query_orderWeChatCode")
    public Result<TblTxnp> query_orderWeChatCode(HttpServletResponse response,Integer payType, String outTradeNo, String roomNum) {
        log.info("进入query_order()方法payType:{}outTradeNo:{}", payType, outTradeNo);
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            PreOccupancyUtil empower = new PreOccupancyUtil();
            log.info("查询支付状态");
            TblTxnp tbl = tblTxnpService.getOne(new QueryWrapper<TblTxnp>().eq("orderid",outTradeNo));
            String qrcode = empower.query_orderWeChartCode(payType,outTradeNo,apikey,nonce_str);
            JSONObject jsonObj = JSON.parseObject(qrcode);
            String message = jsonObj.get("message").toString();
            System.out.println("message:"+message);
            if(message == "SUCCESS" || message.equals("SUCCESS")) {
                String paystatus = jsonObj.get("paystatus").toString();
                System.out.println("jsonObj:"+jsonObj);
                //paystatus 1支付成功 0待付款 2付款失败
                if(paystatus == "1" ||paystatus.equals("1")){
                    tbl.setState("2");
                    tbl.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                }else if(paystatus == "0" ||paystatus.equals("0")){
                    return SetResultUtil.setErrorMsgResult(result,ReturnMessage.forPay);
                }else{
                    return SetResultUtil.setErrorMsgResult(result,ReturnMessage.payFalse);
                }
            }else{
                return SetResultUtil.setErrorMsgResult(result,ReturnMessage.payFalse);
            }

            //}
            //正式环境删除for 直接解开 上面的就可用
            /*for (int i = 0; i < personList.size(); i++) {
                personList.get(i).setStatus(0);
                personList.get(i).setCompleteTime(new Date());
            }
             System.out.println(personList);
             */

            log.info("检测到支付成功修改支付状态");
            tblTxnpService.updateById(tbl);
            return SetResultUtil.setSuccessResult(result,ReturnMessage.success,tbl);
        } catch (Exception e) {
            log.error("query_order()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result);
        }
    }



    /**
     * 查询是否超额
     *
     * @param payType
     * @param outTradeNo
     * @return
     */
    @AutoLog(value = "查询是否超额")
    @ApiOperation(value="查询是否超额", notes="查询是否超额")
    @RequestMapping(value = "/query_excess")
    public Result<TblTxnp> query_excess(Integer payType, String outTradeNo, String amount) {

        System.out.println("支付订单号:"+outTradeNo);
        System.out.println("已经付款的方式:"+payType);
        System.out.println("已用金额:"+amount);

        log.info("进入query_excess()payType:{}outTradeNo:{}amount:{}", payType, outTradeNo, amount);
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("查询要支付金额是否超额");
            PreOccupancyUtil empower = new PreOccupancyUtil();
            String qrcode = empower.query_order(payType, outTradeNo, apikey, nonce_str);
            JSONObject jsonObj = JSON.parseObject(qrcode);
            String paymoney = jsonObj.get("paymoney").toString();//订单的金额
            log.info("获取冻结金额");
            Double a = Double.parseDouble(paymoney);//冻结金额
            System.out.println("冻结金额:"+a);
            log.info("获取已用金额");
            Double b = Double.parseDouble(amount);//已用金额
            System.out.println("已用金额:"+b);
            BigDecimal p1 = new BigDecimal(paymoney);
            BigDecimal p2 = new BigDecimal(amount);
            log.info("判断已用金额是否大于冻结金额");
            if (a >= b) {
                log.info("未超额query_excess()方法结束return:{}", R.ok(p1.subtract(p2) + ""));
                result.setMessage(p1.subtract(p2)+"");
                result.setCode(ReturnCode.postSuccess);
                return result;//未超额
            } else {
                log.info("超额query_excess()方法结束return:{}", SetResultUtil.setResult(result,ReturnMessage.preExcess+":"+p1.subtract(p2),ReturnCode.preExcess,null,true));
                System.out.println("超出金额:"+(p1.subtract(p2)+""));
                return SetResultUtil.setResult(result,ReturnMessage.preExcess+":"+p1.subtract(p2)+"",ReturnCode.preExcess,null,true);//超额
            }
        } catch (Exception e) {
            log.error("query_excess()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setResult(result,ReturnMessage.Exception,ReturnCode.erorDateBase,null,false);//异常
        }
    }

    /**
     * 预授权完成
     * 支付的订单金额，执行完成预授权操作，订单金额打款至商家账户
     *
     * @param amount            余额
     * @param reservationNumber opera预定号
     * @return
     */
    @AutoLog(value = "预授权完成")
    @ApiOperation(value="预授权完成", notes="预授权完成")
    @RequestMapping(value = "/finish_order")
    public Result<TblTxnp> finish_order(HttpServletResponse response,String amount, String reservationNumber) {
        log.info("进入finish_order()方法amount:{}reservationNumber:{}", amount,reservationNumber);
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            String finish;
            if (amount == null || amount.equals("")) {
                return SetResultUtil.setResult(result,ReturnMessage.moneyZeroError,ReturnCode.lackParameter,null,false);
            }
            //查询预授权信息
            TblTxnp tblTxnps = tblTxnpService
                    .getOne(new QueryWrapper<TblTxnp>().eq("pre_orderid", reservationNumber));

            if (tblTxnps == null ) {
                return SetResultUtil.setResult(result,ReturnMessage.orderNotFount,ReturnCode.parameterError,null,false);
            }

            log.info("判断消费金额是否大于预授权金额");
            Double amounts = Double.parseDouble(amount);
            log.info("需要结算的金额amounts:{}" , amounts);
            Double money = Double.parseDouble(tblTxnps.getAmount().toString());
            log.info("预授权金额money:{}" , money);
            if (amounts > money) {
                BigDecimal b1 = new BigDecimal(Double.toString(money));
                BigDecimal b2 = new BigDecimal(Double.toString(amounts));
                log.info("超出预授权金额amounts:{}" ,b2.subtract(b1).doubleValue());
                return SetResultUtil.setResult(result,ReturnMessage.preExcess,ReturnCode.preExcess,null,true);//超额
            } else {
                log.info("结算金额amount:{}",amounts);
                int payType = Integer.parseInt(tblTxnps.getPaymethod());
                log.info("判断支付类型reservationType:{}",payType);
                /*if (reservationType == "nuion" || reservationType.equals("nuion")) {
                    BillsDemoController bills = new BillsDemoController();
                     Double amounts = Double.parseDouble(amount) * 100;
                    String totalFee = amounts.intValue() + "";
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String nowTime = sdf.format(personLists.get(0).getCreateTime());
                    Map donePay =  bills( response, totalFee, personLists.get(0).getOutTradeNo(), mid, tid, msgSrc,
                             instMid, nowTime, key, APIurl);
                    String yesPay = (String) donePay.get("secureStatus");
                    //ALL_COMPLETED 担保完成，PARTLY_COMPLETED 担保部分完成
                    if(yesPay =="UNCOMPLETED" ||yesPay.equals("UNCOMPLETED")){
                        //担保未完成	担保未完成的交易不允许直接做反交易
                        return R.error("订单未完成");
                    }
                    if(yesPay =="CANCELED" ||yesPay.equals("CANCELED")){
                        //担保未完成	担保未完成的交易不允许直接做反交易
                        return R.error("订单已撤销无法结算");
                    }
                }*/
                if(payType==0){//银联插卡
                    String cpInReq ;
                    money = Double.parseDouble(amount) * 100;
                    String qian = String.format("%012d", money.intValue());
                    // 订单号前六位是授权号 七位到13后几位是 银行卡号
                    String outTradeNo =tblTxnps.getOrderid();
                    log.info("预授权订单号outTradeNo:{}",outTradeNo);
                    String warrantyNo = outTradeNo.substring(0, 6);//授权号
                    String thistime = outTradeNo.substring(6, 12);//交易时间
                    String card = outTradeNo.substring(12);//银行卡
                    char[] strMemo = new char[1024 - card.length()];
                    for (int j =  0; j < 1024-card.length(); j++) {
                        strMemo[j] = ' ';
                    }
                    String str = String.valueOf(strMemo);
                    cpInReq = "00000000" + "11111111" + "10"
                            + qian+ "333333" +  thistime + "555555555555"
                            + warrantyNo + "777777" + card+str + "888";
                    log.info("设置入参");
                    iRet = umsips.instanceDll.UMS_SetReq(cpInReq);
                    if(iRet!=0){
                        log.error("UMS_SetReq()结束参数传入失败iRet:{}",iRet);
                        return SetResultUtil.setResult(result,ReturnMessage.parameterFailure,ReturnCode.parameterError,null,false);
                    }
                }

                //链接外网时候使用：正式结算
                if(payType<=1){
                    PreOccupancyUtil r = new PreOccupancyUtil();
                    finish = r.finish_order(payType,tblTxnps.getOrderid(),amounts.toString(),apikey,nonce_str);
                    if(!checkPreResult(finish)){
                        log.error("finish_order()方法结束return:{}","结算失败，500");
                        return SetResultUtil.setResult(result,ReturnMessage.prePayFailure,ReturnCode.erorDateBase,null,false);
                    }
                }
            }
            //修改预授权为已结算
            tblTxnps.setState("2");
            log.info("检测到结算成功添加实际支付金额");
            tblTxnps.setAmount(new BigDecimal(amount));
            /*            personLists.get(0).setCompleteTime(new Date());*/
            Boolean upperson =  tblTxnpService.updateById(tblTxnps);
            log.info("finish_order()方法结束");
            return SetResultUtil.setResult(result,ReturnMessage.success,ReturnCode.postSuccess,tblTxnps,true);
        } catch (Exception e) {
            log.error("finish_order()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setResult(result,ReturnMessage.Exception,ReturnCode.erorDateBase,null,false);//异常
        }
    }
    /**
     *  检测预授权结算情况
     * @param finish 预授权返回的json字符串
     * @return
     */
    private boolean checkPreResult(String finish){
        if(finish == null){
            log.error("finish空");
            return false;
        }
        JSONObject jsonObj = JSON.parseObject(finish);
        String status = jsonObj.get("status").toString();
        if(status != "10000" && !status.equals("10000")){
            log.error("结算失败");
            return false;
        }
        log.info("结算成功");
        return true;
    }
    /**
     * 预授权撤销
     *
     * @param payType
     * @param outTradeNo
     * @param amount
     * @param reservationNumber
     * @return
     */
    @AutoLog(value = "预授权撤销")
    @ApiOperation(value="预授权撤销", notes="预授权撤销")
    @RequestMapping(value = "/cancel_order")
    public Result<TblTxnp> cancel_order(Integer payType, String outTradeNo, String amount, String reservationNumber) {

        System.out.println("冻结的金额:"+amount);
        System.out.println("支付单号:"+outTradeNo);
        System.out.println("付款类型:"+payType);
        System.out.println("预订单号:"+reservationNumber);

        log.info("进入cancel_order()方法");
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("根据outTradeNo撤销订单");
            PreOccupancyUtil r = new PreOccupancyUtil();
            String cancel = r.cancel_order(payType, outTradeNo, apikey, nonce_str);
            JSONObject jsonObj = JSON.parseObject(cancel);
            String status = jsonObj.get("status").toString();
            log.info("判断是否撤销成功");
            if (status == "10000" || status.equals("10000")) {
                TblTxnp tblTxnp = tblTxnpService.getOne(new QueryWrapper<TblTxnp>().eq("orderid", outTradeNo));
                tblTxnp.setState("0");
                tblTxnp.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                tblTxnp.setAmount(new BigDecimal(amount));
                log.info("检测到支付成功添加实际支付金额");
                log.info("修改入住信息中的预授权字段");
                tblTxnpService.updateById(tblTxnp);
                log.info("cancel_order()方法结束return:{}", "成功撤销。");
                return SetResultUtil.setResult(result,ReturnMessage.success,ReturnCode.postSuccess,tblTxnp,true);
            }
            log.info("cancel_order()方法结束return:{}","撤销错误");
            return SetResultUtil.setResult(result,ReturnMessage.revocationFailure,ReturnCode.erorDateBase,null,false);
        } catch (Exception e) {
            log.error("cancel_order()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setResult(result,ReturnMessage.Exception,ReturnCode.erorDateBase,null,false);//异常
        }
    }

    //-------------------------------------------------------------


    /**
     * 第一步
     * 银联预授权 与 预授权完成
     * @param amount
     * @param dealType  0普通支付 1创建预授权订单 支付类型
     * @param orderId 订单号
     * @param roomNum 房间号
     * @return
     */
    @RequestMapping(value = "/UMS_Init")
    private Result<Object> UMS_SetReq(String amount,String dealType,String orderId,String roomNum){
        Result<Object> result = new Result<Object>();
        log.info("UMS_SetReq()进入amount:{}dealType:{}reservationNumber{}roomNum{}",amount,dealType,orderId,roomNum);
        try {
            Map<String,String> map= new HashMap<>();
            map.put("amount",amount);
            map.put("dealType",dealType);
            map.put("orderId",orderId);
            map.put("roomNum",roomNum);
            //Todo  需要 金额 amount 转成 000000000001格式  单位为分
            if(!CheckMapUtil.checkMap(map)){
                return SetResultUtil.setLackParamResult(result,"缺少参数");
            }
            //初始化
            log.info("初始化");
            iRet = UMS_Init();
            log.info("进入UMS_Init()方法");
            if(iRet!=0){
                log.error("UMS_Init(),UMS_SetReq()方法结束初始化失败");
                return SetResultUtil.setErrorMsgResult(result,"初始化失败");
            }
            log.info("UMS_Init()结束");
            //银行卡的卡号#加上空格凑够1024
            for (int i = 0; i < 1024; i++) {
                strMemo[i] = ' ';
            }
            Date thisTime = new Date();
            SimpleDateFormat fmt=new SimpleDateFormat("YYYYMMDD");
            String cpInReq ;
            //0普通支付 1创建预授权订单
            log.info("根据dealType字段判断支付类型dealType:{}",dealType);
            String payForm ="00"; //普通支付
            if("1".equals(dealType)){
                //预授权支付
                log.info("添加预授权");
                payForm = "08";
            }
            log.info("设置入参");
            Double money = Double.parseDouble(amount) * 100;
            String qian = String.format("%012d", money.intValue());
            cpInReq = "00000000" + "11111111" + payForm
                    + qian + "333333" +  fmt.format(thisTime) + "555555555555"
                    + "666666" + "777777" + String.valueOf(strMemo) + "888";
            iRet = umsips.instanceDll.UMS_SetReq(cpInReq);
            if(iRet!=0){
                log.error("UMS_SetReq()结束参数传入失败iRet:{}",iRet);
                return SetResultUtil.setErrorMsgResult(result,"参数传入失败");
            }
            log.info("参数传入成功添加订单信息");
            /*TblTxnp tblTxnp=new TblTxnp();
            tblTxnp.setPreOrderid(orderId);
            String Orderid=UUID.randomUUID().toString().replace("-", "");//自己生成的用于支付的订单号
            tblTxnp.setOrderid(Orderid);
            tblTxnp.setRoomnum(roomNum);
            tblTxnp.setState("1");
            tblTxnp.setPaytype(dealType);
            tblTxnp.setPaymethod("2");
            if ("1".equals(dealType)){
                tblTxnp.setPreamount(new BigDecimal(amount));
                tblTxnp.setAmount(new BigDecimal(0));
            }else{
                tblTxnp.setAmount(new BigDecimal(amount));
                tblTxnp.setPreamount(new BigDecimal(0));
            }
            tblTxnpService.save(tblTxnp);*/
            log.info("UMS_SetReq()结束");
            if ("200".equals(UMS_EnterCard().getCode())){
                return SetResultUtil.setSuccessResult(result,"成功","success");
            }else{
                return UMS_EnterCard();
            }
        }catch (Exception e) {
            log.error("UMS_SetReq()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result,"UMS_SetReq()方法");
        }
    }

    /**
     * 进卡操作
     * @return
     */
    @RequestMapping(value = "/UMS_EnterCard")
    private Result<Object> UMS_EnterCard()  {
        Result<Object> result = new Result<Object>();
        try {
            log.info("进入UMS_EnterCard()方法");
            iRet = umsips.instanceDll.UMS_EnterCard();
            log.info("UMS_EnterCard:iRet:"+iRet);
            if(iRet>2){
                log.error("UMS_EnterCard()结束进卡操作失败iRet:{}",iRet);
                return SetResultUtil.setErrorMsgResult(result,"UMS_EnterCard()结束进卡操作失败");
            }
            log.error("UMS_EnterCard()结束");
            return SetResultUtil.setSuccessResult(result,"进卡成功");
        }catch (Exception e){
            log.error("UMS_EnterCard()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result,"UMS_SetReq()方法");
        }

    }

    /**
     * 第三部
     * 卡的位置
     * @return
     */
    @RequestMapping(value = "/UMS_CheckCard")
    public Result<Object> UMS_CheckCard() {
        Result<Object> result = new Result<Object>();
        try {
            log.info("进入UMS_CheckCard()方法");
            byte[] state_out = new byte[1];
            iRet = umsips.instanceDll.UMS_CheckCard(state_out);
            log.info("读取卡的位置iRet:{}",iRet);
            //TODO 有可能需要弹卡
            if (state_out[0] == 52) {
                log.error("UMS_CheckCard()卡在插卡器卡口位置");
                //UMS_EjectCard("卡在插卡器卡口位置,");
                return SetResultUtil.setErrorMsgResult(result,"UMS_CheckCard()结束卡在插卡器卡口位置");
            }else if (state_out[0] == 53) {
                log.error("UMS_CheckCard()结束未检测到卡");
                return SetResultUtil.setErrorMsgResult(result,"未检测到卡");
            }else if (state_out[0] == 56) {
                log.error("UMS_CheckCard()结束卡在电子现金感应器上");
                //UMS_EjectCard("卡在电子现金感应器上,");
                return SetResultUtil.setErrorMsgResult(result,"UMS_CheckCard()结束卡在电子现金感应器上");
            }else if (state_out[0] == 57){
                log.error("UMS_CheckCard()结束卡在非接联机感应器上");
                //UMS_EjectCard("卡在非接联机感应器上,");
                return SetResultUtil.setErrorMsgResult(result,"卡在非接联机感应器上");
            }
            log.info("UMS_CheckCard()结束");
            return SetResultUtil.setSuccessResult(result,"UMS_CheckCard()成功");
        } catch (Exception e){
            log.error("UMS_CheckCard()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result,"UMS_CheckCard()方法");
        }

    }


    /**
     * 第四步
     * 读取卡的信息
     * @return
     */
    @RequestMapping(value = "/UMS_ReadCard")
    public Result<Object> UMS_ReadCard() {
        Result<Object> result = new Result<Object>();
        try {
            log.info("进入UMS_ReadCard()方法");
            byte[] cpData = new byte[20];
            //读取卡的信息
            iRet = umsips.instanceDll.UMS_ReadCard(cpData);
            log.info("读取卡信息cpData:{}",new String(cpData));
            if(iRet>4){
                log.error("UMS_ReadCard()结束读取卡信息失败iRet:{}",iRet);
                UMS_EjectCard("读取卡信息失败");
                return SetResultUtil.setErrorMsgResult(result,"读取卡信息失败");
            }
            //开启密码键盘
            int keyboard = UMS_StartPin().getCode();// 200成功
            if(keyboard != 200){
                UMS_EjectCard("开启密码键盘失败,");
                return SetResultUtil.setErrorMsgResult(result,"开启密码键盘失败");
            }
            String returnCpData=new String(cpData);//读卡信息
            log.info("returnCpData:"+returnCpData);
            return SetResultUtil.setSuccessResult(result,"读卡成功");
        } catch (Exception e){
            log.error("UMS_ReadCard()方法出现异常:{}", e.getMessage());
            UMS_EjectCard("读卡信息接口出现异常,");
            return SetResultUtil.setExceptionResult(result,"读卡信息接口");
        }

    }

    /**
     * 轮训客户输入的密码
     * @return
     */
    @RequestMapping(value = "/UMS_GetOnePass")
    public Result<Object> UMS_GetOnePass() {
        Result<Object> result = new Result<Object>();
        log.info("轮训客户输入的密码");
        byte key_out[] = new byte[1];
        while (true) {
            iRet = umsips.instanceDll.UMS_GetOnePass(key_out);
            if (iRet != 0) {
                log.error("UMS_ReadCard()结束输入密码失败");
                return SetResultUtil.setErrorMsgResult(result,"UMS_ReadCard()结束输入密码失败");
            }
            if (key_out[0] == 13) {
                log.info("UMS_ReadCard()结束密码输入完成");
                break;
            } else if (key_out[0] == 2) {
                log.error("UMS_ReadCard()结束密码输入超时");
                //R.error(2,"密码输入超时");
                return SetResultUtil.setErrorMsgResult(result,"密码输入超时");
            } else if (key_out[0] == 27) {
                log.info("UMS_ReadCard()结束用户取消");
                break;
            } else if (key_out[0] == 8) {
                log.info("退格");
            }
            try {
                Thread.sleep(600);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            // RetCode.setText("启动加密返回:"+iRet+"");
        }
        return SetResultUtil.setSuccessResult(result,"轮询密码成功");
    }

    /**
     * 第五步
     * 获取密码
     */
    @RequestMapping(value = "/UMS_GetPin")
    public Result<Object> UMS_GetPin(String orderId) {
        Result<Object> result = new Result<Object>();
        try {
            log.info("进入UMS_GetOnePass()方法");
            iRet = umsips.instanceDll.UMS_GetPin();
            System.out.println("取密文返回:" + iRet);
            if (iRet != 0){
                log.error("UMS_GetOnePass()方法结束读取秘钥失败");
                UMS_EjectCard("读取秘钥失败,");
                return SetResultUtil.setErrorMsgResult(result,"读取秘钥失败");
            }
            log.info("UMS_GetOnePass()方法结束");
            return UMS_TransCard(orderId);
        }catch (Exception e){
            log.error("UMS_GetOnePass()方法出现异常:{}", e.getMessage());
            UMS_EjectCard("UMS_GetOnePass()方法异常,");
            return SetResultUtil.setExceptionResult(result,"UMS_GetPin");
        }
    }

    /**
     * 第六步
     * 交易
     * @return
     */
    public Result<Object> UMS_TransCard(String orderId) throws UnsupportedEncodingException {
        Result<Object> result = new Result<Object>();
        try {
            log.info("进入UMS_TransCard()方法orderId:{}",orderId);
            byte[] strReq = new byte[3000];
            byte[] strResp = new byte[3000];
            log.info("确认交易");
            iRet = umsips.instanceDll.UMS_TransCard(strReq, strResp);
            log.info("UMS_TransCard:iRet:"+iRet);
            String thisTime =  new String(subBytes(new String(strResp,"gbk").getBytes(), 90, 6));//交易时间
            String warrantyNo = new String(subBytes(new String(strResp,"gbk").getBytes(), 108, 6)); // 授权号
            String cardNo = new String(subBytes(new String(strResp,"gbk").getBytes(), 42, 20)); // 卡号
            String  zong = warrantyNo+thisTime+cardNo;
            log.info("thisTime:"+thisTime);
            log.info("warrantyNo:"+warrantyNo);
            log.info("cardNo:"+cardNo);
            log.info("zong:"+zong);
            /*TblTxnp tblTxnp=tblTxnpService.getOne(new QueryWrapper<TblTxnp>().eq("pre"))
            List<TransactionRecordEntity> personTrList = transactionRecordService
                    .selectList(new EntityWrapper<TransactionRecordEntity>().eq("reservation_number", reservationNumber));
            if(zong!=null){
                personTrList.get(0).setOutTradeNo(zong);
            }
            log.info("修改订单中的订单号用于预授权结算");
            transactionRecordService.updateAllColumnBatchById(personTrList);*/
            log.info("调用弹卡操作");
            //交易成功弹卡
            UMS_EjectCard("");
            log.info("弹卡成功执行读卡器关闭操作");
            iRet = umsips.instanceDll.UMS_CardClose();
            if (iRet != 0){
                log.error("读卡器关闭失败UMS_TransCard()方法结束");
                return SetResultUtil.setErrorMsgResult(result,"读卡器关闭失败");
            }
            log.info("读卡器关闭成功UMS_TransCard()方法结束");
            return SetResultUtil.setSuccessResult(result,"交易成功",new String(strResp,"gbk"));
        } catch (Exception e){
            log.error("UMS_TransCard()方法出现异常:{}", e.getMessage());
            UMS_EjectCard("UMS_TransCard()方法出现异常,");
            return SetResultUtil.setExceptionResult(result,"UMS_TransCard");
        }
    }

    /**
     * 弹卡
     * @return
     */
    @RequestMapping(value = "/UMS_EjectCard")
    public Result<Object> UMS_EjectCard(String shibai) {
        Result<Object> result = new Result<Object>();
        iRet = umsips.instanceDll.UMS_EjectCard();
        if(iRet!=0){
            //TODO 可以加入吞卡
            log.error("弹卡失败调用吞卡操作，提醒入住人找前台那银行卡");
            iRet =  umsips.instanceDll.UMS_CardSwallow();
            if(iRet!=0){
                log.error("UMS_TransCard()方法结束吞卡失败");
                return SetResultUtil.setErrorMsgResult(result,"UMS_TransCard()方法结束吞卡失败");
            }
            log.error("UMS_TransCard()方法结束弹卡失败，已吞卡");
            return SetResultUtil.setErrorMsgResult(result,"UMS_TransCard()方法结束弹卡失败，已吞卡");
        }
        return SetResultUtil.setSuccessResult(result,shibai+"弹卡成功");
    }
    //初始化
    public int UMS_Init() {
        iRet = umsips.instanceDll.UMS_Init(1);
        return iRet;
    }

    /**
     * 开启密码键盘
     * @return
     */
    public Result<Object> UMS_StartPin() {
        Result<Object> result = new Result<Object>();
        log.info("进入UMS_StartPin()方法");
        iRet = umsips.instanceDll.UMS_StartPin();
        if(iRet!=0){
            log.error("UMS_StartPin()开启密码键盘失败iRet:{}",iRet);
            return SetResultUtil.setErrorMsgResult(result,"UMS_StartPin()开启密码键盘失败");
        }
        return SetResultUtil.setSuccessResult(result,"成功开启密码键盘");
    }

    /**
     * 读取返回信息
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    /**
     * 条码支付
     */
    @AutoLog(value = "条码支付")
    @ApiOperation(value="条码支付", notes="条码支付")
    @RequestMapping(value = "/barcodePay")
    public Result<Object> barcodePay(HttpServletResponse response, String amount,String orderId,String barcode,String roomNo) {

        System.out.println("预订单号:"+orderId);
        System.out.println("支付金额:"+amount);
        Result<Object> result = new Result<Object>();
        log.info("barcodePay()方法amount:{}orderId:{}",amount,orderId);
        try {
            PreOccupancyUtil preOccupancyUtil=new PreOccupancyUtil();
            Map<String,String> map=preOccupancyUtil.barcodePay(amount,apikey,nonce_str,barcode);
            String status=map.get("status");
            if ("10000".equals(status)){
                String weiXinPreorderId=map.get("orderId");
                TblTxnp tbl=new TblTxnp();
                tbl.setPreamount(new BigDecimal(amount));
                tbl.setOrderid(weiXinPreorderId);//微信条码预授权 订单id
                tbl.setPreOrderid(orderId);
                tbl.setPaymethod("1");
                tbl.setRoomnum(roomNo);
                tbl.setState("1");
                tbl.setPaytype("1");//预授权支付
                tbl.setAmount(new BigDecimal("0"));
                tblTxnpService.save(tbl);
                SetResultUtil.setSuccessResult(result,"success",weiXinPreorderId);
            }else{
                SetResultUtil.setErrorMsgResult(result,"微信预授权支付失败");
            }
            return result;
        } catch (Exception e) {
            log.error("barcodePay()方法出现异常:{}", e.getMessage());
            result.setCode(ReturnCode.parameterError);
            result.setMessage(ReturnMessage.logicError);
            result.setSuccess(false);
            return result;
        }
    }

}
