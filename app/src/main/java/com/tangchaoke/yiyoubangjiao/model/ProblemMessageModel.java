package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ProblemMessageModel {

//    {
//        model={
//               ..............
//        }
//        status=状态
//        message= 提示信息
//    }

    private ProblemMessageModelModel model;
    private String status;
    private String message;

    public ProblemMessageModelModel getModel() {
        return model;
    }

    public void setModel(ProblemMessageModelModel model) {
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

    public class ProblemMessageModelModel {

//        userOid=发题人oid
//                userNickName=发题人昵称
//                respondentOid=答题人oid
//                respondentNickName=答题人昵称

        private String userOid;
        private String userNickName;
        private String respondentOid;
        private String respondentNickName;

        public String getUserOid() {
            return userOid;
        }

        public void setUserOid(String userOid) {
            this.userOid = userOid;
        }

        public String getUserNickName() {
            return userNickName;
        }

        public void setUserNickName(String userNickName) {
            this.userNickName = userNickName;
        }

        public String getRespondentOid() {
            return respondentOid;
        }

        public void setRespondentOid(String respondentOid) {
            this.respondentOid = respondentOid;
        }

        public String getRespondentNickName() {
            return respondentNickName;
        }

        public void setRespondentNickName(String respondentNickName) {
            this.respondentNickName = respondentNickName;
        }
    }

}
