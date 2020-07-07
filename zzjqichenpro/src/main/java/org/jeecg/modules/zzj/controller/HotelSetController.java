package org.jeecg.modules.zzj.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.zzj.entity.HotelSetTable;
import org.jeecg.modules.zzj.service.HotelSetTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/HotelSet")
public class HotelSetController {

    @Autowired
    private HotelSetTableService hotelSetTableService;

    /**
     * 查询酒店设置
     */
    @ApiOperation(value = "查询酒店设置")
    @RequestMapping(value = "/getHotelSet", method = RequestMethod.GET)
    public Result<Object> getHotelSet() {
        log.info("getHotelSet()方法");
        List<HotelSetTable> hotelSetTables=hotelSetTableService.list();
        return Result.ok(hotelSetTables);
    }

    /**
     * 设置酒店设置
     */
    @ApiOperation(value = "设置酒店设置")
    @RequestMapping(value = "/setHotelSet", method = RequestMethod.POST)
    public Result<Object> setHotelSet(@RequestBody HotelSetTable hotelSetTable){
        log.info("setHotelSet()方法");
        hotelSetTableService.updateById(hotelSetTable);
        return Result.ok("成功");
    }

}
