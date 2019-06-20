package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/3/22.
 */

public class CertificationInformationModel {

//    {
//        model= {
//             ..........
//	            }
//        status=状态
//        message= 提示信息
//    }

    private CertificationInformationModelModel model;
    private String status;
    private String message;

    public CertificationInformationModelModel getModel() {
        return model;
    }

    public void setModel(CertificationInformationModelModel model) {
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

    public class CertificationInformationModelModel {

//                userRealName=用户实名认证表oid(未认证不返回)
//                userRealName.status=用户实名认证状态:0认证中 1认证失败 2认证成功 3未认证
//                userDiploma=用户成人学历认证表oid(未认证不返回)
//                userDiploma.status=用户成人学历认证状态:0认证中 1认证失败 2认证成功 3未认证
//                substituteTeacher=用户教师资格认证表oid(未认证不返回)
//                substituteTeacher.status=用户教师资格认证状态:0认证中 1认证失败 2认证成功 3未认证
//                studentIdCard=用户在校学生证认证表oid(未认证不返回)
//                studentIdCardStatus=用户在校学生证认证状态:0认证中 1认证失败 2认证成功 3未认证
//                teacherInfo=易优教师认证表oid(未认证不返回)
//                teacherInfoStatus=易优教师认证状态:0认证中 1认证失败 2认证成功 3未认证
//        tutorInfoStatus

        private String tutorInfoStatus;
        private String userRealName;
        private String userRealNameStatus;
        private String userDiploma;
        private String userDiplomaStatus;
        private String userTeacherCertification;
        private String userTeacherCertificationStatus;
        private String studentIdCard;
        private String studentIdCardStatus;
        private String teacherInfo;
        private String teacherInfoStatus;

        public String getTutorInfoStatus() {
            return tutorInfoStatus;
        }

        public void setTutorInfoStatus(String tutorInfoStatus) {
            this.tutorInfoStatus = tutorInfoStatus;
        }

        public String getTeacherInfo() {
            return teacherInfo;
        }

        public void setTeacherInfo(String teacherInfo) {
            this.teacherInfo = teacherInfo;
        }

        public String getTeacherInfoStatus() {
            return teacherInfoStatus;
        }

        public void setTeacherInfoStatus(String teacherInfoStatus) {
            this.teacherInfoStatus = teacherInfoStatus;
        }

        public String getStudentIdCard() {
            return studentIdCard;
        }

        public void setStudentIdCard(String studentIdCard) {
            this.studentIdCard = studentIdCard;
        }

        public String getStudentIdCardStatus() {
            return studentIdCardStatus;
        }

        public void setStudentIdCardStatus(String studentIdCardStatus) {
            this.studentIdCardStatus = studentIdCardStatus;
        }

        public String getUserRealName() {
            return userRealName;
        }

        public void setUserRealName(String userRealName) {
            this.userRealName = userRealName;
        }

        public String getUserRealNameStatus() {
            return userRealNameStatus;
        }

        public void setUserRealNameStatus(String userRealNameStatus) {
            this.userRealNameStatus = userRealNameStatus;
        }

        public String getUserDiploma() {
            return userDiploma;
        }

        public void setUserDiploma(String userDiploma) {
            this.userDiploma = userDiploma;
        }

        public String getUserDiplomaStatus() {
            return userDiplomaStatus;
        }

        public void setUserDiplomaStatus(String userDiplomaStatus) {
            this.userDiplomaStatus = userDiplomaStatus;
        }

        public String getUserTeacherCertification() {
            return userTeacherCertification;
        }

        public void setUserTeacherCertification(String userTeacherCertification) {
            this.userTeacherCertification = userTeacherCertification;
        }

        public String getUserTeacherCertificationStatus() {
            return userTeacherCertificationStatus;
        }

        public void setUserTeacherCertificationStatus(String userTeacherCertificationStatus) {
            this.userTeacherCertificationStatus = userTeacherCertificationStatus;
        }
    }

}
