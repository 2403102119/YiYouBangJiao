package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2019/1/2.
 */

public class SpecialConsultantBean {

//    {
//        model = {
//                ...........
//        }
//        status = 状态
//        message = 提示信息
//    }

    private List<SpecialConsultantModelBean> model;
    private String status;
    private String message;

    public List<SpecialConsultantModelBean> getModel() {
        return model;
    }

    public void setModel(List<SpecialConsultantModelBean> model) {
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

    public class SpecialConsultantModelBean{
//        title = 标题
//                path = 链接

        private String title;
        private String path;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

}
