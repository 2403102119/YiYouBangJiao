package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2019/1/14.
 */

public class CommodityInfoBean {

//    {
//        "map":{
//        .......................
//    },
//        "status":1,
//            "message":"获取成功!"
//    }

    private CommodityInfoMapBean map;
    private String status;
    private String message;

    public CommodityInfoMapBean getMap() {
        return map;
    }

    public void setMap(CommodityInfoMapBean map) {
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

    public class CommodityInfoMapBean{

//        "bannerList":[
//        {
//            ....................
//        }
//        ],
//        "iosMoney":53985,
//                "oid":"I834S7RBF1",
//                "name":"国际详细 木质 棋盘 旗子国际详细 木质 棋盘 旗子国际详细",
//                "money":3599,
//                "creditNum":0,
//                "photo":"uploadImage/commodity_photo/I834S7NWL1.jpg",
//                "commentList":[
//
//        ],
//        "material":"木质",
//                "sold":0,
//                "integral":3599

        private List<CommodityInfoMapBannerBean> bannerList;
        private String iosMoney;
        private String oid;
        private String name;
        private String money;
        private String creditNum;
        private String photo;
        private List<CommodityInfoMapCommentBean> commentList;
        private String material;
        private String sold;
        private String integral;

        public List<CommodityInfoMapBannerBean> getBannerList() {
            return bannerList;
        }

        public void setBannerList(List<CommodityInfoMapBannerBean> bannerList) {
            this.bannerList = bannerList;
        }

        public String getIosMoney() {
            return iosMoney;
        }

        public void setIosMoney(String iosMoney) {
            this.iosMoney = iosMoney;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCreditNum() {
            return creditNum;
        }

        public void setCreditNum(String creditNum) {
            this.creditNum = creditNum;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public List<CommodityInfoMapCommentBean> getCommentList() {
            return commentList;
        }

        public void setCommentList(List<CommodityInfoMapCommentBean> commentList) {
            this.commentList = commentList;
        }

        public String getMaterial() {
            return material;
        }

        public void setMaterial(String material) {
            this.material = material;
        }

        public String getSold() {
            return sold;
        }

        public void setSold(String sold) {
            this.sold = sold;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public class CommodityInfoMapBannerBean{

//            "photo":"uploadImage/commodity_banner_photo/I834SA16G1.jpg"

            private String photo;

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }
        }

        public class CommodityInfoMapCommentBean{

//            userInfoNickName=评价用户昵称
//                    userInfoHead=评价用户头像
//            star=评价星级（1-5）
//            commentContent=评价内容
//                    commentTime=评价时间

            private String userInfoNickName;
            private String userInfoHead;
            private String star;
            private String commentContent;
            private String commentTime;

            public String getUserInfoNickName() {
                return userInfoNickName;
            }

            public void setUserInfoNickName(String userInfoNickName) {
                this.userInfoNickName = userInfoNickName;
            }

            public String getUserInfoHead() {
                return userInfoHead;
            }

            public void setUserInfoHead(String userInfoHead) {
                this.userInfoHead = userInfoHead;
            }

            public String getStar() {
                return star;
            }

            public void setStar(String star) {
                this.star = star;
            }

            public String getCommentContent() {
                return commentContent;
            }

            public void setCommentContent(String commentContent) {
                this.commentContent = commentContent;
            }

            public String getCommentTime() {
                return commentTime;
            }

            public void setCommentTime(String commentTime) {
                this.commentTime = commentTime;
            }
        }

    }

}
