package com.tangchaoke.yiyoubangjiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.utils.NetWorkUsefulUtils;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.PullToRefreshLayout;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/*
* @author hg
* create at 2019/1/2
* description: 消息
*/
public class Activity_Message extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;

    /**
     * 刷新
     */
    @BindView(R.id.pull_to_refresh)
    PullToRefreshLayout mPullToRefresh;

    @BindView(R.id.ll_no_message)
    LinearLayout mLlNoBalanceInfo;

    @BindView(R.id.container)
    FrameLayout mContainer;

    EaseConversationListFragment mConversationListFragment;

    @OnClick({R.id.ll_back, R.id.ll_message_top})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.ll_message_top:
                Intent mIntentMessageSystem = new Intent(Activity_Message.this, Activity_MessageSystem.class);
                startActivity(mIntentMessageSystem);
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_message;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("消息");
    }

    @Override
    protected void initData() {
        initPulltoRefresh();
        initMessageList();
        localBroadcastManager = LocalBroadcastManager.getInstance(Activity_Message.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tangchaoke.yiyoubangjiao.home");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra("tuisong");
            if (num.equals("home")) {
                /**
                 * 是否有未读消息
                 */
                if (!HGTool.isEmpty(BaseApplication.getApplication().getToken())) {
//                    initMessageIdentification();
                }
            } else if (num.equals("message")) {
                /**
                 * 有新的聊天窗口
                 */
                initData();
            } else if (num.equals("message_num")) {
                /**
                 * 未读条数
                 */
//                mActivityMain.updateUnreadLabel();
            } else if (num.equals("answer")) {
                /**
                 *
                 */
                initData();
            } else if (num.equals("delete")) {
                /**
                 * 删除聊天会话
                 */
                initData();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!HGTool.isEmpty(BaseApplication.getApplication().getToken())) {
//            initMessageIdentification();
        }
    }

    /**
     * 消息列表
     */
    private void initMessageList() {
        /**
         * 消息列表总条数
         */
        Map<String, EMConversation> mConversation = EMClient.getInstance().chatManager().getAllConversations();
        if (mConversation.size() != 0) {
            isShowNoData(false);
            Activity_Main.updateUnreadLabel();
            //会话列表
            if (mConversationListFragment == null) {
                mConversationListFragment = new EaseConversationListFragment();
                mConversationListFragment.setArguments(Activity_Message.this.getIntent().getExtras());
                Activity_Message.this.getSupportFragmentManager().beginTransaction().add(R.id.container,
                        mConversationListFragment).commitAllowingStateLoss();
                mConversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
                    @Override
                    public void onListItemClicked(final EMConversation conversation) {
                        Intent mIntentChat = new Intent(Activity_Message.this, Activity_Chat.class);
                        mIntentChat.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
                        mIntentChat.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
                        mIntentChat.putExtra("oid", conversation.getLastMessage().getStringAttribute(EaseConstant.PROBLEM_OID, ""));
                        mIntentChat.putExtra(EaseConstant.TOKEN, BaseApplication.getApplication().getToken());//自己的token
                        mIntentChat.putExtra(EaseConstant.ISCARRYON, "no");
                        mIntentChat.putExtra(EaseConstant.PROBLEM_OID, conversation.getLastMessage().getStringAttribute(EaseConstant.PROBLEM_OID, ""));//问题ID
                        mIntentChat.putExtra(EaseConstant.ME_ID, BaseApplication.getApplication().getOid());//自己的ID
                        mIntentChat.putExtra(EaseConstant.ME_HEAD, BaseApplication.getApplication().getHead());//自己的头像
                        mIntentChat.putExtra(EaseConstant.ME_NAME, BaseApplication.getApplication().getNickName());//自己的名字
                        startActivity(mIntentChat);
                    }
                });
            } else {
                mConversationListFragment.refresh();
            }
        } else if (mConversation.size() == 0) {
            isShowNoData(true);
        }
    }

    /**
     * 刷新界面
     */
    private void initPulltoRefresh() {
        mPullToRefresh.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                if (NetWorkUsefulUtils.getActiveNetwork(Activity_Message.this)) {
                    initData();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    IToast.show(Activity_Message.this, "" + getResources().getString(R.string.netNotUseful));
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                initData();
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
            mLlNoBalanceInfo.setVisibility(View.VISIBLE);
            mContainer.setVisibility(View.GONE);
        } else {
            mLlNoBalanceInfo.setVisibility(View.GONE);
            mContainer.setVisibility(View.VISIBLE);
        }
    }

}
