package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/4/7.
 */

public class AuditFailurePromptModel {

//    {
//　　"model":{
//　　　　...............
//　　},
//　　"status":1,
//　　"message":"认证信息返回成功！"
//    }

    private AuditFailurePromptModelModel model;
    private String status;
    private String message;

    public AuditFailurePromptModelModel getModel() {
        return model;
    }

    public void setModel(AuditFailurePromptModelModel model) {
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

    public class AuditFailurePromptModelModel {

        //        "oid":"I7S0TLH2M1",
//　　　　"name":"别海静",
//　　　　"idNumber":"41132719606170624",
//　　　　"submissionTime":"2018-04-07 17:09:46",
//        "auditTime":"2018-04-04 19:25:57",
//　　　　"status":"1",
//　　　　"reason":"就是拒绝，咋滴了~",
//　　　　"userInfo":{
//　　　　　　"oid":"I7RWNLCSO1"
//　　　　}
        private String oid;
        private String name;
        private String idNumber;
        private String submissionTime;
        private String status;
        private String reason;

        /**
         * 实名认证
         */
//         "sex":"男",
//         "age":24,
//         "dateofBirth":"1994-01-04 00:00:00",
//         "fullFacePhoto":"uploadImage/uploadImage/thum/20180404192353499001.png",
//         "reversePhoto":"uploadImage/uploadImage/thum/20180404192354245001.png",
        private String sex;
        private String age;
        private String dateofBirth;
        private String fullFacePhoto;
        private String reversePhoto;

        /**
         * 教师资格证认证
         */
//　　　　"seniority":"5",
//　　　　"teacherCertificationNumber":"",
//　　　　"teacherCertificationPhoto":"",
        private String seniority;
        private String teacherCertificationNumber;
        private String teacherCertificationPhoto;

        /**
         * 成人学历认证
         */
//        "startTime":"2018-01-01",
//        "schoolName":"南开大学",
//        "userDiplomaPhoto":null,
//        "endTime":"2018-01-01",
//        "userMajor":"汽修专业",
//        "userDiploma":"博士"

        private String startTime;
        private String schoolName;
        private String userDiplomaPhoto;
        private String endTime;
        private String userMajor;
        private String userDiploma;

        /**
         * 在校学生证认证
         */
//                        "studentNo":"123456789",
//                　　　　"userClass":"汽修一班",
//                　　　　"department":"会计系",
//                　　　　"photo":"uploadImage/uploadImage/thum/20180404192458252001.png",
//                　　　　"issuingTime":"2018-04-01",
//                　　　　"placeofOrigin":"河南省南阳市内乡县",

        private String studentNo;
        private String userClass;
        private String department;
        private String photo;
        private String issuingTime;
        private String placeofOrigin;

        public String getStudentNo() {
            return studentNo;
        }

        public void setStudentNo(String studentNo) {
            this.studentNo = studentNo;
        }

        public String getUserClass() {
            return userClass;
        }

        public void setUserClass(String userClass) {
            this.userClass = userClass;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getIssuingTime() {
            return issuingTime;
        }

        public void setIssuingTime(String issuingTime) {
            this.issuingTime = issuingTime;
        }

        public String getPlaceofOrigin() {
            return placeofOrigin;
        }

        public void setPlaceofOrigin(String placeofOrigin) {
            this.placeofOrigin = placeofOrigin;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getUserDiplomaPhoto() {
            return userDiplomaPhoto;
        }

        public void setUserDiplomaPhoto(String userDiplomaPhoto) {
            this.userDiplomaPhoto = userDiplomaPhoto;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getUserMajor() {
            return userMajor;
        }

        public void setUserMajor(String userMajor) {
            this.userMajor = userMajor;
        }

        public String getUserDiploma() {
            return userDiploma;
        }

        public void setUserDiploma(String userDiploma) {
            this.userDiploma = userDiploma;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public String getSubmissionTime() {
            return submissionTime;
        }

        public void setSubmissionTime(String submissionTime) {
            this.submissionTime = submissionTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getDateofBirth() {
            return dateofBirth;
        }

        public void setDateofBirth(String dateofBirth) {
            this.dateofBirth = dateofBirth;
        }

        public String getFullFacePhoto() {
            return fullFacePhoto;
        }

        public void setFullFacePhoto(String fullFacePhoto) {
            this.fullFacePhoto = fullFacePhoto;
        }

        public String getReversePhoto() {
            return reversePhoto;
        }

        public void setReversePhoto(String reversePhoto) {
            this.reversePhoto = reversePhoto;
        }

        public String getSeniority() {
            return seniority;
        }

        public void setSeniority(String seniority) {
            this.seniority = seniority;
        }

        public String getTeacherCertificationNumber() {
            return teacherCertificationNumber;
        }

        public void setTeacherCertificationNumber(String teacherCertificationNumber) {
            this.teacherCertificationNumber = teacherCertificationNumber;
        }

        public String getTeacherCertificationPhoto() {
            return teacherCertificationPhoto;
        }

        public void setTeacherCertificationPhoto(String teacherCertificationPhoto) {
            this.teacherCertificationPhoto = teacherCertificationPhoto;
        }
    }

}
