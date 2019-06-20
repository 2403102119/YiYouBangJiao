package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/4/2.
 */

public class PaymentModel {

//    {
//    "oid":"I7TXH70IT1",
//            "isFree":"1"
//　　"total":1,
//　　"orderNumber":"20180402110047122001",
//　　"status":1,
//　　"message":"购买课程成功!"
//    }

    private String oid;
    private String isFree;
    private String total;
    private String orderNumber;
    private String status;
    private String message;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getIsFree() {
        return isFree;
    }

    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

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
