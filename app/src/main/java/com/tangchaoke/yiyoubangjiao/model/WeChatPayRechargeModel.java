package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/4/13.
 */

public class WeChatPayRechargeModel {

//    {
//　　"app_id":"wx74bb6ff88fc1c2b6",
//　　"partner_id":"1501738801",
//　　"nonce_str":"wbpLfWWCTIP3Rwqg",
//　　"sign":"9CE3AE5DCF424A76EA084F3051587B80",
//　　"prepay_id":"wx131030426555564c64eccd433518393823",
//　　"package_value":"Sign=WXPay",
//　　"time_stamp":1523586637,
//　　"status":"1",
//　　"message":"获取成功"
//    }

    private String app_id;
    private String partner_id;
    private String nonce_str;
    private String sign;
    private String prepay_id;
    private String package_value;
    private String time_stamp;
    private String status;
    private String message;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getPackage_value() {
        return package_value;
    }

    public void setPackage_value(String package_value) {
        this.package_value = package_value;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
