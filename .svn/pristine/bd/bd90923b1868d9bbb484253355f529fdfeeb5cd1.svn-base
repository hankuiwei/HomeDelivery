package com.lenovo.service.fragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovo.service.R;
import com.lenovo.service.Utils;
import com.lenovo.service.activity.FadeBackActivity;
import com.lenovo.service.activity.MyAttachments;
import com.lenovo.service.activity.MyCommentActivity;
import com.lenovo.service.activity.MyQRCodeActivity;
import com.lenovo.service.activity.MySettingActivity;
import com.lenovo.service.cache.SharedPrefManager;
import com.lenovo.service.widget.CircleImageView;
import com.lenovo.service.widget.CustomChooseDialog;
import com.lenovo.service.widget.CustomProgressDialog;


/**
 * 我的tab页面
 * Created by shengtao
 * on 2016/7/26
 * 23:48
 */

public class BaseFragment extends Fragment {
    protected Dialog loadingDialog;
    private  static Handler fragmentLoadingHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = CustomProgressDialog.createDialog(getActivity());
        loadingDialog.setCanceledOnTouchOutside(false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();

        if (this.isHidden()) {
            return;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void showLoadingDialog() {
        fragmentLoadingHandler.post(new Runnable() {
            @Override
            public void run() {
                // 可能存在某种情况下回收了activity，又重新create，导致无attach view
                try {
                    if (loadingDialog != null) {
                        if (!loadingDialog.isShowing())
                            loadingDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void dismissLoadingDialog() {
        fragmentLoadingHandler.post(new Runnable() {
            @Override
            public void run() {
                // 可能存在某种情况下回收了activity，又重新create，导致无attach view
                try {
                    if (loadingDialog != null) {
                        if (loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }
    /**
     * 通知子fragment父容器是否选中
     */
    public void setSelected(boolean selected){}
}
