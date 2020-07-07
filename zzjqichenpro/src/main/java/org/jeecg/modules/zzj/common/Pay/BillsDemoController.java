package org.jeecg.modules.zzj.common.Pay;


import com.alibaba.fastjson.JSON;
import com.alipay.demo.trade.utils.ZxingUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/bills")
public class BillsDemoController {

    //获取二维码页面
    @ResponseBody
    @RequestMapping(value = "/getQrCode.do",method = RequestMethod.POST)//,GetQrCodeRequest requestParams
    public Map getQrCode(HttpServletResponse response,String amount,String qrDir,String APIurl,
                         String mid,String tid,String msgSrc,String instMid,String outTradeNo,String key) throws Exception {
        //组织请求报文
        JSONObject json = new JSONObject();
        json.put("mid", mid);
        json.put("tid", tid);
        json.put("msgType", "bills.getQRCode");
        json.put("msgSrc", msgSrc);
        json.put("instMid", instMid);
        json.put("billNo", outTradeNo);

        //是否要在商户系统下单，看商户需求  createBill()

        json.put("billDate",DateFormatUtils.format(new Date(),"yyyy-MM-dd"));

        json.put("totalAmount", amount);
        json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        //半个小时后二维码失效
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 30);
        json.put("expireTime",DateFormatUtils.format(cal.getTime(), "yyyy-MM-dd HH:mm:ss"));
        json.put("secureTransaction",true);

        Map<String, String> paramsMap = Util.jsonToMap(json);
        paramsMap.put("sign", Util.makeSign(key, paramsMap));
        System.out.println("paramsMap："+paramsMap);

        String strReqJsonStr = JSON.toJSONString(paramsMap);
        System.out.println("strReqJsonStr:"+strReqJsonStr);

        //调用银商平台获取二维码接口
        HttpURLConnection httpURLConnection = null;
        BufferedReader in = null;
        PrintWriter out = null;
//        OutputStreamWriter out = null;
        String resultStr = null;
        Map<String,String> resultMap = new HashMap<String,String>();
        if (!StringUtils.isNotBlank(APIurl)) {
            resultMap.put("errCode","URLFailed");
            resultStr = JSONObject.fromObject(resultMap).toString();
            return resultMap;
        }

        try {
            URL url = new URL(APIurl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content_Type","application/json");
            httpURLConnection.setRequestProperty("Accept_Charset","UTF-8");
            httpURLConnection.setRequestProperty("contentType","UTF-8");
            //发送POST请求参数
            out = new PrintWriter(httpURLConnection.getOutputStream());
//            out = new OutputStreamWriter(httpURLConnection.getOutputStream(),"utf-8");
            out.write(strReqJsonStr);
//            out.println(strReqJsonStr);
            out.flush();

            //读取响应
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuffer content = new StringBuffer();
                String tempStr = null;
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((tempStr=in.readLine()) != null){
                    content.append(tempStr);
                }
                System.out.println("content:"+content.toString());

                //转换成json对象
                com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
                String resultCode = respJson.getString("errCode");
                resultMap.put("errCode",resultCode);
                if (resultCode.equals("SUCCESS")) {
                    String billQRCode = (String) respJson.get("billQRCode");
                    resultMap.put("billQRCode",billQRCode);
                    resultMap.put("respStr",respJson.toString());
                }else {
                    resultMap.put("respStr",respJson.toString());
                }
                resultStr = JSONObject.fromObject(resultMap).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("errCode","HttpURLException");
            resultMap.put("msg","调用银商接口出现异常："+e.toString());
            resultStr = JSONObject.fromObject(resultMap).toString();
            return resultMap;
        }finally {
            if (out != null) {
                out.close();
            }
            httpURLConnection.disconnect();
        }

        System.out.println("hah:"+resultStr);
        String urlCode = resultMap.get("billQRCode");
        System.out.println("二维码路径:"+urlCode);
        File file  = new File(qrDir);
        if(!file.exists()){
            file.mkdir();
        }
        String filePath = String.format(qrDir+"/qr-%s.png",
                outTradeNo);
        ZxingUtils.getQRCodeImge(urlCode, 256, filePath);
        System.out.println(filePath);
        resultMap.put("filePath",filePath);
        resultMap.put("outTradeNo",outTradeNo);
        return resultMap;
    }

    //账单查询
    @ResponseBody
    @RequestMapping(value = "/billQuery.do",method = RequestMethod.POST)
    public Map billQuery(HttpServletResponse response,String outTradeNo,String mid,String tid,String msgSrc,
                         String instMid,String nowTime,String key,String APIurl){
        //System.out.println("请求参数对象："+requestParams);
        //组织请求报文
        JSONObject json = new JSONObject();
        json.put("mid", mid);
        json.put("tid", tid);
        json.put("msgType", "bills.query");
        json.put("msgSrc", msgSrc);
        json.put("instMid", instMid);
        json.put("billNo", outTradeNo);

        //是否要在商户系统下单，看商户需求  createBill()

        json.put("billDate",nowTime);
        json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        Map<String, String> paramsMap = Util.jsonToMap(json);
        paramsMap.put("sign", Util.makeSign(key, paramsMap));
        //System.out.println("paramsMap："+paramsMap);

        String strReqJsonStr = JSON.toJSONString(paramsMap);
        //System.out.println("strReqJsonStr:"+strReqJsonStr);

        //调用银商平台获取二维码接口
        HttpURLConnection httpURLConnection = null;
        BufferedReader in = null;
        PrintWriter out = null;
//        OutputStreamWriter out = null;
        String resultStr = null;
        Map<String,String> resultMap = new HashMap<String,String>();
        if (!StringUtils.isNotBlank(APIurl)) {
            resultMap.put("errCode","URLFailed");
            resultStr = JSONObject.fromObject(resultMap).toString();
            return resultMap;
        }

        try {
            URL url = new URL(APIurl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content_Type","application/json");
            httpURLConnection.setRequestProperty("Accept_Charset","UTF-8");
            httpURLConnection.setRequestProperty("contentType","UTF-8");
            //发送POST请求参数
            out = new PrintWriter(httpURLConnection.getOutputStream());
//            out = new OutputStreamWriter(httpURLConnection.getOutputStream(),"utf-8");
            out.write(strReqJsonStr);
//            out.println(strReqJsonStr);
            out.flush();

            //读取响应
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuffer content = new StringBuffer();
                String tempStr = null;
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((tempStr=in.readLine()) != null){
                    content.append(tempStr);
                }
                //System.out.println("content:"+content.toString());

                //转换成json对象
                com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
                String resultCode = respJson.getString("errCode");
                resultMap.put("errCode",resultCode);
                resultMap.put("respStr",respJson.toString());
                resultStr = JSONObject.fromObject(resultMap).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("errCode","HttpURLException");
            resultMap.put("msg","调用银商接口出现异常："+e.toString());
            resultStr = JSONObject.fromObject(resultMap).toString();
            return resultMap;
        }finally {
            if (out != null) {
                out.close();
            }
            httpURLConnection.disconnect();
        }

        //System.out.println("resultStr:"+resultStr);
        return resultMap;
    }

    /* *
     * 担保完成
     * */

    @ResponseBody
    @RequestMapping(value = "/done.do",method = RequestMethod.POST)
    public Map donePay(HttpServletResponse response,String amount,String outTradeNo,String mid,String tid,String msgSrc,
                       String instMid,String nowTime,String key,String APIurl){

        //组织请求报文
        JSONObject json = new JSONObject();
        json.put("msgType", "secureComplete");
        json.put("msgSrc", msgSrc);
        json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        json.put("merOrderId",outTradeNo);
        json.put("instMid", instMid);
        json.put("mid", mid);
        json.put("tid", tid);
        json.put("completedAmount",amount);
        //是否要在商户系统下单，看商户需求  createBill()
        Map<String, String> paramsMap = Util.jsonToMap(json);
        paramsMap.put("sign", Util.makeSign(key, paramsMap));
        //System.out.println("paramsMap："+paramsMap);
        String strReqJsonStr = JSON.toJSONString(paramsMap);
        //System.out.println("strReqJsonStr:"+strReqJsonStr);
        //调用银商平台获取二维码接口
        HttpURLConnection httpURLConnection = null;
        BufferedReader in = null;
        PrintWriter out = null;
//        OutputStreamWriter out = null;
        String resultStr = null;
        Map<String,String> resultMap = new HashMap<String,String>();
        if (!StringUtils.isNotBlank(APIurl)) {
            resultMap.put("errCode","URLFailed");
            resultStr = JSONObject.fromObject(resultMap).toString();
            return resultMap;
        }

        try {
            URL url = new URL(APIurl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content_Type","application/json");
            httpURLConnection.setRequestProperty("Accept_Charset","UTF-8");
            httpURLConnection.setRequestProperty("contentType","UTF-8");
            //发送POST请求参数
            out = new PrintWriter(httpURLConnection.getOutputStream());
//            out = new OutputStreamWriter(httpURLConnection.getOutputStream(),"utf-8");
            out.write(strReqJsonStr);
//            out.println(strReqJsonStr);
            out.flush();

            //读取响应
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuffer content = new StringBuffer();
                String tempStr = null;
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((tempStr=in.readLine()) != null){
                    content.append(tempStr);
                }
                //System.out.println("content:"+content.toString());

                //转换成json对象
                com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
                String resultCode = respJson.getString("errCode");
                resultMap.put("errCode",resultCode);
                resultMap.put("respStr",respJson.toString());
                resultStr = JSONObject.fromObject(resultMap).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("errCode","HttpURLException");
            resultMap.put("msg","调用银商接口出现异常："+e.toString());
            resultStr = JSONObject.fromObject(resultMap).toString();
            return resultMap;
        }finally {
            if (out != null) {
                out.close();
            }
            httpURLConnection.disconnect();
        }

        //System.out.println("resultStr:"+resultStr);
        return resultMap;
    }


    /* *
     * 撤销接口
     * */

    @ResponseBody
    @RequestMapping(value = "/chexiao.do",method = RequestMethod.POST)
    public Map chexiao(HttpServletResponse response,String outTradeNo,String mid,String tid,String msgSrc,
                       String instMid,String nowTime,String key,String APIurl){

        //组织请求报文
        JSONObject json = new JSONObject();
        json.put("msgSrc", msgSrc);
        json.put("msgType", "secureCancel");
        json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        json.put("merOrderId",outTradeNo);
        json.put("instMid", instMid);
        json.put("mid", mid);
        json.put("tid", tid);
        //是否要在商户系统下单，看商户需求  createBill()
        Map<String, String> paramsMap = Util.jsonToMap(json);
        paramsMap.put("sign", Util.makeSign(key, paramsMap));
        // System.out.println("paramsMap："+paramsMap);
        String strReqJsonStr = JSON.toJSONString(paramsMap);
        // System.out.println("strReqJsonStr:"+strReqJsonStr);
        //调用银商平台获取二维码接口
        HttpURLConnection httpURLConnection = null;
        BufferedReader in = null;
        PrintWriter out = null;
//        OutputStreamWriter out = null;
        String resultStr = null;
        Map<String,String> resultMap = new HashMap<String,String>();
        if (!StringUtils.isNotBlank(APIurl)) {
            resultMap.put("errCode","URLFailed");
            resultStr = JSONObject.fromObject(resultMap).toString();
            return resultMap;
        }

        try {
            URL url = new URL(APIurl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content_Type","application/json");
            httpURLConnection.setRequestProperty("Accept_Charset","UTF-8");
            httpURLConnection.setRequestProperty("contentType","UTF-8");
            //发送POST请求参数
            out = new PrintWriter(httpURLConnection.getOutputStream());
//            out = new OutputStreamWriter(httpURLConnection.getOutputStream(),"utf-8");
            out.write(strReqJsonStr);
//            out.println(strReqJsonStr);
            out.flush();

            //读取响应
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuffer content = new StringBuffer();
                String tempStr = null;
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((tempStr=in.readLine()) != null){
                    content.append(tempStr);
                }
                // System.out.println("content:"+content.toString());

                //转换成json对象
                com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
                String resultCode = respJson.getString("errCode");
                resultMap.put("errCode",resultCode);
                resultMap.put("respStr",respJson.toString());
                resultStr = JSONObject.fromObject(resultMap).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("errCode","HttpURLException");
            resultMap.put("msg","调用银商接口出现异常："+e.toString());
            resultStr = JSONObject.fromObject(resultMap).toString();
            return resultMap;
        }finally {
            if (out != null) {
                out.close();
            }
            httpURLConnection.disconnect();
        }

        //System.out.println("resultStr:"+resultStr);
        return resultMap;
    }

}
