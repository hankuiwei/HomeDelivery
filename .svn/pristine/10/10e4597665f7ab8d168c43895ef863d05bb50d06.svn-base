package com.lenovo.service.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lenovo.service.ServiceApplication;
import com.lenovo.service.Utils;

/**
 * Created by 彤 on 2016/9/23.
 */
public class SystemReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Utils.isServiceWork(ServiceApplication.getInstance().getApplicationContext(), "GetLocationService")) {
            Utils.openLocationService(ServiceApplication.getInstance().getApplicationContext());
        }
    }
}
