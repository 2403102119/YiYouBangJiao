package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2019/1/14.
 */

public class ConfirmationOrderBean {

//    {
//        "map":{
//       ...................
//    },
//        "status":1,
//            "message":"获取成功!"
//    }

    private ConfirmationOrderMapBean map;
    private String status;
    private String message;

    public ConfirmationOrderMapBean getMap() {
        return map;
    }

    public void setMap(ConfirmationOrderMapBean map) {
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

    public class ConfirmationOrderMapBean{

//         "userAddress":{
//
//        },
//        "iosMoney":53985,
//                "orderOid":"I837G42401",
//                "name":"国际详细 木质 棋盘 旗子国际详细 木质 棋盘 旗子国际详细",
//                "money":3599,
//                "material":"木质",
//                "integral":3599

        private ConfirmationOrderMapAddressBean userAddress;
        private String iosMoney;
        private String orderOid;
        private String name;
        private String money;
        private String material;
        private String integral;
        private String photo;

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public ConfirmationOrderMapAddressBean getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(ConfirmationOrderMapAddressBean userAddress) {
            this.userAddress = userAddress;
        }

        public String getIosMoney() {
            return iosMoney;
        }

        public void setIosMoney(String iosMoney) {
            this.iosMoney = iosMoney;
        }

        public String getOrderOid() {
            return orderOid;
        }

        public void setOrderOid(String orderOid) {
            this.orderOid = orderOid;
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

        public class ConfirmationOrderMapAddressBean{

//            oid=收货地址oid
//                    name=收件人姓名
//            phone=收件人电话
//                    detailedAddress=详细地址

            private String oid;
            private String name;
            private String phone;
            private String detailedAddress;

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

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getDetailedAddress() {
                return detailedAddress;
            }

            public void setDetailedAddress(String detailedAddress) {
                this.detailedAddress = detailedAddress;
            }
        }

    }

}
