package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class HeadByOidModel {

//    {
//　　"model":[
//　　　　{
//　　　　　　.........
//　　　　}
//　　],
//　　"status":1,
//　　"message":"数据返回成功！"
//    }

    private List<HeadByOidModelModel> model;
    private String status;
    private String message;

    public List<HeadByOidModelModel> getModel() {
        return model;
    }

    public void setModel(List<HeadByOidModelModel> model) {
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

    public class HeadByOidModelModel {

//        "nickName":"152****3333",
//　　　　　　"oid":"i7scb3lqv1",
//　　　　　　"head":"uploadImage/uploadImage/thum/20180415181808274001.png"

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
