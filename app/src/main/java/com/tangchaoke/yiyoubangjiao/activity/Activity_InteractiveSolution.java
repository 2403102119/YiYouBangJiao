package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
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
* description: 互动答题
*/
public class Activity_InteractiveSolution extends BaseActivity {

    @BindView(R.id.ll_top_activity)
    LinearLayout mLlTopActivity;

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    //界面：1首页互动答题 2我的学校互动答题
    private String mType3 = "";

    private String mLogo = "";

    @OnClick({R.id.ll_back, R.id.but_chess_class, R.id.but_cultura_lessons})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.but_chess_class:
                if (!HGTool.isEmpty(mType3)) {
                    if (mType3.equals("2")) {
                        if (BaseApplication.getApplication().isClub().equals("2")
                                || BaseApplication.getApplication().isClub().equals("1")) {
                            Intent mIntentOnlineAnswer = new Intent(Activity_InteractiveSolution.this, Activity_InteractiveSolution2.class);
                            mIntentOnlineAnswer.putExtra("type3", mType3);//界面：1首页互动答题 2我的学校互动答题
                            mIntentOnlineAnswer.putExtra("type2", "2");//问题类型:1文化课问题 2棋类问题
                            startActivity(mIntentOnlineAnswer);
                        } else {
                            IToast.show(Activity_InteractiveSolution.this, "您是该机构老师，无法查看棋类课 ！ ");
                        }
                    } else if (mType3.equals("1")) {
                        if (BaseApplication.getApplication().isClub().equals("0")) {
                            if (BaseApplication.getApplication().isSchool().equals("2")) {
                                IToast.show(Activity_InteractiveSolution.this, "您是文化课老师，无法查看棋类课 ！");
                                return;
                            }
                            //没有加入俱乐部
                            initChessClass();
                        } else if (BaseApplication.getApplication().isClub().equals("1")
                                || BaseApplication.getApplication().isClub().equals("2")
                                || BaseApplication.getApplication().isClub().equals("3")) {
                            //我是俱乐部学生
                            initCuluraLessons();
                        }
                    }
                }
                break;

            case R.id.but_cultura_lessons:
                if (!HGTool.isEmpty(mType3)) {
                    if (mType3.equals("2")) {
                        if (BaseApplication.getApplication().isClub().equals("3")
                                || BaseApplication.getApplication().isClub().equals("1")) {
                            Intent mIntentOnlineAnswer = new Intent(Activity_InteractiveSolution.this, Activity_InteractiveSolution2.class);
                            mIntentOnlineAnswer.putExtra("type3", mType3);//界面：1首页互动答题 2我的学校互动答题
                            mIntentOnlineAnswer.putExtra("type2", "1");//问题类型:1文化课问题 2棋类问题
                            startActivity(mIntentOnlineAnswer);
                        } else {
                            IToast.show(Activity_InteractiveSolution.this, "您是该机构教练，无法查看文化课 ！ ");
                        }
                    } else if (mType3.equals("1")) {
                        if (BaseApplication.getApplication().isClub().equals("0")
                                || BaseApplication.getApplication().isSchool().equals("2")) {
                            //没有加入俱乐部
                            Intent mIntentOnlineAnswer = new Intent(Activity_InteractiveSolution.this, Activity_InteractiveSolution2.class);
                            mIntentOnlineAnswer.putExtra("type3", mType3);
                            mIntentOnlineAnswer.putExtra("type2", "1");
                            startActivity(mIntentOnlineAnswer);
                        } else if (BaseApplication.getApplication().isClub().equals("1")
                                || BaseApplication.getApplication().isClub().equals("2")
                                || BaseApplication.getApplication().isClub().equals("3")) {
                            //我是俱乐部学生
                            initCuluraLessons();
                        }
                    }
                }
                break;

        }

    }

    @BindView(R.id.rl_select_online_answer_top)
    RelativeLayout mRlSelectOnlineAnswerTop;

    View mPopView;

    PopupWindow mPopupWindow;

    ImageView mImgCancelCulturaLessons;

    private void initCuluraLessons() {
        mPopView = LayoutInflater.from(Activity_InteractiveSolution.this).inflate(R.layout.popup_window_cultura_lessons, null);
        mImgCancelCulturaLessons = mPopView.findViewById(R.id.img_cancel_cultura_lessons);
        mImgCancelCulturaLessons.setOnClickListener(new View.OnClickListener() {
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
        mPopupWindow.showAtLocation(mRlSelectOnlineAnswerTop, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    /**
     * 绑定俱乐部
     */
    private void initChessClass() {
        final InputBoxDialogView mInputBoxDialogView = new InputBoxDialogView(Activity_InteractiveSolution.this);
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
                        IToast.show(Activity_InteractiveSolution.this, "服务器开小差 请稍后再试 ！ ");
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
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {

                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_InteractiveSolution.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_interactive_solution;
    }

    @Override
    public void initTitleBar() {
        if (!HGTool.isEmpty(mType3)) {
            if (mType3.equals("2")) {
                mLlTopActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_top_activity_club));
                mTvTopTitle.setText("互动答题");
                mTvTopTitle.setTextColor(getResources().getColor(R.color.color333333));
                mLlLogo.setVisibility(View.VISIBLE);
                mLlAdoption.setVisibility(View.GONE);
            } else if (mType3.equals("1")) {
                mLlTopActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_top_activity));
                mTvTopTitle.setText("互动答题");
                mTvTopTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
                mLlLogo.setVisibility(View.GONE);
                mLlAdoption.setVisibility(View.INVISIBLE);
            }
        }
    }

    @BindView(R.id.ll_logo)
    LinearLayout mLlLogo;

    @BindView(R.id.img_logo)
    CircleImageView mImgLogo;

    @BindView(R.id.ll_adoption)
    LinearLayout mLlAdoption;

    @Override
    protected void initData() {
        mType3 = getIntent().getStringExtra("type3");
        mLogo = getIntent().getStringExtra("logo");
        if (!HGTool.isEmpty(mLogo)) {
            Glide.with(Activity_InteractiveSolution.this)
                    .load(Api.PATH + mLogo)
                    .into(mImgLogo);
        }
    }

}
