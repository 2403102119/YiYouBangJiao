package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2019/1/14.
 */

public class AddressBean {

//    {
//        userAddress={
//                ......................
//        }
//        status=状态
//        message= 提示信息
//    }

    private AddressUserBean userAddress;
    private String status;
    private String message;

    public AddressUserBean getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(AddressUserBean userAddress) {
        this.userAddress = userAddress;
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

    public class AddressUserBean{

//        oid=用户地址oid
//                name=收货人姓名
//                phone=收货人手机号
//                detailedAddress=详细地址

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
