package org.jeecg.modules.zzj.common.Pay;
import com.alibaba.fastjson.JSONObject;
import com.alipay.demo.trade.utils.ZxingUtils;


import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 预授权工具类
 */
public class RequestUrl {

    /**
     * 预授权汇总查询
     * @param order_time_s
     * @param order_time_e
     * @return
     */
    public  String summary_query (String apikey,String order_time_s,String order_time_e,String nonce_str){
    String service = "mer.gather";
        Map<String,String> parameterMap = new HashMap<>();
        parameterMap.put("apikey",apikey);
        parameterMap.put("nonce_str",nonce_str);
        parameterMap.put("service",service);
        parameterMap.put("order_time_s",order_time_s);
        parameterMap.put("order_time_e",order_time_e);
        parameterMap.put("signkey","punr8ucu");
        //map转换成String类型
        String resullt = getMapToString(parameterMap);
        //32位小写md5加密
        String sign = encrypt32(resullt);
        parameterMap.put("sign",sign);
        parameterMap.remove("signkey");
        String parameter = getMapToString(parameterMap);
        //发送请求
        String sr=sendPost("http://zhaoyin.lfwin.com/deposit/pay/mer_gather?test=1", parameter);
        JSONObject jsonObj = JSONObject.parseObject(sr);
        return sr;
    }
    /**
     * 预授权支付
     * 预授权支付完成后，30天没有执行预授权完成或者撤销，这笔账会自动退款
     * @param payType
     * @param money
     * @param qrDir
     * @return
     */
    public  Map qrcode(Integer payType,String money,String qrDir,String apikey,String nonce_str) {
        String service;
        //payType 0 支付宝 1微信
        if(payType == 0){
            service = "pay.alipay.qrcode";
        }else {
            service = "fuiou.wxpay.qrcode";
        }
        //数据放入map中
        Map<String,String> parameterMap = new HashMap<>();
        parameterMap.put("apikey",apikey);
        parameterMap.put("money",money);
        parameterMap.put("remarks","西苑饭店预授权");
        parameterMap.put("nonce_str",nonce_str);
        parameterMap.put("service",service);
        parameterMap.put("signkey","punr8ucu");
        //map转换成String类型
        String resullt = getMapToString(parameterMap);
        //32位小写md5加密
        String sign = encrypt32(resullt);
        parameterMap.put("sign",sign);
        parameterMap.remove("signkey");
        String parameter = getMapToString(parameterMap);
        //发送请求
        String sr=sendPost("http://zhaoyin.lfwin.com/deposit/pay/qrcode", parameter);
        JSONObject jsonObj = JSONObject.parseObject(sr);
        String urlCode = jsonObj.get("qr_code").toString();
        String orderid = jsonObj.get("orderid").toString();
        //根据urlCode 生成二维码
        File file  = new File(qrDir);
        if(!file.exists()){
            file.mkdir();
        }
        String filePath = String.format(qrDir+"/qr-%s.png",
                orderid);
        ZxingUtils.getQRCodeImge(urlCode, 256, filePath);
        Map resultMap = new HashMap();
        resultMap.put("filePath",filePath);
        resultMap.put("outTradeNo",orderid);
        //返回二维码路径和订单号
        return resultMap;
    }

    /**
     * 返回map
     * @param jsonObj
     * @return
     */
    public  Map returnMap(JSONObject jsonObj){
        Map map = new HashMap();
        map.put("authorizationCode",jsonObj.get("orderid").toString());//授权码
        map.put("cardNumber","");//卡号
        map.put("cardType","");//卡类型
        map.put("amount",jsonObj.get("paymoney").toString());//金额
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
        long lt = new Long(Long.parseLong(jsonObj.get("paytime").toString())*1000);//交易支付当天
        //long j = lt+3*60 * 60 * 24*1000;三天后
        Date date = new Date(lt);
        map.put("expirationDate",simpleDateFormat.format(date));//有效期,opera 格式12/12
        return map;
    }

    /**
     * 预授权支付状态轮询
     * 本接口仅支持对预授权的支付状态进行查询
     * @param payType
     * @param orderid
     * @return
     */
    public  String query_order(Integer payType,String orderid,String apikey,String nonce_str) {
        String service;
        //payType 0 支付宝 1微信
        if(payType == 0){
            service = "pay.alipay.query_order";
        }else {
            service = "fuiou.wxpay.query_order";
        }
        //数据放入map
        Map<String,String> parameterMap = new HashMap<>();
        parameterMap.put("orderid",orderid);
        parameterMap.put("apikey",apikey);
        parameterMap.put("nonce_str",nonce_str);
        parameterMap.put("service",service);
        parameterMap.put("signkey","punr8ucu");
        //转换成String
        String resullt = getMapToString(parameterMap);
        //32位小写md5加密
        String sign = encrypt32(resullt);
        parameterMap.put("sign",sign);
        parameterMap.remove("signkey");//不删除signkey会出现参数错误
        String parameter = getMapToString(parameterMap);
        //发送请求  parameterMap.remove("signkey");不删除signkey会出现参数错误
        String sr= sendPost("http://zhaoyin.lfwin.com/deposit/pay/query_order", parameter);
        return sr;
    }

    /**
     * 预授权（撤销/完成）查询
     * 本接口仅支持对预授权完成跟撤销状态进行查询
     * @param payType
     * @param orderid
     * @return
     */
    public  String query_deposit(Integer payType,String orderid,String apikey,String nonce_str){
        String service;
        //payType 0 支付宝 1微信
        if(payType == 0){
            service = "pay.alipay.query_deposit";
        }else {
            service = "fuiou.wxpay.query_deposit";
        }
        Map<String,String> parameterMap = new HashMap<>();
        parameterMap.put("orderid",orderid);
        parameterMap.put("apikey",apikey);
        parameterMap.put("nonce_str",nonce_str);
        parameterMap.put("service",service);
        parameterMap.put("signkey","punr8ucu");
        String resullt = getMapToString(parameterMap);
        String sign = encrypt32(resullt);
        parameterMap.put("sign",sign);
        parameterMap.remove("signkey");
        String parameter = getMapToString(parameterMap);
        String sr= sendPost("http://zhaoyin.lfwin.com/deposit/pay/query_deposit", parameter);
        return sr;
    }

    /**
     * 预授权撤销
     * 支付的订单金额，原路返回消费者
     * @param payType
     * @param orderid
     * @return
     */
    public  String cancel_order(Integer payType,String orderid,String apikey,String nonce_str) {
        String service;
        //payType 0 支付宝 1微信
        if(payType == 0){
            service = "pay.alipay.cancel_order";
        }else {
            service = "fuiou.wxpay.cancel_order";
        }
        //数据放入map
        Map<String,String> parameterMap = new HashMap<>();
        parameterMap.put("orderid",orderid);
        parameterMap.put("apikey",apikey);
        parameterMap.put("nonce_str",nonce_str);
        parameterMap.put("service",service);
        parameterMap.put("signkey","punr8ucu");
        String resullt = getMapToString(parameterMap);
        String sign = encrypt32(resullt);
        parameterMap.put("sign",sign);
        parameterMap.remove("signkey");
        String parameter = getMapToString(parameterMap);
        //发送请求
        String sr= sendPost("http://zhaoyin.lfwin.com/deposit/pay/cancel_order?test=1", parameter);
       /* JSONObject jsonObj = new JSONObject(sr);
        String message = jsonObj.get("message").toString();*/
        return sr;
    }

    /**
     *  预授权完成
     *  支付的订单金额，执行完成预授权操作，订单金额打款至商家账户
     * @param payType
     * @param orderid
     * @param money
     * @return
     */
    public  String finish_order(Integer payType,String orderid,String money,String apikey,String nonce_str){
        String service;
        //payType 0 支付宝 1微信
        if(payType == 0){
            service = "pay.alipay.finish_order";
        }else {
            service = "fuiou.wxpay.finish_order";
        }
        Map<String,String> parameterMap = new HashMap<>();
        parameterMap.put("orderid",orderid);
        parameterMap.put("apikey",apikey);
        parameterMap.put("nonce_str",nonce_str);
        parameterMap.put("service",service);
        parameterMap.put("money",money);
        parameterMap.put("signkey","punr8ucu");
        String resullt = getMapToString(parameterMap);
        String sign = encrypt32(resullt);
        parameterMap.put("sign",sign);
        parameterMap.remove("signkey");
        String parameter = getMapToString(parameterMap);
        String sr= sendPost("http://zhaoyin.lfwin.com/deposit/pay/finish_order", parameter);
        return sr;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *      发送请求的URL
     * @param param
     *      请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *      发送请求的 URL
     * @param param
     *      请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * map转String
     * @param map
     * @return
     */
    public static String getMapToString(Map<String,String> map){

        Set<String> keySet = map.keySet();
        //将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //给数组排序(升序)
        Arrays.sort(keyArray);
        //因为String拼接效率会很低的，所以转用StringBuilder。博主会在这篇博文发后不久，会更新一篇String与StringBuilder开发时的抉择的博文。
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            // 参数值为空，则不参与签名 这个方法trim()是去空格
            if (map.get(keyArray[i]).trim().length() > 0) {
                sb.append(keyArray[i]).append("=").append(map.get(keyArray[i]).trim());
            }
            if(i != keyArray.length-1){
                sb.append("&");
            }
        }
        return sb.toString();
    }

    /**
     *  小写md5加密
     * @param encryptStr
     * @return
     */
    public static String encrypt32(String encryptStr) {
        MessageDigest md5;
        //662cca5602d241f49528c57f6b8ca2a8
        //201905141027090070052851
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            encryptStr = hexValue.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encryptStr;
    }
    public static void main(String[] args){
      /*  RequestUrl r = new RequestUrl();
        r.qrcode(1,"0.01", "C://qrImage","00014005",UuidUtils.getUUID());*/

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, -1);
        System.out.println(c.getTime());
    }

}