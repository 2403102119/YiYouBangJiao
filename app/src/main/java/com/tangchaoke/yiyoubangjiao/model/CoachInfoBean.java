package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/11/2.
 */

public class CoachInfoBean {

//    {
//        "model":{
//       ..................
//    },
//        "status":1,
//            "message":"获取成功!"
//    }

    private CoachInfoModelBean model;
    private String status;
    private String message;

    public CoachInfoModelBean getModel() {
        return model;
    }

    public void setModel(CoachInfoModelBean model) {
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

    public class CoachInfoModelBean {

//         "teachingExperience":"不会惹结婚乐童音乐家就让他不会惹结婚乐童音乐家就让他不会惹结婚乐童音乐家就让他不会惹结婚乐童音乐家就让他",
//                "successCase":"不会惹结婚乐童音乐家就让他不会惹结婚乐童音乐家就让他不会惹结婚乐童音乐家就让他不会惹结婚乐童音乐家就让他不会惹结婚乐童音乐家就让他",
//                "sex":"2",
//        "phone":"17513602524",
//                "schoolName":"天津大学",
//                "name":"里欧收到",
//        "price":"999",
//                "oid":"I809OQP3G1",
//                "age":"35",
//                "characteristicsofEducation":"本科学历,尖子培训,升学率高,升学超高",
//                "teachingRange":"不会惹结婚乐童音乐家就让他不会惹结婚乐童音乐家就让他不会惹结婚乐童音乐家就让他不会惹结婚乐童音乐家就让他",
//                "type":"4",
//                "city":"北京市"
//         "head":"uploadImage/coach_head/I83M3CTBB1.jpg"

        private String teachingExperience;
        private String successCase;
        private String sex;
        private String phone;
        private String schoolName;
        private String price;
        private String name;
        private String oid;
        private String age;
        private String characteristicsofEducation;
        private String teachingRange;
        private String type;
        private String city;
        private String head;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getTeachingExperience() {
            return teachingExperience;
        }

        public void setTeachingExperience(String teachingExperience) {
            this.teachingExperience = teachingExperience;
        }

        public String getSuccessCase() {
            return successCase;
        }

        public void setSuccessCase(String successCase) {
            this.successCase = successCase;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
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

        public String getTeachingRange() {
            return teachingRange;
        }

        public void setTeachingRange(String teachingRange) {
            this.teachingRange = teachingRange;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
