package com.tangchaoke.yiyoubangjiao.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.fragment.Fragment_Club;
import com.tangchaoke.yiyoubangjiao.fragment.Fragment_Home;
import com.tangchaoke.yiyoubangjiao.fragment.Fragment_Mine;
import com.tangchaoke.yiyoubangjiao.fragment.Fragment_PointsMall;
import com.tangchaoke.yiyoubangjiao.model.AskForVersionInfoModel;
import com.tangchaoke.yiyoubangjiao.model.ContactUsModel;
import com.tangchaoke.yiyoubangjiao.receiver.KeepAliveReceiver;
import com.tangchaoke.yiyoubangjiao.service.MyService;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.ApkUtils;
import com.tangchaoke.yiyoubangjiao.utils.UpdateApp;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.yuyin.VoiceCallActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.lang.reflect.Method;
import butterknife.BindView;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 主界面
*/
public class Activity_Main extends BaseActivity {

    public static final String mFragmentHome = "fragment_home";
    public static final String mFragmentClub = "fragment_club";
    public static final String mFragmentMessage = "fragment_message";
    public static final String mFragmentmine = "fragment_mine";

    /**
     * RadioGroup
     */
    @BindView(R.id.activity_radiogroup)
    RadioGroup mActivityRadiogroup;

    /**
     * 首頁
     */
    @BindView(R.id.activity_radiobut_home)
    RadioButton mActivityRadiobutHome;

    /**
     * 俱乐部
     */
    @BindView(R.id.activity_radiobut_club)
    RadioButton mActivityRadiobutClub;

    /**
     * 消息列表
     */
    @BindView(R.id.activity_radiobut_points_mall)
    RadioButton mActivityRadiobutPointsMall;

    /**
     * 我的
     */
    @BindView(R.id.activity_radiobut_mine)
    RadioButton mActivityRadiobutMine;

    /**
     * Fragment
     */
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Fragment mFragHome;
    Fragment mFragClub;
    Fragment mFragPointsMall;
    Fragment mFragMine;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initTitleBar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 跳转标识
         */
        if (mEach == 0) {
            /**
             * 显示的是 首页
             */
            mActivityRadiobutHome.setChecked(true);
        } else if (mEach == 1) {
            /**
             * 显示的是 找老师
             */
            mActivityRadiobutClub.setChecked(true);
        } else if (mEach == 2) {
            /**
             * 显示的是 积分商城
             */
            mActivityRadiobutPointsMall.setChecked(true);
        } else if (mEach == 3) {
            /**
             * 如果登陆成功  显示 我的界面  则 显示点击我的之前的界面
             */
            if (!BaseApplication.getApplication().isLogined()) {
                if (mAlone == 0) {
                    mActivityRadiobutHome.setChecked(true);
                } else if (mAlone == 1) {
                    mActivityRadiobutClub.setChecked(true);
                } else if (mAlone == 2) {
                    mActivityRadiobutPointsMall.setChecked(true);
                }
            } else {
                mActivityRadiobutMine.setChecked(true);
            }
        }
    }

    /**
     * 接收搜索框里面的内容
     */
    private String mSearchTeacher = "";

    /**
     * 首页的搜索  点击软键盘 的 Enter键   跳转到  找老师界面
     * <p>
     * 将 首页的搜索 传递  给  找老师界面
     *
     * @param mSearch
     */
    public void setSearch(String mSearch) {
        mSearchTeacher = mSearch;
    }

    public String getSearch() {
        return mSearchTeacher;
    }

    /**
     * 跳转  Receiver
     */
    MyBradCastFindTeacher mMyBradCastFindTeacher = new MyBradCastFindTeacher();

    /**
     * 视频聊天 Receiver
     */
    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter mIntentFilter = new IntentFilter("com.tangchaoke.yiyoubangjiao.fragment.club");
        registerReceiver(mMyBradCastFindTeacher, mIntentFilter);
        /**
         * 首次进来显示 首页 界面
         */
        if (savedInstanceState == null) {
            mFragmentManager = getSupportFragmentManager();
            mFragHome = Fragment_Home.newInstance();
            mFragmentManager.beginTransaction().replace(R.id.fl_container, mFragHome, mFragmentHome).commit();
        }
        //1776
        Log.e("===获取屏幕尺寸:::", getTotalHeight(Activity_Main.this) + "====" + getScreenHeight(Activity_Main.this));
    }

    /**
     * @param context
     * @return 获取屏幕原始尺寸高度，包括虚拟功能键高度
     */
    public static int getTotalHeight(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * @param context
     * @return 获取屏幕内容高度不包括虚拟按键
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra("tuisong");
            String toChatUsername = intent.getStringExtra("oid");
            String mMeName = intent.getStringExtra(EaseConstant.TO_NAME);
            String mMeHead = intent.getStringExtra(EaseConstant.TO_HEAD);
            String mMeId = intent.getStringExtra(EaseConstant.TO_ID);
            String mProblemOid = intent.getStringExtra(EaseConstant.PROBLEM_OID);
            if (num.equals("voice")) {
                if (!EMClient.getInstance().isConnected()) {
                    Toast.makeText(Activity_Main.this, R.string.not_connect_to_server,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent1 = new Intent(Activity_Main.this, VoiceCallActivity.class);
                    intent1.putExtra("username", toChatUsername);
                    intent1.putExtra("to_name", mMeName);
                    intent1.putExtra("to_head", mMeHead);
                    intent1.putExtra("to_id", mMeId);
                    intent1.putExtra("problem_oid", mProblemOid);
                    intent1.putExtra("isComingCall", false);
                    startActivity(intent1);
                }
            } else if (num.equals("message")) {
                updateUnreadLabel();
            }
        }

    }

    /**
     * 跳转 Receiver
     */
    class MyBradCastFindTeacher extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mActivityRadiobutClub.setChecked(true);
            Log.e("==获取俱乐部列表Main:::", getSearch() + ":::mSearchText");
        }
    }

    /**
     * 设置标识  首页 0  找老师 1  消息列表  2  我的 3
     * <p>
     * 在没有登录的时候  点击消息列表 和  我的  只能 跳转到 登录界面
     */
    int mEach = 0;

    /**
     * 设置标识  首页 0  找老师 1 积分商城 2
     * <p>
     * 在没有登录的时候  点击消息列表 和  我的  只能 跳转到 登录界面
     * <p>
     * 如果用戶到登录界面没有登录  点击返回  就会回到刚才  点击消息列表或我的  之前的界面  （首页 或  找老师）
     */
    int mAlone = 0;

    @Override
    protected void initData() {
        mTvMessage = findViewById(R.id.tv_message);
        initContactUs();
        askForVersionInfo();
        updateUnreadLabel();
        initReceiver();
        mActivityRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragHome = mFragmentManager.findFragmentByTag(mFragmentHome);
                mFragClub = mFragmentManager.findFragmentByTag(mFragmentClub);
                mFragPointsMall = mFragmentManager.findFragmentByTag(mFragmentMessage);
                mFragMine = mFragmentManager.findFragmentByTag(mFragmentmine);
                if (mFragHome != null) {
                    mFragmentTransaction.hide(mFragHome);
                }
                if (mFragClub != null) {
                    mFragmentTransaction.hide(mFragClub);
                }
                if (mFragPointsMall != null) {
                    mFragmentTransaction.hide(mFragPointsMall);
                }
                if (mFragMine != null) {
                    mFragmentTransaction.hide(mFragMine);
                }
                switch (checkedId) {
                    /**
                     * 首页
                     */
                    case R.id.activity_radiobut_home:
                        mEach = 0;
                        mAlone = 0;
                        if (mFragHome == null) {
                            mFragHome = Fragment_Home.newInstance();
                            mFragmentTransaction.add(R.id.fl_container, mFragHome, mFragmentHome);
                        } else {
                            mFragmentTransaction.show(mFragHome);
                        }
                        break;
                    /**
                     * 俱乐部
                     */
                    case R.id.activity_radiobut_club:
                        mEach = 1;
                        mAlone = 1;
                        if (mFragClub == null) {
                            mFragClub = Fragment_Club.newInstance();
                            mFragmentTransaction.add(R.id.fl_container, mFragClub, mFragmentClub);
                        } else {
                            mFragmentTransaction.show(mFragClub);
                        }
                        break;
                    /**
                     * 积分商城
                     */
                    case R.id.activity_radiobut_points_mall:
                        mEach = 2;
                        mAlone = 2;
                        if (mFragPointsMall == null) {
                            mFragPointsMall = Fragment_PointsMall.newInstance();
                            mFragmentTransaction.add(R.id.fl_container, mFragPointsMall, mFragmentMessage);
                        } else {
                            mFragmentTransaction.show(mFragPointsMall);
                        }
                        break;
                    /**
                     * 我的
                     */
                    case R.id.activity_radiobut_mine:
                        mEach = 3;
                        if (!BaseApplication.getApplication().isLogined()) {
                            Intent mIntentMine = new Intent(Activity_Main.this, Activity_Login.class);
                            mIntentMine.putExtra("name", "mine");
                            startActivity(mIntentMine);
                        }
                        if (mFragMine == null) {
                            mFragMine = Fragment_Mine.newInstance();
                            mFragmentTransaction.add(R.id.fl_container, mFragMine, mFragmentmine);
                        } else {
                            mFragmentTransaction.show(mFragMine);
                        }
                        break;
                }
                mFragmentTransaction.commit();
            }
        });
    }

    /**
     * 获取客服电话
     */
    private void initContactUs() {
        OkHttpUtils
                .get()
                .url(Api.GET_PHONE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Main.this, "服务器开小差！请稍后重试");
                        Log.e("====获取客服电话", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====获取客服电话", Api.GET_PHONE);
                        Log.e("====获取客服电话", response);
                        ContactUsModel mContactUsModel = JSONObject.parseObject(response, ContactUsModel.class);
                        if (RequestType.SUCCESS.equals(mContactUsModel.getStatus())) {
                            SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                            mEditor.putString("consumer_hotline", mContactUsModel.getContent());
                            mEditor.commit();
                        }
                    }
                });
    }

    private static final int REQUEST_CODE_WRITE_STORAGE = 1002;

    private static final int REQUEST_CODE_UNKNOWN_APP = 2001;

    /**
     * 检测版本信息
     */
    public void askForVersionInfo() {
        OkHttpUtils
                .get()
                .url(Api.GET_SYSTEM_VERSION)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Main.this, "服务器开小差！请稍后重试");
                        Log.e("==更新：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==更新：：：", response);
                        AskForVersionInfoModel mAskForVersionInfoModel = JSONObject.parseObject(response, AskForVersionInfoModel.class);
                        if (RequestType.SUCCESS.equals(mAskForVersionInfoModel.getStatus())) {
                            if (mAskForVersionInfoModel.getModel() != null) {
                                if (ApkUtils.getVersionCode(Activity_Main.this) <
                                        mAskForVersionInfoModel.getModel().getVersionCode()) {
                                    if (ContextCompat.checkSelfPermission(Activity_Main.this,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) Activity_Main.this,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//用户已拒绝过一次
                                            //提示用户如果想要正常使用，要手动去设置中授权。
                                            IToast.show(Activity_Main.this, "请到设置-应用管理中开启此应用的读写权限");
                                        } else {
                                            ActivityCompat.requestPermissions(Activity_Main.this,
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                                                    }, REQUEST_CODE_WRITE_STORAGE);
                                        }
                                    } else {
                                        downloadAPK();
                                    }
                                } else {
                                    return;
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @android.support.annotation.NonNull String[] permissions,
                                           @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_STORAGE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                IToast.show(Activity_Main.this, "请到设置-应用管理中打开应用的读写权限");
                return;
            }
            downloadAPK();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UNKNOWN_APP) {
            downloadAPK();
        }
    }

    private void downloadAPK() {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                //检查版本
                new UpdateApp(this, new ProgressDialog(this)).checkUpdate();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                startActivityForResult(intent, REQUEST_CODE_UNKNOWN_APP);
            }
        } else {
            //检查版本
            new UpdateApp(this, new ProgressDialog(this)).checkUpdate();
        }
    }

    /**
     * 程序保活广播
     */
    private KeepAliveReceiver mKeepAliveReceiver;

    /**
     * 注册 Receiver
     */
    private void initReceiver() {
        //启动保证极光推送的Receiver不被系统杀死的service
        Intent intent = new Intent(this, MyService.class);
        startService(intent);

        /* 注册广播，监听手机屏幕事件 */
        mKeepAliveReceiver = new KeepAliveReceiver(Activity_Main.this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON); //亮屏
        filter.addAction(Intent.ACTION_SCREEN_OFF); //锁屏、黑屏
        this.registerReceiver(mKeepAliveReceiver, filter);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tangchaoke.yiyoubangjiao.home");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {//在onDestroy()方法中取消注册。
        super.onDestroy();
        if (mKeepAliveReceiver != null) {
            this.unregisterReceiver(mKeepAliveReceiver);
            mKeepAliveReceiver = null;
        }
        //取消注册调用的是unregisterReceiver()方法，并传入接收器实例。
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    static TextView mTvMessage;

    /**
     * 更新未读邮件计数
     */
    public static void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            mTvMessage.setText(String.valueOf(count));
            mTvMessage.setVisibility(View.VISIBLE);
        } else {
            mTvMessage.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取未读消息计数
     *
     * @return
     */
    public static int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }

}
