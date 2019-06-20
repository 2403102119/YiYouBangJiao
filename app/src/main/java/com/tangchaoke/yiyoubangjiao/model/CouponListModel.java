package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/9/13.
 */

public class CouponListModel {

//    {
//        "list":[
//        {
//            .................
//        }
//    ],
//        "status":1,
//            "message":"获取成功!"
//    }

    private List<CouponListModelList> list;
    private String status;
    private String message;

    public List<CouponListModelList> getList() {
        return list;
    }

    public void setList(List<CouponListModelList> list) {
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

    public class CouponListModelList{

//        "couponName":"qqqqq",
//                "endEffectiveTime":"2018-10-06",
//                "couponStatus":"0",
//                "oid":"I7YBW24YY1",
//                "money":"5",
//                "date":"2018-09-13"

        private String couponName;
        private String endEffectiveTime;
        private String couponStatus;
        private String oid;
        private String money;
        private String date;

        public String getCouponName() {
            return couponName;
        }

        public void setCouponName(String couponName) {
            this.couponName = couponName;
        }

        public String getEndEffectiveTime() {
            return endEffectiveTime;
        }

        public void setEndEffectiveTime(String endEffectiveTime) {
            this.endEffectiveTime = endEffectiveTime;
        }

        public String getCouponStatus() {
            return couponStatus;
        }

        public void setCouponStatus(String couponStatus) {
            this.couponStatus = couponStatus;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

}
