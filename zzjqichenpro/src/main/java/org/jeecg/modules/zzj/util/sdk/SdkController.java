package org.jeecg.modules.zzj.util.sdk;

import java.util.Map;

import org.jeecg.modules.zzj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 *
 * @author fjn
 * @email zhengmaode@huashengzhiku.com
 * @date 2018-08-27 23:25:09
 */
@RestController
@RequestMapping("sdk")
public class SdkController {

    /**
     * 发卡机串口
     */
    @Value("${sdk.ComHandle}")
    private Integer ComHandle;
    /**
     * 读卡机串口
     */
    @Value("${sdk1.ComHandle}")
    private Integer ComHandle1;

    @Autowired
    private SdkService sdkService;
    /**
     * 发卡 一体化test
     */
    @RequestMapping("/sendCard")
    public R sendCard(String idCard,String name,String adress,String phone,String outside,Integer rommId,Integer id){

        Map<Integer, String> sendCard = sdkService.sendCard(idCard,name,adress,phone,outside,rommId);
        System.out.println(sendCard.get(0));
        if(!"-1".equals(sendCard.get(0))){
            if(sendCard.get(1)!=null) {
                sdkService.colse(Integer.parseInt(sendCard.get(1)));
            }
            return R.error(sendCard.get(0));
        }
        return R.ok();
    }

    /**
     * 写卡
     */
    @RequestMapping("/sendCard1")
    public R sendCard1(String idCard,String name,String adress,String phone,String outside){

        Map<Integer, String> sendCard1 = sdkService.sendCard1(ComHandle,ComHandle1,idCard,name,adress,phone,outside);

        if(sendCard1==null){
            return R.ok();
        }else if(sendCard1.get(-1)!=null) {
            //System.out.println(sendCard1.get(0)+"111111111=====================================");
            //退卡
            //sdkService.returnCard(ComHandle);
   			/*if(sendCard1.size()!=1) {
   				sdkService.colse(ComHandle,Integer.parseInt(sendCard1.get(1)));
   			}*/
            System.out.println("error --- > -1");
            return R.error(sendCard1.get(-1));
        }else if(sendCard1.get(-2)!=null){
            //退卡
            // System.out.println(sendCard1.get(1)+"222222222222222222=====================================");
            //sdkService.returnCard(ComHandle);
            System.out.println("error --- > -2");
            if(sendCard1.get(1)!=null) {
                //关闭发卡机
                sdkService.colse(Integer.parseInt(sendCard1.get(1)));
            }
            return R.error(sendCard1.get(0));
        }else if(sendCard1.get(-3)!=null){
            //退卡
            // System.out.println(sendCard1.get(1)+"222222222222222222=====================================");
            //sdkService.returnCard(ComHandle);
            System.out.println("error --- > -3");
            if(sendCard1.get(1)!=null) {
                sdkService.colse1(Integer.parseInt(sendCard1.get(1)));
            }
            return R.error(sendCard1.get(0));
        }
        return R.ok();
    }

    /**
     * 出卡
     */
    @RequestMapping("/outCard")
    public R sendTut(){
        String msg  = sdkService.outCard(ComHandle,ComHandle1);
        if(msg!=null){
            //退卡
            sdkService.returnCard(ComHandle);
            return R.error(msg);
        }

        return R.ok();
    }


}

