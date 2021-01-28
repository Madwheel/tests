package com.iwz.WzFramwork.partern.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/11/1817:15
 * desc   :
 */
public class GlideUtil {
    private static GlideUtil mGlideUtil = null;

    public static GlideUtil getInstance() {
        synchronized (GlideUtil.class) {
            if (mGlideUtil == null) {
                mGlideUtil = new GlideUtil();
            }
        }
        return mGlideUtil;
    }

    private GlideUtil() {
    }

    public void loadImg(Context context, Object img, ImageView imageView) {
        Glide.with(context).load(img).into(imageView);
    }

    public void loadImg(Context context, Object img, int placeholder, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(placeholder);
        Glide.with(context).load(img).apply(requestOptions).into(imageView);
    }

    public void loadImg(Context context, Object img, int placeholder, int error, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(placeholder).error(error);
        Glide.with(context).load(img).apply(requestOptions).into(imageView);
    }

    public void loadImg(Context context, Object img, int placeholder, ImageView imageView, int width, int height) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(placeholder);
        if (width != 0 & height != 0) {
            requestOptions.override(width, height);
        }
        Glide.with(context).load(img).apply(requestOptions).into(imageView);
    }

    public void loadImg(Context context, Object img, int placeholder, int error, ImageView imageView, int width, int height) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(placeholder).error(error);
        if (width != 0 & height != 0) {
            requestOptions.override(width, height);
        }
        Glide.with(context).load(img).apply(requestOptions).into(imageView);
    }

    public void loadImg(Context context, Object img, int placeholder, ImageView imageView, RoundedCornersTransform transform, int width, int height) {
        RequestOptions options = new RequestOptions().placeholder(placeholder);
        if (transform != null) {
            options.transform(transform);
        }
        if (width != 0 & height != 0) {
            options.override(width, height);
        }
        Glide.with(context).load(img).apply(options).into(imageView);
    }

    public void clear(Context context, ImageView imageView) {
        Glide.with(context).clear(imageView);
    }

    public void blurImg(Context context, Object img, ImageView imageView) {
        Glide.with(context)
                .load(img)
                .apply(new RequestOptions().bitmapTransform(new BlurTransformation(context)))
                .into(imageView);
    }

    public void loadImg(final Context context, Object img, final TextView textView) {
        Glide.with(context).asBitmap()
                .load(img)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        //这一步必须要做,否则不会显示.drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), resource);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        textView.setCompoundDrawables(drawable, null, null, null);
                    }
                });
    }

    public void loadImg(Context context, Object img, int placeholder, int radius
            , boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom
            , int width, int height, ImageView imageView) {
        RoundedCornersTransform transform = new RoundedCornersTransform(context, radius);
        transform.setNeedCorner(leftTop, rightTop, leftBottom, rightBottom);
        RequestOptions options = new RequestOptions().placeholder(placeholder).transform(transform).override(width, height).skipMemoryCache(false);
        Glide.with(context).load(img).apply(options).into(imageView);
    }
}
