package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2019/1/12.
 */

public class PointsMallBean {

//    {
//        "list":[
//        {
//            .......................
//        }],
//        "status":1,
//            "message":"获取成功!"
//    }

    private List<PointsMallListBean> list;
    private String status;
    private String message;

    public List<PointsMallListBean> getList() {
        return list;
    }

    public void setList(List<PointsMallListBean> list) {
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

    public class PointsMallListBean{

//        "oid":"I834S7RBF1",
//                "name":"国际详细 木质 棋盘 旗子国际详细 木质 棋盘 旗子国际详细",
//                "material":"木质",
//                "integral":3599,
//                "money":3599,
//                "iosMoney":53985,
//                "stock":0,
//                "sold":0,
//                "creditNum":0,
//                "photo":"uploadImage/commodity_photo/I834S7NWL1.jpg"

        private String oid;
        private String name;
        private String material;
        private String integral;
        private String money;
        private String iosMoney;
        private String stock;
        private String sold;
        private String creditNum;
        private String photo;

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

        public String getMaterial() {
            return material;
        }

        public void setMaterial(String material) {
            this.material = material;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getIosMoney() {
            return iosMoney;
        }

        public void setIosMoney(String iosMoney) {
            this.iosMoney = iosMoney;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getSold() {
            return sold;
        }

        public void setSold(String sold) {
            this.sold = sold;
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
    }

}
