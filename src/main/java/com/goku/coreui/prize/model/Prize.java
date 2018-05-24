package com.goku.coreui.prize.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liwenlong on 2018/5/14.
 */
public class Prize {

    private String prize_id;

    //奖品名称
    private String prize_name;

    //发货地址
    private String send_address;

    //发货时间
    private Date send_time;

    private String user_id;

    //0 未发送  1 已发送
    private Integer prize_status;

    private String prize_qr;

    private String user_name;

    //上传图片路径
    private String prize_img_path;

    public String getPrize_id() {
        return prize_id;
    }

    public void setPrize_id(String prize_id) {
        this.prize_id = prize_id;
    }

    public String getSend_address() {
        return send_address;
    }

    public void setSend_address(String send_address) {
        this.send_address = send_address;
    }

    public Date getSend_time() {
        return send_time;
    }

    public String getSend_time_str(){
        if(this.send_time == null)
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(this.send_time);
    }

    public void setSend_time(Date send_time) {
        this.send_time = send_time;
    }

    public void setSend_time_str(String send_time) throws ParseException {
        if(!send_time.equals("") && send_time != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = sdf.parse(send_time);
            this.setSend_time(time);
        }
    }



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getPrize_status() {
        return prize_status;
    }

    public void setPrize_status(Integer prize_status) {
        this.prize_status = prize_status;
    }

    public String getPrize_qr() {
        return prize_qr;
    }

    public void setPrize_qr(String prize_qr) {
        this.prize_qr = prize_qr;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPrize_name() {
        return prize_name;
    }

    public void setPrize_name(String prize_name) {
        this.prize_name = prize_name;
    }

    public String getPrize_img_path() {
        return prize_img_path;
    }

    public void setPrize_img_path(String prize_img_path) {
        this.prize_img_path = prize_img_path;
    }
}
