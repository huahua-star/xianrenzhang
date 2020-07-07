package org.jeecg.modules.zzj.service;

import org.jeecg.modules.zzj.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 退房客户消费明细
 * @Author: jeecg-boot
 * @Date:   2019-11-18
 * @Version: V1.0
 */
public interface IExpensedetailService extends IService<Expensedetail> {

    boolean insertionlist(List<Klbill> list);

    void inserlists(List<Klrecord> lists);

    void savecklist(Departuredetails departuredetails);
}
