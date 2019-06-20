package com.tangchaoke.yiyoubangjiao.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.GuideViewPagerAdapter;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.qqtheme.framework.util.ScreenUtils;

/*
* @author hg
* create at 2019/1/2
* description: 引导页
*/
public class Activity_WelcomeGuide extends BaseActivity {

    @BindView(R.id.vp_guide)
    ViewPager mViewPager;

    private GuideViewPagerAdapter mGuideViewPagerAdapter;

    int[] mImageInt = {R.drawable.ic_vp1, R.drawable.ic_vp2, R.drawable.ic_vp3, R.drawable.ic_vp4};

    List<View> mImagesList = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.activity_welcome_guide;
    }

    @Override
    public void initTitleBar() {

    }

    @Override
    protected void initData() {
        processLogic();
        setListener();
    }

    private int currentPage;

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        //设置ViewPager的滑动监听,为了滑动到最后一页,继续滑动实现页面的跳转
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float endX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        //获取屏幕的宽度
                        int width = ScreenUtils.widthPixels(getApplicationContext());
                        //根据滑动的距离来切换界面
                        if (currentPage == 3 && startX - endX >= (width / 5)) {
                            startActivity(new Intent(Activity_WelcomeGuide.this, Activity_Main.class));
                            finish();//进入主页
                            //这部分代码是切换Activity时的动画，看起来就不会很生硬
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                            SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                            mEditor.putBoolean("isEnter", true);
                            mEditor.commit();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void processLogic() {
        for (int anImage : mImageInt) {
            ImageView mImageView = new ImageView(this);
            mImageView.setImageResource(anImage);
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mImagesList.add(mImageView);
        }
        mGuideViewPagerAdapter = new GuideViewPagerAdapter(mImagesList);
        mViewPager.setAdapter(mGuideViewPagerAdapter);
    }

}
