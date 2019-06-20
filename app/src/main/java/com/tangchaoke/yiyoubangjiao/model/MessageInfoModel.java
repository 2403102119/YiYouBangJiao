package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/3/22.
 */

public class MessageInfoModel {

//    {
//　　"model":{
//　　　　.............
//　　},
//　　"status":1,
//　　"message":"查询该信息详情成功!"
//    }

    private MessageInfoModelModel model;
    private String status;
    private String message;

    public MessageInfoModelModel getModel() {
        return model;
    }

    public void setModel(MessageInfoModelModel model) {
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

    public class MessageInfoModelModel{

//        "oid":"006",
//　　　　"title":"nizaina",
//　　　　"content":"我不知道",
//　　　　"sendingTime":"2018-03-22 15:16:32",
//　　　　"status":"1"

        private String oid;
        private String title;
        private String content;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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
