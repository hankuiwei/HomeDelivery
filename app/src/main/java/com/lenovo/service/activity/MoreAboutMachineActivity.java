package com.lenovo.service.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lenovo.service.R;
import com.lenovo.service.activity.manager.ActivityCollector;
import com.lenovo.service.adapter.OrderFragmentAdapter;
import com.lenovo.service.fragment.MachineMoreFragment;
import com.lenovo.service.fragment.OrderBoxFragment;
import com.lenovo.service.fragment.UnusedPartsFragment;
import com.lenovo.service.fragment.UsedPartsFragment;
import com.lenovo.service.http.callback.NoDoubleClickLinstenner;

import java.util.ArrayList;
import java.util.List;

public class MoreAboutMachineActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener {
    private RelativeLayout mRelativeBack;
    private TextView mTxtMachineInfoLine;//未使用Tab指示线
    private TextView mTxtFillBoxOrderLine;//已使用Tab指示线
    private RelativeLayout mRelativeMachineInfo;
    private RelativeLayout mRelativeFillBoxOrder;
    private ImageView mImgMachineInfo;
    private ImageView mImgBoxOrder;
    private TextView mTxtMachineInfo;
    private TextView mTxtFillBoxOrder;
    private ViewPager mViewPager;

    //变量

    private MachineMoreFragment machineMoreFragment;//未使用备件fragment
    private OrderBoxFragment orderBoxFragment;//已使用备件fragment
    private List<Fragment> fragments;//数据
    private OrderFragmentAdapter partsAdapter;//适配器\
    private String productSn;
    private boolean isBoxInfo;
    //常量
    private static final int TAB_MACHINEINFO = 0;//未使用页面下标位置
    private static final int TAB_FILLBOX = 1;
    public static final String PRODUCT_SN = "product_sn";//传递SN号key
    public static final String SELECT_INFO = "select_box";//是否是选择装箱单信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        provideLayout();
        initView();
        initData();
        setClickLintenner();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    protected void provideLayout() {
        setContentView(R.layout.activity_more_about_machine);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void initView() {
        mRelativeBack = (RelativeLayout) findViewById(R.id.relative_back);
        mTxtMachineInfoLine = (TextView) findViewById(R.id.text_machineInfoLine);
        mTxtFillBoxOrderLine = (TextView) findViewById(R.id.text_fillBoxLine);
        mRelativeMachineInfo = (RelativeLayout) findViewById(R.id.relative_MachineInfoTab);
        mRelativeFillBoxOrder = (RelativeLayout) findViewById(R.id.relative_FillBoxTab);
        mTxtMachineInfo = (TextView) findViewById(R.id.text_MachineInfo);
        mImgMachineInfo = (ImageView) findViewById(R.id.img_machineInfo);
        mTxtFillBoxOrder = (TextView) findViewById(R.id.text_fillBoxOrder);
        mImgBoxOrder = (ImageView) findViewById(R.id.img_orderBox);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_machineMoreInfo);
    }

    protected void initData() {
        try {
            productSn = getIntent().getExtras().getString(PRODUCT_SN);
            isBoxInfo = getIntent().getExtras().getBoolean(SELECT_INFO);
        } catch (Exception e) {
            productSn = "";
            isBoxInfo = false;
        }
        fragments = new ArrayList<>();
        machineMoreFragment = MachineMoreFragment.newInstance(productSn);
        orderBoxFragment = OrderBoxFragment.newInstance(productSn);
        fragments.add(machineMoreFragment);
        fragments.add(orderBoxFragment);
        partsAdapter = new OrderFragmentAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(partsAdapter);

        //设置监听
        mRelativeMachineInfo.setOnClickListener(noDoubleClickLinstenner);
        mRelativeFillBoxOrder.setOnClickListener(noDoubleClickLinstenner);
        mViewPager.setOnPageChangeListener(this);
        if (isBoxInfo) {//如果是选择装箱单信息，让Listview可点击
            mViewPager.setCurrentItem(TAB_FILLBOX);
            orderBoxFragment.setListCanClick(true);
        } else {
            orderBoxFragment.setListCanClick(false);
        }

    }

    protected void setClickLintenner() {
        mRelativeBack.setOnClickListener(noDoubleClickLinstenner);
    }

    public static void openMoreAboutMachineAct(Activity context, String sn, boolean selectBoxInfo) {
        Intent intentMachine = new Intent(context, MoreAboutMachineActivity.class);
        intentMachine.putExtra(PRODUCT_SN, sn);
        intentMachine.putExtra(SELECT_INFO, selectBoxInfo);
        context.startActivity(intentMachine);
    }

    private NoDoubleClickLinstenner noDoubleClickLinstenner = new NoDoubleClickLinstenner() {
        @Override
        public void doClick(View view) {
            switch (view.getId()) {
                case R.id.relative_MachineInfoTab:
                    changeUIStatus(TAB_MACHINEINFO);
                    //viewpager 切换到未使用标签
                    mViewPager.setCurrentItem(TAB_MACHINEINFO);
                    break;
                case R.id.relative_FillBoxTab:
                    changeUIStatus(TAB_FILLBOX);
                    //viewpager切换到已使用标签
                    mViewPager.setCurrentItem(TAB_FILLBOX);
                    break;
                case R.id.relative_back:
                    finish();
                    break;
            }
        }
    };

    /**
     * 切换状态
     */
    private void changeUIStatus(int position) {
        switch (position) {
            case TAB_MACHINEINFO:
                mTxtMachineInfoLine.setBackgroundColor(getResources().getColor(R.color.red_E47B78));
                mRelativeMachineInfo.setBackgroundColor(getResources().getColor(R.color.white));
                mTxtMachineInfo.setTextColor(getResources().getColor(R.color.red_E47B78));
                mImgMachineInfo.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_machineinfo_selected));

                mTxtFillBoxOrderLine.setBackgroundColor(getResources().getColor(R.color.gray_cccccc));
                mRelativeFillBoxOrder.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_0dpcorner_gray_bg));
                mTxtFillBoxOrder.setTextColor(getResources().getColor(R.color.gray_cccccc));
                mImgBoxOrder.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_boxorder_normal));
                break;
            case TAB_FILLBOX:
                mTxtMachineInfoLine.setBackgroundColor(getResources().getColor(R.color.gray_cccccc));
                mRelativeMachineInfo.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_0dpcorner_gray_bg));
                mTxtMachineInfo.setTextColor(getResources().getColor(R.color.gray_cccccc));
                mImgMachineInfo.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_machineinfo_normal));

                mTxtFillBoxOrderLine.setBackgroundColor(getResources().getColor(R.color.red_E47B78));
                mRelativeFillBoxOrder.setBackgroundColor(getResources().getColor(R.color.white));
                mTxtFillBoxOrder.setTextColor(getResources().getColor(R.color.red_E47B78));
                mImgBoxOrder.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_boxorder_selected));
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeUIStatus(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
