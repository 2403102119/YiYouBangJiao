package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/23.
 */

public class ProblemBean {

//    {
//　　"model":[
//　　　　{
//　　　　　........
//　　　　}
//　　],
//　　"status":1,
//　　"message":"操作成功!"
//    }

    private List<ProblemModelBean> model;
    private String status;
    private String message;

    public List<ProblemModelBean> getModel() {
        return model;
    }

    public void setModel(List<ProblemModelBean> model) {
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

    public class ProblemModelBean {

//        　"content":"5. 已知函数f（x）=lnx，g（x）=  ﹣  （x为实常数）．（1）当a=1时，求函数φ（x）=f（x）﹣g（x）在x∈[4， ∞）上的最小值；（2）若方程e2f（x）=g（x）（其中e=2.71828…）在区间[  ]上有解，求实数a的取值范围．",
//　　　　　　"title":"高中",
//　　　　　　"nickName":"海哥吖海哥",
//　　　　　　"subject":"数学",
//　　　　　　"oid":"I7RF8EPDX1",
//　　　　　　"submissionTime":"2018-03-23 14:33:29",
//　　　　　　"grade":"高一",
//　　　　　　"head":"uploadImage/uploadImage//thum/20180322143716233001.png"
//        photo
//        userOid
//        exercisesStatus
//        price
//        status
// "chessSpecies":"1",
//        "type":"2",

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
        private String exercisesStatus;
        private String price;
        private String status;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getExercisesStatus() {
            return exercisesStatus;
        }

        public void setExercisesStatus(String exercisesStatus) {
            this.exercisesStatus = exercisesStatus;
        }

        public String getUserOid() {
            return userOid;
        }

        public void setUserOid(String userOid) {
            this.userOid = userOid;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
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
    }

}
