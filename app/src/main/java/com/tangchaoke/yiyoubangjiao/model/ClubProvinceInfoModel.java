package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/10/26.
 */

public class ClubProvinceInfoModel {

    private List<ClubProvinceInfoListModel> areaList;
    private String status;
    private String message;

    public List<ClubProvinceInfoListModel> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<ClubProvinceInfoListModel> areaList) {
        this.areaList = areaList;
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

    public class ClubProvinceInfoListModel {

        private String oid;
        private String name;
        private List<ClubProvinceInfoCityListModel> city;

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

        public List<ClubProvinceInfoCityListModel> getCity() {
            return city;
        }

        public void setCity(List<ClubProvinceInfoCityListModel> city) {
            this.city = city;
        }

        public class ClubProvinceInfoCityListModel {

            private String oid;
            private String name;
            private List<ClubProvinceInfoAreaListModel> city;

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

            public List<ClubProvinceInfoAreaListModel> getCity() {
                return city;
            }

            public void setCity(List<ClubProvinceInfoAreaListModel> city) {
                this.city = city;
            }

            public class ClubProvinceInfoAreaListModel {

                private String oid;
                private String name;

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
            }

        }

    }

}
