package family.li.aiyun.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import family.li.aiyun.LiApplication;
import family.li.aiyun.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by keyC on 2019/4/16.
 * 系统工具类
 */

public class SystemUtils {

    private static final String MY_PKG_NAME = "com.li.aiyun";

    /**
     * 截取屏幕画面
     * @param activity
     * @return
     */
    public static boolean shotScreen(Activity activity) {

        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "族谱";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);

        //获取屏幕截图
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
            fos.flush();
            fos.close();

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            activity.getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.i("TAG", "shotScreen: " + e.toString());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取设备的id
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId(){
        try {
            TelephonyManager  telephonyManager = (TelephonyManager) LiApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            assert telephonyManager != null;
            @SuppressLint("MissingPermission") String deviceId = telephonyManager.getDeviceId();
            if (deviceId==null){
                //android.provider.Settings;
                deviceId= Settings.Secure.getString(LiApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            return deviceId;
        } catch (Exception e) {
            e.printStackTrace();
            return "12345678987654321";
        }
    }

    /**
     * 判断网络连接是否已开
     * 2017-09-21
     * true 已打开  false 未打开
     */
    public static boolean isConn(Context context) {
        boolean bisConnFlag = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conManager != null;
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
        }
        return bisConnFlag;
    }

    /**
     * 判断网络是否连通
     */
    public static boolean isNetworkConnected(Context context) {
        try {
            if(context!=null){
                @SuppressWarnings("static-access")
                ConnectivityManager cm = (ConnectivityManager) context
                        .getSystemService(context.CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();
                return info != null && info.isConnected();
            }else{
                /**如果context为空，就返回false，表示网络未连接*/
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断手机是否安装某个应用
     * @param context
     * @param appPackageName 应用包名
     * @return true：安装，false：未安装
     */
    public static boolean isApplicationAvilible(Context context, String appPackageName) {
        try {
            // 获取packagemanager
            PackageManager packageManager = context.getPackageManager();
            // 获取所有已安装程序的包信息
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (appPackageName.equals(pn)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * 判断wifi连接
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && (info.getType() == ConnectivityManager.TYPE_WIFI);
        } else {
            /**如果context为null就表示为未连接*/
            return false;
        }

    }

    /**
     * 打开设置网络界面
     */
    public static void openNetworkSettings(final Activity context, int request) {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
            context.startActivityForResult(intent, request);
        } catch (Exception e) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivityForResult(intent, request);
            e.printStackTrace();
        }
    }

    /**
     * 是否开启了GPS定位
     */
    public static boolean isOpenGPS(Context context){
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean isOpen = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            return isOpen;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 是否开启定位服务
     * @param context
     * @return
     */
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * 打开位置权限界面
     */
    public static void openLocationSettings(final Activity context, int request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivityForResult(intent, request);
        } else {
            try {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivityForResult(intent, request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打开定位服务
     *
     * @param context
     * @param request
     */
    public static void openLocationServiceSettings(final Activity context, int request) {
        try {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivityForResult(intent, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断应用是否在运行
     * @param context
     * @return
     */
    public static boolean isRunApp(Context context){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        //100表示取的最大的任务数，info.topActivity表示当前正在运行的Activity，info.baseActivity表系统后台有此进程在运行
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }

    /**
     * 判断应用程序是否在前台运行
     * @param context
     * @return
     */
    private static boolean isTopApp(Context context){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
        if(tasksInfo.size() > 0){
            //应用程序位于堆栈的顶层
            if(MY_PKG_NAME.equals(tasksInfo.get(0).topActivity.getPackageName())){
                return true;
            }
        }
        return false;
    }

    /**
     * 打开文件
     * 当手机中没有一个app可以打开file时会抛ActivityNotFoundException
     * @param context     activity
     * @param file        File
     * @param contentType 文件类型如：文本（text/html）
     */
    public static void startActionFile(Context context, File file, String contentType) throws ActivityNotFoundException {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(getUriForFile(context, file), contentType);
        intent.setAction(Intent.ACTION_VIEW);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        context.startActivity(intent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //如果不加，最后不会提示完成、打开。
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }, 500);
    }


    /**
     * 获取图片的file uri
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

}
