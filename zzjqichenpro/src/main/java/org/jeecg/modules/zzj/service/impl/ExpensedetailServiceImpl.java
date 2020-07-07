package org.jeecg.modules.zzj.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.zzj.entity.*;
import org.jeecg.modules.zzj.mapper.ExpensedetailMapper;
import org.jeecg.modules.zzj.service.IExpensedetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 退房客户消费明细
 * @Author: jeecg-boot
 * @Date:   2019-11-18
 * @Version: V1.0
 */
@Service
@DS("master")
public class ExpensedetailServiceImpl extends ServiceImpl<ExpensedetailMapper, Expensedetail> implements IExpensedetailService {

    @Autowired
    private ExpensedetailMapper ExpensedetailMapper;


    @Override
    public boolean insertionlist(List<Klbill> list) {
        boolean flag=ExpensedetailMapper.insertionlist(list);
        return flag;

    }

    @Override
    public void inserlists(List<Klrecord> lists) {
        ExpensedetailMapper.inserlists(lists);
    }

    @Override
    public void savecklist(Departuredetails departuredetails) {
        ExpensedetailMapper.savecklist(departuredetails);
    }


}
