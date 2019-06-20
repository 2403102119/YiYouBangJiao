package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.model.IsOpenTutoringModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 选择认证类型
*/
public class Activity_SelectCertified extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    /**
     * 答题者认证
     */
    @BindView(R.id.ll_actor_certified)
    LinearLayout ll_actor_certified;

    @OnClick({R.id.ll_back, R.id.ll_actor_certified})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            /**
             * 答题者认证
             */
            case R.id.ll_actor_certified:
                /**
                 * 答题者 认证协议 是否同意
                 */
                if (!BaseApplication.getApplication().isActor()) {
                    if (BaseApplication.getApplication().isClub().equals("1")
                            || BaseApplication.getApplication().isClub().equals("2")
                            || BaseApplication.getApplication().isSchool().equals("1")) {
                        IToast.show(Activity_SelectCertified.this, "您暂无法认证答题者 ！");
                    } else {
                        Intent mIntentCertifiedAgreement = new Intent(Activity_SelectCertified.this, Activity_CertifiedAgreement.class);
                        mIntentCertifiedAgreement.putExtra("type", "3");//1家教 2代课老师 3答题者
                        startActivity(mIntentCertifiedAgreement);
                    }
                } else {
                    if (BaseApplication.getApplication().isClub().equals("1")
                            || BaseApplication.getApplication().isClub().equals("2")
                            || BaseApplication.getApplication().isSchool().equals("1")) {
                        IToast.show(Activity_SelectCertified.this, "您暂无法认证答题者 ！");
                    } else {
                        Intent mIntentCertifiedAgreement = new Intent(Activity_SelectCertified.this, Activity_Certified.class);
                        mIntentCertifiedAgreement.putExtra("type", "3");//1家教 2代课老师 3答题者
                        startActivity(mIntentCertifiedAgreement);
                    }
                }
                break;

        }

    }


    public int getLayoutResId() {
        return R.layout.activity_select_certified;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("认证类型");
    }

    @Override
    protected void initData() {

    }

}
