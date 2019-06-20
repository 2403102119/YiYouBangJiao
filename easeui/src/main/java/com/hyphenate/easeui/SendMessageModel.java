package com.hyphenate.easeui;

/**
 * Created by Administrator on 2018/4/25.
 */

public class SendMessageModel {

//    {
//        state=是否可以继续解答问题:0不可以 1可以
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
