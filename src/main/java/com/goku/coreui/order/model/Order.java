package com.goku.coreui.order.model;

import java.util.Date;

/**
 * Created by liwenlong on 2018/5/14.
 */
public class Order {

    private String order_id;

    //机器id
    private String device_id;

    //货道集 1,2,3,4,5
    private Date cargo_lane;

    private String user_id;

    private Integer create_time;

    // null 未处理， 1 成功 2失败
    private String order_status;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public Date getCargo_lane() {
        return cargo_lane;
    }

    public void setCargo_lane(Date cargo_lane) {
        this.cargo_lane = cargo_lane;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Integer create_time) {
        this.create_time = create_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
