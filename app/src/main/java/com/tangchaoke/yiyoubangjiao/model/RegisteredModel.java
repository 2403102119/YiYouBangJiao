package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/3/19.
 */

public class RegisteredModel {

//    {
//　　"token":"68278c9b-239c-4131-91ee-34e7de2b12b2",
//　　"status":1,
//　　"message":"注册成功!"
//    }

    private String oid;
    private String status;
    private String message;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
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
