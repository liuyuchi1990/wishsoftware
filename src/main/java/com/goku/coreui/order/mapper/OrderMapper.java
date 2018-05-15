package com.goku.coreui.order.mapper;

import com.goku.coreui.order.model.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by richard on 2018/5/14.
 */
public interface OrderMapper {
    int insert(Order order);

    int edit(Order order);

    int delete(String[] ids);

    Order queryById(@Param("order_id") String order_id);

    List<Order> queryPage(@Param("user_name") String user_name, @Param("begindate") Date begindate, @Param("enddate") Date enddate, @Param("order_status") String order_status);
}
