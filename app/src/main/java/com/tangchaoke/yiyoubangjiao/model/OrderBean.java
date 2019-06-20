package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2019/1/14.
 */

public class OrderBean {

//    {
//        list={
//                .................
//		 }
//        status=状态
//        message= 提示信息
//    }

    private List<OrderListBean> list;
    private String status;
    private String message;

    public List<OrderListBean> getList() {
        return list;
    }

    public void setList(List<OrderListBean> list) {
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

    public class OrderListBean {

//"oid":"I837SUNCV1",
//        "orderNumber":"20190114174003428001",
//        "num":0,
//        "integral":0,
//        "allIntegral":3599,
//        "allMoney":3599,
//        "iosAllMoney":53985,
//        "orderStatus":"1",
//        "payType":"2",
//        "commodity":{
//            .........................
//    }

        private String oid;
        private String orderNumber;
        private String num;
        private String integral;
        private String allIntegral;
        private String allMoney;
        private String iosAllMoney;
        private String orderStatus;
        private String payType;
        private OrderListCommodityBean commodity;

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getAllIntegral() {
            return allIntegral;
        }

        public void setAllIntegral(String allIntegral) {
            this.allIntegral = allIntegral;
        }

        public String getAllMoney() {
            return allMoney;
        }

        public void setAllMoney(String allMoney) {
            this.allMoney = allMoney;
        }

        public String getIosAllMoney() {
            return iosAllMoney;
        }

        public void setIosAllMoney(String iosAllMoney) {
            this.iosAllMoney = iosAllMoney;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public OrderListCommodityBean getCommodity() {
            return commodity;
        }

        public void setCommodity(OrderListCommodityBean commodity) {
            this.commodity = commodity;
        }

        public class OrderListCommodityBean{

//            "name":"国际详细 木质 棋盘 旗子国际详细 木质 棋盘 旗子国际详细",
//                    "material":"木质",
//                    "integral":0,
//                    "iosMoney":0,
//                    "stock":0,
//                    "sold":0,
//                    "creditNum":0
//            "photo":"uploadImage/commodity_photo/I834RZ9Z81.jpg"

            private String name;
            private String material;
            private String integral;
            private String iosMoney;
            private String stock;
            private String sold;
            private String creditNum;
            private String photo;

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
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
        }

    }

}
