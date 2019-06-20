package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/4/25.
 */

public class NoReleaseProblemModel {

//    {
//        state=是否有未解答问题:0没有 1有
//            status=状态
//        message= 提示信息
//    }

    private String state;
    private String status;
    private String message;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
