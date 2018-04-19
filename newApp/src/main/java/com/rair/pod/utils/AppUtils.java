package com.rair.pod.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * <p>Utils初始化相关 </p>
 */
public class AppUtils {

    private static Context context;

    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context Application上下文
     */
    public static void init(Context context) {
        AppUtils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("u should init first");
    }

    /**
     * View获取Activity的工具
     *
     * @param view view
     * @return Activity
     */
    @NonNull
    public static Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        throw new IllegalStateException("View " + view + " is not attached to an Activity");
    }

    /**
     * 全局获取String的方法
     *
     * @param id 资源Id
     * @return String
     */
    public static String getString(@StringRes int id) {
        return context.getResources().getString(id);
    }

    /**
     * 获取androidId
     *
     * @return
     */
    public static String getAndroidId() {
        String androidId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (androidId == null || TextUtils.isEmpty(androidId)) {
            androidId = randomStr(16);
        }
        return androidId;
    }

    /**
     * 获取App包 信息版本号
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    /**
     * 获取App包名
     *
     * @param context
     * @return
     */
    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 震动200毫秒
     */
    public static void vibrate(Activity activity) {
        Vibrator vibrator = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);
        if (vibrator == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect vibrationEffect = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.vibrate(vibrationEffect);
        } else {
            vibrator.vibrate(200);
        }
    }

    /**
     * 获取异常信息在程序中出错的位置及原因
     */
    public static String getErrorInfo(Throwable throwable) throws IOException {
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        throwable.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        writer.close();
        return error;
    }

//    /**
//     * 图片压缩
//     *
//     * @param file             要压缩的图片文件
//     * @param compressListener 结果监听
//     */
//    public static void compressPic(File file, OnCompressListener compressListener) {
//        Luban.with(AppUtils.getContext())
//                .load(file)
//                .ignoreBy(200)
//                .setCompressListener(compressListener)
//                .launch();
//    }

    /**
     * 随机生成字符串(nonce)
     *
     * @return 随机字符串
     */
    private static String randomStr(int length) {
        //生成字符串从此序列中取
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}