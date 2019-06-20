package com.tangchaoke.yiyoubangjiao.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.CoachAdvantageAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.CoachInfoBean;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.view.ContactUsDialogView;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 师资简介 详情 /大师简介 详情
*/
public class Activity_CoachInfo extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.recycler_coach_advantage)
    RecyclerView mRecyclerCoachAdvantage;

    CoachAdvantageAdapter mCoachAdvantageAdapter;

    private String mOid;

    @BindView(R.id.tv_teacher_nick_name)
    TextView mTvTeacherNickName;

    @BindView(R.id.tv_city_type_sex_age)
    TextView mTvCityTypeSexAge;

    @BindView(R.id.tv_start_price)
    TextView mTvStartPrice;

    @BindView(R.id.tv_successCase)
    TextView mTvSuccessCase;

    @BindView(R.id.tv_schoolName)
    TextView mTvSchoolName;

    @BindView(R.id.tv_teachingRange)
    TextView mTvTeachingRange;

    @BindView(R.id.tv_teachingExperience)
    TextView mTvTeachingExperience;

    @BindView(R.id.img_teacher_head)
    ImageView mImgTeacherHead;

    @OnClick({R.id.ll_back, R.id.but_reserva})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.but_reserva:
                String mPhone = BaseApplication.getApplication().getConsumerHotline();
                initContactUsDialog(mPhone);
                break;

        }

    }

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    /**
     * 拨打电话Dialog
     */
    private void initContactUsDialog(final String mPhone) {
        final ContactUsDialogView mContactUsDialogView = new ContactUsDialogView(Activity_CoachInfo.this);
        mContactUsDialogView.setContent(mPhone);
        mContactUsDialogView.setCustomOnClickListener(new ContactUsDialogView.OnCustomDialogListener() {
            @Override
            public void setYesOnclick() {
                // 检查是否获得了权限（Android6.0运行时权限）
                if (ContextCompat.checkSelfPermission(Activity_CoachInfo.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // 不需要解释为何需要该权限，直接请求授权
                    ActivityCompat.requestPermissions(Activity_CoachInfo.this,
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

    @Override
    public int getLayoutResId() {
        return R.layout.activity_coach_info;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("大师详情");
    }

    @Override
    protected void initData() {
        mOid = getIntent().getStringExtra("oid");
        initCoachInfo();
    }

    private void initCoachInfo() {
        OkHttpUtils
                .post()
                .url(Api.GET_COACH_DATA)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_CoachInfo.this, "服务器开小差！请稍后重试");
                        Log.e("====教练详情 ：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====教练详情 ：：：", response);
                        Log.e("====教练详情 ：：：", Api.GET_COACH_DATA);
                        Log.e("====教练详情 ：：：", BaseApplication.getApplication().getToken());
                        Log.e("====教练详情 ：：：", mOid);
                        CoachInfoBean mCoachInfoBean = JSONObject.parseObject(response, CoachInfoBean.class);
                        if (RequestType.SUCCESS.equals(mCoachInfoBean.getStatus())) {
                            initCoachDisplay(mCoachInfoBean.getModel());
                        } else {
                            if (mCoachInfoBean.getStatus().equals("0")) {
                                IToast.show(Activity_CoachInfo.this, mCoachInfoBean.getMessage());
                            }
                        }
                    }
                });
    }

    @BindView(R.id.tv_type_chess)
    TextView mTvTypeChess;

    private void initCoachDisplay(CoachInfoBean.CoachInfoModelBean model) {

        if (!HGTool.isEmpty(model.getName())) {
            mTvTeacherNickName.setText(model.getName());
        }

        if (!HGTool.isEmpty(model.getCity()) && !HGTool.isEmpty(model.getType())
                && !HGTool.isEmpty(model.getSex()) && !HGTool.isEmpty(model.getAge())) {
            String mType = "";
            mType = model.getType().replace(",", " | ");
            mTvTypeChess.setText(mType);
//            if (model.getType().equals("1")) {
//                mType = "国际象棋";
//            } else if (model.getType().equals("2")) {
//                mType = "国际跳棋";
//            } else if (model.getType().equals("3")) {
//                mType = "围棋";
//            } else if (model.getType().equals("4")) {
//                mType = "五子棋";
//            } else if (model.getType().equals("5")) {
//                mType = "象棋";
//            }
            if (model.getSex().equals("1")) {
                mTvCityTypeSexAge.setText(model.getCity()
                        + " | " + "男"
                        + " | " + model.getAge());
                if (!HGTool.isEmpty(model.getHead())) {
                    Glide.with(Activity_CoachInfo.this).load(Api.PATH + model.getHead()).centerCrop().into(mImgTeacherHead);
                } else {
                    Glide.with(Activity_CoachInfo.this).load(R.drawable.ic_nan).into(mImgTeacherHead);
                }
            } else if (model.getSex().equals("2")) {
                mTvCityTypeSexAge.setText(model.getCity()
                        + " | " + "女"
                        + " | " + model.getAge());
                if (!HGTool.isEmpty(model.getHead())) {
                    Glide.with(Activity_CoachInfo.this).load(Api.PATH + model.getHead()).centerCrop().into(mImgTeacherHead);
                } else {
                    Glide.with(Activity_CoachInfo.this).load(R.drawable.ic_nv).into(mImgTeacherHead);
                }
            }
        }

        if (!HGTool.isEmpty(model.getPrice())) {
            mTvStartPrice.setText(model.getPrice());
        }

        if (!HGTool.isEmpty(model.getCharacteristicsofEducation())) {
            String[] arr = model.getCharacteristicsofEducation().split(",");
            List<String> mListString = Arrays.asList(arr);
            initRecyclerCoachAdvantage(mListString);
        }

        if (!HGTool.isEmpty(model.getSuccessCase())) {
            mTvSuccessCase.setText(model.getSuccessCase());
        }

        if (!HGTool.isEmpty(model.getSchoolName())) {
            mTvSchoolName.setText(model.getSchoolName());
        }

        if (!HGTool.isEmpty(model.getTeachingRange())) {
            mTvTeachingRange.setText(model.getTeachingRange());
        }

        if (!HGTool.isEmpty(model.getTeachingExperience())) {
            mTvTeachingExperience.setText(model.getTeachingExperience());
        }

    }

    private void initRecyclerCoachAdvantage(List<String> mListString) {
        mRecyclerCoachAdvantage.setHasFixedSize(true);
        mRecyclerCoachAdvantage.setNestedScrollingEnabled(false);
        mRecyclerCoachAdvantage.setLayoutManager(new GridLayoutManager(Activity_CoachInfo.this, 4));
        mRecyclerCoachAdvantage.setHasFixedSize(true);
        mRecyclerCoachAdvantage.setItemAnimator(new DefaultItemAnimator());
        mCoachAdvantageAdapter = new CoachAdvantageAdapter(Activity_CoachInfo.this, mListString);
        mRecyclerCoachAdvantage.setAdapter(mCoachAdvantageAdapter);
    }

}
