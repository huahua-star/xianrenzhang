package org.jeecg.modules.zzj.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 凯莱酒店房间
 * @Author: jeecg-boot
 * @Date:   2019-10-25
 * @Version: V1.0
 */
@Data
@TableName("ROOM")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ROOM对象", description="凯莱酒店房间")
public class klroom {
    
	/**roomRowid*/
	@Excel(name = "roomRowid", width = 15)
    @ApiModelProperty(value = "roomRowid")
	private java.lang.Integer roomRowid;
	/**resort*/
	@Excel(name = "resort", width = 15)
    @ApiModelProperty(value = "resort")
	private java.lang.Integer resort;
	/**roomNum*/
	@Excel(name = "roomNum", width = 15)
    @ApiModelProperty(value = "roomNum")
	private java.lang.String roomNum;
	/**floor*/
	@Excel(name = "floor", width = 15)
    @ApiModelProperty(value = "floor")
	private java.lang.String floor;
	/**type*/
	@Excel(name = "type", width = 15)
    @ApiModelProperty(value = "type")
	private java.lang.String type;
	/**typeId*/
	@Excel(name = "typeId", width = 15)
    @ApiModelProperty(value = "typeId")
	private java.lang.Integer typeId;
	/**classId*/
	@Excel(name = "classId", width = 15)
    @ApiModelProperty(value = "classId")
	private java.lang.Integer classId;
	/**roomClass*/
	@Excel(name = "roomClass", width = 15)
    @ApiModelProperty(value = "roomClass")
	private java.lang.String roomClass;
	/**internalcode*/
	@Excel(name = "internalcode", width = 15)
    @ApiModelProperty(value = "internalcode")
	private java.lang.Integer internalcode;
	/**description*/
	@Excel(name = "description", width = 15)
    @ApiModelProperty(value = "description")
	private java.lang.String description;
	/**features*/
	@Excel(name = "features", width = 15)
    @ApiModelProperty(value = "features")
	private java.lang.String features;
	/**maxPersons*/
	@Excel(name = "maxPersons", width = 15)
    @ApiModelProperty(value = "maxPersons")
	private java.lang.String maxPersons;
	/**rateCode*/
	@Excel(name = "rateCode", width = 15)
    @ApiModelProperty(value = "rateCode")
	private java.lang.String rateCode;
	/**phone*/
	@Excel(name = "phone", width = 15)
    @ApiModelProperty(value = "phone")
	private java.lang.String phone;
	/**roomstatus*/
	@Excel(name = "roomstatus", width = 15)
    @ApiModelProperty(value = "roomstatus")
	private java.lang.String roomstatus;
	/**reservationstatus*/
	@Excel(name = "reservationstatus", width = 15)
    @ApiModelProperty(value = "reservationstatus")
	private java.lang.String reservationstatus;
	/**am*/
	@Excel(name = "am", width = 15)
    @ApiModelProperty(value = "am")
	private java.lang.String am;
	/**pm*/
	@Excel(name = "pm", width = 15)
    @ApiModelProperty(value = "pm")
	private java.lang.String pm;
	/**note*/
	@Excel(name = "note", width = 15)
    @ApiModelProperty(value = "note")
	private java.lang.String note;
	/**activeYn*/
	@Excel(name = "activeYn", width = 15)
    @ApiModelProperty(value = "activeYn")
	private java.lang.String activeYn;
	/**orderBy*/
	@Excel(name = "orderBy", width = 15)
    @ApiModelProperty(value = "orderBy")
	private java.lang.Integer orderBy;
	/**inactiveDate*/
	@Excel(name = "inactiveDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "inactiveDate")
	private java.util.Date inactiveDate;
	/**housereason*/
	@Excel(name = "housereason", width = 15)
    @ApiModelProperty(value = "housereason")
	private java.lang.String housereason;
	/**insertDate*/
	@Excel(name = "insertDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "insertDate")
	private java.util.Date insertDate;
	/**insertUser*/
	@Excel(name = "insertUser", width = 15)
    @ApiModelProperty(value = "insertUser")
	private java.lang.Integer insertUser;
	/**updateDate*/
	@Excel(name = "updateDate", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "updateDate")
	private java.util.Date updateDate;
	/**updateUser*/
	@Excel(name = "updateUser", width = 15)
    @ApiModelProperty(value = "updateUser")
	private java.lang.Integer updateUser;
	/**isPm*/
	@Excel(name = "isPm", width = 15)
    @ApiModelProperty(value = "isPm")
	private java.lang.String isPm;
	/**haveimg*/
	@Excel(name = "haveimg", width = 15)
    @ApiModelProperty(value = "haveimg")
	private java.lang.String haveimg;
	/**img*/
	@Excel(name = "img", width = 15)
    @ApiModelProperty(value = "img")
	private java.lang.Object img;
	/**insertUserName*/
	@Excel(name = "insertUserName", width = 15)
    @ApiModelProperty(value = "insertUserName")
	private java.lang.String insertUserName;
	/**updateUserName*/
	@Excel(name = "updateUserName", width = 15)
    @ApiModelProperty(value = "updateUserName")
	private java.lang.String updateUserName;
	/**rateAmount*/
	@Excel(name = "rateAmount", width = 15)
    @ApiModelProperty(value = "rateAmount")
	private java.lang.Integer rateAmount;
	/**maxBeds*/
	@Excel(name = "maxBeds", width = 15)
    @ApiModelProperty(value = "maxBeds")
	private java.lang.Integer maxBeds;
	/**connectRoom*/
	@Excel(name = "connectRoom", width = 15)
    @ApiModelProperty(value = "connectRoom")
	private java.lang.String connectRoom;
	/**remart*/
	@Excel(name = "remart", width = 15)
    @ApiModelProperty(value = "remart")
	private java.lang.String remart;
	/**realfostatus*/
	@Excel(name = "realfostatus", width = 15)
    @ApiModelProperty(value = "realfostatus")
	private java.lang.String realfostatus;
	/**realcustomers*/
	@Excel(name = "realcustomers", width = 15)
    @ApiModelProperty(value = "realcustomers")
	private java.lang.String realcustomers;
	/**nowresvrowid*/
	@Excel(name = "nowresvrowid", width = 15)
    @ApiModelProperty(value = "nowresvrowid")
	private java.lang.Integer nowresvrowid;
	/**housestatus*/
	@Excel(name = "housestatus", width = 15)
    @ApiModelProperty(value = "housestatus")
	private java.lang.String housestatus;
	/**autocheckout*/
	@Excel(name = "autocheckout", width = 15)
    @ApiModelProperty(value = "autocheckout")
	private java.lang.Integer autocheckout;
	/**cleaner*/
	@Excel(name = "cleaner", width = 15)
    @ApiModelProperty(value = "cleaner")
	private java.lang.String cleaner;
}
