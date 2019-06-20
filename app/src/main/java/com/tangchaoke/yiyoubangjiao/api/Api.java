package com.tangchaoke.yiyoubangjiao.api;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/19.
 */
public class Api {

    /**
     * 图片路径
     */
//    public static String PATH = "http://yiyoubangjiao.cn/Education/";
    public static String PATH = "http://47.106.75.194:8080/Education/";

    /**
     * 域名
     */
//    public static String DOMAIN_NAME = "http://yiyoubangjiao.cn/Education/action/";
    public static String DOMAIN_NAME = "http://47.106.75.194:8080/Education/action/";

    /**
     * App79/获取轮播图列表和新闻资讯列表
     */
    public static String GET_BANNER_LIST = DOMAIN_NAME + "baseBanner/getBannerAndExercisesList";

    /**
     * App91/获取最新活动
     */
    public static String GET_ACTIVITY = DOMAIN_NAME + "baseActivity/getActivityData";

    /**
     * App1/获取验证码
     */
    public static String SEND_CODE = DOMAIN_NAME + "baseUser/sendYzm";

    /**
     * App2/用户注册1-1
     */
    public static String ADD_USER1 = DOMAIN_NAME + "baseUser/addUser1";

    /**
     * App2/用户注册1-2
     */
    public static String ADD_USER2 = DOMAIN_NAME + "baseUser/addUser2";

    /**
     * App3/用户登录 > 使用用户名+密码登录
     */
    public static String USER_LOGIN = DOMAIN_NAME + "baseUser/userLogin";

    /**
     * App53/第三方登录
     */
    public static String WX_LOGIN = DOMAIN_NAME + "baseUser/wxLogin";

    /**
     * App54/第三方绑定手机
     */
    public static String ADD_USER_BY_WX = DOMAIN_NAME + "baseUser/addUserByWx";

    /**
     * App67/微信绑定新账户设置密码
     */
    public static String SET_WX_PASSWORD = DOMAIN_NAME + "baseUser/setWXPassword";

    /**
     * App49/获取客服电话
     */
    public static String GET_PHONE = DOMAIN_NAME + "baseSystem/getPhone";

    /**
     * App84/判断答题者是否设置推送范围
     */
    public static String JUDGE_RESPONDENT_RANGE = DOMAIN_NAME + "baseRespondentRange/judgeRespondentRange";

    /**
     * App13/在线解答列表
     */
    public static String SEARCH_EXERCISES = DOMAIN_NAME + "baseExercises/searchExercises";

    /**
     * App62/判断用户是否有未解答问题
     */
    public static String JUDGE_USER_EXERCISES = DOMAIN_NAME + "baseExercises/judgeUserExercises";

    /**
     * App60/判断用户是否可以免费发题
     */
    public static String JUDGE_USER = DOMAIN_NAME + "baseExercises/judgeUser";

    /**
     * App20/用户提交问题
     */
    public static String PRESERVA_EXERCISES = DOMAIN_NAME + "baseExercises/preservationExercises";

    /**
     * App92/获取区域列表
     */
    public static String GET_AREALIST = DOMAIN_NAME + "baseArea/getAreaList";

    /**
     * App96/获取教练列表
     */
    public static String GET_COACH_LIST = DOMAIN_NAME + "baseCoach/getCoachList";

    /**
     * App97/教练详情
     */
    public static String GET_COACH_DATA = DOMAIN_NAME + "baseCoach/getClubData";

    /**
     * App93/获取俱乐部列表
     */
    public static String GET_CLUB_LIST = DOMAIN_NAME + "baseClub/getClubList";

    /**
     * App94/俱乐部详情
     */
    public static String GET_CLUB_DATA = DOMAIN_NAME + "baseClub/getClubData";

    /**
     * App95/获取附近俱乐部
     */
    public static String GET_NEARBY_CLUB_LIST = DOMAIN_NAME + "baseClub/getNearbyClubList";

    /**
     * App8/修改用户资料
     */
    public static String UPDATE_USER_INFO = DOMAIN_NAME + "baseUser/updateUserInfo";

    /**
     * App7/上传头像
     */
    public static String UPDATE_HEAD = DOMAIN_NAME + "baseUser/updateHead";

    /**
     * App69/同意协议
     */
    public static String UPDATE_AGREEMENT_STATUS = DOMAIN_NAME + "baseUser/updateAgreementStatus";

    /**
     * App40/返回用户认证状态
     */
    public static String GET_AUTHENTICA_INFORMA = DOMAIN_NAME + "baseUser/getAuthenticationInformation";

    /**
     * App30/学历认证
     */
    public static String UPDATE_USER_DIPLOMA = DOMAIN_NAME + "baseUserDiploma/updateUserDiploma";

    /**
     * App43/学生证认证
     */
    public static String UPDATE_STUDENT_ID_CARD = DOMAIN_NAME + "baseStudentIdCard/updateStudentIdCard";

    /**
     * App48/返回认证信息
     */
    public static String RETURN_MESSAGE = DOMAIN_NAME + "baseUser/returnMessage";

    /**
     * App100/获取新闻资讯列表
     */
    public static String GET_CLUB_NEWST_LIST = DOMAIN_NAME + "baseNews/getNewstList";

    /**
     * App41/获取用户余额和易优币
     */
    public static String GET_USER_BALANCE = DOMAIN_NAME + "baseUser/getUserBalance";

    /**
     * App28/消费明细列表
     */
    public static String GET_BALANCE_LIST = DOMAIN_NAME + "baseBalance/getBalanceList";

    /**
     * App5/用户找回密码
     */
    public static String UPDATE_USER_PASSWORD_BY_ACCOUNT = DOMAIN_NAME + "baseUser/updateUserPasswordByAccount";

    /**
     * App102/学校教师绑定学校
     */
    public static String ADD_TEACHER_TO_SCHOOL = DOMAIN_NAME + "baseSchool/addTeacherToSchool";

    /**
     * App101/加入俱乐部
     */
    public static String JUDGE_USER_CLUB = DOMAIN_NAME + "baseClub/judgeUserClub";

    /**
     * App103/获取用户积分明细列表
     */
    public static String GET_INTEGRAL_LIST = DOMAIN_NAME + "baseIntegral/getIntegralList";

    /**
     * App98/咨询俱乐部
     */
    public static String CONSULTING_CLUB = DOMAIN_NAME + "baseClub/consultingClub";

    /**
     * App104/获取特约顾问列表
     */
    public static String GET_SPECIAL_ADVISER_LIST = DOMAIN_NAME + "baseSpecialAdviser/getSpecialAdviserList";

    /**
     * App33/上传反馈信息
     */
    public static String SUBMIT_FEEDBACK = DOMAIN_NAME + "baseFeedback/submitFeedback";

    /**
     * App85/获取答题者推送范围
     */
    public static String GET_RESPONDENT_RANGE = DOMAIN_NAME + "baseRespondentRange/getRespondentRange";

    /**
     * App80/设置答题者推送年级科目范围
     */
    public static String SET_RESPONDENT_RANGE = DOMAIN_NAME + "baseRespondentRange/setRespondentRange";

    /**
     * App66/删除待抢答的题目
     */
    public static String DELETE_EXERCISES = DOMAIN_NAME + "baseExercises/deleteExercises";

    /**
     * App70/根据题目状态查询未读数量
     */
    public static String GET_EXERCISES_NUMBER = DOMAIN_NAME + "baseExercises/getExercisesNumber";

    /**
     * App18/获取问题列表
     */
    public static String ALREADY_ANSWERED = DOMAIN_NAME + "baseExercises/alreadyAnswered";

    /**
     * App46/根据年级获取问题金额区间
     */
    public static String PAY_GET_FREE_COURSE_LIST = DOMAIN_NAME + "baseProblemMoney/getFreeCourseList";

    /**
     * App99/获取我的俱乐部教练列表
     */
    public static String GET_CLUB_COACH_LIST = DOMAIN_NAME + "baseCoach/getClubCoachList";

    /**
     * App55/返回用户头像昵称
     */
    public static String GET_HEAD_BY_OID = DOMAIN_NAME + "baseUser/getHeadByOid";

    /**
     * App45/开通身份
     */
    public static String BEING_TUTOR = DOMAIN_NAME + "baseUser/beingIdentity";

    /**
     * App105/获取俱乐部教师抽成比例
     */
    public static String GET_CLUB_PROPORTION = DOMAIN_NAME + "baseClub/getClubProportion";

    /**
     * App44/根据用户ID获取用户信息
     */
    public static String GET_USER_INFO_BYOID = DOMAIN_NAME + "baseUser/getUserInfoByOid";

    /**
     * App47/提现
     */
    public static String GET_MONEY = DOMAIN_NAME + "baseBalance/getMoney";

    /**
     * App19/个人问题列表查看问题详情
     */
    public static String DETAILS_PROBLEM = DOMAIN_NAME + "baseExercises/detailsProblem";

    /**
     * App34/消息列表
     */
    public static String GET_USER_MESSAGE_LIST = DOMAIN_NAME + "baseUserMessage/getUserMessageList";

    /**
     * App35/消息详情
     */
    public static String VIEW_MESSAGE = DOMAIN_NAME + "baseUserMessage/viewMessage";

    /**
     * App36/删除消息
     */
    public static String DELETE_MESSAGE = DOMAIN_NAME + "baseUserMessage/deleteMessage";

    /**
     * App14/在线问题列表获取问题详细信息
     */
    public static String EXERCISES_INFORMATION = DOMAIN_NAME + "baseExercises/exercisesInformation";

    /**
     * 支付宝支付
     */
    public static String APP_PAY = DOMAIN_NAME + "payAlipay/appPay";

    /**
     * 微信支付
     */
    public static String APP_PAY_WX = DOMAIN_NAME + "payWxpay/appPay";

    /**
     * App15/立即抢答
     */
    public static String ANSWER = DOMAIN_NAME + "baseExercises/answer";

    /**
     * App16/开始解答
     */
    public static String ANSWER_EXERCISES = DOMAIN_NAME + "baseExercises/answerExercises";

    /**
     * App17/结束答题
     */
    public static String END_ANSWER = DOMAIN_NAME + "baseExercises/endAnswer";

    /**
     * App51/根据题目oid判断题目是否被采纳
     */
    public static String JUDGE_PROBLEM = DOMAIN_NAME + "baseExercises/judgeProblem";

    /**
     * App52/根据题目oid返回数据
     */
    public static String GET_PROBLEM_MESSAGE = DOMAIN_NAME + "baseExercises/getProblemMessage";

    /**
     * App56/根据token判断用户是否有未读消息
     */
    public static String JUDGE_MESSAGE = DOMAIN_NAME + "baseUserMessage/judgeMessage";

    /**
     * App59/获取APP版本信息
     */
    public static String GET_SYSTEM_VERSION = DOMAIN_NAME + "baseSystemVersion/getSystemVersion";

    /**
     * App72/答题人解答完毕
     */
    public static String END_ANSWER_BY_RESPONDENT = DOMAIN_NAME + "baseExercises/endAnswerByRespondent";

    /**
     * App73/发题推送
     */
    public static String PUSH_ALL_USER = DOMAIN_NAME + "baseExercises/pushAllUser";

    /**
     * App83/判断当前版本是否最新版本（安卓专用）
     */
    public static String JUDGE_SYSTEM_VERSION = DOMAIN_NAME + "baseSystemVersion/judgeSystemVersion";

    /**
     * App65/开启、关闭推送
     */
    public static String OPERA_TION_PUSH_SWITCH = DOMAIN_NAME + "baseUser/operationPushSwitch";

    /**
     * App106/获取商品列表
     */
    public static String GET_COMMODITY_LIST = DOMAIN_NAME + "baseCommodity/getCommodityList";

    /**
     * App107/获取商品详情
     */
    public static String GET_COMMODITY = DOMAIN_NAME + "baseCommodity/getCommodity";

    /**
     * App110/确认订单
     */
    public static String CONFIRMATION_ORDER = DOMAIN_NAME + "baseOrder/confirmationOrder";

    /**
     * App108/新增或修改用户收货地址
     */
    public static String EDIT_USER_ADDRESS = DOMAIN_NAME + "baseUserAddress/editUserAddress";

    /**
     * App111/提交订单
     */
    public static String SUBMIT_ORDER = DOMAIN_NAME + "baseOrder/submitOrder";

    /**
     * App112/获取用户订单列表
     */
    public static String GET_USER_ORDER_LIST = DOMAIN_NAME + "baseOrder/getUserOrderList";

    /**
     * App115/支付订单
     */
    public static String PAY_ORDER = DOMAIN_NAME + "baseOrder/payOrder";

    /**
     * App116/获取订单详情
     */
    public static String GET_ORDER_DETAILS = DOMAIN_NAME + "baseOrder/getOrderDetails";

    /**
     * App113/确认收货
     */
    public static String CONFIRM_RECEIPT = DOMAIN_NAME + "baseOrder/confirmReceipt";

    /**
     * App114/发表订单评价
     */
    public static String PUBLICATION_EVALUATION = DOMAIN_NAME + "baseComment/publicationEvaluation";

    /**
     * App109/获取商品评论列表
     */
    public static String GET_COMMENT_LIST = DOMAIN_NAME + "baseComment/getCommentList";

    /**
     * App117/获取我的俱乐部信息
     */
    public static String GET_CLUB_DATE = DOMAIN_NAME + "baseClub/getClubDate";


    /**
     * 用户注册
     */
    public static String ADD_USER = DOMAIN_NAME + "baseUser/addUser";

    /**
     * 实名认证
     */
    public static String UPDATE_USERREAL_NAME = DOMAIN_NAME + "baseUserRealName/updateUserRealName";

    /**
     * 教师资格认证
     */
    public static String UPDATE_USER_TEACHER_CERTIFICA = DOMAIN_NAME + "baseUserTeacherCertification/updateUserTeacherCertification";

    /**
     * 易优教师认证
     */
    public static String UPDATE_TEACHER = DOMAIN_NAME + "baseTeacher/updateTeacher";

    /**
     * 获取教师列表
     */
    public static String GET_TEACHER_LIST = DOMAIN_NAME + "baseTeacher/getTeacherList";

    /**
     * 判断是否开启家教模块
     */
    public static String JUDGE_TUTOR = DOMAIN_NAME + "baseSystem/judgeTutor";

    /**
     * App86/获取用户优惠券列表
     */
    public static String GET_USER_COUPON_LIST = DOMAIN_NAME + "baseUserCoupon/getUserCouponList";

}
