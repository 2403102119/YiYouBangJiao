package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2019/1/15.
 */

public class CommentBean {

//    {
//        "list":[
//        {
//            .......................
//        }
//    ],
//        "status":1,
//            "message":"获取成功!"
//    }

    private List<CommentListBean> list;
    private String status;
    private String message;

    public List<CommentListBean> getList() {
        return list;
    }

    public void setList(List<CommentListBean> list) {
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

    public class CommentListBean{

//        "commentTime":"2019-01-15 16:40:12",
//                "commentContent":"拒绝就就渐渐健健康康",
//                "star":5,
//                "userInfoHead":"uploadImage/uploadImage/image/20190111171053428001.png",
//                "userInfoNickName":"171****1111"

        private String commentTime;
        private String commentContent;
        private String star;
        private String userInfoHead;
        private String userInfoNickName;

        public String getCommentTime() {
            return commentTime;
        }

        public void setCommentTime(String commentTime) {
            this.commentTime = commentTime;
        }

        public String getCommentContent() {
            return commentContent;
        }

        public void setCommentContent(String commentContent) {
            this.commentContent = commentContent;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getUserInfoHead() {
            return userInfoHead;
        }

        public void setUserInfoHead(String userInfoHead) {
            this.userInfoHead = userInfoHead;
        }

        public String getUserInfoNickName() {
            return userInfoNickName;
        }

        public void setUserInfoNickName(String userInfoNickName) {
            this.userInfoNickName = userInfoNickName;
        }
    }

}
