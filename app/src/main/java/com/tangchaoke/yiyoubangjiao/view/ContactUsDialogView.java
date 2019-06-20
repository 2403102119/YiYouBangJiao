package com.tangchaoke.yiyoubangjiao.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* @author hg
* @create at 2018/3/14
* @description 拨打电话Dialog
*/
public class ContactUsDialogView extends Dialog {
    private static int mTheme = R.style.CustomDialog;//自定义dialog样式

    private static final int MSG_SET_CONTENT = 0x20;//设置内容文字
    private static final int MSG_SET_CONTENT_COLOR = 0x21;//设置内容文字颜色
    private static final int MSG_SET_TITLE_CONTENT_COLOR = 0x12;//设置标题及内容文字的颜色
    private static final int MSG_SET_YES = 0x30;//设置确定按钮文字
    private static final int MSG_SET_YES_COLOR = 0x31;//设置确定按钮文字颜色
    private static final int MSG_SET_NO = 0x40;//设置取消按钮文字
    private static final int MSG_SET_NO_COLOR = 0x41;//设置取消按钮文字颜色
    private static final int MSG_SET_YES_NO_COLOR = 0x34;//设置确定及取消按钮文字的颜色

    @BindView(R.id.dialog_but_yes)
    Button mDialogButYes;

    @BindView(R.id.dialog_but_no)
    Button mDialogButNo;

    @BindView(R.id.dialog_tv_title)
    TextView mDialogTvTitle;

    private OnCustomDialogListener mOnCustomDialogListener;

    @OnClick({R.id.dialog_but_yes, R.id.dialog_but_no})
    void onClick(View view) {
        switch (view.getId()) {

            case R.id.dialog_but_yes:
                mOnCustomDialogListener.setYesOnclick();
                break;

            case R.id.dialog_but_no:
                mOnCustomDialogListener.setNoOnclick();
                break;

        }
    }

    public ContactUsDialogView(Context context) {
        this(context, mTheme);
    }

    public ContactUsDialogView(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setCustomOnClickListener(OnCustomDialogListener listener) {
        mOnCustomDialogListener = listener;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SET_CONTENT:
                    mDialogTvTitle.setText((String) msg.obj);
                    break;
                case MSG_SET_CONTENT_COLOR:
                    mDialogTvTitle.setTextColor(msg.arg1);
                    break;
                case MSG_SET_TITLE_CONTENT_COLOR:
                    mDialogTvTitle.setTextColor(msg.arg1);
                    break;
                case MSG_SET_YES:
                    mDialogButYes.setText((String) msg.obj);
                    break;
                case MSG_SET_YES_COLOR:
                    mDialogButYes.setTextColor(msg.arg1);
                    break;
                case MSG_SET_NO:
                    mDialogButNo.setText((String) msg.obj);
                    break;
                case MSG_SET_NO_COLOR:
                    mDialogButNo.setTextColor(msg.arg1);
                    break;
                case MSG_SET_YES_NO_COLOR:
                    mDialogButYes.setTextColor(msg.arg1);
                    mDialogButNo.setTextColor(msg.arg1);
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_contact_us);
        ButterKnife.bind(this);
    }

    /**
     * 设置内容文字
     *
     * @param content
     */
    public void setContent(String content) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_CONTENT;
        msg.obj = content;
        mHandler.sendMessage(msg);
    }

    /**
     * 设置内容文字颜色
     *
     * @param colorId
     */
    public void setContentColor(int colorId) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_CONTENT_COLOR;
        msg.arg1 = colorId;
        mHandler.sendMessage(msg);
    }

    /**
     * 设置标题及内容文字的颜色
     *
     * @param colorId
     */
    public void setTitleContentColor(int colorId) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_TITLE_CONTENT_COLOR;
        msg.arg1 = colorId;
        mHandler.sendMessage(msg);
    }

    /**
     * 设置确定按钮文字
     *
     * @param yes
     */
    public void setYes(String yes) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_YES;
        msg.obj = yes;
        mHandler.sendMessage(msg);
    }

    /**
     * 设置确定按钮文字颜色
     *
     * @param colorId
     */
    public void setYesColor(int colorId) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_YES_COLOR;
        msg.arg1 = colorId;
        mHandler.sendMessage(msg);
    }

    /**
     * 设置取消按钮文字
     *
     * @param no
     */
    public void setNo(String no) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_NO;
        msg.obj = no;
        mHandler.sendMessage(msg);
    }

    /**
     * 设置取消按钮文字颜色
     *
     * @param colorId
     */
    public void setNoColor(int colorId) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_NO_COLOR;
        msg.arg1 = colorId;
        mHandler.sendMessage(msg);
    }

    /**
     * 设置确定及取消按钮文字的颜色
     *
     * @param colorId
     */
    public void setYesNoColor(int colorId) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SET_YES_NO_COLOR;
        msg.arg1 = colorId;
        mHandler.sendMessage(msg);
    }

    //自定义接口
    public interface OnCustomDialogListener {
        void setYesOnclick();

        void setNoOnclick();
    }
}
