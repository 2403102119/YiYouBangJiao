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
import java.util.List;

/**
 * 我提问的  我解答的  tabLayout   适配器
 * Created by Administrator on 2017/11/20.
 */
public class AnsweredFragmentAdapter extends FragmentPagerAdapter {

    private String[] titles = {"我提问的", "我解答的"};
    private ArrayList<Fragment> data;
    private QuantityModel.QuantityModelModel mListQuantity;
    private Context context;

    public AnsweredFragmentAdapter(FragmentManager fm,
                                   ArrayList<Fragment> data,
                                   QuantityModel.QuantityModelModel mListQuantity,
                                   Context context) {
        super(fm);
        this.data = data;
        this.mListQuantity = mListQuantity;
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
        return titles[position];
    }

    public View getTabView(int position) {  //自定义方法getTabView
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab_layout, null);//获取标签视图
        TextView tv = view.findViewById(R.id.tv);
        TextView mTvQuantity = view.findViewById(R.id.tv_quantity);
        tv.setText(titles[position]);//设置文字
        if (position == 0) {
            if (!mListQuantity.getExercises1().equals("0")) {
                mTvQuantity.setVisibility(View.VISIBLE);
                mTvQuantity.setText(mListQuantity.getExercises1());
            } else {
                mTvQuantity.setVisibility(View.INVISIBLE);
            }
        }
        if (position == 1) {
            if (!mListQuantity.getExercises2().equals("0")) {
                mTvQuantity.setVisibility(View.VISIBLE);
                mTvQuantity.setText(mListQuantity.getExercises2());
            } else {
                mTvQuantity.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        container.removeView(data.get(position).getView());//删除页卡
    }
}