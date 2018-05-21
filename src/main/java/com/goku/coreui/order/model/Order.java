package com.goku.coreui.order.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liwenlong on 2018/5/14.
 */
public class Order {

    private String order_id;

    //机器id
    private String device_id;

    private String device_name;

    private String device_address;

    //货道集 1,2,3,4,5
    private String cargo_lane;

    private String user_id;

    private String user_name;

    private Date create_time;

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

    public String getCargo_lane() {
        return cargo_lane;
    }

    public void setCargo_lane(String cargo_lane) {
        this.cargo_lane = cargo_lane;
    }

    public String getUser_id() {
        return user_name;
    }

    public void setUser_id(String user_id) {
        this.user_name = user_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_address() {
        return device_address;
    }

    public void setDevice_address(String device_address) {
        this.device_address = device_address;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getCreate_time_str(){
        if(this.create_time == null)
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(this.create_time);
    }
}
