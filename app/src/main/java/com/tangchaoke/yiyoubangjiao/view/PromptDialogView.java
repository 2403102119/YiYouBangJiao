package com.tangchaoke.yiyoubangjiao.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;

/**
 * Created by Administrator on 2017\8\29 0029.
 */

public class PromptDialogView extends Dialog implements View.OnClickListener {
    private static int mTheme = R.style.CustomDialog;//自定义dialog样式
    private TextView mTvDialogYes;
    private TextView mTvDialogNo;
    private TextView tv_prompt;
    private OnCustomDialogListener mOnCustomDialogListener;
    private TextView mTvDialogTitle;


    public PromptDialogView(Context context) {
        this(context, mTheme);
    }

    public PromptDialogView(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setCustomOnClickListenerYes(OnCustomDialogListener mOnCustomDialogListener) {
        this.mOnCustomDialogListener = mOnCustomDialogListener;
    }

    private static final int MSG_SET_TITLE = 0x10;//设置标题文字
    private static final int MSG_SET_CONTENT = 0x20;//设置内容文字

    private static final int MSG_SET_YES = 0x30;//设置确定按钮文字
    private static final int MSG_SET_NO = 0x40;//设置取消按钮文字

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SET_TITLE:
                    mTvDialogTitle.setText((String) msg.obj);//设置标题文字
                    break;
                case MSG_SET_CONTENT:
                    tv_prompt.setText((String) msg.obj);//设置标题文字
                    break;
                case MSG_SET_YES:
                    mTvDialogYes.setText((String) msg.obj);
                    break;

                case MSG_SET_NO:
                    mTvDialogNo.setText((String) msg.obj);
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_prompt);
        mTvDialogTitle = findViewById(R.id.tv_dialog_title);
        mTvDialogYes = findViewById(R.id.tv_dialog_yes);
        mTvDialogYes.setOnClickListener(this);
        mTvDialogNo = findViewById(R.id.tv_dialog_no);
        mTvDialogNo.setOnClickListener(this);
        tv_prompt = findViewById(R.id.tv_prompt);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dialog_yes:
                mOnCustomDialogListener.setYesOnclick();
                break;
            case R.id.tv_dialog_no:
                mOnCustomDialogListener.setNoOnclick();
                break;
        }
    }

    //Dialog对外提供的设置方法,设置内容区域内容、颜色
    public void setTitle(String title) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_TITLE;
        msg.obj = title;
        mHandler.sendMessage(msg);
    }

    public void setContent(String content) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_CONTENT;
        msg.obj = content;
        mHandler.sendMessage(msg);
    }

    public void setYes(String yes) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_YES;
        msg.obj = yes;
        mHandler.sendMessage(msg);
    }

    public void setNo(String yes) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_NO;
        msg.obj = yes;
        mHandler.sendMessage(msg);
    }

//自定义接口
public interface OnCustomDialogListener {
    void setYesOnclick();

    void setNoOnclick();
}

}