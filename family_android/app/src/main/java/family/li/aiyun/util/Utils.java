package family.li.aiyun.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by keyC on 2019/6/12.
 */

public class Utils {

    public static String WEB_TREE = "http://sit.zuapi.a56999.com/web/tree"; //web树结构地址

    public static String URL_L1 = "http://sit.zuapi.a56999.com/web/history/pageOne"; //李氏史载
    public static String URL_L2 = "http://sit.zuapi.a56999.com/web/history/pageTwo"; //莲峰李公祠

    public static String USER_ID = "-1"; //用户USER_ID
    public static String TOKEN = null; //用户TOKEN
    public static int HIDE_INFO = 0; //是否隐藏用户更多信息（0 隐藏， 1 显示）
    public static int ADMIN = 0; //是否属于管理员（0 否， 1 是）
    public static String USER_NAME = ""; //用户姓名
    public static String USER_HEAD_IMG = ""; //用户头像

    public static String APP_VERSION_NAME = "1.0.0"; //版本号
    public static int APP_VERSION_CODE = 0; //内部版本号

    public static final int READ_REQUEST_CONTACTS_PERMISSIONS = 1001; //访问手机通讯录权限
    public static final int READ_REQUEST_CONTACTS = 1002; //访问手机通讯录
    public static final int SELECT_COUNTRY = 1003; //选择国家地区
    public static final int PERSONAL_DATA = 1004; //查看人员信息
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1005; //检查系统写入权限
    public static final int READ_PHONE_STATE = 1006; //检查系统读取权限


    /**
     * 去除字符串中的空格、回车、换行符、制表符
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 保存用户信息
     * @param context
     * @param key
     * @param value
     */
    public static void savePreference(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
        if (key.equals("token")) {
            TOKEN = value;
        }
    }

    /**
     *清除用户信息
     * @param context
     */
    public static void clearPreference(Context context) {
        TOKEN = null;
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
//        String adNo = preferences.getString("hasShowAdNo", null);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
//        editor.putString("hasShowAdNo", adNo);
        editor.commit();
    }

    /**
     *清除用户某个信息
     * @param context
     */
    public static void removePreference(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 获取用户信息
     * @param context
     * @param newKey
     * @return
     */
    public static String getPreference(Context context, String newKey) {
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String value = preferences.getString(newKey, null);
        if (newKey.equals("token") && value != null) {
            Utils.TOKEN = value;
        }
        return value;
    }

    // 判断一个字符串是否都为数字
    public static boolean isDigit(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) strNum);
        return matcher.matches();
    }

    //截取数字
    public static int getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return Integer.valueOf(matcher.group(0));
        }
        return 0;
    }

    // 截取非数字
    public static String splitNotNumber(String content) {
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 是否是中文字符
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        if (str == null) {
            return false;
        }
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 获取版本信息
     * @param context
     */
    public static void getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;

        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            APP_VERSION_NAME = packInfo.versionName;
            APP_VERSION_CODE = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断手机厂商
     * @return
     */
    public static boolean isMIUI() {
        String manufacturer = Build.MANUFACTURER;
        //这个字符串可以自己定义,例如判断华为就填写huawei,魅族就填写meizu
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

    /**
     *  输出json字符串
     */
    public static String jsonToStr(JSONArray jsonArray, int index) {
        String str = "";
        try {
            JSONObject jo = (JSONObject) jsonArray.get(index);
            str = jo.getString("txt");
        } catch (JSONException e) {
            e.getStackTrace();
        }
        return str;
    }


    /**
     * 将图片转换成Base64编码的字符串
     * @param path
     * @return base64编码的字符串
     */
    public static String getBase64(String path){
        String mBase64 = "";
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            mBase64 = Base64.encodeToString(data, Base64.DEFAULT);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mBase64;
    }

    /**
     * 照片转byte二进制
     * @param imagepath 需要转byte的照片路径
     * @return 已经转成的byte
     * @throws Exception
     */
    public static byte[] readStream(String imagepath) throws Exception {
        FileInputStream fs = new FileInputStream(imagepath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = fs.read(buffer))) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            fs.close();
            return outStream.toByteArray();
        } catch (Exception e) {
            return outStream.toByteArray();
        }
    }

    /**
     * 补足两位小数
     * @param str
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getDecimalFormat(String str) {
        String s = "0.00";
        if (!("").equals(str)) {
            float f = Float.parseFloat(str);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            s = decimalFormat.format(f);
        }
        return s;
    }

    /**
     * 拼接时间显示的字符串
     * @param s
     * @return
     */
    public static String spliceString(String s) {
        if(s != null && !("").equals(s)) {
            if(Integer.valueOf(s) < 10){
                return "0" + s;
            } else {
                return s;
            }
        }
        return "00";
    }

    /**
     * 格式化时间戳
     * 年-月-日 时-分-秒
     * @param timeStamp
     * @return
     */
    public static String formatTime0(String timeStamp) {
        if (timeStamp != null && !TextUtils.isEmpty(timeStamp) && !("null").equals(timeStamp)) {
            try {
                String dateString = null;
                Calendar cd = Calendar.getInstance();
                int nowDayOfYear = cd.get(Calendar.DAY_OF_YEAR);
                cd.setTime(new Date(Double.valueOf(timeStamp).longValue() * 1000L));
                int date = cd.get(Calendar.DAY_OF_YEAR);
                int value = nowDayOfYear - date;
                // 获取指定日期转换成星期几
                if (value == -2) {
                    dateString = "后天";
                } else if (value == -1) {
                    dateString = "明天";
                } else if (value == 0) {
                    dateString = "今天";
                } else if (value == 1) {
                    dateString = "昨天";
                } else if (value == 2) {
                    dateString = "前天";
                } else {
                    dateString = cd.get(Calendar.YEAR) + "-" + ((cd.get(Calendar.MONTH) + 1) > 9 ? (cd.get(Calendar.MONTH) + 1) : "0" + (cd.get(Calendar.MONTH) + 1)) + "-" + (cd.get(Calendar.DAY_OF_MONTH) > 9 ? cd.get(Calendar.DAY_OF_MONTH) : "0" + cd.get(Calendar.DAY_OF_MONTH));
                }
                dateString += " " + (cd.get(Calendar.HOUR_OF_DAY) > 9 ? cd.get(Calendar.HOUR_OF_DAY) : "0" + cd.get(Calendar.HOUR_OF_DAY)) + ":" + (cd.get(Calendar.MINUTE) > 9 ? cd.get(Calendar.MINUTE) : "0" + cd.get(Calendar.MINUTE)) + ":" + (cd.get(Calendar.SECOND) > 9 ? cd.get(Calendar.SECOND) : "0" + cd.get(Calendar.SECOND));
                return dateString;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 格式化时间戳
     * 年-月-日 时-分
     * @param timeStamp
     * @return
     */
    public static String formatTime(String timeStamp) {
        if (timeStamp != null && !TextUtils.isEmpty(timeStamp) && !("null").equals(timeStamp)) {
            try {
                String dateString = null;
                Calendar cd = Calendar.getInstance();
                int nowDayOfYear = cd.get(Calendar.DAY_OF_YEAR);
                cd.setTime(new Date(Double.valueOf(timeStamp).longValue() * 1000L));
                int date = cd.get(Calendar.DAY_OF_YEAR);
                int value = nowDayOfYear - date;
                // 获取指定日期转换成星期几
                if (value == -2) {
                    dateString = "后天";
                } else if (value == -1) {
                    dateString = "明天";
                } else if (value == 0) {
                    dateString = "今天";
                } else if (value == 1) {
                    dateString = "昨天";
                } else if (value == 2) {
                    dateString = "前天";
                } else {
                    dateString = cd.get(Calendar.YEAR) + "-" + ((cd.get(Calendar.MONTH) + 1) > 9 ? (cd.get(Calendar.MONTH) + 1) : "0" + (cd.get(Calendar.MONTH) + 1)) + "-" + (cd.get(Calendar.DAY_OF_MONTH) > 9 ? cd.get(Calendar.DAY_OF_MONTH) : "0" + cd.get(Calendar.DAY_OF_MONTH));
                }
                dateString += " " + (cd.get(Calendar.HOUR_OF_DAY) > 9 ? cd.get(Calendar.HOUR_OF_DAY) : "0" + cd.get(Calendar.HOUR_OF_DAY)) + ":" + (cd.get(Calendar.MINUTE) > 9 ? cd.get(Calendar.MINUTE) : "0" + cd.get(Calendar.MINUTE));
                return dateString;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 格式化时间戳
     * 年-月-日
     * @param timeStamp
     * @return
     */
    public static String formatTime1(String timeStamp) {
        if (timeStamp != null && !TextUtils.isEmpty(timeStamp) && !("null").equals(timeStamp)) {
            String dateString = null;
            Calendar cd = Calendar.getInstance();
            cd.setTime(new Date(Double.valueOf(timeStamp).longValue() * 1000L));
            dateString = cd.get(Calendar.YEAR) + "-" + ((cd.get(Calendar.MONTH) + 1) > 9 ? (cd.get(Calendar.MONTH) + 1) : "0" + (cd.get(Calendar.MONTH) + 1)) + "-" + (cd.get(Calendar.DAY_OF_MONTH) > 9 ? cd.get(Calendar.DAY_OF_MONTH) : "0" + cd.get(Calendar.DAY_OF_MONTH));
            return dateString;
        } else {
            return "";
        }
    }

    /**
     * 格式化时间戳
     * 时-分
     * @param timeStamp
     * @return
     */
    public static String formatTime2(String timeStamp) {
        if (timeStamp != null && !TextUtils.isEmpty(timeStamp) && !("null").equals(timeStamp)) {
            String dateString = null;
            Calendar cd = Calendar.getInstance();
            int nowDayOfYear = cd.get(Calendar.DAY_OF_YEAR);
            cd.setTime(new Date(Double.valueOf(timeStamp).longValue() * 1000L));
            int date = cd.get(Calendar.DAY_OF_YEAR);
            dateString = (cd.get(Calendar.HOUR_OF_DAY) > 9 ? cd.get(Calendar.HOUR_OF_DAY) : "0" + cd.get(Calendar.HOUR_OF_DAY)) + ":" + (cd.get(Calendar.MINUTE) > 9 ? cd.get(Calendar.MINUTE) : "0" + cd.get(Calendar.MINUTE));
            return dateString;
        } else {
            return "";
        }
    }

    /**
     * 格式化时间戳
     * 时-分 or 年-月-日 时-分
     * @param timeStamp
     * @return
     */
    public static String formatTime3(String timeStamp) {
        if (timeStamp != null && !TextUtils.isEmpty(timeStamp) && !("null").equals(timeStamp)) {
            try {
                String dateString = null;
                Calendar cd = Calendar.getInstance();
                int nowDayOfYear = cd.get(Calendar.DAY_OF_YEAR);
                cd.setTime(new Date(Double.valueOf(timeStamp).longValue() * 1000L));
                int date = cd.get(Calendar.DAY_OF_YEAR);
                int value = nowDayOfYear - date;
                // 获取指定日期转换成星期几
                if (value == 0) {
                    dateString = (cd.get(Calendar.HOUR_OF_DAY) > 9 ? cd.get(Calendar.HOUR_OF_DAY) : "0" + cd.get(Calendar.HOUR_OF_DAY)) + ":" + (cd.get(Calendar.MINUTE) > 9 ? cd.get(Calendar.MINUTE) : "0" + cd.get(Calendar.MINUTE));
                } else {
                    dateString = cd.get(Calendar.YEAR) + "-" + ((cd.get(Calendar.MONTH) + 1) > 9 ? (cd.get(Calendar.MONTH) + 1) : "0" + (cd.get(Calendar.MONTH) + 1)) + "-" + (cd.get(Calendar.DAY_OF_MONTH) > 9 ? cd.get(Calendar.DAY_OF_MONTH) : "0" + cd.get(Calendar.DAY_OF_MONTH));
                    dateString += " " + (cd.get(Calendar.HOUR_OF_DAY) > 9 ? cd.get(Calendar.HOUR_OF_DAY) : "0" + cd.get(Calendar.HOUR_OF_DAY)) + ":" + (cd.get(Calendar.MINUTE) > 9 ? cd.get(Calendar.MINUTE) : "0" + cd.get(Calendar.MINUTE));
                }
                return dateString;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 格式化时间戳
     * 年-月-日 /n
     * 时-分
     * @param timeStamp
     * @return
     */
    public static String formatTime4(String timeStamp) {
        if (timeStamp != null && !TextUtils.isEmpty(timeStamp) && !("null").equals(timeStamp)) {
            try {
                String dateString = null;
                Calendar cd = Calendar.getInstance();
                int nowDayOfYear = cd.get(Calendar.DAY_OF_YEAR);
                cd.setTime(new Date(Double.valueOf(timeStamp).longValue() * 1000L));
                int date = cd.get(Calendar.DAY_OF_YEAR);
                int value = nowDayOfYear - date;
                // 获取指定日期转换成星期几
                if (value == 0) {
                    dateString = (cd.get(Calendar.HOUR_OF_DAY) > 9 ? cd.get(Calendar.HOUR_OF_DAY) : "0" + cd.get(Calendar.HOUR_OF_DAY)) + ":" + (cd.get(Calendar.MINUTE) > 9 ? cd.get(Calendar.MINUTE) : "0" + cd.get(Calendar.MINUTE));
                } else {
                    dateString = cd.get(Calendar.YEAR) + "-" + ((cd.get(Calendar.MONTH) + 1) > 9 ? (cd.get(Calendar.MONTH) + 1) : "0" + (cd.get(Calendar.MONTH) + 1)) + "-" + (cd.get(Calendar.DAY_OF_MONTH) > 9 ? cd.get(Calendar.DAY_OF_MONTH) : "0" + cd.get(Calendar.DAY_OF_MONTH));
                    dateString += "\n" + (cd.get(Calendar.HOUR_OF_DAY) > 9 ? cd.get(Calendar.HOUR_OF_DAY) : "0" + cd.get(Calendar.HOUR_OF_DAY)) + ":" + (cd.get(Calendar.MINUTE) > 9 ? cd.get(Calendar.MINUTE) : "0" + cd.get(Calendar.MINUTE));
                }
                return dateString;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 日期格式字符串转换时间戳
     * @param date
     * @param format
     * @return
     */
    public static String formatTime5(String date, String format) {
        if (date != null && !TextUtils.isEmpty(date) && !("null").equals(date)) {
            if (format == null || format.isEmpty()) {
                format = "yyyy-MM-dd HH:mm:ss";
            }
            try {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
                return String.valueOf(sdf.parse(date).getTime() / 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        } else {
            return "";
        }
    }

    /**
     * 日期字符串转换Date实体
     * @param serverTime
     * @return
     */
    public static Date formatTime7(String serverTime) {
        if (serverTime != null && !TextUtils.isEmpty(serverTime) && !("null").equals(serverTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
            String time = sdf.format(new Date(serverTime));
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            Date date = null;
            try {
                date = sdf.parse(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return date;
        } else {
            return null;
        }
    }

    /**
     * Date对象获取时间字符串
     * @param date
     * @param format
     * @return
     */
    public static String getDateStr(Date date,String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * 秒数转换成分秒
     * @param timer
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String formatTime6(long timer) {
        long nHour = timer / 3600;
        long nMin = timer % 3600;
        long nSec = nMin % 60;
        nMin = nMin / 60;

        return String.format("%02d:%02d", nMin, nSec);
    }

    /**
     * 秒数转换成时分秒
     * @param lSeconds
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String convertSecToTimeString(long lSeconds) {
        long nHour = lSeconds / 3600;
        long nMin = lSeconds % 3600;
        long nSec = nMin % 60;
        nMin = nMin / 60;

        return String.format("%02d小时%02d分钟%02d秒", nHour, nMin, nSec);
    }

    /**
     * 时间戳转换日期格式字符串
     * @param time
     * @param format
     * @return
     */
    public static String timeStamp2Date(long time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 日期格式字符串转换时间戳
     * @param date
     * @param format
     * @return
     */
    public static String date2TimeStamp(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取某个日期前后N天的日期
     * @param beginDate
     * @param distanceDay 前后几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @param format      日期格式，默认"yyyy-MM-dd"
     * @return
     */
    public static String getOldDateByDay(Date beginDate, int distanceDay, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat dft = new SimpleDateFormat(format);
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    /**
     * 获取前后几个月的日期
     * @param beginDate
     * @param distanceMonth
     * @param format
     * @return
     */
    public static String getOldDateByMonth(Date beginDate, int distanceMonth, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat dft = new SimpleDateFormat(format);
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.MONTH, date.get(Calendar.MONTH) + distanceMonth);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    /**
     * 判断一个时间是否在某个时间范围内
     * @param deadlineHour
     * @param deadlineMin
     * @return
     */
    public static boolean isCurrentInTimeScope(int deadlineHour, int deadlineMin) {
        boolean result;
        // 1000 * 60 * 60 * 24
        final long aDayInMillis = 86400000;
        final long currentTimeMillis = System.currentTimeMillis();
        //截止时间
        Time deadlineTime = new Time();
        deadlineTime.set(currentTimeMillis);
        deadlineTime.hour = deadlineHour;
        deadlineTime.minute = deadlineMin;
        //当前时间
        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        //当前时间推后20分钟
        Date d = new Date(currentTimeMillis);
        long myTime = (d.getTime() / 1000) + 20 * 60;
        d.setTime(myTime * 1000);
        Time endTime = new Time();
        endTime.set(myTime);
        if (!startTime.before(endTime)) {
            // 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !deadlineTime.before(startTime) && !deadlineTime.after(endTime);
            // startTime <= deadlineTime <=endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!deadlineTime.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            // 普通情况(比如 8:00 - 14:00)
            result = !deadlineTime.before(startTime) && !deadlineTime.after(endTime);
            // startTime <= deadlineTime <=endTime
        }
        return result;
    }

    /**
     * 字符串截取替换
     * @param string
     * @return
     */
    public static String formatStr(String string) {
        String phoneNumber = "";
        if ( string.length() == 11) {
            phoneNumber = string.substring(0, 3) + "****" + string.substring(7, string.length());

        } else if(string.length() == 12) {
            phoneNumber = string.substring(0, 3) + "****" + string.substring(8, string.length());

        } else if(string.length() == 13) {
            phoneNumber = string.substring(0, 3) + "****" + string.substring(9, string.length());

        } else {
            phoneNumber = string;
        }
        return phoneNumber;
    }

    /**
     * 转换姓名
     * @param name
     * @return
     */
    public static String getNameFormat(String name) {
        if (!TextUtils.isEmpty(name)) {
            name = name.substring(0, 1) + "师傅";
        }
        return name;
    }

    /**
     * 判断手机号是否合法
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isChinaPhoneLegal(String str)
            throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(166)|(17[0-8])|(18[0-9])|(19[8-9])|(147)|(145))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 获取最大宽度
     * @param context
     * @return
     */
    public static int getMaxWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics( dm );
        return dm.widthPixels;
    }

    /**
     * 获取最大高度
     * @param context
     * @return
     */
    public static int getMaxHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics( dm );
        return dm.heightPixels;
    }

    /**
     * 读取json文件
     * @param fileName 文件地址
     * @param context 上下文对象
     * @return 返回json字符串
     */
    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
