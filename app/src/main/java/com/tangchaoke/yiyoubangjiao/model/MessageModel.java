package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class MessageModel {

//    {
//　　"model":[
//　　　　{
//　　　　　　.............
//　　　　}
//　　],
//　　"status":1,
//　　"message":"信息列表查询成功!"
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

//        "oid":"005",
//　　　　　　"status":"0",
//　　　　　　"message":{
//　　　　　　　.............
//　　　　　　}

        private String oid;
        private String status;
        private MessageModelMessageModel message;

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

        public MessageModelMessageModel getMessage() {
            return message;
        }

        public void setMessage(MessageModelMessageModel message) {
            this.message = message;
        }

        public class MessageModelMessageModel{

//            　"oid":"005",
//　　　　　　　　"title":"在不在",
//　　　　　　　　"sendingTime":"2018-03-22 15:16:04",
//　　　　　　　　"status":"0"

            private String oid;
            private String title;
            private String sendingTime;
            private String status;

            public String getOid() {
                return oid;
            }

            public void setOid(String oid) {
                this.oid = oid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSendingTime() {
                return sendingTime;
            }

            public void setSendingTime(String sendingTime) {
                this.sendingTime = sendingTime;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }

    }

}
