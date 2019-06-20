package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public class FindTeacherModel {

//    {
//　　"model":[
//　　　　{
//　　　　　...........
//　　　　}
//　　],
//　　"status":1,
//　　"message":"获取易优教师列表成功!"
//    }

    private List<FindTeacherModelModel> model;
    private String status;
    private String message;

    public List<FindTeacherModelModel> getModel() {
        return model;
    }

    public void setModel(List<FindTeacherModelModel> model) {
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

    public class FindTeacherModelModel {

//        　"educationalScope":"高一,英语",
//　　　　　　"nickName":"",
//　　　　　　"oid":"I7R2K60AX1",
//　　　　　　"characteristicsofEducation":[
//　　　　　　　　{
//　　　　　　　　　　...........
//　　　　　　　　}
//　　　　　　],
//　　　　　　"head":"",
//　　　　　　"soldHours":"0",
//　　　　　　"startPrice":0
//        　"substituteTeacher":"1",
//                "tutor":"1",
//        seniority
//        sex

        private String educationalScope;
        private String nickName;
        private String oid;
        private List<FindTeacherModelCharacterModel> characteristicsofEducation;
        private String head;
        private String soldHours;
        private String startPrice;
        private String substituteTeacher;
        private String tutor;
        private String seniority;
        private String sex;

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSeniority() {
            return seniority;
        }

        public void setSeniority(String seniority) {
            this.seniority = seniority;
        }

        public String getSubstituteTeacher() {
            return substituteTeacher;
        }

        public void setSubstituteTeacher(String substituteTeacher) {
            this.substituteTeacher = substituteTeacher;
        }

        public String getTutor() {
            return tutor;
        }

        public void setTutor(String tutor) {
            this.tutor = tutor;
        }

        public String getEducationalScope() {
            return educationalScope;
        }

        public void setEducationalScope(String educationalScope) {
            this.educationalScope = educationalScope;
        }

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

        public List<FindTeacherModelCharacterModel> getCharacteristicsofEducation() {
            return characteristicsofEducation;
        }

        public void setCharacteristicsofEducation(List<FindTeacherModelCharacterModel> characteristicsofEducation) {
            this.characteristicsofEducation = characteristicsofEducation;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getSoldHours() {
            return soldHours;
        }

        public void setSoldHours(String soldHours) {
            this.soldHours = soldHours;
        }

        public String getStartPrice() {
            return startPrice;
        }

        public void setStartPrice(String startPrice) {
            this.startPrice = startPrice;
        }

        public class FindTeacherModelCharacterModel {

//            "characteristic":"本科学历"

            private String characteristic;

            public String getCharacteristic() {
                return characteristic;
            }

            public void setCharacteristic(String characteristic) {
                this.characteristic = characteristic;
            }
        }

    }

}
