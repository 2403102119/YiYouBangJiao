package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.model.NearbyModel;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 棋类机构地址
*/
public class Activity_ClubAddress extends BaseActivity implements
        LocationSource,
        AMapLocationListener,
        AMap.InfoWindowAdapter,
        AMap.OnInfoWindowClickListener {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.map_view)
    MapView mMapView;

    AMap mAMap;

    OnLocationChangedListener mOnLocationChangedListener;

    AMapLocationClient mAMapLocationClient;

    AMapLocationClientOption mAMapLocationClientOption;

    MarkerOptions mMarkerOptions;

    private boolean isFirstLoc = true;

    List<LatLng> mList = new ArrayList<LatLng>();

    private String mType = "";

    private double mLatitude;

    private double mLongitude;

    @OnClick({R.id.ll_back})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_club_address;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("附近机构");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
    }

    /**
     * 纬度小经度大
     */
    @Override
    public void initData() {
        mType = getIntent().getStringExtra("type");
        if (mType.equals("1")) {
            mLatitude = getIntent().getDoubleExtra("latitude", 0);
            mLongitude = getIntent().getDoubleExtra("longitude", 0);
            mList.add(new LatLng(mLatitude, mLongitude));
        }
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        // 设置定位监听
        mAMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        mMarkerOptions = new MarkerOptions();
        if (mType.equals("1")) {
            for (int i = 0; i < mList.size(); i++) {
                mMarkerOptions.position(mList.get(i));
                mMarkerOptions.visible(true);
                mMarkerOptions.title("当前位置");
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_position_logo));
                mMarkerOptions.icon(bitmapDescriptor);
                mAMap.addMarker(mMarkerOptions);
            }
        }
        mAMap.setInfoWindowAdapter(this);//AMap类中
        mAMap.setOnInfoWindowClickListener(this);
    }

    NearbyModel mNearbyModel;

    private void initClubNearby() {
        OkHttpUtils
                .post()
                .url(Api.GET_NEARBY_CLUB_LIST)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("longitude", mLongitude + "")
                .addParams("latitude", mLatitude + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_ClubAddress.this, "服务器开小差 请稍后再试 ！ ");
                        Log.e("====获取附近俱乐部:::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====获取附近俱乐部:::", response);
                        Log.e("====获取附近俱乐部:::", Api.GET_NEARBY_CLUB_LIST);
                        Log.e("====获取附近俱乐部:::", mLongitude + "");
                        Log.e("====获取附近俱乐部:::", mLatitude + "");
                        mNearbyModel = JSONObject.parseObject(response, NearbyModel.class);
                        for (int i = 0; i < mNearbyModel.getList().size(); i++) {
                            String[] arr = mNearbyModel.getList().get(i).get_location().split(",");
                            List<String> mListString = Arrays.asList(arr);
                            mList.add(new LatLng(Double.valueOf(mListString.get(1)), Double.valueOf(mListString.get(0))));
                        }
                        for (int i = 0; i < mList.size(); i++) {
                            mMarkerOptions.position(mList.get(i));
                            mMarkerOptions.visible(true);
                            mMarkerOptions.title(i + "");
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                                    .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_position_logo));
                            mMarkerOptions.icon(bitmapDescriptor);
                            mAMap.addMarker(mMarkerOptions);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if (null != mAMapLocationClient) {
            mAMapLocationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListener = onLocationChangedListener;
        if (mAMapLocationClient == null) {
            //初始化定位
            mAMapLocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mAMapLocationClientOption = new AMapLocationClientOption();
            //设置定位回调监听
            mAMapLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mAMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //该方法默认为false，true表示只定位一次
            mAMapLocationClientOption.setOnceLocation(true);
            //设置定位参数
            mAMapLocationClient.setLocationOption(mAMapLocationClientOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mAMapLocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mOnLocationChangedListener = null;
        if (mAMapLocationClient != null) {
            mAMapLocationClient.stopLocation();
            mAMapLocationClient.onDestroy();
        }
        mAMapLocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mOnLocationChangedListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mOnLocationChangedListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));//设置地图的放缩
                    mAMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
                    MyLocationStyle myLocationStyle = new MyLocationStyle();
                    myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_me_map));
                    myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
                    myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色  。
                    mAMap.setMyLocationStyle(myLocationStyle);
                    if (mType.equals("1")) {
                        //将地图移动到定位点
                        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(mLatitude, mLongitude)));
                    } else if (mType.equals("2")) {
                        //将地图移动到定位点
                        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(
                                new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    }
                    isFirstLoc = false;
                    mLongitude = aMapLocation.getLongitude();
                    mLatitude = aMapLocation.getLatitude();
                    if (mType.equals("2")) {
                        initClubNearby();
                    }
                }
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    View infoWindow = null;

    private String mOid = "";

    /**
     * 监听自定义infowindow窗口的infowindow事件回调
     */
    @Override
    public View getInfoWindow(Marker marker) {
        if (mType.equals("1")) {
            return null;
        }
        infoWindow = LayoutInflater.from(this).inflate(R.layout.popup_window_club_address, null);
        //custom_info_window为自定义的layout文件
        TextView mTvClubMap = infoWindow.findViewById(R.id.tv_club_map);
        ImageView mImgClubMap = infoWindow.findViewById(R.id.img_club_map);
        if (mType.equals("2")) {
            mTvClubMap.setText(mNearbyModel.getList().get(Integer.parseInt(marker.getTitle())).get_name());
            Glide.with(Activity_ClubAddress.this).load(
                    Api.PATH + mNearbyModel.getList().get(Integer.parseInt(marker.getTitle())).getClub_logo()).into(mImgClubMap);
            mOid = mNearbyModel.getList().get(Integer.parseInt(marker.getTitle())).getClub_oid();
        }
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (mType.equals("1")) {
            return;
        }
        Intent mIntentClubInfo = new Intent(Activity_ClubAddress.this, Activity_ClubInfo.class);
        mIntentClubInfo.putExtra("oid", mOid);
        startActivity(mIntentClubInfo);
    }

}
