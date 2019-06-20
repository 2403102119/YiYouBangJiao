package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/8/8.
 */

public class VersionCodeModel {

//    {
//        "versionStatus":"0",
//            "status":1,
//            "message":"判断版本信息成功!"
//    }

    private String versionStatus;
    private String status;
    private String message;

    public String getVersionStatus() {
        return versionStatus;
    }

    public void setVersionStatus(String versionStatus) {
        this.versionStatus = versionStatus;
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
