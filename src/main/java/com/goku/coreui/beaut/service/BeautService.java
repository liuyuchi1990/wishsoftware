package com.goku.coreui.beaut.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goku.coreui.beaut.mapper.BeautMapper;
import com.goku.coreui.beaut.model.Beaut;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by liwenlong on 2018/5/15.
 */
@Service
public class BeautService {

    @Autowired
    BeautMapper beautMapper;

    public PageInfo queryPage(String user_name,String status, Date begindate, Date enddate,int pageindex, int pagenum){
        PageHelper.startPage(pageindex, pagenum);
        List<Beaut> list = beautMapper.queryPage(user_name,status,begindate,enddate);
        PageInfo page = new PageInfo(list);
        return page;
    }

    public int delete(String ids){
        return beautMapper.delete(ids.split(","));
    }

    public int add(Beaut beaut){
        return beautMapper.add(beaut);
    }

    public int delImgStatus(String ids){
        return beautMapper.delImgStatus(ids.split(","));
    }

    public int approvalImgStatus(String ids){
        return beautMapper.approvalImgStatus(ids.split(","));
    }

    public int setFabulous(String type,String id){
        return beautMapper.setFabulous(type,id);
    }

}
