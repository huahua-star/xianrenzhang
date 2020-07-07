package org.jeecg.modules.zzj.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.zzj.util.PhotoCompareUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags="摄像头识别")
@RestController
@RequestMapping("/zzj/PhotoCompare")
public class PhotoCompareController {

    @ApiOperation(value="人证对比", notes="人证对比")
    @RequestMapping(value = "/photoCompare")
    public Result<?> photoCompare(String filePath) {
        log.info("photoCompare()filePath:{}",filePath);


        int key= PhotoCompareUtil.CLibrary.INSTANCE.Init("C:\\Users\\HTWD\\Desktop\\xianrenzhang");
        if (1==key){
            key=PhotoCompareUtil.CLibrary.INSTANCE.FaceCompare("C:\\images\\testHead.jpg");
            if (1==key){
                key=PhotoCompareUtil.CLibrary.INSTANCE.UnInit();
                System.out.println("key:uninit："+key);
                return Result.ok("成功");
            }else {
                return Result.error("人脸对比失败");
            }
        }else{
            return Result.error("初始化失败");
        }
    }
}
