package org.jeecg.modules.zzj.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.zzj.entity.Food;
import org.jeecg.modules.zzj.service.IFoodService;
import org.jeecg.modules.zzj.util.CardClient;
import TTCEPackage.K7X0Util;
import org.jeecg.modules.zzj.entity.Checkout;
import org.jeecg.modules.zzj.entity.Room;
import org.jeecg.modules.zzj.mapper.CheckoutMapper;
import org.jeecg.modules.zzj.service.ICheckoutService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.zzj.service.IRoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: 离店退房
 * @Author: jeecg-boot
 * @Date: 2019-09-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "离店退房")
@RestController
@RequestMapping("/zzj/checkout")
public class CheckoutController {
    @Autowired
    private ICheckoutService checkoutService;
    @Resource
    private CheckoutMapper checkoutMapper;
    @Autowired
    private IRoomService roomService;
    @Autowired
    private IFoodService foodService;

    @Value("${print.cityaddress}")
    private String cityaddress;
    @Value("${print.areaaddress}")
    private String areaaddress;
    @Value("${print.roadaddress}")
    private String roadaddress;
    @Value("${print.communityaddress}")
    private String communityaddress;
    @Value("${print.numberaddress}")
    private String numberaddress;
    @Value("${print.phone}")
    private String phone;
    @Value("${print.wifiname}")
    private String wifiname;
    @Value("${print.wifipass}")
    private String wifipass;
    @Value("${print.hotelname}")
    private String hotelname;
    @Value("${sdk.ComHandle}")
    private Integer comHandle;
    @Value("${qrDir}")
    private String qrDir;
    @Value("${Invoiqr.appCode}")
    private String appCode;
    @Value("${Invoiqr.taxpayerCode}")
    private String taxpayerCode;
    @Value("${Invoiqr.keyStorePath}")
    private String keyStorePath;
    @Value("${Invoiqr.keyStoreAbner}")
    private String keyStoreAbner;
    @Value("${Invoiqr.keyStorePassWord}")
    private String keyStorePassWord;
    @Value("${Invoiqr.facadeUrl}")
    private String facadeUrl;
    @Value("${cardUrl}")
    private String cardUrl;


    //房卡对应的房间号
    String roomkey=null;



    /**
     * 退房时回收卡
     * @return
     */
    @AutoLog(value = "退房时回收房卡")
    @ApiOperation(value = "退卡")
    @RequestMapping(value = "/Recoverycards", method = RequestMethod.GET)
    public Result Recoverycard() {
        //回收到发卡箱
        boolean flag = K7X0Util.regainCard(comHandle);
        if (!flag) {
            return Result.error("未检测到卡插入");
        }
        //将卡片发送到读卡位置
        K7X0Util.sendToRead(comHandle);
        String uri = cardUrl + "/ReadCardData2";
        Map cardData = new HashMap();
        try {
            Map resultMap = CardClient.send(uri, "");
            cardData = (Map) resultMap.get("data");
            log.info("cardData-->{}", cardData);
        } catch (Exception e) {
            log.error("读卡程序出差-->{}", e.getMessage());
            return Result.error("读卡失败");
        }
        if ("39".equals(cardData.get("cardType").toString())) {
            //"39" 代表客人卡
            //根据卡的房间号查询真实房间号
            String cardNo = cardData.get("roomnum").toString();
            log.info("读到的cardNo-->{}", cardNo);
            if (cardNo.substring(0, 1).equals("0")) {
                cardNo = cardNo.substring(1);
                log.info("以零开头，去零，cardNo-->{}", cardNo);
            }
            if (cardNo.substring(cardNo.length() - 1).equals("0")) {
                cardNo = cardNo.substring(0, cardNo.length() - 1);
                log.info("以零结尾，去零，cardNo-->{}", cardNo);
            }
            log.info("根据房卡的房间号查询对应房间，处理后的cardNo-->{}", cardNo);
            List<Room> roomList = roomService.list(new QueryWrapper<Room>().eq("card", cardNo));
            if (roomList == null || roomList.size() == 0) {
                //未找到该卡对应的房间 进行吐卡
                K7X0Util.sendCardToTake(comHandle);
                log.info("未查询到该房卡对应的房间,cardNo-->{}", cardNo);
                return Result.error("未查询到该房卡对应的房间");
            }
            //回收卡片到发卡箱
            K7X0Util.regain();
             roomkey= roomList.get(0).getRoomnum(); // roomList.get(0).getRoomnum() 房卡对应的房间号
            log.info("回收卡片到发卡箱，返回的房间号为-->{}", roomkey);
            return Result.ok(roomkey);
        } else {
            //不是酒店的客房卡 将吐出
            log.error("不是客人卡或者酒店卡,将卡吐出");
            K7X0Util.sendCardToTake(comHandle);
            return Result.error("卡类型错误,请插入正确的卡类型");
        }
    }


    /**
     * 判断退房是否超时
     * @param roomnum 房间号
     * @return
     */
    //TODO 退房时得加上迷你吧的费用及其他费用 所有的费用先从押金的扣除如果不够就让用户补费
    @AutoLog(value = "判断此房间入住时间否超时")  // roomList.get(0).getRoomnum()  房卡对应的房间号
    @GetMapping(value = "/queryroomnums")
    public Result<Checkout> queryroomnum(@RequestParam(required = true) String roomnum) {

        Result<Checkout> result = new Result<Checkout>();
        //TODO 从数据库获取预定离店时间
        Checkout checkout = checkoutService.getOne(new QueryWrapper<Checkout>().eq("roomnum", roomnum));
        String updateTime = checkout.getUpdateTime();
        if (StringUtils.isEmpty(updateTime)) {
            return result.success("参数为空");
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String format = dateFormat.format(new Date());
            Date parse = dateFormat.parse(updateTime);//预计离店时间
            Date date = dateFormat.parse(format);  //真实离店时间
            if (parse.before(date)) {
                return result.success("超时");
                //TODO 超时需补费

            } else {
                return result.success("未超时");

            }

        } catch (ParseException e) {
            result.error500("判断退房超时方法异常");
        }
        return result;
    }


    /**
     * 根据订单号获取迷你吧的消费
     * @param orderid 订单号
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/expensefood")
    public Result<Food> expensefood(String orderid) {
        Result<Food>result = new Result<>();
        if (StringUtils.isEmpty(orderid)) {
            Result.error("订单号为空");
        }
        //获取客户使用迷你吧情况
        List<Food>foods=foodService.getList(orderid);
        return result;

    }


    /**
     * 根据订单号删除入住人
     * @param orderid
     * @return
     */
    @RequestMapping(value = "/changeofstate")
    public Result<Checkout> changeofstate(String orderid) {
        List<Checkout> checkoutList = checkoutService.list(new QueryWrapper<Checkout>().eq("orderid", orderid));
        if (checkoutList==null&&checkoutList.size()<0) {
            throw new RuntimeException("数据为空");
        }
        for (int i = 0; i < checkoutList.size(); i++) {

        }
        return null;
    }


}
