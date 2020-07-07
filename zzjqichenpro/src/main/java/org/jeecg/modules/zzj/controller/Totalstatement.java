package org.jeecg.modules.zzj.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.zzj.entity.Consumption;
import org.jeecg.modules.zzj.entity.Klbill;
import org.jeecg.modules.zzj.service.IExpensedetailService;
import org.jeecg.modules.zzj.service.IklrecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@Api(tags = "退房客户全部消费明细")
@RestController
@RequestMapping("/zzj/klprofile")
public class Totalstatement {

    @Autowired
    private IklrecordService iklrecordService;
    @Autowired
    private IExpensedetailService iExpensedetailService;

    /**
     * 查询账单明细
     *
     * @param roomkey  房间号
     * @param request
     * @param response
     */
    @ApiOperation(value = "查看退房客户消费明细")
    @RequestMapping(value = "/queryklbill", method = RequestMethod.GET)
    public void queryklbill(String roomkey, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        if (StringUtils.isEmpty(roomkey)) {
           return;
        }
        Consumption consumption = new Consumption();
        List<Klbill> list = null;
        try {
            list = iklrecordService.queryklbill(roomkey);
        } catch (Exception e) {
            throw new RuntimeException("参数为空.");
        }
        if (list == null) {
            iExpensedetailService.insertionlist(list);
            return;
        }
        BigDecimal prices = new BigDecimal("0");//总消费
        BigDecimal roomrate = new BigDecimal("0");
        BigDecimal anroomrate = new BigDecimal("0");
        BigDecimal rateupselling = new BigDecimal("0");
        BigDecimal allroomcharge = new BigDecimal("0");
        BigDecimal halfdayroomcharge = new BigDecimal("0");
        BigDecimal bed = new BigDecimal("0");
        BigDecimal cancellation = new BigDecimal("0");
        BigDecimal adjustment = new BigDecimal("0");
        BigDecimal Roomservicecharge = new BigDecimal("0");
        BigDecimal Roomservicechargeadjustment = new BigDecimal("0");
        BigDecimal Roommiscellaneousincome = new BigDecimal("0");
        BigDecimal incomeadjustment = new BigDecimal("0");
        BigDecimal Roomincome = new BigDecimal("0");
        BigDecimal Theroomcharge = new BigDecimal("0");
        BigDecimal Breakfastfood = new BigDecimal("0");
        BigDecimal breakfastdrinks = new BigDecimal("0");
        BigDecimal Greenfeesforbreakfast = new BigDecimal("0");
        BigDecimal Otherbreakfast = new BigDecimal("0");
        BigDecimal Foodforlunch = new BigDecimal("0");
        BigDecimal Drinkforlunch = new BigDecimal("0");
        BigDecimal forbreakfast = new BigDecimal("0");
        BigDecimal Banquethalllunchother = new BigDecimal("0");
        BigDecimal fordinner = new BigDecimal("0");
        BigDecimal Halldinnerdrinks = new BigDecimal("0");
        BigDecimal Rentals = new BigDecimal("0");
        BigDecimal Otherdinner = new BigDecimal("0");
        BigDecimal servicecharge = new BigDecimal("0");
        BigDecimal Mealservicecharge = new BigDecimal("0");
        BigDecimal Dinnerservicecharge = new BigDecimal("0");
        BigDecimal Adjustthebreakfast = new BigDecimal("0");
        BigDecimal Adjustthelunch = new BigDecimal("0");
        BigDecimal Banquethallbreakfastdrinks = new BigDecimal("0");
        BigDecimal Banquethalldinnerfood = new BigDecimal("0");
        BigDecimal Banquethalldinnerdrinks = new BigDecimal("0");
        BigDecimal Dinnertoadjust = new BigDecimal("0");
        BigDecimal Hallgreenfees = new BigDecimal("0");
        BigDecimal rentadjustment = new BigDecimal("0");
        BigDecimal Cafebreakfast = new BigDecimal("0");
        BigDecimal Breakfastdrink = new BigDecimal("0");
        BigDecimal Cafebreakfastother = new BigDecimal("0");
        BigDecimal Cafelunch = new BigDecimal("0");
        BigDecimal Cafelunchdrinks = new BigDecimal("0");
        BigDecimal Cafelunchother = new BigDecimal("0");
        BigDecimal Cafedinnerfood = new BigDecimal("0");
        BigDecimal Cafedinnerdrinks = new BigDecimal("0");
        BigDecimal Cafedinnerother = new BigDecimal("0");
        BigDecimal Coffeeshop = new BigDecimal("0");
        BigDecimal Cafebreakfastservicecharge = new BigDecimal("0");
        BigDecimal Cafelunchservice = new BigDecimal("0");
        BigDecimal Cafedinnerservice = new BigDecimal("0");
        BigDecimal Cafebreakfastadjustment = new BigDecimal("0");
        BigDecimal Cafelunchadjustment = new BigDecimal("0");
        BigDecimal Cafedinneradjustment = new BigDecimal("0");
        BigDecimal Roomservicebreakfastfood = new BigDecimal("0");
        BigDecimal Roomservicebreakfastdrinks = new BigDecimal("0");
        BigDecimal Roomservicebreakfastother = new BigDecimal("0");
        BigDecimal Roomservicelunch = new BigDecimal("0");
        BigDecimal Roomservicelunchdrinks = new BigDecimal("0");
        BigDecimal Roomservicelunchother = new BigDecimal("0");
        BigDecimal Roomservicedinnerfood = new BigDecimal("0");
        BigDecimal Roomservicedinnerdrinks = new BigDecimal("0");
        BigDecimal Roomservicedinnerother = new BigDecimal("0");
        BigDecimal Roomservicechargeforbreakfast = new BigDecimal("0");
        BigDecimal Roomservicelunchservicecharge = new BigDecimal("0");
        BigDecimal Roomservicefordinner = new BigDecimal("0");
        BigDecimal Roomservicebreakfastadjustment = new BigDecimal("0");
        BigDecimal Roomservicelunchadjustment = new BigDecimal("0");
        BigDecimal Roomservicedinneradjustment = new BigDecimal("0");
        BigDecimal localcalls = new BigDecimal("0");
        BigDecimal domestictollcall = new BigDecimal("0");
        BigDecimal IDD = new BigDecimal("0");
        BigDecimal Telephonerateadjustment = new BigDecimal("0");
        BigDecimal Telephonecharges9 = new BigDecimal("0");
        BigDecimal Businesscenterprint = new BigDecimal("0");
        BigDecimal Adjustmentofbusinesscenter = new BigDecimal("0");
        BigDecimal CBD = new BigDecimal("0");
        BigDecimal miniba = new BigDecimal("0");
        BigDecimal minibavat = new BigDecimal("0");
        BigDecimal Minibaradjustment = new BigDecimal("0");
        BigDecimal Laundryoutsidethestore = new BigDecimal("0");
        BigDecimal Laundrychargeinstore = new BigDecimal("0");
        BigDecimal Laundrychargeadjustment = new BigDecimal("0");
        BigDecimal laundrycharge = new BigDecimal("0");
        BigDecimal compensatefor = new BigDecimal("0");
        BigDecimal compensateforvat = new BigDecimal("0");
        BigDecimal Thecompensationadjustment = new BigDecimal("0");
        BigDecimal carrental = new BigDecimal("0");
        BigDecimal adjustcarrental = new BigDecimal("0");
        BigDecimal Pickupanddropoff = new BigDecimal("0");
        BigDecimal scenicspots = new BigDecimal("0");
        BigDecimal Collectutilities = new BigDecimal("0");
        BigDecimal Othercollection = new BigDecimal("0");
        BigDecimal Healthcentrefoodadjustment = new BigDecimal("0");
        BigDecimal miscellaneousincome = new BigDecimal("0");
        BigDecimal adjustmiscellaneousincome = new BigDecimal("0");
        BigDecimal miscellaneousincomevat = new BigDecimal("0");
        BigDecimal rental = new BigDecimal("0");
        BigDecimal adjustrental = new BigDecimal("0");
        BigDecimal breakfastfoodvat = new BigDecimal("0");
        BigDecimal Greenfeesforbreakfastvat = new BigDecimal("0");
        BigDecimal Banquethallbreakfastother = new BigDecimal("0");
        BigDecimal Banquethalllunchfoodvat = new BigDecimal("0");
        BigDecimal Banquethalllunchdrinks = new BigDecimal("0");
        BigDecimal Banquethalllunchrental = new BigDecimal("0");
        BigDecimal Banquethalllunchothervat = new BigDecimal("0");
        BigDecimal Banquethalldinnerrental = new BigDecimal("0");
        BigDecimal Banquethalldinnerother = new BigDecimal("0");
        BigDecimal breakfastinbanquethall = new BigDecimal("0");
        BigDecimal servicechargevat = new BigDecimal("0");
        BigDecimal dinnerservicechargevat = new BigDecimal("0");
        BigDecimal Banquethallbreakfastadjustment = new BigDecimal("0");
        BigDecimal Banquethalllunchadjustment = new BigDecimal("0");
        BigDecimal Banquethalldinneradjustment = new BigDecimal("0");
        BigDecimal Banquethallrental = new BigDecimal("0");
        BigDecimal Banquethallrentadjustment = new BigDecimal("0");
        BigDecimal Cafebreakfastvat = new BigDecimal("0");
        BigDecimal Coffeeshopbreakfastdrinks = new BigDecimal("0");
        BigDecimal Cafebreakfastothervat = new BigDecimal("0");
        BigDecimal Cafelunchvat = new BigDecimal("0");
        BigDecimal Cafelunchdrinksvat = new BigDecimal("0");
        BigDecimal Cafelunchothervat = new BigDecimal("0");
        BigDecimal Cafedinnerfoodvat = new BigDecimal("0");
        BigDecimal Cafedinnerdrinksvat = new BigDecimal("0");
        BigDecimal Cafedinnerothervat = new BigDecimal("0");
        BigDecimal servicechargevat6 = new BigDecimal("0");
        BigDecimal Cafelunchservicevat = new BigDecimal("0");
        BigDecimal Cafedinnerservicevat = new BigDecimal("0");
        BigDecimal Cafebreakfastadjustmentvat = new BigDecimal("0");
        BigDecimal Cafelunchadjustmentvat = new BigDecimal("0");
        BigDecimal Cafedinneradjustmentvat = new BigDecimal("0");
        BigDecimal Roomservicebreakfastfoodvat = new BigDecimal("0");
        BigDecimal Roomservicebreakfastdrinksvat = new BigDecimal("0");
        BigDecimal Roomservicebreakfastothervat = new BigDecimal("0");
        BigDecimal Roomservicelunchvat = new BigDecimal("0");
        BigDecimal Roomservicelunchdrinksvat = new BigDecimal("0");
        BigDecimal Roomservicelunchothervat = new BigDecimal("0");
        BigDecimal Roomservicedinnerfoodvat = new BigDecimal("0");
        BigDecimal Roomservicedinnerdrinksvat = new BigDecimal("0");
        BigDecimal roomservice = new BigDecimal("0");
        BigDecimal Roomservicechargeforbreakfastvat = new BigDecimal("0");
        BigDecimal Roomservicelunchvatvat = new BigDecimal("0");
        BigDecimal Roomservicefordinnervat = new BigDecimal("0");
        BigDecimal Roomservicebreakfastadjustmentvat = new BigDecimal("0");
        BigDecimal Roomservicelunchadjustmentvat = new BigDecimal("0");
        BigDecimal Roomservicedinneradjustmentvat = new BigDecimal("0");
        BigDecimal charterhire = new BigDecimal("0");
        BigDecimal Acashrefund = new BigDecimal("0");
        BigDecimal cash = new BigDecimal("0");
        BigDecimal Paymentofthecity = new BigDecimal("0");
        BigDecimal WeChatPay = new BigDecimal("0");
        BigDecimal Alipay = new BigDecimal("0");
        BigDecimal banktransfer = new BigDecimal("0");
        BigDecimal membershipcard = new BigDecimal("0");
        BigDecimal POScash = new BigDecimal("0");
        BigDecimal POSForeigncard = new BigDecimal("0");
        BigDecimal POSDomesticcard = new BigDecimal("0");
        BigDecimal POSPaymentofthecity = new BigDecimal("0");
        BigDecimal poswx = new BigDecimal("0");
        BigDecimal poszfb = new BigDecimal("0");
        BigDecimal posvip = new BigDecimal("0");
        BigDecimal Cashreceivable = new BigDecimal("0");
        BigDecimal Bankreceivables = new BigDecimal("0");
        BigDecimal commission = new BigDecimal("0");
        BigDecimal Creditcardcollection = new BigDecimal("0");
        BigDecimal Creditcarddebit = new BigDecimal("0");
        BigDecimal Creditcrdcommission = new BigDecimal("0");
        BigDecimal Creditcardcommissioncharge = new BigDecimal("0");
        BigDecimal ARAccountsreceivablerefund = new BigDecimal("0");
        BigDecimal compress = new BigDecimal("0");
        BigDecimal Generalledgerhedge = new BigDecimal("0");
        BigDecimal Systemadjustment = new BigDecimal("0");
        BigDecimal Adjustmentofpickupandpickup = new BigDecimal("0");
        BigDecimal Cafelunchpackage = new BigDecimal("0");
        BigDecimal Cafedinnerincluded = new BigDecimal("0");
        BigDecimal Guestroompackage = new BigDecimal("0");
        BigDecimal Honeymoonarrangement = new BigDecimal("0");
        BigDecimal Toupgradetheroomcharge = new BigDecimal("0");
        BigDecimal Balancetransfer = new BigDecimal("0");
        BigDecimal Recreationcentrefood = new BigDecimal("0");
        BigDecimal Recreationcentrebeverages = new BigDecimal("0");
        BigDecimal Recreationcentreswimmingring = new BigDecimal("0");
        BigDecimal Recreationcentrefishtherapy = new BigDecimal("0");
        BigDecimal Recreationcentrebicycles = new BigDecimal("0");
        BigDecimal Sandtoysforchildren = new BigDecimal("0");
        BigDecimal Recreationcentertax = new BigDecimal("0");
        BigDecimal Recreationswimwear = new BigDecimal("0");
        BigDecimal Recreationswimwear6 = new BigDecimal("0");
        BigDecimal beverageadjustment = new BigDecimal("0");
        BigDecimal swimmingringadjustment = new BigDecimal("0");
        BigDecimal Fishtoadjust = new BigDecimal("0");
        BigDecimal Bicycleadjustment = new BigDecimal("0");
        BigDecimal childrenstoys = new BigDecimal("0");
        BigDecimal Otheradjustments = new BigDecimal("0");
        BigDecimal Theswimsuitadjustment = new BigDecimal("0");
        BigDecimal LandmarkTicket = new BigDecimal("0");
        BigDecimal utilitybills = new BigDecimal("0");
        BigDecimal Othercollectionadjust = new BigDecimal("0");
        BigDecimal honeymoonadjust = new BigDecimal("0");
        BigDecimal foodstuff = new BigDecimal("0");
        BigDecimal Healthbeverage = new BigDecimal("0");
        BigDecimal swimring = new BigDecimal("0");
        BigDecimal Fishpedicure = new BigDecimal("0");
        BigDecimal sportsbicycle = new BigDecimal("0");
        BigDecimal sportschildrenstoys = new BigDecimal("0");
        BigDecimal sportsother = new BigDecimal("0");
        BigDecimal Theotherpackage = new BigDecimal("0");
        BigDecimal Systemswitchingbalance = new BigDecimal("0");
        BigDecimal switchingbalance = new BigDecimal("0");
        BigDecimal cheque = new BigDecimal("0");
        BigDecimal Foreigncard = new BigDecimal("0");
        BigDecimal Domesticcard = new BigDecimal("0");
        BigDecimal UnionPaycard = new BigDecimal("0");
        BigDecimal POSUnionPaycard = new BigDecimal("0");
        BigDecimal footwear = new BigDecimal("0");
        BigDecimal Frozencoconutsetmeal = new BigDecimal("0");
        BigDecimal Fishmeal = new BigDecimal("0");
        BigDecimal Swimminglapspackage = new BigDecimal("0");
        BigDecimal Icecreamset = new BigDecimal("0");
        BigDecimal Toypackages = new BigDecimal("0");
        BigDecimal JBCCard = new BigDecimal("0");
        BigDecimal DinersClubcard = new BigDecimal("0");
        BigDecimal Thetransportcard = new BigDecimal("0");
        BigDecimal MasterCard = new BigDecimal("0");
        BigDecimal visacard = new BigDecimal("0");
        BigDecimal POSUnionPaycard2 = new BigDecimal("0");
        BigDecimal roomcharge = new BigDecimal("0");
        BigDecimal Coffeeshopdrinksincluded = new BigDecimal("0");
        BigDecimal packagepricedrink = new BigDecimal("0");
        BigDecimal packagepricefood = new BigDecimal("0");
        BigDecimal otherone = new BigDecimal("0");
        BigDecimal otherrevenue = new BigDecimal("0");
        BigDecimal otherrevenuetax = new BigDecimal("0");
        BigDecimal tallage9 = new BigDecimal("0");
        BigDecimal tallage13 = new BigDecimal("0");
        BigDecimal tallage5 = new BigDecimal("0");
        BigDecimal tallage6 = new BigDecimal("0");
        BigDecimal tallagevat9 = new BigDecimal("0");
        BigDecimal tallage13vat = new BigDecimal("0");
        BigDecimal taxadjustment5 = new BigDecimal("0");
        BigDecimal taxadjustment6 = new BigDecimal("0");
        BigDecimal taxadjustment9 = new BigDecimal("0");
        BigDecimal taxadjustment13 = new BigDecimal("0");
        BigDecimal Thecollectingpackage = new BigDecimal("0");
        BigDecimal Honeymoonarrangementvat = new BigDecimal("0");
        BigDecimal Thecollectingtaxes = new BigDecimal("0");

        for (int i = 0; i < list.size(); i++) {
            String transactioncode = list.get(i).getTransactioncode();
            if (StringUtils.isEmpty(transactioncode)) {
                throw new RuntimeException("参数为空..");
            }
            prices = prices.add(new BigDecimal(list.get(i).getPrice()));

            if (transactioncode.equals("1000")) {
                roomrate = new BigDecimal(list.get(i).getPrice());//房费
            }
            if (transactioncode.equals("1010")) {
                anroomrate = new BigDecimal(list.get(i).getPrice());//追加房费
            }
            if (transactioncode.equals("1020")) {
                rateupselling = new BigDecimal(list.get(i).getPrice());//房费- UPSELLING
            }
            if (transactioncode.equals("1030")) {
                allroomcharge = new BigDecimal(list.get(i).getPrice());// 房费- 全日房费
            }
            if (transactioncode.equals("1040")) {
                halfdayroomcharge = new BigDecimal(list.get(i).getPrice());//房费- 半日房费
            }
            if (transactioncode.equals("1050")) {
                bed = new BigDecimal(list.get(i).getPrice());//房费- 加床
            }
            if (transactioncode.equals("1060")) {
                cancellation = new BigDecimal(list.get(i).getPrice());//预订未到/取消
            }
            if (transactioncode.equals("1099")) {
                adjustment = new BigDecimal(list.get(i).getPrice());//房费调整
            }
            if (transactioncode.equals("1200")) {
                Roomservicecharge = new BigDecimal(list.get(i).getPrice());//房费服务费
            }
            if (transactioncode.equals("1299")) {
                Roomservicechargeadjustment = new BigDecimal(list.get(i).getPrice());//房费服务费调整
            }
            if (transactioncode.equals("1500")) {
                Roommiscellaneousincome = new BigDecimal(list.get(i).getPrice());//客房杂项收入
            }
            if (transactioncode.equals("1509")) {
                incomeadjustment = new BigDecimal(list.get(i).getPrice());//客房杂项收入调整
            }
            if (transactioncode.equals("1508")) {
                Roomincome = new BigDecimal(list.get(i).getPrice());//客房杂项收入 6%VAT
            }
            if (transactioncode.equals("1800")) {
                Theroomcharge = new BigDecimal(list.get(i).getPrice());//房费6%VAT
            }
            if (transactioncode.equals("2111")) {
                Breakfastfood = new BigDecimal(list.get(i).getPrice());// 宴会厅早餐食品
            }
            if (transactioncode.equals("2112")) {
                breakfastdrinks = new BigDecimal(list.get(i).getPrice());// 宴会厅早餐饮品
            }
            if (transactioncode.equals("2114")) {
                Greenfeesforbreakfast = new BigDecimal(list.get(i).getPrice());// 宴会厅早餐场租
            }
            if (transactioncode.equals("2113")) {
                Otherbreakfast = new BigDecimal(list.get(i).getPrice());//宴会厅早餐其他
            }
            if (transactioncode.equals("2121")) {
                Foodforlunch = new BigDecimal(list.get(i).getPrice());//宴会厅午餐食品
            }
            if (transactioncode.equals("2122")) {
                Drinkforlunch = new BigDecimal(list.get(i).getPrice());//宴会厅午餐饮品
            }
            if (transactioncode.equals("2124")) {
                forbreakfast = new BigDecimal(list.get(i).getPrice());//宴会厅早餐场租
            }
            if (transactioncode.equals("2123")) {
                Banquethalllunchother = new BigDecimal(list.get(i).getPrice());//宴会厅午餐其他
            }
            if (transactioncode.equals("2131")) {
                fordinner = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐食品
            }
            if (transactioncode.equals("2132")) {
                Halldinnerdrinks = new BigDecimal(list.get(i).getPrice());// 宴会厅晚餐饮品
            }
            if (transactioncode.equals("2134")) {
                Rentals = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐场租
            }
            if (transactioncode.equals("2133")) {
                Otherdinner = new BigDecimal(list.get(i).getPrice());// 宴会厅晚餐其他
            }
            if (transactioncode.equals("2115")) {
                servicecharge = new BigDecimal(list.get(i).getPrice());// 宴会厅早餐服务费
            }
            if (transactioncode.equals("2125")) {
                Mealservicecharge = new BigDecimal(list.get(i).getPrice());//宴会厅午餐服务费
            }
            if (transactioncode.equals("2135")) {
                Dinnerservicecharge = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐服务费
            }
            if (transactioncode.equals("2119")) {
                Adjustthebreakfast = new BigDecimal(list.get(i).getPrice());//宴会厅早餐调整
            }
            if (transactioncode.equals("2129")) {
                Adjustthelunch = new BigDecimal(list.get(i).getPrice());//宴会厅午餐调整
            }
            if (transactioncode.equals("2139")) {
                Dinnertoadjust = new BigDecimal(list.get(i).getPrice());// 宴会厅晚餐调整
            }
            if (transactioncode.equals("2144")) {
                Hallgreenfees = new BigDecimal(list.get(i).getPrice());//宴会厅场租
            }
            if (transactioncode.equals("2149")) {
                rentadjustment = new BigDecimal(list.get(i).getPrice());// 宴会厅场租调整
            }
            if (transactioncode.equals("2211")) {
                Cafebreakfast = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐食品
            }
            if (transactioncode.equals("2212")) {
                Breakfastdrink = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐饮品
            }
            if (transactioncode.equals("2213")) {
                Cafebreakfastother = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐其他
            }
            if (transactioncode.equals("2221")) {
                Cafelunch = new BigDecimal(list.get(i).getPrice());// 咖啡厅午餐食品
            }
            if (transactioncode.equals("2222")) {
                Cafelunchdrinks = new BigDecimal(list.get(i).getPrice());// 咖啡厅午餐饮品
            }
            if (transactioncode.equals("2223")) {
                Cafelunchother = new BigDecimal(list.get(i).getPrice());// 咖啡厅午餐其他
            }
            if (transactioncode.equals("2231")) {
                Cafedinnerfood = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐食品
            }
            if (transactioncode.equals("2232")) {
                Cafedinnerdrinks = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐饮品
            }
            if (transactioncode.equals("2233")) {
                Cafedinnerother = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐其他
            }
            if (transactioncode.equals("2200")) {
                Coffeeshop = new BigDecimal(list.get(i).getPrice());// 咖啡厅早餐包价
            }
            if (transactioncode.equals("2215")) {
                Cafebreakfastservicecharge = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐服务费
            }
            if (transactioncode.equals("2225")) {
                Cafelunchservice = new BigDecimal(list.get(i).getPrice());//咖啡厅午餐服务费
            }
            if (transactioncode.equals("2235")) {
                Cafedinnerservice = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐服务费
            }
            if (transactioncode.equals("2219")) {
                Cafebreakfastadjustment = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐调整
            }
            if (transactioncode.equals("2229")) {
                Cafelunchadjustment = new BigDecimal(list.get(i).getPrice());//咖啡厅午餐调整
            }
            if (transactioncode.equals("2239")) {
                Cafedinneradjustment = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐调整
            }
            if (transactioncode.equals("2311")) {
                Roomservicebreakfastfood = new BigDecimal(list.get(i).getPrice());//客房送餐早餐食品
            }
            if (transactioncode.equals("2312")) {
                Roomservicebreakfastdrinks = new BigDecimal(list.get(i).getPrice());//客房送餐早餐饮品
            }
            if (transactioncode.equals("2313")) {
                Roomservicebreakfastother = new BigDecimal(list.get(i).getPrice());//客房送餐早餐其他
            }
            if (transactioncode.equals("2321")) {
                Roomservicelunch = new BigDecimal(list.get(i).getPrice());//客房送餐午餐食品
            }
            if (transactioncode.equals("2322")) {
                Roomservicelunchdrinks = new BigDecimal(list.get(i).getPrice());//客房送餐午餐饮品
            }
            if (transactioncode.equals("2323")) {
                Roomservicelunchother = new BigDecimal(list.get(i).getPrice());//客房送餐午餐其他
            }
            if (transactioncode.equals("2331")) {
                Roomservicedinnerfood = new BigDecimal(list.get(i).getPrice());//客房送餐晚餐食品
            }
            if (transactioncode.equals("2332")) {
                Roomservicedinnerdrinks = new BigDecimal(list.get(i).getPrice());//客房送餐晚餐饮品
            }
            if (transactioncode.equals("2333")) {
                Roomservicedinnerother = new BigDecimal(list.get(i).getPrice());//客房送餐晚餐其他
            }
            if (transactioncode.equals("2315")) {
                Roomservicechargeforbreakfast = new BigDecimal(list.get(i).getPrice());//客房送餐早餐服务费
            }
            if (transactioncode.equals("2325")) {
                Roomservicelunchservicecharge = new BigDecimal(list.get(i).getPrice());//客房送餐午餐服务费
            }
            if (transactioncode.equals("2335")) {
                Roomservicefordinner = new BigDecimal(list.get(i).getPrice());//客房送餐晚餐服务费
            }
            if (transactioncode.equals("2319")) {
                Roomservicebreakfastadjustment = new BigDecimal(list.get(i).getPrice());//客房送餐早餐调整
            }
            if (transactioncode.equals("2329")) {
                Roomservicelunchadjustment = new BigDecimal(list.get(i).getPrice());//客房送餐午餐调整
            }
            if (transactioncode.equals("2339")) {
                Roomservicedinneradjustment = new BigDecimal(list.get(i).getPrice());//客房送餐晚餐调整
            }
            if (transactioncode.equals("4001")) {
                localcalls = new BigDecimal(list.get(i).getPrice());//市话
            }
            if (transactioncode.equals("4002")) {
                domestictollcall = new BigDecimal(list.get(i).getPrice());//国内长途
            }
            if (transactioncode.equals("4003")) {
                IDD = new BigDecimal(list.get(i).getPrice());//国际长途
            }
            if (transactioncode.equals("4009")) {
                Telephonerateadjustment = new BigDecimal(list.get(i).getPrice());//电话费调整
            }
            if (transactioncode.equals("4008")) {
                Telephonecharges9 = new BigDecimal(list.get(i).getPrice());//电话费 9%VAT
            }
            if (transactioncode.equals("4600")) {
                Businesscenterprint = new BigDecimal(list.get(i).getPrice());//商务中心-打印
            }
            if (transactioncode.equals("4609")) {
                Adjustmentofbusinesscenter = new BigDecimal(list.get(i).getPrice());//商务中心调整
            }
            if (transactioncode.equals("4608")) {
                CBD = new BigDecimal(list.get(i).getPrice());//商务中心 6%VAT
            }
            if (transactioncode.equals("5100")) {
                miniba = new BigDecimal(list.get(i).getPrice());//迷你吧
            }
            if (transactioncode.equals("5108")) {
                minibavat = new BigDecimal(list.get(i).getPrice());//迷你吧 13%VAT
            }
            if (transactioncode.equals("5109")) {
                Minibaradjustment = new BigDecimal(list.get(i).getPrice());//迷你吧调整
            }
            if (transactioncode.equals("5201")) {
                Laundryoutsidethestore = new BigDecimal(list.get(i).getPrice());//洗衣费-店外
            }
            if (transactioncode.equals("5202")) {
                Laundrychargeinstore = new BigDecimal(list.get(i).getPrice());//洗衣费-店内
            }
            if (transactioncode.equals("5209")) {
                Laundrychargeadjustment = new BigDecimal(list.get(i).getPrice());//洗衣费调整
            }
            if (transactioncode.equals("5208")) {
                laundrycharge = new BigDecimal(list.get(i).getPrice());//洗衣费 6%VAT
            }
            if (transactioncode.equals("5300")) {
                compensatefor = new BigDecimal(list.get(i).getPrice());//赔偿
            }
            if (transactioncode.equals("5308")) {
                compensateforvat = new BigDecimal(list.get(i).getPrice());//赔偿 6%VAT
            }
            if (transactioncode.equals("5309")) {
                Thecompensationadjustment = new BigDecimal(list.get(i).getPrice());//赔偿调整
            }
            if (transactioncode.equals("5400")) {
                carrental = new BigDecimal(list.get(i).getPrice());//租车
            }
            if (transactioncode.equals("5409")) {
                adjustcarrental = new BigDecimal(list.get(i).getPrice());//租车调整
            }
            if (transactioncode.equals("5500")) {
                Pickupanddropoff = new BigDecimal(list.get(i).getPrice());//代收接送机
            }
            if (transactioncode.equals("5510")) {
                scenicspots = new BigDecimal(list.get(i).getPrice());//代收景点门票
            }
            if (transactioncode.equals("5520")) {
                Collectutilities = new BigDecimal(list.get(i).getPrice());//代收水电费
            }
            if (transactioncode.equals("5530")) {
                Othercollection = new BigDecimal(list.get(i).getPrice());//其它代收
            }
            if (transactioncode.equals("6091")) {
                Healthcentrefoodadjustment = new BigDecimal(list.get(i).getPrice());//康体中心-食品调整
            }
            if (transactioncode.equals("6110")) {
                miscellaneousincome = new BigDecimal(list.get(i).getPrice());//杂项收入
            }
            if (transactioncode.equals("6119")) {
                adjustmiscellaneousincome = new BigDecimal(list.get(i).getPrice());//杂项收入调整
            }
            if (transactioncode.equals("6118")) {
                miscellaneousincomevat = new BigDecimal(list.get(i).getPrice());//杂项收入 6%VAT
            }
            if (transactioncode.equals("6200")) {
                rental = new BigDecimal(list.get(i).getPrice());//租金
            }
            if (transactioncode.equals("6209")) {
                adjustrental = new BigDecimal(list.get(i).getPrice());//租金调整
            }
            if (transactioncode.equals("7111")) {
                breakfastfoodvat = new BigDecimal(list.get(i).getPrice());//宴会厅早餐食品 6%VAT
            }
            if (transactioncode.equals("7114")) {
                Greenfeesforbreakfastvat = new BigDecimal(list.get(i).getPrice());//宴会厅早餐场租 5%VAT
            }
            if (transactioncode.equals("7113")) {
                Banquethallbreakfastother = new BigDecimal(list.get(i).getPrice());//宴会厅早餐其他 6%VAT
            }
            if (transactioncode.equals("7121")) {
                Banquethalllunchfoodvat = new BigDecimal(list.get(i).getPrice());//宴会厅午餐食品 6%VAT
            }
            if (transactioncode.equals("7122")) {
                Banquethalllunchdrinks = new BigDecimal(list.get(i).getPrice());//宴会厅午餐饮品 6%VAT
            }
            if (transactioncode.equals("7124")) {
                Banquethalllunchrental = new BigDecimal(list.get(i).getPrice());//宴会厅午餐场租 5%VAT
            }
            if (transactioncode.equals("7123")) {
                Banquethalllunchothervat = new BigDecimal(list.get(i).getPrice());//宴会厅午餐其他 6%VAT
            }
            if (transactioncode.equals("7112")) {
                Banquethallbreakfastdrinks = new BigDecimal(list.get(i).getPrice());//宴会厅早餐饮品 6%VAT
            }
            if (transactioncode.equals("7131")) {
                Banquethalldinnerfood = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐食品 6%VAT
            }
            if (transactioncode.equals("7132")) {
                Banquethalldinnerdrinks = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐饮品 6%VAT
            }
            if (transactioncode.equals("7134")) {
                Banquethalldinnerrental = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐场租 5%VAT
            }
            if (transactioncode.equals("7133")) {
                Banquethalldinnerother = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐其他 6%VAT
            }
            if (transactioncode.equals("7115")) {
                breakfastinbanquethall = new BigDecimal(list.get(i).getPrice());// 宴会厅早餐服务费 6%VAT
            }
            if (transactioncode.equals("7125")) {
                servicechargevat = new BigDecimal(list.get(i).getPrice());//宴会厅午餐服务费 6%VAT
            }
            if (transactioncode.equals("7135")) {
                dinnerservicechargevat = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐服务费 6%VAT
            }
            if (transactioncode.equals("7119")) {
                Banquethallbreakfastadjustment = new BigDecimal(list.get(i).getPrice());// 宴会厅早餐调整 6%VAT
            }
            if (transactioncode.equals("7129")) {
                Banquethalllunchadjustment = new BigDecimal(list.get(i).getPrice());//宴会厅午餐调整 6%VAT
            }
            if (transactioncode.equals("7139")) {
                Banquethalldinneradjustment = new BigDecimal(list.get(i).getPrice());//宴会厅晚餐调整 6%VAT
            }
            if (transactioncode.equals("7144")) {
                Banquethallrental = new BigDecimal(list.get(i).getPrice());//宴会厅场租5%VAT
            }
            if (transactioncode.equals("7149")) {
                Banquethallrentadjustment = new BigDecimal(list.get(i).getPrice());//宴会厅场租调整5%VAT
            }
            if (transactioncode.equals("7211")) {
                Cafebreakfastvat = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐食品 6%VAT
            }
            if (transactioncode.equals("7212")) {
                Coffeeshopbreakfastdrinks = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐饮品6%VAT
            }
            if (transactioncode.equals("7213")) {
                Cafebreakfastothervat = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐其他 6%VAT
            }
            if (transactioncode.equals("7221")) {
                Cafelunchvat = new BigDecimal(list.get(i).getPrice());//咖啡厅午餐食品 6%VAT
            }
            if (transactioncode.equals("7222")) {
                Cafelunchdrinksvat = new BigDecimal(list.get(i).getPrice());//咖啡厅午餐饮品 6%VAT
            }
            if (transactioncode.equals("7223")) {
                Cafelunchothervat = new BigDecimal(list.get(i).getPrice());// 咖啡厅午餐其他 6%VAT
            }
            if (transactioncode.equals("7231")) {
                Cafedinnerfoodvat = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐食品 6%VAT
            }
            if (transactioncode.equals("7232")) {
                Cafedinnerdrinksvat = new BigDecimal(list.get(i).getPrice());// 咖啡厅晚餐饮品 6%VAT
            }
            if (transactioncode.equals("7233")) {
                Cafedinnerothervat = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐其他 6%VAT
            }
            if (transactioncode.equals("7215")) {
                servicechargevat6 = new BigDecimal(list.get(i).getPrice());//咖啡厅早餐服务费 6%VAT
            }
            if (transactioncode.equals("7225")) {
                Cafelunchservicevat = new BigDecimal(list.get(i).getPrice());// 咖啡厅午餐服务费 6%VAT
            }
            if (transactioncode.equals("7235")) {
                Cafedinnerservicevat = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐服务费 6%VAT
            }
            if (transactioncode.equals("7219")) {
                Cafebreakfastadjustmentvat = new BigDecimal(list.get(i).getPrice());// 咖啡厅早餐调整 6%VAT
            }
            if (transactioncode.equals("7229")) {
                Cafelunchadjustmentvat = new BigDecimal(list.get(i).getPrice());//咖啡厅午餐调整 6%VAT
            }
            if (transactioncode.equals("7239")) {
                Cafedinneradjustmentvat = new BigDecimal(list.get(i).getPrice());//咖啡厅晚餐调整 6%VAT
            }
            if (transactioncode.equals("7311")) {
                Roomservicebreakfastfoodvat = new BigDecimal(list.get(i).getPrice());//客房送餐早餐食品 6%VAT
            }
            if (transactioncode.equals("7312")) {
                Roomservicebreakfastdrinksvat = new BigDecimal(list.get(i).getPrice());//客房送餐早餐饮品 6%VAT
            }
            if (transactioncode.equals("7313")) {
                Roomservicebreakfastothervat = new BigDecimal(list.get(i).getPrice());//客房送餐早餐其他 6%VAT
            }
            if (transactioncode.equals("7321")) {
                Roomservicelunchvat = new BigDecimal(list.get(i).getPrice());// 客房送餐午餐食品 6%VAT
            }
            if (transactioncode.equals("7322")) {
                Roomservicelunchdrinksvat = new BigDecimal(list.get(i).getPrice());//客房送餐午餐饮品 6%VAT
            }
            if (transactioncode.equals("7323")) {
                Roomservicelunchothervat = new BigDecimal(list.get(i).getPrice());//客房送餐午餐其他 6%VAT
            }
            if (transactioncode.equals("7331")) {
                Roomservicedinnerfoodvat = new BigDecimal(list.get(i).getPrice());//客房送餐晚餐食品 6%VAT
            }
            if (transactioncode.equals("7332")) {
                Roomservicedinnerdrinksvat = new BigDecimal(list.get(i).getPrice());//客房送餐晚餐饮品 6%VAT
            }
            if (transactioncode.equals("7333")) {
                roomservice = new BigDecimal(list.get(i).getPrice());// 客房送餐
            }
            if (transactioncode.equals("7315")) {
                Roomservicechargeforbreakfastvat = new BigDecimal(list.get(i).getPrice());// 客房送餐早餐服务费 6%VAT
            }
            if (transactioncode.equals("7325")) {
                Roomservicelunchvatvat = new BigDecimal(list.get(i).getPrice());//客房送餐午餐服务费 6%VAT
            }
            if (transactioncode.equals("7335")) {
                Roomservicefordinnervat = new BigDecimal(list.get(i).getPrice());// 客房送餐晚餐服务费 6%VAT
            }
            if (transactioncode.equals("7319")) {
                Roomservicebreakfastadjustmentvat = new BigDecimal(list.get(i).getPrice());//客房送餐早餐调整 6%VAT
            }
            if (transactioncode.equals("7329")) {
                Roomservicelunchadjustmentvat = new BigDecimal(list.get(i).getPrice());//客房送餐午餐调整 6%VAT
            }
            if (transactioncode.equals("7339")) {
                Roomservicedinneradjustmentvat = new BigDecimal(list.get(i).getPrice());// 客房送餐晚餐调整 6%VAT
            }
            if (transactioncode.equals("6208")) {
                charterhire = new BigDecimal(list.get(i).getPrice());//租金 5%VAT
            }
            if (transactioncode.equals("8000")) {
                Acashrefund = new BigDecimal(list.get(i).getPrice());//现金退款
            }
            if (transactioncode.equals("9000")) {
                cash = new BigDecimal(list.get(i).getPrice());// 现金
            }
            if (transactioncode.equals("9130")) {
                WeChatPay = new BigDecimal(list.get(i).getPrice());//微信支付
            }
            if (transactioncode.equals("9140")) {
                Alipay = new BigDecimal(list.get(i).getPrice());//支付宝
            }
            if (transactioncode.equals("9150")) {
                banktransfer = new BigDecimal(list.get(i).getPrice());//银行汇款
            }
            if (transactioncode.equals("9160")) {
                membershipcard = new BigDecimal(list.get(i).getPrice());//会员卡
            }
            if (transactioncode.equals("9200")) {
                POScash = new BigDecimal(list.get(i).getPrice());//POS-现金
            }
            if (transactioncode.equals("9300")) {
                POSForeigncard = new BigDecimal(list.get(i).getPrice());//POS-国外卡
            }
            if (transactioncode.equals("9320")) {
                POSDomesticcard = new BigDecimal(list.get(i).getPrice());//POS-国内卡
            }
            if (transactioncode.equals("9340")) {
                poswx = new BigDecimal(list.get(i).getPrice());//POS-微信
            }
            if (transactioncode.equals("9350")) {
                poszfb = new BigDecimal(list.get(i).getPrice());//POS-支付宝
            }
            if (transactioncode.equals("9330")) {
                POSPaymentofthecity = new BigDecimal(list.get(i).getPrice());//POS-城市挂账
            }
            if (transactioncode.equals("9030")) {
                Paymentofthecity = new BigDecimal(list.get(i).getPrice());//城市挂账
            }
            if (transactioncode.equals("9360")) {
                posvip = new BigDecimal(list.get(i).getPrice());//POS-会员
            }
            if (transactioncode.equals("9500")) {
                Cashreceivable = new BigDecimal(list.get(i).getPrice());//AR-应收现金
            }
            if (transactioncode.equals("9520")) {
                Bankreceivables = new BigDecimal(list.get(i).getPrice());//AR-应收银行回款
            }
            if (transactioncode.equals("9540")) {
                commission = new BigDecimal(list.get(i).getPrice());//AR-佣金
            }
            if (transactioncode.equals("9550")) {
                Creditcardcollection = new BigDecimal(list.get(i).getPrice());// AR-信用卡回款(信用卡）
            }
            if (transactioncode.equals("9551")) {
                Creditcarddebit = new BigDecimal(list.get(i).getPrice());//AR-信用卡回款（挂账）
            }
            if (transactioncode.equals("9560")) {
                Creditcrdcommission = new BigDecimal(list.get(i).getPrice());//AR-信用卡佣金(信用卡）
            }
            if (transactioncode.equals("9561")) {
                Creditcardcommissioncharge = new BigDecimal(list.get(i).getPrice());//AR-信用卡佣金(挂账）
            }
            if (transactioncode.equals("9570")) {
                ARAccountsreceivablerefund = new BigDecimal(list.get(i).getPrice());// AR-应收退款
            }
            if (transactioncode.equals("9571")) {
                compress = new BigDecimal(list.get(i).getPrice());//AR-压缩
            }
            if (transactioncode.equals("9590")) {
                Generalledgerhedge = new BigDecimal(list.get(i).getPrice());// AR-应收总帐对冲
            }
            if (transactioncode.equals("9999")) {
                Systemadjustment = new BigDecimal(list.get(i).getPrice());//系统调整
            }
            if (transactioncode.equals("5509")) {
                Adjustmentofpickupandpickup = new BigDecimal(list.get(i).getPrice());//代收接送机调整
            }
            if (transactioncode.equals("2201")) {
                Cafelunchpackage = new BigDecimal(list.get(i).getPrice());//咖啡厅午餐包价
            }
            if (transactioncode.equals("2202")) {
                Cafedinnerincluded = new BigDecimal(list.get(i).getPrice());// 咖啡厅晚餐包价
            }
            if (transactioncode.equals("1210")) {
                Guestroompackage = new BigDecimal(list.get(i).getPrice());//客房包价
            }
            if (transactioncode.equals("5540")) {
                Honeymoonarrangement = new BigDecimal(list.get(i).getPrice());// 蜜月布置
            }
            if (transactioncode.equals("1070")) {
                Toupgradetheroomcharge = new BigDecimal(list.get(i).getPrice());//升级房费
            }
            if (transactioncode.equals("8790")) {
                Balancetransfer = new BigDecimal(list.get(i).getPrice());// 余额转移
            }
            if (transactioncode.equals("6081")) {
                Recreationcentrefood = new BigDecimal(list.get(i).getPrice());//康体中心-食品 6%
            }
            if (transactioncode.equals("6082")) {
                Recreationcentrebeverages = new BigDecimal(list.get(i).getPrice());//康体中心-饮品6%
            }
            if (transactioncode.equals("6083")) {
                Recreationcentreswimmingring = new BigDecimal(list.get(i).getPrice());//康体中心-泳圈6%
            }
            if (transactioncode.equals("6084")) {
                Recreationcentrefishtherapy = new BigDecimal(list.get(i).getPrice());//康体中心-鱼疗 6%
            }
            if (transactioncode.equals("6085")) {
                Recreationcentrebicycles = new BigDecimal(list.get(i).getPrice());//康体中心-自行车 6%
            }
            if (transactioncode.equals("6086")) {
                Sandtoysforchildren = new BigDecimal(list.get(i).getPrice());//康体中心-儿童沙雕玩具
            }
            if (transactioncode.equals("6087")) {
                Recreationcentertax = new BigDecimal(list.get(i).getPrice());//康体中心税金 6%
            }
            if (transactioncode.equals("6009")) {
                Recreationswimwear = new BigDecimal(list.get(i).getPrice());//康乐-泳衣
            }
            if (transactioncode.equals("6089")) {
                Recreationswimwear6 = new BigDecimal(list.get(i).getPrice());//康乐-泳衣 6%
            }
            if (transactioncode.equals("6092")) {
                beverageadjustment = new BigDecimal(list.get(i).getPrice());//康体中心-饮品调整
            }
            if (transactioncode.equals("6093")) {
                swimmingringadjustment = new BigDecimal(list.get(i).getPrice());//康体中心-泳圈调整
            }
            if (transactioncode.equals("6094")) {
                Fishtoadjust = new BigDecimal(list.get(i).getPrice());// 康体中心-鱼疗调整
            }
            if (transactioncode.equals("6095")) {
                Bicycleadjustment = new BigDecimal(list.get(i).getPrice());//康体中心-自行车调整
            }
            if (transactioncode.equals("6096")) {
                childrenstoys = new BigDecimal(list.get(i).getPrice());//康体中心-儿童玩具调整
            }
            if (transactioncode.equals("6097")) {
                Otheradjustments = new BigDecimal(list.get(i).getPrice());//康体中心-其它调整
            }
            if (transactioncode.equals("6099")) {
                Theswimsuitadjustment = new BigDecimal(list.get(i).getPrice());// 康乐-泳衣调整
            }
            if (transactioncode.equals("5519")) {
                LandmarkTicket = new BigDecimal(list.get(i).getPrice());//代收景点门票调整
            }
            if (transactioncode.equals("5529")) {
                utilitybills = new BigDecimal(list.get(i).getPrice());//代收水电费调整
            }
            if (transactioncode.equals("5539")) {
                Othercollectionadjust = new BigDecimal(list.get(i).getPrice());//其它代收调整
            }
            if (transactioncode.equals("5549")) {
                honeymoonadjust = new BigDecimal(list.get(i).getPrice());//蜜月布置调整
            }
            if (transactioncode.equals("6001")) {
                foodstuff = new BigDecimal(list.get(i).getPrice());//康体中心-食品
            }
            if (transactioncode.equals("6002")) {
                Healthbeverage = new BigDecimal(list.get(i).getPrice());//康体中心-饮品
            }
            if (transactioncode.equals("6003")) {
                swimring = new BigDecimal(list.get(i).getPrice());// 康体中心-泳圈
            }
            if (transactioncode.equals("6004")) {
                Fishpedicure = new BigDecimal(list.get(i).getPrice());// 康体中心-鱼疗
            }
            if (transactioncode.equals("6005")) {
                sportsbicycle = new BigDecimal(list.get(i).getPrice());//康体中心-自行车
            }
            if (transactioncode.equals("6006")) {
                sportschildrenstoys = new BigDecimal(list.get(i).getPrice());//康体中心-儿童玩具
            }
            if (transactioncode.equals("6007")) {
                sportsother = new BigDecimal(list.get(i).getPrice());//康体中心-其它
            }
            if (transactioncode.equals("6000")) {
                Theotherpackage = new BigDecimal(list.get(i).getPrice());//康乐其它包价
            }
            if (transactioncode.equals("8999")) {
                Systemswitchingbalance = new BigDecimal(list.get(i).getPrice());//系统切换余额
            }
            if (transactioncode.equals("8980")) {
                switchingbalance = new BigDecimal(list.get(i).getPrice());//系统切换余额(前台暂挂账)
            }
            if (transactioncode.equals("9010")) {
                cheque = new BigDecimal(list.get(i).getPrice());//支票
            }
            if (transactioncode.equals("9100")) {
                Foreigncard = new BigDecimal(list.get(i).getPrice());//国外卡
            }
            if (transactioncode.equals("9110")) {
                Domesticcard = new BigDecimal(list.get(i).getPrice());//国内卡
            }
            if (transactioncode.equals("9120")) {
                UnionPaycard = new BigDecimal(list.get(i).getPrice());//银联卡
            }
            if (transactioncode.equals("9310")) {
                POSUnionPaycard = new BigDecimal(list.get(i).getPrice());// POS-银联卡
            }
            if (transactioncode.equals("6010")) {
                footwear = new BigDecimal(list.get(i).getPrice());//康乐-球类
            }
            if (transactioncode.equals("6011")) {
                Frozencoconutsetmeal = new BigDecimal(list.get(i).getPrice());//康乐-冰冻椰青套餐
            }
            if (transactioncode.equals("6012")) {
                Fishmeal = new BigDecimal(list.get(i).getPrice());// 康乐-鱼疗套餐
            }
            if (transactioncode.equals("6013")) {
                Swimminglapspackage = new BigDecimal(list.get(i).getPrice());//康乐-泳圈套餐
            }
            if (transactioncode.equals("6014")) {
                Icecreamset = new BigDecimal(list.get(i).getPrice());// 康乐-冰淇淋套餐
            }
            if (transactioncode.equals("6015")) {
                Toypackages = new BigDecimal(list.get(i).getPrice());//康乐-玩具套餐
            }
            if (transactioncode.equals("9361")) {
                JBCCard = new BigDecimal(list.get(i).getPrice());// POS-JCB卡
            }
            if (transactioncode.equals("9362")) {
                DinersClubcard = new BigDecimal(list.get(i).getPrice());// POS-大莱卡
            }
            if (transactioncode.equals("9363")) {
                Thetransportcard = new BigDecimal(list.get(i).getPrice());//POS-美运卡
            }
            if (transactioncode.equals("9364")) {
                MasterCard = new BigDecimal(list.get(i).getPrice());//POS-万事达
            }
            if (transactioncode.equals("9365")) {
                visacard = new BigDecimal(list.get(i).getPrice());// POS-维萨卡
            }
            if (transactioncode.equals("9366")) {
                POSUnionPaycard2 = new BigDecimal(list.get(i).getPrice());//POS-银联卡
            }
            if (transactioncode.equals("9367")) {
                roomcharge = new BigDecimal(list.get(i).getPrice());//房帐
            }
            if (transactioncode.equals("2203")) {
                Coffeeshopdrinksincluded = new BigDecimal(list.get(i).getPrice());//咖啡厅酒水包价
            }
            if (transactioncode.equals("6020")) {
                packagepricedrink = new BigDecimal(list.get(i).getPrice());//康乐饮品包价
            }
            if (transactioncode.equals("6030")) {
                packagepricefood = new BigDecimal(list.get(i).getPrice());//康乐食品包价
            }
            if (transactioncode.equals("2204")) {
                otherone = new BigDecimal(list.get(i).getPrice());//咖啡厅其它包价
            }
            if (transactioncode.equals("6120")) {
                otherrevenue = new BigDecimal(list.get(i).getPrice());//其他收入5%税
            }
            if (transactioncode.equals("6130")) {
                otherrevenuetax = new BigDecimal(list.get(i).getPrice());//其他收入
            }
            if (transactioncode.equals("6140")) {
                tallage9 = new BigDecimal(list.get(i).getPrice());//其他收入9%税
            }
            if (transactioncode.equals("6150")) {
                tallage13 = new BigDecimal(list.get(i).getPrice());// 其他收入13%税
            }
            if (transactioncode.equals("6128")) {
                tallage5 = new BigDecimal(list.get(i).getPrice());//其他收入 5%VAT
            }
            if (transactioncode.equals("6138")) {
                tallage6 = new BigDecimal(list.get(i).getPrice());//其他收入 6%VAT
            }
            if (transactioncode.equals("6148")) {
                tallagevat9 = new BigDecimal(list.get(i).getPrice());//其他收入 9%VAT
            }
            if (transactioncode.equals("6158")) {
                tallage13vat = new BigDecimal(list.get(i).getPrice());// 其他收入 13%VAT
            }
            if (transactioncode.equals("6129")) {
                taxadjustment5 = new BigDecimal(list.get(i).getPrice());// 其它收入5%税调整
            }
            if (transactioncode.equals("6139")) {
                taxadjustment6 = new BigDecimal(list.get(i).getPrice());//其他收入6%税调整
            }
            if (transactioncode.equals("6149")) {
                taxadjustment9 = new BigDecimal(list.get(i).getPrice());//其他收入9%税调整
            }
            if (transactioncode.equals("6159")) {
                taxadjustment13 = new BigDecimal(list.get(i).getPrice());// 其他收入13%税调整
            }
            if (transactioncode.equals("5410")) {
                Thecollectingpackage = new BigDecimal(list.get(i).getPrice());// 代收包价
            }
            if (transactioncode.equals("5548")) {
                Honeymoonarrangementvat = new BigDecimal(list.get(i).getPrice());//蜜月布置6%VAT
            }
            if (transactioncode.equals("5507")) {
                Thecollectingtaxes = new BigDecimal(list.get(i).getPrice());// 代收税金6%VAT
            }

        }

        consumption.setRoomrate(roomrate);
        consumption.setAnroomrate(anroomrate);
        consumption.setRateupselling(rateupselling);
        consumption.setAllroomcharge(allroomcharge);
        consumption.setHalfdayroomcharge(halfdayroomcharge);
        consumption.setBed(bed);
        consumption.setCancellation(cancellation);
        consumption.setAdjustment(adjustment);
        consumption.setRoomservicecharge(Roomservicecharge);
        consumption.setRoomservicechargeadjustment(Roomservicechargeadjustment);
        consumption.setRoommiscellaneousincome(Roommiscellaneousincome);
        consumption.setIncomeadjustment(incomeadjustment);
        consumption.setRoomincome(Roomincome);
        consumption.setTheroomcharge(Theroomcharge);
        consumption.setBreakfastfood(Breakfastfood);
        consumption.setBreakfastdrinks(breakfastdrinks);
        consumption.setGreenfeesforbreakfast(Greenfeesforbreakfast);
        consumption.setOtherbreakfast(Otherbreakfast);
        consumption.setFoodforlunch(Foodforlunch);
        consumption.setDrinkforlunch(Drinkforlunch);
        consumption.setForbreakfast(forbreakfast);
        consumption.setBanquethalllunchother(Banquethalllunchother);
        consumption.setFordinner(fordinner);
        consumption.setHalldinnerdrinks(Halldinnerdrinks);
        consumption.setRentals(Rentals);
        consumption.setOtherdinner(Otherdinner);
        consumption.setServicecharge(servicecharge);
        consumption.setMealservicecharge(Mealservicecharge);
        consumption.setDinnerservicecharge(Dinnerservicecharge);
        consumption.setAdjustthebreakfast(Adjustthebreakfast);
        consumption.setAdjustthelunch(Adjustthelunch);
        consumption.setBanquethallbreakfastdrinks(Banquethallbreakfastdrinks);
        consumption.setBanquethalldinnerfood(Banquethalldinnerfood);
        consumption.setBanquethalldinnerdrinks(Banquethalldinnerdrinks);
        consumption.setDinnertoadjust(Dinnertoadjust);
        consumption.setHallgreenfees(Hallgreenfees);
        consumption.setRentadjustment(rentadjustment);
        consumption.setCafebreakfast(Cafebreakfast);
        consumption.setBreakfastdrink(Breakfastdrink);
        consumption.setCafebreakfastother(Cafebreakfastother);
        consumption.setCafelunch(Cafelunch);
        consumption.setCafelunchdrinks(Cafelunchdrinks);
        consumption.setCafelunchother(Cafelunchother);
        consumption.setCafedinnerfood(Cafedinnerfood);
        consumption.setCafedinnerdrinks(Cafedinnerdrinks);
        consumption.setCafedinnerother(Cafedinnerother);
        consumption.setCoffeeshop(Coffeeshop);
        consumption.setCafebreakfastservicecharge(Cafebreakfastservicecharge);
        consumption.setCafelunchservice(Cafelunchservice);
        consumption.setCafedinnerservice(Cafedinnerservice);
        consumption.setCafebreakfastadjustment(Cafebreakfastadjustment);
        consumption.setCafelunchadjustment(Cafelunchadjustment);
        consumption.setCafedinneradjustment(Cafedinneradjustment);
        consumption.setRoomservicebreakfastfood(Roomservicebreakfastfood);
        consumption.setRoomservicebreakfastdrinks(Roomservicebreakfastdrinks);
        consumption.setRoomservicebreakfastother(Roomservicebreakfastother);
        consumption.setRoomservicelunch(Roomservicelunch);
        consumption.setRoomservicelunchdrinks(Roomservicelunchdrinks);
        consumption.setRoomservicelunchother(Roomservicelunchother);
        consumption.setRoomservicedinnerfood(Roomservicedinnerfood);
        consumption.setRoomservicedinnerdrinks(Roomservicedinnerdrinks);
        consumption.setRoomservicedinnerother(Roomservicedinnerother);
        consumption.setRoomservicechargeforbreakfast(Roomservicechargeforbreakfast);
        consumption.setRoomservicelunchservicecharge(Roomservicelunchservicecharge);
        consumption.setRoomservicefordinner(Roomservicefordinner);
        consumption.setRoomservicebreakfastadjustment(Roomservicebreakfastadjustment);
        consumption.setRoomservicelunchadjustment(Roomservicelunchadjustment);
        consumption.setRoomservicedinneradjustment(Roomservicedinneradjustment);
        consumption.setLocalcalls(localcalls);
        consumption.setDomestictollcall(domestictollcall);
        consumption.setIDD(IDD);
        consumption.setTelephonerateadjustment(Telephonerateadjustment);
        consumption.setTelephonecharges9(Telephonecharges9);
        consumption.setBusinesscenterprint(Businesscenterprint);
        consumption.setAdjustmentofbusinesscenter(Adjustmentofbusinesscenter);
        consumption.setCBD(CBD);
        consumption.setMiniba(miniba);
        consumption.setMinibavat(minibavat);
        consumption.setMinibaradjustment(Minibaradjustment);
        consumption.setLaundryoutsidethestore(Laundryoutsidethestore);
        consumption.setLaundrychargeinstore(Laundrychargeinstore);
        consumption.setLaundrychargeadjustment(Laundrychargeadjustment);
        consumption.setLaundrycharge(laundrycharge);
        consumption.setCompensatefor(compensatefor);
        consumption.setCompensateforvat(compensateforvat);
        consumption.setThecompensationadjustment(Thecompensationadjustment);
        consumption.setCarrental(carrental);
        consumption.setAdjustcarrental(adjustcarrental);
        consumption.setPickupanddropoff(Pickupanddropoff);
        consumption.setScenicspots(scenicspots);
        consumption.setCollectutilities(Collectutilities);
        consumption.setOthercollection(Othercollection);
        consumption.setHealthcentrefoodadjustment(Healthcentrefoodadjustment);
        consumption.setMiscellaneousincome(miscellaneousincome);
        consumption.setAdjustmiscellaneousincome(adjustmiscellaneousincome);
        consumption.setMiscellaneousincomevat(miscellaneousincomevat);
        consumption.setRental(rental);
        consumption.setAdjustrental(adjustrental);
        consumption.setBreakfastfoodvat(breakfastfoodvat);
        consumption.setGreenfeesforbreakfastvat(Greenfeesforbreakfastvat);
        consumption.setBanquethallbreakfastother(Banquethallbreakfastother);
        consumption.setBanquethalllunchfoodvat(Banquethalllunchfoodvat);
        consumption.setBanquethalllunchdrinks(Banquethalllunchdrinks);
        consumption.setBanquethalllunchrental(Banquethalllunchrental);
        consumption.setBanquethalllunchothervat(Banquethalllunchothervat);
        consumption.setBanquethalldinnerrental(Banquethalldinnerrental);
        consumption.setBanquethalldinnerother(Banquethalldinnerother);
        consumption.setBreakfastinbanquethall(breakfastinbanquethall);
        consumption.setServicechargevat(servicechargevat);
        consumption.setDinnerservicechargevat(dinnerservicechargevat);
        consumption.setBanquethallbreakfastadjustment(Banquethallbreakfastadjustment);
        consumption.setBanquethalllunchadjustment(Banquethalllunchadjustment);
        consumption.setBanquethalldinneradjustment(Banquethalldinneradjustment);
        consumption.setBanquethallrental(Banquethallrental);
        consumption.setBanquethallrentadjustment(Banquethallrentadjustment);
        consumption.setCafebreakfastvat(Cafebreakfastvat);
        consumption.setCoffeeshopbreakfastdrinks(Coffeeshopbreakfastdrinks);
        consumption.setCafebreakfastothervat(Cafebreakfastothervat);
        consumption.setCafelunchvat(Cafelunchvat);
        consumption.setCafelunchdrinksvat(Cafelunchdrinksvat);
        consumption.setCafelunchothervat(Cafelunchothervat);
        consumption.setCafedinnerfoodvat(Cafedinnerfoodvat);
        consumption.setCafedinnerdrinksvat(Cafedinnerdrinksvat);
        consumption.setCafedinnerothervat(Cafedinnerothervat);
        consumption.setServicechargevat6(servicechargevat6);
        consumption.setCafelunchservicevat(Cafelunchservicevat);
        consumption.setCafedinnerservicevat(Cafedinnerservicevat);
        consumption.setCafebreakfastadjustmentvat(Cafebreakfastadjustmentvat);
        consumption.setCafelunchadjustmentvat(Cafelunchadjustmentvat);
        consumption.setCafedinneradjustmentvat(Cafedinneradjustmentvat);
        consumption.setRoomservicebreakfastfoodvat(Roomservicebreakfastfoodvat);
        consumption.setRoomservicebreakfastdrinksvat(Roomservicebreakfastdrinksvat);
        consumption.setRoomservicebreakfastothervat(Roomservicebreakfastothervat);
        consumption.setRoomservicelunchvat(Roomservicelunchvat);
        consumption.setRoomservicelunchdrinksvat(Roomservicelunchdrinksvat);
        consumption.setRoomservicelunchothervat(Roomservicelunchothervat);
        consumption.setRoomservicedinnerfoodvat(Roomservicedinnerfoodvat);
        consumption.setRoomservicedinnerdrinksvat(Roomservicedinnerdrinksvat);
        consumption.setRoomservice(roomservice);
        consumption.setRoomservicechargeforbreakfastvat(Roomservicechargeforbreakfastvat);
        consumption.setRoomservicelunchvatvat(Roomservicelunchvatvat);
        consumption.setRoomservicefordinnervat(Roomservicefordinnervat);
        consumption.setRoomservicebreakfastadjustmentvat(Roomservicebreakfastadjustmentvat);
        consumption.setRoomservicelunchadjustmentvat(Roomservicelunchadjustmentvat);
        consumption.setRoomservicedinneradjustmentvat(Roomservicedinneradjustmentvat);
        consumption.setCharterhire(charterhire);
        consumption.setAcashrefund(Acashrefund);
        consumption.setCash(cash);
        consumption.setPaymentofthecity(Paymentofthecity);
        consumption.setWeChatPay(WeChatPay);
        consumption.setAlipay(Alipay);
        consumption.setBanktransfer(banktransfer);
        consumption.setMembershipcard(membershipcard);
        consumption.setPOScash(POScash);
        consumption.setPOSForeigncard(POSForeigncard);
        consumption.setPOSDomesticcard(POSDomesticcard);
        consumption.setPOSPaymentofthecity(POSPaymentofthecity);
        consumption.setPoswx(poswx);
        consumption.setPoszfb(poszfb);
        consumption.setPosvip(posvip);
        consumption.setCashreceivable(Cashreceivable);
        consumption.setBankreceivables(Bankreceivables);
        consumption.setCommission(commission);
        consumption.setCreditcardcollection(Creditcardcollection);
        consumption.setCreditcarddebit(Creditcarddebit);
        consumption.setCreditcrdcommission(Creditcrdcommission);
        consumption.setCreditcardcommissioncharge(Creditcardcommissioncharge);
        consumption.setARAccountsreceivablerefund(ARAccountsreceivablerefund);
        consumption.setCompress(compress);
        consumption.setGeneralledgerhedge(Generalledgerhedge);
        consumption.setSystemadjustment(Systemadjustment);
        consumption.setAdjustmentofpickupandpickup(Adjustmentofpickupandpickup);
        consumption.setCafelunchpackage(Cafelunchpackage);
        consumption.setCafedinnerincluded(Cafedinnerincluded);
        consumption.setGuestroompackage(Guestroompackage);
        consumption.setHoneymoonarrangement(Honeymoonarrangement);
        consumption.setToupgradetheroomcharge(Toupgradetheroomcharge);
        consumption.setBalancetransfer(Balancetransfer);
        consumption.setRecreationcentrefood(Recreationcentrefood);
        consumption.setRecreationcentrebeverages(Recreationcentrebeverages);
        consumption.setRecreationcentreswimmingring(Recreationcentreswimmingring);
        consumption.setRecreationcentrefishtherapy(Recreationcentrefishtherapy);
        consumption.setRecreationcentrebicycles(Recreationcentrebicycles);
        consumption.setSandtoysforchildren(Sandtoysforchildren);
        consumption.setRecreationcentertax(Recreationcentertax);
        consumption.setRecreationswimwear(Recreationswimwear);
        consumption.setRecreationswimwear6(Recreationswimwear6);
        consumption.setBeverageadjustment(beverageadjustment);
        consumption.setSwimmingringadjustment(swimmingringadjustment);
        consumption.setFishtoadjust(Fishtoadjust);
        consumption.setBicycleadjustment(Bicycleadjustment);
        consumption.setChildrenstoys(childrenstoys);
        consumption.setOtheradjustments(Otheradjustments);
        consumption.setTheswimsuitadjustment(Theswimsuitadjustment);
        consumption.setLandmarkTicket(LandmarkTicket);
        consumption.setUtilitybills(utilitybills);
        consumption.setOthercollectionadjust(Othercollectionadjust);
        consumption.setHoneymoonadjust(honeymoonadjust);
        consumption.setFoodstuff(foodstuff);
        consumption.setHealthbeverage(Healthbeverage);
        consumption.setSwimring(swimring);
        consumption.setFishpedicure(Fishpedicure);
        consumption.setSportsbicycle(sportsbicycle);
        consumption.setSportschildrenstoys(sportschildrenstoys);
        consumption.setSportsother(sportsother);
        consumption.setTheotherpackage(Theotherpackage);
        consumption.setSystemswitchingbalance(Systemswitchingbalance);
        consumption.setSwitchingbalance(switchingbalance);
        consumption.setCheque(cheque);
        consumption.setForeigncard(Foreigncard);
        consumption.setDomesticcard(Domesticcard);
        consumption.setUnionPaycard(UnionPaycard);
        consumption.setPOSUnionPaycard(POSUnionPaycard);
        consumption.setFootwear(footwear);
        consumption.setFrozencoconutsetmeal(Frozencoconutsetmeal);
        consumption.setFishmeal(Fishmeal);
        consumption.setSwimminglapspackage(Swimminglapspackage);
        consumption.setIcecreamset(Icecreamset);
        consumption.setToypackages(Toypackages);
        consumption.setJBCCard(JBCCard);
        consumption.setDinersClubcard(DinersClubcard);
        consumption.setThetransportcard(Thetransportcard);
        consumption.setMasterCard(MasterCard);
        consumption.setVisacard(visacard);
        consumption.setPOSUnionPaycard2(POSUnionPaycard2);
        consumption.setRoomcharge(roomcharge);
        consumption.setCoffeeshopdrinksincluded(Coffeeshopdrinksincluded);
        consumption.setPackagepricedrink(packagepricedrink);
        consumption.setPackagepricefood(packagepricefood);
        consumption.setOtherone(otherone);
        consumption.setOtherrevenue(otherrevenue);
        consumption.setOtherrevenuetax(otherrevenuetax);
        consumption.setTallage9(tallage9);
        consumption.setTallage13(tallage13);
        consumption.setTallage5(tallage5);
        consumption.setTallage6(tallage6);
        consumption.setTallagevat9(tallagevat9);
        consumption.setTallage13vat(tallage13vat);
        consumption.setTaxadjustment5(taxadjustment5);
        consumption.setTaxadjustment6(taxadjustment6);
        consumption.setTaxadjustment9(taxadjustment9);
        consumption.setTaxadjustment13(taxadjustment13);
        consumption.setThecollectingpackage(Thecollectingpackage);
        consumption.setHoneymoonarrangementvat(Honeymoonarrangementvat);
        consumption.setThecollectingtaxes(Thecollectingtaxes);
        consumption.setPrices(prices);
        String sun = JSON.toJSONString(consumption);
        try {
            PrintWriter responseWriter = response.getWriter();
            responseWriter.append(sun.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
