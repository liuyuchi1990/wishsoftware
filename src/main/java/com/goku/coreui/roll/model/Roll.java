package com.goku.coreui.roll.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liwenlong on 2018/5/14.
 */
public class Roll {

    private String roll_id;

    //轮播图名称
    private String roll_name;

    private String user_id;

    //0 未发送  1 已发送
    private Integer roll_status;

    //上传图片路径
    private String roll_description;

    //上传图片路径
    private String roll_img_path;

    public String getRoll_id() {
        return roll_id;
    }

    public void setRoll_id(String roll_id) {
        this.roll_id = roll_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getRoll_status() {
        return roll_status;
    }

    public void setRoll_status(Integer roll_status) {
        this.roll_status = roll_status;
    }

    public String getRoll_name() {
        return roll_name;
    }

    public void setRoll_name(String roll_name) {
        this.roll_name = roll_name;
    }

    public String getRoll_img_path() {
        return roll_img_path;
    }

    public void setRoll_img_path(String roll_img_path) {
        this.roll_img_path = roll_img_path;
    }

    public String getRoll_description() {
        return roll_description;
    }

    public void setRoll_description(String roll_description) {
        this.roll_description = roll_description;
    }
    
}
