package com.tangchaoke.yiyoubangjiao.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.PushRangeModel;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.model.VersionCodeModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.ApkUtils;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.utils.DataCleanUtils;
import com.tangchaoke.yiyoubangjiao.utils.UpdateApp;
import com.tangchaoke.yiyoubangjiao.view.CleanUpCacheDialogView;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.InputBoxDialogView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 设置
*/
public class Activity_Settings extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.ll_settings)
    RelativeLayout mLlSettings;

    private static final int MSG_SET_ALIAS = 1001;

    private static final int REQUEST_CODE_UNKNOWN_APP = 2001;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    tuisongSetAlias(((String) msg.obj));
                    break;
            }
        }
    };

    private static final int REQUEST_CODE_WRITE_STORAGE = 1002;

    @OnClick({R.id.ll_back, R.id.ll_binding_school, R.id.ll_updated, R.id.ll_feedback, R.id.ll_login_password_modification,
            R.id.but_out, R.id.ll_clear_caching, R.id.ll_mine_about_us, R.id.ll_push_range})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.ll_binding_school:
                if (!HGTool.isEmpty(BaseApplication.getApplication().isSchool())) {
                    if (BaseApplication.getApplication().isCoach().equals("0")//是否是教练:0不是 1大师 2俱乐部教练  isCoach
                            && BaseApplication.getApplication().isClub().equals("0")//是否有俱乐部：0没有 1俱乐部学生 2俱乐部教练 3俱乐部教师  isClub
                            && BaseApplication.getApplication().isSchool().equals("0")) {//是否有学校：0没有 1学校学生 2学校老师  isSchool
                        if (BaseApplication.getApplication().isPushRange().equals("2")) {
                            //未开通答题者权限
                            IToast.show(Activity_Settings.this, "未开通答题者权限,无法设置!");
                            return;
                        } else {
                            //没有设置
                            initBindingSchool();
                        }
                    } else if (BaseApplication.getApplication().isSchool().equals("1")) {
                        IToast.show(Activity_Settings.this, "已是在校学生，无法成为学校老师");
                        return;
                    } else if (BaseApplication.getApplication().isSchool().equals("2")) {
                        IToast.show(Activity_Settings.this, "已是学校老师，不能再次修改");
                        return;
                    } else {
                        IToast.show(Activity_Settings.this, "您的身份暂不能成为学校老师");
                        return;
                    }
                }
                break;

            case R.id.ll_push_range:
                if (!HGTool.isEmpty(BaseApplication.getApplication().isPushRange())) {
                    if (BaseApplication.getApplication().isPushRange().equals("0")) {
                        //没有设置
                        Intent mIntentPushRange = new Intent(Activity_Settings.this, Activity_PushRange.class);
                        mIntentPushRange.putExtra("type", "2");
                        mIntentPushRange.putExtra("info", "yes");
                        startActivity(mIntentPushRange);
                    } else if (BaseApplication.getApplication().isPushRange().equals("1")) {
                        //已设置
                        Intent mIntentPushRangeInfo = new Intent(Activity_Settings.this, Activity_PushRangeInfo.class);
                        startActivity(mIntentPushRangeInfo);
                    } else if (BaseApplication.getApplication().isPushRange().equals("2")) {
                        //未开通答题者权限
                        IToast.show(Activity_Settings.this, "未开通答题者权限,无法设置!");
                        return;
                    }
                }
                break;

            /**
             * 更新
             */
            case R.id.ll_updated:
                if (ContextCompat.checkSelfPermission(Activity_Settings.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) Activity_Settings.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//用户已拒绝过一次
                        //提示用户如果想要正常使用，要手动去设置中授权。
                        IToast.show(Activity_Settings.this, "请到设置-应用管理中开启此应用的读写权限");
                    } else {
                        ActivityCompat.requestPermissions(Activity_Settings.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, REQUEST_CODE_WRITE_STORAGE);
                    }
                } else {
                    downloadAPK();
                }
                break;

            /**
             * 意见与反馈
             */
            case R.id.ll_feedback:
                Intent mIntentFeedBack = new Intent(Activity_Settings.this, Activity_FeedBack.class);
                startActivity(mIntentFeedBack);
                break;

            /**
             * 登录密码修改
             */
            case R.id.ll_login_password_modification:
                Intent mIntentLoginPasswordModification = new Intent(Activity_Settings.this, Activity_LoginPasswordModification.class);
                startActivity(mIntentLoginPasswordModification);
                addActivity(Activity_Settings.this);
                break;

            /**
             * 退出
             */
            case R.id.but_out:
                ReleaseOpenPop();
                break;

            case R.id.ll_clear_caching:
                try {
                    String mClearCacheSize = DataCleanUtils.getTotalCacheSize(this);
                    final CleanUpCacheDialogView mCleanUpCacheDialogView = new CleanUpCacheDialogView(Activity_Settings.this);
                    mCleanUpCacheDialogView.setTitle("清除缓存");
                    mCleanUpCacheDialogView.setContent("需要清理" + mClearCacheSize);
                    mCleanUpCacheDialogView.setCancelable(true);
                    mCleanUpCacheDialogView.setCustomOnClickListener(new CleanUpCacheDialogView.OnCustomDialogListener() {
                        @Override
                        public void setYesOnclick() {
                            DataCleanUtils.clearAllCache(Activity_Settings.this);
                            mCleanUpCacheDialogView.dismiss();
                        }
                    });
                    mCleanUpCacheDialogView.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            /**
             * 关于我们
             */
            case R.id.ll_mine_about_us:
                Intent mIntentAboutUs = new Intent(Activity_Settings.this, Activity_AboutUs.class);
                startActivity(mIntentAboutUs);
                break;

        }

    }

    private void initBindingSchool() {
        final InputBoxDialogView mInputBoxDialogView = new InputBoxDialogView(Activity_Settings.this);
        mInputBoxDialogView.setContentInputBox("成为学校教师");
        mInputBoxDialogView.setHint("请输入所在学校教师识别码");
        mInputBoxDialogView.setNo("取消");
        mInputBoxDialogView.setCustomOnClickListener(new InputBoxDialogView.OnCustomDialogListener() {
            @Override
            public void setYesOnclick() {
                //需要请求接口 成为在校教师
                String mTeacherCode = mInputBoxDialogView.getText().toString();
                initAddBindingSchool(mTeacherCode, mInputBoxDialogView);
            }

            @Override
            public void setNoOnclick() {
                mInputBoxDialogView.dismiss();
            }
        });
        mInputBoxDialogView.setCancelable(false);
        mInputBoxDialogView.show();
    }

    private void initAddBindingSchool(final String mTeacherCode, final InputBoxDialogView mInputBoxDialogView) {
        OkHttpUtils
                .post()
                .url(Api.ADD_TEACHER_TO_SCHOOL)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("teacherCode", mTeacherCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("==学校教师绑定学校:::", e.getMessage());
                        IToast.show(Activity_Settings.this, "服务器开小差 请稍后再试 ！ ");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==学校教师绑定学校:::", response);
                        Log.e("==学校教师绑定学校:::", Api.ADD_TEACHER_TO_SCHOOL);
                        Log.e("==学校教师绑定学校:::", BaseApplication.getApplication().getToken());
                        Log.e("==学校教师绑定学校:::", mTeacherCode);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            mInputBoxDialogView.dismiss();
                            SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                            //是否有学校：0没有 1学校学生 2学校老师  isSchool
                            mEditor.putString("isSchool", "2");
                            mEditor.commit();
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {

                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_Settings.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_STORAGE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                IToast.show(Activity_Settings.this, "请到设置-应用管理中打开应用的读写权限");
                return;
            }
            downloadAPK();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UNKNOWN_APP) {
            Log.e("==xiazai:::", resultCode + "");
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

    View mPopView;

    PopupWindow mPopupWindow;

    TextView mTvOutConfirm;

    TextView mTvOutCancel;

    /**
     * 发布弹窗
     * <p>
     * 弹出底部对话框，达到背景背景透明效果
     * <p>
     * 实现原理：弹出一个全屏popupWind+ow,将Gravity属性设置bottom,根背景设置为一个半透明的颜色，
     * 弹出时popupWindow的半透明效果背景覆盖了在Activity之上 给人感觉上是popupWindow弹出后，背景变成半透明
     */
    private void ReleaseOpenPop() {
        mPopView = LayoutInflater.from(Activity_Settings.this).inflate(R.layout.popup_window_out, null);
        mTvOutConfirm = mPopView.findViewById(R.id.tv_out_confirm);
        mTvOutConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMClient.getInstance().logout(false, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        initReleaseOpenClear();
                        /**
                         * 登录之后才能注册别名
                         */
                        if (!BaseApplication.getApplication().isLogined()) {
                            JPushInterface.onResume(Activity_Settings.this);
                            tuisongSetAlias("");
                        }
                        Intent mIntentLogin = new Intent(Activity_Settings.this, Activity_Login.class);
                        startActivity(mIntentLogin);
                        finish();
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        Log.e("退出失败", progress + "..." + status);
                    }

                    @Override
                    public void onError(int code, String message) {

                    }
                });
            }
        });
        mTvOutCancel = mPopView.findViewById(R.id.tv_out_cancel);
        mTvOutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow = new PopupWindow(mPopView,
                GridLayoutManager.LayoutParams.MATCH_PARENT,
                GridLayoutManager.LayoutParams.MATCH_PARENT);
        // 设置弹出动画
        mPopupWindow.setAnimationStyle(R.style.take_photo_anim);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        // 显示在根布局的底部
        mPopupWindow.showAtLocation(mLlSettings, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    private void initReleaseOpenClear() {
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putBoolean("isLogined", false);
        mEditor.clear();
        mEditor.commit();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_settings;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("系统设置");
        Log.e("系统设置", BaseApplication.getApplication().isPush() + "---");
        if (BaseApplication.getApplication().isPush() == 0) {
            mTvPush.setText("关闭推送声音");
            switchBtn.setChecked(true);
        } else if (BaseApplication.getApplication().isPush() == 1) {
            mTvPush.setText("开启推送声音");
            switchBtn.setChecked(false);
        }
    }

    /**
     * 是否接收推送
     */
    @BindView(R.id.shopMessage_switchId)
    CheckBox switchBtn;

    String mType = "0";

    @Override
    protected void initData() {

        initLayoutDisplay();

        initVersionCode();

        initPushRange();

        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchBtn.isChecked()) {
                    mTvPush.setText("关闭推送声音");
                    mType = "0";
                } else {
                    mTvPush.setText("开启推送声音");
                    mType = "1";
                }
                initPush(mType);
            }
        });
    }

    @BindView(R.id.ll_push_range)
    LinearLayout mLlPushRange;

    @BindView(R.id.view_push_range)
    View mViewPushRange;

    @BindView(R.id.ll_binding_school)
    LinearLayout mLlBindingSchool;

    @BindView(R.id.view_binding_school)
    View mViewBindingSchool;

    /**
     * 布局显示
     */
    private void initLayoutDisplay() {
        if (BaseApplication.getApplication().isSchool().equals("2")
                && BaseApplication.getApplication().isClub().equals("0")) {//学校老师
            mLlPushRange.setVisibility(View.VISIBLE);
            mViewPushRange.setVisibility(View.VISIBLE);
            mLlBindingSchool.setVisibility(View.VISIBLE);
            mViewBindingSchool.setVisibility(View.VISIBLE);
        } else if (BaseApplication.getApplication().isSchool().equals("2")
                && BaseApplication.getApplication().isClub().equals("3")) {//俱乐部教师 + 在校老师
            mLlPushRange.setVisibility(View.VISIBLE);
            mViewPushRange.setVisibility(View.VISIBLE);
            mLlBindingSchool.setVisibility(View.VISIBLE);
            mViewBindingSchool.setVisibility(View.VISIBLE);
        } else if (BaseApplication.getApplication().isSchool().equals("0")
                && BaseApplication.getApplication().isClub().equals("3")) {//俱乐部教师
            mLlPushRange.setVisibility(View.VISIBLE);
            mViewPushRange.setVisibility(View.VISIBLE);
            mLlBindingSchool.setVisibility(View.GONE);
            mViewBindingSchool.setVisibility(View.GONE);
        } else if (BaseApplication.getApplication().isSchool().equals("0")
                && BaseApplication.getApplication().isClub().equals("0")) {//游客答题者 游客
            mLlPushRange.setVisibility(View.VISIBLE);
            mViewPushRange.setVisibility(View.VISIBLE);
            mLlBindingSchool.setVisibility(View.VISIBLE);
            mViewBindingSchool.setVisibility(View.VISIBLE);
        } else {
            mLlPushRange.setVisibility(View.GONE);
            mViewPushRange.setVisibility(View.GONE);
            mLlBindingSchool.setVisibility(View.GONE);
            mViewBindingSchool.setVisibility(View.GONE);
        }
    }

    @BindView(R.id.tv_new)
    TextView mTvNew;

    @BindView(R.id.img_new)
    ImageView mImgNew;

    private void initVersionCode() {

        OkHttpUtils
                .post()
                .url(Api.JUDGE_SYSTEM_VERSION)
                .addParams("versionCode", ApkUtils.getVersionCode(Activity_Settings.this) + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Settings.this, "服务器开小差！请稍后重试");
                        Log.e("==是否显示更新New：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==是否显示更新New：：：", response);
                        Log.e("==是否显示更新New：：：", Api.JUDGE_SYSTEM_VERSION);
                        Log.e("==是否显示更新New：：：", ApkUtils.getVersionCode(Activity_Settings.this) + "");
                        VersionCodeModel mVersionCodeModel = JSONObject.parseObject(response, VersionCodeModel.class);
                        if (mVersionCodeModel.getStatus().equals("1")) {
                            if (!HGTool.isEmpty(mVersionCodeModel.getVersionStatus())) {
                                if (mVersionCodeModel.getVersionStatus().equals("0")) {
                                    /**
                                     * 需要更新
                                     */
                                    mTvNew.setVisibility(View.VISIBLE);
                                    mImgNew.setVisibility(View.GONE);
                                } else if (mVersionCodeModel.getVersionStatus().equals("1")) {
                                    /**
                                     * 不需要更新
                                     */
                                    mImgNew.setVisibility(View.VISIBLE);
                                    mTvNew.setVisibility(View.GONE);
                                }
                            } else {
                                mImgNew.setVisibility(View.VISIBLE);
                                mTvNew.setVisibility(View.GONE);
                            }
                        }
                    }
                });

    }

    private void initPushRange() {
        if (HGTool.isEmpty(BaseApplication.getApplication().getToken())) {
            return;
        }
        OkHttpUtils
                .post()
                .url(Api.JUDGE_RESPONDENT_RANGE)
                .addParams("token", BaseApplication.getApplication().getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Settings.this, "服务器开小差！请稍后重试");
                        Log.e("====是否设置推送范围", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====是否设置推送范围", response);
                        Log.e("====是否设置推送范围", Api.JUDGE_RESPONDENT_RANGE);
                        Log.e("====是否设置推送范围", BaseApplication.getApplication().getToken());
                        PushRangeModel mPushRangeModel = JSONObject.parseObject(response, PushRangeModel.class);
                        if (RequestType.SUCCESS.equals(mPushRangeModel.getStatus())) {
                            SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                            mEditor.putString("pushRange", mPushRangeModel.getModel());
                            mEditor.commit();
                        }
                    }
                });
    }

    @BindView(R.id.tv_push)
    TextView mTvPush;

    /**
     * 推送
     */
    private void initPush(final String mType) {
        OkHttpUtils
                .post()
                .url(Api.OPERA_TION_PUSH_SWITCH)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("type", mType)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Settings.this, "服务器开小差！请稍后重试");
                        Log.e("==开启、关闭推送：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==开启、关闭推送：：：", response);
                        Log.e("==开启、关闭推送：：：", Api.OPERA_TION_PUSH_SWITCH);
                        Log.e("==开启、关闭推送：：：", BaseApplication.getApplication().getToken());
                        Log.e("==开启、关闭推送：：：", mType);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            if (switchBtn.isChecked()) {
                                mTvPush.setText("关闭推送声音");
                            } else {
                                mTvPush.setText("开启推送声音");
                            }
                            SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                            mEditor.putInt("push", Integer.parseInt(mType));
                            mEditor.commit();
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(), Activity_Settings.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_Settings.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_Settings.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
    }

    /**
     * 为用户设置极光推送的别名
     */
    private void tuisongSetAlias(String mAlias) {
        JPushInterface.setAlias(Activity_Settings.this, mAlias + "", new TagAliasCallback() {
            @Override
            public void gotResult(int code, String alias, Set<String> set) {
                switch (code) {
                    case 0://设置成功
                        BaseApplication.getApplication().setIsAliasMap(BaseApplication.getApplication().getOid(), true);
                        Log.e("==推送注册别名", BaseApplication.getApplication().getOid());
                        break;
                    case 6002://网络不稳定,稍延迟60秒后重试
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                        break;
                    case 6001:
                        Log.e("==推送注册别名", "无效的设置，tag/alias 不应参数都为 null");
                        break;
                    case 6003://alias字符串不合法
                        break;
                    case 6004://alias超长，最多40个字节
                        break;
                    case 6009://未知错误
                        break;
                    default:
                        break;
                }
            }
        });
    }

}
