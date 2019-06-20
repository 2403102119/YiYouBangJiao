package com.tangchaoke.yiyoubangjiao.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.model.QuantityModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/10/18.
 */

public class TabFragmentAdapter extends FragmentPagerAdapter {

    /**
     * mType 1: 余额 积分  2 :待支付 进行中 已完成
     */
    private String[] titles = {"余额", "积分"};
    private String[] titlesOrd = {"待支付", "进行中", "已完成"};
    private ArrayList<Fragment> data;
    int mType;
    private Context context;

    public TabFragmentAdapter(FragmentManager fm, int mType, ArrayList<Fragment> data, Context context) {
        super(fm);
        this.data = data;
        this.mType = mType;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mType == 1) {
            return titles[position];
        }
        return titlesOrd[position];
    }

    public View getTabView(int position) {  //自定义方法getTabView
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab_layout, null);//获取标签视图
        TextView tv = view.findViewById(R.id.tv);
        TextView mTvQuantity = view.findViewById(R.id.tv_quantity);
        if (mType == 1) {
            tv.setText(titles[position]);//设置文字
        } else if (mType == 2) {
            tv.setText(titlesOrd[position]);
        }
        mTvQuantity.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        container.removeView(data.get(position).getView());//删除页卡
    }
}