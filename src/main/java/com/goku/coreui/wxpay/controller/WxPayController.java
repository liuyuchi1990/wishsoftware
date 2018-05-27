package com.goku.coreui.wxpay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goku.coreui.order.model.Order;
import com.goku.coreui.order.model.OrderInfo;
import com.goku.coreui.order.service.OrderService;
import com.goku.coreui.sys.config.Constants;
import com.goku.coreui.sys.model.PayInfo;
import com.goku.coreui.sys.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/wxpay")
public class WxPayController {
    private static Logger log = Logger.getLogger(WxPayController.class);

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/prepay", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String prePay(@RequestParam(required = false) String code,
                         @RequestParam(required = false) String orderId,
                         HttpServletRequest request) {

        String content = null;
        Map map = new HashMap();
        ObjectMapper mapper = new ObjectMapper();

        boolean result = true;
        String info = "";

        log.error("\n======================================================");
        log.error("code: " + code);

        String openId = WxUtil.getSessionKeyOropenid(code).get("openid").toString();
        if (StringUtils.isBlank(openId)) {
            result = false;
            info = "获取到openId为空";
        } else {
            openId = openId.replace("\"", "").trim();

            String clientIP = CommonUtil.getClientIp(request);

            log.error("openId: " + openId + ", clientIP: " + clientIP);

            String randomNonceStr = RandomUtils.generateMixString(32);
            String prepayId = unifiedOrder(openId, clientIP, randomNonceStr, orderId);

            log.error("prepayId: " + prepayId);

            if (StringUtils.isBlank(prepayId)) {
                result = false;
                info = "出错了，未获取到prepayId";
            } else {
                map.put("prepayId", prepayId);
                map.put("nonceStr", randomNonceStr);
            }
        }

        try {
            map.put("result", result);
            map.put("info", info);
            content = mapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }


    /**
     * 调用统一下单接口
     *
     * @param openId
     */
    private String unifiedOrder(String openId, String clientIP, String randomNonceStr, String orderId) {

        try {

            String url = Constants.URL_UNIFIED_ORDER;

            PayInfo payInfo = createPayInfo(openId, clientIP, randomNonceStr);
            String md5 = getSign(payInfo);
            payInfo.setSign(md5);
            payInfo.setOut_trade_no(orderId);

            log.error("md5 value: " + md5);

            String xml = CommonUtil.payInfoToXML(payInfo);
            xml = xml.replace("__", "_").replace("<![CDATA[1]]>", "1");
            //xml = xml.replace("__", "_").replace("<![CDATA[", "").replace("]]>", "");
            log.error(xml);

            StringBuffer buffer = HttpUtil.httpsRequest(url, "POST", xml);
            log.error("unifiedOrder request return body: \n" + buffer.toString());
            Map<String, String> result = CommonUtil.parseXml(buffer.toString());


            String return_code = result.get("return_code");
            if (StringUtils.isNotBlank(return_code) && return_code.equals("SUCCESS")) {

                String return_msg = result.get("return_msg");
                if (StringUtils.isNotBlank(return_msg) && !return_msg.equals("OK")) {
                    //log.error("统一下单错误！");
                    return "";
                }

                String prepay_Id = result.get("prepay_id");
                return prepay_Id;

            } else {
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private PayInfo createPayInfo(String openId, String clientIP, String randomNonceStr) {

        Date date = new Date();
        String timeStart = TimeUtils.getFormatTime(date, Constants.TIME_FORMAT);
        String timeExpire = TimeUtils.getFormatTime(TimeUtils.addDay(date, Constants.TIME_EXPIRE), Constants.TIME_FORMAT);

        String randomOrderId = CommonUtil.getRandomOrderId();

        PayInfo payInfo = new PayInfo();
        payInfo.setAppid(Constants.APPID);
        payInfo.setMch_id(Constants.MCH_ID);
        payInfo.setDevice_info("WEB");
        payInfo.setNonce_str(randomNonceStr);
        payInfo.setSign_type("MD5");  //默认即为MD5
        payInfo.setBody("JSAPI支付测试");
        payInfo.setAttach("支付测试4luluteam");
        payInfo.setOut_trade_no(randomOrderId);
        payInfo.setTotal_fee(1);
        payInfo.setSpbill_create_ip(clientIP);
        payInfo.setTime_start(timeStart);
        payInfo.setTime_expire(timeExpire);
        payInfo.setNotify_url(Constants.URL_NOTIFY);
        payInfo.setTrade_type("JSAPI");
        payInfo.setLimit_pay("no_credit");
        payInfo.setOpenid(openId);

        return payInfo;
    }

    private String getSign(PayInfo payInfo) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("appid=" + payInfo.getAppid())
                .append("&attach=" + payInfo.getAttach())
                .append("&body=" + payInfo.getBody())
                .append("&device_info=" + payInfo.getDevice_info())
                .append("&limit_pay=" + payInfo.getLimit_pay())
                .append("&mch_id=" + payInfo.getMch_id())
                .append("&nonce_str=" + payInfo.getNonce_str())
                .append("&notify_url=" + payInfo.getNotify_url())
                .append("&openid=" + payInfo.getOpenid())
                .append("&out_trade_no=" + payInfo.getOut_trade_no())
                .append("&sign_type=" + payInfo.getSign_type())
                .append("&spbill_create_ip=" + payInfo.getSpbill_create_ip())
                .append("&time_expire=" + payInfo.getTime_expire())
                .append("&time_start=" + payInfo.getTime_start())
                .append("&total_fee=" + payInfo.getTotal_fee())
                .append("&trade_type=" + payInfo.getTrade_type())
                .append("&key=" + Constants.APP_KEY);

        log.error("排序后的拼接参数：" + sb.toString());

        return CommonUtil.getMD5(sb.toString().trim()).toUpperCase();
    }

    @RequestMapping(value = "/getOrderByUserId", method = RequestMethod.GET)
    public void payCallback(HttpServletRequest request,HttpServletResponse response) {
        log.info("微信回调接口方法 start");
        log.info("微信回调接口 操作逻辑 start");
        String inputLine = "";
        String notityXml = "";
        try {
            while((inputLine = request.getReader().readLine()) != null){
                notityXml += inputLine;
            }
            //关闭流
            request.getReader().close();
            log.info("微信回调内容信息："+notityXml);
            //解析成Map
            Map<String,String> map = WxUtil.doXMLParse(notityXml);
            //判断 支付是否成功
            if("SUCCESS".equals(map.get("result_code"))){
                log.info("微信回调返回是否支付成功：是");
                //获得 返回的商户订单号
                String outTradeNo = map.get("out_trade_no");
                log.info("微信回调返回商户订单号："+outTradeNo);
                //访问DB
                OrderInfo orderInfo = new OrderInfo();
                    //修改支付状态
                    orderInfo.setOrder_status("3");
                    int rs = orderService.edit(orderInfo);
                    //判断 是否更新成功
                    if(rs > 0){
                        log.info("微信回调  订单号："+outTradeNo +",修改状态成功");
                        //封装 返回值
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("<xml>");
                        buffer.append("<return_code>SUCCESS</return_code>");
                        buffer.append("<return_msg>OK</return_msg>");
                        buffer.append("<xml>");

                        //给微信服务器返回 成功标示 否则会一直询问 咱们服务器 是否回调成功
                        PrintWriter writer = response.getWriter();
                        //返回
                        writer.print(buffer.toString());
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
