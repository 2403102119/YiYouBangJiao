package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/8/18.
 */

public class PushRangeInfoModel {

//    {
//        "range":{
//        .....................
//    },
//        "status":1,
//            "message":"获取成功!"
//    }

    private PushRangeInfoRangeModel range;
    private String status;
    private String message;

    public PushRangeInfoRangeModel getRange() {
        return range;
    }

    public void setRange(PushRangeInfoRangeModel range) {
        this.range = range;
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

    public class PushRangeInfoRangeModel{

//        "highSchool":[
//        {
//           ................
//        }
//        ],
//        "middleSchool":[
//        {
//           ............
//        }
//        ],
//        "primarySchool":[
//        {
//           .............
//        }
//        ]

        private List<PushRangeInfoRangeHighSchoolModel> highSchool;
        private List<PushRangeInfoRangeMiddleSchoolModel> middleSchool;
        private List<PushRangeInfoRangePrimarySchoolModel> primarySchool;

        public List<PushRangeInfoRangeHighSchoolModel> getHighSchool() {
            return highSchool;
        }

        public void setHighSchool(List<PushRangeInfoRangeHighSchoolModel> highSchool) {
            this.highSchool = highSchool;
        }

        public List<PushRangeInfoRangeMiddleSchoolModel> getMiddleSchool() {
            return middleSchool;
        }

        public void setMiddleSchool(List<PushRangeInfoRangeMiddleSchoolModel> middleSchool) {
            this.middleSchool = middleSchool;
        }

        public List<PushRangeInfoRangePrimarySchoolModel> getPrimarySchool() {
            return primarySchool;
        }

        public void setPrimarySchool(List<PushRangeInfoRangePrimarySchoolModel> primarySchool) {
            this.primarySchool = primarySchool;
        }

        public class PushRangeInfoRangeHighSchoolModel{

//             "rangeke":"物理,生物,地理,政治",
//                "rangenian":"高一"

            private String rangeke;
            private String rangenian;

            public String getRangeke() {
                return rangeke;
            }

            public void setRangeke(String rangeke) {
                this.rangeke = rangeke;
            }

            public String getRangenian() {
                return rangenian;
            }

            public void setRangenian(String rangenian) {
                this.rangenian = rangenian;
            }
        }

        public class PushRangeInfoRangeMiddleSchoolModel{

//             "rangeke":"语文,化学,历史",
//                "rangenian":"初一"

            private String rangeke;
            private String rangenian;

            public String getRangeke() {
                return rangeke;
            }

            public void setRangeke(String rangeke) {
                this.rangeke = rangeke;
            }

            public String getRangenian() {
                return rangenian;
            }

            public void setRangenian(String rangenian) {
                this.rangenian = rangenian;
            }
        }

        public class PushRangeInfoRangePrimarySchoolModel{

//             "rangeke":"语文,数学,英语",
//                "rangenian":"一年级"

            private String rangeke;
            private String rangenian;

            public String getRangeke() {
                return rangeke;
            }

            public void setRangeke(String rangeke) {
                this.rangeke = rangeke;
            }

            public String getRangenian() {
                return rangenian;
            }

            public void setRangenian(String rangenian) {
                this.rangenian = rangenian;
            }
        }

    }

}
