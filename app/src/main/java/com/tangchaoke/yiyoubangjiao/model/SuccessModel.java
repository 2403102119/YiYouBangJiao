package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/3/19.
 */

public class SuccessModel {

//    {
//        status=状态
//        message= 提示信息
//    exercisesStatus
//    }

    private String status;
    private String message;
    private String exercisesStatus;

    public String getExercisesStatus() {
        return exercisesStatus;
    }

    public void setExercisesStatus(String exercisesStatus) {
        this.exercisesStatus = exercisesStatus;
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
}
