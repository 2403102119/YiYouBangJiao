package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/12/26.
 */

public class NewsBean {

//    {
//        "list":[
//        {
//            ...............
//        }
//    ],
//        "status":1,
//            "message":"获取成功!"
//    }

    private List<NewsListBean> list;
    private String status;
    private String message;

    public List<NewsListBean> getList() {
        return list;
    }

    public void setList(List<NewsListBean> list) {
        this.list = list;
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

    public class NewsListBean{

//        "oid":"I82GA3SFB1",
//                "title":"中国南极科考队内陆队抵达泰山站",
//                "path":"uploadImage/news/I82GA3RKH1.jpg",
//                "content":"12月25日，中国南极科考队内陆队抵达泰山站。从远处看，泰山站的钢结构主体既像一个中国红灯笼，又像一个降落在冰雪世界的“不明飞行物”。进入其中，透过窗户向外张望时，环形的建筑结构使视野十分开阔。 当日，经过8天的风雪跋涉，中国第35次南极科学考察队内陆队——泰山队和昆仑队的共37名队员顺利抵达泰山站",
//                "date":"2018-12-26 11:25:30"

        private String oid;
        private String title;
        private String path;
        private String content;
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

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

}
