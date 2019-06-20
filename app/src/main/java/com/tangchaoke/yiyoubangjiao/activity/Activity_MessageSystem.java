package com.tangchaoke.yiyoubangjiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.MessageAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.model.MessageModel;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.utils.NetWorkUsefulUtils;
import com.tangchaoke.yiyoubangjiao.utils.ScreenLengthUtils;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.tangchaoke.yiyoubangjiao.view.PullToRefreshLayout;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 消息列表
*/
public class Activity_MessageSystem extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.recycler)
    SwipeMenuRecyclerView mRecycler;

    MessageAdapter mMessageAdapter;

    @BindView(R.id.pull_to_refresh)
    PullToRefreshLayout mPullToRefresh;

    @BindView(R.id.ll_no)
    LinearLayout mLlNo;

    @OnClick({R.id.ll_back})
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
        return R.layout.activity_message_system;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("系统消息");
    }

    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;

    @Override
    protected void initData() {
        initPulltoRefresh();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tangchaoke.yiyoubangjiao.message");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra("tuisong");
            /**
             * 如果有新的消息，在此界面要 及时进行刷新
             */
            if (num.equals("message")) {
                mPageIndex = 0;
                mPageNum = 10;
                initMessage();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initMessage();
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putString("activity", "Activity_MessageSystem");
        mEditor.commit();
    }

    private int mPageIndex = 0;
    private int mPageNum = 10;

    List<MessageModel.MessageModelModel> mMessageModelList = new ArrayList<MessageModel.MessageModelModel>();

    private void initMessage() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_MessageSystem.this, "加载中", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.GET_USER_MESSAGE_LIST)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("page.index", mPageIndex + "")
                .addParams("page.num", mPageNum + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_MessageSystem.this, "服务器开小差！请稍后重试");
                        Log.e("==消息列表：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==消息列表：：：", response);
                        Log.e("==消息列表：：：", Api.GET_USER_MESSAGE_LIST);
                        Log.e("==消息列表：：：", BaseApplication.getApplication().getToken());
                        Log.e("==消息列表：：：", mPageIndex + "");
                        Log.e("==消息列表：：：", mPageNum + "");
                        mProgressHUD.dismiss();
                        MessageModel mMessageModel = JSONObject.parseObject(response, MessageModel.class);
                        if (RequestType.SUCCESS.equals(mMessageModel.getStatus())) {
                            if (mMessageModel.getModel().size() != 0) {
                                initRecyclerMessage();
                                isShowNoData(false);
                                if (mPageIndex == 0) {
                                    mMessageAdapter.clear();
                                }
                                mMessageModelList.addAll(mMessageModel.getModel());
                            } else {
                                if (mPageIndex == 0) {
                                    isShowNoData(true);
                                }
                            }
                        } else {
                            isShowNoData(true);
                            if (mMessageModel.getStatus().equals("9") || mMessageModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mMessageModel.getStatus(), Activity_MessageSystem.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_MessageSystem.this, "登录失效");
                                }
                            } else if (mMessageModel.getStatus().equals("0")) {
                                IToast.show(Activity_MessageSystem.this, mMessageModel.getMessage());
                            }
                        }
                    }
                });

    }

    private void initRecyclerMessage() {
        LinearLayoutManager manager = new LinearLayoutManager(Activity_MessageSystem.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(manager);
        mRecycler.setLongPressDragEnabled(false);//关闭长按拖拽
        mRecycler.setItemViewSwipeEnabled(false);//禁止滑动删除
        mRecycler.setHasFixedSize(true);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.setSwipeMenuCreator(swipeMenuCreator);
        mRecycler.setSwipeMenuItemClickListener(menuItemClickListener);
        mMessageAdapter = new MessageAdapter(Activity_MessageSystem.this, mMessageModelList, new MessageAdapter.ItemOnClick() {
            @Override
            public void onItemOnClick(int position, View view) {
                Intent mIntentMessageInfo = new Intent(Activity_MessageSystem.this, Activity_MessageInfo.class);
                mIntentMessageInfo.putExtra("oid", mMessageModelList.get(position).getMessage().getOid());
                startActivity(mIntentMessageInfo);
            }
        });
        mRecycler.setAdapter(mMessageAdapter);
    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            int width = ScreenLengthUtils.deleteBtnWidth(Activity_MessageSystem.this);
            SwipeMenuItem deleteItem = new SwipeMenuItem(Activity_MessageSystem.this)
                    .setBackgroundColor(getResources().getColor(R.color.colorFF3A3A))
                    .setText("删除")
                    .setTextColor(getResources().getColor(R.color.colorPrimary))
                    .setTextSize(14)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);
        }
    };

    /**
     * 删除监听
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(com.yanzhenjie.recyclerview.swipe.Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            if (menuPosition == 0) {
                //删除某条消息
                initDeleteMessage(adapterPosition);
            }
        }
    };

    /**
     * 删除消息
     *
     * @param adapterPosition
     */
    private void initDeleteMessage(final int adapterPosition) {
        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_MessageSystem.this, "删除中", true, false, null);
        final String mOid = mMessageModelList.get(adapterPosition).getMessage().getOid();
        OkHttpUtils
                .post()
                .url(Api.DELETE_MESSAGE)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_MessageSystem.this, "服务器开小差！请稍后重试");
                        Log.e("==删除消息：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==删除消息：：：", response);
                        Log.e("==删除消息：：：", Api.DELETE_MESSAGE);
                        Log.e("==删除消息：：：", BaseApplication.getApplication().getToken());
                        Log.e("==删除消息：：：", mOid);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        mProgressHUD.dismiss();
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            IToast.show(Activity_MessageSystem.this, mSuccessModel.getMessage());
                            mMessageModelList.remove(adapterPosition);
                            mMessageAdapter.notifyDataSetChanged();
                            initMessage();
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                IToast.show(Activity_MessageSystem.this, "登录失效");
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_MessageSystem.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
    }

    private void initPulltoRefresh() {
        mPullToRefresh.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                if (NetWorkUsefulUtils.getActiveNetwork(Activity_MessageSystem.this)) {
                    mPageIndex = 0;
                    mPageNum = 10;
                    initMessage();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    IToast.show(Activity_MessageSystem.this, "" + getResources().getString(R.string.netNotUseful));
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                mPageIndex = mPageIndex + 10;
                mPageNum = 10;
                initMessage();
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
    }

    /**
     * 没有数据显示
     *
     * @param show
     */
    public void isShowNoData(boolean show) {
        if (show) {
            mLlNo.setVisibility(View.VISIBLE);
            mRecycler.setVisibility(View.GONE);
        } else {
            mLlNo.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
        }
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
