package org.jeecg.modules.zzj.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 退房用户消费明细
 */
@Data
public class Consumption {
    private BigDecimal roomrate;
    private BigDecimal anroomrate;
    private BigDecimal rateupselling;
    private BigDecimal allroomcharge;
    private BigDecimal halfdayroomcharge;
    private BigDecimal bed;
    private BigDecimal cancellation;
    private BigDecimal adjustment;
    private BigDecimal Roomservicecharge;
    private BigDecimal Roomservicechargeadjustment;
    private BigDecimal Roommiscellaneousincome;
    private BigDecimal incomeadjustment;
    private BigDecimal Roomincome;
    private BigDecimal Theroomcharge;
    private BigDecimal Breakfastfood;
    private BigDecimal breakfastdrinks;
    private BigDecimal Greenfeesforbreakfast;
    private BigDecimal Otherbreakfast;
    private BigDecimal Foodforlunch;
    private BigDecimal Drinkforlunch;
    private BigDecimal forbreakfast;
    private BigDecimal Banquethalllunchother;
    private BigDecimal fordinner;
    private BigDecimal Halldinnerdrinks;
    private BigDecimal Rentals;
    private BigDecimal Otherdinner;
    private BigDecimal servicecharge;
    private BigDecimal Mealservicecharge;
    private BigDecimal Dinnerservicecharge;
    private BigDecimal Adjustthebreakfast;
    private BigDecimal Adjustthelunch;
    private BigDecimal Banquethallbreakfastdrinks;
    private BigDecimal Banquethalldinnerfood;
    private BigDecimal Banquethalldinnerdrinks;
    private BigDecimal Dinnertoadjust;
    private BigDecimal Hallgreenfees;
    private BigDecimal rentadjustment;
    private BigDecimal Cafebreakfast;
    private BigDecimal Breakfastdrink;
    private BigDecimal Cafebreakfastother;
    private BigDecimal Cafelunch;
    private BigDecimal Cafelunchdrinks;
    private BigDecimal Cafelunchother;
    private BigDecimal Cafedinnerfood;
    private BigDecimal Cafedinnerdrinks;
    private BigDecimal Cafedinnerother;
    private BigDecimal Coffeeshop;
    private BigDecimal Cafebreakfastservicecharge;
    private BigDecimal Cafelunchservice;
    private BigDecimal Cafedinnerservice;
    private BigDecimal Cafebreakfastadjustment;
    private BigDecimal Cafelunchadjustment;
    private BigDecimal Cafedinneradjustment;
    private BigDecimal Roomservicebreakfastfood;
    private BigDecimal Roomservicebreakfastdrinks;
    private BigDecimal Roomservicebreakfastother;
    private BigDecimal Roomservicelunch;
    private BigDecimal Roomservicelunchdrinks;
    private BigDecimal Roomservicelunchother;
    private BigDecimal Roomservicedinnerfood;
    private BigDecimal Roomservicedinnerdrinks;
    private BigDecimal Roomservicedinnerother;
    private BigDecimal Roomservicechargeforbreakfast;
    private BigDecimal Roomservicelunchservicecharge;
    private BigDecimal Roomservicefordinner;
    private BigDecimal Roomservicebreakfastadjustment;
    private BigDecimal Roomservicelunchadjustment;
    private BigDecimal Roomservicedinneradjustment;
    private BigDecimal localcalls;
    private BigDecimal domestictollcall;
    private BigDecimal IDD;
    private BigDecimal Telephonerateadjustment;
    private BigDecimal Telephonecharges9;
    private BigDecimal Businesscenterprint;
    private BigDecimal Adjustmentofbusinesscenter;
    private BigDecimal CBD;
    private BigDecimal miniba;
    private BigDecimal minibavat;
    private BigDecimal Minibaradjustment;
    private BigDecimal Laundryoutsidethestore;
    private BigDecimal Laundrychargeinstore;
    private BigDecimal Laundrychargeadjustment;
    private BigDecimal laundrycharge;
    private BigDecimal compensatefor;
    private BigDecimal compensateforvat;
    private BigDecimal Thecompensationadjustment;
    private BigDecimal carrental;
    private BigDecimal adjustcarrental;
    private BigDecimal Pickupanddropoff;
    private BigDecimal scenicspots;
    private BigDecimal Collectutilities;
    private BigDecimal Othercollection;
    private BigDecimal Healthcentrefoodadjustment;
    private BigDecimal miscellaneousincome;
    private BigDecimal adjustmiscellaneousincome;
    private BigDecimal miscellaneousincomevat;
    private BigDecimal rental;
    private BigDecimal adjustrental;
    private BigDecimal breakfastfoodvat;
    private BigDecimal Greenfeesforbreakfastvat;
    private BigDecimal Banquethallbreakfastother;
    private BigDecimal Banquethalllunchfoodvat;
    private BigDecimal Banquethalllunchdrinks;
    private BigDecimal Banquethalllunchrental;
    private BigDecimal Banquethalllunchothervat;
    private BigDecimal Banquethalldinnerrental;
    private BigDecimal Banquethalldinnerother;
    private BigDecimal breakfastinbanquethall;
    private BigDecimal servicechargevat;
    private BigDecimal dinnerservicechargevat;
    private BigDecimal Banquethallbreakfastadjustment;
    private BigDecimal Banquethalllunchadjustment;
    private BigDecimal Banquethalldinneradjustment;
    private BigDecimal Banquethallrental;
    private BigDecimal Banquethallrentadjustment;
    private BigDecimal Cafebreakfastvat;
    private BigDecimal Coffeeshopbreakfastdrinks;
    private BigDecimal Cafebreakfastothervat;
    private BigDecimal Cafelunchvat;
    private BigDecimal Cafelunchdrinksvat;
    private BigDecimal Cafelunchothervat;
    private BigDecimal Cafedinnerfoodvat;
    private BigDecimal Cafedinnerdrinksvat;
    private BigDecimal Cafedinnerothervat;
    private BigDecimal servicechargevat6;
    private BigDecimal Cafelunchservicevat;
    private BigDecimal Cafedinnerservicevat;
    private BigDecimal Cafebreakfastadjustmentvat;
    private BigDecimal Cafelunchadjustmentvat;
    private BigDecimal Cafedinneradjustmentvat;
    private BigDecimal Roomservicebreakfastfoodvat;
    private BigDecimal Roomservicebreakfastdrinksvat;
    private BigDecimal Roomservicebreakfastothervat;
    private BigDecimal Roomservicelunchvat;
    private BigDecimal Roomservicelunchdrinksvat;
    private BigDecimal Roomservicelunchothervat;
    private BigDecimal Roomservicedinnerfoodvat;
    private BigDecimal Roomservicedinnerdrinksvat;
    private BigDecimal roomservice;
    private BigDecimal Roomservicechargeforbreakfastvat;
    private BigDecimal Roomservicelunchvatvat;
    private BigDecimal Roomservicefordinnervat;
    private BigDecimal Roomservicebreakfastadjustmentvat;
    private BigDecimal Roomservicelunchadjustmentvat;
    private BigDecimal Roomservicedinneradjustmentvat;
    private BigDecimal charterhire;
    private BigDecimal Acashrefund;
    private BigDecimal cash;
    private BigDecimal Paymentofthecity;
    private BigDecimal WeChatPay;
    private BigDecimal Alipay;
    private BigDecimal banktransfer;
    private BigDecimal membershipcard;
    private BigDecimal POScash;
    private BigDecimal POSForeigncard;
    private BigDecimal POSDomesticcard;
    private BigDecimal POSPaymentofthecity;
    private BigDecimal poswx;
    private BigDecimal poszfb;
    private BigDecimal posvip;
    private BigDecimal Cashreceivable;
    private BigDecimal Bankreceivables;
    private BigDecimal commission;
    private BigDecimal Creditcardcollection;
    private BigDecimal Creditcarddebit;
    private BigDecimal Creditcrdcommission;
    private BigDecimal Creditcardcommissioncharge;
    private BigDecimal ARAccountsreceivablerefund;
    private BigDecimal compress;
    private BigDecimal Generalledgerhedge;
    private BigDecimal Systemadjustment;
    private BigDecimal Adjustmentofpickupandpickup;
    private BigDecimal Cafelunchpackage;
    private BigDecimal Cafedinnerincluded;
    private BigDecimal Guestroompackage;
    private BigDecimal Honeymoonarrangement;
    private BigDecimal Toupgradetheroomcharge;
    private BigDecimal Balancetransfer;
    private BigDecimal Recreationcentrefood;
    private BigDecimal Recreationcentrebeverages;
    private BigDecimal Recreationcentreswimmingring;
    private BigDecimal Recreationcentrefishtherapy;
    private BigDecimal Recreationcentrebicycles;
    private BigDecimal Sandtoysforchildren;
    private BigDecimal Recreationcentertax;
    private BigDecimal Recreationswimwear;
    private BigDecimal Recreationswimwear6;
    private BigDecimal beverageadjustment;
    private BigDecimal swimmingringadjustment;
    private BigDecimal Fishtoadjust;
    private BigDecimal Bicycleadjustment;
    private BigDecimal childrenstoys;
    private BigDecimal Otheradjustments;
    private BigDecimal Theswimsuitadjustment;
    private BigDecimal LandmarkTicket;
    private BigDecimal utilitybills;
    private BigDecimal Othercollectionadjust;
    private BigDecimal honeymoonadjust;
    private BigDecimal foodstuff;
    private BigDecimal Healthbeverage;
    private BigDecimal swimring;
    private BigDecimal Fishpedicure;
    private BigDecimal sportsbicycle;
    private BigDecimal sportschildrenstoys;
    private BigDecimal sportsother;
    private BigDecimal Theotherpackage;
    private BigDecimal Systemswitchingbalance;
    private BigDecimal switchingbalance;
    private BigDecimal cheque;
    private BigDecimal Foreigncard;
    private BigDecimal Domesticcard;
    private BigDecimal UnionPaycard;
    private BigDecimal POSUnionPaycard;
    private BigDecimal footwear;
    private BigDecimal Frozencoconutsetmeal;
    private BigDecimal Fishmeal;
    private BigDecimal Swimminglapspackage;
    private BigDecimal Icecreamset;
    private BigDecimal Toypackages;
    private BigDecimal JBCCard;
    private BigDecimal DinersClubcard;
    private BigDecimal Thetransportcard;
    private BigDecimal MasterCard;
    private BigDecimal visacard;
    private BigDecimal POSUnionPaycard2;
    private BigDecimal roomcharge;
    private BigDecimal Coffeeshopdrinksincluded;
    private BigDecimal packagepricedrink;
    private BigDecimal packagepricefood;
    private BigDecimal otherone;
    private BigDecimal otherrevenue;
    private BigDecimal otherrevenuetax;
    private BigDecimal tallage9;
    private BigDecimal tallage13;
    private BigDecimal tallage5;
    private BigDecimal tallage6;
    private BigDecimal tallagevat9;
    private BigDecimal tallage13vat;
    private BigDecimal taxadjustment5;
    private BigDecimal taxadjustment6;
    private BigDecimal taxadjustment9;
    private BigDecimal taxadjustment13;
    private BigDecimal Thecollectingpackage;
    private BigDecimal Honeymoonarrangementvat;
    private BigDecimal Thecollectingtaxes;

    private BigDecimal prices;//总消费

}
