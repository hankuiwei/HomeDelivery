package com.lenovo.service.http;


import com.lenovo.service.http.provider.OKHttpProvider;
import com.lenovo.service.http.provider.base.IHttpProvider;

/**
 * Created by shengtao
 * Date:2016/7/21
 * Time:11:03
 */
public class HttpHelper {

    private static volatile IHttpProvider mProvider;

    public static IHttpProvider getProvider() {
        if (mProvider == null) {
            synchronized (HttpHelper.class) {
                if (mProvider == null) {
                    mProvider = new OKHttpProvider();
                }
            }
        }
        return mProvider;
    }
}
