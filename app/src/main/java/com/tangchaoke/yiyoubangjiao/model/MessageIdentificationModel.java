package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/4/13.
 */

public class MessageIdentificationModel {

//    {
//        messageStatus=是否有未读消息：0没有 1有
//            status=状态
//        message= 提示信息
//    }

    private String messageStatus;
    private String status;
    private String message;

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
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
