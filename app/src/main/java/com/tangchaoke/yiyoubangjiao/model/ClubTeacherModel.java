package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/11/5.
 */

public class ClubTeacherModel {

//    {
//        "map":{
//        .................
//    },
//        "status":1,
//            "message":"获取成功!"
//    }

    private ClubTeacherMapModel map;
    private String status;
    private String message;

    public ClubTeacherMapModel getMap() {
        return map;
    }

    public void setMap(ClubTeacherMapModel map) {
        this.map = map;
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

    public class ClubTeacherMapModel{

//        "isClub":"0"

        private String isClub;

        public String getIsClub() {
            return isClub;
        }

        public void setIsClub(String isClub) {
            this.isClub = isClub;
        }
    }

}
