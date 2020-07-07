package org.jeecg.modules.zzj.common.Pay;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import com.alipay.demo.trade.utils.ZxingUtils;
import org.jdom.JDOMException;

/**
 *	 微信扫码支付功能
 */
public class WxpayUtili{
	/**
	 * 	生成二维码
	 * @return 图片地址和订单号
	 * @throws UnknownHostException 
	 */
	 public static Map getqrcode(String qrDir,String totalFee) throws UnknownHostException{
		 String urlCode = null;
		 SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();
		 	String outTradeNo = UUID.randomUUID().toString().replace("-", "");
	        String currTime = PayToolUtil.getCurrTime();
	        String strTime = currTime.substring(8, currTime.length());
	        String strRandom = String.valueOf(PayToolUtil.buildRandom(4));
	        String nonceStr = strTime + strRandom;
	        InetAddress addr = InetAddress.getLocalHost();  
	        String createIp=addr.getHostAddress().toString(); //获取本机ip  
	        packageParams.put("appid", PayConfigUtil.APP_ID);//公众账号ID
	        packageParams.put("mch_id", PayConfigUtil.MCH_ID);//商户号
	        packageParams.put("nonce_str", nonceStr);//随机字符串
	        packageParams.put("body", "kailai");  //商品描述
	        packageParams.put("out_trade_no", outTradeNo);//商户订单号
	        packageParams.put("total_fee", totalFee); //标价金额 订单总金额，单位为分
	        packageParams.put("spbill_create_ip", createIp);//终端IP APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
	        packageParams.put("notify_url", "http://www.weixin.qq.com/wxpay/pay.php");//通知地址 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数
	        packageParams.put("trade_type", "NATIVE");//交易类型 NATIVE 扫码支付
	        // 签名
	        String sign = PayToolUtil.createSign("UTF-8", packageParams, PayConfigUtil.API_KEY);
	        packageParams.put("sign", sign);
	        // 将请求参数转换为xml格式的string
	        String requestXML = PayToolUtil.getRequestXml(packageParams);
	        // 调用微信支付统一下单接口
	        String resXml = HttpUtil.postData(PayConfigUtil.UFDODER_URL, requestXML);

	        // 解析微信支付结果
	        Map map = null;
	        try {
	            map = XMLUtil4jdom.doXMLParse(resXml);
	        } catch (JDOMException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 
	        // 返回微信支付的二维码连接
	        urlCode = (String) map.get("code_url");
	        File file  = new File(qrDir);
	        if(!file.exists()){
	            file.mkdir();
	        }
	        String filePath = String.format(qrDir+"/qr-%s.png",
	        		outTradeNo);
	        ZxingUtils.getQRCodeImge(urlCode, 256, filePath);
	        Map resultMap = new HashMap();
	        resultMap.put("filePath",filePath);
            resultMap.put("outTradeNo",outTradeNo);
		return resultMap;
	 }
	 public static Map query(String outTradeNo){
		 SortedMap<Object,Object> parameterMap = new TreeMap<Object,Object>();
		 parameterMap.put("appid", PayConfigUtil.APP_ID);//公众账号ID
		 parameterMap.put("mch_id", PayConfigUtil.MCH_ID);//商户号
    	 String currTime = PayToolUtil.getCurrTime();
    	 String strTime = currTime.substring(8, currTime.length());
         String strRandom = String.valueOf(PayToolUtil.buildRandom(4));
         String nonce_str = strTime + strRandom;
         parameterMap.put("out_trade_no", outTradeNo);
    	 parameterMap.put("nonce_str", nonce_str);//随机字符串
         // 签名
         String sign = PayToolUtil.createSign("UTF-8", parameterMap, PayConfigUtil.API_KEY);
         parameterMap.put("sign", sign);
    	 String requestXML = PayToolUtil.getRequestXml(parameterMap);
    	 String result = HttpUtil.postData("https://api.mch.weixin.qq.com/pay/orderquery", requestXML);
    
    	 Map<String, String> map = null; 
    	 try {
    		 map = XMLUtil4jdom.doXMLParse(result);
    		 } catch (JDOMException e) { 
    			 e.printStackTrace();
    			 } catch (IOException e) { 
    				 e.printStackTrace();
    				 } 
    	 return map;
	 }
}
