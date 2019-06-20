package com.tangchaoke.yiyoubangjiao.activity;

import android.view.View;
import android.widget.TextView;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.utils.ApkUtils;
import butterknife.BindView;
import butterknife.OnClick;

/*
* @author hg
* create at 2019/1/2
* description:  关于我们
*/
public class Activity_AboutUs extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    /**
     * 项目版本号
     */
    @BindView(R.id.tv_about_us_version_name)
    TextView mTvAboutUsVersionName;

    @OnClick(R.id.ll_back)
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("关于易优帮教");
        mTvAboutUsVersionName.setText("易优帮教 V " + ApkUtils.getVersionName(this));
    }

    @Override
    protected void initData() {

    }

}
