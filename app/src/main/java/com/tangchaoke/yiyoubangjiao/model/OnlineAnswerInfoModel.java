package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/3/23.
 */

public class OnlineAnswerInfoModel {

//    {
//　　"model":{
//　　　　...........
//　　},
//　　"status":1,
//　　"message":"获取成功!"
//    }

    private OnlineAnswerInfoModelModel model;
    private String status;
    private String message;

    public OnlineAnswerInfoModelModel getModel() {
        return model;
    }

    public void setModel(OnlineAnswerInfoModelModel model) {
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

    public class OnlineAnswerInfoModelModel{

//        "respondent":"0",
//　　　　"content":"如图所示，半圆形玻璃砖按图中实线位置放置，直边与BD重合。一束激光沿着半圆形玻璃砖的半径从圆弧面垂直BD射到圆心O点上。使玻璃砖绕O点逆时针缓慢地转过角度θ（θ<90°），观察到折射光斑和反射光斑在弧形屏上移动。",
//　　　　"title":"我滴天呐被张杰吃咯。",
//　　　　"nickName":"海哥吖海哥",
//　　　　"subject":"物理",
//　　　　"oid":"I7RFARRFK1",
//　　　　"submissionTime":"2018-03-23 15:39:37",
//　　　　"grade":"高二",
//　　　　"head":"uploadImage/uploadImage//thum/20180322143716233001.png",
//　　　　"photo":"uploadImage/uploadImage/thum/20180323153928019001.png"
//        userOid
//        price
//        proportion
//        exercisesStatus

        private String exercisesStatus;
        private String respondent;
        private String content;
        private String title;
        private String nickName;
        private String subject;
        private String oid;
        private String submissionTime;
        private String grade;
        private String head;
        private String photo;
        private String userOid;
        private String price;
        private String proportion;
        private String chessSpecies;
        private String type;

        public String getChessSpecies() {
            return chessSpecies;
        }

        public void setChessSpecies(String chessSpecies) {
            this.chessSpecies = chessSpecies;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getExercisesStatus() {
            return exercisesStatus;
        }

        public void setExercisesStatus(String exercisesStatus) {
            this.exercisesStatus = exercisesStatus;
        }

        public String getProportion() {
            return proportion;
        }

        public void setProportion(String proportion) {
            this.proportion = proportion;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getUserOid() {
            return userOid;
        }

        public void setUserOid(String userOid) {
            this.userOid = userOid;
        }

        public String getRespondent() {
            return respondent;
        }

        public void setRespondent(String respondent) {
            this.respondent = respondent;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getSubmissionTime() {
            return submissionTime;
        }

        public void setSubmissionTime(String submissionTime) {
            this.submissionTime = submissionTime;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }

}
