package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/5/14.
 */

public class QuantityModel {

//    {
//        "model":{
//       ............
//    },
//        "status":1,
//            "message":"获取成功!"
//    }

    private QuantityModelModel model;
    private String status;
    private String message;

    public QuantityModelModel getModel() {
        return model;
    }

    public void setModel(QuantityModelModel model) {
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

    public class QuantityModelModel{

//         "exercises1":0,
//                "exercises2":1,
//                "exercises3":0

        private String exercises1;
        private String exercises2;
        private String exercises3;

        public String getExercises1() {
            return exercises1;
        }

        public void setExercises1(String exercises1) {
            this.exercises1 = exercises1;
        }

        public String getExercises2() {
            return exercises2;
        }

        public void setExercises2(String exercises2) {
            this.exercises2 = exercises2;
        }

        public String getExercises3() {
            return exercises3;
        }

        public void setExercises3(String exercises3) {
            this.exercises3 = exercises3;
        }
    }

}
