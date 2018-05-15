package com.goku.coreui.order.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goku.coreui.order.mapper.OrderMapper;
import com.goku.coreui.order.model.Order;
import com.goku.coreui.prize.model.Prize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by richard on 2018/5/14.
 */
@Service
public class OrderService {
    @Autowired
    OrderMapper orderMapper;

    public int insert(Order order){
        order.setOrder_id(UUID.randomUUID().toString().replaceAll("-", ""));
        return orderMapper.insert(order);
    }

    public int edit(Order order){
        return orderMapper.edit(order);
    }

    public int delete(String ids){
        return orderMapper.delete(ids.split(","));
    }

    public Order queryById(String order_id){
        return orderMapper.queryById(order_id);
    }

    public PageInfo queryPage(String user_name, Date begindate, Date enddate, String order_status, int pageindex, int pagenum){
        PageHelper.startPage(pageindex, pagenum);
        List<Order> list = orderMapper.queryPage(user_name,begindate,enddate,order_status);
        PageInfo page = new PageInfo(list);
        return page;
    }
}
