package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2019/1/14.
 */

public class SubmitOrderBean {

//    {
//        orderNumber=订单编号（积分、余额、易优币支付时返空字符串）
//        status=状态
//        message= 提示信息
//    }

    private String orderNumber;
    private String status;
    private String message;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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
