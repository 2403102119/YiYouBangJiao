package com.tangchaoke.yiyoubangjiao.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.support.multidex.MultiDex;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseUI;
import com.tangchaoke.yiyoubangjiao.hx.PreferenceManager;
import com.tangchaoke.yiyoubangjiao.service.MyService;
import com.tangchaoke.yiyoubangjiao.tuisong.DemoHelper;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2018/3/19.
 */

public class BaseApplication extends Application {

    public static BaseApplication mBaseApplication;

    private SharedPreferences mSharedPreferences;

    private SharedPreferences.Editor mEditor;

    public static Context mContext;

    private boolean isLogined;

    private Map<String, Boolean> isAliasMap = new HashMap<>();


    public static BaseApplication getApplication() {
        return mBaseApplication;
    }

    public static AudioManager mAudioManager;

    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);
        mBaseApplication = this;
        initSharedpre();
        isLogined = false;
        mContext = this;

        EaseUI.getInstance().init(this, null);
        EMClient.getInstance().setDebugMode(true);

        //init demo helper
        DemoHelper.getInstance().init(mBaseApplication);

        initJpush();

        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:【友盟+】 AppKey
         * 参数3:【友盟+】 Channel
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret，需要集成Push功能时必须传入Push的secret，否则传空。
         */
        UMConfigure.init(mContext, "5b02214df29d9821de0000f4", "YYBJ", UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(true);
        //启动保证极光推送的Receiver不被系统杀死的service
        Intent mIntentMyService = new Intent(this, MyService.class);
        startService(mIntentMyService);
        PlatformConfig.setWeixin("wx74bb6ff88fc1c2b6", "9aa2da5a5151be5d32969c38cb66b15b");
        Config.DEBUG = true;

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 设置 app 不随着系统字体的调整而变化
     */
    /**
     * 限制应用字体不随着系统字体大小的变化而变化
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            // 非默认值
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {  // 非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults(); // 设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    private void initJpush() {
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        JPushInterface.setLatestNotificationNumber(this, 3);
    }

    public void setIsAliasMap(String key, boolean isAlias) {
        this.isAliasMap.put(key, isAlias);
    }

    private void initSharedpre() {
        mSharedPreferences = getSharedPreferences("userMsg", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return mEditor;
    }

    public static Context getContext() {
        return mContext;
    }

    public boolean isLogined() {
        return mSharedPreferences.getBoolean("isLogined", false);
    }

    public boolean isEnter() {
        return mSharedPreferences.getBoolean("isEnter", false);
    }

    /**
     * 用户  token
     *
     * @return
     */
    public String getToken() {
        return mSharedPreferences.getString("token", "");
    }

    /**
     * 用户  oid
     *
     * @return
     */
    public String getOid() {
        return mSharedPreferences.getString("oid", "");
    }

    /**
     * 用户  头像
     *
     * @return
     */
    public String getHead() {
        return mSharedPreferences.getString("head", "");
    }

    /**
     * 用户  昵称
     *
     * @return
     */
    public String getNickName() {
        return mSharedPreferences.getString("nickName", "");
    }

    /**
     * 等级
     *
     * @return
     */
    public String getGrade() {
        return mSharedPreferences.getString("grade", "");
    }

    /**
     * 帐号
     *
     * @return
     */
    public String getAccount() {
        return mSharedPreferences.getString("account", "");
    }

    /**
     * 判断答题者是否设置推送范围
     *
     * @return
     */
    public String isPushRange() {
        return mSharedPreferences.getString("pushRange", "");
    }

    /**
     * 是否认证答题者:0未认证 1已认证
     *
     * @return
     */
    public String isRespondent() {
        return mSharedPreferences.getString("respondent", "");
    }

    /**
     * 答题者协议同意状态:0未同意 1已同意 respondentAgreement
     *
     * @return
     */
    public boolean isActor() {
        return mSharedPreferences.getBoolean("respondentAgreement", false);
    }

    /**
     * 学历认证状态:0认证中 1认证失败 2认证成功 3未认证  userDiplomaStatus
     *
     * @return
     */
    public String isUserDiplomaStatus() {
        return mSharedPreferences.getString("userDiplomaStatus", "");
    }

    /**
     * 学生证认证状态:0认证中 1认证失败 2认证成功 3未认证  studentIdCardStatus
     *
     * @return
     */
    public String isStudentIdCardStatus() {
        return mSharedPreferences.getString("studentIdCardStatus", "");
    }

    /**
     * 是否是教练:0不是 1大师 2俱乐部教练  isCoach
     *
     * @return
     */
    public String isCoach() {
        return mSharedPreferences.getString("isCoach", "");
    }

    /**
     * 是否有俱乐部：0没有 1俱乐部学生 2俱乐部教练 3俱乐部教师  isClub
     *
     * @return
     */
    public String isClub() {
        return mSharedPreferences.getString("isClub", "");
    }

    /**
     * 是否有学校：0没有 1学校学生 2学校老师  isSchool
     *
     * @return
     */
    public String isSchool() {
        return mSharedPreferences.getString("isSchool", "");
    }

    /**
     * 推送开关:0开启 1关闭  pushSwitch
     *
     * @return
     */
    public String isPushSwitch() {
        return mSharedPreferences.getString("pushSwitch", "");
    }

    /**
     * Activity
     *
     * @return
     */
    public String getActivity() {
        return mSharedPreferences.getString("activity", "");
    }

    /**
     * 客服电话
     *
     * @return
     */
    public String getConsumerHotline() {
        return mSharedPreferences.getString("consumer_hotline", "");
    }

    /**
     * 是否认证家教  不需要
     *
     * @return
     */
    public String isTutor() {
        return mSharedPreferences.getString("tutor", "");
    }

    /**
     * 是否认证代课老师  不需要
     *
     * @return
     */
    public String isSubstituteTeacher() {
        return mSharedPreferences.getString("substituteTeacher", "");
    }

    /**
     * 代课老师协议  不需要
     *
     * @return
     */
    public boolean isSubstitution() {
        return mSharedPreferences.getBoolean("substitution", false);
    }

    /**
     * 家教协议协议  不需要
     *
     * @return
     */
    public boolean isTutoring() {
        return mSharedPreferences.getBoolean("tutoring", false);
    }

    /**
     * 用户实名认证状态  不需要
     *
     * @return
     */
    public String isUserRealNameStatus() {
        return mSharedPreferences.getString("userRealNameStatus", "");
    }

    /**
     * 教师资格证认证状态  不需要
     *
     * @return
     */
    public String isUserTeacherCertificationStatus() {
        return mSharedPreferences.getString("userTeacherCertificationStatus", "");
    }

    /**
     * 代课老师 易优教师认证状态  不需要
     *
     * @return
     */
    public String isTeacherInfoStatus() {
        return mSharedPreferences.getString("teacherInfoStatus", "");
    }

    /**
     * 家教老师 易优教师认证状态  不需要
     *
     * @return
     */
    public String isTutorInfoStatus() {
        return mSharedPreferences.getString("tutorInfoStatus", "");
    }

    /**
     * 家教老师 易优教师认证状态  不需要
     *
     * @return
     */
    public int isPush() {
        return mSharedPreferences.getInt("push", 0);
    }

}
