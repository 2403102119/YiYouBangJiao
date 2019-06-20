package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/4/24.
 */

public class FreeReleaseModel {

//    {
//        "model":{
//          ........
// },
//            "status":1,
//            "message":"判断成功!"
//    }

    private String isFree;
    private String price;
    private String status;
    private String message;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIsFree() {
        return isFree;
    }

    public void setIsFree(String isFree) {
        this.isFree = isFree;
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
