package org.jeecg.modules.zzj.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFAnchor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.zzj.entity.*;
import org.jeecg.modules.zzj.service.*;
import org.jeecg.modules.zzj.util.Card.SetResultUtil;
import org.jeecg.modules.zzj.util.CheckMapUtil;
import org.jeecg.modules.zzj.util.EmailUtil;
import org.jeecg.modules.zzj.util.FilterListUtil;
import org.jeecg.modules.zzj.util.SendemailsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 凯莱对接 皆为调用对方给出的存储过程 （非接口对接）
 */

/**
 * @Description: 凯莱入住功能
 * @Author: jeecg-boot
 * @Date:   2019-10-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="凯莱入住功能")
@RestController
@RequestMapping("/zzj/KaiLaiCheckIn")
public class KaiLaiCheckInController {
    //凯莱可用房服务
    @Autowired
    private IKaiLaiRoomService iKaiLaiRoomService;
    //
    @Autowired
    private IKaiLaiOrderService iKaiLaiOrderService;

    @Autowired
    private IDueToSourceService iDueToSourceService;

    @Autowired
    private IPayMentService iPayMentService;

    @Autowired
    private IProFileService iProFileService;

    @Autowired
    private IPreAuthorizationService iPreAuthorizationService;

    @Value("${sdk.ComHandle}")
    private Integer comHandle;

    @Value("${cardUrl}")
    private String cardUrl;
    /**
     * 无预订单入住--查询可用房型及数量等信息
     * @param
     * @return
     */
    @AutoLog(value = "查询可用房型及数量等信息")
    @ApiOperation(value="查询可用房型及数量等信息-selectRoomInformation", notes="查询可用房型及数量等信息-selectRoomInformation")
    @GetMapping(value = "/selectRoomInformation")
    public Result<Object> selectRoomInformation(String beginTime, String endTime) throws Exception {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        String message="";
        if (!CheckMapUtil.checkMap(map)){
            message="缺少参数";
            SetResultUtil.setLackParamResult(result,message);
            return result;
        }
        // 查询凯莱的 可用房间 （暂时都为 全天房）
        System.out.println("查询可用房型中");
        List<KaiLaiRoom> list= iKaiLaiRoomService.SelectKaiLaiRoom(map);
        System.out.println("查询可用房结束");
        for(KaiLaiRoom kaiLaiRoom : list){
            //判断是否禁烟
            if (kaiLaiRoom.getFeatures()!=null&&kaiLaiRoom.getFeatures().contains("不吸烟")){
                kaiLaiRoom.setIsSmoke("0");
            }else{
                kaiLaiRoom.setIsSmoke("1");
            }
            //判断包价中是否包含早餐
            if (kaiLaiRoom.getPackages()!=null&&
                    kaiLaiRoom.getPackages().contains("BKF（P）28")){
                kaiLaiRoom.setIsBreakfast("1");
            }else{
                kaiLaiRoom.setIsBreakfast("0");
            }
        }
        String returnString= JSON.toJSONString(list);
        message="成功查询";
        SetResultUtil.setSuccessResult(result,message,returnString);
        return result;
    }

    /**
     * 生成订单
     * 返回true 为生成成功
     */
    @AutoLog(value = "无预订单入住--生成预订单")
    @ApiOperation(value="生成预订单-generateOrder", notes="生成预订单-generateOrder")
    @GetMapping(value = "/generateOrder")
    public Result<Object> generateOrder(String beginTime, String endTime, String number, String roomType
            , String phone,String channelCode,String name){
        Result<Object> result = new Result<Object>();
        String message="";
        Map<String,String> map=new HashMap<>();
        map.put("beginTime",beginTime);//预定入住时间
        map.put("endTime",endTime);//预定离店时间
        map.put("number",number);//预定房间数量
        map.put("roomType",roomType);//房型代码
        map.put("phone",phone);//手机号
        map.put("channelCode",channelCode);//订单来源
        map.put("name",name);//姓名
        if (!CheckMapUtil.checkMap(map)){
            message="缺少参数";
            SetResultUtil.setLackParamResult(result,message);
            return result;
        }
        int returnResult=iKaiLaiOrderService.generateOrder(map);
        if (returnResult==1){
            SetResultUtil.setSuccessResult(result);
        }else{
            message="创建预订单失败";
            SetResultUtil.setErrorMsgResult(result,message);
        }
        return result;
    }


    /**
     * 无预订单入住-- 返回订单详细信息
     * @param
     * @return
     */
    @AutoLog(value = "getOrderByPhone")
    @ApiOperation(value="手机号查询订单-getOrderByPhone", notes="手机号查询订单-getOrderByPhone")
    @GetMapping(value = "/getOrderByPhone")
    public Result<Object> getOrderByPhone(String phone) throws Exception {
        Result<Object> result = new Result<Object>();
        if(StringUtil.isNullOrEmpty(phone)){
            String message="手机号为空。";
            SetResultUtil.setNotFoundResult(result,message);
            return result;
        }
        List<KaiLaiOrder> list=iKaiLaiOrderService.getOrderByPhone(phone);
        List<KaiLaiOrder> resultList=new ArrayList<>();
        if (list.size()>0){
            for(KaiLaiOrder k : list){
                Date nowDate=new Date();
                Date checkInDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(k.getActualCheckInDate());
                Date checkOutDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(k.getActualCheckOutDate());
                if (checkInDate.before(nowDate)&&nowDate.before(checkOutDate)){
                    k.setIsFlag("1");
                }else{
                    k.setIsFlag("0");
                }
                KaiLaiRoom kaiLaiRoom=iKaiLaiRoomService.SelectRoomNameByType(k.getRoomType());
                k.setRoomName(kaiLaiRoom.getDescription());
                if (!k.getResvStatus().equalsIgnoreCase("CHECKOUT")&&!k.getResvStatus().equalsIgnoreCase("CANCEL")&&!k.getResvStatus().equalsIgnoreCase("NOSHOW")){
                    resultList.add(k);
                }
                resultList=FilterListUtil.fiterCheckStateChinese(resultList);
            }
            String data=JSON.toJSONString(resultList);
            SetResultUtil.setSuccessResult(result,"成功查询",data);
        }else{
            String message="无该手机号的预定信息";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }

    /**
     * 根据订单号查询订单信息
     * @param
     * @return
     */
    @AutoLog(value = "根据订单号查询订单信息")
    @ApiOperation(value="根据订单号查询订单信息-getOrderByOrderId", notes="根据订单号查询订单信息-getOrderByOrderId")
    @GetMapping(value = "/getOrderByOrderId")
    public Result<Object> getOrderByOrderId(String orderId) throws Exception {
        Result<Object> result = new Result<Object>();
        if(StringUtil.isNullOrEmpty(orderId)){
            String message="订单号为空。";
            SetResultUtil.setNotFoundResult(result,message);
            return result;
        }
        //查询PMS订单
        KaiLaiOrder kaiLaiOrder=iKaiLaiOrderService.getOrderByOrderId(orderId);
        //客户真正需要的订单
        KaiLaiOrder saveOder=null;
        //查询CRS订单
        KaiLaiOrder kaiLaiOrderCRS=iKaiLaiOrderService.getOrderByCRSId(orderId);
        if (null!=kaiLaiOrder && (kaiLaiOrder.getResvStatus().equals("DUEIN")||
                kaiLaiOrder.getResvStatus().equals("RESERVED")||
                kaiLaiOrder.getResvStatus().equals("CHECKIN")||kaiLaiOrder.getResvStatus().equals("DUEOUT"))){
            saveOder=kaiLaiOrder;
        }
        if (null!=kaiLaiOrderCRS && (kaiLaiOrderCRS.getResvStatus().equals("DUEIN")||kaiLaiOrderCRS.getResvStatus().equals("RESERVED")||
                kaiLaiOrderCRS.getResvStatus().equals("CHECKIN")||kaiLaiOrderCRS.getResvStatus().equals("DUEOUT"))){
            saveOder=kaiLaiOrderCRS;
        }
        if (null!=saveOder){
            KaiLaiOrder kSelect=iKaiLaiOrderService.getOne(new QueryWrapper<KaiLaiOrder>().eq("is_accompany","0").eq("resv_name_id",saveOder.getResvNameId()));
            System.out.println(kSelect);
            if(null==kSelect){
                iKaiLaiOrderService.save(saveOder);
            }
            List<KaiLaiOrder> list=new ArrayList<>();
            list.add(saveOder);
            List<KaiLaiOrder> resultList=new ArrayList<>();
            for(KaiLaiOrder k : list){
                Date nowDate=new Date();
                Date checkInDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(k.getActualCheckInDate());
                Date checkOutDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(k.getActualCheckOutDate());
                if (checkInDate.before(nowDate)&&nowDate.before(checkOutDate)){
                    k.setIsFlag("1");
                }else{
                    k.setIsFlag("0");
                }
                KaiLaiRoom kaiLaiRoom=iKaiLaiRoomService.SelectRoomNameByType(k.getRoomType());
                k.setRoomName(kaiLaiRoom.getDescription());
                if (!k.getResvStatus().equalsIgnoreCase("CHECKOUT")&&
                        !k.getResvStatus().equalsIgnoreCase("CANCEL")&&
                        !k.getResvStatus().equalsIgnoreCase("NOSHOW")
                ){
                    resultList.add(k);
                }
            }
            if (resultList.size()>0){
                System.out.println(saveOder);
                SetResultUtil.setSuccessResult(result,"成功查询",JSON.toJSONString(resultList));
            }else{
                String message="无该订单号";
                SetResultUtil.setNotFoundResult(result,message);
            }
        }else{
            String message="无该订单号";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }

    /**
     * 无预订单入住-- 根据身份证好查询订单信息
     * @param
     * @return
     */
    @AutoLog(value = "根据身份证号查询订单信息")
    @ApiOperation(value="身份证号查询-searchResvByIdcard", notes="身份证号查询-searchResvByIdcard")
    @GetMapping(value = "/searchResvByIdcard")
    public Result<Object> searchResvByIdcard(String idNumber) throws Exception {
        Result<Object> result = new Result<Object>();
        if(StringUtil.isNullOrEmpty(idNumber)){
            String message="身份证号为空。";
            SetResultUtil.setNotFoundResult(result,message);
            return result;
        }
        List<KaiLaiOrder> list=iKaiLaiOrderService.searchResvByIdcard(idNumber);
        List<KaiLaiOrder> resultList=new ArrayList<>();
        if (list.size()>0){
            for(KaiLaiOrder k : list){
                Date nowDate=new Date();
                Date checkInDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(k.getActualCheckInDate());
                Date checkOutDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(k.getActualCheckOutDate());
                if (checkInDate.before(nowDate)&&nowDate.before(checkOutDate)){
                    k.setIsFlag("1");
                }else{
                    k.setIsFlag("0");
                }
                KaiLaiRoom kaiLaiRoom=iKaiLaiRoomService.SelectRoomNameByType(k.getRoomType());
                k.setRoomName(kaiLaiRoom.getDescription());
                if (!k.getResvStatus().equalsIgnoreCase("CHECKOUT")&&!k.getResvStatus().equalsIgnoreCase("CANCEL")&&!k.getResvStatus().equalsIgnoreCase("NOSHOW")){
                    resultList.add(k);
                }
            }
            if (resultList.size()>0){
                String data=JSON.toJSONString(resultList);
                SetResultUtil.setSuccessResult(result,"成功查询",data);
            }else {
                String message="无该身份证号的预定信息";
                SetResultUtil.setNotFoundResult(result,message);
            }
        }else{
            String message="无该身份证号的预定信息";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }

    /**
     * 根据订单号修改预订单为已入住状态
     * @param
     * @return
     */
    @AutoLog(value = "根据订单号--修改状态为入住")
    @ApiOperation(value="根据订单号修改状态为入住-checkInOrder", notes="根据订单号修改状态为入住-checkInOrder")
    @GetMapping(value = "/checkInOrder")
    public Result<Object> checkInOrder(String orderId) throws Exception {
        Result<Object> result = new Result<Object>();
        KaiLaiOrder kaiLaiOrder=iKaiLaiOrderService.getOrderByOrderId(orderId);
        //判断该房间是否绑定了 入住人
        if(StringUtil.isNullOrEmpty(kaiLaiOrder.getIdEntityCard())){
            SetResultUtil.setErrorMsgResult(result,"该房间还未绑定入住人信息");
            return result;
        }
        int returnKey=iKaiLaiOrderService.checkInOrder(orderId);
        if(returnKey==1)
        {
            KaiLaiOrder kaiLaiOrderChcekIn=iKaiLaiOrderService.getOne(new QueryWrapper<KaiLaiOrder>().eq("resv_name_id",orderId));
            kaiLaiOrderChcekIn.setResvStatus("CHECKIN");
            iKaiLaiOrderService.updateById(kaiLaiOrderChcekIn);
            SetResultUtil.setSuccessResult(result,"修改订单状态为入住成功");
        }else{
            String message="修改订单状态为入住失败";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }
    /**
     * 根据订单号修改预订单为已退房状态
     * @param
     * @return
     */
    @AutoLog(value = "根据订单号--修改状态为退房")
    @ApiOperation(value="根据订单号修改状态为退房-checkOutOrder", notes="根据订单号修改状态为退房-checkOutOrder")
    @GetMapping(value = "/checkOutOrder")
    public Result<Object> checkOutOrder(String orderId) throws Exception {
        Result<Object> result = new Result<Object>();
        int returnKey=iKaiLaiOrderService.checkOutOrder(orderId);
        if(returnKey==1)
        {
            KaiLaiOrder kaiLaiOrder=iKaiLaiOrderService.getOrderByOrderId(orderId);
            kaiLaiOrder.setResvStatus("checkout");
            iKaiLaiOrderService.updateById(kaiLaiOrder);
            SetResultUtil.setSuccessResult(result,"修改订单状态为退房状态成功");
        }else{
            String message="修改订单状态为退房状态失败";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }

    /**
     * 根据订单号 修改预订单信息  添加入住人
     * @param
     * @return
     */
    @AutoLog(value = "根据订单号--添加入住人")
    @ApiOperation(value="根据订单号添加入住人-updateProfileByRid", notes="根据订单号添加入住人-updateProfileByRid")
    @GetMapping(value = "/updateProfileByRid")
    public Result<Object> updateProfileByRid(String orderId, String lastName, String firstName, String name,
                                             String gender, String borthDay, String nation, String address,
                                             String idNumber,String certificateType,String nationality,String phone,String visadate) throws Exception {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("orderId",orderId);
        map.put("lastName",lastName);
        map.put("firstName",firstName);
        map.put("name",name);
        map.put("gender",gender);
        map.put("borthDay",borthDay);
        map.put("nation",nation);
        map.put("address",address);
        map.put("idNumber",idNumber);
        map.put("certificateType",certificateType);
        map.put("nationality",nationality);
        map.put("visadate",visadate);
        map.put("phone",phone);
        //修改凯莱订单
        int returnKey=iKaiLaiOrderService.updateProfileByRid(map);
        if(returnKey==1)
        {
            //修改本地订单 添加 入住人
            KaiLaiOrder kSelect=iKaiLaiOrderService.getOne(new QueryWrapper<KaiLaiOrder>().eq("is_accompany","0").eq("resv_name_id",orderId));
            kSelect.setLastName(lastName);
            kSelect.setFirstName(firstName);
            kSelect.setAltName(name);
            kSelect.setGender(gender);
            kSelect.setNation(nation);
            kSelect.setAddress(address);
            kSelect.setBorthday(borthDay);
            kSelect.setIdEntityCard(idNumber);
            kSelect.setCertificateType(certificateType);
            kSelect.setNationality(nationality);
            kSelect.setPhone(phone);
            kSelect.setVisadate(visadate);
            iKaiLaiOrderService.updateById(kSelect);
            SetResultUtil.setSuccessResult(result,"修改订单信息-添加入住人成功");
        }else{
            String message="修改订单信息-添加入住人失败";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }
    /**
     * 根据订单号 修改预订单信息  添加同住人
     * @param
     * @return
     */
    @AutoLog(value = "根据订单号--添加同住人")
    @ApiOperation(value="根据订单号添加同住人-CreateAccompany", notes="根据订单号添加同住人-CreateAccompany")
    @GetMapping(value = "/CreateAccompany")
    public Result<Object> CreateAccompany(String orderId, String lastName, String firstName, String name,
                                             String gender, String borthDay, String nation, String address,
                                          String idNumber,String certificateType,String nationality,String visadate) throws Exception {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("orderId",orderId);
        map.put("lastName",lastName);
        map.put("firstName",firstName);
        map.put("name",name);
        map.put("gender",gender);
        map.put("borthDay",borthDay);
        map.put("nation",nation);
        map.put("address",address);
        map.put("idNumber",idNumber);
        map.put("certificateType",certificateType);
        map.put("nationality",nationality);
        map.put("visadate",visadate);
        KaiLaiOrder kaiLaiOrder=iKaiLaiOrderService.getOrderByOrderId(orderId);
        if (kaiLaiOrder!=null){
            if (kaiLaiOrder.getIdEntityCard().equalsIgnoreCase(idNumber)){
                return SetResultUtil.setNotFoundResult(result,"同住人不能和入住人是同一个人。");
            }
        }
        //修改凯莱订单 添加同住人
        int returnKey=iKaiLaiOrderService.createAccompany(map);
        if(returnKey==1)
        {
            //本地新建同住人订单
            //查询入住人订单
            KaiLaiOrder kSelect=iKaiLaiOrderService.getOne(new QueryWrapper<KaiLaiOrder>().eq("is_accompany","0").eq("resv_name_id",orderId));
            //生成新 同住人订单
            KaiLaiOrder newCompanyOrder=kSelect;
            newCompanyOrder.setIsAccompany("1");//1 表示 同住人订单
            //重新赋值  居住人信息 为 同住人的信息
            //设置id为空
            newCompanyOrder.setId(null);
            newCompanyOrder.setLastName(lastName);
            newCompanyOrder.setFirstName(firstName);
            newCompanyOrder.setAltName(name);
            newCompanyOrder.setGender(gender);
            newCompanyOrder.setNation(nation);
            newCompanyOrder.setAddress(address);
            newCompanyOrder.setBorthday(borthDay);
            newCompanyOrder.setIdEntityCard(idNumber);
            newCompanyOrder.setCertificateType(certificateType);
            newCompanyOrder.setNationality(nationality);
            newCompanyOrder.setVisadate(visadate);
            iKaiLaiOrderService.save(newCompanyOrder);
            SetResultUtil.setSuccessResult(result,"修改订单信息-添加同住人成功");
        }else{
            String message="修改订单信息-添加同住人失败";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }

    /**
     * 根据房间号 查询 订单信息 用于续住
     * @param
     * @return
     */
    @AutoLog(value = "续房--根据房间号查询订单信息")
    @ApiOperation(value="续房房间号查询订单-searchByRoomNo", notes="续房房间号查询订单-searchByRoomNo")
    @GetMapping(value = "/searchByRoomNo")
    public Result<Object> searchByRoomNo(String roomNo) throws Exception {
        Result<Object> result = new Result<Object>();
        List<KaiLaiOrder> list=iKaiLaiOrderService.searchByRoomNo(roomNo);
        if (list.size()>0){
            List<String> idList=new ArrayList<>();
            for(KaiLaiOrder k : list){
                KaiLaiRoom kaiLaiRoom=iKaiLaiRoomService.SelectRoomNameByType(k.getRoomType());
                k.setRoomName(kaiLaiRoom.getDescription());
                //返回同住人身份证信息
                KaiLaiOrder kaiLaiOrder=iKaiLaiOrderService.getOrderByOrderId(k.getResvNameId());
                k.setAccompanyId(kaiLaiOrder.getAccompanyId());
            }
            String data=JSON.toJSONString(list);
            SetResultUtil.setSuccessResult(result,"成功查询",data);
        }else{
            String message="无该房号的订单信息，无法续住";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }



    /**
     * 根据订单号 离店时间 续住
     * @param
     * @return
     */
    @AutoLog(value = "根据订单号--续住")
    @ApiOperation(value="根据订单号续住-goonCheckIn", notes="根据订单号续住-goonCheckIn")
    @GetMapping(value = "/goonCheckIn")
    public Result<Object> goonCheckIn(String orderId,int dayNum) throws Exception {
        Result<Object> result = new Result<Object>();
        KaiLaiOrder kaiLaiOrderT=iKaiLaiOrderService.getOrderByOrderId(orderId);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(kaiLaiOrderT.getActualCheckOutDate()));
        calendar.add(Calendar.DAY_OF_MONTH,dayNum);
        String endDate=new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        System.out.println("endDate:"+endDate);
        Map<String,String> map=new HashMap<>();
        map.put("orderId",orderId);
        map.put("endDate",endDate);
        int returnKey=iKaiLaiOrderService.goonCheckIn(map);
        if(returnKey==1)
        {
            KaiLaiOrder kaiLaiOrder=iKaiLaiOrderService.getOrderByOrderId(orderId);
            List<KaiLaiOrder> list=new ArrayList<>();
            list.add(kaiLaiOrder);
            SetResultUtil.setSuccessResult(result,"续房成功",JSON.toJSONString(list));
        }else{
            String message="续房失败";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }
    /**
     * 根据订单号 离店时间 判断是否能续房
     * @param
     * @return
     */
    @AutoLog(value = "根据订单号--判断是否能续房")
    @ApiOperation(value="根据订单号判断是否能续住-isCanGoonCheckIn", notes="根据订单号判断是否能续住-isCanGoonCheckIn")
    @GetMapping(value = "/isCanGoonCheckIn")
    public Result<Object> isCanGoonCheckIn(String orderId,int dayNum) throws Exception {
        Result<Object> result = new Result<Object>();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,dayNum);
        String endDate=new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        Map<String,String> map=new HashMap<>();
        map.put("orderId",orderId);
        map.put("endDate",endDate);
        int returnKey=iKaiLaiOrderService.isCanGoonCheckIn(map);
        if(returnKey==1)
        {
            KaiLaiOrder kaiLaiOrder=iKaiLaiOrderService.getOrderByOrderId(orderId);
            List<KaiLaiOrder> list=new ArrayList<>();
            list.add(kaiLaiOrder);
            SetResultUtil.setSuccessResult(result,"可以续房",JSON.toJSONString(list));
        }else{
            String message="不能续房";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }
    /**
     * 查询订单来源
     * @param
     * @return
     */
    @AutoLog(value = "查询订单来源")
    @ApiOperation(value="查询订单来源", notes="查询订单来源")
    @GetMapping(value = "/searchSourceCode")
    public Result<Object> searchSourceCode() throws Exception {
        Result<Object> result = new Result<Object>();
        List<DueToSource> list=iDueToSourceService.searchSourceCode();
        SetResultUtil.setSuccessResult(result,"成功查询。",JSON.toJSONString(list));
        return result;
    }

    /**
     * 根据姓名和预定渠道 查询订单信息
     * @param
     * @return
     */
    @AutoLog(value = "根据姓名和预定渠道 查询订单信息")
    @ApiOperation(value="根据姓名和预定渠道 查询订单信息", notes="根据姓名和预定渠道 查询订单信息")
    @GetMapping(value = "/searchResvByName")
    public Result<Object> searchResvByName(String name,String code) throws Exception {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("name",name);
        if(!CheckMapUtil.checkMap(map)){
            String message="参数不全。";
            SetResultUtil.setNotFoundResult(result,message);
            return result;
        }
        map.put("code",code);
        //1.姓名查询
        List<KaiLaiOrder> list=iKaiLaiOrderService.searchResvByName(map);
        List<KaiLaiOrder> resultList= FilterListUtil.filterList(list);
        if (resultList.size()>0){
            for(KaiLaiOrder k : list){
                KaiLaiRoom kaiLaiRoom=iKaiLaiRoomService.SelectRoomNameByType(k.getRoomType());
                k.setRoomName(kaiLaiRoom.getDescription());
            }
            resultList=FilterListUtil.fiterCheckStateChinese(resultList);
            return SetResultUtil.setSuccessResult(result,"姓名查询成功",JSON.toJSONString(resultList));
        }else{
            if (name.length()>2){
                //2.根据 大写拼音 姓 + 名 查询
                String lastNameBigPinYin= SendemailsUtils.getPinYin(name.substring(0,1)).toUpperCase();
                String firstNameBigPinYin=SendemailsUtils.getPinYin(name.substring(1,name.length())).toUpperCase();
                Map<String,String> bigPinYinXingAndMingMap=new HashMap<>();
                bigPinYinXingAndMingMap.put("lastName",lastNameBigPinYin);
                bigPinYinXingAndMingMap.put("firstName",firstNameBigPinYin);
                list=iKaiLaiOrderService.searchResvByLFName(bigPinYinXingAndMingMap);
                resultList=FilterListUtil.filterList(list);
                if (resultList.size()>0){
                    for(KaiLaiOrder k : list){
                        KaiLaiRoom kaiLaiRoom=iKaiLaiRoomService.SelectRoomNameByType(k.getRoomType());
                        k.setRoomName(kaiLaiRoom.getDescription());
                    }
                    resultList=FilterListUtil.fiterCheckStateChinese(resultList);
                    return SetResultUtil.setSuccessResult(result,"姓名查询成功",JSON.toJSONString(resultList));
                }else{
                    //3.根据 大写拼音姓名  查询
                    map.put("name",SendemailsUtils.getPinYin(name).toUpperCase());
                    list=iKaiLaiOrderService.searchResvByName(map);
                    resultList=FilterListUtil.filterList(list);
                    if (resultList.size()>0){
                        for(KaiLaiOrder k : list){
                            KaiLaiRoom kaiLaiRoom=iKaiLaiRoomService.SelectRoomNameByType(k.getRoomType());
                            k.setRoomName(kaiLaiRoom.getDescription());
                        }
                        resultList=FilterListUtil.fiterCheckStateChinese(resultList);
                        return SetResultUtil.setSuccessResult(result,"姓名查询成功",JSON.toJSONString(resultList));
                    }else{
                        //4.根据 汉字 姓+名 查询
                        String lastName= name.substring(0,1);
                        String firstName=name.substring(1,name.length());
                        Map<String,String> XingAndMingMap=new HashMap<>();
                        XingAndMingMap.put("lastName",lastName);
                        XingAndMingMap.put("firstName",firstName);
                        list=iKaiLaiOrderService.searchResvByLFName(XingAndMingMap);
                        resultList=FilterListUtil.filterList(list);
                        if (resultList.size()>0){
                            for(KaiLaiOrder k : list){
                                KaiLaiRoom kaiLaiRoom=iKaiLaiRoomService.SelectRoomNameByType(k.getRoomType());
                                k.setRoomName(kaiLaiRoom.getDescription());
                            }
                            resultList=FilterListUtil.fiterCheckStateChinese(resultList);
                            return SetResultUtil.setSuccessResult(result,"姓名查询成功",JSON.toJSONString(resultList));
                        }else{
                            //5.小写 拼音 姓+名 查询
                            String lastNameSmallPinYin= SendemailsUtils.getPinYin(name.substring(0,1));
                            String firstNameSmallPinYin=SendemailsUtils.getPinYin(name.substring(1,name.length()));
                            Map<String,String> SmallPinYinXingAndMingMap=new HashMap<>();
                            SmallPinYinXingAndMingMap.put("lastName",lastNameSmallPinYin);
                            SmallPinYinXingAndMingMap.put("firstName",firstNameSmallPinYin);
                            list=iKaiLaiOrderService.searchResvByLFName(SmallPinYinXingAndMingMap);
                            resultList=FilterListUtil.filterList(list);
                            if (resultList.size()>0){
                                for(KaiLaiOrder k : list){
                                    KaiLaiRoom kaiLaiRoom=iKaiLaiRoomService.SelectRoomNameByType(k.getRoomType());
                                    k.setRoomName(kaiLaiRoom.getDescription());
                                }
                                resultList=FilterListUtil.fiterCheckStateChinese(resultList);
                                return SetResultUtil.setSuccessResult(result,"姓名查询成功",JSON.toJSONString(resultList));
                            }else{
                                //6.小写 拼音姓名查询
                                map.put("name",SendemailsUtils.getPinYin(name));
                                list=iKaiLaiOrderService.searchResvByName(map);
                                resultList=FilterListUtil.filterList(list);
                                if (resultList.size()>0){
                                    for(KaiLaiOrder k : list){
                                        KaiLaiRoom kaiLaiRoom=iKaiLaiRoomService.SelectRoomNameByType(k.getRoomType());
                                        k.setRoomName(kaiLaiRoom.getDescription());
                                    }
                                    resultList=FilterListUtil.fiterCheckStateChinese(resultList);
                                    return SetResultUtil.setSuccessResult(result,"姓名查询成功",JSON.toJSONString(resultList));
                                }else{
                                    String message="无该姓名预定";
                                    SetResultUtil.setNotFoundResult(result,message);
                                }
                            }
                        }
                    }
                }
            }else{
                String message="无该姓名预定";
                SetResultUtil.setNotFoundResult(result,message);
            }
        }
        return result;
    }

    /**
     * 根据姓名和预定渠道 查询订单信息
     * @param
     * @return
     */
    @AutoLog(value = "根据订单号查询该订单的支付状态")
    @ApiOperation(value="根据订单号查询该订单的支付状态", notes="根据订单号查询该订单的支付状态")
    @GetMapping(value = "/searchStateResvByRnsvId")
    public Result<Object> searchStateResvByRnsvId(String orderId) throws Exception {
        Result<Object> result = new Result<Object>();
        if(StringUtil.isNullOrEmpty(orderId)){
            String message="订单号为空。";
            SetResultUtil.setNotFoundResult(result,message);
            return result;
        }
        KaiLaiOrder kaiLaiOrder=iKaiLaiOrderService.getOrderByOrderId(orderId);
       String paymentMethod=kaiLaiOrder.getPaymentMethod();
       if (StringUtil.isNullOrEmpty(paymentMethod)){//0未 预付 1 已预付
           SetResultUtil.setSuccessResult(result,"该订单未预付。","0");
       }else{
           SetResultUtil.setSuccessResult(result,"该订单已预付。","1");
        }
        return result;
    }

    /**
     * 根据姓名和预定渠道 查询订单信息
     * @param
     * @return
     */
    @AutoLog(value = "根据订单号修改客户个人档案profile")
    @ApiOperation(value="根据订单号修改客户个人档案profile", notes="根据订单号修改客户个人档案profile")
    @GetMapping(value = "/UpdateProfileByNameId")
    public Result<Object> UpdateProfileByNameId(String orderId,String phone) throws Exception {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("orderId",orderId);
        map.put("phone",phone);
        if(!CheckMapUtil.checkMap(map)){
            String message="参数不全。";
            SetResultUtil.setNotFoundResult(result,message);
            return result;
        }
        KaiLaiOrder kaiLaiOrder=iKaiLaiOrderService.getOrderByOrderId(orderId);
        if (null== kaiLaiOrder){
            SetResultUtil.setErrorMsgResult(result,"该订单不存在。");
            return result;
        }
        String nameId=kaiLaiOrder.getNameId();
        Map<String,String> profileMap=new HashMap<>();
        profileMap.put("nameId",nameId);
        ProFile proFile=iProFileService.searchProfileByNameID(profileMap);
        System.out.println(proFile);
        map.put("nameId",nameId);
        map.put("lastName",proFile.getLastName());
        map.put("firstName",proFile.getFirstName());
        map.put("sname",proFile.getName());
        map.put("gender",proFile.getGender());
        map.put("birthDay",proFile.getBirthDay().substring(0,10));
        map.put("nation","汉");//待修改
        map.put("address",proFile.getAddress());
        map.put("idNumber",proFile.getIdNumber());
        map.put("identityType",proFile.getIdType());
        map.put("visadate",proFile.getVisadate().substring(0,10));
        map.put("phone",phone);
        int returnKey=iKaiLaiOrderService.updateProfileByNameId(map);
        if (1==returnKey){
            kaiLaiOrder.setPhone(phone);
            iKaiLaiOrderService.updateById(kaiLaiOrder);
            SetResultUtil.setSuccessResult(result,"修改客户信息成功。",JSON.toJSONString(kaiLaiOrder));
        }else{
            SetResultUtil.setErrorMsgResult(result,"修改客户信息失败");
        }
        return result;
    }

    /**
     * 查所有支付方式
     * @param
     * @return
     */
    @AutoLog(value = "查所有支付方式")
    @ApiOperation(value="查所有支付方式", notes="查所有支付方式")
    @GetMapping(value = "/SearchPayment")
    public Result<Object> SearchPayment() throws Exception {
        Result<Object> result = new Result<Object>();
        List<PayMent> list=iPayMentService.searchPayment();
        SetResultUtil.setSuccessResult(result,"成功查询所有支付方式",JSON.toJSONString(list));
        return result;
    }

    /**
     * 修改订单的支付方式
     * @param
     * @return
     */
    @AutoLog(value = "修改订单的支付方式")
    @ApiOperation(value="修改订单的支付方式", notes="修改订单的支付方式")
    @GetMapping(value = "/updateResvPayment")
    public Result<Object> updateResvPayment(String orderId,String payMethod) throws Exception {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("orderId",orderId);
        map.put("payMethod",payMethod);
        if(!CheckMapUtil.checkMap(map)){
            String message="参数不全。";
            SetResultUtil.setNotFoundResult(result,message);
            return result;
        }
        int returnKey=iKaiLaiOrderService.updateResvPayment(map);
        if (1==returnKey){
            KaiLaiOrder kSelect=iKaiLaiOrderService.getOne(new QueryWrapper<KaiLaiOrder>().eq("is_accompany","0").eq("resv_name_id",orderId));
            kSelect.setPaymentMethod(payMethod);
            iKaiLaiOrderService.updateById(kSelect);
            SetResultUtil.setSuccessResult(result,"修改订单的支付方式成功。");
        }else{
            SetResultUtil.setErrorMsgResult(result,"修改订单的支付方式失败");
        }
        return result;
    }

    /**
     * 根据nameId查询 客户profile
     * @param
     * @return
     */
    @AutoLog(value = "根据nameId查询 客户profile")
    @ApiOperation(value="根据nameId查询 客户profile", notes="根据nameId查询 客户profile")
    @GetMapping(value = "/getProfileByNameId")
    public Result<Object> getProfileByNameId(String nameId) throws Exception {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("nameId",nameId);
        if(!CheckMapUtil.checkMap(map)){
            String message="参数不全。";
            SetResultUtil.setNotFoundResult(result,message);
            return result;
        }
        ProFile proFile=iProFileService.searchProfileByNameID(map);
        if (null!=proFile){
            SetResultUtil.setSuccessResult(result,"查询profile成功。", JSON.toJSONString(proFile));
        }else{
            SetResultUtil.setErrorMsgResult(result,"查询profile失败");
        }
        return result;
    }

    /**
     * 根据type 类型 查询房间名称
     * @param
     * @return
     */
    @AutoLog(value = "根据type 类型 查询房间名称")
    @ApiOperation(value="根据type 类型 查询房间名称", notes="根据type 类型 查询房间名称")
    @GetMapping(value = "/SelectRoomNameByType")
    public Result<Object> SelectRoomNameByType(String type) throws Exception {
        Result<Object> result = new Result<Object>();
        KaiLaiRoom kaiLaiRoom=iKaiLaiRoomService.SelectRoomNameByType(type);
        SetResultUtil.setSuccessResult(result,"查询成功",kaiLaiRoom);
        return result;
    }

    /**
     * 根据orderid 修改订单备注
     * @param
     * @return
     */
    @AutoLog(value = "根据orderid 修改订单备注")
    @ApiOperation(value="根据orderid 修改订单备注", notes="根据orderid 修改订单备注")
    @GetMapping(value = "/updateResvMessage")
    public Result<Object> updateResvMessage(String orderId,String message) throws Exception {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("orderId",orderId);
        map.put("message",message);
        if(!CheckMapUtil.checkMap(map)){
            String messages="参数不全。";
            SetResultUtil.setNotFoundResult(result,messages);
            return result;
        }
        int returnKey=iKaiLaiOrderService.updateResvMessage(map);
        if (1==returnKey){
            KaiLaiOrder kSelect=iKaiLaiOrderService.getOne(new QueryWrapper<KaiLaiOrder>().eq("is_accompany","0").eq("resv_name_id",orderId));
            kSelect.setMessage(message);
            iKaiLaiOrderService.updateById(kSelect);
            SetResultUtil.setSuccessResult(result,"修改订单的备注成功。");
        }else{
            SetResultUtil.setErrorMsgResult(result,"修改订单的备注失败");
        }
        return result;
    }

    /**
     * 查询包价
     */
    @AutoLog(value = "查询包价")
    @ApiOperation(value="查询包价", notes="查询包价")
    @GetMapping(value = "/helpSearch")
    public Result<Object> helpSearch(String beginTime, String endTime) throws Exception {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        String message="";
        if (CheckMapUtil.checkMap(map)){
            message="缺少参数";
            SetResultUtil.setLackParamResult(result,message);
        }
        // 查询凯莱的 可用房间 （暂时都为 全天房）
        List<KaiLaiRoom> list= iKaiLaiRoomService.HelpSearch(map);
        if (list.size()>0){
            String returnString= JSON.toJSONString(list);
            message="成功查询";
            SetResultUtil.setSuccessResult(result,message,returnString);
        }else{
            SetResultUtil.setErrorMsgResult(result,"数据异常");
        }
        return result;
    }

    /**
     * 查询包价
     */
    @AutoLog(value = "查询total--预授权金额")
    @ApiOperation(value="查询total--预授权金额", notes="查询total--预授权金额")
    @GetMapping(value = "/searchTotalAmount")
    public Result<Object> searchTotalAmount(String orderId) throws Exception {
        Result<Object> result = new Result<Object>();
        if (StringUtil.isNullOrEmpty(orderId)){
            SetResultUtil.setLackParamResult(result,"参数不全");
        }
        String total=iKaiLaiOrderService.searchTotalAmount(orderId);
        if (null!=total){
            SetResultUtil.setSuccessResult(result,"查询成功",total);
        }else{
            SetResultUtil.setErrorMsgResult(result,"查询失败");
        }
        return result;
    }

    /**
     * 根据团队订单号查询订单信息
     */
    @AutoLog(value = "根据团队订单号查询订单信息")
    @ApiOperation(value="根据团队订单号查询订单信息", notes="根据团队订单号查询订单信息")
    @GetMapping(value = "/searchResvBygroupid")
    public Result<Object> searchResvBygroupid(String groupId) throws Exception {
        Result<Object> result = new Result<Object>();
        if (StringUtil.isNullOrEmpty(groupId)){
            SetResultUtil.setLackParamResult(result,"参数不全");
        }
        List<KaiLaiOrder> list=iKaiLaiOrderService.searchResvByBlockByID(groupId);
        if (list.size()>0){
            SetResultUtil.setSuccessResult(result,"查询团队订单成功",JSON.toJSONString(list));
        }else{
            SetResultUtil.setErrorMsgResult(result,"查询失败");
        }
        return result;
    }

    /**
     * 生成团队预订单
     * 返回true 为生成成功
     */
    @AutoLog(value = "生成团队预订单")
    @ApiOperation(value="generateOrderBlockID", notes="生成团队预订单-generateOrderBlockID")
    @PostMapping(value = "/generateOrderBlockID",consumes = "application/json")
    public Result<Object> generateOrderBlockID(@RequestBody KaiLaiOrderList teamList){
        System.out.println(teamList.getKaiLaiOrderList());
        Result<Object> result = new Result<Object>();
        String message="";
        for (KaiLaiOrder kaiLaiOrder : teamList.getKaiLaiOrderList()){
            Map<String,String> map=new HashMap<>();
            map.put("beginTime",kaiLaiOrder.getActualCheckInDate());//预定入住时间
            map.put("endTime",kaiLaiOrder.getActualCheckOutDate());//预定离店时间
            map.put("number","1");//预定房间数量
            map.put("roomType",kaiLaiOrder.getRoomType());//房型代码
            map.put("phone",kaiLaiOrder.getPhone());//手机号
            map.put("channelCode",kaiLaiOrder.getChanelCode());//订单来源
            map.put("blockId",kaiLaiOrder.getBlockId());//团队id
            int returnResult=iKaiLaiOrderService.createResvAndBlockID(map);
            if (returnResult==1){
                kaiLaiOrder.setIsFlag("1");// 1成功
            }else{
                kaiLaiOrder.setIsFlag("0");//0失败
            }
        }
        boolean flag=true;
        for (KaiLaiOrder kaiLaiOrder : teamList.getKaiLaiOrderList()){
            if ("0".equals(kaiLaiOrder.getIsFlag())){
                flag=false;
                break;
            }
        }
        if (flag){
            SetResultUtil.setSuccessResult(result,message,JSON.toJSONString(teamList));
        }else{
            SetResultUtil.setErrorMsgResult(result,message,JSON.toJSONString(teamList));
        }
        return result;
    }
    /**
     * 根据excel 生成团队预订单
     * 返回true 为生成成功
     */
    @AutoLog(value = "根据excel 生成团队预订单")
    @ApiOperation(value="generateOrderBlockIDByExcel", notes="生成团队预订单-generateOrderBlockIDByExcel")
    @GetMapping(value = "/generateOrderBlockIDByExcel")
    public Result<Object> generateOrderBlockIDByExcel(String file) throws Exception {
        Result<Object> result = new Result<Object>();
        String message="";
        List<KaiLaiOrder> list=new ArrayList<>();
        String excelPath=file;
        File excel = new File(excelPath);
        if (excel.isFile() && excel.exists()) {   //判断文件是否存在
            String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
            Workbook wb;
            //根据文件后缀（xls/xlsx）进行判断
            if ( "xls".equals(split[1])){
                FileInputStream fis = new FileInputStream(excel);   //文件流对象
                wb = new HSSFWorkbook(fis);
            }else if ("xlsx".equals(split[1])){
                wb = new XSSFWorkbook(excel);
            }else {
                System.out.println("文件类型错误!");
                return SetResultUtil.setErrorMsgResult(result,"文件类型错误");
            }
            //开始解析
            Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

            int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
            int lastRowIndex = sheet.getLastRowNum();

            for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                System.out.println("rIndex: " + rIndex);
                Row row = sheet.getRow(rIndex);
                if (row != null) {
                    KaiLaiOrder kaiLaiOrder=new KaiLaiOrder();
                    int firstCellIndex = row.getFirstCellNum();
                    int lastCellIndex = row.getLastCellNum();
                    for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                        String lieConclum=sheet.getRow(0).getCell(cIndex).toString();
                        Cell cell = row.getCell(cIndex);
                        if (cell != null) {
                            switch (lieConclum){
                                case "预定入住时间":
                                    kaiLaiOrder.setActualCheckInDate(new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue()));
                                    break;
                                case "预定离店时间":
                                    kaiLaiOrder.setActualCheckOutDate(new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue()));
                                    break;
                                case "房型代码":
                                    kaiLaiOrder.setRoomType(cell.toString());
                                    break;
                                case "手机号":
                                    kaiLaiOrder.setPhone(cell.toString());
                                    break;
                                case "订单来源":
                                    kaiLaiOrder.setChanelCode(cell.toString());
                                    break;
                                case "团队订单号":
                                    kaiLaiOrder.setBlockId(cell.toString());
                                    break;
                            }
                        }
                    }
                    list.add(kaiLaiOrder);
                }
            }
        } else {
            return SetResultUtil.setErrorMsgResult(result,"找不到指定的文件");
        }
        for (KaiLaiOrder kaiLaiOrder : list){
            Map<String,String> map=new HashMap<>();
            map.put("beginTime",kaiLaiOrder.getActualCheckInDate());//预定入住时间
            map.put("endTime",kaiLaiOrder.getActualCheckOutDate());//预定离店时间
            map.put("number","1");//预定房间数量
            map.put("roomType",kaiLaiOrder.getRoomType());//房型代码
            map.put("phone",kaiLaiOrder.getPhone());//手机号
            map.put("channelCode",kaiLaiOrder.getChanelCode());//订单来源
            map.put("blockId",kaiLaiOrder.getBlockId());//团队id
            int returnResult=iKaiLaiOrderService.createResvAndBlockID(map);
            if (returnResult==1){
                kaiLaiOrder.setIsFlag("1");// 1成功
            }else{
                kaiLaiOrder.setIsFlag("0");//0失败
            }
        }
        boolean flag=true;
        for (KaiLaiOrder kaiLaiOrder : list){
            if ("0".equals(kaiLaiOrder.getIsFlag())){
                flag=false;
                break;
            }
        }
        if (flag){
            SetResultUtil.setSuccessResult(result,message,JSON.toJSONString(list));
        }else{
            SetResultUtil.setErrorMsgResult(result,message,JSON.toJSONString(list));
        }
        return result;
    }

    /**
     * 团队分房
     */
    @AutoLog(value = "团队分房")
    @ApiOperation(value="团队分房-teamTime", notes="团队分房-teamTime")
    @GetMapping(value = "/teamTime")
    public Result<Object> teamTime(String blockId,String lastName, String firstName, String name,
                                   String gender, String borthDay, String nation, String address,
                                   String idNumber,String certificateType,String nationality) throws Exception {
        Result<Object> result = new Result<Object>();
        //查询团队订单
        List<KaiLaiOrder> list=iKaiLaiOrderService.searchResvByBlockByID(blockId);
        list=FilterListUtil.filterList(list);
        List<KaiLaiOrder> bigOnelist=FilterListUtil.filterListNumber(list);
        KaiLaiOrder returnKaiLaiOrder=new KaiLaiOrder();
        if (bigOnelist.size()<=0){
            return SetResultUtil.setErrorMsgResult(result,"团队已经住满");
        }
        //入住人数为1的订单
        List<KaiLaiOrder> onelist=FilterListUtil.filterListNumberOne(list);
        if (onelist.size()>0){
            boolean flag=false;
            for (KaiLaiOrder kaiLaiOrder : onelist){
                if (kaiLaiOrder.getGender().equals(gender)){
                    Map<String,String> map=new HashMap<>();
                    map.put("orderId",kaiLaiOrder.getResvNameId());
                    map.put("lastName",lastName);
                    map.put("firstName",firstName);
                    map.put("name",name);
                    map.put("gender",gender);
                    map.put("borthDay",borthDay);
                    map.put("nation",nation);
                    map.put("address",address);
                    map.put("idNumber",idNumber);
                    map.put("certificateType",certificateType);
                    map.put("nationality",nationality);
                    int key=iKaiLaiOrderService.createAccompany(map);
                    if (key!=1){
                        return SetResultUtil.setErrorMsgResult(result,"团队入住单条失败");
                    }
                    KaiLaiOrder kSelect=iKaiLaiOrderService.getOne(new QueryWrapper<KaiLaiOrder>().eq("is_accompany","0").eq("resv_name_id",kaiLaiOrder.getResvNameId()));
                    //生成新 同住人订单
                    KaiLaiOrder newCompanyOrder=kSelect;
                    newCompanyOrder.setIsAccompany("1");//1 表示 同住人订单
                    //重新赋值  居住人信息 为 同住人的信息
                    //设置id为空
                    newCompanyOrder.setId(null);
                    newCompanyOrder.setLastName(lastName);
                    newCompanyOrder.setFirstName(firstName);
                    newCompanyOrder.setAltName(name);
                    newCompanyOrder.setGender(gender);
                    newCompanyOrder.setNation(nation);
                    newCompanyOrder.setAddress(address);
                    newCompanyOrder.setBorthday(borthDay);
                    newCompanyOrder.setIdEntityCard(idNumber);
                    newCompanyOrder.setCertificateType(certificateType);
                    newCompanyOrder.setNationality(nationality);
                    iKaiLaiOrderService.save(newCompanyOrder);
                    returnKaiLaiOrder=kaiLaiOrder;
                    flag=true;
                    break;
                }
            }
            if (flag){
                return SetResultUtil.setSuccessResult(result,"团队入住单条成功",JSON.toJSONString(returnKaiLaiOrder));
            }
        }
        List<KaiLaiOrder> zeroList=FilterListUtil.filterListNumberZero(list);
        if (zeroList.size()>0){
            for (KaiLaiOrder kaiLaiOrder : zeroList){
                Map<String,String> map=new HashMap<>();
                map.put("orderId",kaiLaiOrder.getResvNameId());
                map.put("lastName",lastName);
                map.put("firstName",firstName);
                map.put("name",name);
                map.put("gender",gender);
                map.put("borthDay",borthDay);
                map.put("nation",nation);
                map.put("address",address);
                map.put("idNumber",idNumber);
                map.put("certificateType",certificateType);
                map.put("nationality",nationality);
                //修改凯莱订单
                int returnKey=iKaiLaiOrderService.updateProfileByRid(map);
                if(returnKey==1)
                {
                    KaiLaiOrder kaiLai=iKaiLaiOrderService.getOrderByOrderId(kaiLaiOrder.getResvNameId());
                    iKaiLaiOrderService.save(kaiLai);
                    return SetResultUtil.setSuccessResult(result,"团队入住单条成功",JSON.toJSONString(kaiLaiOrder));
                }else{
                    return SetResultUtil.setErrorMsgResult(result,"团队入住单条失败");
                }
            }
        }else{
            if(onelist.size()!=0){
                return SetResultUtil.setErrorMsgResult(result,"最后一个订单，性别不同无法入住");
            }else{
                return SetResultUtil.setErrorMsgResult(result,"团队已经住满");
            }
        }
        return SetResultUtil.setErrorMsgResult(result,"最后一个订单，性别不同无法入住");
    }


    /**
     * 分房
     */
    @AutoLog(value = "分房")
    @ApiOperation(value="分房-getRoomNo", notes="分房-getRoomNo")
    @GetMapping(value = "/getRoomNo")
    public Result<Object> getRoomNo(String orderId){
        Result<Object> result = new Result<Object>();
        String roomNo=iKaiLaiOrderService.getRoomNo(orderId);
        if (null!=roomNo&&!"0".equals(roomNo)){
            SetResultUtil.setSuccessResult(result,"分房成功",roomNo);
        }else{
            SetResultUtil.setErrorMsgResult(result,"分房失败");
        }
        return result;
    }

    /**
     * 预授权暂结账
     */
    @AutoLog(value = "预授权暂结账")
    @ApiOperation(value="预授权暂结账-payBillByCardAmount", notes="预授权暂结账-payBillByCardAmount")
    @GetMapping(value = "/payBillByCardAmount")
    public Result<Object> payBillByCardAmount(String orderId,String roomNo,String billNo,String amount){
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("orderId",orderId);
        map.put("roomNo",roomNo);
        map.put("billNo",billNo);
        map.put("amount",amount);
        if(!CheckMapUtil.checkMap(map)){
            return SetResultUtil.setErrorMsgResult(result,"参数不全");
        }
        int returnKey=iPreAuthorizationService.payBillByCardAmount(map);
        if (1== returnKey){
            SetResultUtil.setSuccessResult(result,"预授权暂结账成功。",orderId);
        }else{
            SetResultUtil.setErrorMsgResult(result,"预授权暂结账失败。");
        }
        return result;
    }

    /**
     * 分页列表查询
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "凯莱订单-分页列表查询")
    @ApiOperation(value="凯莱订单-分页列表查询", notes="凯莱订单-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<KaiLaiOrder>> queryPageList(KaiLaiOrder kaiLaiOrder,
                                               @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                               HttpServletRequest req) {
        Result<IPage<KaiLaiOrder>> result = new Result<IPage<KaiLaiOrder>>();
        QueryWrapper<KaiLaiOrder> queryWrapper = QueryGenerator.initQueryWrapper(kaiLaiOrder, req.getParameterMap());
        Page<KaiLaiOrder> page = new Page<KaiLaiOrder>(pageNo, pageSize);
        IPage<KaiLaiOrder> pageList = iKaiLaiOrderService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 分页列表 时间  查询
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "凯莱订单-分页列表时间查询")
    @ApiOperation(value="凯莱订单-分页列表时间查询", notes="凯莱订单-分页列表时间查询")
    @GetMapping(value = "/timelist")
    public Result<IPage<KaiLaiOrder>> timelist(String beginTime,String endTime,String conlumn,
                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                    HttpServletRequest req) {
        Result<IPage<KaiLaiOrder>> result = new Result<IPage<KaiLaiOrder>>();
        QueryWrapper<KaiLaiOrder> queryWrapper = new QueryWrapper<KaiLaiOrder>().between(conlumn,beginTime,endTime);
        Page<KaiLaiOrder> page = new Page<KaiLaiOrder>(pageNo, pageSize);
        IPage<KaiLaiOrder> pageList = iKaiLaiOrderService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

}
