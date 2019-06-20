package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/4/21.
 */

public class AskForVersionInfoModel {

//    {
//        "model":{
//       ...........
//    },
//        "status":1,
//            "message":"获取版本信息成功!"
//    }

    private AskForVersionInfoModelModel model;
    private String status;
    private String message;

    public AskForVersionInfoModelModel getModel() {
        return model;
    }

    public void setModel(AskForVersionInfoModelModel model) {
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

    public class AskForVersionInfoModelModel {

//         "oid":"301",
//                "versionCode":"2",
//                "versionName":"1.1",
//                "description":"易优帮教1.1版本",
//                "apkurl":"apk/1.1.apk"

        private String oid;
        private int versionCode;
        private String versionName;
        private String description;
        private String apkurl;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getApkurl() {
            return apkurl;
        }

        public void setApkurl(String apkurl) {
            this.apkurl = apkurl;
        }
    }

}
