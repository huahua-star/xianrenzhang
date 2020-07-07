package org.jeecg.modules.zzj.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.modules.zzj.entity.group;
import org.jeecg.modules.zzj.service.IKlprofileService;
import org.jeecg.modules.zzj.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 根据团队名入住
 */
@Api(tags = "团队入住")
@RestController
@RequestMapping("/zzj/TeamcheckinController")
public class TeamcheckinController {

    @Autowired
    public IKlprofileService iklrecordService;

    /**
     * 查询入住团队订单信息
     * @param nameid 团队号
     * @return
     */
    @ApiOperation(value = "查询团队入住信息")
    @RequestMapping(value = "/team", method = RequestMethod.GET)
    public R team(String nameid) {
       List<group>list= iklrecordService.queryteam(nameid);
        System.out.println(list);
        return null;

    }


}
