package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2019/1/15.
 */

public class OrderDetailsBean {

//    {
//        "map":{
//        .......................
//    },
//        "status":1,
//            "message":"获取成功!"
//    }

    private OrderDetailsMapBean map;
    private String status;
    private String message;

    public OrderDetailsMapBean getMap() {
        return map;
    }

    public void setMap(OrderDetailsMapBean map) {
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

    public class OrderDetailsMapBean {

//        "phone":"17111111111",
//                "allIntegral":3599,
//                "commodityName":"国际详细 木质 棋盘 旗子国际详细 木质 棋盘 旗子国际详细",
//                "leavingMessage":"",
//                "orderStatus":"1",
//                "commodityPhoto":"uploadImage/commodity_photo/I834S7NWL1.jpg",
//                "startTime":"2019-01-15 11:27:37",
//                "commodityMaterial":"木质",
//                "oid":"I838UZI3S1",
//                "iosAllMoney":53985,
//                "name":"别",
//                "orderNumber":"20190115112735309001",
//                "allMoney":3599,
//                "payType":"2",
//                "detailedAddress":"天津市 河西区 永安道"

        private String phone;
        private String allIntegral;
        private String commodityName;
        private String leavingMessage;
        private String orderStatus;
        private String commodityPhoto;
        private String startTime;
        private String commodityMaterial;
        private String oid;
        private String iosAllMoney;
        private String name;
        private String orderNumber;
        private String allMoney;
        private String payType;
        private String detailedAddress;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAllIntegral() {
            return allIntegral;
        }

        public void setAllIntegral(String allIntegral) {
            this.allIntegral = allIntegral;
        }

        public String getCommodityName() {
            return commodityName;
        }

        public void setCommodityName(String commodityName) {
            this.commodityName = commodityName;
        }

        public String getLeavingMessage() {
            return leavingMessage;
        }

        public void setLeavingMessage(String leavingMessage) {
            this.leavingMessage = leavingMessage;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getCommodityPhoto() {
            return commodityPhoto;
        }

        public void setCommodityPhoto(String commodityPhoto) {
            this.commodityPhoto = commodityPhoto;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getCommodityMaterial() {
            return commodityMaterial;
        }

        public void setCommodityMaterial(String commodityMaterial) {
            this.commodityMaterial = commodityMaterial;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getIosAllMoney() {
            return iosAllMoney;
        }

        public void setIosAllMoney(String iosAllMoney) {
            this.iosAllMoney = iosAllMoney;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getAllMoney() {
            return allMoney;
        }

        public void setAllMoney(String allMoney) {
            this.allMoney = allMoney;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getDetailedAddress() {
            return detailedAddress;
        }

        public void setDetailedAddress(String detailedAddress) {
            this.detailedAddress = detailedAddress;
        }
    }

}
