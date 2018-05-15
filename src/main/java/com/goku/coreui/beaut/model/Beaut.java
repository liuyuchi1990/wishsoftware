package com.goku.coreui.beaut.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liwenlong on 2018/5/15.
 */
public class Beaut {

    private String img_id;

    private String img_name;

    private String img_path;

    private Integer img_status;

    private String user_id;

    private String user_name;

    private Date create_time;

    private Integer fabulous;


    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public Integer getImg_status() {
        return img_status;
    }

    public void setImg_status(Integer img_status) {
        this.img_status = img_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getCreate_time_str(){
        if(this.create_time == null)
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(this.create_time);
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Integer getFabulous() {
        return fabulous;
    }

    public void setFabulous(Integer fabulous) {
        this.fabulous = fabulous;
    }
}
