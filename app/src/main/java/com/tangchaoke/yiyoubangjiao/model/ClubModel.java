package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/10/30.
 */

public class ClubModel {

//    {
//        "list":[
//        {
//           .................
//        },
//    ],
//        "status":1,
//            "message":"获取成功!"
//    }

    private List<ClubListModel> list;
    private String status;
    private String message;

    public List<ClubListModel> getList() {
        return list;
    }

    public void setList(List<ClubListModel> list) {
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

    public class ClubListModel {

//         "oid":"I806R1EMO1",
//                "name":"天津俱乐部",
//                "briefIntroduction":"那数据答复内容鹅肉公考热给客人给客人",
//                "star":"2",
//                "logo":"assets/admin_default.jpg",
//                "province":{
//
//        },
//            "city":{
//
//        },
//            "area":{
//
//        }

        private String oid;
        private String name;
        private String briefIntroduction;
        private String star;
        private String logo;
        private List<ClubListProvinceModel> province;
        private List<ClubListCityModel> city;
        private List<ClubListAreaModel> area;

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

        public String getBriefIntroduction() {
            return briefIntroduction;
        }

        public void setBriefIntroduction(String briefIntroduction) {
            this.briefIntroduction = briefIntroduction;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public List<ClubListProvinceModel> getProvince() {
            return province;
        }

        public void setProvince(List<ClubListProvinceModel> province) {
            this.province = province;
        }

        public List<ClubListCityModel> getCity() {
            return city;
        }

        public void setCity(List<ClubListCityModel> city) {
            this.city = city;
        }

        public List<ClubListAreaModel> getArea() {
            return area;
        }

        public void setArea(List<ClubListAreaModel> area) {
            this.area = area;
        }

        public class ClubListProvinceModel {

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

        }

        public class ClubListCityModel {

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

        }

        public class ClubListAreaModel {

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

    }

}
