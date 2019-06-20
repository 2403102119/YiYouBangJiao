package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/3/20.
 */

public class MineDataModel {

//    {
//　　"model":{
//　　　　.......
//　　},
//　　"status":1,
//　　"message":"资料修改成功!"
//    }

    private MineDataModelModel model;
    private String status;
    private String message;

    public MineDataModelModel getModel() {
        return model;
    }

    public void setModel(MineDataModelModel model) {
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

    public class MineDataModelModel{

//        "oid":"I7R9JZ5NT1",
//　　　　"nickName":"我滴天呐吖"

        private String oid;
        private String nickName;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }

}
