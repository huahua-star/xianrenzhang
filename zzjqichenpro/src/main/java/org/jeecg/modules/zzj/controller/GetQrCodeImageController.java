package org.jeecg.modules.zzj.controller;

import com.alipay.demo.trade.utils.ZxingUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/zzj/GetQrCode")
public class GetQrCodeImageController {


    /**
     * @param accnt 团队号
     * @return
     */
    @AutoLog(value = "生成二维码")
    @ApiOperation(value="生成二维码", notes="生成二维码")
    @RequestMapping(value = "/GetTeamQrCode")
    public String GetTeamQrCode(String accnt) {
        //根据urlCode 生成二维码
        String qrDir="C://img//";
        File file  = new File(qrDir);
        if(!file.exists()){
            file.mkdir();
        }
        String filePath = String.format(qrDir+"/qr-%s.png",
                accnt+new Date().getTime());
        ZxingUtils.getQRCodeImge(accnt, 256, filePath);
        return filePath;
    }
    @AutoLog(value = "获取二维码")
    @ApiOperation(value="获取二维码", notes="获取二维码")
    @RequestMapping(value = "/getQrImage")
    public void getQrImage(String filePath, HttpServletResponse response) {
        log.info("进入getQrImage()方法filePath:{}", filePath);
        response.setContentType("image/png");
        try {
            FileCopyUtils.copy(new FileInputStream(filePath), response.getOutputStream());
        } catch (FileNotFoundException e) {
            log.error("getQrImage()方法出现异常:{}", e.getMessage());
        } catch (IOException e) {
            log.error("getQrImage()方法出现异常:{}", e.getMessage());
        } catch (Exception e) {
            log.error("getQrImage()方法出现异常:{}", e.getMessage());
        }
    }
}
