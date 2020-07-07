package org.jeecg.modules.zzj.util.sdk;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.zzj.entity.Room;
import org.jeecg.modules.zzj.entity.Sendrecord;
import org.jeecg.modules.zzj.service.IRoomService;
import org.jeecg.modules.zzj.service.ISendrecordService;
import TTCEPackage.K7X0Sdk;
import TTCEPackage.RunFunction;
import TTCEPackage.RunFunction1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SdkService {

    @Autowired
    private ISendrecordService sendRecordService;
    @Autowired
    private IRoomService roomService;

    @Transactional
    public Map<Integer, String> sendCard(String idCard,String name,String adress,String phone,String outside,Integer rommId) {


        //写卡数据
        String dataStr="#######写卡数据######";

        //发卡到 读卡位置
        Map<Integer,String> result= K7X0Sdk.openAndSend();
        if(!"-1".equals(result.get(0))) {
            return result;
        }

        //插入入住表 处理逻辑
        System.out.println("--------------------------要写卡---------------------");
        //写开锁秘要
		/*String commOpen = RunFunction1.CommOpenAndWrite("COM"+ComHandle1,(byte)0x01, (byte)0x30,dataStr);
		if(null!= commOpen){
			return commOpen+"{}{}{}{} 开锁秘要 写卡失败";
		}
		String id = UUID.randomUUID().toString().substring(0, 11);
		//写数据库关联 send_record id  至0x09 扇区
		String commOpenAndWrite = RunFunction1.CommOpenAndWrite("COM"+ComHandle1,(byte)0x09, (byte)0x30,id);
		if(null!= commOpenAndWrite){
			return commOpenAndWrite+"{}{}{}{} 数据库关联 写卡失败";
		}
		//关闭发卡机
		String shutDown = RunFunction1.shutDown("COM"+ComHandle1);
		if(null!= shutDown){
			return commOpenAndWrite+"{}{}{}{} 读卡器关闭复位失败";
		}*/
        System.out.println("--------------------------完成写卡---------------------");
        //发卡到 出卡位置
        Map<Integer, String> sendCardToExit = K7X0Sdk.sendCardToExit();
        if(!"-1".equals(sendCardToExit.get(0))) {
            return sendCardToExit;
        }

        //记录发卡
        Sendrecord sre =new Sendrecord();
        //写卡 数据
        sre.setCardnum(dataStr);
        sre.setCreatetime(new Date());
        sre.setIdnumber(idCard);
        sre.setName(name);
        //酒馆系统房id
        sre.setOutside(outside);
        sre.setPhonenum(phone);
        //本系统房id
        sre.setRoomnumid(rommId);
        sre.setSendstatus(1);
        sendRecordService.save(sre);
        sendCardToExit.put(0, "success");
        return sendCardToExit;
    }

    public Map<Integer,String> sendCard1(Integer comHandle, Integer ComHandle1,
                                         String idCard, String name, String adress, String phone,
                                         String outside) {
        Map<Integer,String> map =new HashMap<Integer,String>();

        if(StringUtils.isBlank(outside)){
            map.put(-1, "房间，预定信息有误 ==》 103");
            return map;
        }
        //查询 预定 房间
        Room selectOne = roomService.getOne(new QueryWrapper<Room>().eq("room_key", outside));
        if(selectOne==null){
            map.put(-1,"预定信息有误 ==》 101");
            return map;
        }
        if(StringUtils.isBlank(selectOne.getRoomkey())){
            map.put(-1,selectOne.getRoomnum()+"房间，预定信息有误 ==》 102");
            return map;
        }

        //写卡数据
        String dataStr=selectOne.getRoomkey();

        //发卡到 读卡位置 0 1
        Map<Integer,String> result= RunFunction.openAndSend(comHandle);
        if(!"-1".equals(result.get(0))) {
            result.put(-2,"error");
            return result;
        }

        //插入入住表 处理逻辑
        System.out.println("--------------------------要写卡---------------------");
        //写开锁秘要
        Map<Integer, String> commOpenAndWrite = RunFunction1.CommOpenAndWrite("COM"+ComHandle1,(byte)0x04,(byte)0x04, (byte)0x30,dataStr);
        if(!"-1".equals(commOpenAndWrite.get(0))) {
            commOpenAndWrite.put(-3, "error");
            return commOpenAndWrite;
        }
        String id = UUID.randomUUID().toString().substring(0, 11);

        //写数据库关联 send_record id  至0x09 扇区
		/*String commOpenAndWrite = RunFunction1.CommOpenAndWrite("COM"+ComHandle1,(byte)0x05,(byte)0x20, (byte)0x30,id);
		if(null!= commOpenAndWrite){
			map.put(1,commOpenAndWrite+"{}{}{}{} 数据库关联 写卡失败");
			return map;
		}*/
        //关闭发卡机
        //String shutDown = RunFunction1.shutDown("COM"+ComHandle1);
		/*if(null!= shutDown){
			return commOpenAndWrite+"{}{}{}{} 读卡器关闭复位失败";
		}*/
        System.out.println("--------------------------完成写卡---------------------");
        //发卡
        Map<Integer, String> sendCardToExit = RunFunction.sendCardToExit(comHandle);
        if(!"-1".equals(sendCardToExit.get(0))) {
            result.put(-2,"error");
            return result;
        }

        //记录发卡
        Sendrecord sre =new Sendrecord();
        //写卡 数据
        sre.setIdval(id);
        sre.setCardnum(dataStr);
        sre.setCreatetime(new Date());
        sre.setIdnumber(idCard);
        sre.setName(name);
        //酒馆系统房id
        sre.setOutside(outside);
        sre.setPhonenum(phone);
        //本系统房id
        sre.setRoomnumid(selectOne.getTypeid());
        sre.setSendstatus(1);
        sendRecordService.save(sre);
        return null;
    }

    public String outCard(Integer comHandle, Integer comHandle1) {
        //发卡到 出卡位置
        //return RunFunction.sendCardToExit(comHandle);
        return null;
    }

    //退回卡
    public int returnCard(Integer comHandle){
        return RunFunction.returnCard(comHandle);
    }
    //关闭发卡机
    public void colse(Integer comHandle) {
        System.out.println(comHandle+"===================全不关发卡机=================");
        K7X0Sdk.colse(comHandle);

    }
    //关闭读卡机
    public void colse1(Integer comHandle1) {
        System.out.println(comHandle1+"===================全不关读卡器=================");
        RunFunction1.colse(comHandle1);

    }

}

