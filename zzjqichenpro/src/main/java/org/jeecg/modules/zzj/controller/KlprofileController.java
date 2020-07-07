package org.jeecg.modules.zzj.controller;

import TTCEPackage.K7X0Util;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.zzj.common.Invoiqrutil.Invoiqr;
import org.jeecg.modules.zzj.common.Pay.WxpayUtili;
import org.jeecg.modules.zzj.entity.*;
import org.jeecg.modules.zzj.entity.invoice.ResponseData;
import org.jeecg.modules.zzj.service.*;
import org.jeecg.modules.zzj.util.Card.*;
import org.jeecg.modules.zzj.util.CardClient;
import org.jeecg.modules.zzj.util.Pay.AlipayUtil;
import org.jeecg.modules.zzj.util.R;
import org.jeecg.modules.zzj.util.StringToMap;
import org.jeecg.modules.zzj.util.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.jeecg.modules.zzj.util.Ommon.middle;


/**
 * @Description: 客户档案表
 * @Author: jeecg-boot
 * @Date: 2019-10-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "凯莱酒店用户退房类")
@RestController
@RequestMapping("/zzj/klprofile")
public class KlprofileController {
    @Autowired
    private IKlprofileService klprofileService;
    @Autowired
    private ITbRoomEntityService itbRoomEntityService;
    @Autowired
    private IklrecordService iklrecordService;
    @Autowired
    private IWrrecordService iWrrecordService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ITransactionRecordService iTransactionRecordService;
    @Autowired
    private IbreakfastService ibreakfastService;
    @Autowired
    private ICheckinpersonService iCheckinpersonService;
    @Autowired
    private IFoodService iFoodService;
    @Autowired
    private IHotelTipsService iHotelTipsService;
    @Autowired
    private IExpensedetailService iExpensedetailService;
    @Autowired
    private ITblTxnpService tblTxnpService;
    @Autowired
    private IKaiLaiOrderService iKaiLaiOrderService;
    @Autowired
    private IKaiLaiRoomService iKaiLaiRoomService;


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

//    @Autowired
//    private RabbitHelper rabbitHelper;
//    private final String cardQueue = "cardQueue";
//    private final String printQueue= "printQueue";
//    private final String chenkin= "checkinQueue";
//    private final String chenkout= "checkoutQueue";

   /* 房卡对应的房间号
    String roomkey = "2009";*/

    /**
     * test 打印测试
     */
    @ApiOperation(value = "打印测试", httpMethod = "GET")
    @RequestMapping(value = "/testPrint", method = RequestMethod.GET)
    public R testPrint() {
        PrintUtil pu = new PrintUtil();
        pu.print("1015", phone, wifiname, wifipass, hotelname, cityaddress, areaaddress, roadaddress, communityaddress,
                numberaddress, "0.01", "支付宝", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), "742183");
        return R.ok();
    }


    /**
     * 批量制卡
     *
     * @param origination 房间数起始值
     * @param finish      房间数结束值
     * @param sDate       离店日期
     * @param endDate     入住日期
     * @return
     */
    @ApiOperation(value = "批量制卡", httpMethod = "GET")  //TODO 该方法后续会修改
    @RequestMapping(value = "/Batchwritecards", method = RequestMethod.GET)
    public R Batchwritecards(String sDate, String endDate, Integer origination, Integer finish) {
        log.info("进入Batchwritecards()批量制卡方法");
        try {
            Wrrecord tsre = new Wrrecord();
            // 打开发卡机
            log.info("检测发卡机是否有卡");
            K7X0Util.open(comHandle);
            // 检测发卡机是否预空
            boolean isEmpty = K7X0Util.check(2, 0x31);
            System.out.println(isEmpty + "===0:isEmpty");
            if (isEmpty) {
                System.out.println(isEmpty + "===1:isEmpty");
                String str = "发卡机卡箱预空";
                log.info("发卡机卡箱预空");
            }
            // 检测发卡机是否有卡
            isEmpty = K7X0Util.check(3, 0x38);
            if (isEmpty) {
                System.out.println(isEmpty + "===2:isEmpty");
                log.error("Batchwritecards()方法结束异常:卡箱已空");
                return R.error(201, "卡箱已空");
            }
            PrintUtil pu = new PrintUtil();//打印小票
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
            SimpleDateFormat endeFormat = new SimpleDateFormat("HHmm");
            boolean flag = true;
            for (int i = origination; i <= finish; i++) { //发卡状态库
                log.info("进入循环阶段");
                Map map = new HashMap();
                String cardRoomNum = "0" + i + "00";
                map.put("roomNo", cardRoomNum);
                map.put("message", "send");
                map.put("sDate", sDate);
                map.put("endDate", endDate);
                map.put("name", "张三");
                if (flag) {
                    // 新卡
                    log.info("新卡");
                    map.put("type", "0");
                    flag = false;
                } else {
                    // 复制卡
                    log.info("复制卡");
                    map.put("type", "1");
                }
//                String leave = sDate.substring(0, 10);//离店时间
//                String money = "";//预授权金额
//                String reservationType = "";//支付方式
//                pu.print(Integer.toString(i), phone, wifiname, wifipass, hotelname, cityaddress, areaaddress, roadaddress, communityaddress,
//                numberaddress, money, reservationType, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), leave, reservationNumber);
                System.out.println(map + "===map");
                redisUtils.lpush("cardCMD", map);
            }
            return R.ok();
        } catch (Exception e) {
            log.error("Batchwritecards()方法异常:批量发卡异常");
            return R.error();
        }
    }


    /**
     * 将卡片从读卡位置取出
     *
     * @return
     */
    @ApiOperation(value = "将卡片从读卡位置取出", httpMethod = "GET")
    @RequestMapping(value = "/takeCard", method = RequestMethod.GET)
    public R takeCard() {
        log.info("进入takeCard()方法");
        try {
            log.info("将卡片从读卡位置取出");
            K7X0Util.sendCardToTake(comHandle);
            log.info("takeCard()方法结束");
            return R.ok();
        } catch (Exception e) {
            log.error("takeCard()出现异常error:{}", e.getMessage());
            return R.error();
        }
    }


    /**
     * 离店打印小票
     *
     * @param reservationNumber 订单号
     * @param stime             预计入住时间
     * @param ztime             预计离店时间
     * @param roomNum           房间号
     * @return
     */
    @RequestMapping(value = "/updatechckInPerson", method = RequestMethod.GET)
    @ApiOperation(value = "离店打印小票", httpMethod = "GET")
    public R updatechckInPerson(@RequestParam(name = "reservationNumber", required = true)
                                @ApiParam(name = "reservationNumber", value = "订单号")
                                        String reservationNumber, String stime, String ztime, String roomNum) {
        log.info("进入updatechckInPerson()方法reservationNumber:{}", reservationNumber);
        if (StringUtils.isEmpty(reservationNumber) || StringUtils.isEmpty(stime) || StringUtils.isEmpty(ztime) || StringUtils.isEmpty(roomNum)) {
            return R.error("请输入必须值");
        }
        try {
            leavePrintUtil pu = new leavePrintUtil();
            leavewuPrintUtil wupu = new leavewuPrintUtil();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String orderNo = null;
            Date kstime = null;
            Date jstime = null;
            //打印小票
            kstime = sdf.parse(stime);
            jstime = sdf.parse(ztime);
            log.info("打印小票无发票");
            wupu.print(roomNum, null, kstime, jstime, new Date());
            return R.ok();
        } catch (Exception e) {
            log.error("updatechckInPerson()方法出现异常error:{}", e.getMessage());
            return R.error("打印退房小票失败!");
        }
    }


    @ApiOperation(value = "检查打印机是否缺纸")
    @RequestMapping(value = "/printers", method = RequestMethod.POST)
    public R SeePrinterstate() {
        log.info("检查打印机是否缺纸方法开始");
        try {
            CLibraryUtil.INSTANCE.SetUsbportauto();
            CLibraryUtil.INSTANCE.SetInit();
            log.info("SeePrinterstate()结束");
            log.info(CLibraryUtil.INSTANCE.GetStatus() + "");
            if (CLibraryUtil.INSTANCE.GetStatus() == 7) {
                return R.error(7, "缺纸");
            } else {
                if (CLibraryUtil.INSTANCE.GetStatus() == 8) {
                    String printStr = "设备即将缺纸请检测";
                    return R.error(8, "缺纸");
                }
            }
            if (CLibraryUtil.INSTANCE.GetStatus() == 0) {
                log.info("打印机状态良好,暂时不缺纸");
                return R.ok();
            } else {
                return R.error(CLibraryUtil.INSTANCE.GetStatus(), "打印机故障或纸头没有放对位置");
            }

        } catch (UnsatisfiedLinkError e) {
            log.error("SeePrinterstate()出现异常error:{}", e.getMessage());
            return R.error("无法检测打印机状况!");
        }
    }


    /**
     * 离店打印普票
     *
     * @param reservationNumber 订单号
     * @param stime             预计入住时间
     * @param ztime             预计离店时间
     * @param amount            开普票金额
     * @param roomNum           房间号
     * @return
     */
    @ApiOperation(value = "离店打印发票", httpMethod = "GET")
    @RequestMapping(value = "/invoice", method = RequestMethod.GET)
    public R invoice(
            @RequestParam(name = "reservationNumber", required = true) @ApiParam(name = "reservationNumber", value = "订单号") String reservationNumber,
            String stime, String ztime, String amount, String roomNum) {
        if (StringUtils.isEmpty(reservationNumber) || StringUtils.isEmpty(stime) || StringUtils.isEmpty(ztime) ||
                StringUtils.isEmpty(amount) || StringUtils.isEmpty(roomNum)) {
            return R.error("请输入开票必须值!");
        }
        if (amount.equals("0")) {
            return R.error("金额不能为零");
        }
        leavePrintUtil pu = new leavePrintUtil();
        leavewuPrintUtil wupu = new leavewuPrintUtil();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        log.info("根据amount是否打印发票");
        String orderNo = null;
        Date kstime = null;
        Date jstime = null;
        int stamptype = 0;
        try {
            Invoiqr i = new Invoiqr();
            //打印电子发票开票
            Map s = i.getCheckInPerson(amount, qrDir, appCode, taxpayerCode, keyStorePath,
                    keyStoreAbner, keyStorePassWord, facadeUrl, reservationNumber);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
            String dateString = formatter.format(new Date());
            String imgurl;
            orderNo = (String) s.get("orderNo");
            imgurl = (String) s.get("filePath");
            kstime = sdf.parse(stime);
            jstime = sdf.parse(ztime);
            stamptype = 1;
            pu.print(roomNum, imgurl, kstime, jstime, new Date(), orderNo);
        } catch (Exception e) {
            return R.error("打印发票失败!");
        }
        return R.ok();
    }

    /**
     * 查询电子发票订单状态
     *
     * @param orderNo 电子发票订单号
     * @return 状态码 以及说明
     * @throws Exception
     */
    @ApiOperation(value = "查询电子发票订单状态")
    @RequestMapping(value = "/invoiceQuery", method = RequestMethod.GET)
    @ResponseBody
    public R invoiceQuery(String orderNo) {
        if (StringUtils.isEmpty(orderNo)) {
            return R.error("请输入发票订单号");
        }
        Invoiqr i = new Invoiqr();
        try {
            ResponseData responseData = i.quiry_order(orderNo);
            return R.ok("Response", responseData);
        } catch (Exception e) {
            log.error("invoiceQuery()方法异常:查询失败");
        }
        return R.error();
    }


    /**
     * 查询用户基本信息
     * roomkey   房间号
     * beginTime 预定入住时间
     * endTime  预定离店时间
     * orderno  订单号
     *
     * @return
     */
    @ApiOperation(value = "查看退房用户基本信息")
    @RequestMapping(value = "/querynumber", method = RequestMethod.GET)
    public R querynumber(String roomkey, String beginTime, String endTime, String orderno) {
        if (StringUtils.isEmpty(roomkey) || StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime) || StringUtils.isEmpty(orderno)) {
            return R.error("请输入必须值");
        }
        List<TblTxnp> txnps = tblTxnpService.list(new QueryWrapper<TblTxnp>().eq("pre_orderid", orderno));//支付方式、商家订单号等
        List<KaiLaiOrder> cklists = null;//订单信息支付方式
        List<Klbill> cklist = null;
        try {
            cklists = iKaiLaiOrderService.searchByRoomNo(roomkey);
            cklist = iklrecordService.queryklbill(roomkey);
        } catch (Exception e) {
            return R.error("订单信息为空或者无消费!");
        }
        List<Klrecord> list = iklrecordService.querylist(roomkey);//用户信息
        if (list == null || list.size() == 0) {
            return R.error("参数为空");
        }
        if ((cklists.get(0).getResvNameId()) == null || (list.get(0).getRoomkey()) == null) {
            return R.error("未找到对应的房间号或者订单号");
        }
        BigDecimal consume = new BigDecimal("0");//消费总计及 抛帐金额
        for (int i = 0; i < cklist.size(); i++) {
            consume = consume.add(new BigDecimal(cklist.get(i).getPrice()));
        }
        Map<String, String> maps = new HashMap<>();
        maps.put("beginTime", beginTime);
        maps.put("endTime", endTime);
        List<KaiLaiRoom> kailais = iKaiLaiRoomService.SelectKaiLaiRoom(maps);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("list", list);
        hashMap.put("cklists", cklists);
        hashMap.put("consume", consume);
        hashMap.put("txnps", txnps);
        hashMap.put("kailais", kailais);
        if (list != null && list.size() > 0) {
            iExpensedetailService.inserlists(list);
        }
        return R.ok(hashMap);
    }


    /**
     * 判断用户退房时间否超时
     *
     * @param roomkey 房间号
     * @return
     */
    @ApiOperation(value = "判断退房是否超时")
    @RequestMapping(value = "/timeout", method = RequestMethod.GET)
    public R timeout(String roomkey) {
        if (StringUtils.isEmpty(roomkey)) {
            return R.error("请输入房间号");
        }
        try {
            List<Klrecord> lists = iklrecordService.querylist(roomkey);
            SimpleDateFormat sj = new SimpleDateFormat("yyyy-MM-dd");
            Date outtime = null;
            try {
                outtime = lists.get(0).getOut();
            } catch (Exception e) {
                return R.error("参数为空....");
            }
            if (outtime == null) {
                return R.error();
            }
            String reality = sj.format(outtime);//订单号离店时间
            String now = sj.format(new Date());//实际离店时间
            Date parse1 = sj.parse(reality);
            Date parse2 = sj.parse(now);
            long out = parse1.getTime();
            long actual = parse2.getTime();
            if (parse2.before(parse1)) {
                return R.ok("0");
            } else {
                long day = (out - actual) / (60 * 1000);
                return R.ok("1", day);
            }
        } catch (ParseException e) {
            return R.error("判断退房超时方法异常;");
        }
    }


    /**
     * @param roomkey 房间号
     * @return
     */
    @ApiOperation(value = "退房离店")
    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public R checkout(String roomkey) {
        if (StringUtils.isEmpty(roomkey)) {
            return R.error("请输入房间号");
        }
        List<KaiLaiOrder> list=iKaiLaiOrderService.searchByRoomNo(roomkey);
        List<Klbill> cklist = null; //有消费不允许退房
        try {
            cklist = iklrecordService.queryklbill(roomkey);
        } catch (Exception e) {
            log.error("checkout()方法异常:参数为空");
        }
        BigDecimal consume = new BigDecimal("0");
        for (int i = 0; i < cklist.size(); i++) {
            consume = consume.add(new BigDecimal(cklist.get(i).getPrice()));
        }
        if (consume.equals(BigDecimal.ZERO) ||  consume.toString().equals("0.00")) {
            boolean state = iklrecordService.updateresrowld(roomkey);
            if (state){
                List<KaiLaiOrder> kaiLaiOrderList=iKaiLaiOrderService.list(new QueryWrapper<KaiLaiOrder>().eq("resv_name_id",list.get(0).getResvNameId()));
                if(null!=kaiLaiOrderList){
                    for (KaiLaiOrder kaiLaiOrderList1 : kaiLaiOrderList){
                        kaiLaiOrderList1.setResvStatus("CHECKOUT");
                        iKaiLaiOrderService.updateById(kaiLaiOrderList1);
                    }
                }else{
                    KaiLaiOrder order=iKaiLaiOrderService.getOrderByOrderId(list.get(0).getResvNameId());
                    iKaiLaiOrderService.save(order);
                }
                return R.ok();//退房成功
            }else{
                return R.error(consume.toString());//需要付款金额
            }
        } else {
            return R.error(consume.toString());//需要付款金额
        }
    }



    /**
     * 根据房间号 查询 房费 餐饮 其他 共计 及需要补费
     *
     * @param roomkey 房间号
     * @return
     */
    @ApiOperation(value = "根据房间号查询房费,餐饮,其他,共计,需要补费")
    @RequestMapping(value = "/heading", method = RequestMethod.GET)
    public R heading(String roomkey) {
        if (StringUtils.isEmpty(roomkey)) {
            return R.error("请输入房间号!");
        }
        List<Klrecord> client = iklrecordService.querylist(roomkey);//用户信息
        List<Klbill> list = iklrecordService.queryklbill(roomkey);//消費描述
        if(client == null || client.size() == 0){
            log.error("用户信息为空");
            return R.error("用户信息为空");
        }
        if (list == null || list.size() == 0) {
            log.error("账单为空");
            return R.ok("账单为空");
        }
        String reach = middle(client.get(0).getReach().toString(), 1, 10);//到店时间
        String out = middle(client.get(0).getOut().toString(), 1, 10);//离店时间
        BigDecimal house = new BigDecimal("0");//单纯房费
        BigDecimal roomrate = new BigDecimal("0");//房费
        BigDecimal catering = new BigDecimal("0");//餐饮
        BigDecimal elses = new BigDecimal("0");//其他
        BigDecimal sum = new BigDecimal("0");//共计
        BigDecimal Repairfee = new BigDecimal("0");//需要补费
        for (int i = 0; i < list.size(); i++) {
            String transactioncode = list.get(i).getTransactioncode();//账项代码
            String trndescription = list.get(i).getTrndescription();//消费描述
            if (StringUtils.isEmpty(transactioncode)) {
                log.error("heading()方法异常:账项代码为空");
            }
            if ("1000".equals(transactioncode)) {
                house = house.add(new BigDecimal(list.get(i).getPrice()));
            }
            sum = sum.add(new BigDecimal(list.get(i).getPrice()));
            int of = trndescription.indexOf("房费");
            if (of != -1) {
                roomrate = roomrate.add(new BigDecimal(list.get(i).getPrice()));//房费
            }
            int of1 = trndescription.indexOf("餐");
            if (of1 != -1) {
                catering = catering.add(new BigDecimal(list.get(i).getPrice()));//餐饮
            }
        }
        if (reach.equals(out) && (house.intValue()) > 0) {
            return R.error("failure");//入住当天离店且有房费消费
        }
        elses = sum.subtract(roomrate).subtract(catering);//其他
        Repairfee = sum;//补费
        Map<String, Object> map = new HashMap<>();
        map.put("roomrate", roomrate);
        map.put("catering", catering);
        map.put("elses", elses);
        map.put("sum", sum);
        map.put("Repairfee", Repairfee);
        return R.ok(map);
    }


    /**
     * 根据订单号查询婚房
     *
     * @param resrowld 订单号
     * @return
     */
    @ApiOperation(value = "查询是否是婚房")
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public R home(String resrowld) {
        if (StringUtils.isEmpty(resrowld)) {
            return R.error("请输入订单号");
        }
        KaiLaiOrder orderId = iKaiLaiOrderService.getOrderByOrderId(resrowld);
        if (orderId == null) {
            log.error("home()方法异常:参数为空");
            return R.error("参数为空");
        }
        String message = orderId.getMessage();
        if (StringUtils.isEmpty(message)) {
            return R.error("参数为空");
        }
        int index = message.indexOf("婚房");
        if (index != -1) {
            return R.ok("1");//该房间是婚房
        } else {
            return R.ok("0");//该房间不是婚房
        }
    }


    /**
     * 该方法暂时不管用
     * 查询是否有预授权
     *
     * @param reservationNumber
     * @return
     */
    @ApiOperation(value = "查询是否有预授权", httpMethod = "POST")
    @RequestMapping(value = "/isPre", method = RequestMethod.POST)
    public R isPre(String reservationNumber) {
        try {
            List<TransactionRecord> personTrList = iTransactionRecordService
                    .list(new QueryWrapper<TransactionRecord>().eq("reservation_number", reservationNumber));
            if (personTrList.size() > 0) {
                if (personTrList.get(0).getAsAuthorize() == 0) {
                    return R.ok("code", 1);//没有预授权
                }
                return R.ok("PreList", personTrList);
            }
            return R.ok("code", 2);
        } catch (Exception e) {
            return R.error();
        }
    }


    /**
     * 发起支付请求
     *
     * @param amount            金额
     * @param roomkey           房间号
     * @param reservationNumber op系统的订单号
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "普通支付请求")
    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public R pay(@RequestParam String amount, @RequestParam String roomNum, @RequestParam String
            reservationNumber, @RequestParam String roomkey,
                 @RequestParam Integer payType) throws Exception {
        log.info("进入pay()方法,amount:{}roomNum:{}reservationNumber:{}payType:{}", amount, roomNum, reservationNumber, payType);

        try {
            TblTxnp tr = new TblTxnp();
            Map map = new HashMap<>();
            // payType 0 支付宝 1微信
            log.info("根据payType判断是支付宝还是微信");
            String outTradeNo = UUID.randomUUID().toString().replace("-", "");
            switch (payType) {
                case 0:
                    log.info("进入支付宝支付");
                    map = AlipayUtil.trade_precreate(qrDir, amount, roomNum, roomkey);//roomName
                    tr.setPaymethod("aliPay");
                    break;
                case 1:
                    log.info("进入微信支付");
                    Double a = Double.parseDouble(amount) * 100;
                    String totalFee = a.intValue() + "";
                    map = WxpayUtili.getqrcode(qrDir, totalFee);
                    tr.setPaymethod("weChat");
                    break;
            }
            //获取商家订单号
            String PartnerID = StringToMap.getMapToString(map).substring(StringToMap.getMapToString(map).lastIndexOf(":") + 1);

            // 添加交易记录
            log.info("添加交易记录");
            tr.setId(UuidUtils.getUUID());
            tr.setPreamount(new BigDecimal(amount));
            tr.setAmount(new BigDecimal(amount));
            tr.setOrderid(PartnerID);
            tr.setPreOrderid(reservationNumber);
            tr.setRoomnum(roomkey);
            tr.setState("1");
            tr.setPaytype("0");
            tr.setCreateTime(new Date().toString());
            tr.setUpdateBy(null);
            tr.setUpdateTime(null);
            tblTxnpService.save(tr);
            log.info("pay()方法之行结束return:{}", map);
            return R.ok(map);
        } catch (NumberFormatException e) {
            log.error("普通支付方法出现异常:{}", e.getMessage());
            throw new RuntimeException("支付异常");
        }
    }


    /**
     * 查询普通支付是否成功
     *
     * @param outTradeNo 商家订单号
     * @payType 支付类型 支付宝还是微信
     */
    @ApiOperation(value = "查询普通支付是否成功")
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public R query(@RequestParam String outTradeNo, @RequestParam Integer payType) {
        log.info("进入query()方法outTradeNo:{}payType:{}", outTradeNo, payType);
        try {
            log.info("根据outTradeNo查询信息");
            String error = null;
            log.info("根据payType判断支付宝或微信支付");
            switch (payType) {
                case 0:
                    if (AlipayUtil.trade_query(outTradeNo)) {
                        log.info("支付宝支付成功");
                        break;
                    } else {
                        error = "暂未查询到该订单支付成功";
                        break;
                    }
                case 1:
                    Map map = WxpayUtili.query(outTradeNo);
                    if ("支付成功".equals(map.get("trade_state_desc"))) {
                        log.info("微信支付成功");
                        break;
                    } else {
                        error = "暂未查询到该订单支付成功";
                        break;
                    }
            }
            if (error != null) {
                log.info("query()方法执行结束暂未查询到该订单支付成功");
                return R.error("暂未查询到该订单支付成功");
            }
            List<TblTxnp> txnpList = tblTxnpService.list(new QueryWrapper<TblTxnp>().eq("orderid", outTradeNo));
            for (int i = 0; i < txnpList.size(); i++) {
                txnpList.get(i).setState("2");
            }
            boolean batch = tblTxnpService.updateBatchById(txnpList);
            Map maps = new HashMap();
            if (batch) {
                log.info("检测到支付成功修改支付状态");
                maps.put("TblTxnp", txnpList);
                return R.ok(maps);
            }
            return R.error("修改支付状态失败!");
        } catch (Exception e) {
            log.error("查询普通支付是否成功方法出现异常:{}", e.getMessage());
            return R.error();
        }
    }


    /**
     * 查询入账账项代码
     */
    @ApiOperation(value = "查询入账账项代码")
    @RequestMapping(value = "/Queryentrycode",method = RequestMethod.GET)
    public R Queryentrycode() {
        List<Object> list = iklrecordService.Queryentrycode();
        System.out.println(list);
        return R.ok("List", list);
    }


    /**
     * 查询结账账项代码
     */
    @ApiOperation(value = "查询结账账项代码")
    @RequestMapping(value = "/Querytheentrycode",method = RequestMethod.GET)
    public R Querytheentrycode() {
        List<Object> list = iklrecordService.Querytheentrycode();
        return R.ok("List", list);
    }







}

