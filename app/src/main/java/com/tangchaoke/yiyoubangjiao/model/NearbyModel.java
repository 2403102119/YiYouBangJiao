package com.tangchaoke.yiyoubangjiao.model;

import com.amap.api.maps.model.LatLng;

import java.util.List;

/**
 * Created by Administrator on 2018/12/25.
 */

public class NearbyModel {

//    {
//        "list":[
//        {
//            ..............
//        }
//    ],
//        "status":1,
//            "message":"获取成功!"
//    }

    private List<NearbyListModel> list;
    private String status;
    private String message;

    public List<NearbyListModel> getList() {
        return list;
    }

    public void setList(List<NearbyListModel> list) {
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

    public class NearbyListModel{

//        "_id":"4",
//                "_province":"天津市",
//                "_city":"天津市",
//                "_image":[
//
//            ],
//            "_name":"唐朝客俱乐部",
//                "_distance":"51",
//                "_location":"117.21577,39.114334",
//                "_updatetime":"2018-12-25 17:25:23",
//                "_district":"河西区",
//                "_createtime":"2018-12-25 17:25:23",
//                "_address":"天津市河西区大营门街道南京路45号天津国际贸易中心"
//        "club_logo":"uploadImage/club_logo/I82XM64WA1.jpg",
//                "club_oid":"I82XLLF751",

        private String _id;
        private String _province;
        private String _city;
        private String _image;
        private String _name;
        private String _distance;
        private String  _location;
        private String _updatetime;
        private String _district;
        private String _createtime;
        private String _address;
        private String club_logo;
        private String club_oid;

        public String getClub_logo() {
            return club_logo;
        }

        public void setClub_logo(String club_logo) {
            this.club_logo = club_logo;
        }

        public String getClub_oid() {
            return club_oid;
        }

        public void setClub_oid(String club_oid) {
            this.club_oid = club_oid;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String get_province() {
            return _province;
        }

        public void set_province(String _province) {
            this._province = _province;
        }

        public String get_city() {
            return _city;
        }

        public void set_city(String _city) {
            this._city = _city;
        }

        public String get_image() {
            return _image;
        }

        public void set_image(String _image) {
            this._image = _image;
        }

        public String get_name() {
            return _name;
        }

        public void set_name(String _name) {
            this._name = _name;
        }

        public String get_distance() {
            return _distance;
        }

        public void set_distance(String _distance) {
            this._distance = _distance;
        }

        public String get_location() {
            return _location;
        }

        public void set_location(String _location) {
            this._location = _location;
        }

        public String get_updatetime() {
            return _updatetime;
        }

        public void set_updatetime(String _updatetime) {
            this._updatetime = _updatetime;
        }

        public String get_district() {
            return _district;
        }

        public void set_district(String _district) {
            this._district = _district;
        }

        public String get_createtime() {
            return _createtime;
        }

        public void set_createtime(String _createtime) {
            this._createtime = _createtime;
        }

        public String get_address() {
            return _address;
        }

        public void set_address(String _address) {
            this._address = _address;
        }
    }

}
