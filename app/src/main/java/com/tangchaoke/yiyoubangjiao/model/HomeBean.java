package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */

public class HomeBean {

//    {
//　　"model1":[
//　　　　{
//　　　　　　......
//　　　　}
//　　],
//　　"model2":[
//　　　　{
//　　　　　　......
//　　　　}
//　　],
//　　"status":1,
//　　"message":"获取首页轮播图列表和易优教师列表成功!"
//    }

    private List<HomeMode11Bean> model1;
    private List<HomeMode12Bean> model2;
    private String status;
    private String message;

    public List<HomeMode11Bean> getModel1() {
        return model1;
    }

    public void setModel1(List<HomeMode11Bean> model1) {
        this.model1 = model1;
    }

    public List<HomeMode12Bean> getModel2() {
        return model2;
    }

    public void setModel2(List<HomeMode12Bean> model2) {
        this.model2 = model2;
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

    public class HomeMode11Bean {

        //        "oid":"001",
        //        "status":"0",
        //　　　　　　"title":"001",
        //　　　　　　"path":"111",
        //　　　　　　"bannerPhoto":"111"

        private String oid;
        private String status;
        private String title;
        private String path;
        private String bannerPhoto;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getBannerPhoto() {
            return bannerPhoto;
        }

        public void setBannerPhoto(String bannerPhoto) {
            this.bannerPhoto = bannerPhoto;
        }
    }

    public class HomeMode12Bean {

        //     "oid":"I7ZSJLSW71",
        //             "title":"gewg",
        //             "content":"geeg",
        //             "path":"uploadImage/news/I7ZSJLGNE1.gif",
        //             "date":"2018-10-20 11:25:35"

        private String oid;
        private String title;
        private String content;
        private String path;
        private String date;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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
