package com.tangchaoke.yiyoubangjiao.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.TabFragmentAdapter;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.fragment.Fragment_Balance;
import com.tangchaoke.yiyoubangjiao.fragment.Fragment_Integral;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/*
* @author hg
* create at 2019/1/2
* description: 交易明细
*/
public class Activity_Detail extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private ArrayList<Fragment> mArrayList;

    TabFragmentAdapter mTabFragmentAdapter;

    @OnClick({R.id.ll_back})
    void onClivk(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_detail;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("交易明细");
    }

    @Override
    protected void initData() {
        mArrayList = new ArrayList<Fragment>();
        /**
         * 余额
         */
        mArrayList.add(new Fragment_Balance());
        /**
         * 积分
         */
        mArrayList.add(new Fragment_Integral());
        mTabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), 1, mArrayList, Activity_Detail.this);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mTabFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);//获取tab标签
            if (tab != null) {
                tab.setCustomView(mTabFragmentAdapter.getTabView(i));//设置标签内容
            }
        }
    }
}
