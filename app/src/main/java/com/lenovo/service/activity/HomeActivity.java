package com.lenovo.service.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.igexin.sdk.PushManager;
import com.lenovo.service.R;
import com.lenovo.service.activity.manager.ActivityCollector;
import com.lenovo.service.entity.base.LoginData;
import com.lenovo.service.http.HttpClientManager;
import com.lenovo.service.http.callback.adapter.JsonHttpCallBack;
import com.lenovo.service.push.PushBoradCastReceiver;
import android.annotation.TargetApi;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shengtao
 * on 2016/7/21
 * 16:48
 */
public class HomeActivity extends BaseFragmentActivity {
    /**
     * 个推start,第三方应用Master Secret
     */
    private static final int REQUEST_PERMISSION = 0;
    private static final int ADDTAG = 100;//添加Tag
    private static final int VERSION = 101;//当前版本
    private static final int SILENTTIME = 102;//设置静默时间
    private static final int EXIT = 103;//退出
    private static final int GETCLIENTID = 106;//手动获取CID
    /**
     * SDK服务是否启动
     */
    private SimpleDateFormat formatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        requestPermission();
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_home);
       // postWithModelCallBack();
    }

    public void postWithModelCallBack() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_code", "c47018");
        params.put("password", "1234qwer");
        params.put("mei_id", "xyz");
        params.put("app_version", "1.0.0");
        params.put("machine_name", "联想100");
        params.put("client_id", "d2d7d419e75e9e51e9c86c5ece0bfc8f");
        HttpClientManager.post("http://10.118.0.130:8100/api/login", params, new JsonHttpCallBack<LoginData>() {
            @Override
            public void onSuccess(LoginData result) {
                Log.e("test", "aaaaa");
            }

            @Override
            public void onError(Exception e) {
                Log.e("test", "bbbbb");
            }
        });
    }
    @TargetApi(23)
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e("clientid-test", "1");
            ArrayList<String> permissions = new ArrayList<String>();
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissions.size() > 0) {
                Log.e("clientid-test", "2");
                requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_PERMISSION);
            }
        }
        else{
            Log.e("clientid-test", "3");
            PushManager.getInstance().initialize(this.getApplicationContext());
        }
    }
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            Log.e("clientid-test", "4");
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                PushManager.getInstance().initialize(this.getApplicationContext());
                Log.e("clientid-test", "6");
            } else {
                Log.e("clientid-test", "7");
                PushManager.getInstance().initialize(this.getApplicationContext());
            }
        } else {
            Log.e("clientid-test", "5");
            onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public void onDestroy() {
        Log.d("GetuiSdkDemo", "onDestroy()");
        PushBoradCastReceiver.payloadData.delete(0, PushBoradCastReceiver.payloadData.length());
        super.onDestroy();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 返回键最小化程序
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public static void goHomeActivity(Context context, boolean isNewTask) {
        Intent homeIntent = new Intent(context, HomeActivity.class);
        if (isNewTask) {
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //在广播接受者中启动活动需要添加这个标志
        }
        context.startActivity(homeIntent);
    }
}
