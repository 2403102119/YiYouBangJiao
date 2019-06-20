package com.tangchaoke.yiyoubangjiao.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.FreeReleaseModel;
import com.tangchaoke.yiyoubangjiao.model.PaymentModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.tangchaoke.yiyoubangjiao.view.PromptDialogView;
import com.tangchaoke.yiyoubangjiao.view.ZoomImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 发布问题
*/
public class Activity_Release extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    /**
     * 右上角 采纳按钮
     */
    @BindView(R.id.ll_adoption)
    LinearLayout mLlAdoption;

    /**
     * 右上角 发布问题按钮
     */
    @BindView(R.id.ll_top_right_release)
    LinearLayout mLlTopRightRelease;

    /**
     * 根布局
     */
    @BindView(R.id.ll_release)
    LinearLayout mLlRelease;

    @BindView(R.id.img_release_question)
    ImageView mImgReleaseQuestion;

    public static final int CROP_IMAGE = 3;
    public static final int OPEN_RELEASE_QUESTION_CAMERA = 1;
    public static final int OPEN_RELEASE_QUESTION_PICTURE = 2;
    private Uri mCropImageUri;

    /**
     * 显示问题类型
     */
    @BindView(R.id.tv_release_title)
    TextView mTvReleaseTitle;

    /**
     * 接收 年级
     */
    private String mSelectedGrade = "";

    /**
     * 接收科目
     */
    private String mSelectedSubject = "";

    //接收棋种
    private String mSelectedChess = "";

    //问题类型：1文化课问题 2棋类问题
    private String mType3 = "";

    //界面：1首页互动答题 2我的学校互动答题
    private String mInterface = "";

    /**
     * 问题内容
     */
    @BindView(R.id.edit_content)
    EditText mEditContent;

    /**
     * 接收问题内容
     */
    private String mContent = "";

    /**
     * 删除问题图片
     */
    @BindView(R.id.img_delete)
    ImageView mImgDelete;

    /**
     * 发布问题金额
     */
    private String mPrice = "0.0";

    @OnClick({R.id.ll_back, R.id.ll_grade_subject_back, R.id.img_delete, R.id.ll_top_right_release,
            R.id.ll_release_question, R.id.img_release_question})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.ll_grade_subject_back:
                finish();
                break;

            case R.id.img_delete:
                mBitmap = null;
                mImageUriIntent = null;
                mImgDelete.setVisibility(View.INVISIBLE);
                mImgReleaseQuestion.setImageDrawable(getResources().getDrawable(R.drawable.ic_release_plus));
                break;

            case R.id.ll_top_right_release:
                mContent = mEditContent.getText().toString().trim();
                initReleaseType(mContent, mSelectedGrade, mSelectedSubject, mSelectedChess);
                break;

            case R.id.ll_release_question:
                HGTool.hintKbTwo(Activity_Release.this);
                initReleaseQuestionPop();
                break;

            case R.id.img_release_question:
                HGTool.hintKbTwo(Activity_Release.this);
                if (mBitmap != null) {
                    initPhotoPow();
                } else {
                    initReleaseQuestionPop();
                }
                break;

        }

    }

    View popView;

    PopupWindow mPopupWindowOne;

    /**
     * 弹出底部对话框，达到背景背景透明效果
     * <p>
     * 实现原理：弹出一个全屏popupWindow,将Gravity属性设置bottom,根背景设置为一个半透明的颜色，
     * 弹出时popupWindow的半透明效果背景覆盖了在Activity之上 给人感觉上是popupWindow弹出后，背景变成半透明
     */
    public void initPhotoPow() {
        popView = LayoutInflater.from(Activity_Release.this).inflate(R.layout.popup_window_online_answer_info, null);
        ZoomImageView mZoomImageView = popView.findViewById(R.id.web_view_img);
        mZoomImageView.setImageBitmap(mBitmap);
        mPopupWindowOne = new PopupWindow(popView, GridLayoutManager.LayoutParams.MATCH_PARENT,
                GridLayoutManager.LayoutParams.MATCH_PARENT);
        // 设置弹出动画
        mPopupWindowOne.setAnimationStyle(R.style.take_photo_anim);
        mPopupWindowOne.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindowOne.setFocusable(true);
        // 顯示在根佈局的底部
        mPopupWindowOne.showAtLocation(mLlRelease, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    private void initReleaseType(String mContent, String mSelectedGrade, String mSelectedSubject, String mSelectedChess) {

        if (mBitmap == null && HGTool.isEmpty(mContent)) {
            IToast.show(Activity_Release.this, "请填写问题内容或添加问题照片");
            return;
        }

        if (mType3.equals("1")) {

            if (HGTool.isEmpty(mSelectedGrade)) {
                IToast.show(Activity_Release.this, "请返回上一級选择年级");
                return;
            }

            if (HGTool.isEmpty(mSelectedSubject)) {
                IToast.show(Activity_Release.this, "请返回上一級选择科目");
                return;
            }

        } else if (mType3.equals("2")) {

            if (HGTool.isEmpty(mSelectedChess)) {
                IToast.show(Activity_Release.this, "请返回上一級选择棋类");
                return;
            }

        }

        if (mInterface.equals("1")) {
            //游客发布文化课问题
            addActivity(Activity_Release.this);
            Intent mIntentCustodyBounty = new Intent(Activity_Release.this, Activity_CustodyBounty.class);
            mIntentCustodyBounty.putExtra("grade", mSelectedGrade);
            mIntentCustodyBounty.putExtra("subject", mSelectedSubject);
            mIntentCustodyBounty.putExtra("content", mContent);
            mIntentCustodyBounty.putExtra("price", mPrice);
            mIntentCustodyBounty.putExtra("chessSpecies", mChessSpecies);
            mIntentCustodyBounty.putExtra("type3", mType3);
            if (mImageUriIntent != null) {
                mIntentCustodyBounty.putExtra("bitmap", mImageUriIntent.toString());
            } else {
                mIntentCustodyBounty.putExtra("bitmap", "");
            }
            startActivity(mIntentCustodyBounty);
        } else if (mInterface.equals("2")) {
            if (mType3.equals("1")) {
                //俱乐部学生发布文化课 需要知道是否免费
                initFree();
            } else if (mType3.equals("2")) {
                //俱乐部学生发布棋类课
                initRelease(mContent, mPrice, mSelectedGrade, mSelectedSubject, mSelectedChess);
            }
        }

    }

    private void initFree() {
        OkHttpUtils
                .post()
                .url(Api.JUDGE_USER)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("grade", mSelectedGrade)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("====判断用户是否可以免费发题:::", e.getMessage());
                        IToast.show(Activity_Release.this, "服务器开小差 请稍后再试 ！ ");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====判断用户是否可以免费发题:::", response);
                        Log.e("====判断用户是否可以免费发题:::", Api.JUDGE_USER);
                        Log.e("====判断用户是否可以免费发题:::", BaseApplication.getApplication().getToken());
                        Log.e("====判断用户是否可以免费发题:::", mSelectedGrade);
                        FreeReleaseModel mFreeReleaseModel = JSONObject.parseObject(response, FreeReleaseModel.class);
                        if (RequestType.SUCCESS.equals(mFreeReleaseModel.getStatus())) {
                            if (!HGTool.isEmpty(mFreeReleaseModel.getIsFree())) {
                                if (mFreeReleaseModel.getIsFree().equals("0")) {
                                    /**
                                     * 不可以免费发题
                                     */
                                    addActivity(Activity_Release.this);
                                    Intent mIntentCustodyBounty = new Intent(Activity_Release.this, Activity_CustodyBounty.class);
                                    mIntentCustodyBounty.putExtra("grade", mSelectedGrade);
                                    mIntentCustodyBounty.putExtra("subject", mSelectedSubject);
                                    mIntentCustodyBounty.putExtra("content", mContent);
                                    mIntentCustodyBounty.putExtra("price", mPrice);
                                    mIntentCustodyBounty.putExtra("chessSpecies", mChessSpecies);
                                    mIntentCustodyBounty.putExtra("type3", mType3);
                                    if (mImageUriIntent != null) {
                                        mIntentCustodyBounty.putExtra("bitmap", mImageUriIntent.toString());
                                    } else {
                                        mIntentCustodyBounty.putExtra("bitmap", "");
                                    }
                                    startActivity(mIntentCustodyBounty);
                                } else if (mFreeReleaseModel.getIsFree().equals("1")) {
                                    mPrice = mFreeReleaseModel.getPrice();
                                    /**
                                     * 可以免费发题
                                     */
                                    initRelease(mContent, mPrice, mSelectedGrade, mSelectedSubject, mSelectedChess);
                                }
                            }
                        } else {
                            if (mFreeReleaseModel.getStatus().equals("9") || mFreeReleaseModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mFreeReleaseModel.getStatus(), Activity_Release.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_Release.this, "登录失效");
                                }
                            } else if (mFreeReleaseModel.getStatus().equals("0")) {
                                IToast.show(Activity_Release.this, mFreeReleaseModel.getMessage());
                            }
                        }
                    }
                });
    }

    View mPopView;
    PopupWindow mPopupWindow;
    TextView mTvAvatarCancel;
    TextView mTvPhotoAlbum;
    TextView mTvTakingPictures;

    /**
     * 弹窗
     */
    private void initReleaseQuestionPop() {
        mPopView = LayoutInflater.from(Activity_Release.this).inflate(R.layout.popup_window_avatar, null);
        mTvAvatarCancel = mPopView.findViewById(R.id.tv_avatar_cancel);
        mTvAvatarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mPopupWindow.dismiss();
        }
    });
        mTvPhotoAlbum = mPopView.findViewById(R.id.tv_photo_album);
        mTvPhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initReleaseQuestionPhotoAlbum();
            }
        });
        mTvTakingPictures = mPopView.findViewById(R.id.tv_taking_pictures);
        mTvTakingPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initReleaseQuestionTakingPictures();
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
        mPopupWindow.showAtLocation(mLlRelease, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    /**
     * 发布 题  拍照
     */
    private void initReleaseQuestionTakingPictures() {
        mPopupWindow.dismiss();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkStoragePermission();
            checkCameraPermission(OPEN_RELEASE_QUESTION_CAMERA);
        } else {
            // 拍照
            startCamera(OPEN_RELEASE_QUESTION_CAMERA);
        }
    }

    /**
     * 发布 题  相册
     */
    private void initReleaseQuestionPhotoAlbum() {
        // 打开系统图库
        Intent i = new Intent(Intent.ACTION_PICK, null);
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(i, OPEN_RELEASE_QUESTION_PICTURE);
        mPopupWindow.dismiss();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_release;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("发布问题");
        if (!HGTool.isEmpty(mSelectedGrade) && !HGTool.isEmpty(mSelectedSubject)) {
            mTvReleaseTitle.setText("教育类·" + mSelectedGrade + "·" + mSelectedSubject);
        } else if (!HGTool.isEmpty(mSelectedChess)) {
            mTvReleaseTitle.setText("棋类·" + mSelectedChess);
        }
    }

    private int maxNum = 200;

    @BindView(R.id.tv_suggestion)
    TextView mTvSuggestion;

    @Override
    protected void initData() {
        mLlAdoption.setVisibility(View.GONE);
        mLlTopRightRelease.setVisibility(View.VISIBLE);
        File imgPathFile = new File(Environment.getExternalStorageDirectory(), "mydownloads");
        if (!imgPathFile.exists()) {
            imgPathFile.mkdirs();
        }
        cameraFile = new File(imgPathFile, System.currentTimeMillis() + ".jpg");
        mSelectedGrade = getIntent().getStringExtra("grade");
        mSelectedSubject = getIntent().getStringExtra("subject");
        mSelectedChess = getIntent().getStringExtra("chessSpecies");
        mType3 = getIntent().getStringExtra("type3");
        mInterface = getIntent().getStringExtra("interface");
        if (mBitmap != null) {
            mImgDelete.setVisibility(View.VISIBLE);
        } else {
            mImgDelete.setVisibility(View.INVISIBLE);
        }
        mEditContent.setSelection(mEditContent.getText().length());
        mTvSuggestion.setText("还能输入" + (maxNum - mEditContent.getText().toString().length()) + "字");
        mEditContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int number = maxNum - editable.length();
                mTvSuggestion.setText("还能输入" + (number) + "字");
            }
        });
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
        File imgPathFile = new File(Environment.getExternalStorageDirectory(), ".");
        if (!imgPathFile.exists()) {
            imgPathFile.mkdirs();
        }
        cameraFile = new File(imgPathFile, System.currentTimeMillis() + ".jpg");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //当前系统为Android 6.0及以下版本
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
            camera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(camera, requestCode);
        } else {
            // 当前系统为7.0及以上版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!cameraFile.getParentFile().exists())
                    cameraFile.getParentFile().mkdirs();
                //通过FileProvider创建一个content类型的Uri
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (requestCode == OPEN_RELEASE_QUESTION_CAMERA) {
                    mCropImageUri = FileProvider.getUriForFile(Activity_Release.this,
                            "com.tangchaoke.yiyoubangjiao.fileprovider", cameraFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImageUri);
                    startActivityForResult(intent, OPEN_RELEASE_QUESTION_CAMERA);
                }
            }
        }
    }

    Bitmap mBitmap = null;

    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";

    private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);// The Uri to store

    private Uri mImageUriIntent;

    private File cameraFile;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_Release.this, "读取中",
                true, false, null);
        if (requestCode == OPEN_RELEASE_QUESTION_PICTURE && resultCode == RESULT_OK && data != null) {
            final Uri selectImg = data.getData();
            //获取图片真正的宽高
            Glide.with(this)
                    .load(selectImg)
                    .asBitmap()//强制Glide返回一个Bitmap对象
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            cropPhoto(selectImg, width, height, mProgressHUD);
                        }
                    });

        } else if (requestCode == OPEN_RELEASE_QUESTION_CAMERA && resultCode == RESULT_OK) {
            final Uri selectImg = Uri.fromFile(cameraFile);
            //获取图片真正的宽高
            Glide.with(this)
                    .load(selectImg)
                    .asBitmap()//强制Glide返回一个Bitmap对象
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            crop(selectImg, width, height, mProgressHUD);
                        }
                    });
        } else if (requestCode == CROP_IMAGE && resultCode == RESULT_OK && data != null) {
            mProgressHUD.dismiss();
            mBitmap = decodeUriAsBitmap(imageUri);
            mImageUriIntent = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap,
                    null, null));
            if (data != null) {
                mImgDelete.setVisibility(View.VISIBLE);
            } else {
                mImgDelete.setVisibility(View.INVISIBLE);
            }
            mImgReleaseQuestion.setImageBitmap(mBitmap);
            writeFileByBitmap2(mBitmap);
            deleFile(imageUri);
            if (cameraFile != null) {
                cameraFile.delete();
            }
        } else {
            mProgressHUD.dismiss();
        }
    }

    private void deleFile(Uri uri) {
        try {
            File file = new File(new URI(uri.toString()));
            if (file.exists()) {
                file.delete();
            }
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 以时间戳命名将bitmap写入文件
     *
     * @param bitmap
     */
    public static void writeFileByBitmap2(Bitmap bitmap) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();//手机设置的存储位置
        File file = new File(path);
        File imageFile = new File(file, System.currentTimeMillis() + ".png");

        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            imageFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public void cropPhoto(Uri uri, int width, int height, ProgressHUD mProgressHUD) {
        mProgressHUD.dismiss();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CROP_IMAGE);
    }

    private void crop(Uri uri, int width, int height, ProgressHUD mProgressHUD) {
        mProgressHUD.dismiss();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mCropImageUri = FileProvider.getUriForFile(this,
                    "com.tangchaoke.yiyoubangjiao.fileprovider", cameraFile);
            Uri sourceImageUri = FileProvider.getUriForFile(this,
                    "com.tangchaoke.yiyoubangjiao.fileprovider", cameraFile);
            imageUri = mCropImageUri;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.setDataAndType(sourceImageUri, "image/*");
        } else {
            imageUri = Uri.fromFile(cameraFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.setDataAndType(uri, "image/*");
        }
        imageUri = Uri.fromFile(cameraFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//定义输出的File Uri
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scaleUpIfNeeded", true);
        startActivityForResult(intent, CROP_IMAGE);
    }

    PaymentModel mPaymentModel;

    /**
     * 问题图片文件
     */
    File mFileReleaseQuestioLicense = new File("");
    private String mReleaseQuestioLicense;
    private String mChessSpecies = "";

    /**
     * 发布问题
     */
    private void initRelease(String mContent, String mPrice,
                             final String mSelectedGrade, final String mSelectedSubject, String mSelectedChess) {

        if (!HGTool.isEmpty(mSelectedChess)) {
            if (mSelectedChess.equals("国际象棋")) {
                mChessSpecies = "1";
            } else if (mSelectedChess.equals("国际跳棋")) {
                mChessSpecies = "2";
            } else if (mSelectedChess.equals("围棋")) {
                mChessSpecies = "3";
            } else if (mSelectedChess.equals("五子棋")) {
                mChessSpecies = "4";
            } else if (mSelectedChess.equals("象棋")) {
                mChessSpecies = "5";
            }
        }

        if (mPrice.equals("0.0")) {
            mPrice = "0.0";
        }

        if (!HGTool.isEmpty(mContent)) {
            mContent = mContent.replace("%", "%25");
            mContent = mContent.replace("+", "%2B");
            mContent = mContent.replace("&", "%26");
        }

        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_Release.this, "正在提交",
                true, false, null);
        PostFormBuilder builder = OkHttpUtils.post();

        if (mBitmap != null) {
            mReleaseQuestioLicense = "releasequestio.png";
            mFileReleaseQuestioLicense = compressImage(mBitmap);/* 题目图片 */
        } else {
            mReleaseQuestioLicense = "";
            mFileReleaseQuestioLicense = new File("");/* 题目图片 */
        }
        if (mFileReleaseQuestioLicense.exists()) {
            builder.addFile("release_questio", mReleaseQuestioLicense, mFileReleaseQuestioLicense);
        }

        builder.url(Api.PRESERVA_EXERCISES
                + "?token=" + BaseApplication.getApplication().getToken()
                + "&model.grade=" + mSelectedGrade
                + "&model.subject=" + mSelectedSubject
                + "&model.price=" + mPrice
                + "&model.content= " + mContent
                + "&model.chessSpecies=" + mChessSpecies
                + "&type3=" + mType3
                + "&type2=" + "Android");
        final String finalMContent = mContent;
        final String finalMPrice = mPrice;
        builder
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Release.this, "服务器开小差！请稍后重试");
                        Log.e("==发布问题：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==发布问题：：：", response);
                        Log.e("==发布问题：：：", Api.PRESERVA_EXERCISES);
                        Log.e("==发布问题：：：", BaseApplication.getApplication().getToken());
                        Log.e("==发布问题：：：", mSelectedGrade + "::::model.grade");
                        Log.e("==发布问题：：：", mSelectedSubject + "::::model.subject");
                        Log.e("==发布问题：：：", finalMContent + "::::model.content");
                        Log.e("==发布问题：：：", finalMPrice + "::::model.price");
                        Log.e("==发布问题：：：", "Android" + "::::type2");
                        Log.e("==发布问题：：：", mType3 + "::::type3");
                        Log.e("==发布问题：：：", mChessSpecies + "::::model.chessSpecies");
                        mPaymentModel = JSONObject.parseObject(response, PaymentModel.class);
                        if (RequestType.SUCCESS.equals(mPaymentModel.getStatus())) {
                            IToast.show(Activity_Release.this, mPaymentModel.getMessage());
                            mProgressHUD.dismiss();
                            if (mPaymentModel.getIsFree().equals("1")) {
                                new MyThread().start();
                            }
                            Intent mIntentUnAnswered = new Intent(Activity_Release.this, Activity_Answered.class);
                            mIntentUnAnswered.putExtra("type", "0");//0 未解答 1 待采纳 2 已解答
                            startActivity(mIntentUnAnswered);
                            finish();
                            clearActivity();
                        } else {
                            mProgressHUD.dismiss();
                            if (mPaymentModel.getStatus().equals("9") || mPaymentModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mPaymentModel.getStatus(),
                                        Activity_Release.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_Release.this, "登录失效");
                                }
                            } else if (mPaymentModel.getStatus().equals("0")) {
                                IToast.show(Activity_Release.this, mPaymentModel.getMessage());
                            }
                        }
                    }
                });


    }

    /**
     * 压缩图片（质量压缩）
     *
     * @param bitmap
     */
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(), filename + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        recycleBitmap(bitmap);
        return file;
    }

    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

    private void initTuiSong(String oid) {
        OkHttpUtils
                .post()
                .url(Api.PUSH_ALL_USER)
                .addParams("model.oid", oid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                    }

                    @Override
                    public void onResponse(String response) {
                    }
                });
    }

    public class MyThread extends Thread {
        //继承Thread类，并改写其run方法
        public void run() {
            initTuiSong(mPaymentModel.getOid());
        }
    }

}
