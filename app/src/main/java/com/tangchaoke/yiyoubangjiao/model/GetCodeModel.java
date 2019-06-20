package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/3/19.
 */

public class GetCodeModel {

//    {
//　　"status":"1",
//　　"message":"验证码发送成功!5763"
//    }

    private String status;
    private String message;

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
