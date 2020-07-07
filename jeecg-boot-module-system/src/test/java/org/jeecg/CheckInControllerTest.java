package org.jeecg;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;
import org.jeecg.modules.zzj.entity.KaiLaiRoom;
import org.jeecg.modules.zzj.service.IKaiLaiOrderService;
import org.jeecg.modules.zzj.service.IKaiLaiRoomService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = JeecgApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CheckInControllerTest {

    @Autowired
    private IKaiLaiRoomService iKaiLaiRoomService;

    @Autowired
    private IKaiLaiOrderService iKaiLaiOrderService;

    @Test
    public void test() {
        /*Map<String,String> map=new HashMap<>();
        map.put("beginTime","2019-10-23");
        map.put("endTime","2019-10-30");
        List<KaiLaiRoom> list2= iKaiLaiRoomService.SelectKaiLaiRoom(map);
        for(KaiLaiRoom kaiLaiTest : list2){
            System.out.println(kaiLaiTest);
        }*/

        /*Map<String,String> map=new HashMap<>();
        map.put("beginTime","2019-10-28");
        map.put("endTime","2019-10-30");
        map.put("rateCode","RAC");
        map.put("roomType","BCR");
        map.put("ratePrice","229");
        map.put("phone","1234567890");
        int returnResult=iKaiLaiOrderService.generateOrder(map);
        System.out.println(returnResult);*/

       /* String phone="1234567890";
        //List<KaiLaiOrder> list=iKaiLaiOrderService.getOrderByPhone(phone);
        */

        String orderId="30528";
        KaiLaiOrder kSelect=iKaiLaiOrderService.getOne(new QueryWrapper<KaiLaiOrder>().eq("is_accompany","1").eq("resv_name_id",orderId));
        System.out.println(kSelect);
    }
}
    //判断该人在今天是否已经入住
    //request.getParameterMap 将前端页面 发送的所有 参数 都以 key value的形式传递过来，
    // 且转换为Map<String,String[]>类型的 且不能修改
        /*QueryWrapper<CheckIn> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("idnumber","123");
        queryWrapper.eq("state","1");
        Integer i=mapper.selectCount(queryWrapper);
        List<Map<String,Object>> list=mapper.selectMaps(queryWrapper);
        System.out.println(i);
        for (Map<String,Object> map : list){
            for (Map.Entry<String,Object> obj : map.entrySet()){
                System.out.println("key:"+obj.getKey()+" value:"+obj.getValue());
            }
        }*/
        /*CheckIn checkIn=checkInService.getOne(new QueryWrapper<CheckIn>().eq("idnumber","123"));
        System.out.println(checkIn);
        Map<String, Object> map=checkInService.getMap(new QueryWrapper<CheckIn>());
        for (Map.Entry<String, Object> obj : map.entrySet()){
            System.out.println("key:"+obj.getKey()+"value:"+obj.getValue());
        }
        List<CheckIn> list=checkInService.list();
        System.out.println(list.size());
        for(CheckIn cc : list){
            System.out.println(cc);
        }
        List<CheckIn> handList=mapper.getAll();
        for(CheckIn cc : list){
            System.out.println(cc);
        }*/
   /* List<CheckIn> XMLhandList=mapper.xmlgetAll();
        for(CheckIn cc : XMLhandList){
                System.out.println(cc);
                }*/

   /* List<Trace> list=iTraceService.list();
        System.out.println(list.size());*/