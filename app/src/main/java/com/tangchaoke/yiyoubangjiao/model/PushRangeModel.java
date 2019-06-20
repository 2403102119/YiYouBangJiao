package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/8/18.
 */

public class PushRangeModel {

//    {
//        "model":"1",
//            "status":1,
//            "message":"易优教师认证提交成功!"
//    }

    private String model;
    private String status;
    private String message;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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
