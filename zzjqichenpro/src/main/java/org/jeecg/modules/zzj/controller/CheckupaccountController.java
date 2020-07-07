package org.jeecg.modules.zzj.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.zzj.entity.Checkupaccounts;
import org.jeecg.modules.zzj.entity.Consumption;
import org.jeecg.modules.zzj.entity.Klbill;
import org.jeecg.modules.zzj.entity.Posting;
import org.jeecg.modules.zzj.service.IklrecordService;
import org.jeecg.modules.zzj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Api(tags = "入账")
@RestController
@RequestMapping("/zzj/Checkupaccount")
public class CheckupaccountController {

    @Autowired
    private IklrecordService iklrecordService;

    /**
     * Test
     */
    @RequestMapping(value = "/Throwcurtain", method = RequestMethod.GET)
    public R Throwcurtain(String resrowid) {
        if (StringUtils.isEmpty(resrowid)) {
            return R.error("请输入订单号");
        }
//        resrowid = "742007 ";
        List<Checkupaccounts> list = iklrecordService.querythrow(resrowid);

        BigDecimal individual = new BigDecimal("0");//散客帐
        BigDecimal team = new BigDecimal("0");//团队帐
        BigDecimal travelagency = new BigDecimal("0");//旅行社
        BigDecimal agreement = new BigDecimal("0");//协议公司
        Map<String, BigDecimal> map = new HashMap<>();
        System.out.println(list);
        String nametype = list.get(0).getNametype();
        if (StringUtils.isEmpty(nametype)) {
            return R.error("参数为空");
        }
        if (nametype.equals("Individual")) {
            Integer blockid = list.get(0).getBlockid();
            if (blockid.equals(0)) {
                for (int i = 0; i < list.size(); i++) {
                    individual = individual.add(new BigDecimal(list.get(i).getPrice()));
                }
                System.out.println("散客" + individual);
            } else if (blockid > 0) {
                for (int i = 0; i < list.size(); i++) {
                    team = team.add(new BigDecimal(list.get(i).getPrice()));
                }
                System.out.println("团队" + team);
            }
        }
        if (nametype.equals("Agent")) {
            for (int i = 0; i < list.size(); i++) {
                travelagency = travelagency.add(new BigDecimal(list.get(i).getPrice()));
            }
            System.out.println("旅行社" + travelagency);
        }
        if (nametype.equals("Company")) {
            for (int i = 0; i < list.size(); i++) {
                agreement = agreement.add(new BigDecimal(list.get(i).getPrice()));
            }
            System.out.println("协议公司" + agreement);
        }

        if (individual != BigDecimal.ZERO) {
            return R.ok("散客", individual);
        }
        if (team != BigDecimal.ZERO) {
            return R.ok("团队", team);
        }
        if (travelagency != BigDecimal.ZERO) {
            return R.ok("旅行社", travelagency);
        }
        if (agreement != BigDecimal.ZERO) {
            return R.ok("协议公司", agreement);
        }
        return R.error("该订单无结果!");
    }

    /**
     * 单笔消费入帐
     *
     * @param resrowid 订单号
     * roomkey 房间号
     * username 客户姓名
     * @return
     */
    @ApiOperation(value = "单笔消费入账")
    @RequestMapping(value = "/CarrytoOne", method = RequestMethod.GET)
    public R CarrytoOne(@RequestParam(name = "resrowid", required = true) String resrowid,
                     @RequestParam(name = "roomkey", required = true) String roomkey,
                     @RequestParam(name = "username", required = true) String username,
                    @RequestParam(name ="TransactionCode",required = true )String TransactionCode,
                     String roomrate,String subcode,String amount
    ) {
        if (StringUtils.isEmpty(resrowid) || StringUtils.isEmpty(roomkey) || StringUtils.isEmpty(username)) {
            return R.error("请输入订单号或者房间号、姓名!");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("resrowid", resrowid);
        map.put("roomkey", roomkey);
        map.put("TransactionCode",TransactionCode);
        map.put("amount", amount);
        map.put("roomrate", roomrate);
        map.put("subcode", subcode);
        map.put("username", username);
        int flag = iklrecordService.insertentry(map);
        return  R.ok(flag+"");
    }



    /**
     * 每笔消费入帐
     *
     * @param resrowid 订单号
     * roomkey 房间号
     * username 客户姓名
     * @return
     */
    @ApiOperation(value = "消费入账")
    @RequestMapping(value = "/Carryto", method = RequestMethod.GET)
    public R Carryto(@RequestParam(name = "resrowid", required = true) String resrowid,
                     @RequestParam(name = "roomkey", required = true) String roomkey,
                     @RequestParam(name = "username", required = true) String username) {
        if (StringUtils.isEmpty(resrowid) || StringUtils.isEmpty(roomkey) || StringUtils.isEmpty(username)) {
            return R.error("请输入订单号或者房间号、姓名!");
        }
        int roomkeys = Integer.parseInt(roomkey);//房间号
        int resrowids = Integer.parseInt(resrowid);//预订单号
        int anInt = Integer.parseInt(new SimpleDateFormat("HHmmss").format(new Date()));//时分秒
        Integer sub = roomkeys + resrowids + anInt;//subcode
        String subcode = sub.toString();
        List<Klbill> list = null;
        try {
            list = iklrecordService.queryklbill(roomkey);
        } catch (Exception e) {
            throw new RuntimeException("参数为空.");
        }
        if (list == null || list.size() == 0) {
            return R.error("参数为空,该订单无消费!");
        }
        for (int i = 0; i < list.size(); i++) {
            String transactioncode = list.get(i).getTransactioncode();
            if (StringUtils.isEmpty(transactioncode)) {
                return R.error("参数为空..");
            }
            if (transactioncode.equals("1000")) {
                BigDecimal roomrate = new BigDecimal(list.get(i).getPrice());//房费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1000");
                map.put("amount", "1");
                map.put("roomrate", roomrate);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1010")) {
                BigDecimal anroomrate = new BigDecimal(list.get(i).getPrice());//追加房费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1010");
                map.put("amount", "1");
                map.put("roomrate", anroomrate);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1020")) {
                BigDecimal rateupselling = new BigDecimal(list.get(i).getPrice());//房费- UPSELLING
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1020");
                map.put("amount", "1");
                map.put("roomrate", rateupselling);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1030")) {
                BigDecimal allroomcharge = new BigDecimal(list.get(i).getPrice());// 房费- 全日房费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1030");
                map.put("amount", "1");
                map.put("roomrate", allroomcharge);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1040")) {
                BigDecimal halfdayroomcharge = new BigDecimal(list.get(i).getPrice());//房费- 半日房费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1040");
                map.put("amount", "1");
                map.put("roomrate", halfdayroomcharge);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1050")) {
                BigDecimal bed = new BigDecimal(list.get(i).getPrice());//房费- 加床
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1050");
                map.put("amount", "1");
                map.put("roomrate", bed);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1060")) {
                BigDecimal cancellation = new BigDecimal(list.get(i).getPrice());//预订未到/取消
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1060");
                map.put("amount", "1");
                map.put("roomrate", cancellation);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1099")) {
                BigDecimal adjustment = new BigDecimal(list.get(i).getPrice());//房费调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1099");
                map.put("amount", "1");
                map.put("roomrate", adjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1200")) {
                BigDecimal Roomservicecharge = new BigDecimal(list.get(i).getPrice());//房费服务费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1200");
                map.put("amount", "1");
                map.put("roomrate", Roomservicecharge);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1299")) {
                BigDecimal Roomservicechargeadjustment = new BigDecimal(list.get(i).getPrice());//房费服务费调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1299");
                map.put("amount", "1");
                map.put("roomrate", Roomservicechargeadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1500")) {
                BigDecimal Roommiscellaneousincome = new BigDecimal(list.get(i).getPrice());//客房杂项收入
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1500");
                map.put("amount", "1");
                map.put("roomrate", Roommiscellaneousincome);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1509")) {
                BigDecimal incomeadjustment = new BigDecimal(list.get(i).getPrice());//客房杂项收入调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1509");
                map.put("amount", "1");
                map.put("roomrate", incomeadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1508")) {
                BigDecimal Roomincome = new BigDecimal(list.get(i).getPrice());//客房杂项收入 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1508");
                map.put("amount", "1");
                map.put("roomrate", Roomincome);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1800")) {
                BigDecimal Theroomcharge = new BigDecimal(list.get(i).getPrice());//房费6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1800");
                map.put("amount", "1");
                map.put("roomrate", Theroomcharge);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2111")) {
                BigDecimal Breakfastfood = new BigDecimal(list.get(i).getPrice());// 宴会厅早餐食品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2111");
                map.put("amount", "1");
                map.put("roomrate", Breakfastfood);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2112")) {
                BigDecimal breakfastdrinks = new BigDecimal(list.get(i).getPrice());// 宴会厅早餐饮品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2112");
                map.put("amount", "1");
                map.put("roomrate", breakfastdrinks);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2114")) {
                BigDecimal Greenfeesforbreakfast = new BigDecimal(list.get(i).getPrice());// 宴会厅早餐场租
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2114");
                map.put("amount", "1");
                map.put("roomrate", Greenfeesforbreakfast);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2113")) {
                BigDecimal Otherbreakfast = new BigDecimal(list.get(i).getPrice());//宴会厅早餐其他
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2113");
                map.put("amount", "1");
                map.put("roomrate", Otherbreakfast);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2121")) {
                BigDecimal Foodforlunch = new BigDecimal(list.get(i).getPrice());//宴会厅午餐食品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2121");
                map.put("amount", "1");
                map.put("roomrate", Foodforlunch);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2122")) {
                BigDecimal Drinkforlunch = new BigDecimal(list.get(i).getPrice());//宴会厅午餐饮品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2122");
                map.put("amount", "1");
                map.put("roomrate", Drinkforlunch);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2124")) {
                BigDecimal forbreakfast = new BigDecimal(list.get(i).getPrice());//宴会厅早餐场租
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2124");
                map.put("amount", "1");
                map.put("roomrate", forbreakfast);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2123")) {
                BigDecimal Banquethalllunchother = new BigDecimal(list.get(i).getPrice());//宴会厅午餐其他
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2123");
                map.put("amount", "1");
                map.put("roomrate", Banquethalllunchother);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2131")) {
                BigDecimal fordinner = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐食品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2131");
                map.put("amount", "1");
                map.put("roomrate", fordinner);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2132")) {
                BigDecimal Halldinnerdrinks = new BigDecimal(list.get(i).getPrice());// 宴会厅晚餐饮品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2132");
                map.put("amount", "1");
                map.put("roomrate", Halldinnerdrinks);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2134")) {
                BigDecimal Rentals = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐场租
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2134");
                map.put("amount", "1");
                map.put("roomrate", Rentals);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2133")) {
                BigDecimal Otherdinner = new BigDecimal(list.get(i).getPrice());// 宴会厅晚餐其他
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2133");
                map.put("amount", "1");
                map.put("roomrate", Otherdinner);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2115")) {
                BigDecimal servicecharge = new BigDecimal(list.get(i).getPrice());// 宴会厅早餐服务费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2115");
                map.put("amount", "1");
                map.put("roomrate", servicecharge);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2125")) {
                BigDecimal Mealservicecharge = new BigDecimal(list.get(i).getPrice());//宴会厅午餐服务费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2125");
                map.put("amount", "1");
                map.put("roomrate", Mealservicecharge);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2135")) {
                BigDecimal Dinnerservicecharge = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐服务费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2135");
                map.put("amount", "1");
                map.put("roomrate", Dinnerservicecharge);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2119")) {
                BigDecimal Adjustthebreakfast = new BigDecimal(list.get(i).getPrice());//宴会厅早餐调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2119");
                map.put("amount", "1");
                map.put("roomrate", Adjustthebreakfast);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2129")) {
                BigDecimal Adjustthelunch = new BigDecimal(list.get(i).getPrice());//宴会厅午餐调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2129");
                map.put("amount", "1");
                map.put("roomrate", Adjustthelunch);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2139")) {
                BigDecimal Dinnertoadjust = new BigDecimal(list.get(i).getPrice());// 宴会厅晚餐调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2129");
                map.put("amount", "1");
                map.put("roomrate", Dinnertoadjust);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2144")) {
                BigDecimal Hallgreenfees = new BigDecimal(list.get(i).getPrice());//宴会厅场租
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2144");
                map.put("amount", "1");
                map.put("roomrate", Hallgreenfees);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2149")) {
                BigDecimal rentadjustment = new BigDecimal(list.get(i).getPrice());// 宴会厅场租调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2149");
                map.put("amount", "1");
                map.put("roomrate", rentadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2211")) {
                BigDecimal Cafebreakfast = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐食品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2211");
                map.put("amount", "1");
                map.put("roomrate", Cafebreakfast);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2212")) {
                BigDecimal Breakfastdrink = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐饮品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2212");
                map.put("amount", "1");
                map.put("roomrate", Breakfastdrink);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2213")) {
                BigDecimal Cafebreakfastother = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐其他
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2213");
                map.put("amount", "1");
                map.put("roomrate", Cafebreakfastother);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2221")) {
                BigDecimal Cafelunch = new BigDecimal(list.get(i).getPrice());// 咖啡厅午餐食品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2221");
                map.put("amount", "1");
                map.put("roomrate", Cafelunch);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2222")) {
                BigDecimal Cafelunchdrinks = new BigDecimal(list.get(i).getPrice());// 咖啡厅午餐饮品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2222");
                map.put("amount", "1");
                map.put("roomrate", Cafelunchdrinks);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2223")) {
                BigDecimal Cafelunchother = new BigDecimal(list.get(i).getPrice());// 咖啡厅午餐其他
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2223");
                map.put("amount", "1");
                map.put("roomrate", Cafelunchother);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2231")) {
                BigDecimal Cafedinnerfood = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐食品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2231");
                map.put("amount", "1");
                map.put("roomrate", Cafedinnerfood);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2232")) {
                BigDecimal Cafedinnerdrinks = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐饮品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2232");
                map.put("amount", "1");
                map.put("roomrate", Cafedinnerdrinks);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2233")) {
                BigDecimal Cafedinnerother = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐其他
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2233");
                map.put("amount", "1");
                map.put("roomrate", Cafedinnerother);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2200")) {
                BigDecimal Coffeeshop = new BigDecimal(list.get(i).getPrice());// 咖啡厅早餐包价
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2200");
                map.put("amount", "1");
                map.put("roomrate", Coffeeshop);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2215")) {
                BigDecimal Cafebreakfastservicecharge = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐服务费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2215");
                map.put("amount", "1");
                map.put("roomrate", Cafebreakfastservicecharge);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2225")) {
                BigDecimal Cafelunchservice = new BigDecimal(list.get(i).getPrice());//咖啡厅午餐服务费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2225");
                map.put("amount", "1");
                map.put("roomrate", Cafelunchservice);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2235")) {
                BigDecimal Cafedinnerservice = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐服务费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2235");
                map.put("amount", "1");
                map.put("roomrate", Cafedinnerservice);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2219")) {
                BigDecimal Cafebreakfastadjustment = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2219");
                map.put("amount", "1");
                map.put("roomrate", Cafebreakfastadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2229")) {
                BigDecimal Cafelunchadjustment = new BigDecimal(list.get(i).getPrice());//咖啡厅午餐调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2229");
                map.put("amount", "1");
                map.put("roomrate", Cafelunchadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2239")) {
                BigDecimal Cafedinneradjustment = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2239");
                map.put("amount", "1");
                map.put("roomrate", Cafedinneradjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2311")) {
                BigDecimal Roomservicebreakfastfood = new BigDecimal(list.get(i).getPrice());//客房送餐早餐食品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2311");
                map.put("amount", "1");
                map.put("roomrate", Roomservicebreakfastfood);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2312")) {
                BigDecimal Roomservicebreakfastdrinks = new BigDecimal(list.get(i).getPrice());//客房送餐早餐饮品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2312");
                map.put("amount", "1");
                map.put("roomrate", Roomservicebreakfastdrinks);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2313")) {
                BigDecimal Roomservicebreakfastother = new BigDecimal(list.get(i).getPrice());//客房送餐早餐其他
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2313");
                map.put("amount", "1");
                map.put("roomrate", Roomservicebreakfastother);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2321")) {
                BigDecimal Roomservicelunch = new BigDecimal(list.get(i).getPrice());//客房送餐午餐食品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2321");
                map.put("amount", "1");
                map.put("roomrate", Roomservicelunch);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2322")) {
                BigDecimal Roomservicelunchdrinks = new BigDecimal(list.get(i).getPrice());//客房送餐午餐饮品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2322");
                map.put("amount", "1");
                map.put("roomrate", Roomservicelunchdrinks);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2323")) {
                BigDecimal Roomservicelunchother = new BigDecimal(list.get(i).getPrice());//客房送餐午餐其他
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2323");
                map.put("amount", "1");
                map.put("roomrate", Roomservicelunchother);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2331")) {
                BigDecimal Roomservicedinnerfood = new BigDecimal(list.get(i).getPrice());//客房送餐晚餐食品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2331");
                map.put("amount", "1");
                map.put("roomrate", Roomservicedinnerfood);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2332")) {
                BigDecimal Roomservicedinnerdrinks = new BigDecimal(list.get(i).getPrice());//客房送餐晚餐饮品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2332");
                map.put("amount", "1");
                map.put("roomrate", Roomservicedinnerdrinks);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2333")) {
                BigDecimal Roomservicedinnerother = new BigDecimal(list.get(i).getPrice());//客房送餐晚餐其他
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2333");
                map.put("amount", "1");
                map.put("roomrate", Roomservicedinnerother);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2315")) {
                BigDecimal Roomservicechargeforbreakfast = new BigDecimal(list.get(i).getPrice());//客房送餐早餐服务费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2315");
                map.put("amount", "1");
                map.put("roomrate", Roomservicechargeforbreakfast);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2325")) {
                BigDecimal Roomservicelunchservicecharge = new BigDecimal(list.get(i).getPrice());//客房送餐午餐服务费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2325");
                map.put("amount", "1");
                map.put("roomrate", Roomservicelunchservicecharge);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2335")) {
                BigDecimal Roomservicefordinner = new BigDecimal(list.get(i).getPrice());//客房送餐晚餐服务费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2335");
                map.put("amount", "1");
                map.put("roomrate", Roomservicefordinner);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2319")) {
                BigDecimal Roomservicebreakfastadjustment = new BigDecimal(list.get(i).getPrice());//客房送餐早餐调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2319");
                map.put("amount", "1");
                map.put("roomrate", Roomservicebreakfastadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2329")) {
                BigDecimal Roomservicelunchadjustment = new BigDecimal(list.get(i).getPrice());//客房送餐午餐调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2329");
                map.put("amount", "1");
                map.put("roomrate", Roomservicelunchadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2339")) {
                BigDecimal Roomservicedinneradjustment = new BigDecimal(list.get(i).getPrice());//客房送餐晚餐调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2339");
                map.put("amount", "1");
                map.put("roomrate", Roomservicedinneradjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("4001")) {
                BigDecimal localcalls = new BigDecimal(list.get(i).getPrice());//市话
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "4001");
                map.put("amount", "1");
                map.put("roomrate", localcalls);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("4002")) {
                BigDecimal domestictollcall = new BigDecimal(list.get(i).getPrice());//国内长途
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "4002");
                map.put("amount", "1");
                map.put("roomrate", domestictollcall);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("4003")) {
                BigDecimal IDD = new BigDecimal(list.get(i).getPrice());//国际长途
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "4003");
                map.put("amount", "1");
                map.put("roomrate", IDD);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("4009")) {
                BigDecimal Telephonerateadjustment = new BigDecimal(list.get(i).getPrice());//电话费调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "4009");
                map.put("amount", "1");
                map.put("roomrate", Telephonerateadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("4008")) {
                BigDecimal Telephonecharges9 = new BigDecimal(list.get(i).getPrice());//电话费 9%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "4008");
                map.put("amount", "1");
                map.put("roomrate", Telephonecharges9);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("4600")) {
                BigDecimal Businesscenterprint = new BigDecimal(list.get(i).getPrice());//商务中心-打印
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "4600");
                map.put("amount", "1");
                map.put("roomrate", Businesscenterprint);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("4609")) {
                BigDecimal Adjustmentofbusinesscenter = new BigDecimal(list.get(i).getPrice());//商务中心调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "4609");
                map.put("amount", "1");
                map.put("roomrate", Adjustmentofbusinesscenter);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("4608")) {
                BigDecimal CBD = new BigDecimal(list.get(i).getPrice());//商务中心 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "4608");
                map.put("amount", "1");
                map.put("roomrate", CBD);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5100")) {
                BigDecimal miniba = new BigDecimal(list.get(i).getPrice());//迷你吧
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5100");
                map.put("amount", "1");
                map.put("roomrate", miniba);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5108")) {
                BigDecimal minibavat = new BigDecimal(list.get(i).getPrice());//迷你吧 13%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5108");
                map.put("amount", "1");
                map.put("roomrate", minibavat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5109")) {
                BigDecimal Minibaradjustment = new BigDecimal(list.get(i).getPrice());//迷你吧调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5109");
                map.put("amount", "1");
                map.put("roomrate", Minibaradjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5201")) {
                BigDecimal Laundryoutsidethestore = new BigDecimal(list.get(i).getPrice());//洗衣费-店外
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5201");
                map.put("amount", "1");
                map.put("roomrate", Laundryoutsidethestore);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5202")) {
                BigDecimal Laundrychargeinstore = new BigDecimal(list.get(i).getPrice());//洗衣费-店内
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5202");
                map.put("amount", "1");
                map.put("roomrate", Laundrychargeinstore);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5209")) {
                BigDecimal Laundrychargeadjustment = new BigDecimal(list.get(i).getPrice());//洗衣费调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5209");
                map.put("amount", "1");
                map.put("roomrate", Laundrychargeadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5208")) {
                BigDecimal laundrycharge = new BigDecimal(list.get(i).getPrice());//洗衣费 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5208");
                map.put("amount", "1");
                map.put("roomrate", laundrycharge);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5300")) {
                BigDecimal compensatefor = new BigDecimal(list.get(i).getPrice());//赔偿
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5300");
                map.put("amount", "1");
                map.put("roomrate", compensatefor);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5308")) {
                BigDecimal compensateforvat = new BigDecimal(list.get(i).getPrice());//赔偿 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5308");
                map.put("amount", "1");
                map.put("roomrate", compensateforvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5309")) {
                BigDecimal Thecompensationadjustment = new BigDecimal(list.get(i).getPrice());//赔偿调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5309");
                map.put("amount", "1");
                map.put("roomrate", Thecompensationadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5400")) {
                BigDecimal carrental = new BigDecimal(list.get(i).getPrice());//租车
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5400");
                map.put("amount", "1");
                map.put("roomrate", carrental);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5409")) {
                BigDecimal adjustcarrental = new BigDecimal(list.get(i).getPrice());//租车调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5409");
                map.put("amount", "1");
                map.put("roomrate", adjustcarrental);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5500")) {
                BigDecimal Pickupanddropoff = new BigDecimal(list.get(i).getPrice());//代收接送机
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5409");
                map.put("amount", "1");
                map.put("roomrate", Pickupanddropoff);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5510")) {
                BigDecimal scenicspots = new BigDecimal(list.get(i).getPrice());//代收景点门票
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5510");
                map.put("amount", "1");
                map.put("roomrate", scenicspots);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5520")) {
                BigDecimal Collectutilities = new BigDecimal(list.get(i).getPrice());//代收水电费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5520");
                map.put("amount", "1");
                map.put("roomrate", Collectutilities);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5530")) {
                BigDecimal Othercollection = new BigDecimal(list.get(i).getPrice());//其它代收
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5530");
                map.put("amount", "1");
                map.put("roomrate", Othercollection);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6091")) {
                BigDecimal Healthcentrefoodadjustment = new BigDecimal(list.get(i).getPrice());//康体中心-食品调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6091");
                map.put("amount", "1");
                map.put("roomrate", Healthcentrefoodadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6110")) {
                BigDecimal miscellaneousincome = new BigDecimal(list.get(i).getPrice());//杂项收入
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6110");
                map.put("amount", "1");
                map.put("roomrate", miscellaneousincome);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6119")) {
                BigDecimal adjustmiscellaneousincome = new BigDecimal(list.get(i).getPrice());//杂项收入调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6119");
                map.put("amount", "1");
                map.put("roomrate", adjustmiscellaneousincome);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6118")) {
                BigDecimal miscellaneousincomevat = new BigDecimal(list.get(i).getPrice());//杂项收入 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6118");
                map.put("amount", "1");
                map.put("roomrate", miscellaneousincomevat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6200")) {
                BigDecimal rental = new BigDecimal(list.get(i).getPrice());//租金
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6200");
                map.put("amount", "1");
                map.put("roomrate", rental);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6209")) {
                BigDecimal adjustrental = new BigDecimal(list.get(i).getPrice());//租金调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6209");
                map.put("amount", "1");
                map.put("roomrate", adjustrental);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7111")) {
                BigDecimal breakfastfoodvat = new BigDecimal(list.get(i).getPrice());//宴会厅早餐食品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7111");
                map.put("amount", "1");
                map.put("roomrate", breakfastfoodvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7114")) {
                BigDecimal Greenfeesforbreakfastvat = new BigDecimal(list.get(i).getPrice());//宴会厅早餐场租 5%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7114");
                map.put("amount", "1");
                map.put("roomrate", Greenfeesforbreakfastvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7113")) {
                BigDecimal Banquethallbreakfastother = new BigDecimal(list.get(i).getPrice());//宴会厅早餐其他 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7113");
                map.put("amount", "1");
                map.put("roomrate", Banquethallbreakfastother);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7121")) {
                BigDecimal Banquethalllunchfoodvat = new BigDecimal(list.get(i).getPrice());//宴会厅午餐食品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7121");
                map.put("amount", "1");
                map.put("roomrate", Banquethalllunchfoodvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7122")) {
                BigDecimal Banquethalllunchdrinks = new BigDecimal(list.get(i).getPrice());//宴会厅午餐饮品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7122");
                map.put("amount", "1");
                map.put("roomrate", Banquethalllunchdrinks);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7124")) {
                BigDecimal Banquethalllunchrental = new BigDecimal(list.get(i).getPrice());//宴会厅午餐场租 5%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7124");
                map.put("amount", "1");
                map.put("roomrate", Banquethalllunchrental);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7123")) {
                BigDecimal Banquethalllunchothervat = new BigDecimal(list.get(i).getPrice());//宴会厅午餐其他 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7123");
                map.put("amount", "1");
                map.put("roomrate", Banquethalllunchothervat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7112")) {
                BigDecimal Banquethallbreakfastdrinks = new BigDecimal(list.get(i).getPrice());//宴会厅早餐饮品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7112");
                map.put("amount", "1");
                map.put("roomrate", Banquethallbreakfastdrinks);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7131")) {
                BigDecimal Banquethalldinnerfood = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐食品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7131");
                map.put("amount", "1");
                map.put("roomrate", Banquethalldinnerfood);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7132")) {
                BigDecimal Banquethalldinnerdrinks = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐饮品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7132");
                map.put("amount", "1");
                map.put("roomrate", Banquethalldinnerdrinks);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7134")) {
                BigDecimal Banquethalldinnerrental = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐场租 5%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7134");
                map.put("amount", "1");
                map.put("roomrate", Banquethalldinnerrental);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7133")) {
                BigDecimal Banquethalldinnerother = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐其他 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7133");
                map.put("amount", "1");
                map.put("roomrate", Banquethalldinnerother);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7115")) {
                BigDecimal breakfastinbanquethall = new BigDecimal(list.get(i).getPrice());// 宴会厅早餐服务费 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7115");
                map.put("amount", "1");
                map.put("roomrate", breakfastinbanquethall);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7125")) {
                BigDecimal servicechargevat = new BigDecimal(list.get(i).getPrice());//宴会厅午餐服务费 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7125");
                map.put("amount", "1");
                map.put("roomrate", servicechargevat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7135")) {
                BigDecimal dinnerservicechargevat = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐服务费 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7135");
                map.put("amount", "1");
                map.put("roomrate", dinnerservicechargevat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7119")) {
                BigDecimal Banquethallbreakfastadjustment = new BigDecimal(list.get(i).getPrice());// 宴会厅早餐调整 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7119");
                map.put("amount", "1");
                map.put("roomrate", Banquethallbreakfastadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7129")) {
                BigDecimal Banquethalllunchadjustment = new BigDecimal(list.get(i).getPrice());//宴会厅午餐调整 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7129");
                map.put("amount", "1");
                map.put("roomrate", Banquethalllunchadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7139")) {
                BigDecimal Banquethalldinneradjustment = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐调整 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7139");
                map.put("amount", "1");
                map.put("roomrate", Banquethalldinneradjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7144")) {
                BigDecimal Banquethallrental = new BigDecimal(list.get(i).getPrice());//宴会厅场租5%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7144");
                map.put("amount", "1");
                map.put("roomrate", Banquethallrental);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7149")) {
                BigDecimal Banquethallrentadjustment = new BigDecimal(list.get(i).getPrice());//宴会厅场租调整5%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7149");
                map.put("amount", "1");
                map.put("roomrate", Banquethallrentadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7211")) {
                BigDecimal Cafebreakfastvat = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐食品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7211");
                map.put("amount", "1");
                map.put("roomrate", Cafebreakfastvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7212")) {
                BigDecimal Coffeeshopbreakfastdrinks = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐饮品6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7212");
                map.put("amount", "1");
                map.put("roomrate", Coffeeshopbreakfastdrinks);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7213")) {
                BigDecimal Cafebreakfastothervat = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐其他 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7213");
                map.put("amount", "1");
                map.put("roomrate", Cafebreakfastothervat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7221")) {
                BigDecimal Cafelunchvat = new BigDecimal(list.get(i).getPrice());//咖啡厅午餐食品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7221");
                map.put("amount", "1");
                map.put("roomrate", Cafelunchvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7222")) {
                BigDecimal Cafelunchdrinksvat = new BigDecimal(list.get(i).getPrice());//咖啡厅午餐饮品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7222");
                map.put("amount", "1");
                map.put("roomrate", Cafelunchdrinksvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7223")) {
                BigDecimal Cafelunchothervat = new BigDecimal(list.get(i).getPrice());// 咖啡厅午餐其他 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7223");
                map.put("amount", "1");
                map.put("roomrate", Cafelunchothervat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7231")) {
                BigDecimal Cafedinnerfoodvat = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐食品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7231");
                map.put("amount", "1");
                map.put("roomrate", Cafedinnerfoodvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7232")) {
                BigDecimal Cafedinnerdrinksvat = new BigDecimal(list.get(i).getPrice());// 咖啡厅晚餐饮品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7232");
                map.put("amount", "1");
                map.put("roomrate", Cafedinnerdrinksvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7233")) {
                BigDecimal Cafedinnerothervat = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐其他 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7233");
                map.put("amount", "1");
                map.put("roomrate", Cafedinnerothervat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7215")) {
                BigDecimal servicechargevat6 = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐服务费 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7215");
                map.put("amount", "1");
                map.put("roomrate", servicechargevat6);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7225")) {
                BigDecimal Cafelunchservicevat = new BigDecimal(list.get(i).getPrice());// 咖啡厅午餐服务费 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7225");
                map.put("amount", "1");
                map.put("roomrate", Cafelunchservicevat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7235")) {
                BigDecimal Cafedinnerservicevat = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐服务费 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7235");
                map.put("amount", "1");
                map.put("roomrate", Cafedinnerservicevat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);

            }
            if (transactioncode.equals("7219")) {
                BigDecimal Cafebreakfastadjustmentvat = new BigDecimal(list.get(i).getPrice());// 咖啡厅早餐调整 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7219");
                map.put("amount", "1");
                map.put("roomrate", Cafebreakfastadjustmentvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7229")) {
                BigDecimal Cafelunchadjustmentvat = new BigDecimal(list.get(i).getPrice());//咖啡厅午餐调整 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7229");
                map.put("amount", "1");
                map.put("roomrate", Cafelunchadjustmentvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7239")) {
                BigDecimal Cafedinneradjustmentvat = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐调整 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7239");
                map.put("amount", "1");
                map.put("roomrate", Cafedinneradjustmentvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7311")) {
                BigDecimal Roomservicebreakfastfoodvat = new BigDecimal(list.get(i).getPrice());//客房送餐早餐食品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7311");
                map.put("amount", "1");
                map.put("roomrate", Roomservicebreakfastfoodvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7312")) {
                BigDecimal Roomservicebreakfastdrinksvat = new BigDecimal(list.get(i).getPrice());//客房送餐早餐饮品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7312");
                map.put("amount", "1");
                map.put("roomrate", Roomservicebreakfastdrinksvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7313")) {
                BigDecimal Roomservicebreakfastothervat = new BigDecimal(list.get(i).getPrice());//客房送餐早餐其他 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7313");
                map.put("amount", "1");
                map.put("roomrate", Roomservicebreakfastothervat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7321")) {
                BigDecimal Roomservicelunchvat = new BigDecimal(list.get(i).getPrice());// 客房送餐午餐食品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7321");
                map.put("amount", "1");
                map.put("roomrate", Roomservicelunchvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7322")) {
                BigDecimal Roomservicelunchdrinksvat = new BigDecimal(list.get(i).getPrice());//客房送餐午餐饮品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7322");
                map.put("amount", "1");
                map.put("roomrate", Roomservicelunchdrinksvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7323")) {
                BigDecimal Roomservicelunchothervat = new BigDecimal(list.get(i).getPrice());//客房送餐午餐其他 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7323");
                map.put("amount", "1");
                map.put("roomrate", Roomservicelunchothervat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7331")) {
                BigDecimal Roomservicedinnerfoodvat = new BigDecimal(list.get(i).getPrice());//客房送餐晚餐食品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7331");
                map.put("amount", "1");
                map.put("roomrate", Roomservicedinnerfoodvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7332")) {
                BigDecimal Roomservicedinnerdrinksvat = new BigDecimal(list.get(i).getPrice());//客房送餐晚餐饮品 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7332");
                map.put("amount", "1");
                map.put("roomrate", Roomservicedinnerdrinksvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7333")) {
                BigDecimal roomservice = new BigDecimal(list.get(i).getPrice());// 客房送餐
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7333");
                map.put("amount", "1");
                map.put("roomrate", roomservice);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7315")) {
                BigDecimal Roomservicechargeforbreakfastvat = new BigDecimal(list.get(i).getPrice());// 客房送餐早餐服务费 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7315");
                map.put("amount", "1");
                map.put("roomrate", Roomservicechargeforbreakfastvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7325")) {
                BigDecimal Roomservicelunchvatvat = new BigDecimal(list.get(i).getPrice());//客房送餐午餐服务费 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7325");
                map.put("amount", "1");
                map.put("roomrate", Roomservicelunchvatvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7335")) {
                BigDecimal Roomservicefordinnervat = new BigDecimal(list.get(i).getPrice());// 客房送餐晚餐服务费 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7335");
                map.put("amount", "1");
                map.put("roomrate", Roomservicefordinnervat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7319")) {
                BigDecimal Roomservicebreakfastadjustmentvat = new BigDecimal(list.get(i).getPrice());//客房送餐早餐调整 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7319");
                map.put("amount", "1");
                map.put("roomrate", Roomservicebreakfastadjustmentvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7329")) {
                BigDecimal Roomservicelunchadjustmentvat = new BigDecimal(list.get(i).getPrice());//客房送餐午餐调整 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7329");
                map.put("amount", "1");
                map.put("roomrate", Roomservicelunchadjustmentvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("7339")) {
                BigDecimal Roomservicedinneradjustmentvat = new BigDecimal(list.get(i).getPrice());// 客房送餐晚餐调整 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "7339");
                map.put("amount", "1");
                map.put("roomrate", Roomservicedinneradjustmentvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6208")) {
                BigDecimal charterhire = new BigDecimal(list.get(i).getPrice());//租金 5%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6208");
                map.put("amount", "1");
                map.put("roomrate", charterhire);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("8000")) {
                BigDecimal Acashrefund = new BigDecimal(list.get(i).getPrice());//现金退款
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "8000");
                map.put("amount", "1");
                map.put("roomrate", Acashrefund);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9000")) {
                BigDecimal cash = new BigDecimal(list.get(i).getPrice());// 现金
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9000");
                map.put("amount", "1");
                map.put("roomrate", cash);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9130")) {
                BigDecimal WeChatPay = new BigDecimal(list.get(i).getPrice());//微信支付
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9130");
                map.put("amount", "1");
                map.put("roomrate", WeChatPay);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9140")) {
                BigDecimal Alipay = new BigDecimal(list.get(i).getPrice());//支付宝
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9140");
                map.put("amount", "1");
                map.put("roomrate", Alipay);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9150")) {
                BigDecimal banktransfer = new BigDecimal(list.get(i).getPrice());//银行汇款
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9150");
                map.put("amount", "1");
                map.put("roomrate", banktransfer);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9160")) {
                BigDecimal membershipcard = new BigDecimal(list.get(i).getPrice());//会员卡
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9160");
                map.put("amount", "1");
                map.put("roomrate", membershipcard);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9200")) {
                BigDecimal POScash = new BigDecimal(list.get(i).getPrice());//POS-现金
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9200");
                map.put("amount", "1");
                map.put("roomrate", POScash);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9300")) {
                BigDecimal POSForeigncard = new BigDecimal(list.get(i).getPrice());//POS-国外卡
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9300");
                map.put("amount", "1");
                map.put("roomrate", POSForeigncard);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9320")) {
                BigDecimal POSDomesticcard = new BigDecimal(list.get(i).getPrice());//POS-国内卡
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9320");
                map.put("amount", "1");
                map.put("roomrate", POSDomesticcard);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9340")) {
                BigDecimal poswx = new BigDecimal(list.get(i).getPrice());//POS-微信
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9340");
                map.put("amount", "1");
                map.put("roomrate", poswx);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9350")) {
                BigDecimal poszfb = new BigDecimal(list.get(i).getPrice());//POS-支付宝
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9350");
                map.put("amount", "1");
                map.put("roomrate", poszfb);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9330")) {
                BigDecimal POSPaymentofthecity = new BigDecimal(list.get(i).getPrice());//POS-城市挂账
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9330");
                map.put("amount", "1");
                map.put("roomrate", POSPaymentofthecity);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9030")) {
                BigDecimal Paymentofthecity = new BigDecimal(list.get(i).getPrice());//城市挂账
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9030");
                map.put("amount", "1");
                map.put("roomrate", Paymentofthecity);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9360")) {
                BigDecimal posvip = new BigDecimal(list.get(i).getPrice());//POS-会员
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9360");
                map.put("amount", "1");
                map.put("roomrate", posvip);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9500")) {
                BigDecimal Cashreceivable = new BigDecimal(list.get(i).getPrice());//AR-应收现金
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9500");
                map.put("amount", "1");
                map.put("roomrate", Cashreceivable);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9520")) {
                BigDecimal Bankreceivables = new BigDecimal(list.get(i).getPrice());//AR-应收银行回款
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9520");
                map.put("amount", "1");
                map.put("roomrate", Bankreceivables);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9540")) {
                BigDecimal commission = new BigDecimal(list.get(i).getPrice());//AR-佣金
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9540");
                map.put("amount", "1");
                map.put("roomrate", commission);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9550")) {
                BigDecimal Creditcardcollection = new BigDecimal(list.get(i).getPrice());// AR-信用卡回款(信用卡）
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9550");
                map.put("amount", "1");
                map.put("roomrate", Creditcardcollection);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9551")) {
                BigDecimal Creditcarddebit = new BigDecimal(list.get(i).getPrice());//AR-信用卡回款（挂账）
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9551");
                map.put("amount", "1");
                map.put("roomrate", Creditcarddebit);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9560")) {
                BigDecimal Creditcrdcommission = new BigDecimal(list.get(i).getPrice());//AR-信用卡佣金(信用卡）
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9560");
                map.put("amount", "1");
                map.put("roomrate", Creditcrdcommission);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9561")) {
                BigDecimal Creditcardcommissioncharge = new BigDecimal(list.get(i).getPrice());//AR-信用卡佣金(挂账）
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9561");
                map.put("amount", "1");
                map.put("roomrate", Creditcardcommissioncharge);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9570")) {
                BigDecimal ARAccountsreceivablerefund = new BigDecimal(list.get(i).getPrice());// AR-应收退款
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9570");
                map.put("amount", "1");
                map.put("roomrate", ARAccountsreceivablerefund);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9571")) {
                BigDecimal compress = new BigDecimal(list.get(i).getPrice());//AR-压缩
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9571");
                map.put("amount", "1");
                map.put("roomrate", compress);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9590")) {
                BigDecimal Generalledgerhedge = new BigDecimal(list.get(i).getPrice());// AR-应收总帐对冲
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9590");
                map.put("amount", "1");
                map.put("roomrate", Generalledgerhedge);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9999")) {
                BigDecimal Systemadjustment = new BigDecimal(list.get(i).getPrice());//系统调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9999");
                map.put("amount", "1");
                map.put("roomrate", Systemadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5509")) {
                BigDecimal Adjustmentofpickupandpickup = new BigDecimal(list.get(i).getPrice());//代收接送机调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5509");
                map.put("amount", "1");
                map.put("roomrate", Adjustmentofpickupandpickup);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2201")) {
                BigDecimal Cafelunchpackage = new BigDecimal(list.get(i).getPrice());//咖啡厅午餐包价
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2201");
                map.put("amount", "1");
                map.put("roomrate", Cafelunchpackage);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2202")) {
                BigDecimal Cafedinnerincluded = new BigDecimal(list.get(i).getPrice());// 咖啡厅晚餐包价
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2202");
                map.put("amount", "1");
                map.put("roomrate", Cafedinnerincluded);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1210")) {
                BigDecimal Guestroompackage = new BigDecimal(list.get(i).getPrice());//客房包价
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1210");
                map.put("amount", "1");
                map.put("roomrate", Guestroompackage);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5540")) {
                BigDecimal Honeymoonarrangement = new BigDecimal(list.get(i).getPrice());// 蜜月布置
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5540");
                map.put("amount", "1");
                map.put("roomrate", Honeymoonarrangement);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("1070")) {
                BigDecimal Toupgradetheroomcharge = new BigDecimal(list.get(i).getPrice());//升级房费
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "1070");
                map.put("amount", "1");
                map.put("roomrate", Toupgradetheroomcharge);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("8790")) {
                BigDecimal Balancetransfer = new BigDecimal(list.get(i).getPrice());// 余额转移
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "8790");
                map.put("amount", "1");
                map.put("roomrate", Balancetransfer);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6081")) {
                BigDecimal Recreationcentrefood = new BigDecimal(list.get(i).getPrice());//康体中心-食品 6%
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6081");
                map.put("amount", "1");
                map.put("roomrate", Recreationcentrefood);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6082")) {
                BigDecimal Recreationcentrebeverages = new BigDecimal(list.get(i).getPrice());//康体中心-饮品6%
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6082");
                map.put("amount", "1");
                map.put("roomrate", Recreationcentrebeverages);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6083")) {
                BigDecimal Recreationcentreswimmingring = new BigDecimal(list.get(i).getPrice());//康体中心-泳圈6%
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6083");
                map.put("amount", "1");
                map.put("roomrate", Recreationcentreswimmingring);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6084")) {
                BigDecimal Recreationcentrefishtherapy = new BigDecimal(list.get(i).getPrice());//康体中心-鱼疗 6%
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6084");
                map.put("amount", "1");
                map.put("roomrate", Recreationcentrefishtherapy);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6085")) {
                BigDecimal Recreationcentrebicycles = new BigDecimal(list.get(i).getPrice());//康体中心-自行车 6%
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6085");
                map.put("amount", "1");
                map.put("roomrate", Recreationcentrebicycles);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6086")) {
                BigDecimal Sandtoysforchildren = new BigDecimal(list.get(i).getPrice());//康体中心-儿童沙雕玩具
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6086");
                map.put("amount", "1");
                map.put("roomrate", Sandtoysforchildren);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6087")) {
                BigDecimal Recreationcentertax = new BigDecimal(list.get(i).getPrice());//康体中心税金 6%
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6087");
                map.put("amount", "1");
                map.put("roomrate", Recreationcentertax);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6009")) {
                BigDecimal Recreationswimwear = new BigDecimal(list.get(i).getPrice());//康乐-泳衣
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6009");
                map.put("amount", "1");
                map.put("roomrate", Recreationswimwear);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6089")) {
                BigDecimal Recreationswimwear6 = new BigDecimal(list.get(i).getPrice());//康乐-泳衣 6%
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6089");
                map.put("amount", "1");
                map.put("roomrate", Recreationswimwear6);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6092")) {
                BigDecimal beverageadjustment = new BigDecimal(list.get(i).getPrice());//康体中心-饮品调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6092");
                map.put("amount", "1");
                map.put("roomrate", beverageadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6093")) {
                BigDecimal swimmingringadjustment = new BigDecimal(list.get(i).getPrice());//康体中心-泳圈调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6093");
                map.put("amount", "1");
                map.put("roomrate", swimmingringadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6094")) {
                BigDecimal Fishtoadjust = new BigDecimal(list.get(i).getPrice());// 康体中心-鱼疗调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6094");
                map.put("amount", "1");
                map.put("roomrate", Fishtoadjust);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6095")) {
                BigDecimal Bicycleadjustment = new BigDecimal(list.get(i).getPrice());//康体中心-自行车调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6095");
                map.put("amount", "1");
                map.put("roomrate", Bicycleadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6096")) {
                BigDecimal childrenstoys = new BigDecimal(list.get(i).getPrice());//康体中心-儿童玩具调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6096");
                map.put("amount", "1");
                map.put("roomrate", childrenstoys);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6097")) {
                BigDecimal Otheradjustments = new BigDecimal(list.get(i).getPrice());//康体中心-其它调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6097");
                map.put("amount", "1");
                map.put("roomrate", Otheradjustments);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6099")) {
                BigDecimal Theswimsuitadjustment = new BigDecimal(list.get(i).getPrice());// 康乐-泳衣调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6099");
                map.put("amount", "1");
                map.put("roomrate", Theswimsuitadjustment);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5519")) {
                BigDecimal LandmarkTicket = new BigDecimal(list.get(i).getPrice());//代收景点门票调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5519");
                map.put("amount", "1");
                map.put("roomrate", LandmarkTicket);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5529")) {
                BigDecimal utilitybills = new BigDecimal(list.get(i).getPrice());//代收水电费调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5529");
                map.put("amount", "1");
                map.put("roomrate", utilitybills);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5539")) {
                BigDecimal Othercollectionadjust = new BigDecimal(list.get(i).getPrice());//其它代收调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5539");
                map.put("amount", "1");
                map.put("roomrate", Othercollectionadjust);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5549")) {
                BigDecimal honeymoonadjust = new BigDecimal(list.get(i).getPrice());//蜜月布置调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5549");
                map.put("amount", "1");
                map.put("roomrate", honeymoonadjust);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6001")) {
                BigDecimal foodstuff = new BigDecimal(list.get(i).getPrice());//康体中心-食品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6001");
                map.put("amount", "1");
                map.put("roomrate", foodstuff);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6002")) {
                BigDecimal Healthbeverage = new BigDecimal(list.get(i).getPrice());//康体中心-饮品
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6002");
                map.put("amount", "1");
                map.put("roomrate", Healthbeverage);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6003")) {
                BigDecimal swimring = new BigDecimal(list.get(i).getPrice());// 康体中心-泳圈
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6003");
                map.put("amount", "1");
                map.put("roomrate", swimring);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6004")) {
                BigDecimal Fishpedicure = new BigDecimal(list.get(i).getPrice());// 康体中心-鱼疗
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6004");
                map.put("amount", "1");
                map.put("roomrate", Fishpedicure);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6005")) {
                BigDecimal sportsbicycle = new BigDecimal(list.get(i).getPrice());//康体中心-自行车
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6005");
                map.put("amount", "1");
                map.put("roomrate", sportsbicycle);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6006")) {
                BigDecimal sportschildrenstoys = new BigDecimal(list.get(i).getPrice());//康体中心-儿童玩具
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6006");
                map.put("amount", "1");
                map.put("roomrate", sportschildrenstoys);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6007")) {
                BigDecimal sportsother = new BigDecimal(list.get(i).getPrice());//康体中心-其它
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6007");
                map.put("amount", "1");
                map.put("roomrate", sportsother);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6000")) {
                BigDecimal Theotherpackage = new BigDecimal(list.get(i).getPrice());//康乐其它包价
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6000");
                map.put("amount", "1");
                map.put("roomrate", Theotherpackage);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("8999")) {
                BigDecimal Systemswitchingbalance = new BigDecimal(list.get(i).getPrice());//系统切换余额
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "8999");
                map.put("amount", "1");
                map.put("roomrate", Systemswitchingbalance);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("8980")) {
                BigDecimal switchingbalance = new BigDecimal(list.get(i).getPrice());//系统切换余额(前台暂挂账)
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "8980");
                map.put("amount", "1");
                map.put("roomrate", switchingbalance);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9010")) {
                BigDecimal cheque = new BigDecimal(list.get(i).getPrice());//支票
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9010");
                map.put("amount", "1");
                map.put("roomrate", cheque);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9100")) {
                BigDecimal Foreigncard = new BigDecimal(list.get(i).getPrice());//国外卡
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9100");
                map.put("amount", "1");
                map.put("roomrate", Foreigncard);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9110")) {
                BigDecimal Domesticcard = new BigDecimal(list.get(i).getPrice());//国内卡
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9110");
                map.put("amount", "1");
                map.put("roomrate", Domesticcard);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9120")) {
                BigDecimal UnionPaycard = new BigDecimal(list.get(i).getPrice());//银联卡
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9120");
                map.put("amount", "1");
                map.put("roomrate", UnionPaycard);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9310")) {
                BigDecimal POSUnionPaycard = new BigDecimal(list.get(i).getPrice());// POS-银联卡
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9310");
                map.put("amount", "1");
                map.put("roomrate", POSUnionPaycard);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6010")) {
                BigDecimal footwear = new BigDecimal(list.get(i).getPrice());//康乐-球类
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6010");
                map.put("amount", "1");
                map.put("roomrate", footwear);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6011")) {
                BigDecimal Frozencoconutsetmeal = new BigDecimal(list.get(i).getPrice());//康乐-冰冻椰青套餐
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6011");
                map.put("amount", "1");
                map.put("roomrate", Frozencoconutsetmeal);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6012")) {
                BigDecimal Fishmeal = new BigDecimal(list.get(i).getPrice());// 康乐-鱼疗套餐
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6012");
                map.put("amount", "1");
                map.put("roomrate", Fishmeal);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6013")) {
                BigDecimal Swimminglapspackage = new BigDecimal(list.get(i).getPrice());//康乐-泳圈套餐
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6013");
                map.put("amount", "1");
                map.put("roomrate", Swimminglapspackage);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6014")) {
                BigDecimal Icecreamset = new BigDecimal(list.get(i).getPrice());// 康乐-冰淇淋套餐
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6014");
                map.put("amount", "1");
                map.put("roomrate", Icecreamset);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6015")) {
                BigDecimal Toypackages = new BigDecimal(list.get(i).getPrice());//康乐-玩具套餐
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6015");
                map.put("amount", "1");
                map.put("roomrate", Toypackages);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9361")) {
                BigDecimal JBCCard = new BigDecimal(list.get(i).getPrice());// POS-JCB卡
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9361");
                map.put("amount", "1");
                map.put("roomrate", JBCCard);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9362")) {
                BigDecimal DinersClubcard = new BigDecimal(list.get(i).getPrice());// POS-大莱卡
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9362");
                map.put("amount", "1");
                map.put("roomrate", DinersClubcard);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9363")) {
                BigDecimal Thetransportcard = new BigDecimal(list.get(i).getPrice());//POS-美运卡
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9363");
                map.put("amount", "1");
                map.put("roomrate", Thetransportcard);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9364")) {
                BigDecimal MasterCard = new BigDecimal(list.get(i).getPrice());//POS-万事达
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9364");
                map.put("amount", "1");
                map.put("roomrate", MasterCard);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9365")) {
                BigDecimal visacard = new BigDecimal(list.get(i).getPrice());// POS-维萨卡
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9365");
                map.put("amount", "1");
                map.put("roomrate", visacard);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9366")) {
                BigDecimal POSUnionPaycard2 = new BigDecimal(list.get(i).getPrice());//POS-银联卡
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9366");
                map.put("amount", "1");
                map.put("roomrate", POSUnionPaycard2);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("9367")) {
                BigDecimal roomcharge = new BigDecimal(list.get(i).getPrice());//房帐
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "9367");
                map.put("amount", "1");
                map.put("roomrate", roomcharge);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2203")) {
                BigDecimal Coffeeshopdrinksincluded = new BigDecimal(list.get(i).getPrice());//咖啡厅酒水包价
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2203");
                map.put("amount", "1");
                map.put("roomrate", Coffeeshopdrinksincluded);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6020")) {
                BigDecimal packagepricedrink = new BigDecimal(list.get(i).getPrice());//康乐饮品包价
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6020");
                map.put("amount", "1");
                map.put("roomrate", packagepricedrink);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6030")) {
                BigDecimal packagepricefood = new BigDecimal(list.get(i).getPrice());//康乐食品包价
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6030");
                map.put("amount", "1");
                map.put("roomrate", packagepricefood);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("2204")) {
                BigDecimal otherone = new BigDecimal(list.get(i).getPrice());//咖啡厅其它包价
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "2204");
                map.put("amount", "1");
                map.put("roomrate", otherone);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6120")) {
                BigDecimal otherrevenue = new BigDecimal(list.get(i).getPrice());//其他收入5%税
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6120");
                map.put("amount", "1");
                map.put("roomrate", otherrevenue);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6130")) {
                BigDecimal otherrevenuetax = new BigDecimal(list.get(i).getPrice());//其他收入
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6130");
                map.put("amount", "1");
                map.put("roomrate", otherrevenuetax);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6140")) {
                BigDecimal tallage9 = new BigDecimal(list.get(i).getPrice());//其他收入9%税
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6140");
                map.put("amount", "1");
                map.put("roomrate", tallage9);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6150")) {
                BigDecimal tallage13 = new BigDecimal(list.get(i).getPrice());// 其他收入13%税
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6150");
                map.put("amount", "1");
                map.put("roomrate", tallage13);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6128")) {
                BigDecimal tallage5 = new BigDecimal(list.get(i).getPrice());//其他收入 5%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6128");
                map.put("amount", "1");
                map.put("roomrate", tallage5);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6138")) {
                BigDecimal tallage6 = new BigDecimal(list.get(i).getPrice());//其他收入 6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6138");
                map.put("amount", "1");
                map.put("roomrate", tallage6);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6148")) {
                BigDecimal tallagevat9 = new BigDecimal(list.get(i).getPrice());//其他收入 9%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6148");
                map.put("amount", "1");
                map.put("roomrate", tallagevat9);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6158")) {
                BigDecimal tallage13vat = new BigDecimal(list.get(i).getPrice());// 其他收入 13%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6158");
                map.put("amount", "1");
                map.put("roomrate", tallage13vat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6129")) {
                BigDecimal taxadjustment5 = new BigDecimal(list.get(i).getPrice());// 其它收入5%税调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6129");
                map.put("amount", "1");
                map.put("roomrate", taxadjustment5);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6139")) {
                BigDecimal taxadjustment6 = new BigDecimal(list.get(i).getPrice());//其他收入6%税调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6139");
                map.put("amount", "1");
                map.put("roomrate", taxadjustment6);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);

            }
            if (transactioncode.equals("6149")) {
                BigDecimal taxadjustment9 = new BigDecimal(list.get(i).getPrice());//其他收入9%税调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6149");
                map.put("amount", "1");
                map.put("roomrate", taxadjustment9);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("6159")) {
                BigDecimal taxadjustment13 = new BigDecimal(list.get(i).getPrice());// 其他收入13%税调整
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "6159");
                map.put("amount", "1");
                map.put("roomrate", taxadjustment13);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5410")) {
                BigDecimal Thecollectingpackage = new BigDecimal(list.get(i).getPrice());// 代收包价
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5410");
                map.put("amount", "1");
                map.put("roomrate", Thecollectingpackage);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5548")) {
                BigDecimal Honeymoonarrangementvat = new BigDecimal(list.get(i).getPrice());//蜜月布置6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5548");
                map.put("amount", "1");
                map.put("roomrate", Honeymoonarrangementvat);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
            if (transactioncode.equals("5507")) {
                BigDecimal Thecollectingtaxes = new BigDecimal(list.get(i).getPrice());// 代收税金6%VAT
                Map<String, Object> map = new HashMap<>();
                map.put("resrowid", resrowid);
                map.put("roomkey", roomkey);
                map.put("TransactionCode", "5507");
                map.put("amount", "1");
                map.put("roomrate", Thecollectingtaxes);
                map.put("subcode", subcode);
                map.put("username", username);
                int flag = iklrecordService.insertentry(map);
            }
        }

        return R.ok();
    }
}
