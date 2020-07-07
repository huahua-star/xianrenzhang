package org.jeecg.modules.zzj.controller;

import javax.servlet.http.HttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.zzj.entity.Departuredetails;
import org.jeecg.modules.zzj.entity.Klrecord;
import org.jeecg.modules.zzj.service.IDeparturedetailsService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.zzj.service.IklrecordService;
import org.jeecg.modules.zzj.util.QRCodeUtils;
import org.jeecg.modules.zzj.util.R;
import org.jeecg.modules.zzj.util.StringToMap;
import org.jeecg.modules.zzj.util.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 自助机离店明细
 * @Author: jeecg-boot
 * @Date: 2019-12-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "自助机离店明细及离店发票")
@RestController
@RequestMapping("/zzj/departuredetails")
public class DeparturedetailsController {

    @Autowired
    private IDeparturedetailsService departuredetailsService;
    @Autowired
    private IklrecordService iklrecordService;

    /**
     * 分页列表查询
     * pageNo     页数
     * @param pageSize 每页条数
     * @param req
     * @return
     */
    @AutoLog(value = "自助机离店明细,分页列表查询")
    @ApiOperation(value = "自助机离店明细")
    @GetMapping(value = "/list")
    public Result<IPage<Departuredetails>> queryPageList(@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                         HttpServletRequest req) {
        Result<IPage<Departuredetails>> result = new Result<IPage<Departuredetails>>();
        Departuredetails departuredetails = new Departuredetails();
        QueryWrapper<Departuredetails> queryWrapper = QueryGenerator.initQueryWrapper(departuredetails, req.getParameterMap());
        Page<Departuredetails> page = new Page<Departuredetails>(1, pageSize);
        IPage<Departuredetails> pageList = departuredetailsService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 按日期查询退房客户明细
     * @param dates 退房日期
     * @return
     */
    @ApiOperation(value = "查询指定日期自助机离店明细")
    @RequestMapping(value = "/Chineserose", method = RequestMethod.GET)
    public Result<?> Chineserose(String dates) {
        if (StringUtils.isEmpty(dates)) {
            return Result.error("请输入必须值!");
        }
        List<Departuredetails> list = departuredetailsService.list(new QueryWrapper<Departuredetails>().eq("tui", dates));
        if (list != null && !list.isEmpty()) {
            return Result.ok(list);
        }
        return Result.error("该日期无人离店");
    }

    /**
     * 跳转路径
     *
     * @param url
     * @return
     */
    @ApiOperation(value = "离店生成跳转二维码")
    @RequestMapping(value = "/Codejump", method = RequestMethod.GET)
    public R Codejump(@RequestParam(name = "url", required = true) String url) {
        if (StringUtils.isEmpty(url)) {
            return R.error("路径不能为空");
        }
        String paths = UuidUtils.getUUID() + ".jpg";
        String destPath = "D:\\img\\" + paths;// 生成的二维码的路径及名称
        try {
            QRCodeUtils.encode(url, null, destPath, true);
            String str = QRCodeUtils.decode(destPath);
        } catch (Exception e) {
            return R.error("生成二维码失败");
        }
        return R.ok(destPath);
    }


    /**
     * 根据用户退房时间及房间号及订单号生成二维码
     *
     * @param roomkey 房间号
     * @return
     */
    @ApiOperation(value = "用户信息二维码")
    @RequestMapping(value = "/messagecode", method = RequestMethod.GET)
    public R messagecode(String roomkey) {
        if (StringUtils.isEmpty(roomkey)) {
            return R.error("房间号不能为空");
        }
        Map map = new HashMap<>();
        try {
            List<Klrecord> list = iklrecordService.querylist(roomkey);//用户信息
            map.put("姓名", list.get(0).getChinesename());
            map.put("性别", list.get(0).getGender());
            map.put("国籍", list.get(0).getNationality());
            map.put("出生年月", list.get(0).getBirth());
            map.put("证件种类", list.get(0).getIdentity());
            map.put("证件号码", list.get(0).getIdnumber());
            map.put("到店日期", list.get(0).getReach().toString());
            map.put("离店日期", list.get(0).getOut().toString());
            map.put("房间号", list.get(0).getRoomkey());
        } catch (Exception e) {
            return R.error("信息为空,无法生成二维码");
        }
        String message = StringToMap.getMapToString(map);
        String paths = UuidUtils.getUUID() + ".jpg";
        String path = "D:\\img\\" + paths;// 生成的二维码的路径及名称
        try {
            QRCodeUtils.encode(message, null, path, true);
            String str = QRCodeUtils.decode(path);
        } catch (Exception e) {
            log.info("messagecode()方法异常:生成二维码失败");
            return R.error("生成用户信息二维码失败");
        }
        return R.ok(path);
    }

    /**
     * 团队号生成二维码
     */
    @ApiOperation(value="团队号生成二维码")
    @RequestMapping(value = "/teamNo", method = RequestMethod.GET)
    public R teamnos(String teamNo) {
        if (StringUtils.isEmpty(teamNo)) {
            return R.error("团队号为空!");
        }
        String path = UuidUtils.getUUID() + ".jpg";
        String Pathss = "D:\\img\\" + path;
        try {
            QRCodeUtils.encode(teamNo, null, Pathss, true);
            String str = QRCodeUtils.decode(Pathss);
        } catch (Exception e) {
            log.info("teamnos()方法异常:生成二维码失败");
            return R.error("生成二维码失败");
        }
        return R.ok(Pathss);
    }











}