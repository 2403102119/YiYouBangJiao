package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/4/2.
 */

public class AlipayRechargeModel {

//    {
//　　"data":"alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018012202025976&biz_content=%7B%22body%22%3A%22%E8%AF%BE%E7%A8%8B%E8%B4%AD%E4%B9%B0%22%2C%22out_trade_no%22%3A%2220180402112747795001%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E8%AF%BE%E7%A8%8B%E8%B4%AD%E4%B9%B0%22%2C%22timeout_express%22%3A%2220m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay¬ify_url=http%3A%2F%2F111.231.75.62%2FNewone%2Faction%2FpayAlipay%2FnotifyPage&sign=r%2BfNpYQ1h9G38mMhp%2Fibm76CysJ5Yo40VBu%2Fv6SJzUldnlKuGM7PvvvOVuAihQPplDt%2BDwdjId4UdPz%2BTqd15WrUBOw145D200VoVerJwkrIyug1i%2B7pjikQFurPB5CYXcaNkZs6Wg%2BSLkjTmnoE%2Btw1Ajc%2FnLGm9mu8zgMpEk0QexgbGVC2iv%2B3CCOTXbcXJN0xsGD%2FAr0gqdnbyddTV1588HnZp7SBZ9PpHLsQtSVxbLNU%2BKi95CoO927SeXdcicxg%2Bw%2BNHMrfh3XE6IwvqgdXbTn2amJvrCH5%2FHHDn3MxTFJvzEz7eLYcoAfHmoh6malvK6AKqN39sD6QO8RKbQ%3D%3D&sign_type=RSA2×tamp=2018-04-02+11%3A27%3A48&version=1.0&sign=r%2BfNpYQ1h9G38mMhp%2Fibm76CysJ5Yo40VBu%2Fv6SJzUldnlKuGM7PvvvOVuAihQPplDt%2BDwdjId4UdPz%2BTqd15WrUBOw145D200VoVerJwkrIyug1i%2B7pjikQFurPB5CYXcaNkZs6Wg%2BSLkjTmnoE%2Btw1Ajc%2FnLGm9mu8zgMpEk0QexgbGVC2iv%2B3CCOTXbcXJN0xsGD%2FAr0gqdnbyddTV1588HnZp7SBZ9PpHLsQtSVxbLNU%2BKi95CoO927SeXdcicxg%2Bw%2BNHMrfh3XE6IwvqgdXbTn2amJvrCH5%2FHHDn3MxTFJvzEz7eLYcoAfHmoh6malvK6AKqN39sD6QO8RKbQ%3D%3D",
//　　"status":"1",
//　　"message":"获取成功"
//    }

    private String data;
    private String status;
    private String message;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
