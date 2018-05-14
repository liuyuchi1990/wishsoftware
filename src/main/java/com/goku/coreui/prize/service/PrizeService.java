package com.goku.coreui.prize.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goku.coreui.prize.mapper.PrizeMapper;
import com.goku.coreui.prize.model.Prize;
import com.goku.coreui.sys.model.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by liwenlong on 2018/5/14.
 */
@Service
public class PrizeService {

    @Autowired
    PrizeMapper prizeMapper;

    public int insert(Prize prize){
        prize.setPrize_id(UUID.randomUUID().toString().replaceAll("-", ""));
        return prizeMapper.insert(prize);
    }

    public int edit(Prize prize){
        return prizeMapper.edit(prize);
    }

    public int delete(String ids){
        return prizeMapper.delete(ids.split(","));
    }

    public Prize queryById(String prize_id){
        return prizeMapper.queryById(prize_id);
    }

    public PageInfo queryPage(String user_name, Date begindate, Date enddate, String prize_status, int pageindex, int pagenum){
        PageHelper.startPage(pageindex, pagenum);
        List<Prize> list = prizeMapper.queryPage(user_name,begindate,enddate,prize_status);
        PageInfo page = new PageInfo(list);
        return page;
    }
}
