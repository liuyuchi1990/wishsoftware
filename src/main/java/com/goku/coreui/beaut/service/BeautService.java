package com.goku.coreui.beaut.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goku.coreui.beaut.mapper.BeautMapper;
import com.goku.coreui.beaut.model.Beaut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by liwenlong on 2018/5/15.
 */
@Service
public class BeautService {

    @Autowired
    BeautMapper beautMapper;

    public PageInfo queryPage(int pageindex, int pagenum){
        PageHelper.startPage(pageindex, pagenum);
        List<Beaut> list = beautMapper.queryPage();
        PageInfo page = new PageInfo(list);
        return page;
    }

    public int delete(String ids){
        return beautMapper.delete(ids.split(","));
    }
}
