package org.jeecg.modules.zzj.util.timer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.zzj.util.Card.RedisUtils;
import org.jeecg.modules.zzj.util.CardClient;
import TTCEPackage.K7X0Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Map;

//轮询检测发卡位置状况
@Slf4j
@Component
public class SendCardTime {

	@Autowired
	private RedisUtils redisUtils;
	@Value("${sdk.ComHandle}")
	private Integer comHandle;
	@Value("${cardUrl}")
	private String cardUrl;
	/**
	 * 5秒 执行一次
	 */
	//@Scheduled(cron = "0/5 * * * * *")
    public void checkSendCard(){
		Map map = redisUtils.rpop("cardCMD", Map.class);
		//System.out.println(" =====执行轮询=====");
		if(map !=null && "send".equals(map.get("message"))){

            //检测发卡位置如果有卡，向redis中存入发卡命令，不执行发卡命令
            while (K7X0Util.check(3,0x31)){
                redisUtils.rpush("cardCMD",map);
                System.out.println("##########有卡未取出");
                return;
            }
			System.out.println("发送到读卡位置");
			K7X0Util.sendToRead(comHandle);
			System.out.println("-------------------------------");
			String uri = cardUrl+"/WriteGuestCard2?";
			System.out.println("------------------"+map.get("name").toString());
			String name = map.get("name").toString();
			StringBuffer sb = new StringBuffer("name="+ name);
			sb.append("&roomNo="+map.get("roomNo").toString());
			sb.append("&sDate="+map.get("sDate").toString());
			sb.append("&endDate="+map.get("endDate").toString());
			sb.append("&type="+map.get("type").toString());
			String result = CardClient.doGet(uri,sb.toString());
			Map resultCode = (Map)((Map) JSONObject.parse(result)).get("data");
			System.out.println(resultCode.toString()+"######################");
			if("0".equals(resultCode.get("code").toString())||"-1".equals(resultCode.get("code").toString())){ //TODO 暂时
				//写卡成功
				System.out.println("开始发卡");
				K7X0Util.sendCardToTake(comHandle);
			}else {
				//写卡失败 重新
				redisUtils.rpush("cardCMD",map);
				//回收卡
				K7X0Util.regainCard(comHandle);
				System.out.println("##########写卡失败，回收卡");
			}

		}
		//System.out.println("======轮询结束======");
    }

}
