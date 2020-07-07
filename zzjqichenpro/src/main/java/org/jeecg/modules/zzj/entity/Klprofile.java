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

/**
 * @Description: 客户档案表
 * @Author: jeecg-boot
 * @Date:   2019-10-28
 * @Version: V1.0
 */
@Data
@TableName("PROFILE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PROFILE对象", description="客户档案表")
public class Klprofile {
    
	/**nameId*/
	@Excel(name = "nameId", width = 15)
    @ApiModelProperty(value = "nameId")
	private Integer nameId;
	/**rowid*/
	@Excel(name = "rowid", width = 15)
    @ApiModelProperty(value = "rowid")
	private String rowid;
	/**resort*/
	@Excel(name = "resort", width = 15)
    @ApiModelProperty(value = "resort")
	private Integer resort;
	/**nameType*/
	@Excel(name = "nameType", width = 15)
    @ApiModelProperty(value = "nameType")
	private String nameType;
	/**nameCode*/
	@Excel(name = "nameCode", width = 15)
    @ApiModelProperty(value = "nameCode")
	private String nameCode;
	/**last*/
	@Excel(name = "last", width = 15)
    @ApiModelProperty(value = "last")
	private String last;
	/**first*/
	@Excel(name = "first", width = 15)
    @ApiModelProperty(value = "first")
	private String first;
	/**language*/
	@Excel(name = "language", width = 15)
    @ApiModelProperty(value = "language")
	private String language;
	/**title*/
	@Excel(name = "title", width = 15)
    @ApiModelProperty(value = "title")
	private String title;
	/**gender*/
	@Excel(name = "gender", width = 15)
    @ApiModelProperty(value = "gender")
	private String gender;
	/**salutation*/
	@Excel(name = "salutation", width = 15)
    @ApiModelProperty(value = "salutation")
	private String salutation;
	/**company*/
	@Excel(name = "company", width = 15)
    @ApiModelProperty(value = "company")
	private String company;
	/**sname*/
	@Excel(name = "sname", width = 15)
    @ApiModelProperty(value = "sname")
	private String sname;
	/**sfirst*/
	@Excel(name = "sfirst", width = 15)
    @ApiModelProperty(value = "sfirst")
	private String sfirst;
	/**nationality*/
	@Excel(name = "nationality", width = 15)
    @ApiModelProperty(value = "nationality")
	private String nationality;
	/**birthDate*/
	@Excel(name = "birthDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "birthDate")
	private java.util.Date birthDate;
	/**vip*/
	@Excel(name = "vip", width = 15)
    @ApiModelProperty(value = "vip")
	private String vip;
	/**passport*/
	@Excel(name = "passport", width = 15)
    @ApiModelProperty(value = "passport")
	private String passport;
	/**keyword*/
	@Excel(name = "keyword", width = 15)
    @ApiModelProperty(value = "keyword")
	private String keyword;
	/**rateCode*/
	@Excel(name = "rateCode", width = 15)
    @ApiModelProperty(value = "rateCode")
	private String rateCode;
	/**scope*/
	@Excel(name = "scope", width = 15)
    @ApiModelProperty(value = "scope")
	private String scope;
	/**priority*/
	@Excel(name = "priority", width = 15)
    @ApiModelProperty(value = "priority")
	private String priority;
	/**industryCode*/
	@Excel(name = "industryCode", width = 15)
    @ApiModelProperty(value = "industryCode")
	private String industryCode;
	/**xlast*/
	@Excel(name = "xlast", width = 15)
    @ApiModelProperty(value = "xlast")
	private String xlast;
	/**xfirst*/
	@Excel(name = "xfirst", width = 15)
    @ApiModelProperty(value = "xfirst")
	private String xfirst;
	/**xlanguage*/
	@Excel(name = "xlanguage", width = 15)
    @ApiModelProperty(value = "xlanguage")
	private String xlanguage;
	/**xtitle*/
	@Excel(name = "xtitle", width = 15)
    @ApiModelProperty(value = "xtitle")
	private String xtitle;
	/**xsalutation*/
	@Excel(name = "xsalutation", width = 15)
    @ApiModelProperty(value = "xsalutation")
	private String xsalutation;
	/**xcompany*/
	@Excel(name = "xcompany", width = 15)
    @ApiModelProperty(value = "xcompany")
	private String xcompany;
	/**sxname*/
	@Excel(name = "sxname", width = 15)
    @ApiModelProperty(value = "sxname")
	private String sxname;
	/**sxfirst*/
	@Excel(name = "sxfirst", width = 15)
    @ApiModelProperty(value = "sxfirst")
	private String sxfirst;
	/**idType*/
	@Excel(name = "idType", width = 15)
    @ApiModelProperty(value = "idType")
	private String idType;
	/**idNumber*/
	@Excel(name = "idNumber", width = 15)
    @ApiModelProperty(value = "idNumber")
	private String idNumber;
	/**idNumberStr*/
	@Excel(name = "idNumberStr", width = 15)
    @ApiModelProperty(value = "idNumberStr")
	private String idNumberStr;
	/**visaType*/
	@Excel(name = "visaType", width = 15)
    @ApiModelProperty(value = "visaType")
	private String visaType;
	/**passportStr*/
	@Excel(name = "passportStr", width = 15)
    @ApiModelProperty(value = "passportStr")
	private String passportStr;
	/**expDate*/
	@Excel(name = "expDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "expDate")
	private java.util.Date expDate;
	/**mailYn*/
	@Excel(name = "mailYn", width = 15)
    @ApiModelProperty(value = "mailYn")
	private String mailYn;
	/**mailAction*/
	@Excel(name = "mailAction", width = 15)
    @ApiModelProperty(value = "mailAction")
	private String mailAction;
	/**preferredRoomNo*/
	@Excel(name = "preferredRoomNo", width = 15)
    @ApiModelProperty(value = "preferredRoomNo")
	private String preferredRoomNo;
	/**restrictionMsg*/
	@Excel(name = "restrictionMsg", width = 15)
    @ApiModelProperty(value = "restrictionMsg")
	private String restrictionMsg;
	/**activeYn*/
	@Excel(name = "activeYn", width = 15)
    @ApiModelProperty(value = "activeYn")
	private String activeYn;
	/**contactYn*/
	@Excel(name = "contactYn", width = 15)
    @ApiModelProperty(value = "contactYn")
	private String contactYn;
	/**historyYn*/
	@Excel(name = "historyYn", width = 15)
    @ApiModelProperty(value = "historyYn")
	private String historyYn;
	/**arNo*/
	@Excel(name = "arNo", width = 15)
    @ApiModelProperty(value = "arNo")
	private String arNo;
	/**arLanguage*/
	@Excel(name = "arLanguage", width = 15)
    @ApiModelProperty(value = "arLanguage")
	private String arLanguage;
	/**refCurrency*/
	@Excel(name = "refCurrency", width = 15)
    @ApiModelProperty(value = "refCurrency")
	private String refCurrency;
	/**srepCode*/
	@Excel(name = "srepCode", width = 15)
    @ApiModelProperty(value = "srepCode")
	private String srepCode;
	/**commissionCode*/
	@Excel(name = "commissionCode", width = 15)
    @ApiModelProperty(value = "commissionCode")
	private String commissionCode;
	/**revPromotionInfo*/
	@Excel(name = "revPromotionInfo", width = 15)
    @ApiModelProperty(value = "revPromotionInfo")
	private String revPromotionInfo;
	/**revMarketResearchInfo*/
	@Excel(name = "revMarketResearchInfo", width = 15)
    @ApiModelProperty(value = "revMarketResearchInfo")
	private String revMarketResearchInfo;
	/**resThridpartyInfo*/
	@Excel(name = "resThridpartyInfo", width = 15)
    @ApiModelProperty(value = "resThridpartyInfo")
	private String resThridpartyInfo;
	/**revEmail*/
	@Excel(name = "revEmail", width = 15)
    @ApiModelProperty(value = "revEmail")
	private String revEmail;
	/**scopeCity*/
	@Excel(name = "scopeCity", width = 15)
    @ApiModelProperty(value = "scopeCity")
	private String scopeCity;
	/**influence*/
	@Excel(name = "influence", width = 15)
    @ApiModelProperty(value = "influence")
	private String influence;
	/**actionCode*/
	@Excel(name = "actionCode", width = 15)
    @ApiModelProperty(value = "actionCode")
	private String actionCode;
	/**businessSegment*/
	@Excel(name = "businessSegment", width = 15)
    @ApiModelProperty(value = "businessSegment")
	private String businessSegment;
	/**territory*/
	@Excel(name = "territory", width = 15)
    @ApiModelProperty(value = "territory")
	private String territory;
	/**roomProtential*/
	@Excel(name = "roomProtential", width = 15)
    @ApiModelProperty(value = "roomProtential")
	private String roomProtential;
	/**competitionCode*/
	@Excel(name = "competitionCode", width = 15)
    @ApiModelProperty(value = "competitionCode")
	private String competitionCode;
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
	/**certificateType*/
	@Excel(name = "certificateType", width = 15)
    @ApiModelProperty(value = "certificateType")
	private String certificateType;
	/**certificateCode*/
	@Excel(name = "certificateCode", width = 15)
    @ApiModelProperty(value = "certificateCode")
	private String certificateCode;
	/**address*/
	@Excel(name = "address", width = 15)
    @ApiModelProperty(value = "address")
	private String address;
	/**email*/
	@Excel(name = "email", width = 15)
    @ApiModelProperty(value = "email")
	private String email;
	/**profession*/
	@Excel(name = "profession", width = 15)
    @ApiModelProperty(value = "profession")
	private String profession;
	/**phone*/
	@Excel(name = "phone", width = 15)
    @ApiModelProperty(value = "phone")
	private String phone;
	/**notes*/
	@Excel(name = "notes", width = 15)
    @ApiModelProperty(value = "notes")
	private String notes;
	/**state*/
	@Excel(name = "state", width = 15)
    @ApiModelProperty(value = "state")
	private String state;
	/**city*/
	@Excel(name = "city", width = 15)
    @ApiModelProperty(value = "city")
	private String city;
	/**zipcode*/
	@Excel(name = "zipcode", width = 15)
    @ApiModelProperty(value = "zipcode")
	private String zipcode;
	/**searchDate*/
	@Excel(name = "searchDate", width = 15)
    @ApiModelProperty(value = "searchDate")
	private String searchDate;
	/**country*/
	@Excel(name = "country", width = 15)
    @ApiModelProperty(value = "country")
	private String country;
	/**housecode*/
	@Excel(name = "housecode", width = 15)
    @ApiModelProperty(value = "housecode")
	private String housecode;
	/**iatanumber*/
	@Excel(name = "iatanumber", width = 15)
    @ApiModelProperty(value = "iatanumber")
	private String iatanumber;
	/**fax*/
	@Excel(name = "fax", width = 15)
    @ApiModelProperty(value = "fax")
	private String fax;
	/**call*/
	@Excel(name = "call", width = 15)
    @ApiModelProperty(value = "call")
	private String call;
	/**contractprice*/
	@Excel(name = "contractprice", width = 15)
    @ApiModelProperty(value = "contractprice")
	private String contractprice;
	/**recievedate*/
	@Excel(name = "recievedate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "recievedate")
	private java.util.Date recievedate;
	/**financialcontact*/
	@Excel(name = "financialcontact", width = 15)
    @ApiModelProperty(value = "financialcontact")
	private String financialcontact;
	/**description*/
	@Excel(name = "description", width = 15)
    @ApiModelProperty(value = "description")
	private String description;
	/**job*/
	@Excel(name = "job", width = 15)
    @ApiModelProperty(value = "job")
	private String job;
	/**insertDate*/
	@Excel(name = "insertDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "insertDate")
	private java.util.Date insertDate;
	/**insertUser*/
	@Excel(name = "insertUser", width = 15)
    @ApiModelProperty(value = "insertUser")
	private Integer insertUser;
	/**updateDate*/
	@Excel(name = "updateDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "updateDate")
	private java.util.Date updateDate;
	/**updateUser*/
	@Excel(name = "updateUser", width = 15)
    @ApiModelProperty(value = "updateUser")
	private Integer updateUser;
	/**membercard*/
	@Excel(name = "membercard", width = 15)
    @ApiModelProperty(value = "membercard")
	private String membercard;
	/**salesmanager*/
	@Excel(name = "salesmanager", width = 15)
    @ApiModelProperty(value = "salesmanager")
	private String salesmanager;
	/**visadate*/
	@Excel(name = "visadate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "visadate")
	private java.util.Date visadate;
	/**specialNeed*/
	@Excel(name = "specialNeed", width = 15)
    @ApiModelProperty(value = "specialNeed")
	private String specialNeed;
	/**ethnic*/
	@Excel(name = "ethnic", width = 15)
    @ApiModelProperty(value = "ethnic")
	private String ethnic;
	/**roomFeatures*/
	@Excel(name = "roomFeatures", width = 15)
    @ApiModelProperty(value = "roomFeatures")
	private String roomFeatures;
	/**membertype*/
	@Excel(name = "membertype", width = 15)
    @ApiModelProperty(value = "membertype")
	private String membertype;
	/**memberdate*/
	@Excel(name = "memberdate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "memberdate")
	private java.util.Date memberdate;
	/**addressType1*/
	@Excel(name = "addressType1", width = 15)
    @ApiModelProperty(value = "addressType1")
	private String addressType1;
	/**address1*/
	@Excel(name = "address1", width = 15)
    @ApiModelProperty(value = "address1")
	private String address1;
	/**addressType2*/
	@Excel(name = "addressType2", width = 15)
    @ApiModelProperty(value = "addressType2")
	private String addressType2;
	/**address2*/
	@Excel(name = "address2", width = 15)
    @ApiModelProperty(value = "address2")
	private String address2;
	/**addressType3*/
	@Excel(name = "addressType3", width = 15)
    @ApiModelProperty(value = "addressType3")
	private String addressType3;
	/**address3*/
	@Excel(name = "address3", width = 15)
    @ApiModelProperty(value = "address3")
	private String address3;
	/**phoneType1*/
	@Excel(name = "phoneType1", width = 15)
    @ApiModelProperty(value = "phoneType1")
	private String phoneType1;
	/**phone1*/
	@Excel(name = "phone1", width = 15)
    @ApiModelProperty(value = "phone1")
	private String phone1;
	/**phoneType2*/
	@Excel(name = "phoneType2", width = 15)
    @ApiModelProperty(value = "phoneType2")
	private String phoneType2;
	/**phone2*/
	@Excel(name = "phone2", width = 15)
    @ApiModelProperty(value = "phone2")
	private String phone2;
	/**phoneType3*/
	@Excel(name = "phoneType3", width = 15)
    @ApiModelProperty(value = "phoneType3")
	private String phoneType3;
	/**phone3*/
	@Excel(name = "phone3", width = 15)
    @ApiModelProperty(value = "phone3")
	private String phone3;
	/**portofentry*/
	@Excel(name = "portofentry", width = 15)
    @ApiModelProperty(value = "portofentry")
	private String portofentry;
	/**protocolinformation*/
	@Excel(name = "protocolinformation", width = 15)
    @ApiModelProperty(value = "protocolinformation")
	private String protocolinformation;
	/**lastcontactsdate*/
	@Excel(name = "lastcontactsdate", width = 15)
    @ApiModelProperty(value = "lastcontactsdate")
	private String lastcontactsdate;
	/**emailnumber*/
	@Excel(name = "emailnumber", width = 15)
    @ApiModelProperty(value = "emailnumber")
	private String emailnumber;
	/**specialroompayment*/
	@Excel(name = "specialroompayment", width = 15)
    @ApiModelProperty(value = "specialroompayment")
	private String specialroompayment;
	/**customerstatus*/
	@Excel(name = "customerstatus", width = 15)
    @ApiModelProperty(value = "customerstatus")
	private String customerstatus;
	/**lossesinfo*/
	@Excel(name = "lossesinfo", width = 15)
    @ApiModelProperty(value = "lossesinfo")
	private String lossesinfo;
	/**accountsreceivable*/
	@Excel(name = "accountsreceivable", width = 15)
    @ApiModelProperty(value = "accountsreceivable")
	private String accountsreceivable;
	/**billsreceivable*/
	@Excel(name = "billsreceivable", width = 15)
    @ApiModelProperty(value = "billsreceivable")
	private String billsreceivable;
	/**insertUserName*/
	@Excel(name = "insertUserName", width = 15)
    @ApiModelProperty(value = "insertUserName")
	private String insertUserName;
	/**updateUserName*/
	@Excel(name = "updateUserName", width = 15)
    @ApiModelProperty(value = "updateUserName")
	private String updateUserName;
	/**companyId*/
	@Excel(name = "companyId", width = 15)
    @ApiModelProperty(value = "companyId")
	private Integer companyId;
	/**salesmanagerRowid*/
	@Excel(name = "salesmanagerRowid", width = 15)
    @ApiModelProperty(value = "salesmanagerRowid")
	private String salesmanagerRowid;
	/**introducername*/
	@Excel(name = "introducername", width = 15)
    @ApiModelProperty(value = "introducername")
	private String introducername;
	/**introducermember*/
	@Excel(name = "introducermember", width = 15)
    @ApiModelProperty(value = "introducermember")
	private String introducermember;
	/**introducermemberrowid*/
	@Excel(name = "introducermemberrowid", width = 15)
    @ApiModelProperty(value = "introducermemberrowid")
	private String introducermemberrowid;
	/**introducerphone*/
	@Excel(name = "introducerphone", width = 15)
    @ApiModelProperty(value = "introducerphone")
	private String introducerphone;
	/**introduceridentitycard*/
	@Excel(name = "introduceridentitycard", width = 15)
    @ApiModelProperty(value = "introduceridentitycard")
	private String introduceridentitycard;
	/**cityname*/
	@Excel(name = "cityname", width = 15)
    @ApiModelProperty(value = "cityname")
	private String cityname;
	/**districtname*/
	@Excel(name = "districtname", width = 15)
    @ApiModelProperty(value = "districtname")
	private String districtname;
	/**provicename*/
	@Excel(name = "provicename", width = 15)
    @ApiModelProperty(value = "provicename")
	private String provicename;
}
