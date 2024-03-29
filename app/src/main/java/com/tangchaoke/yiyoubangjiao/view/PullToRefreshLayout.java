package com.tangchaoke.yiyoubangjiao.view;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义的布局，
 * 用来管理三个子控件，
 * 其中一个是下拉头，
 * 一个是包含内容的pullableView（可以是实现Pullable接口的的任何View），
 * 还有一个上拉头
 *
 * @author HG
 */
public class PullToRefreshLayout extends RelativeLayout {
    // 初始状态
    public static final int INIT = 0;
    // 释放刷新
    public static final int RELEASE_TO_REFRESH = 1;
    // 正在刷新
    public static final int REFRESHING = 2;
    // 释放加载
    public static final int RELEASE_TO_LOAD = 3;
    // 正在加载
    public static final int LOADING = 4;
    // 操作完毕
    public static final int DONE = 5;
    // 当前状态
    private int state = INIT;
    // 刷新回调接口
    private OnRefreshListener mListener;
    // 刷新成功
    public static final int SUCCEED = 0;
    // 刷新失败
    public static final int FAIL = 1;
    // 按下Y坐标，上一个事件点Y坐标
    private float downY, lastY;

    // 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
    public float pullDownY = 0;
    // 上拉的距离
    private float pullUpY = 0;

    // 释放刷新的距离
    private float refreshDist = 200;
    // 释放加载的距离
    private float loadmoreDist = 200;

    private MyTimer timer;
    // 回滚速度
    public float MOVE_SPEED = 8;
    // 第一次执行布局
    private boolean isLayout = false;
    // 在刷新过程中滑动操作
    private boolean isTouch = false;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float radio = 2;

    // 下拉箭头的转180°动画
    private RotateAnimation rotateAnimation;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;

    // 下拉头
    private View mDropDownView;
    // 下拉的箭头
    private View mImgDropDown;
    // 正在刷新的图标
    private View mImgDropDownLoad;
    // 刷新结果图标
    private View mImgDropDownResult;
    // 刷新结果：成功或失败
    private TextView mTvDropDownPrompt;

    // 上拉头
    private View mPullUpView;
    // 上拉的箭头
    private View mImgPullUp;
    // 正在加载的图标
    private View mImgPullUpLoad;
    // 加载结果图标
    private View mImgPullUpResult;
    // 加载结果：成功或失败
    private TextView mTvPullUpPrompt;

    // 实现了Pullable接口的View
    private View mPullableView;

    // 过滤多点触碰
    private int mEvents;

    // 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
    private boolean mCanPullDown = true;
    private boolean mCanPullUp = true;

    private Context mContext;

    /**
     * 执行自动回滚的handler
     */
    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // 回弹速度随下拉距离moveDeltaY增大而增大
            MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
                    / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
            if (!isTouch) {
                // 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
                if (state == REFRESHING && pullDownY <= refreshDist) {
                    pullDownY = refreshDist;
                    timer.cancel();
                } else if (state == LOADING && -pullUpY <= loadmoreDist) {
                    pullUpY = -loadmoreDist;
                    timer.cancel();
                }

            }
            if (pullDownY > 0)
                pullDownY -= MOVE_SPEED;
            else if (pullUpY < 0)
                pullUpY += MOVE_SPEED;
            if (pullDownY < 0) {
                // 已完成回弹
                pullDownY = 0;
                mImgDropDown.clearAnimation();
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING)
                    changeState(INIT);
                timer.cancel();
                requestLayout();
            }
            if (pullUpY > 0) {
                // 已完成回弹
                pullUpY = 0;
                mImgPullUp.clearAnimation();
                // 隐藏上拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING)
                    changeState(INIT);
                timer.cancel();
                requestLayout();
            }
            Log.d("handle", "handle");
            // 刷新布局,会自动调用onLayout
            requestLayout();
            // 没有拖拉或者回弹完成
            if (pullDownY + Math.abs(pullUpY) == 0)
                timer.cancel();
        }

    };


    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public PullToRefreshLayout(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        timer = new MyTimer(updateHandler);
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.pullto_refresh_reverse);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.pullto_refresh_rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
    }

    private void hide() {
        timer.schedule(5);
    }

    /**
     * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
     */
    /**
     * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
     */
    public void refreshFinish(int refreshResult) {
        mImgDropDownLoad.clearAnimation();
        mImgDropDownLoad.setVisibility(View.GONE);
        switch (refreshResult) {
            case SUCCEED:
                // 刷新成功
                mImgDropDownResult.setVisibility(View.VISIBLE);
                mTvDropDownPrompt.setText(R.string.refresh_succeed);
                mImgDropDownResult
                        .setBackgroundResource(R.drawable.ic_success);
                break;
            case FAIL:
            default:
                // 刷新失败
                mImgDropDownResult.setVisibility(View.VISIBLE);
                mTvDropDownPrompt.setText(R.string.refresh_fail);
                mImgDropDownResult
                        .setBackgroundResource(R.drawable.ic_failure);
                break;
        }
        if (pullDownY > 0) {
            // 刷新结果停留1秒
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    changeState(DONE);
                    hide();
                }
            }.sendEmptyMessageDelayed(0, 500);
        } else {
            changeState(DONE);
            hide();
        }
    }

    /**
     * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
     *
     * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
     */
    public void loadmoreFinish(int refreshResult) {
        mImgPullUpLoad.clearAnimation();
        mImgPullUpLoad.setVisibility(View.GONE);
        switch (refreshResult) {
            case SUCCEED:
                // 加载成功
                mImgPullUpResult.setVisibility(View.VISIBLE);
                mTvPullUpPrompt.setText(R.string.load_succeed);
                mImgPullUpResult.setBackgroundResource(R.drawable.ic_success);
                break;
            case FAIL:
            default:
                // 加载失败
                mImgPullUpResult.setVisibility(View.VISIBLE);
                mTvPullUpPrompt.setText(R.string.load_fail);
                mImgPullUpResult.setBackgroundResource(R.drawable.ic_failure);
                break;
        }
        if (pullUpY < 0) {
            // 刷新结果停留1秒
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    changeState(DONE);
                    hide();
                }
            }.sendEmptyMessageDelayed(0, 500);
        } else {
            changeState(DONE);
            hide();
        }
    }

    private void changeState(int to) {
        state = to;
        switch (state) {
            case INIT:
                // 下拉布局初始状态
                mImgDropDownResult.setVisibility(View.GONE);
                mTvDropDownPrompt.setText(R.string.pull_to_refresh);
                mImgDropDown.clearAnimation();
                mImgDropDown.setVisibility(View.VISIBLE);
                // 上拉布局初始状态
                mImgPullUpResult.setVisibility(View.GONE);
                mTvPullUpPrompt.setText(R.string.pullup_to_load);
                mImgPullUp.clearAnimation();
                mImgPullUp.setVisibility(View.VISIBLE);
                break;
            case RELEASE_TO_REFRESH:
                // 释放刷新状态
                mTvDropDownPrompt.setText(R.string.release_to_refresh);
                mImgDropDown.startAnimation(rotateAnimation);
                break;
            case REFRESHING:
                // 正在刷新状态
                mImgDropDown.clearAnimation();
                mImgDropDownLoad.setVisibility(View.VISIBLE);
                mImgDropDown.setVisibility(View.GONE);
                mImgDropDownLoad.startAnimation(refreshingAnimation);
                mTvDropDownPrompt.setText(R.string.refreshing);
                break;
            case RELEASE_TO_LOAD:
                // 释放加载状态
                mTvPullUpPrompt.setText(R.string.release_to_load);
                mImgPullUp.startAnimation(rotateAnimation);
                break;
            case LOADING:
                // 正在加载状态
                mImgPullUp.clearAnimation();
                mImgPullUpLoad.setVisibility(View.VISIBLE);
                mImgPullUp.setVisibility(View.VISIBLE);
                mImgPullUpLoad.startAnimation(refreshingAnimation);
                mTvPullUpPrompt.setText(R.string.loading);
                break;
            case DONE:
                // 刷新或加载完毕，啥都不做
                break;
        }
    }


    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        mCanPullDown = true;
        mCanPullUp = true;
    }

    /*
     * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                lastY = downY;
                timer.cancel();
                mEvents = 0;
                releasePull();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mEvents == 0) {
                    if (pullDownY > 0
                            || (((Pullable) mPullableView).canPullDown()
                            && mCanPullDown && state != LOADING)) {
                        // 可以下拉，正在加载时不能下拉
                        // 对实际滑动距离做缩小，造成用力拉的感觉
                        pullDownY = pullDownY + (ev.getY() - lastY) / radio;
                        if (pullDownY < 0) {
                            pullDownY = 0;
                            mCanPullDown = false;
                            mCanPullUp = true;
                        }
                        if (pullDownY > getMeasuredHeight())
                            pullDownY = getMeasuredHeight();
                        if (state == REFRESHING) {
                            // 正在刷新的时候触摸移动
                            isTouch = true;
                        }
                    } else if (pullUpY < 0
                            || (((Pullable) mPullableView).canPullUp() && mCanPullUp && state != REFRESHING)) {
                        // 可以上拉，正在刷新时不能上拉
                        pullUpY = pullUpY + (ev.getY() - lastY) / radio;
                        if (pullUpY > 0) {
                            pullUpY = 0;
                            mCanPullDown = true;
                            mCanPullUp = false;
                        }
                        if (pullUpY < -getMeasuredHeight())
                            pullUpY = -getMeasuredHeight();
                        if (state == LOADING) {
                            // 正在加载的时候触摸移动
                            isTouch = true;
                        }
                    } else
                        releasePull();
                } else
                    mEvents = 0;
                lastY = ev.getY();
                // 根据下拉距离改变比例
                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
                        * (pullDownY + Math.abs(pullUpY))));
                if (pullDownY > 0 || pullUpY < 0)
                    requestLayout();
                if (pullDownY > 0) {
                    if (pullDownY <= refreshDist
                            && (state == RELEASE_TO_REFRESH || state == DONE)) {
                        // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                        changeState(INIT);
                    }
                    if (pullDownY >= refreshDist && state == INIT) {
                        // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                        changeState(RELEASE_TO_REFRESH);
                    }
                } else if (pullUpY < 0) {
                    // 下面是判断上拉加载的，同上，注意pullUpY是负值
                    if (-pullUpY <= loadmoreDist
                            && (state == RELEASE_TO_LOAD || state == DONE)) {
                        changeState(INIT);
                    }
                    // 上拉操作
                    if (-pullUpY >= loadmoreDist && state == INIT) {
                        changeState(RELEASE_TO_LOAD);
                    }

                }
                // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
                // Math.abs(pullUpY))就可以不对当前状态作区分了
                if ((pullDownY + Math.abs(pullUpY)) > 8) {
                    // 防止下拉过程中误触发长按事件和点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pullDownY > refreshDist || -pullUpY > loadmoreDist)
                // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
                {
                    isTouch = false;
                }
                if (state == RELEASE_TO_REFRESH) {
                    changeState(REFRESHING);
                    // 刷新操作
                    if (mListener != null)
                        mListener.onRefresh(this);
                } else if (state == RELEASE_TO_LOAD) {
                    changeState(LOADING);
                    // 加载操作
                    if (mListener != null)
                        mListener.onLoadMore(this);
                }
                hide();
            default:
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    /**
     * @author chenjing 自动模拟手指滑动的task
     */
    private class AutoRefreshAndLoadTask extends
            AsyncTask<Integer, Float, String> {

        @Override
        protected String doInBackground(Integer... params) {
            while (pullDownY < 4 / 3 * refreshDist) {
                pullDownY += MOVE_SPEED;
                publishProgress(pullDownY);
                try {
                    Thread.sleep(params[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            changeState(REFRESHING);
            // 刷新操作
            if (mListener != null)
                mListener.onRefresh(PullToRefreshLayout.this);
            hide();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            if (pullDownY > refreshDist)
                changeState(RELEASE_TO_REFRESH);
            requestLayout();
        }

    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        AutoRefreshAndLoadTask task = new AutoRefreshAndLoadTask();
        task.execute(20);
    }

    /**
     * 自动加载
     */
    public void autoLoad() {
        pullUpY = -loadmoreDist;
        requestLayout();
        changeState(LOADING);
        // 加载操作
        if (mListener != null)
            mListener.onLoadMore(this);
    }

    private void initView() {
        // 初始化下拉布局
        mImgDropDown = mDropDownView.findViewById(R.id.img_drop_down);
        mTvDropDownPrompt = mDropDownView.findViewById(R.id.tv_drop_down_prompt);
        mImgDropDownLoad = mDropDownView.findViewById(R.id.img_drop_down_load);
        mImgDropDownResult = mDropDownView.findViewById(R.id.img_drop_down_result);
        // 初始化上拉布局
        mImgPullUp = mPullUpView.findViewById(R.id.img_pull_up);
        mTvPullUpPrompt = mPullUpView.findViewById(R.id.tv_pull_up_prompt);
        mImgPullUpLoad = mPullUpView.findViewById(R.id.img_pull_up_load);
        mImgPullUpResult = mPullUpView.findViewById(R.id.img_pull_up_result);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d("Test", "Test");
        if (!isLayout) {
            // 这里是第一次进来的时候做一些初始化
            mDropDownView = getChildAt(0);
            mPullableView = getChildAt(1);
            mPullUpView = getChildAt(2);
            isLayout = true;
            initView();
            refreshDist = ((ViewGroup) mDropDownView).getChildAt(0)
                    .getMeasuredHeight();
            loadmoreDist = ((ViewGroup) mPullUpView).getChildAt(0)
                    .getMeasuredHeight();
        }
        // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
        mDropDownView.layout(0,
                (int) (pullDownY + pullUpY) - mDropDownView.getMeasuredHeight(),
                mDropDownView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
        mPullableView.layout(0, (int) (pullDownY + pullUpY),
                mPullableView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
                        + mPullableView.getMeasuredHeight());
        mPullUpView.layout(0,
                (int) (pullDownY + pullUpY) + mPullableView.getMeasuredHeight(),
                mPullUpView.getMeasuredWidth(),
                (int) (pullDownY + pullUpY) + mPullableView.getMeasuredHeight()
                        + mPullUpView.getMeasuredHeight());
    }

    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }

    /**
     * 刷新加载回调接口
     *
     * @author chenjing
     */
    public interface OnRefreshListener {
        /**
         * 刷新操作
         */
        void onRefresh(PullToRefreshLayout pullToRefreshLayout);

        /**
         * 加载操作
         */
        void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
    }
}
