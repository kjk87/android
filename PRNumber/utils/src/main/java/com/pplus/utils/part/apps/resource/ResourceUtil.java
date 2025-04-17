package com.pplus.utils.part.apps.resource;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorRes;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by j2n on 2016. 7. 29..
 * <p>
 * <pre>
 *     리소스에 관련된 유틸 클래스
 * </pre>
 */
public class ResourceUtil{

    private static final String ANDROID_RESOURCE = "android.resource://";
    private static final String FOREWARD_SLASH = "/";
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * @return resource -> uri
     */
    public static Uri resourceIdToUri(Context context, int resourceId){

        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }

    public static int getColor(Context context, int id){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static Drawable getDrawable(Context context, int id){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static float getDpToPixel(Context context, int pixel){

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixel, context.getResources().getDisplayMetrics());
    }

    public static int getBitmapOfWidth(String fileName){

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);
            return options.outWidth;
        } catch (Exception e) {
            return 0;
        }
    }

    public static ColorStateList getColorStateList(Context context, @ColorRes int res){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColorStateList(res, null);
        } else {
            return context.getResources().getColorStateList(res);
        }
    }

    /**
     * Generate a value suitable for use in {@link #setId(int)}. This value will not collide with ID
     * values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    private static int generateViewId(){

        for(; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if(newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if(sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static void setGenerateViewId(View v){

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            v.setId(generateViewId());
        } else {
            v.setId(View.generateViewId());
        }
    }

    //    top_title_layout

    public static int getStatusBarHeight(Context context){

        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavigationBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @SuppressLint("NewApi")
    public static int getSoftButtonsBarHeight(Activity activity) {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }

        return 0;
    }

}
