package com.lenovo.service.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lenovo.service.R;
import com.lenovo.service.Utils;

import java.util.ArrayList;

public class MoreAboutCustomActivity extends BaseActivity {
    private TextView mTxtConsumerName;
    private LinearLayout mLinearPhones;
    private TextView mTxtAddress;
    private TextView mTxtConsumerLevel;
    private RecyclerView mRecyclerRecords;

    private ArrayList<String> datas;//Recycler数据源
    private SharedPreferences mSharedPreference;
    private String token;
    private String userCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void provideLayout() {
        setContentView(R.layout.activity_more_about_custom);

    }

    @Override
    protected void initView() {
        mTxtConsumerName = (TextView) findViewById(R.id.text_consumerName);
        mLinearPhones = (LinearLayout) findViewById(R.id.linearPhones);
        mTxtAddress = (TextView) findViewById(R.id.text_consumerAddress);
        mTxtConsumerLevel = (TextView) findViewById(R.id.text_consumerLevel);
        mRecyclerRecords = (RecyclerView) findViewById(R.id.recycler_contactRecords);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setClickLintenner() {

    }


    /**
     * 获取用户接触记录
     */
    private void getContactRecord() {
        if (Utils.checkInternetStatus(this) == 0) {
            Toast.makeText(this, R.string.text_internet_unavalible, Toast.LENGTH_SHORT).show();
            return;
        }


    }

    /**
     * 跳转到更多用户信息的界面
     *
     * @param context
     * @param orderID
     */
    public static void openMoreAboutCustomerAct(Context context, String orderID) {
        Intent intent_backPieces = new Intent(context, MoreAboutCustomActivity.class);
//        intent_backPieces.putExtra(, orderID);
        context.startActivity(intent_backPieces);
    }
}
