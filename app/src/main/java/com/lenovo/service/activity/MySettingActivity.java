package com.lenovo.service.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.lenovo.service.R;
import com.lenovo.service.Utils;
import com.lenovo.service.cache.SharedPrefManager;
import com.lenovo.service.entity.base.LoginData;
import com.lenovo.service.http.ErrorCode;
import com.lenovo.service.http.HttpClientManager;
import com.lenovo.service.http.NetInterface;
import com.lenovo.service.http.ResultUtils;
import com.lenovo.service.http.callback.adapter.JsonHttpCallBack;



import java.util.HashMap;
import java.util.Map;

/**
 * Created by hankw on 16-7-20.
 * 我的设置
 */
public class MySettingActivity extends BaseActivity implements View.OnClickListener{

    private final String TAG = this.getClass().getSimpleName();
    private TextView mtimeText;
    //private int mtime;
    private String mtime;
    private int stime;
    private int checkedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void provideLayout() {
        setContentView(R.layout.activity_my_setting);
    }

    @Override
    protected void initView() {
        findViewById(R.id.mine_title_Lin).setOnClickListener(this);
        findViewById(R.id.mine_remind).setOnClickListener(this);
        findViewById(R.id.mine_save_btn).setOnClickListener(this);
        findViewById(R.id.mine_title_Lin).setOnClickListener(this);
        mtimeText = (TextView) findViewById(R.id.mine_remind_TextView);
        int lmtime = getLMtime();
        if (lmtime == 16){
            mtime = getMtime();
            if ("0".equals(mtime)){
              mtime = "15";
            }
            Log.e(TAG,"getMtime() = ("+mtime+")");
        } else {
            mtime =""+ lmtime;
            if ("0".equals(mtime)){
                mtime = "15";
            }
        }

        Log.e(TAG,"mtime = ("+mtime+")");
        int showTime = Integer.parseInt(mtime);
        if (showTime != 15)
            checkedItem = showTime / 15 - 1;

        if (checkedItem == 7)
            checkedItem = 4;
        Log.e(TAG,"checkedItem = ("+checkedItem+")");
        String select = getResources().getStringArray(R.array.tip_times)[checkedItem];
        mtimeText.setText(select);
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void setClickLintenner() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_title_Lin:
                finish();
                break;
            case R.id.mine_remind:
                Dialog dialog = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(MySettingActivity.this);
                builder.setTitle(getResources().getString(R.string.mine_hint));
                builder.setSingleChoiceItems(R.array.tip_times,checkedItem,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String select = getResources().getStringArray(R.array.tip_times)[which];
                        int time = (which +1) * 15;
                        if (time == 75) {
                            time = 120;
                        }
                        stime = time;
                        Log.d(TAG,"select = ("+select+")");
                        Log.i(TAG,"time  = ("+time+")");

                        mtimeText.setText(select);
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.mine_save_btn:
               int netState =  Utils.checkInternetStatus(MySettingActivity.this);
                if (netState == 0) {
                    Toast.makeText(MySettingActivity.this,"网络不可用,请连接网络后重试",Toast.LENGTH_SHORT).show();
                    return;
                }
                showLoadingDialog();
                SharedPreferences sharedPreferences = SharedPrefManager.getSystemSharedPref(MySettingActivity.this);
                String userCode = SharedPrefManager.getStringInSharePref(sharedPreferences,SharedPrefManager.LOGIN_USER_CODE,"userCode");
                String mToken = SharedPrefManager.getStringInSharePref(sharedPreferences,SharedPrefManager.LOGIN_TOKEN,"mToken");
                if ("userCode".equals(userCode) || "mToken".equals(mToken)) {
                    dismissLoadingDialog();
                    Toast.makeText(MySettingActivity.this,"请登录！",Toast.LENGTH_LONG).show();
                    return;
                }
                // int savetime = getMtime();
                Log.e(TAG,"save time  ==> time =("+stime+")");
                /*if (stime == 0){
                    stime = getMtime();
                }*/
                if (stime == 0 ){
                    stime = Integer.parseInt(mtime);
                }else if(stime == 15){
                    stime = 0;
                }
                loadTime(userCode,""+stime,mToken);

                break;
        }

    }

    private int getLMtime(){
       SharedPreferences sharedPreferences =  SharedPrefManager.getSystemSharedPref(MySettingActivity.this);
        return SharedPrefManager.getInt(sharedPreferences,"remindTime",16);
    }

    private String getMtime(){
        SharedPreferences sharedPreferences =  SharedPrefManager.getSystemSharedPref(MySettingActivity.this);
      return SharedPrefManager.getStringInSharePref(sharedPreferences,SharedPrefManager.LOGIN_SET_TIME,"15");
    }

    private void loadTime(String userId , final String time, String token){

        Log.d(TAG,"loadTime() ==> save remand time is ("+time+")");
        Map<String, String> params = new HashMap<String, String>();
        params.put("token",token);
        params.put("user_code", userId);
        params.put("set_time",time);

        HttpClientManager.post(NetInterface.SET_TIME, params, new JsonHttpCallBack<LoginData>() {
            @Override
            public void onSuccess(LoginData result) {

                if (ResultUtils.isSuccess(MySettingActivity.this,result)) {
                    dismissLoadingDialog();
                    SharedPreferences preferences = SharedPrefManager.getSystemSharedPref(MySettingActivity.this);
                    SharedPrefManager.saveIntInSharePref(preferences,"remindTime",Integer.parseInt(time));
                    String err = result.getMessage();
                    Toast.makeText(MySettingActivity.this,err,Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"loadTime() ==> save remand time is is("+err+")");
                    finish();
                }else {
                    if (result != null){
                        String err = result.getMessage();
                        Log.d(TAG,"onSuccess() ==　> err is("+err+")");
                        Toast.makeText(MySettingActivity.this,err,Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onError(Exception e) {
                dismissLoadingDialog();
                Utils.showServerError(MySettingActivity.this);
                Log.d(TAG,"onError() == > Exception is ("+e.getMessage()+")");
            }
        });



    }


    public static void toSet(Activity activity){
        Intent intent = new Intent(activity,MySettingActivity.class);
        activity.startActivity(intent);
    }
}
