package com.lenovo.service.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lenovo.service.ServiceApplication;
import com.lenovo.service.Utils;
import com.lenovo.service.entity.base.BaseData;
import com.lenovo.service.http.HttpClientManager;
import com.lenovo.service.http.NetInterface;
import com.lenovo.service.http.callback.adapter.JsonHttpCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class GetLocationService extends Service {
    public LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListener();

    //变量声明
    private int locationType;//位置获取类型
    private double latitude;
    private double longtitude;
    private String userCode;
    private HashMap<String, String> params;
    private String token = "";
    private String pattern;
    private SimpleDateFormat sdf;
    private String time;
    private FileOutputStream fos;
    private File file;

    //常量定义
    public static final int span = 5 * 60 * 1000;
    private static final String TAG = "LOCATION";
    private String address;

    public GetLocationService() {
    }

    public void onCreate() {
        token = Utils.getToken(this);
        params = new HashMap<>();
        userCode = Utils.getUserCode(this);
        pattern = new String("yyyy-MM-dd HH:mm:ss");
        sdf = new SimpleDateFormat(pattern);
//        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        file = new File(Environment.getExternalStorageDirectory().getPath() + "/location.txt");
//        } else {
        if (!file.exists())
            file = new File(getFilesDir(), "location.txt");
//        }
        try {
            if (!file.exists())
                file.createNewFile();
            fos = new FileOutputStream(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLocationClient == null)
            mLocationClient = new LocationClient(this);   //声明LocationClient类
        initLocation();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mLocationClient.start();
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }


    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            locationType = bdLocation.getLocType();

            address = "";
            if (locationType == BDLocation.TypeGpsLocation ||
                    locationType == BDLocation.TypeNetWorkLocation ||
                    locationType == BDLocation.TypeOffLineLocation) {//任一种定位成功就上传位置
                latitude = bdLocation.getLatitude();
                longtitude = bdLocation.getLongitude();
//                writeTime("lat=" + latitude + "  lng=" + longtitude);
                address = bdLocation.getAddrStr() + " lat=" + latitude + "--longtitude=" + longtitude;
//                Log.e(TAG, address);
//                writeTime(address);
                reportLocation();
            }

        }
    }

    /**
     * 初始化定位信息参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private void reportLocation() {
        if (Utils.checkInternetStatus(this) == 0)
            return;
        if (TextUtils.isEmpty(token))
            token = Utils.getToken(this);
        if (TextUtils.isEmpty(userCode))
            userCode = Utils.getUserCode(this);
        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(userCode))
            return;
        params.clear();
        params.put("user_code", userCode);
        params.put("lat", latitude + "");
        params.put("lng", longtitude + "");
        HttpClientManager.post(NetInterface.UPLOAD_LOCATION + "token="+token, params, new JsonHttpCallBack<BaseData>() {

            @Override
            public void onSuccess(BaseData result) {
                Log.e(TAG, result.getMessage());
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Failed location");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Utils.openLocationService(this.getApplicationContext());
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(myListener);
    }
//
//    private void writeTime(String location) {
//        Date date = new Date(System.currentTimeMillis());
//        time = sdf.format(date);
//        try {
//            fos.write((time + "---location=" + location + "\r\n").getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
