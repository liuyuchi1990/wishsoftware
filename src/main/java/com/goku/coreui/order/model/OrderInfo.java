package com.goku.coreui.order.model;

public class OrderInfo {
    private String order_id;

    private String device_id;

    private Cargo[] cargo_lane;

    private String order_status;

    private Integer Integral;

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

    public Cargo[] getCargo_lane() {
        return cargo_lane;
    }

    public void setCargo_lane(Cargo[] cargo_lane) {
        this.cargo_lane = cargo_lane;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public Integer getIntegral() {
        return Integral;
    }

    public void setIntegral(Integer integral) {
        Integral = integral;
    }
}
