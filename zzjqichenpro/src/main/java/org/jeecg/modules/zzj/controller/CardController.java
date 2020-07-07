package org.jeecg.modules.zzj.controller;

import TTCEPackage.Adel;
import TTCEPackage.K7X0Util;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.zzj.entity.CardData;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;
import org.jeecg.modules.zzj.entity.Record;
import org.jeecg.modules.zzj.entity.TblTxnp;
import org.jeecg.modules.zzj.service.IKaiLaiOrderService;
import org.jeecg.modules.zzj.service.IRecordService;
import org.jeecg.modules.zzj.service.ITblTxnpService;
import org.jeecg.modules.zzj.util.Card.PrintBrkUtil;
import org.jeecg.modules.zzj.util.Card.PrintUtil;
import org.jeecg.modules.zzj.util.Card.RedisUtils;
import org.jeecg.modules.zzj.util.Card.SetResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;


import static TTCEPackage.K7X0Util.check;

/**
 * 卡Controller
 */
@Slf4j
@Api(tags = "制读卡")
@RestController
@RequestMapping("/zzj/card")
public class CardController {

    @Value("${sdk.ComHandle}")
    private Integer comHandle;


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

    @Autowired
    private RedisUtils redisUtil;

    @Autowired
    private ITblTxnpService tblTxnpService;
    @Autowired
    private IKaiLaiOrderService iKaiLaiOrderService;
    @Autowired
    private IRecordService iRecordService;//发卡记录

    private static String ip="119.2.7.45";//数据库ip地址

    @ApiOperation(value = "testInit", notes = "testInit")
    @GetMapping(value = "/testInit")
    public Result<Object> testInit(){
        if (0  ==  Adel.CLibrary.INSTANCE.Init(18, ip, null, 0, 0, 5)){
            //redisUtil.set("init","true",RedisUtils.DEFAULT_EXPIRE);//一天
            return  Result.ok();
        }else{
            return  Result.error("初始化失败");
        }

    }

    @ApiOperation(value = "NewKey", notes = "NewKey")
    @GetMapping(value = "/NewKey")
    public Result<Object> NewKey(String room,String stime){
        if (testInit().getCode()==200){
            //LongByReference longByReference=new LongByReference();
            int key=Adel.CLibrary.INSTANCE.NewKey(room, "00", stime, null, null, 1,0 ,0,null,null);
            //System.out.println("longByReference:"+longByReference.getValue());
            if (0 == key){
                System.out.println("ok");
                return  Result.ok(key);
            }else {
                System.out.println("key:"+key);
                return Result.error(key+"");
            }
        }else{
            return Result.error("初始化失败");
        }
    }


    @ApiOperation(value = "DupKey", notes = "DupKey")
    @GetMapping(value = "/DupKey")
    public Result<Object> DupKey(String room,String stime){

        if (testInit().getCode()==200){
            //LongByReference longByReference=new LongByReference();
            int key=Adel.CLibrary.INSTANCE.DupKey(room, "00", stime, null, null, 1, 0, 0,null,null);
            //System.out.println("longByReference:"+longByReference.getValue());
            if (0 == key){
                return  Result.ok(key);
            }else {
                return Result.error(key+"");
            }
        }else{
            return Result.error("初始化失败");
        }
    }

    @ApiOperation(value = "ReadCard", notes = "ReadCard")
    @GetMapping(value = "/ReadCard")
    public Result<Object> ReadCard(){
        Result<Object> result=new Result<>();
        if (testInit().getCode()==200){
            int size = 1024 * 1024;
            LongByReference Cardno =new LongByReference();
            Pointer room =new Memory(size);
            Pointer  stime =new Memory(size);
            int key=Adel.CLibrary.INSTANCE.ReadCard(room , null, stime, null,
                    null, null,null,Cardno,0,0);
            if (0 == key){
                System.out.println("Cardno:"+Cardno.getValue());
                System.out.println("room:"+room.getString(0));
                System.out.println("stime:"+stime.getString(0));
                CardData cardData =new CardData();
                cardData.setCardno(Cardno.getValue()+"");
                cardData.setRoom((room.getString(0)).substring(2));
                cardData.setSTime((stime.getString(0)).substring(0,12));
                cardData.setEndTime((stime.getString(0)).substring(12));

                long peer = Pointer.nativeValue(stime);
                Native.free(peer);
                peer=Pointer.nativeValue(room);
                Native.free(peer);
                Pointer.nativeValue(stime,0);
                Pointer.nativeValue(room,0);
                return  SetResultUtil.setSuccessResult(result,"读卡成功",cardData);
            }else {
                return Result.error(key+"");
            }
        }else{
            return Result.error("初始化失败");
        }
    }





    /**
     * 发卡
     */
    @ApiOperation(value = "发卡", notes = "发卡-sendCard")
    @GetMapping(value = "/sendCard")
    public Result<Object> sendCard(String CheckInTime, String CheckOutTime,
                                   String orderId, String id,String roomno,
                                   int num,boolean flag) {
        System.out.println("CheckInTime:"+CheckInTime);
        System.out.println("CheckOutTime:"+CheckOutTime);
        System.out.println("orderId:"+orderId);
        System.out.println("id:"+id);
        System.out.println("num:"+num);

        Result<Object> result = new Result<>();
        log.info("进入sendCard()方法");
        try {
            // 打开发卡机
            log.info("检测发卡机是否有卡");
            K7X0Util.open(comHandle);
            // 检测发卡机是否预空
            boolean isEmpty = check(2, 0x31);
            if (isEmpty) {
                String str = "发卡机卡箱预空";
                log.info("发卡机卡箱预空");
            }
            // 检测发卡机是否有卡
            isEmpty = check(3, 0x38);
            if (isEmpty) {
                // 发卡失败
                log.info("发卡失败失败数据加入数据库中");
                log.info("sendCard()方法结束return:{卡箱已空}");
                return SetResultUtil.setErrorMsgResult(result, "发卡机卡箱已空");
            }
            while (check(3, 0x31)) {
                System.out.println("##########有卡未取出");
                return SetResultUtil.setExceptionResult(result, "读卡位置有卡未取出");
            }
            for (int i = 0; i < num; i++) {
                System.out.println("发送到读卡位置");
                K7X0Util.sendToRead(comHandle);
                /**
                 * 写卡
                 */
                Result<Object> writeResult=null;
                if (i==0&&flag){
                    writeResult=NewKey("01"+roomno,CheckInTime+CheckOutTime);
                }else{
                    writeResult=DupKey("01"+roomno,CheckInTime+CheckOutTime);
                }
                if (writeResult.getCode()==200) {
                    System.out.println("开始发卡");
                    K7X0Util.sendCardToTake(comHandle);

                    log.info("打印小票需要的数据");
                    PrintUtil pu = new PrintUtil();
                    //查询数据库中存储的流水记录
                    String money = "";
                    String reservationType = "";
                    if (null != id && !"".equals(id)) {
                        TblTxnp tblTxnp = tblTxnpService.getOne(new QueryWrapper<TblTxnp>().eq("id", id));
                        money = tblTxnp.getPreamount().toString();//预授权金额
                        reservationType = tblTxnp.getPaymethod();
                        if (reservationType == "0" || reservationType.equals("0")) {
                            reservationType = "支付宝";
                        } else if (reservationType == "1" || reservationType.equals("1")) {
                            reservationType = "微信";
                        } else {
                            reservationType = "银联";
                        }
                    }
                    //查询订单的入住离店时间
                    KaiLaiOrder kaiLaiOrder = iKaiLaiOrderService.getOrderByOrderId(orderId);
                    String leave = kaiLaiOrder.getActualCheckOutDate().substring(0, 10);
                    //打印小票无 早餐数据
                    pu.print(kaiLaiOrder.getRoomNo(), phone, wifiname, wifipass, hotelname, cityaddress, areaaddress, roadaddress, communityaddress,
                            numberaddress, money, reservationType, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), leave, orderId);
                } else {
                    //记录发卡
                    Record record=new Record();
                    record.setNumber(num+"");
                    record.setOrderid(orderId);
                    record.setRoomnum(roomno);
                    record.setState("0");
                    iRecordService.save(record);
                    K7X0Util.regain();
                    return SetResultUtil.setErrorMsgResult(result, "写卡失败");
                }
            }
            //记录发卡
            Record record=new Record();
            record.setNumber(num+"");
            record.setOrderid(orderId);
            record.setRoomnum(roomno);
            record.setState("1");
            iRecordService.save(record);

            log.info("sendCard()方法结束");
            
            return SetResultUtil.setSuccessResult(result, "发卡成功");
        } catch (Exception e) {
            log.error("sendCard()出现异常error:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result, "sendCard");
        }
    }

    /**
     * 不打印发卡
     */
    @ApiOperation(value = "发卡", notes = "发卡-sendCardNoPaper")
    @GetMapping(value = "/sendCardNoPaper")
    public Result<Object> sendCardNoPaper(String CheckInTime, String CheckOutTime,String roomno,
                                          int num,boolean flag
    ) {
        Result<Object> result = new Result<>();
        log.info("进入sendCard()方法");
        try {
            // 打开发卡机
            log.info("检测发卡机是否有卡");
            K7X0Util.open(comHandle);
            // 检测发卡机是否预空
            boolean isEmpty = K7X0Util.check(2, 0x31);
            if (isEmpty) {
                String str = "发卡机卡箱预空";
                log.info("发卡机卡箱预空");
            }
            // 检测发卡机是否有卡
            isEmpty = K7X0Util.check(3, 0x38);
            if (isEmpty) {
                // 发卡失败
                log.info("发卡失败失败数据加入数据库中");
                log.info("sendCard()方法结束return:{卡箱已空}");
                return SetResultUtil.setErrorMsgResult(result, "发卡机卡箱已空");
            }
            while (K7X0Util.check(3, 0x31)) {
                System.out.println("##########有卡未取出");
                return SetResultUtil.setExceptionResult(result, "读卡位置有卡未取出");
            }
            for (int i = 0; i < num; i++) {
                System.out.println("发送到读卡位置");
                K7X0Util.sendToRead(comHandle);
                /**
                 * 写卡
                 */
                Result<Object> writeResult=null;
                if (i==0&&flag){
                    writeResult=NewKey("01"+roomno,CheckInTime+CheckOutTime);
                }else{
                    writeResult=DupKey("01"+roomno,CheckInTime+CheckOutTime);
                }
                if (writeResult.getCode()==200) {
                    System.out.println("开始发卡");
                    K7X0Util.sendCardToTake(comHandle);
                    Thread.currentThread().sleep(1000);
                }else{
                    K7X0Util.regain();
                    return SetResultUtil.setErrorMsgResult(result, "写卡失败");
                }
            }
            log.info("sendCard()方法结束");
            
            return SetResultUtil.setSuccessResult(result, "发卡成功");
        } catch (Exception e) {
            log.error("sendCard()出现异常error:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result, "sendCard");
        }
    }


    /**
     * 退卡
     */
    @ApiOperation(value = "退卡", notes = "退卡-Recoverycard")
    @GetMapping(value = "/Recoverycard")
    public Result<Object> Recoverycard() throws InterruptedException {
        Result<Object> result = new Result<>();
        log.info("进入sendCard()方法");
        // 回收到发卡箱
        Boolean flag = K7X0Util.regainCard(comHandle);
        if (!flag) {
            return SetResultUtil.setErrorMsgResult(result, "退卡失败");
        }
        // 将卡片发送到读卡位置
        K7X0Util.sendToRead(comHandle);
        //读卡
        result=ReadCard();
        K7X0Util.regain();
        
        return result;
    }

    /**
     * 检测发卡位置是否有卡
     */
    @ApiOperation(value = "检测发卡是否有卡", httpMethod = "GET")
    @RequestMapping(value = "/checkTureCard", method = RequestMethod.GET)
    public Result<Object> checkTureCard() {
        Result<Object> result = new Result<>();
        //打开发卡器
        K7X0Util.open(comHandle);
        boolean isEmpty = K7X0Util.check(3, 0x35);//0x35
        System.out.println("isEmpty:" + isEmpty);
        SetResultUtil.setSuccessResult(result, "检测是否有卡", isEmpty);
        return result;
    }







    /**
     * 批量写发卡
     *
     * @param UserType     房间类型,GUEST
     * @param UserGroup    房间分组,GUEST
     * @param CheckInTime  入住时间
     * @param CheckOutTime 离店时间
     * @param username     姓名
     * @param orderId      订单号
     * @param origination  房间数起始值(房间号)
     * @param finish       房间数结束值(房间号)
     * @param noSendFirst  不發卡房间数起始值(房间号)
     * @param noSendEnd       不發卡房间数结束值(房间号)
     * @return
     */
    /*@ApiOperation(value = "批量写发卡")
    @RequestMapping(value = "/batchcard", method = RequestMethod.GET)
    public Result<?> batchcard(String UserType, String UserGroup, String CheckInTime,
                               String CheckOutTime, String username, String orderId,
                               Integer origination, Integer finish, Integer noSendFirst, Integer noSendEnd) {
        Result<Object> result = new Result<>();
        log.info("进入batchcard()方法");
        if (StringUtils.isEmpty(UserType) || StringUtils.isEmpty(UserGroup) || StringUtils.isEmpty(CheckInTime) || StringUtils.isEmpty(CheckOutTime) ||
                StringUtils.isEmpty(username) || StringUtils.isEmpty(orderId) || origination == null || finish == null) {
            return SetResultUtil.setExceptionResult(result, "请求参数为空");
        }
        String FamilyName = getPinYin(middle(username, 1, 1));//姓
        String FirstName = getPinYin(middle(username, 2, 10));//名
        try {
            Wrrecord tsre = new Wrrecord();
            // 打开发卡机
            log.info("检测发卡机是否有卡");
            K7X0Util.open(comHandle);
            // 检测发卡机是否预空
            boolean isEmpty = K7X0Util.check(2, 0x31);
            if (isEmpty) {
                String str = "发卡机卡箱预空";
                log.info("发卡机卡箱预空");
            }
            // 检测发卡机是否有卡
            isEmpty = K7X0Util.check(3, 0x38);
            if (isEmpty) {
                // 发卡失败
                log.info("发卡失败失败数据加入数据库中");
                log.info("batchcard()方法结束return:{卡箱已空}");
                return SetResultUtil.setErrorMsgResult(result, "发卡机卡箱已空");
            }
            while (K7X0Util.check(3, 0x31)) {
                System.out.println("##########有卡未取出");
                return SetResultUtil.setExceptionResult(result, "读卡位置有卡未取出");
            }

            for (int i = origination; i <= finish; i++) {
                if (noSendEnd!=null && noSendFirst!=null){
                    if(i<noSendFirst || i>noSendEnd){ //逻辑判断  小于不发卡初始值  大于不发卡末尾值
                        System.out.println("发送到读卡位置");
                        K7X0Util.sendToRead(comHandle);
                        //写卡操作
                        String url = netIp + "/api/vingcard/IssueKeycard";
                        Map<String, String> map = new HashMap<>();
                        map.put("RoomName", Integer.toString(i));
                        map.put("RoomList", Integer.toString(i));
                        map.put("UserType", UserType);
                        map.put("UserGroup", UserGroup);
                        map.put("CheckInTime", CheckInTime);
                        map.put("CheckOutTime", CheckOutTime);
                        map.put("FamilyName", FamilyName);
                        map.put("FirstName", FirstName);
                        map.put("Debug", "true");
                        map.put("DeviceID", DeviceID);
                        map.put("OperationUserID", OperationUserID);
                        map.put("OperationUserFirstName", OperationUserFirstName);
                        map.put("OperationUserLastName", OperationUserLastName);
                        String param = PreOccupancyUtil.getMapToString(map);
                        CardThread cardThread = new CardThread();
                        cardThread.start();
                        String returnJson = sendPost(url, param, IP, License, Token, ApplicationName, Port);
                        JSONObject jsonObj = JSONObject.parseObject(returnJson);
                        String Code = jsonObj.getString("Code");
                        if ("200".equals(Code)) {
                            log.info("进入打印小票阶段");
                            String Msg = jsonObj.getString("Msg");
                            String data = jsonObj.getString("UserData");
                            PrintUtil pu = new PrintUtil();
                            PrintBrkUtil puBrk = new PrintBrkUtil();
                            //查询数据库中存储的流水记录
                            String money = "No Cash Pledge";//预授权金额
                            String reservationType = "Without paying";//支付方式
                            //查询订单的入住离店时间
                            KaiLaiOrder kaiLaiOrder = iKaiLaiOrderService.getOrderByOrderId(orderId);
                            String leave = kaiLaiOrder.getActualCheckOutDate().substring(0, 10);
                            //打印小票无 早餐数据
                            pu.print(Integer.toString(i), phone, wifiname, wifipass, hotelname, cityaddress, areaaddress, roadaddress, communityaddress,
                                    numberaddress, money, reservationType, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), leave, orderId);
                            Thread.currentThread().sleep(1000);
                        }
                    }
                }else{
                    System.out.println("发送到读卡位置");
                    K7X0Util.sendToRead(comHandle);
                    //写卡操作
                    String url = netIp + "/api/vingcard/IssueKeycard";
                    Map<String, String> map = new HashMap<>();
                    map.put("RoomName", Integer.toString(i));
                    map.put("RoomList", Integer.toString(i));
                    map.put("UserType", UserType);
                    map.put("UserGroup", UserGroup);
                    map.put("CheckInTime", CheckInTime);
                    map.put("CheckOutTime", CheckOutTime);
                    map.put("FamilyName", FamilyName);
                    map.put("FirstName", FirstName);
                    map.put("Debug", "true");
                    map.put("DeviceID", DeviceID);
                    map.put("OperationUserID", OperationUserID);
                    map.put("OperationUserFirstName", OperationUserFirstName);
                    map.put("OperationUserLastName", OperationUserLastName);
                    String param = PreOccupancyUtil.getMapToString(map);
                    CardThread cardThread = new CardThread();
                    cardThread.start();
                    String returnJson = sendPost(url, param, IP, License, Token, ApplicationName, Port);
                    JSONObject jsonObj = JSONObject.parseObject(returnJson);
                    String Code = jsonObj.getString("Code");
                    if ("200".equals(Code)) {
                        log.info("进入打印小票阶段");
                        String Msg = jsonObj.getString("Msg");
                        String data = jsonObj.getString("UserData");
                        PrintUtil pu = new PrintUtil();
                        PrintBrkUtil puBrk = new PrintBrkUtil();
                        //查询数据库中存储的流水记录
                        String money = "No Cash Pledge";//预授权金额
                        String reservationType = "Without paying";//支付方式
                        //查询订单的入住离店时间
                        KaiLaiOrder kaiLaiOrder = iKaiLaiOrderService.getOrderByOrderId(orderId);
                        String leave = kaiLaiOrder.getActualCheckOutDate().substring(0, 10);
                        //打印小票无 早餐数据
                        pu.print(Integer.toString(i), phone, wifiname, wifipass, hotelname, cityaddress, areaaddress, roadaddress, communityaddress,
                                numberaddress, money, reservationType, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), leave, orderId);
                        Thread.currentThread().sleep(1000);
                    }
                }
            }
            log.info("batchcard()方法结束");
            return SetResultUtil.setSuccessResult(result, "发卡成功");
        } catch (Exception e) {
            log.error("batchcard()出现异常error:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result, "batchcard()方法异常");
        }
    }*/


}


   /* public Result<?> batchcard(String UserType, String UserGroup, String CheckInTime,
                               String CheckOutTime, String username, String orderId,
                               Integer origination, Integer finish) {
        Result<Object> result = new Result<>();
        log.info("进入batchcard()方法");
        if (StringUtils.isEmpty(UserType) || StringUtils.isEmpty(UserGroup) || StringUtils.isEmpty(CheckInTime) || StringUtils.isEmpty(CheckOutTime) ||
                StringUtils.isEmpty(username) || StringUtils.isEmpty(orderId) || origination == null || finish == null) {
            return SetResultUtil.setExceptionResult(result, "请求参数为空");
        }
        String FamilyName = getPinYin(middle(username, 1, 1));//姓
        String FirstName = getPinYin(middle(username, 2, 10));//名
        try {
            Wrrecord tsre = new Wrrecord();
            // 打开发卡机
            log.info("检测发卡机是否有卡");
            K7X0Util.open(comHandle);
            // 检测发卡机是否预空
            boolean isEmpty = K7X0Util.check(2, 0x31);
            if (isEmpty) {
                String str = "发卡机卡箱预空";
                log.info("发卡机卡箱预空");
            }
            // 检测发卡机是否有卡
            isEmpty = K7X0Util.check(3, 0x38);
            if (isEmpty) {
                // 发卡失败
                log.info("发卡失败失败数据加入数据库中");
                log.info("batchcard()方法结束return:{卡箱已空}");
                return SetResultUtil.setErrorMsgResult(result, "发卡机卡箱已空");
            }
            while (K7X0Util.check(3, 0x31)) {
                System.out.println("##########有卡未取出");
                return SetResultUtil.setExceptionResult(result, "读卡位置有卡未取出");
            }

            for (int i = origination; i <= finish; i++) {
                System.out.println("发送到读卡位置");
                K7X0Util.sendToRead(comHandle);
                //写卡操作
                String url = netIp + "/api/vingcard/IssueKeycard";
                Map<String, String> map = new HashMap<>();
                map.put("RoomName", Integer.toString(i));
                map.put("RoomList", Integer.toString(i));
                map.put("UserType", UserType);
                map.put("UserGroup", UserGroup);
                map.put("CheckInTime", CheckInTime);
                map.put("CheckOutTime", CheckOutTime);
                map.put("FamilyName", FamilyName);
                map.put("FirstName", FirstName);
                map.put("Debug", "true");
                map.put("DeviceID", DeviceID);
                map.put("OperationUserID", OperationUserID);
                map.put("OperationUserFirstName", OperationUserFirstName);
                map.put("OperationUserLastName", OperationUserLastName);
                String param = PreOccupancyUtil.getMapToString(map);
                CardThread cardThread = new CardThread();
                cardThread.start();
                String returnJson = sendPost(url, param, IP, License, Token, ApplicationName, Port);
                JSONObject jsonObj = JSONObject.parseObject(returnJson);
                String Code = jsonObj.getString("Code");
                if ("200".equals(Code)) {
                    log.info("进入打印小票阶段");
                    String Msg = jsonObj.getString("Msg");
                    String data = jsonObj.getString("UserData");
                    PrintUtil pu = new PrintUtil();
                    PrintBrkUtil puBrk = new PrintBrkUtil();
                    //查询数据库中存储的流水记录
                    String money = "No Cash Pledge";//预授权金额
                    String reservationType = "Without paying";//支付方式
                    //查询订单的入住离店时间
                    KaiLaiOrder kaiLaiOrder = iKaiLaiOrderService.getOrderByOrderId(orderId);
                    String leave = kaiLaiOrder.getActualCheckOutDate().substring(0, 10);
                    //打印小票无 早餐数据
                    pu.print(Integer.toString(i), phone, wifiname, wifipass, hotelname, cityaddress, areaaddress, roadaddress, communityaddress,
                            numberaddress, money, reservationType, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), leave, orderId);
                    Thread.currentThread().sleep(1000);
                }
            }
            log.info("batchcard()方法结束");
            return SetResultUtil.setSuccessResult(result, "发卡成功");
        } catch (Exception e) {
            log.error("batchcard()出现异常error:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result, "batchcard()方法异常");
        }
    }*/