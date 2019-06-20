package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */

public class LoginModel {

//    {
//        model={
//                ............
//              }
//        status=状态
//        message=提示信息
//    }

    private LoginModelModel model;
    private String status;
    private String message;

    public LoginModelModel getModel() {
        return model;
    }

    public void setModel(LoginModelModel model) {
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

    public class LoginModelModel {

//        "respondent":"是否认证答题者:0未认证 1已认证",
//        "isCoach":"是否是教练:0不是 1大师 2俱乐部教练",
//        "nickName":"昵称",
//        "token":"用户token",
//        "studentIdCardStatus":"学生证认证状态:0认证中 1认证失败 2认证成功 3未认证",
//        "oid":"用户oid",
//        "pushSwitch":"推送开关:0开启 1关闭",
//        "account":"帐号",
//        "grade":"等级",
//        "userDiplomaStatus":"学历认证状态:0认证中 1认证失败 2认证成功 3未认证",
//        "isClub":"是否有俱乐部：0没有 1俱乐部学生 2俱乐部教练 3俱乐部教师",
//        "head":"头像",
//        "respondentAgreement":"答题者协议同意状态:0未同意 1已同意"
//        "isSchool":"是否有学校：0没有 1学校学生 2学校老师"

//         "userStatus":"微信登录-----用户是否绑定手机号:0未绑定 1已绑定"

//        "setPassword":"绑定手机号-----是否是新手机号 0不需要（旧手机号） 1需要（新手机号）"

        private String respondent;
        private String isCoach;
        private String nickName;
        private String token;
        private String studentIdCardStatus;
        private String oid;
        private String pushSwitch;
        private String account;
        private String grade;
        private String userDiplomaStatus;
        private String isClub;
        private String head;
        private String respondentAgreement;
        private String isSchool;
        private String userStatus;
        private String setPassword;

        public String getIsSchool() {
            return isSchool;
        }

        public void setIsSchool(String isSchool) {
            this.isSchool = isSchool;
        }

        public String getRespondent() {
            return respondent;
        }

        public void setRespondent(String respondent) {
            this.respondent = respondent;
        }

        public String getIsCoach() {
            return isCoach;
        }

        public void setIsCoach(String isCoach) {
            this.isCoach = isCoach;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getStudentIdCardStatus() {
            return studentIdCardStatus;
        }

        public void setStudentIdCardStatus(String studentIdCardStatus) {
            this.studentIdCardStatus = studentIdCardStatus;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getPushSwitch() {
            return pushSwitch;
        }

        public void setPushSwitch(String pushSwitch) {
            this.pushSwitch = pushSwitch;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getUserDiplomaStatus() {
            return userDiplomaStatus;
        }

        public void setUserDiplomaStatus(String userDiplomaStatus) {
            this.userDiplomaStatus = userDiplomaStatus;
        }

        public String getIsClub() {
            return isClub;
        }

        public void setIsClub(String isClub) {
            this.isClub = isClub;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getRespondentAgreement() {
            return respondentAgreement;
        }

        public void setRespondentAgreement(String respondentAgreement) {
            this.respondentAgreement = respondentAgreement;
        }

        public String getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(String userStatus) {
            this.userStatus = userStatus;
        }

        public String getSetPassword() {
            return setPassword;
        }

        public void setSetPassword(String setPassword) {
            this.setPassword = setPassword;
        }
    }

}
