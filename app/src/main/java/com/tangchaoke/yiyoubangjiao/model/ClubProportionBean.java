package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2019/1/8.
 */

public class ClubProportionBean {

//    {
//        proportion=抽成比例
//        status=状态：0失败 1成功
//            message= 提示信息
//    }

    private String proportion;
    private String status;
    private String message;

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
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
