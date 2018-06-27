package com.goku.coreui.sys.config;

public class Constants {
    /**
     * 微信APPID
     */
    public static final String APPID = "wx7dee141dcedc0421";

    /**
     * 微信SERCRET
     */
    public static final String SERCRET = "ee702e5c923782990236ce7f2decdff4";

    /**
     * 微信单次货物购买积分
     */
    public static final Integer Integral = 20;


    /**
     * 微信单次货物购买价格
     */
    public static final Double PRICE = 0.01;

    //签名方式，固定值
    public static final String SIGNTYPE = "MD5";
    //交易类型，小程序支付的固定值为JSAPI
    public static final String TRADETYPE = "JSAPI";

    public static final String APP_KEY = "xyj185568014781qaz2wsx3edc4rfv5t";

    public static final String MCH_ID = "1504738871";  //商户号

    public static final String URL_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static final String DOMAIN = "https://www.xingyuanji.com";

    public static final String URL_NOTIFY = Constants.DOMAIN + "/api/wxpay/payCallback";

    public static final String TIME_FORMAT = "yyyyMMddHHmmss";

    public static final int TIME_EXPIRE = 2;  //单位是day

    public static final String QR_FILE_PATH = "C:/apache-tomcat-8.5.31/webapps/imgs/qr/";

    /**
     * @Fields EN_CODING : 定义编码格式
     */
    public static final String EN_CODING = "UTF-8";

    public static final String CONTENT_DISPOSITION = "Content-disposition";

    public static final String ATTACHMENT_FILENAME = "attachment; filename=\"%s\"";

    /**
     * iso-8859-1编码格式
     */
    public static final String EN_CODING_ISO = "iso-8859-1";

    /**
     * gbk编码格式
     */
    public static final String EN_CODING_GBK = "gbk";

    public static final String FILE_NAME = "fileName";
}
