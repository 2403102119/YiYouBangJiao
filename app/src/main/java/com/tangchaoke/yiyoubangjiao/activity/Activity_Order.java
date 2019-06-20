package com.tangchaoke.yiyoubangjiao.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.TabFragmentAdapter;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.fragment.Fragment_CompletOrder;
import com.tangchaoke.yiyoubangjiao.fragment.Fragment_ProcesOrder;
import com.tangchaoke.yiyoubangjiao.fragment.Fragment_WaitOrder;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/*
* @author hg
* create at 2019/1/2
* description: 订单
*/
public class Activity_Order extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private ArrayList<Fragment> mArrayList;

    TabFragmentAdapter mTabFragmentAdapter;

    private String mType = "";

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
        return R.layout.activity_order;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("我的订单");
    }

    @Override
    protected void initData() {
        mType = getIntent().getStringExtra("type");
        mArrayList = new ArrayList<Fragment>();
        /**
         * 待支付
         */
        mArrayList.add(new Fragment_WaitOrder());
        /**
         * 进行中
         */
        mArrayList.add(new Fragment_ProcesOrder());
        /**
         * 已完成
         */
        mArrayList.add(new Fragment_CompletOrder());
        mTabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), 2, mArrayList, Activity_Order.this);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mTabFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);//获取tab标签
            if (tab != null) {
                tab.setCustomView(mTabFragmentAdapter.getTabView(i));//设置标签内容
            }
        }
        if (!HGTool.isEmpty(mType)){
            if (mType.equals("2")){
                mTabLayout.getTabAt(1).select();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
