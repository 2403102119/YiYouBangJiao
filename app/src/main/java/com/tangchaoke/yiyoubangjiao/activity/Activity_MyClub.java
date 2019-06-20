package com.tangchaoke.yiyoubangjiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.ClubDateBean;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.InputBoxDialogView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 我的学校
*/
public class Activity_MyClub extends BaseActivity {

    @BindView(R.id.ll_top_activity)
    LinearLayout mLlTopActivity;

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.rl_my_club_top)
    RelativeLayout mRlMyClubTop;

    @BindView(R.id.rl_null_club)
    RelativeLayout mRlNullClub;

    @OnClick({R.id.ll_back, R.id.img_interactive_nswer, R.id.img_teacher, R.id.img_news, R.id.but_bind_immediately})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                mEditor.putString("activity", "");
                mEditor.commit();
                break;

            case R.id.img_interactive_nswer:
                Intent mIntentOnlineAnswer = new Intent(Activity_MyClub.this, Activity_InteractiveSolution.class);
                mIntentOnlineAnswer.putExtra("type3", "2");//界面：1首页互动答题 2我的学校互动答题
                mIntentOnlineAnswer.putExtra("logo", mClubDateBean.getLogo());
                startActivity(mIntentOnlineAnswer);
                break;

            case R.id.img_teacher:
                Intent mIntentFindTeach = new Intent(Activity_MyClub.this, Activity_Coach.class);
                mIntentFindTeach.putExtra("type", "2");//界面：1首页互动答题 2我的学校互动答题
                mIntentFindTeach.putExtra("logo", mClubDateBean.getLogo());
                startActivity(mIntentFindTeach);
                break;

            case R.id.img_news:
                Intent mIntentClupStyle = new Intent(Activity_MyClub.this, Activity_News.class);
                mIntentClupStyle.putExtra("type", "2");//界面：1首页互动答题 2我的学校互动答题
                mIntentClupStyle.putExtra("logo", mClubDateBean.getLogo());
                startActivity(mIntentClupStyle);
                break;

            case R.id.but_bind_immediately:
                if (BaseApplication.getApplication().isSchool().equals("2")) {
                    IToast.show(Activity_MyClub.this, "您是在校老师，无法绑定俱乐部 ！");
                    return;
                }
                initBindImmediately();
                break;

        }

    }

    /**
     * 绑定棋类机构
     */
    private void initBindImmediately() {
        final InputBoxDialogView mInputBoxDialogView = new InputBoxDialogView(Activity_MyClub.this);
        mInputBoxDialogView.setCustomOnClickListener(new InputBoxDialogView.OnCustomDialogListener() {
            @Override
            public void setYesOnclick() {
                String mInvitationCode = mInputBoxDialogView.getText().toString();
                initJudgeUserClub(mInvitationCode, mInputBoxDialogView);
            }

            @Override
            public void setNoOnclick() {
                mInputBoxDialogView.dismiss();
            }
        });
        mInputBoxDialogView.setCancelable(false);
        mInputBoxDialogView.show();
    }

    /**
     * 加入俱乐部
     *
     * @param mInvitationCode
     * @param mInputBoxDialogView
     */
    private void initJudgeUserClub(final String mInvitationCode, final InputBoxDialogView mInputBoxDialogView) {
        OkHttpUtils
                .post()
                .url(Api.JUDGE_USER_CLUB)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("invitationCode", mInvitationCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("==加入俱乐部:::", e.getMessage());
                        IToast.show(Activity_MyClub.this, "服务器开小差 请稍后再试 ！ ");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==加入俱乐部:::", response);
                        Log.e("==加入俱乐部:::", Api.JUDGE_USER_CLUB);
                        Log.e("==加入俱乐部:::", BaseApplication.getApplication().getToken());
                        Log.e("==加入俱乐部:::", mInvitationCode);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            mInputBoxDialogView.dismiss();
                            SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                            mEditor.putString("isClub", "1");
                            mEditor.commit();
                            if (BaseApplication.getApplication().isClub().equals("0")) {
                                //没有加入俱乐部
                                mRlMyClubTop.setVisibility(View.GONE);
                                mRlNullClub.setVisibility(View.VISIBLE);
                                mLlLogo.setVisibility(View.GONE);
                                mLlAdoption.setVisibility(View.INVISIBLE);
                            } else if (BaseApplication.getApplication().isClub().equals("1")) {
                                //我是俱乐部学生
                                mRlMyClubTop.setVisibility(View.VISIBLE);
                                mRlNullClub.setVisibility(View.GONE);
                                mLlLogo.setVisibility(View.VISIBLE);
                                mLlAdoption.setVisibility(View.GONE);
                                initClubDate();
                            }
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {

                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_MyClub.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_club;
    }

    @Override
    public void initTitleBar() {
        mLlTopActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_top_activity_club));
        mTvTopTitle.setText("我的学校");
        mTvTopTitle.setTextColor(getResources().getColor(R.color.color333333));
    }

    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;

    @BindView(R.id.ll_logo)
    LinearLayout mLlLogo;

    @BindView(R.id.img_logo)
    CircleImageView mImgLogo;

    @BindView(R.id.ll_adoption)
    LinearLayout mLlAdoption;

    @Override
    protected void initData() {

        if (BaseApplication.getApplication().isClub().equals("0")) {
            //没有加入俱乐部
            mRlMyClubTop.setVisibility(View.GONE);
            mRlNullClub.setVisibility(View.VISIBLE);
            mLlLogo.setVisibility(View.GONE);
            mLlAdoption.setVisibility(View.INVISIBLE);
        } else if (BaseApplication.getApplication().isClub().equals("1")
                || BaseApplication.getApplication().isClub().equals("2")
                || BaseApplication.getApplication().isClub().equals("3")) {
            //我是俱乐部学生
            mRlMyClubTop.setVisibility(View.VISIBLE);
            mRlNullClub.setVisibility(View.GONE);
            mLlLogo.setVisibility(View.VISIBLE);
            mLlAdoption.setVisibility(View.GONE);
            initClubDate();
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tangchaoke.yiyoubangjiao.myclub");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    @BindView(R.id.tv_club_name)
    TextView mTvClubName;

    ClubDateBean mClubDateBean;

    private void initClubDate() {
        OkHttpUtils
                .post()
                .url(Api.GET_CLUB_DATE)
                .addParams("token", BaseApplication.getApplication().getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_MyClub.this, "服务器开小差 请稍后再试 ！ ");
                        Log.e("==获取我的俱乐部信息:::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==获取我的俱乐部信息:::", response);
                        Log.e("==获取我的俱乐部信息:::", Api.GET_CLUB_DATE);
                        Log.e("==获取我的俱乐部信息:::", BaseApplication.getApplication().getToken());
                        mClubDateBean = JSONObject.parseObject(response, ClubDateBean.class);
                        if (RequestType.SUCCESS.equals(mClubDateBean.getStatus())) {
                            if (!HGTool.isEmpty(mClubDateBean.getName())) {
                                mTvTopTitle.setText(mClubDateBean.getName());
                                mTvClubName.setText(mClubDateBean.getName());
                            }
                            if (!HGTool.isEmpty(mClubDateBean.getLogo())) {
                                Glide.with(Activity_MyClub.this)
                                        .load(Api.PATH + mClubDateBean.getLogo())
                                        .fitCenter()
                                        .into(mImgLogo);
                            }
                        }
                    }
                });
    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra("tuisong");
            /**
             * 如果有新的消息，在此界面要 及时进行刷新
             */
            if (num.equals("myclub")) {
                if (BaseApplication.getApplication().isClub().equals("0")) {
                    //没有加入俱乐部
                    mRlMyClubTop.setVisibility(View.GONE);
                    mRlNullClub.setVisibility(View.VISIBLE);
                    mLlLogo.setVisibility(View.GONE);
                    mLlAdoption.setVisibility(View.INVISIBLE);
                } else if (BaseApplication.getApplication().isClub().equals("1")
                        || BaseApplication.getApplication().isClub().equals("2")
                        || BaseApplication.getApplication().isClub().equals("3")) {
                    //我是俱乐部学生
                    mRlMyClubTop.setVisibility(View.VISIBLE);
                    mRlNullClub.setVisibility(View.GONE);
                    mLlLogo.setVisibility(View.VISIBLE);
                    mLlAdoption.setVisibility(View.GONE);
                    initClubDate();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putString("activity", "Activity_MyClub");
        mEditor.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
            mEditor.putString("activity", "");
            mEditor.commit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
