package com.hyphenate.easeui.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.Api;
import com.hyphenate.easeui.MessageModel;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.EaseConversationAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class EaseConversationList extends ListView {

    protected int primaryColor;
    protected int secondaryColor;
    protected int timeColor;
    protected int primarySize;
    protected int secondarySize;
    protected float timeSize;
    protected final int MSG_REFRESH_ADAPTER_DATA = 0;
    protected Context context;
    protected EaseConversationAdapter adapter;
    protected List<EMConversation> conversations = new ArrayList<EMConversation>();
    protected List<EMConversation> passedListRef = null;


    public EaseConversationList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EaseConversationList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseConversationList);
        primaryColor = ta.getColor(R.styleable.EaseConversationList_cvsListPrimaryTextColor, getResources().getColor(R.color.list_itease_primary_color));
        secondaryColor = ta.getColor(R.styleable.EaseConversationList_cvsListSecondaryTextColor, getResources().getColor(R.color.list_itease_secondary_color));
        timeColor = ta.getColor(R.styleable.EaseConversationList_cvsListTimeTextColor, getResources().getColor(R.color.list_itease_secondary_color));
        primarySize = ta.getDimensionPixelSize(R.styleable.EaseConversationList_cvsListPrimaryTextSize, 0);
        secondarySize = ta.getDimensionPixelSize(R.styleable.EaseConversationList_cvsListSecondaryTextSize, 0);
        timeSize = ta.getDimension(R.styleable.EaseConversationList_cvsListTimeTextSize, 0);
        ta.recycle();

    }

    public void init(List<EMConversation> conversationList) {
        this.init(conversationList, null);
        this.conversations = conversationList;
    }

    /**
     *
     */
    StringBuilder mStringBuilder = new StringBuilder();

    /**
     * 上传
     */
    String mUserOid = "";

    public void init(List<EMConversation> conversationList, EaseConversationListHelper helper) {
        conversations = conversationList;
        Log.e("==conversations", conversations.size() + "----");
        if (conversationList.size() != 0) {
            Paint paint = new Paint();
            float size = paint.measureText(mUserOid);
            mStringBuilder.delete(0, (int) size);
            for (int i = 0; i < conversationList.size(); i++) {
                mStringBuilder.append(conversationList.get(i).conversationId() + ",");
            }
            mStringBuilder.delete(mStringBuilder.length() - 1, mStringBuilder.length());
            mUserOid = mStringBuilder.toString();
            mStringBuilder.delete(0, (int) size);
        }
        if (conversationList.size() != 0) {
            initData(mUserOid, conversationList, helper);
        }
    }

    MessageModel mMessageModel;

    List<MessageModel.MessageModelModel> mList = new ArrayList<MessageModel.MessageModelModel>();

    private void initData(final String mUserOid, final List<EMConversation> conversationList,
                          final EaseConversationListHelper helper) {
        OkHttpUtils
                .post()
                .url(Api.GET_HEAD_BY_OID)
                .addParams("oids", mUserOid.trim().toUpperCase())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("==消息列表：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("===消息列表：：：", "response:::" + response);
                        Log.e("===消息列表：：：", "mUserOid:::" + mUserOid.trim().toUpperCase());
                        Log.e("===消息列表：：：", "mUserOid::::" + mUserOid + "===conversationList:::" + conversationList.size());
                        mMessageModel = JSONObject.parseObject(response, MessageModel.class);
                        if (mMessageModel.getStatus().equals("1")) {
                            if (mList.size() != 0) {
                                mList.clear();
                            }
                            mList.addAll(mMessageModel.getModel());
                            if (helper != null) {
                                conversationListHelper = helper;
                            }
                            adapter = new EaseConversationAdapter(context, 0, conversationList, mList);
                            adapter.setCvsListHelper(conversationListHelper);
                            adapter.setPrimaryColor(primaryColor);
                            adapter.setPrimarySize(primarySize);
                            adapter.setSecondaryColor(secondaryColor);
                            adapter.setSecondarySize(secondarySize);
                            adapter.setTimeColor(timeColor);
                            adapter.setTimeSize(timeSize);
                            setAdapter(adapter);
                        }
                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_REFRESH_ADAPTER_DATA:
                    if (adapter != null) {
                        init(conversations, null);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public EMConversation getItem(int position) {
        return (EMConversation) adapter.getItem(position);
    }

    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH_ADAPTER_DATA)) {
            handler.sendEmptyMessage(MSG_REFRESH_ADAPTER_DATA);
        }
    }

    public void filter(CharSequence str) {
        adapter.getFilter().filter(str);
    }


    private EaseConversationListHelper conversationListHelper;


    public interface EaseConversationListHelper {
        /**
         * set content of second line
         *
         * @param lastMessage
         * @return
         */
        String onSetItemSecondaryText(EMMessage lastMessage);
    }

    public void setConversationListHelper(EaseConversationListHelper helper) {
        conversationListHelper = helper;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
