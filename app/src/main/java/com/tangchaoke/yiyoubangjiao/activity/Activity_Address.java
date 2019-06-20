package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.AddressBean;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class Activity_Address extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    private String mType = "";

    private String mAddressOid = "";

    @BindView(R.id.edit_address_name)
    EditText mEditAddressName;

    private String mAddressName = "";

    @BindView(R.id.edit_address_phone)
    EditText mEditAddressPhone;

    private String mAddressPhone = "";

    @BindView(R.id.edit_address)
    EditText mEditAddress;

    private String mAddress = "";

    @OnClick({R.id.ll_back, R.id.but_save})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.but_save:
                mAddressName = mEditAddressName.getText().toString().trim();
                mAddressPhone = mEditAddressPhone.getText().toString().trim();
                mAddress = mEditAddress.getText().toString().trim();
                initSaveAddress(mAddressName, mAddressPhone, mAddress);
                break;

        }

    }

    private void initSaveAddress(final String mAddressName, final String mAddressPhone, final String mAddress) {

        if (HGTool.isEmpty(mAddressName)) {
            IToast.show(Activity_Address.this, "请输入收货人姓名");
            return;
        }

        if (HGTool.isEmpty(mAddressPhone)) {
            IToast.show(Activity_Address.this, "请输入收货人手机号");
            return;
        }

        if (mAddressPhone.length() < 11) {
            IToast.show(Activity_Address.this, "请输入正确的手机号");
            return;
        }

        if (HGTool.isEmpty(mAddress)) {
            IToast.show(Activity_Address.this, "请输入详细的收货地址");
            return;
        }

        OkHttpUtils
                .post()
                .url(Api.EDIT_USER_ADDRESS)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.name", mAddressName)
                .addParams("model.phone", mAddressPhone)
                .addParams("model.detailedAddress", mAddress)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Address.this, "服务器开小差！请稍后重试");
                        Log.e("==新增或修改用户收货地址 ：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==新增或修改用户收货地址 ：：：", response);
                        Log.e("==新增或修改用户收货地址 ：：：", Api.EDIT_USER_ADDRESS);
                        Log.e("==新增或修改用户收货地址 ：：：", BaseApplication.getApplication().getToken());
                        Log.e("==新增或修改用户收货地址 ：：：", mAddressName);
                        Log.e("==新增或修改用户收货地址 ：：：", mAddressPhone);
                        Log.e("==新增或修改用户收货地址 ：：：", mAddress);
                        AddressBean mAddressBean = JSONObject.parseObject(response, AddressBean.class);
                        if (RequestType.SUCCESS.equals(mAddressBean.getStatus())) {
                            Intent mIntent = new Intent();
                            mIntent.putExtra("addressOid", mAddressBean.getUserAddress().getOid());
                            mIntent.putExtra("addressName", mAddressBean.getUserAddress().getName());
                            mIntent.putExtra("addressPhone", mAddressBean.getUserAddress().getPhone());
                            mIntent.putExtra("address", mAddressBean.getUserAddress().getDetailedAddress());
                            setResult(201, mIntent);
                            finish();
                        } else {
                            IToast.show(Activity_Address.this, mAddressBean.getMessage());
                        }
                    }
                });

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_address;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("添加地址");
    }

    @Override
    protected void initData() {
        mType = getIntent().getStringExtra("type");
        if (!HGTool.isEmpty(mType)) {
            if (mType.equals("2")) {
                mAddressOid = getIntent().getStringExtra("addressOid");
                mAddressName = getIntent().getStringExtra("addressName");
                mAddressPhone = getIntent().getStringExtra("addressPhone");
                mAddress = getIntent().getStringExtra("address");
                if (!HGTool.isEmpty(mAddressName)) {
                    mEditAddressName.setText(mAddressName);
                }
                if (!HGTool.isEmpty(mAddressPhone)) {
                    mEditAddressPhone.setText(mAddressPhone);
                }
                if (!HGTool.isEmpty(mAddress)) {
                    mEditAddress.setText(mAddress);
                }
            }
        }
    }
}
