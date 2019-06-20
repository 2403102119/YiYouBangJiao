package com.tangchaoke.yiyoubangjiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.AnsweredFragmentAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.fragment.Fragment_Answered;
import com.tangchaoke.yiyoubangjiao.fragment.Fragment_HelpAnswered;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.QuantityModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 问题列表
*/
public class Activity_Answered extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private ArrayList<Fragment> mArrayList;

    AnsweredFragmentAdapter mAnsweredFragmentAdapter;

    /**
     * 已解答：：2   未解答：：0  待采纳：：1
     */
    public static String mType = "";

    @OnClick(R.id.ll_back)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                mEditor.putString("activity", "");
                mEditor.commit();
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_answered;
    }

    @Override
    public void initTitleBar() {
        if (!HGTool.isEmpty(mType)) {
            if (mType.equals("2")) {
                mTvTopTitle.setText("已解答");
            } else if (mType.equals("0")) {
                mTvTopTitle.setText("未解答");
            } else if (mType.equals("1")) {
                mTvTopTitle.setText("待采纳");
            }
        }
    }

    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;

    @Override
    protected void initData() {

        localBroadcastManager = LocalBroadcastManager.getInstance(Activity_Answered.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tangchaoke.yiyoubangjiao.home");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

        mType = getIntent().getStringExtra("type");

        mArrayList = new ArrayList<Fragment>();
        /**
         * 我提问的
         */
        mArrayList.add(new Fragment_Answered());
        /**
         * 我解答的
         */
        mArrayList.add(new Fragment_HelpAnswered());
    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra("tuisong");
            if (num.equals("popups")) {
                initQuantity();
            } else if (num.equals("answered")) {
                initQuantity();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initQuantity();
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putString("activity", "Activity_Answered");
        mEditor.commit();
    }

    /**
     * 题目未读数量
     */
    public void initQuantity() {

        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_Answered.this, "加载中", true, false, null);

        OkHttpUtils
                .post()
                .url(Api.GET_EXERCISES_NUMBER)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("type1", mType + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("==根据题目状态查询未读数量:::", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        mProgressHUD.dismiss();
                        Log.e("==根据题目状态查询未读数量:::", response);
                        Log.e("==根据题目状态查询未读数量:::", BaseApplication.getApplication().getToken());
                        Log.e("==根据题目状态查询未读数量:::", mType + "");
                        QuantityModel mQuantityModel = JSONObject.parseObject(response, QuantityModel.class);
                        if (RequestType.SUCCESS.equals(mQuantityModel.getStatus())) {
                            mAnsweredFragmentAdapter = new AnsweredFragmentAdapter(getSupportFragmentManager(), mArrayList, mQuantityModel.getModel(), Activity_Answered.this);
                            mViewPager.setOffscreenPageLimit(2);
                            mViewPager.setAdapter(mAnsweredFragmentAdapter);
                            mTabLayout.setupWithViewPager(mViewPager);
                            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                                TabLayout.Tab tab = mTabLayout.getTabAt(i);//获取tab标签
                                if (tab != null) {
                                    tab.setCustomView(mAnsweredFragmentAdapter.getTabView(i));//设置标签内容
                                }
                            }
                        } else {
                            if (mQuantityModel.getStatus().equals("9") || mQuantityModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mQuantityModel.getStatus(), Activity_Answered.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_Answered.this, "登录失效");
                                }
                            } else if (mQuantityModel.getStatus().equals("0")) {
                                IToast.show(Activity_Answered.this, mQuantityModel.getMessage());
                            }
                        }
                    }
                });

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
