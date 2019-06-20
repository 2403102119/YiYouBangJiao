package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/4/12.
 */

public class JudgeProblemModel {

//    {
//        model={
//                ..............
//	 }
//        status=状态
//        message= 提示信息
//    }

    private JudgeProblemModelModel model;
    private String status;
    private String message;

    public JudgeProblemModelModel getModel() {
        return model;
    }

    public void setModel(JudgeProblemModelModel model) {
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

    public class JudgeProblemModelModel{

//        oid=题目id
//                status=采纳状态：0未采纳 1采纳

        private String oid;
        private String status;
        private String respondentEndAnswer;

        public String getRespondentEndAnswer() {
            return respondentEndAnswer;
        }

        public void setRespondentEndAnswer(String respondentEndAnswer) {
            this.respondentEndAnswer = respondentEndAnswer;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
