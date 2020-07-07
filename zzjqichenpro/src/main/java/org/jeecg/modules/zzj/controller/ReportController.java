package org.jeecg.modules.zzj.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFAnchor;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.zzj.entity.CheckIn;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;
import org.jeecg.modules.zzj.entity.KaiLaiRoom;
import org.jeecg.modules.zzj.service.IKaiLaiOrderService;
import org.jeecg.modules.zzj.service.IKaiLaiRoomService;
import org.jeecg.modules.zzj.util.Card.SetResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 入住报表
 * @Author: jeecg-boot
 * @Date:   2019-10-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="入住报表")
@RestController
@RequestMapping("/zzj/CheckInReport")
public class ReportController {
    @Autowired
    private IKaiLaiOrderService iKaiLaiOrderService;

    @Autowired
    private IKaiLaiRoomService iKaiLaiRoomService;

    /**
     * 入住明细
     * @param
     * @return
     */
    @AutoLog(value = "checkInDetails-入住明细")
    @ApiOperation(value="checkInDetails-入住明细", notes="checkInDetails-入住明细")
    @GetMapping(value = "/checkInDetails")
    public Result<IPage<KaiLaiOrder>> checkInDetails(KaiLaiOrder kaiLaiOrder,
                                         @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                         HttpServletRequest req) throws Exception {
        Result<IPage<KaiLaiOrder>> result = new Result<IPage<KaiLaiOrder>>();
        QueryWrapper<KaiLaiOrder> queryWrapper = QueryGenerator.initQueryWrapper(kaiLaiOrder, req.getParameterMap());
        Page<KaiLaiOrder> page = new Page<KaiLaiOrder>(pageNo, pageSize);
        IPage<KaiLaiOrder> pageList = iKaiLaiOrderService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 按时间查询 报表明细
     */
    @AutoLog(value = "checkInDetailsByTime-时间查询入住明细")
    @ApiOperation(value="checkInDetailsByTime-时间查询入住明细", notes="checkInDetailsByTime-时间查询入住明细")
    @GetMapping(value = "/checkInDetailsByTime")
    public Result<IPage<KaiLaiOrder>> checkInDetailsByTime(String before,String after,String column,
                                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                     HttpServletRequest req) throws Exception {
        Result<IPage<KaiLaiOrder>> result = new Result<IPage<KaiLaiOrder>>();
        Page<KaiLaiOrder> page = new Page<KaiLaiOrder>(pageNo, pageSize);
        IPage<KaiLaiOrder> pageList = iKaiLaiOrderService.page(page,new QueryWrapper<KaiLaiOrder>().between(column,before,after));
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 单条件 报表明细 查询
     */
    @AutoLog(value = "checkInDetailsByOneCase-单条件查询入住明细")
    @ApiOperation(value="checkInDetailsByOneCase-单条件查询入住明细", notes="checkInDetailsByOneCase-单条件查询入住明细")
    @GetMapping(value = "/checkInDetailsByOneCase")
    public Result<IPage<KaiLaiOrder>> checkInDetailsByOneCase(String zhi,String column,String cases,
                                                           @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                           @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                           HttpServletRequest req) throws Exception {
        Result<IPage<KaiLaiOrder>> result = new Result<IPage<KaiLaiOrder>>();
        Page<KaiLaiOrder> page = new Page<KaiLaiOrder>(pageNo, pageSize);
        QueryWrapper<KaiLaiOrder> queryWrapper =null;
        switch (cases){
            case "eq"://等于
                queryWrapper = new QueryWrapper<KaiLaiOrder>().eq(column,zhi);
                break;
            case "ne"://不等于
                queryWrapper = new QueryWrapper<KaiLaiOrder>().ne(column,zhi);
                break;
            case "gt"://大于
                queryWrapper = new QueryWrapper<KaiLaiOrder>().gt(column,zhi);
                break;
            case "ge"://大于等于
                queryWrapper = new QueryWrapper<KaiLaiOrder>().ge(column,zhi);
                break;
            case "lt"://小于
                queryWrapper = new QueryWrapper<KaiLaiOrder>().lt(column,zhi);
                break;
            case "le"://小于等于
                queryWrapper = new QueryWrapper<KaiLaiOrder>().le(column,zhi);
                break;
            case "like"://模糊查询
                queryWrapper = new QueryWrapper<KaiLaiOrder>().like(column,zhi);
                break;
        }
        IPage<KaiLaiOrder> pageList = iKaiLaiOrderService.page(page,queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }





}
