package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/10/22.
 */

public class ActivityCenterModel {

//    {
//        "model":{
//        ................
//    },
//        "status":1,
//            "message":"获取成功!"
//    }

    private ActivityCenterModelModel model;
    private String status;
    private String message;

    public ActivityCenterModelModel getModel() {
        return model;
    }

    public void setModel(ActivityCenterModelModel model) {
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

    public class ActivityCenterModelModel{

//        "title":"gner ",
//                "oid":"I7ZVAIM0B1",
//                "path":"uploadImage/activity/I7ZVAIL441.gif",
//                "date":"2018-10-22 09:34:28"

        private String title;
        private String oid;
        private String path;
        private String date;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

}
