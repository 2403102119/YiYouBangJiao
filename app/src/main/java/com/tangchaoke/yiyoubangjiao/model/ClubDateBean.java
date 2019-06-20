package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2019/1/22.
 */

public class ClubDateBean {

//    {
//        name=俱乐部名字
//        logo=俱乐部logo
//        status=状态：0失败 1成功
//            message= 提示信息
//    }

    private String name;
    private String logo;
    private String status;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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
