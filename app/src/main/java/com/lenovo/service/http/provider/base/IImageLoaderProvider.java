package com.lenovo.service.http.provider.base;

import android.content.Context;

import com.lenovo.service.http.request.ImageRequest;


/**
 * Created by shengtao
 * Date:2016/7/21
 * Time:11:03
 */
public interface IImageLoaderProvider {

    void loadImage(ImageRequest request);

    void loadImage(Context context, ImageRequest request);
}
