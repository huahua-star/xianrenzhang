package org.jeecg.modules.zzj.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.zzj.util.JsonUtils;
import org.jeecg.modules.zzj.util.Sample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
@Api(tags="语音")
@RestController
@RequestMapping("/zzj/audio")
public class AudioContoller {
    private static final Logger log = LoggerFactory.getLogger(AudioContoller.class);
    /**
     * 根据文字生成语音
     * @param text 语音内容
     * @return
     */
    @AutoLog(value = "根据文字生成语音-getAutdio")
    @ApiOperation(value="根据文字生成语音-getAutdio", notes="根据文字生成语音-getAutdio")
    @GetMapping(value = "/getAutdio")
    public void getAutdio(String text, HttpServletRequest request, HttpServletResponse response){
        log.info("getAutdio()方法text:{}",text);
        try {
            log.info("根据text获取信息");
            File file = new File("C:/audio/"+text+".mp3");
            byte[] buff;
            if(!file.exists()){
                //本地不存在语音文件
                //百度语音合成
                log.info("根据获取信息生成语音");
                buff = Sample.synthesis(text);
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                log.info("保存在本地");
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(buff);
                fileOutputStream.close();

            }else{
                log.info("保存在本地");
                FileInputStream fileInputStream = new FileInputStream(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int x = 0 ;
                while ((x = fileInputStream.read() )!= -1){
                    byteArrayOutputStream.write(x);
                }
                buff = byteArrayOutputStream.toByteArray();
                fileInputStream.close();
                byteArrayOutputStream.close();

            }
            //设置发送到客户端的响应内容类型
            response.setContentType("audio/*");
            OutputStream out = response.getOutputStream();
            out.write(buff);
            //关闭响应输出流
            out.close();

        } catch (FileNotFoundException e) {
            log.error("getAutdio()方法出现异常",e.getMessage());
        } catch (IOException e) {
            log.error("getAutdio()方法出现异常",e.getMessage());
        } catch (Exception e){
            log.error("getAutdio()方法出现异常",e.getMessage());
        }
    }

    public static void main(String[] args) {
        getAutdio("你好");
    }
    public static void getAutdio(String text){
        log.info("getAutdio()方法text:{}",text);
        try {
            log.info("根据text获取信息");
            File file = new File("F:/audio/"+text+".mp3");
            byte[] buff;
            if(!file.exists()){
                //本地不存在语音文件
                //百度语音合成
                log.info("根据获取信息生成语音");
                buff = Sample.synthesis(text);
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                log.info("保存在本地");
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(buff);
                fileOutputStream.close();

            }else{
                log.info("保存在本地");
                FileInputStream fileInputStream = new FileInputStream(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int x = 0 ;
                while ((x = fileInputStream.read() )!= -1){
                    byteArrayOutputStream.write(x);
                }
                buff = byteArrayOutputStream.toByteArray();
                fileInputStream.close();
                byteArrayOutputStream.close();

            }

        } catch (FileNotFoundException e) {
            log.error("getCheckInPerson()方法出现异常",e.getMessage());
        } catch (IOException e) {
            log.error("getCheckInPerson()方法出现异常",e.getMessage());
        } catch (Exception e){
            log.error("getCheckInPerson()方法出现异常",e.getMessage());
        }
    }
}
