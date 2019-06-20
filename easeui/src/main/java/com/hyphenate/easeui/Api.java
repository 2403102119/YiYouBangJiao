package com.hyphenate.easeui;

/**
 * Created by Administrator on 2018/3/19.
 */
public class Api {

    /**
     * 图片路径
     */
    public static String PATH = "http://yiyoubangjiao.cn/Education/";
//    public static String PATH = "http://47.106.229.1:8080/Education/";

    /**
     * 域名
     */
    public static String DOMAIN_NAME = "http://yiyoubangjiao.cn/Education/action/";
//    public static String DOMAIN_NAME = "http://47.106.229.1:8080/Education/action/";

    /**
     * 返回用户头像昵称
     */
    public static String GET_HEAD_BY_OID = DOMAIN_NAME + "baseUser/getHeadByOid";

    /**
     * /判断用户是否可以继续解答问题
     */
    public static String JUDGE_RESPONDENT = DOMAIN_NAME + "baseExercises/judgeRespondent";

}
