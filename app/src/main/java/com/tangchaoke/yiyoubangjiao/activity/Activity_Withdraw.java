package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.utils.MoneyTextWatcher;
import com.tangchaoke.yiyoubangjiao.view.IToast;

import butterknife.BindView;
import butterknife.OnClick;

/*
* @author hg
* create at 2019/1/2
* description: 提现1
*/
public class Activity_Withdraw extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.edit_money)
    EditText mEditMoney;

    private String mMoney;

    private int mType = 1;

    @OnClick({R.id.ll_back, R.id.ll_zfb, R.id.tv_back})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.ll_zfb:
                mType = 1;
                break;

            case R.id.tv_back:
                    mMoney = mEditMoney.getText().toString().trim();
                    if (!HGTool.isEmpty(mMoney)) {
                        Intent mIntentWithdrawInfo = new Intent(Activity_Withdraw.this, Activity_WithdrawInfo.class);
                        mIntentWithdrawInfo.putExtra("money", mMoney);
                        mIntentWithdrawInfo.putExtra("type", mType);
                        startActivity(mIntentWithdrawInfo);
                        addActivity(Activity_Withdraw.this);
                    } else {
                        IToast.show(Activity_Withdraw.this, "请输入提现金额");
                    }
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_withdraw;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("提现");
    }

    @Override
    protected void initData() {
        mEditMoney.addTextChangedListener(new MoneyTextWatcher(mEditMoney).setDigits(2));
    }
}
