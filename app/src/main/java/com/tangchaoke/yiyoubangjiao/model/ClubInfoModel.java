package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/10/30.
 */

public class ClubInfoModel {

//    {
//        "model":{
//        /.............
//    },
//        "status":1,
//            "message":"获取成功!"
//    }

    private ClubInfoBean model;
    private String status;
    private String message;

    public ClubInfoBean getModel() {
        return model;
    }

    public void setModel(ClubInfoBean model) {
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

    public class ClubInfoBean{

        //        "bannerList":[
//           ....................
//        ],
//                "briefIntroduction":"那数据答复内容鹅肉公考热给客人给客人",
//                "area":null,
//        "address":"北京市朝阳区XX路20号",
//                "businessHours":"早上8点-晚上10点",
//                "oid":"I806QWT2F1",
//                "name":"efgre",
//                "star":"3",
//                "province":null,
//                "longitude":"1111",
//                "latitude":"2222",
//                "city":null

        private List<ClubInfoBanner> bannerList;
        private String briefIntroduction;
        private String area;
        private String address;
        private String businessHours;
        private String oid;
        private String name;
        private String star;
        private String province;
        private double longitude;
        private double latitude;
        private String city;

        public List<ClubInfoBanner> getBannerList() {
            return bannerList;
        }

        public void setBannerList(List<ClubInfoBanner> bannerList) {
            this.bannerList = bannerList;
        }

        public String getBriefIntroduction() {
            return briefIntroduction;
        }

        public void setBriefIntroduction(String briefIntroduction) {
            this.briefIntroduction = briefIntroduction;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBusinessHours() {
            return businessHours;
        }

        public void setBusinessHours(String businessHours) {
            this.businessHours = businessHours;
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

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public class ClubInfoBanner{

//            bannerPhoto=轮播图路径
//            sort=轮播图排序

            private String bannerPhoto;
            private String sort;

            public String getBannerPhoto() {
                return bannerPhoto;
            }

            public void setBannerPhoto(String bannerPhoto) {
                this.bannerPhoto = bannerPhoto;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }
        }

    }

}
