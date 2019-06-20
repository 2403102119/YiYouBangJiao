package com.tangchaoke.yiyoubangjiao.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/*
* @author hg
* create at 2019/1/2
* description: 提交成功
*/
public class Activity_Success extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @OnClick({R.id.ll_back})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_success;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("提交成功");
    }

    @Override
    protected void initData() {

    }
}
