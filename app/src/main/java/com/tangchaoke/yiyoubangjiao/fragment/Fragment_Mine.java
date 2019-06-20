package com.tangchaoke.yiyoubangjiao.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hedgehog.ratingbar.RatingBar;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Answered;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Balance;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Certified;
import com.tangchaoke.yiyoubangjiao.activity.Activity_CertifiedAgreement;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Main;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Message;
import com.tangchaoke.yiyoubangjiao.activity.Activity_MessageSystem;
import com.tangchaoke.yiyoubangjiao.activity.Activity_MineData;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Order;
import com.tangchaoke.yiyoubangjiao.activity.Activity_SelectCertified;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Settings;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.api.Constants;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.ContactUsModel;
import com.tangchaoke.yiyoubangjiao.model.LoginModel;
import com.tangchaoke.yiyoubangjiao.model.MessageIdentificationModel;
import com.tangchaoke.yiyoubangjiao.model.QuantityModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.WXUtil;
import com.tangchaoke.yiyoubangjiao.view.ContactUsDialogView;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class Fragment_Mine extends Fragment {

    View view;

    @BindView(R.id.img_user_head)
    CircleImageView mImgUserHead;

    @BindView(R.id.tv_nick_name)
    TextView mTvNickName;

    @BindView(R.id.rating_bar)
    RatingBar mRatingBar;

    public static Fragment_Mine newInstance() {
        Fragment_Mine fragment = new Fragment_Mine();
        return fragment;
    }

    @OnClick({R.id.ll_mine_about_us, R.id.ll_mine_settings,
            R.id.ll_mine_answered, R.id.ll_mine_unanswered, R.id.ll_mine_to_be_adopted,
            R.id.ll_balance, R.id.ll_mine_certified, R.id.ll_mine_orders, R.id.ll_mine_data, R.id.ll_contact_us,
            R.id.img_message, R.id.img_open_answer})
    void onClick(View view) {

        switch (view.getId()) {

            /**
             * 分享
             */
            case R.id.ll_mine_about_us:
                share();
                break;

            /**
             * 设置
             */
            case R.id.ll_mine_settings:
                Intent mIntentSettings = new Intent(getActivity(), Activity_Settings.class);
                startActivity(mIntentSettings);
                break;

            /**
             * 已解答
             */
            case R.id.ll_mine_answered:
                Intent mIntentAnswered = new Intent(getActivity(), Activity_Answered.class);
                mIntentAnswered.putExtra("type", "2");//0 未解答 1 待采纳 2 已解答
                startActivity(mIntentAnswered);
                break;

            /**
             * 未解答
             */
            case R.id.ll_mine_unanswered:
                Intent mIntentUnAnswered = new Intent(getActivity(), Activity_Answered.class);
                mIntentUnAnswered.putExtra("type", "0");//0 未解答 1 待采纳 2 已解答
                startActivity(mIntentUnAnswered);
                break;

            /**
             * 待采纳
             */
            case R.id.ll_mine_to_be_adopted:
                Intent mIntentToBeAdopted = new Intent(getActivity(), Activity_Answered.class);
                mIntentToBeAdopted.putExtra("type", "1");//0 未解答 1 待采纳 2 已解答
                startActivity(mIntentToBeAdopted);
                break;

            /**
             * 余额
             */
            case R.id.ll_balance:
                Intent mIntentBalance = new Intent(getActivity(), Activity_Balance.class);
                startActivity(mIntentBalance);
                break;

            /**
             * 认证
             */
            case R.id.ll_mine_certified:
                Intent mIntentSelectCertified = new Intent(getActivity(), Activity_SelectCertified.class);
                startActivity(mIntentSelectCertified);
                break;

            /**
             * 订单
             */
            case R.id.ll_mine_orders:
                Intent mIntentOrder = new Intent(getActivity(), Activity_Order.class);
                startActivity(mIntentOrder);
                break;

            /**
             * 我的资料
             */
            case R.id.ll_mine_data:
                Intent mIntentMineData = new Intent(getActivity(), Activity_MineData.class);
                startActivity(mIntentMineData);
                break;

            case R.id.ll_contact_us:
                mPhone = BaseApplication.getApplication().getConsumerHotline();
                initContactUsDialog();
                break;

            /**
             * 消息
             */
            case R.id.img_message:
                Intent mIntentMessage = new Intent(getActivity(), Activity_Message.class);
                startActivity(mIntentMessage);
                break;

            /**
             * 开通答题
             */
            case R.id.img_open_answer:
                /**
                 * 答题者 认证协议 是否同意
                 */
                if (!BaseApplication.getApplication().isActor()) {
                    if (BaseApplication.getApplication().isClub().equals("1")
                            || BaseApplication.getApplication().isClub().equals("2")
                            || BaseApplication.getApplication().isSchool().equals("1")) {
                        IToast.show(getActivity(), "您暂无法认证答题者 ！");
                    } else {
                        Intent mIntentCertifiedAgreement = new Intent(getActivity(), Activity_CertifiedAgreement.class);
                        mIntentCertifiedAgreement.putExtra("type", "3");//1家教 2代课老师 3答题者
                        startActivity(mIntentCertifiedAgreement);
                    }
                } else {
                    if (BaseApplication.getApplication().isClub().equals("1")
                            || BaseApplication.getApplication().isClub().equals("2")
                            || BaseApplication.getApplication().isSchool().equals("1")) {
                        IToast.show(getActivity(), "您暂无法认证答题者 ！");
                    } else {
                        Intent mIntentCertifiedAgreement = new Intent(getActivity(), Activity_Certified.class);
                        mIntentCertifiedAgreement.putExtra("type", "3");//1家教 2代课老师 3答题者
                        startActivity(mIntentCertifiedAgreement);
                    }
                }
                break;

        }

    }

    View mPopView;
    PopupWindow mPopupWindow;
    LinearLayout ll_wx_friend;
    LinearLayout ll_wx_pyq;
    TextView tv_avatar_cancel;

    @BindView(R.id.ll_mine)
    LinearLayout mLlMine;

    private void share() {

        /**
         * 发布弹窗
         * <p>
         * 弹出底部对话框，达到背景背景透明效果
         * <p>
         * 实现原理：弹出一个全屏popupWind+ow,将Gravity属性设置bottom,根背景设置为一个半透明的颜色，
         * 弹出时popupWindow的半透明效果背景覆盖了在Activity之上 给人感觉上是popupWindow弹出后，背景变成半透明
         */

        mPopView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window_share, null);
        ll_wx_friend = mPopView.findViewById(R.id.ll_wx_friend);
        ll_wx_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                wechatShare(false);
            }
        });
        ll_wx_pyq = mPopView.findViewById(R.id.ll_wx_pyq);
        ll_wx_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                wechatShare(true);
            }
        });
        tv_avatar_cancel = mPopView.findViewById(R.id.tv_avatar_cancel);
        tv_avatar_cancel.setOnClickListener(new View.OnClickListener() {
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
        mPopupWindow.showAtLocation(mLlMine, Gravity.BOTTOM | Gravity.LEFT, 0, 0);

    }

    private void wechatShare(boolean isChecked) {
        IWXAPI wxapi = WXAPIFactory.createWXAPI(getActivity(), Constants.WECHAT_APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        webpage.webpageUrl = Api.PATH + "ewm/index.html";
        msg.title = "易优帮教";
        msg.description = "易优帮教是当前教育创业者都在关注课外辅导的环境下，以帮助学生解决学习中的困难答疑迷惑为切入点而开发的互联网平台。  平台的优势聚焦当下帮助学习困难者解决难题，从只知答案到知其原理，提问者解决学习困惑，由学会提问变为学会解答，达到高效学习。 解答者由助力他人学习转化为助力个人收获，形成学习的转化，达到学习的共赢。";
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
        bmp.recycle();
        msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        /**
         * SendMessageToWX.Req.WXSceneTimeline 朋友圈
         *
         * SendMessageToWX.Req.WXSceneSession 好友
         */
        req.scene = isChecked ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        req.message = msg;
        wxapi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    String mPhone = "17550312524";

    /**
     * 拨打电话Dialog
     */
    private void initContactUsDialog() {
        final ContactUsDialogView mContactUsDialogView = new ContactUsDialogView(getActivity());
        mContactUsDialogView.setContent(mPhone);
        mContactUsDialogView.setCustomOnClickListener(new ContactUsDialogView.OnCustomDialogListener() {
            @Override
            public void setYesOnclick() {
                // 检查是否获得了权限（Android6.0运行时权限）
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // 不需要解释为何需要该权限，直接请求授权
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                } else {
                    initCallPhone(mPhone);
                }
                mContactUsDialogView.dismiss();
            }

            @Override
            public void setNoOnclick() {
                mContactUsDialogView.dismiss();
            }
        });
        mContactUsDialogView.setCancelable(false);
        mContactUsDialogView.show();
    }

    private void initCallPhone(String mPhone) {
        // 已经获得授权，可以打电话
        Intent intent = new Intent(); // 意图对象：动作 + 数据
        intent.setAction(Intent.ACTION_CALL); // 设置动作
        Uri data = Uri.parse("tel:" + mPhone); // 设置数据
        intent.setData(data);
        startActivity(intent); // 激活Activity组件
    }

    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tangchaoke.yiyoubangjiao.home");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
        initTitleBar();
        return view;
    }

    Activity_Main mActivityMain = new Activity_Main();

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra("tuisong");
            if (num.equals("mine")) {
                initTitleBar();
            } else if (num.equals("home")) {
                if (!HGTool.isEmpty(BaseApplication.getApplication().getToken())) {
                    initMessageIdentification();
                }
            } else if (num.equals("message_num")) {
                mActivityMain.updateUnreadLabel();
            }
        }
    }

    @BindView(R.id.img_message_identification)
    ImageView mImgMessageIdentification;

    private void initMessageIdentification() {
        OkHttpUtils
                .post()
                .url(Api.JUDGE_MESSAGE)
                .addParams("token", BaseApplication.getApplication().getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(getActivity(), "服务器开小差！请稍后重试");
                        Log.e("==根据token判断用户是否有未读消息：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==根据token判断用户是否有未读消息：：：", response);
                        Log.e("==根据token判断用户是否有未读消息：：：", Api.JUDGE_MESSAGE);
                        MessageIdentificationModel mMessageIdentification = JSONObject.parseObject(response,
                                MessageIdentificationModel.class);
                        if (RequestType.SUCCESS.equals(mMessageIdentification.getStatus())) {
                            if (mMessageIdentification.getMessageStatus().equals("0")) {
                                mImgMessageIdentification.setVisibility(View.GONE);
                            } else if (mMessageIdentification.getMessageStatus().equals("1")) {
                                mImgMessageIdentification.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        initTitleBar();
        if (!HGTool.isEmpty(BaseApplication.getApplication().getToken())) {
            initMessageIdentification();
            initQuantity();
        }
    }

    @BindView(R.id.tv_answered_quantity)
    TextView mTvAnsweredQuantity;

    @BindView(R.id.tv_unanswered_quantity)
    TextView mTvUnansweredQuantity;

    @BindView(R.id.tv_to_be_adopted_quantity)
    TextView mTvToBeAdoptedQuantity;

    /**
     * 题目未读数量
     */
    private void initQuantity() {

        OkHttpUtils
                .post()
                .url(Api.GET_EXERCISES_NUMBER)
                .addParams("token", BaseApplication.getApplication().getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("==根据题目状态查询未读数量:::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==根据题目状态查询未读数量:::", response);

                        QuantityModel mQuantityModel = JSONObject.parseObject(response, QuantityModel.class);

                        if (RequestType.SUCCESS.equals(mQuantityModel.getStatus())) {
                            if (mQuantityModel.getModel().getExercises3().equals("0")) {
                                mTvAnsweredQuantity.setVisibility(View.INVISIBLE);
                            } else {
                                mTvAnsweredQuantity.setVisibility(View.VISIBLE);
                                mTvAnsweredQuantity.setText(mQuantityModel.getModel().getExercises3());
                            }
                            if (mQuantityModel.getModel().getExercises1().equals("0")) {
                                mTvUnansweredQuantity.setVisibility(View.INVISIBLE);
                            } else {
                                mTvUnansweredQuantity.setVisibility(View.VISIBLE);
                                mTvUnansweredQuantity.setText(mQuantityModel.getModel().getExercises1());
                            }
                            if (mQuantityModel.getModel().getExercises2().equals("0")) {
                                mTvToBeAdoptedQuantity.setVisibility(View.INVISIBLE);
                            } else {
                                mTvToBeAdoptedQuantity.setVisibility(View.VISIBLE);
                                mTvToBeAdoptedQuantity.setText(mQuantityModel.getModel().getExercises2());
                            }
                        } else {
                            mTvAnsweredQuantity.setVisibility(View.INVISIBLE);
                            mTvUnansweredQuantity.setVisibility(View.INVISIBLE);
                            mTvToBeAdoptedQuantity.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }

    private void initData() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), "加载中~", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.GET_USER_INFO_BYOID)
                .addParams("model.oid", BaseApplication.getApplication().getOid())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("==根据用户ID获取用户信息 ：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==根据用户ID获取用户信息 ：：：", response);
                        Log.e("==根据用户ID获取用户信息 ：：：", Api.GET_USER_INFO_BYOID);
                        Log.e("==根据用户ID获取用户信息 ：：：", BaseApplication.getApplication().getOid());
                        LoginModel mLoginModel = JSONObject.parseObject(response, LoginModel.class);
                        if (RequestType.SUCCESS.equals(mLoginModel.getStatus())) {
                            mProgressHUD.dismiss();
                            initSaveUserInfo(mLoginModel);
                        } else {
                            mProgressHUD.dismiss();
                        }
                    }
                });
    }

    @BindView(R.id.img_open_answer)
    Button mImgOpenAnswer;

    @BindView(R.id.tv_bind_phone)
    TextView mTtvBindPhone;

    /**
     * 初始化数据
     */
    private void initTitleBar() {
        if (!BaseApplication.getApplication().getHead().equals("")) {
//            Glide.with(getActivity()).load(BaseApplication.getApplication().getHead()).into(mImgUserHead);
            Glide.with(getActivity()).load(BaseApplication.getApplication().getHead())
                    .error(R.drawable.ic_user)
                    .placeholder(R.drawable.ic_user)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            mImgUserHead.setImageDrawable(resource);
                        }
                    });
        } else {
            Glide.with(getActivity()).load(R.drawable.ic_user).into(mImgUserHead);
        }
        if (!BaseApplication.getApplication().getNickName().equals("")) {
            if (BaseApplication.getApplication().getNickName().length() > 8) {
                mTvNickName.setMaxLines(1);
                mTvNickName.setMaxEms(8);
                mTvNickName.setEllipsize(TextUtils.TruncateAt.END);
                mTvNickName.setSingleLine();
                mTvNickName.setText(BaseApplication.getApplication().getNickName());
            } else {
                mTvNickName.setText(BaseApplication.getApplication().getNickName());
            }
        } else {
            mTvNickName.setText("昵称");
        }

        if (!HGTool.isEmpty(BaseApplication.getApplication().getAccount())) {
            mTtvBindPhone.setVisibility(View.VISIBLE);
            mTtvBindPhone.setText(HGTool.isConnected(BaseApplication.getApplication().getAccount()));
        } else {
            mTtvBindPhone.setVisibility(View.INVISIBLE);
        }

        if (!HGTool.isEmpty(BaseApplication.getApplication().getGrade())) {
            float markF = Float.valueOf(BaseApplication.getApplication().getGrade());
            mRatingBar.setStar(markF);
        }
        if (!HGTool.isEmpty(BaseApplication.getApplication().isRespondent())) {
            if (BaseApplication.getApplication().isRespondent().equals("1")) {
                mImgOpenAnswer.setVisibility(View.GONE);
            } else if (BaseApplication.getApplication().isRespondent().equals("0")) {
                mImgOpenAnswer.setVisibility(View.VISIBLE);
            }
        }
    }

    // 处理权限申请的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功，继续打电话
                    initCallPhone(mPhone);
                } else {
                    // 授权失败！
                    IToast.show(getActivity(), "授权失败！");
                }
                break;
            }
        }

    }

    /**
     * 保存用户信息
     *
     * @param mLoginModel
     */
    private void initSaveUserInfo(LoginModel mLoginModel) {
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putBoolean("isLogined", true);
        mEditor.putString("token", mLoginModel.getModel().getToken());
        mEditor.putString("oid", mLoginModel.getModel().getOid());
        if (!HGTool.isEmpty(mLoginModel.getModel().getHead())) {
            if (mLoginModel.getModel().getHead().toString().contains("http://")) {
                mEditor.putString("head", mLoginModel.getModel().getHead());
            } else {
                mEditor.putString("head", Api.PATH + mLoginModel.getModel().getHead());
            }
        }
        mEditor.putString("account", mLoginModel.getModel().getAccount());
        mEditor.putString("nickName", mLoginModel.getModel().getNickName());
        mEditor.putString("grade", mLoginModel.getModel().getGrade());
        mEditor.putString("respondent", mLoginModel.getModel().getRespondent());
        if (mLoginModel.getModel().getRespondentAgreement().equals("0")) {
            /**
             * 答题者认证 协议
             */
            mEditor.putBoolean("respondentAgreement", false);
            mEditor.putString("pushRange", "0");
        } else if (mLoginModel.getModel().getRespondentAgreement().equals("1")) {
            /**
             * 答题者认证 协议
             */
            mEditor.putBoolean("respondentAgreement", true);
            mEditor.putString("pushRange", "1");
        }
        mEditor.putString("userDiplomaStatus", mLoginModel.getModel().getUserDiplomaStatus());
        mEditor.putString("studentIdCardStatus", mLoginModel.getModel().getStudentIdCardStatus());
        mEditor.putString("isCoach", mLoginModel.getModel().getIsCoach());
        mEditor.putString("isClub", mLoginModel.getModel().getIsClub());
        mEditor.putInt("pushSwitch", Integer.parseInt(mLoginModel.getModel().getPushSwitch()));
        mEditor.putString("isSchool", mLoginModel.getModel().getIsSchool());
        mEditor.commit();
    }

}
