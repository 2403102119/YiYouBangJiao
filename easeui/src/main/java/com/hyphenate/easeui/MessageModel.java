package com.hyphenate.easeui;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class MessageModel {

//    {
//　　"model":[
//　　　　{
//　　　　　　...............
//　　　　},
//　　],
//　　"status":1,
//　　"message":"数据返回成功！"
//    }

    private List<MessageModelModel> model;
    private String status;
    private String message;

    public List<MessageModelModel> getModel() {
        return model;
    }

    public void setModel(List<MessageModelModel> model) {
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

    public class MessageModelModel{

//        "nickName":"冷落哦监控",
//　　　　　　"oid":"I7SAPVTQL1",
//　　　　　　"head":""

        private String nickName;
        private String oid;
        private String head;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }
    }

}
