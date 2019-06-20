package com.tangchaoke.yiyoubangjiao.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.AuditFailurePromptModel;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.type.SubjectType;
import com.tangchaoke.yiyoubangjiao.utils.BitmapUtils;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.utils.ImageDispose;
import com.tangchaoke.yiyoubangjiao.utils.PictureUtils;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.tangchaoke.yiyoubangjiao.view.PromptEchoDialogView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 成人学历认证
*/
public class Activity_AcademicCertificate extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    /**
     * 根布局
     */
    @BindView(R.id.ll_academic_certificate)
    RelativeLayout mLlAcademicCertificate;

    /**
     * 毕业证照片
     */
    @BindView(R.id.img_diploma)
    ImageView mImgDiploma;

    /**
     * 学校名称
     */
    @BindView(R.id.edit_school_name)
    EditText mEditSchoolName;

    private String mSchoolName = "";

    /**
     * 学历
     */
    @BindView(R.id.tv_education)
    TextView mTvEducation;

    String mEducation = "";

    /**
     * @param view
     */
    @OnClick({R.id.ll_back, R.id.ll_diploma, R.id.but_upload, R.id.fl_education})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            /**
             * 毕业证照片
             */
            case R.id.ll_diploma:
                initDiplomaPop();
                HGTool.hintKbTwo(Activity_AcademicCertificate.this);
                break;

            /**
             * 上传
             */
            case R.id.but_upload:
                mSchoolName = mEditSchoolName.getText().toString().trim();
                initUpload(mSchoolName, mEducation);
                break;

            /**
             * 学历
             */
            case R.id.fl_education:
                initEducation();
                HGTool.hintKbTwo(Activity_AcademicCertificate.this);
                break;

        }

    }

    /**
     * 学历选择器
     */
    OptionsPickerView mEducationPvOptions;

    /**
     * 学历
     */
    private void initEducation() {
        mEducationPvOptions = new OptionsPickerView.Builder(Activity_AcademicCertificate.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是选中位置
                mEducation = SubjectType.mEducationList.get(options1);
                mTvEducation.setText(mEducation);

            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("请选择学历")//标题
                .setSubCalSize(20)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Activity_AcademicCertificate.this.getResources().getColor(R.color.color999999))//确定按钮文字颜色
                .setCancelColor(Activity_AcademicCertificate.this.getResources().getColor(R.color.color999999))//取消按钮文字颜色
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .setOutSideCancelable(true)//点击外部dismiss default true
                .build();
        mEducationPvOptions.setPicker(SubjectType.mEducationList);
        mEducationPvOptions.show();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_academic_certificate;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("成人学历认证");
    }

    private String mType = "";

    @Override
    protected void initData() {
        mType = getIntent().getStringExtra("type");
        if (!HGTool.isEmpty(mType)) {
            if (mType.equals("1")) {
                initAuditFailurePrompt();
            }
        }
    }

    /**
     * 审核失败提示
     */
    private void initAuditFailurePrompt() {
        OkHttpUtils
                .post()
                .url(Api.RETURN_MESSAGE)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("type", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_AcademicCertificate.this, "服务器开小差！请稍后重试");
                        Log.e("==返回认证信息：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==返回认证信息：：：", response);
                        Log.e("==返回认证信息：：：", Api.RETURN_MESSAGE);
                        Log.e("==返回认证信息：：：", BaseApplication.getApplication().getToken());
                        Log.e("==返回认证信息：：：", "1");
                        AuditFailurePromptModel mAuditFailurePromptModel = JSONObject.parseObject(response, AuditFailurePromptModel.class);
                        if (RequestType.SUCCESS.equals(mAuditFailurePromptModel.getStatus())) {
                            initAuditFailurePromptDisplay(mAuditFailurePromptModel.getModel());
                        } else {
                            if (mAuditFailurePromptModel.getStatus().equals("9") || mAuditFailurePromptModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mAuditFailurePromptModel.getStatus(), Activity_AcademicCertificate.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_AcademicCertificate.this, "登录失效");
                                }
                            } else if (mAuditFailurePromptModel.getStatus().equals("0")) {
                                IToast.show(Activity_AcademicCertificate.this, mAuditFailurePromptModel.getMessage());
                            }
                        }
                    }
                });
    }

    private String mImage = "";

    /**
     * 显示数据
     *
     * @param mAuditFailurePromptModel
     */
    private void initAuditFailurePromptDisplay(AuditFailurePromptModel.AuditFailurePromptModelModel mAuditFailurePromptModel) {
        if (!HGTool.isEmpty(mAuditFailurePromptModel.getReason())) {
            isLoginDialog(mAuditFailurePromptModel.getReason());
        }

        if (!HGTool.isEmpty(mAuditFailurePromptModel.getSchoolName())) {
            mEditSchoolName.setText(mAuditFailurePromptModel.getSchoolName());
        }

        if (!HGTool.isEmpty(mAuditFailurePromptModel.getUserDiploma())) {
            mEducation = mAuditFailurePromptModel.getUserDiploma();
            mTvEducation.setText(mAuditFailurePromptModel.getUserDiploma());
        }

        if (!HGTool.isEmpty(mAuditFailurePromptModel.getUserDiplomaPhoto())) {
            mImage = mAuditFailurePromptModel.getUserDiplomaPhoto();
            Glide.with(Activity_AcademicCertificate.this).load(Api.PATH + mAuditFailurePromptModel.getUserDiplomaPhoto()).centerCrop().into(mImgDiploma);
        }

    }

    /**
     * 是否失败 0 失败  1 提交审核成功
     */
    private int isfailure = 0;

    private void isLoginDialog(String mReason) {
        try {
            final PromptEchoDialogView mCleanUpCacheDialogView = new PromptEchoDialogView(Activity_AcademicCertificate.this);
            if (isfailure == 0) {
                mCleanUpCacheDialogView.setTitle("温馨提示");
            } else if (isfailure == 1) {
                mCleanUpCacheDialogView.setTitle("提交成功");
            }
            mCleanUpCacheDialogView.setContent(mReason);
            mCleanUpCacheDialogView.setCancelable(false);
            mCleanUpCacheDialogView.setCustomOnClickListener(new PromptEchoDialogView.OnCustomDialogListener() {
                @Override
                public void setYesOnclick() {
                    if (isfailure == 0) {
                        mCleanUpCacheDialogView.dismiss();
                    } else if (isfailure == 1) {
                        finish();
                        mCleanUpCacheDialogView.dismiss();
                    }
                }
            });
            mCleanUpCacheDialogView.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    View mPopView;
    PopupWindow mPopupWindow;
    TextView mTvAvatarCancel;
    TextView mTvPhotoAlbum;
    TextView mTvTakingPictures;
    public static final int OPEN_DIPLOMA_CAMERA = 1;
    public static final int OPEN_DIPLOMA_PICTURE = 2;
    private String mDiplomaPath = ""; // 毕业证照片路径
    private String mDiplomaUploadPath = "";
    private Uri mImageDiplomaUri;

    /**
     * 毕业证照片
     */
    private void initDiplomaPop() {
        mPopView = LayoutInflater.from(Activity_AcademicCertificate.this).inflate(R.layout.popup_window_avatar, null);
        mTvAvatarCancel = mPopView.findViewById(R.id.tv_avatar_cancel);
        /**
         * 取消
         */
        mTvAvatarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        mTvPhotoAlbum = mPopView.findViewById(R.id.tv_photo_album);
        /**
         * 相册
         */
        mTvPhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDiplomaPhotoAlbum();
            }
        });
        mTvTakingPictures = mPopView.findViewById(R.id.tv_taking_pictures);
        /**
         * 拍照
         */
        mTvTakingPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDiplomaTakingPictures();
            }
        });
        mPopupWindow = new PopupWindow(mPopView,
                GridLayoutManager.LayoutParams.MATCH_PARENT,
                GridLayoutManager.LayoutParams.MATCH_PARENT);
        // 设置弹出动画
        mPopupWindow.setAnimationStyle(R.style.take_photo_anim);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        // 显示在根布局的底部
        mPopupWindow.showAtLocation(mLlAcademicCertificate, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    /**
     * 毕业证照片  拍照
     */
    private void initDiplomaTakingPictures() {
        mPopupWindow.dismiss();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkStoragePermission();
            checkCameraPermission(OPEN_DIPLOMA_CAMERA);
        } else {
            // 拍照
            startCamera(OPEN_DIPLOMA_CAMERA);
        }
    }

    /**
     * 毕业证照片  相册
     */
    private void initDiplomaPhotoAlbum() {
        // 打开系统图库
        Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, OPEN_DIPLOMA_PICTURE);
        mPopupWindow.dismiss();
    }

    /**
     * 检查相机权限
     */
    public void checkCameraPermission(int requestCode) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            startCamera(requestCode);
        }
    }

    /**
     * 打开相机
     *
     * @param requestCode
     */
    private void startCamera(int requestCode) {
        File imgPathFile = new File(Environment.getExternalStorageDirectory(), "mydownloads");
        if (!imgPathFile.exists()) {
            imgPathFile.mkdirs();
        }
        File file = new File(imgPathFile, System.currentTimeMillis() + ".jpg");
        if (requestCode == OPEN_DIPLOMA_CAMERA) {
            mDiplomaPath = file.getAbsolutePath();
            mDiplomaUploadPath = imgPathFile + "/upload_" + file.getName();
            mImage = "";
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //当前系统为Android 6.0及以下版本
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            camera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(camera, requestCode);
        } else {
            // 当前系统为7.0及以上版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                //通过FileProvider创建一个content类型的Uri
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                if (requestCode == OPEN_DIPLOMA_CAMERA) {
                    mImageDiplomaUri = FileProvider.getUriForFile(Activity_AcademicCertificate.this, "com.tangchaoke.yiyoubangjiao.fileprovider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageDiplomaUri);//将拍取的照片保存到指定URI
                    startActivityForResult(intent, OPEN_DIPLOMA_CAMERA);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imgPath = Environment.getExternalStorageDirectory() + "/mydownloads/";
        File file = new File(imgPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (requestCode == OPEN_DIPLOMA_CAMERA && resultCode == RESULT_OK) {
            // 拍照用户头像
            Bitmap bitmap = BitmapUtils.createImageThumbnail(mDiplomaPath);
            byte[] mByte = ImageDispose.Bitmap2Bytes(bitmap);
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 0;
            /**
             * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
             */
            bitmap = ImageDispose.getPicFromBytes(mByte, bitmapOptions);
            saveSmallBitmap(bitmap, mDiplomaPath, mDiplomaUploadPath);
            Glide.with(Activity_AcademicCertificate.this).load(mDiplomaPath).centerCrop().into(mImgDiploma);
//            mImgDiploma.setImageBitmap(bitmap);
        } else if (requestCode == OPEN_DIPLOMA_PICTURE && resultCode == RESULT_OK && data != null) {
            // 相册用户头像
            String pathResult = getPicPath(Activity_AcademicCertificate.this, data);
            mDiplomaPath = pathResult;
            File file1 = new File(mDiplomaPath);
            mDiplomaUploadPath = imgPath + "upload_" + file1.getName();
            mImage = "";
            Bitmap bitmap = getPicBitmap(Activity_AcademicCertificate.this, data);
            byte[] mByte = ImageDispose.Bitmap2Bytes(bitmap);
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 0;
            /**
             * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
             */
            bitmap = ImageDispose.getPicFromBytes(mByte, bitmapOptions);
            saveSmallBitmap(bitmap, mDiplomaPath, mDiplomaUploadPath);
            Glide.with(Activity_AcademicCertificate.this).load(mDiplomaPath).centerCrop().into(mImgDiploma);
//            mImgDiploma.setImageBitmap(bitmap);
        }
    }

    /**
     * 将拍照或选择所得的图片压缩后保存在指定路径下
     *
     * @param bitmap        拍照或选择所得的图片对应的Bitmap
     * @param imgPath       拍照或选择所得的图片路径
     * @param uploadImgPath 压缩后的图片路径（上传用的图片）
     */
    private void saveSmallBitmap(Bitmap bitmap, String imgPath, String uploadImgPath) {
        Bitmap smallBitmap;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            smallBitmap = PictureUtils.getSmallBitmap(imgPath, 1120, 630);
        } else {
            smallBitmap = PictureUtils.getSmallBitmap(imgPath, 630, 1120);
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(uploadImgPath);
            smallBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把数据写入文件
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            smallBitmap.recycle();
        }
    }

    File mDiplomaFile;
    private String mAvatar = "";

    /**
     * 上传实名认证信息
     */
    private void initUpload(final String mSchoolName, final String mEducation) {
        if (HGTool.isEmpty(mSchoolName)) {
            IToast.show(Activity_AcademicCertificate.this, "请输入毕业院校");
            return;
        }
        PostFormBuilder builder = OkHttpUtils.post();
        builder.url(Api.UPDATE_USER_DIPLOMA
                + "?token=" + BaseApplication.getApplication().getToken()
                + "&model.schoolName=" + mSchoolName
                + "&model.userDiploma=" + mEducation
                + "&url=" + mImage);

        if (!HGTool.isEmpty(mDiplomaPath)) {
            mAvatar = "diploma.png";
            mDiplomaFile = new File(mDiplomaPath);/* 学历 */
        } else {
            mAvatar = "";
            mDiplomaFile = new File("");/* 学历 */
        }
        if (mDiplomaFile.exists()) {
            builder.addFile("diploma_pic", mAvatar, mDiplomaFile);
        }
        final ProgressHUD mProgressHUD = ProgressHUD.show(this, "正在提交", true, false, null);
        builder
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_AcademicCertificate.this, "服务器开小差！请稍后重试");
                        Log.e("==上传成人学历认证：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==上传成人学历认证：：：", response);
                        Log.e("==上传成人学历认证：：：", Api.UPDATE_USER_DIPLOMA);
                        Log.e("==上传成人学历认证：：：", BaseApplication.getApplication().getToken());
                        Log.e("==上传成人学历认证：：：", mSchoolName);
                        Log.e("==上传成人学历认证：：：", mEducation);
                        Log.e("==上传成人学历认证：：：", mImage);
                        Log.e("==上传成人学历认证：：：", mDiplomaUploadPath);
                        mProgressHUD.dismiss();
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            isfailure = 1;
                            isLoginDialog("您的认证资料已经提交成功，我们将尽快为你审核");
                            mProgressHUD.dismiss();
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                mProgressHUD.dismiss();
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(), Activity_AcademicCertificate.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_AcademicCertificate.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                mProgressHUD.dismiss();
                                IToast.show(Activity_AcademicCertificate.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
    }

}
