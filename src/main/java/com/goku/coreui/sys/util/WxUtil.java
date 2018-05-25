package com.goku.coreui.sys.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.goku.coreui.sys.config.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    /**
     * StringUtils工具类方法
     * 获取一定长度的随机字符串，范围0-9，a-z
     * @param length：指定字符串长度
     * @return 一定长度的随机字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    /**
     * IpUtils工具类方法
     * 获取真实的ip地址
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }
}
