package com.goku.coreui.sys.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.goku.coreui.sys.config.Constants;

import java.util.HashMap;
import java.util.Map;

public class WxUtil {
    public static String getWxAccessToken(){
        Map<String,String> requestUrlParam = new HashMap<String,String>();
        requestUrlParam.put("appid", Constants.APPID);  //开发者设置中的appId
        requestUrlParam.put("secret", Constants.SERCRET); //开发者设置中的appSecret
        //requestUrlParam.put("js_code", wxCode); //小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "client_credential");    //默认参数

        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        JSONObject jsonObject = JSON.parseObject(HttpUtil.sendPost("https://api.weixin.qq.com/cgi-bin/token", requestUrlParam));
        return jsonObject.get("access_token").toString();
    }

    public static Map<String,Object> getSessionKeyOropenid(String wxCode){
        Map<String,String> requestUrlParam = new HashMap<String,String>();
        Map<String,Object> map = new HashMap<String,Object>();
        requestUrlParam.put("appid", Constants.APPID);  //开发者设置中的appId
        requestUrlParam.put("secret", Constants.SERCRET); //开发者设置中的appSecret
        requestUrlParam.put("js_code", wxCode); //小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "client_credential");    //默认参数

        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        JSONObject jsonObject = JSON.parseObject(HttpUtil.sendPost("https://api.weixin.qq.com/sns/jscode2session", requestUrlParam));
        map.put("openid",jsonObject.get("openid"));
        map.put("session_key",jsonObject.get("session_key"));
        return map;
    }
}
