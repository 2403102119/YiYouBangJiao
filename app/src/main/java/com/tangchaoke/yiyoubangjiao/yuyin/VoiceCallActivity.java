/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tangchaoke.yiyoubangjiao.yuyin;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.EMNoActiveCallException;
import com.hyphenate.exceptions.EMServiceNotReadyException;
import com.hyphenate.util.EMLog;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Chat;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.HeadByOidModel;
import com.tangchaoke.yiyoubangjiao.model.OnlineAnswerInfoModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ZoomImageView;
import com.tangchaoke.yiyoubangjiao.xf.FloatDragView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.UUID;

import okhttp3.Call;


/**
 * 语音通话页面
 */
public class VoiceCallActivity extends CallActivity implements OnClickListener {
    private LinearLayout comingBtnContainer;
    private Button hangupBtn;
    private Button refuseBtn;
    private LinearLayout mRootLayout;
    private RelativeLayout mRlPr;
    /**
     * 问题
     */
    private Button mButProblem;
    private Button answerBtn;
    private ImageView muteImage;
    private ImageView handsFreeImage;

    private boolean isMuteState;
    private boolean isHandsfreeState;

    private TextView callStateTextView;
    private int streamID;
    private boolean endCallTriggerByMe = false;
    private Handler handler = new Handler();
    private TextView nickTextView;
    private ImageView mSwingCard;
    private TextView durationTextView;
    private Chronometer chronometer;
    String st1;
    private boolean isAnswered;
    private LinearLayout voiceContronlLayout;

    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_voice_call);

        //HXSDKHelper.getInstance().isVoiceCalling = true;

        comingBtnContainer = (LinearLayout) findViewById(R.id.ll_coming_call);
        refuseBtn = (Button) findViewById(R.id.btn_refuse_call);
        mRootLayout = (LinearLayout) findViewById(R.id.root_layout);
        mRlPr = findViewById(R.id.rl_pr);
        /**
         * 查看问题
         */
        mButProblem = (Button) findViewById(R.id.but_problem);
        answerBtn = (Button) findViewById(R.id.btn_answer_call);
        hangupBtn = (Button) findViewById(R.id.btn_hangup_call);
        muteImage = (ImageView) findViewById(R.id.iv_mute);
        handsFreeImage = (ImageView) findViewById(R.id.iv_handsfree);
        callStateTextView = (TextView) findViewById(R.id.tv_call_state);
        nickTextView = (TextView) findViewById(R.id.tv_nick);
        mSwingCard = findViewById(R.id.swing_card);
        durationTextView = (TextView) findViewById(R.id.tv_calling_duration);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        voiceContronlLayout = (LinearLayout) findViewById(R.id.ll_voice_control);

        refuseBtn.setOnClickListener(this);
        answerBtn.setOnClickListener(this);
        hangupBtn.setOnClickListener(this);
        muteImage.setOnClickListener(this);
        mButProblem.setOnClickListener(this);
        handsFreeImage.setOnClickListener(this);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        // 注册语音电话的状态的监听
        addCallStateListener();
        msgid = UUID.randomUUID().toString();

        username = getIntent().getStringExtra("username");

        mToName = getIntent().getStringExtra("to_name");

        mToHead = getIntent().getStringExtra("to_head");

        mToId = getIntent().getStringExtra("to_id");

        mProblemOid = getIntent().getStringExtra("problem_oid");

        initData(username);

        initProblem(mProblemOid);

        FloatDragView.addFloatDragView(this, mRlPr, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击事件
                ReleaseOpenPop();
            }
        });

        Log.e("==视频：：：", username + "--username--"
                + mToName + "--mToName--"
                + mToHead + "--mToHead--"
                + mToId + "--mToId--"
                + mProblemOid + "--mProblemOid--");

    }

    @Override
    protected void onResume() {
        super.onResume();
        localBroadcastManager = LocalBroadcastManager.getInstance(VoiceCallActivity.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tangchaoke.yiyoubangjiao.voice");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra("tuisong");
            if (num.equals("phone")) {
                hangupBtn.setEnabled(false);
                if (soundPool != null)
                    soundPool.stop(streamID);
                chronometer.stop();
                endCallTriggerByMe = true;
                callStateTextView.setText(getResources().getString(R.string.hanging_up));
                try {
                    EMClient.getInstance().callManager().endCall();
                } catch (Exception e) {
                    e.printStackTrace();
                    saveCallRecord(0);
                    finish();
                }
            }
        }
    }

    private void initData(final String from) {

        OkHttpUtils
                .post()
                .url(Api.GET_HEAD_BY_OID)
                .addParams("oids", from.toLowerCase())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("==视频拨打方:::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==视频拨打方:::", response);
                        HeadByOidModel mHeadByOidModel = JSONObject.parseObject(response, HeadByOidModel.class);
                        if (RequestType.SUCCESS.equals(mHeadByOidModel.getStatus())) {
                            // 语音电话是否为接收的
                            isInComingCall = getIntent().getBooleanExtra("isComingCall", false);

                            // 设置通话人
                            if (!HGTool.isEmpty(mHeadByOidModel.getModel().get(0).getNickName())) {
                                nickTextView.setText(mHeadByOidModel.getModel().get(0).getNickName());
                            } else {
                                nickTextView.setText(from);
                            }
                            if (!HGTool.isEmpty(mHeadByOidModel.getModel().get(0).getHead())) {
                                Glide.with(VoiceCallActivity.this).load(Api.PATH + mHeadByOidModel.getModel().get(0).getHead()).into(mSwingCard);
                            } else {
                                Glide.with(VoiceCallActivity.this).load(R.drawable.ic_user).into(mSwingCard);
                            }
                            if (!isInComingCall) {
                                // 拨打电话
                                soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
                                outgoing = soundPool.load(VoiceCallActivity.this, R.raw.outgoing, 1);

                                comingBtnContainer.setVisibility(View.INVISIBLE);
                                hangupBtn.setVisibility(View.VISIBLE);
                                st1 = getResources().getString(R.string.are_connected_to_each_other);
                                callStateTextView.setText(st1);
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        streamID = playMakeCallSounds();
                                    }
                                }, 300);
                                try {
                                    // 拨打语音电话
                                    String json = "{\"problem_oid\":\"" + mProblemOid + "\",\"to_id\":\"" + mToId + "\",\"to_name\":\"" + mToName + "\",\"to_head\":\"" + mToHead + "\"}";
                                    //EMChatManager.getInstance().makeVoiceCall(username);
                                    EMClient.getInstance().callManager().makeVoiceCall(username, json);
                                    Log.e("==拨打语音电话:::", json);
                                } catch (EMServiceNotReadyException e) {
                                    e.printStackTrace();
                                    final String st2 = getResources().getString(R.string.is_not_yet_connected_to_the_server);
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(VoiceCallActivity.this, st2, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } else {
                                // 有电话进来
                                voiceContronlLayout.setVisibility(View.INVISIBLE);
                                Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                                audioManager.setMode(AudioManager.MODE_RINGTONE);
                                audioManager.setSpeakerphoneOn(true);
                                ringtone = RingtoneManager.getRingtone(VoiceCallActivity.this, ringUri);
                                ringtone.play();
                            }
                        }
                    }
                });

    }

    OnlineAnswerInfoModel mOnlineAnswerInfoModel;

    private void initProblem(String mProblemOid) {
        OkHttpUtils
                .post()
                .url(Api.EXERCISES_INFORMATION)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mProblemOid)
                .addParams("type1", "Android")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(VoiceCallActivity.this, "服务器开小差！请稍后重试");
                        Log.e("==互动解答详情：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==互动解答详情：：：", response);
                        Log.e("==互动解答详情：：：", Api.EXERCISES_INFORMATION);
                        Log.e("==互动解答详情：：：", BaseApplication.getApplication().getToken());
                        Log.e("==互动解答详情：：：", VoiceCallActivity.this.mProblemOid);
                        mOnlineAnswerInfoModel = JSONObject.parseObject(response, OnlineAnswerInfoModel.class);
                        if (RequestType.SUCCESS.equals(mOnlineAnswerInfoModel.getStatus())) {

                        } else {
                            if (mOnlineAnswerInfoModel.getStatus().equals("9") || mOnlineAnswerInfoModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mOnlineAnswerInfoModel.getStatus(), VoiceCallActivity.this, true);
                                if (isAccountException == false) {
                                    IToast.show(VoiceCallActivity.this, "登录失效");
                                }
                            } else if (mOnlineAnswerInfoModel.getStatus().equals("0")) {
                                IToast.show(VoiceCallActivity.this, mOnlineAnswerInfoModel.getMessage());
                            }
                        }
                    }
                });
    }

    /**
     * 设置电话监听
     */
    void addCallStateListener() {
        callStateListener = new EMCallStateChangeListener() {

            @Override
            public void onCallStateChanged(CallState callState, CallError error) {
                // Message msg = handler.obtainMessage();
                switch (callState) {

                    case CONNECTING: // 正在连接对方
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                callStateTextView.setText(st1);
                            }

                        });
                        break;
                    case CONNECTED: // 双方已经建立连接
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                String st3 = getResources().getString(R.string.have_connected_with);
                                callStateTextView.setText(st3);
                            }

                        });
                        break;

                    case ACCEPTED: // 电话接通成功
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    if (soundPool != null)
                                        soundPool.stop(streamID);
                                } catch (Exception e) {
                                }
                                if (!isHandsfreeState)
                                    closeSpeakerOn();
                                //显示是否为直连，方便测试
                                ((TextView) findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
                                        ? R.string.direct_call : R.string.relay_call);
                                chronometer.setVisibility(View.VISIBLE);
                                chronometer.setBase(SystemClock.elapsedRealtime());
                                // 开始记时
                                chronometer.start();
                                String str4 = getResources().getString(R.string.in_the_call);
                                callStateTextView.setText(str4);
                                callingState = CallingState.NORMAL;
                            }

                        });
                        break;
                    case DISCONNECTED: // 电话断了
                        final CallError fError = error;
                        runOnUiThread(new Runnable() {
                            private void postDelayedCloseMsg() {
                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        saveCallRecord(0);
                                        Animation animation = new AlphaAnimation(1.0f, 0.0f);
                                        animation.setDuration(800);
                                        findViewById(R.id.root_layout).startAnimation(animation);
                                        finish();
//                                        if (mPopupWindowProblem.isShowing()) {
//                                            mPopupWindowProblem.dismiss();
//                                        }
                                    }

                                }, 200);
                            }

                            @Override
                            public void run() {
                                chronometer.stop();
                                callDruationText = chronometer.getText().toString();
                                String st2 = getResources().getString(R.string.the_other_party_refused_to_accept);
                                String st3 = getResources().getString(R.string.connection_failure);
                                String st4 = getResources().getString(R.string.the_other_party_is_not_online);
                                String st5 = getResources().getString(R.string.the_other_is_on_the_phone_please);
                                String st6 = getResources().getString(R.string.the_other_party_did_not_answer);
                                String st7 = getResources().getString(R.string.hang_up);
                                String st8 = getResources().getString(R.string.the_other_is_hang_up);

                                String st9 = getResources().getString(R.string.did_not_answer);
                                String st10 = getResources().getString(R.string.has_been_cancelled);
                                String st11 = getResources().getString(R.string.hang_up);

                                if (fError == CallError.REJECTED) {
                                    callingState = CallingState.BEREFUESD;
                                    callStateTextView.setText(st2);
                                } else if (fError == CallError.ERROR_TRANSPORT) {
                                    callStateTextView.setText(st3);
                                } else if (fError == CallError.ERROR_UNAVAILABLE) {
                                    callingState = CallingState.OFFLINE;
                                    callStateTextView.setText(st4);
                                } else if (fError == CallError.ERROR_BUSY) {
                                    callingState = CallingState.BUSY;
                                    callStateTextView.setText(st5);
                                } else if (fError == CallError.ERROR_NORESPONSE) {
                                    callingState = CallingState.NORESPONSE;
                                    callStateTextView.setText(st6);
                                } else {
                                    if (isAnswered) {
                                        callingState = CallingState.NORMAL;
                                        if (endCallTriggerByMe) {
//                                        callStateTextView.setText(st7);
                                        } else {
                                            callStateTextView.setText(st8);
                                        }
                                    } else {
                                        if (isInComingCall) {
                                            callingState = CallingState.UNANSWERED;
                                            callStateTextView.setText(st9);
                                        } else {
                                            if (callingState != CallingState.NORMAL) {
                                                callingState = CallingState.CANCED;
                                                callStateTextView.setText(st10);
                                            } else {
                                                callStateTextView.setText(st11);
                                            }
                                        }
                                    }
                                }
                                postDelayedCloseMsg();
                            }

                        });

                        break;

                    default:
                        break;
                }

            }
        };
        EMClient.getInstance().callManager().addCallStateChangeListener(callStateListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_problem:
                ReleaseOpenPop();
                break;
            case R.id.btn_refuse_call: // 拒绝接听
                refuseBtn.setEnabled(false);
                if (ringtone != null)
                    ringtone.stop();
                try {
                    EMClient.getInstance().callManager().rejectCall();
//                    if (mPopupWindowProblem.isShowing()) {
//                        mPopupWindowProblem.dismiss();
//                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    saveCallRecord(0);
                    finish();
                }
                callingState = CallingState.REFUESD;
                break;

            case R.id.btn_answer_call: // 接听电话
                answerBtn.setEnabled(false);
                if (ringtone != null)
                    ringtone.stop();
                if (isInComingCall) {
                    try {
                        callStateTextView.setText("正在接听...");
                        EMClient.getInstance().callManager().answerCall();
                        isAnswered = true;
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        saveCallRecord(0);
                        finish();
                        return;
                    }
                }
                comingBtnContainer.setVisibility(View.INVISIBLE);
                hangupBtn.setVisibility(View.VISIBLE);
                voiceContronlLayout.setVisibility(View.VISIBLE);
                closeSpeakerOn();
                break;

            case R.id.btn_hangup_call: // 挂断电话
                hangupBtn.setEnabled(false);
                if (soundPool != null)
                    soundPool.stop(streamID);
                chronometer.stop();
                endCallTriggerByMe = true;
                callStateTextView.setText(getResources().getString(R.string.hanging_up));
                try {
                    EMClient.getInstance().callManager().endCall();
//                    if (mPopupWindowProblem.isShowing()) {
//                        mPopupWindowProblem.dismiss();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    saveCallRecord(0);
                    finish();
                }
                break;

            case R.id.iv_mute: // 静音开关
                if (isMuteState) {
                    // 关闭静音
                    muteImage.setImageResource(R.drawable.icon_mute_normal);
                    audioManager.setMicrophoneMute(false);
                    isMuteState = false;
                } else {
                    // 打开静音
                    muteImage.setImageResource(R.drawable.icon_mute_on);
                    audioManager.setMicrophoneMute(true);
                    isMuteState = true;
                }
                break;
            case R.id.iv_handsfree: // 免提开关
                if (isHandsfreeState) {
                    // 关闭免提
                    handsFreeImage.setImageResource(R.drawable.icon_speaker_normal);
                    closeSpeakerOn();
                    isHandsfreeState = false;
                } else {
                    handsFreeImage.setImageResource(R.drawable.icon_speaker_on);
                    openSpeakerOn();
                    isHandsfreeState = true;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 发布问题弹窗
     */
    View mPopViewProblem;
    public static PopupWindow mPopupWindowProblem;
    /**
     * 左上角 返回 按钮
     */
    ImageView mImgBack;

    TextView mTvProblem;

    ImageView mImgPhono;

    String mPhotoPath;

    /**
     * 问题详情
     * <p>
     * 弹出底部对话框，达到背景背景透明效果
     * <p>
     * 实现原理：弹出一个全屏popupWind+ow,将Gravity属性设置bottom,根背景设置为一个半透明的颜色，
     * 弹出时popupWindow的半透明效果背景覆盖了在Activity之上 给人感觉上是popupWindow弹出后，背景变成半透明
     */
    private void ReleaseOpenPop() {
        mPopViewProblem = LayoutInflater.from(VoiceCallActivity.this).inflate(R.layout.popup_window_problem, null);
        mImgBack = mPopViewProblem.findViewById(R.id.img_back);
        mTvProblem = mPopViewProblem.findViewById(R.id.tv_problem);
        mImgPhono = mPopViewProblem.findViewById(R.id.img_phono);
        if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getModel().getContent())) {
            mTvProblem.setVisibility(View.VISIBLE);
            mTvProblem.setText(mOnlineAnswerInfoModel.getModel().getContent());
        } else {
            mTvProblem.setVisibility(View.GONE);
        }
        if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getModel().getPhoto())) {
            mImgPhono.setVisibility(View.VISIBLE);
            Glide.with(VoiceCallActivity.this).load(Api.PATH + mOnlineAnswerInfoModel.getModel().getPhoto()).into(mImgPhono);
        } else {
            mImgPhono.setVisibility(View.GONE);
        }
        mImgPhono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoPath = Api.PATH + mOnlineAnswerInfoModel.getModel().getPhoto();
                initPhotoPow(mPhotoPath);
            }
        });
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindowProblem.dismiss();//关闭弹窗
            }
        });
        mPopupWindowProblem = new PopupWindow(mPopViewProblem,
                GridLayoutManager.LayoutParams.MATCH_PARENT,
                GridLayoutManager.LayoutParams.MATCH_PARENT);
        // 设置弹出动画
        mPopupWindowProblem.setAnimationStyle(R.style.take_photo_anim);
        mPopupWindowProblem.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindowProblem.setFocusable(true);
        // 显示在根布局的底部
        mPopupWindowProblem.showAtLocation(mRootLayout, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    View popView;

    PopupWindow mPopupWindowPhone;

    /**
     * 弹出底部对话框，达到背景背景透明效果
     * <p>
     * 实现原理：弹出一个全屏popupWindow,将Gravity属性设置bottom,根背景设置为一个半透明的颜色，
     * 弹出时popupWindow的半透明效果背景覆盖了在Activity之上 给人感觉上是popupWindow弹出后，背景变成半透明
     *
     * @param mPhotoPath
     */
    public void initPhotoPow(String mPhotoPath) {
        popView = LayoutInflater.from(VoiceCallActivity.this).inflate(R.layout.popup_window_online_answer_info, null);
        ZoomImageView mZoomImageView = popView.findViewById(R.id.web_view_img);
        Glide.with(VoiceCallActivity.this).load(mPhotoPath).into(mZoomImageView);
        mPopupWindowPhone = new PopupWindow(popView,
                GridLayoutManager.LayoutParams.MATCH_PARENT,
                GridLayoutManager.LayoutParams.MATCH_PARENT);
        // 设置弹出动画
        mPopupWindowPhone.setAnimationStyle(R.style.take_photo_anim);
        mPopupWindowPhone.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindowPhone.setFocusable(true);
        // 顯示在根佈局的底部
        mPopupWindowPhone.showAtLocation(mRootLayout, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //HXSDKHelper.getInstance().isVoiceCalling = false;
    }

    @Override
    public void onBackPressed() {
        try {
            EMClient.getInstance().callManager().endCall();
        } catch (EMNoActiveCallException e) {
            e.printStackTrace();
        }
        callDruationText = chronometer.getText().toString();
        saveCallRecord(0);
        finish();
    }

}

