package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/11/1.
 */

public class CoachBean {

//    {
//        "list":[
//        {
//          ..............
//        }
//        ],
//        "status":1,
//            "message":"获取成功!"
//    }

    private List<CoachListBean> list;
    private String status;
    private String message;

    public List<CoachListBean> getList() {
        return list;
    }

    public void setList(List<CoachListBean> list) {
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

    public class CoachListBean {

//         "oid":"I82XLXR811",
//            "type":"2",
//                    "phone":"18237727931",
//                    "name":"海胖砸",
//                    "sex":"1",
//                    "price":999,
//                    "age":"22",
//        "head":"uploadImage/coach_head/I83N1BBZV1.jpg",
//                    "characteristicsofEducation":"本科学历,尖子培训",
//                    "city":{
//            ......................
//        }

        private String oid;
        private String type;
        private String phone;
        private String name;
        private String sex;
        private String price;
        private String age;
        private String head;
        private String characteristicsofEducation;
        private CoachListCityBean city;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getCharacteristicsofEducation() {
            return characteristicsofEducation;
        }

        public void setCharacteristicsofEducation(String characteristicsofEducation) {
            this.characteristicsofEducation = characteristicsofEducation;
        }

        public CoachListCityBean getCity() {
            return city;
        }

        public void setCity(CoachListCityBean city) {
            this.city = city;
        }

        public class CoachListCityBean{

//            "name":"天津市"

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
