package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class TeacherInfoModel {

//    {
//　　"model":{
//　　　.......
//　　},
//　　"status":1,
//　　"message":"查询该教师详情成功!"
//    }

    private TeacherInfoModelModel model;
    private String status;
    private String message;

    public TeacherInfoModelModel getModel() {
        return model;
    }

    public void setModel(TeacherInfoModelModel model) {
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

    public class TeacherInfoModelModel {

//        　"teachingExperience":"0",
//　　　　"successCase":"0",
//　　　　"schoolName":null,
//　　　　"characteristicsofEducation":[
//　　　　　　{
//　　　　　　　　..........
//　　　　　　}
//　　　　],
//　　　　"endTime":null,
//　　　　"startPrice":0,
//　　　　"soldHours":"0",
//　　　　"educationalScope":"高三,数学",
//　　　　"startTime":null,
//　　　　"nickName":"11111111",
//　　　　"oid":"I7R2C9D881",
//　　　　"comment":[
//　　　　　　{
//　　　　　　　........
//　　　　　　}
//　　　　],
//　　　　"head":"",
//　　　　"userMajor":null
//        status
//        seniority
//        highSchool
//        middleSchool
//        primarySchool

        private String teachingExperience;
        private String successCase;
        private String schoolName;
        private List<TeacherInfoModelCharacterModel> characteristicsofEducation;
        private String endTime;
        private String startPrice;
        private String soldHours;
        private String educationalScope;
        private String startTime;
        private String nickName;
        private String oid;
        private String head;
        private String userMajor;
        private String grade;
        private String status;
        private String seniority;
        private List<TeacherInfoModelHighSchoolModel> highSchool;
        private List<TeacherInfoModelMiddleSchoolModel> middleSchool;
        private List<TeacherInfoModelPrimarySchoolModel> primarySchool;

        public List<TeacherInfoModelHighSchoolModel> getHighSchool() {
            return highSchool;
        }

        public void setHighSchool(List<TeacherInfoModelHighSchoolModel> highSchool) {
            this.highSchool = highSchool;
        }

        public List<TeacherInfoModelMiddleSchoolModel> getMiddleSchool() {
            return middleSchool;
        }

        public void setMiddleSchool(List<TeacherInfoModelMiddleSchoolModel> middleSchool) {
            this.middleSchool = middleSchool;
        }

        public List<TeacherInfoModelPrimarySchoolModel> getPrimarySchool() {
            return primarySchool;
        }

        public void setPrimarySchool(List<TeacherInfoModelPrimarySchoolModel> primarySchool) {
            this.primarySchool = primarySchool;
        }

        public String getSeniority() {
            return seniority;
        }

        public void setSeniority(String seniority) {
            this.seniority = seniority;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getTeachingExperience() {
            return teachingExperience;
        }

        public void setTeachingExperience(String teachingExperience) {
            this.teachingExperience = teachingExperience;
        }

        public String getSuccessCase() {
            return successCase;
        }

        public void setSuccessCase(String successCase) {
            this.successCase = successCase;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public List<TeacherInfoModelCharacterModel> getCharacteristicsofEducation() {
            return characteristicsofEducation;
        }

        public void setCharacteristicsofEducation(List<TeacherInfoModelCharacterModel> characteristicsofEducation) {
            this.characteristicsofEducation = characteristicsofEducation;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStartPrice() {
            return startPrice;
        }

        public void setStartPrice(String startPrice) {
            this.startPrice = startPrice;
        }

        public String getSoldHours() {
            return soldHours;
        }

        public void setSoldHours(String soldHours) {
            this.soldHours = soldHours;
        }

        public String getEducationalScope() {
            return educationalScope;
        }

        public void setEducationalScope(String educationalScope) {
            this.educationalScope = educationalScope;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
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

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getUserMajor() {
            return userMajor;
        }

        public void setUserMajor(String userMajor) {
            this.userMajor = userMajor;
        }

        public class TeacherInfoModelCharacterModel {

//            "characteristic":"本科学历"

            private String characteristic;

            public String getCharacteristic() {
                return characteristic;
            }

            public void setCharacteristic(String characteristic) {
                this.characteristic = characteristic;
            }

        }

        public class TeacherInfoModelHighSchoolModel {

            //                          "rangeke":"数学,英语",
            //        　　　　　　　　"rangenian":"高三"

            private String rangeke;
            private String rangenian;

            public String getRangeke() {
                return rangeke;
            }

            public void setRangeke(String rangeke) {
                this.rangeke = rangeke;
            }

            public String getRangenian() {
                return rangenian;
            }

            public void setRangenian(String rangenian) {
                this.rangenian = rangenian;
            }
        }

        public class TeacherInfoModelMiddleSchoolModel {
            private String rangeke;
            private String rangenian;

            public String getRangeke() {
                return rangeke;
            }

            public void setRangeke(String rangeke) {
                this.rangeke = rangeke;
            }

            public String getRangenian() {
                return rangenian;
            }

            public void setRangenian(String rangenian) {
                this.rangenian = rangenian;
            }

        }

        public class TeacherInfoModelPrimarySchoolModel {
            private String rangeke;
            private String rangenian;

            public String getRangeke() {
                return rangeke;
            }

            public void setRangeke(String rangeke) {
                this.rangeke = rangeke;
            }

            public String getRangenian() {
                return rangenian;
            }

            public void setRangenian(String rangenian) {
                this.rangenian = rangenian;
            }

        }

    }

}
