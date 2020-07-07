package org.jeecg.modules.zzj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 预订单号
 * @Author: jeecg-boot
 * @Date:   2019-10-28
 * @Version: V1.0
 */
@Data
@TableName("Reservation_Name")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Reservation_Name对象", description="预订单号")
public class Klreservationname {
    
	/**resvNameId*/
	@Excel(name = "resvNameId", width = 15)
    @ApiModelProperty(value = "resvNameId")
	private Integer resvNameId;
	/**resort*/
	@Excel(name = "resort", width = 15)
    @ApiModelProperty(value = "resort")
	private Integer resort;
	/**nameId*/
	@Excel(name = "nameId", width = 15)
    @ApiModelProperty(value = "nameId")
	private Integer nameId;
	/**altName*/
	@Excel(name = "altName", width = 15)
    @ApiModelProperty(value = "altName")
	private String altName;
	/**lastName*/
	@Excel(name = "lastName", width = 15)
    @ApiModelProperty(value = "lastName")
	private String lastName;
	/**firstName*/
	@Excel(name = "firstName", width = 15)
    @ApiModelProperty(value = "firstName")
	private String firstName;
	/**language*/
	@Excel(name = "language", width = 15)
    @ApiModelProperty(value = "language")
	private String language;
	/**confirmationNo*/
	@Excel(name = "confirmationNo", width = 15)
    @ApiModelProperty(value = "confirmationNo")
	private String confirmationNo;
	/**resvStatus*/
	@Excel(name = "resvStatus", width = 15)
    @ApiModelProperty(value = "resvStatus")
	private String resvStatus;
	/**resvType*/
	@Excel(name = "resvType", width = 15)
    @ApiModelProperty(value = "resvType")
	private String resvType;
	/**rateCode*/
	@Excel(name = "rateCode", width = 15)
    @ApiModelProperty(value = "rateCode")
	private String rateCode;
	/**rateFinally*/
	@Excel(name = "rateFinally", width = 15)
    @ApiModelProperty(value = "rateFinally")
	private java.math.BigDecimal rateFinally;
	/**rateNotDiscount*/
	@Excel(name = "rateNotDiscount", width = 15)
    @ApiModelProperty(value = "rateNotDiscount")
	private java.math.BigDecimal rateNotDiscount;
	/**fixedRate*/
	@Excel(name = "fixedRate", width = 15)
    @ApiModelProperty(value = "fixedRate")
	private String fixedRate;
	/**packages*/
	@Excel(name = "packages", width = 15)
    @ApiModelProperty(value = "packages")
	private String packages;
	/**rtc*/
	@Excel(name = "rtc", width = 15)
    @ApiModelProperty(value = "rtc")
	private String rtc;
	/**block*/
	@Excel(name = "block", width = 15)
    @ApiModelProperty(value = "block")
	private String block;
	/**isBlock*/
	@Excel(name = "isBlock", width = 15)
    @ApiModelProperty(value = "isBlock")
	private String isBlock;
	/**roomClass*/
	@Excel(name = "roomClass", width = 15)
    @ApiModelProperty(value = "roomClass")
	private String roomClass;
	/**roomType*/
	@Excel(name = "roomType", width = 15)
    @ApiModelProperty(value = "roomType")
	private String roomType;
	/**roomNo*/
	@Excel(name = "roomNo", width = 15)
    @ApiModelProperty(value = "roomNo")
	private String roomNo;
	/**roomCounter*/
	@Excel(name = "roomCounter", width = 15)
    @ApiModelProperty(value = "roomCounter")
	private Integer roomCounter;
	/**floor*/
	@Excel(name = "floor", width = 15)
    @ApiModelProperty(value = "floor")
	private String floor;
	/**company*/
	@Excel(name = "company", width = 15)
    @ApiModelProperty(value = "company")
	private String company;
	/**group*/
	@Excel(name = "group", width = 15)
    @ApiModelProperty(value = "group")
	private String group;
	/**source*/
	@Excel(name = "source", width = 15)
    @ApiModelProperty(value = "source")
	private String source;
	/**agent*/
	@Excel(name = "agent", width = 15)
    @ApiModelProperty(value = "agent")
	private String agent;
	/**contact*/
	@Excel(name = "contact", width = 15)
    @ApiModelProperty(value = "contact")
	private String contact;
	/**memberType*/
	@Excel(name = "memberType", width = 15)
    @ApiModelProperty(value = "memberType")
	private String memberType;
	/**memberNo*/
	@Excel(name = "memberNo", width = 15)
    @ApiModelProperty(value = "memberNo")
	private String memberNo;
	/**membershipLevel*/
	@Excel(name = "membershipLevel", width = 15)
    @ApiModelProperty(value = "membershipLevel")
	private String membershipLevel;
	/**marketCode*/
	@Excel(name = "marketCode", width = 15)
    @ApiModelProperty(value = "marketCode")
	private String marketCode;
	/**sourceCode*/
	@Excel(name = "sourceCode", width = 15)
    @ApiModelProperty(value = "sourceCode")
	private String sourceCode;
	/**country*/
	@Excel(name = "country", width = 15)
    @ApiModelProperty(value = "country")
	private String country;
	/**state*/
	@Excel(name = "state", width = 15)
    @ApiModelProperty(value = "state")
	private String state;
	/**city*/
	@Excel(name = "city", width = 15)
    @ApiModelProperty(value = "city")
	private String city;
	/**creditCard*/
	@Excel(name = "creditCard", width = 15)
    @ApiModelProperty(value = "creditCard")
	private String creditCard;
	/**creditCardHolder*/
	@Excel(name = "creditCardHolder", width = 15)
    @ApiModelProperty(value = "creditCardHolder")
	private String creditCardHolder;
	/**creditCardId*/
	@Excel(name = "creditCardId", width = 15)
    @ApiModelProperty(value = "creditCardId")
	private String creditCardId;
	/**creditCardDate*/
	@Excel(name = "creditCardDate", width = 15)
    @ApiModelProperty(value = "creditCardDate")
	private String creditCardDate;
	/**vipLevel*/
	@Excel(name = "vipLevel", width = 15)
    @ApiModelProperty(value = "vipLevel")
	private String vipLevel;
	/**title*/
	@Excel(name = "title", width = 15)
    @ApiModelProperty(value = "title")
	private String title;
	/**nights*/
	@Excel(name = "nights", width = 15)
    @ApiModelProperty(value = "nights")
	private Integer nights;
	/**adults*/
	@Excel(name = "adults", width = 15)
    @ApiModelProperty(value = "adults")
	private Integer adults;
	/**children*/
	@Excel(name = "children", width = 15)
    @ApiModelProperty(value = "children")
	private Integer children;
	/**beginDate*/
	@Excel(name = "beginDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "beginDate")
	private Date beginDate;
	/**endDate*/
	@Excel(name = "endDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "endDate")
	private Date endDate;
	/**paymentMethod*/
	@Excel(name = "paymentMethod", width = 15)
    @ApiModelProperty(value = "paymentMethod")
	private String paymentMethod;
	/**approlcalCode*/
	@Excel(name = "approlcalCode", width = 15)
    @ApiModelProperty(value = "approlcalCode")
	private String approlcalCode;
	/**specialNeed*/
	@Excel(name = "specialNeed", width = 15)
    @ApiModelProperty(value = "specialNeed")
	private String specialNeed;
	/**goodsUsed*/
	@Excel(name = "goodsUsed", width = 15)
    @ApiModelProperty(value = "goodsUsed")
	private String goodsUsed;
	/**promotion*/
	@Excel(name = "promotion", width = 15)
    @ApiModelProperty(value = "promotion")
	private String promotion;
	/**message*/
	@Excel(name = "message", width = 15)
    @ApiModelProperty(value = "message")
	private String message;
	/**noPost*/
	@Excel(name = "noPost", width = 15)
    @ApiModelProperty(value = "noPost")
	private Integer noPost;
	/**partyCode*/
	@Excel(name = "partyCode", width = 15)
    @ApiModelProperty(value = "partyCode")
	private Integer partyCode;
	/**walkinYn*/
	@Excel(name = "walkinYn", width = 15)
    @ApiModelProperty(value = "walkinYn")
	private Integer walkinYn;
	/**doNotMove*/
	@Excel(name = "doNotMove", width = 15)
    @ApiModelProperty(value = "doNotMove")
	private String doNotMove;
	/**originalEndDate*/
	@Excel(name = "originalEndDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "originalEndDate")
	private Date originalEndDate;
	/**actualCheckInDate*/
	@Excel(name = "actualCheckInDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "actualCheckInDate")
	private Date actualCheckInDate;
	/**actualCheckOutDate*/
	@Excel(name = "actualCheckOutDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "actualCheckOutDate")
	private Date actualCheckOutDate;
	/**channelRowid*/
	@Excel(name = "channelRowid", width = 15)
    @ApiModelProperty(value = "channelRowid")
	private String channelRowid;
	/**channelCode*/
	@Excel(name = "channelCode", width = 15)
    @ApiModelProperty(value = "channelCode")
	private String channelCode;
	/**reservationComments*/
	@Excel(name = "reservationComments", width = 15)
    @ApiModelProperty(value = "reservationComments")
	private String reservationComments;
	/**cashierComments*/
	@Excel(name = "cashierComments", width = 15)
    @ApiModelProperty(value = "cashierComments")
	private String cashierComments;
	/**callerName*/
	@Excel(name = "callerName", width = 15)
    @ApiModelProperty(value = "callerName")
	private String callerName;
	/**callerPhone*/
	@Excel(name = "callerPhone", width = 15)
    @ApiModelProperty(value = "callerPhone")
	private String callerPhone;
	/**discountAmount*/
	@Excel(name = "discountAmount", width = 15)
    @ApiModelProperty(value = "discountAmount")
	private java.math.BigDecimal discountAmount;
	/**discountPrecent*/
	@Excel(name = "discountPrecent", width = 15)
    @ApiModelProperty(value = "discountPrecent")
	private java.math.BigDecimal discountPrecent;
	/**discountReason*/
	@Excel(name = "discountReason", width = 15)
    @ApiModelProperty(value = "discountReason")
	private String discountReason;
	/**discountComments*/
	@Excel(name = "discountComments", width = 15)
    @ApiModelProperty(value = "discountComments")
	private String discountComments;
	/**roomFeatures*/
	@Excel(name = "roomFeatures", width = 15)
    @ApiModelProperty(value = "roomFeatures")
	private String roomFeatures;
	/**groupCommissionCode*/
	@Excel(name = "groupCommissionCode", width = 15)
    @ApiModelProperty(value = "groupCommissionCode")
	private String groupCommissionCode;
	/**groupCommissionAmout*/
	@Excel(name = "groupCommissionAmout", width = 15)
    @ApiModelProperty(value = "groupCommissionAmout")
	private java.math.BigDecimal groupCommissionAmout;
	/**groupCommissionPaid*/
	@Excel(name = "groupCommissionPaid", width = 15)
    @ApiModelProperty(value = "groupCommissionPaid")
	private java.math.BigDecimal groupCommissionPaid;
	/**commissionCode*/
	@Excel(name = "commissionCode", width = 15)
    @ApiModelProperty(value = "commissionCode")
	private String commissionCode;
	/**commissionAmount*/
	@Excel(name = "commissionAmount", width = 15)
    @ApiModelProperty(value = "commissionAmount")
	private java.math.BigDecimal commissionAmount;
	/**commissionPaid*/
	@Excel(name = "commissionPaid", width = 15)
    @ApiModelProperty(value = "commissionPaid")
	private java.math.BigDecimal commissionPaid;
	/**addressId*/
	@Excel(name = "addressId", width = 15)
    @ApiModelProperty(value = "addressId")
	private String addressId;
	/**address*/
	@Excel(name = "address", width = 15)
    @ApiModelProperty(value = "address")
	private String address;
	/**phone*/
	@Excel(name = "phone", width = 15)
    @ApiModelProperty(value = "phone")
	private String phone;
	/**preChargeYn*/
	@Excel(name = "preChargeYn", width = 15)
    @ApiModelProperty(value = "preChargeYn")
	private String preChargeYn;
	/**postChargeYn*/
	@Excel(name = "postChargeYn", width = 15)
    @ApiModelProperty(value = "postChargeYn")
	private String postChargeYn;
	/**businessDateCreated*/
	@Excel(name = "businessDateCreated", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "businessDateCreated")
	private Date businessDateCreated;
	/**printRateYn*/
	@Excel(name = "printRateYn", width = 15)
    @ApiModelProperty(value = "printRateYn")
	private Integer printRateYn;
	/**extraText1*/
	@Excel(name = "extraText1", width = 15)
    @ApiModelProperty(value = "extraText1")
	private String extraText1;
	/**extraText2*/
	@Excel(name = "extraText2", width = 15)
    @ApiModelProperty(value = "extraText2")
	private String extraText2;
	/**extraText3*/
	@Excel(name = "extraText3", width = 15)
    @ApiModelProperty(value = "extraText3")
	private String extraText3;
	/**searchDate*/
	@Excel(name = "searchDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "searchDate")
	private Date searchDate;
	/**insertDate*/
	@Excel(name = "insertDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "insertDate")
	private Date insertDate;
	/**insertUser*/
	@Excel(name = "insertUser", width = 15)
    @ApiModelProperty(value = "insertUser")
	private Integer insertUser;
	/**updateDate*/
	@Excel(name = "updateDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "updateDate")
	private Date updateDate;
	/**updateUser*/
	@Excel(name = "updateUser", width = 15)
    @ApiModelProperty(value = "updateUser")
	private Integer updateUser;
	/**companyId*/
	@Excel(name = "companyId", width = 15)
    @ApiModelProperty(value = "companyId")
	private Integer companyId;
	/**blockId*/
	@Excel(name = "blockId", width = 15)
    @ApiModelProperty(value = "blockId")
	private Integer blockId;
	/**sourceId*/
	@Excel(name = "sourceId", width = 15)
    @ApiModelProperty(value = "sourceId")
	private Integer sourceId;
	/**agentId*/
	@Excel(name = "agentId", width = 15)
    @ApiModelProperty(value = "agentId")
	private Integer agentId;
	/**naDate*/
	@Excel(name = "naDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "naDate")
	private Date naDate;
	/**isPm*/
	@Excel(name = "isPm", width = 15)
    @ApiModelProperty(value = "isPm")
	private Integer isPm;
	/**isFreeroom*/
	@Excel(name = "isFreeroom", width = 15)
    @ApiModelProperty(value = "isFreeroom")
	private Integer isFreeroom;
	/**isHouseuse*/
	@Excel(name = "isHouseuse", width = 15)
    @ApiModelProperty(value = "isHouseuse")
	private Integer isHouseuse;
	/**rtcReason*/
	@Excel(name = "rtcReason", width = 15)
    @ApiModelProperty(value = "rtcReason")
	private String rtcReason;
	/**isOpenbalance*/
	@Excel(name = "isOpenbalance", width = 15)
    @ApiModelProperty(value = "isOpenbalance")
	private Integer isOpenbalance;
	/**waitreason*/
	@Excel(name = "waitreason", width = 15)
    @ApiModelProperty(value = "waitreason")
	private String waitreason;
	/**waitpriority*/
	@Excel(name = "waitpriority", width = 15)
    @ApiModelProperty(value = "waitpriority")
	private Integer waitpriority;
	/**waitdescription*/
	@Excel(name = "waitdescription", width = 15)
    @ApiModelProperty(value = "waitdescription")
	private String waitdescription;
	/**insertUserName*/
	@Excel(name = "insertUserName", width = 15)
    @ApiModelProperty(value = "insertUserName")
	private String insertUserName;
	/**updateUserName*/
	@Excel(name = "updateUserName", width = 15)
    @ApiModelProperty(value = "updateUserName")
	private String updateUserName;
	/**cancelReason*/
	@Excel(name = "cancelReason", width = 15)
    @ApiModelProperty(value = "cancelReason")
	private String cancelReason;
	/**rate*/
	@Excel(name = "rate", width = 15)
    @ApiModelProperty(value = "rate")
	private java.math.BigDecimal rate;
	/**checkintime*/
	@Excel(name = "checkintime", width = 15)
    @ApiModelProperty(value = "checkintime")
	private String checkintime;
	/**checkouttime*/
	@Excel(name = "checkouttime", width = 15)
    @ApiModelProperty(value = "checkouttime")
	private String checkouttime;
	/**crsId*/
	@Excel(name = "crsId", width = 15)
    @ApiModelProperty(value = "crsId")
	private String crsId;
	/**sharecode*/
	@Excel(name = "sharecode", width = 15)
    @ApiModelProperty(value = "sharecode")
	private Integer sharecode;
	/**balance*/
	@Excel(name = "balance", width = 15)
    @ApiModelProperty(value = "balance")
	private java.math.BigDecimal balance;
	/**totamount*/
	@Excel(name = "totamount", width = 15)
    @ApiModelProperty(value = "totamount")
	private java.math.BigDecimal totamount;
	/**groupId*/
	@Excel(name = "groupId", width = 15)
    @ApiModelProperty(value = "groupId")
	private Integer groupId;
	/**changeReason*/
	@Excel(name = "changeReason", width = 15)
    @ApiModelProperty(value = "changeReason")
	private String changeReason;
	/**seller*/
	@Excel(name = "seller", width = 15)
    @ApiModelProperty(value = "seller")
	private String seller;
	/**originalBeginDate*/
	@Excel(name = "originalBeginDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "originalBeginDate")
	private Date originalBeginDate;
	/**sellerrowid*/
	@Excel(name = "sellerrowid", width = 15)
    @ApiModelProperty(value = "sellerrowid")
	private String sellerrowid;
	/**pad*/
	@Excel(name = "pad", width = 15)
    @ApiModelProperty(value = "pad")
	private java.math.BigDecimal pad;
	/**extraText4*/
	@Excel(name = "extraText4", width = 15)
    @ApiModelProperty(value = "extraText4")
	private String extraText4;
	/**superblock*/
	@Excel(name = "superblock", width = 15)
    @ApiModelProperty(value = "superblock")
	private Integer superblock;
	/**callerMember*/
	@Excel(name = "callerMember", width = 15)
    @ApiModelProperty(value = "callerMember")
	private String callerMember;
	/**callerIdcard*/
	@Excel(name = "callerIdcard", width = 15)
    @ApiModelProperty(value = "callerIdcard")
	private String callerIdcard;
	/**identitycard*/
	@Excel(name = "identitycard", width = 15)
    @ApiModelProperty(value = "identitycard")
	private String identitycard;
	/**sharetype*/
	@Excel(name = "sharetype", width = 15)
    @ApiModelProperty(value = "sharetype")
	private Integer sharetype;
	/**splitType*/
	@Excel(name = "splitType", width = 15)
    @ApiModelProperty(value = "splitType")
	private Integer splitType;
	/**isdelayed*/
	@Excel(name = "isdelayed", width = 15)
    @ApiModelProperty(value = "isdelayed")
	private Integer isdelayed;
	/**checkoutdelaytime*/
	@Excel(name = "checkoutdelaytime", width = 15)
    @ApiModelProperty(value = "checkoutdelaytime")
	private String checkoutdelaytime;
	/**certificateType*/
	@Excel(name = "certificateType", width = 15)
    @ApiModelProperty(value = "certificateType")
	private String certificateType;
	/**accompanyId*/
	@Excel(name = "accompanyId", width = 15)
    @ApiModelProperty(value = "accompanyId")
	private String accompanyId;
	/**idNumber*/
	@Excel(name = "idNumber", width = 15)
    @ApiModelProperty(value = "idNumber")
	private Integer idNumber;
}
