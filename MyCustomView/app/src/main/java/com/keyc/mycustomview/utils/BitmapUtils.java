package com.keyc.mycustomview.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by keyC on 2019/6/17.
 * 图片获取工具类
 */

public class BitmapUtils {
    /**
     * 通过BitmapDrawable来获取Bitmap
     * @param mContext
     * @param fileName
     * @return
     */
    public static Bitmap getBitmapFromBitmapDrawable(Context mContext, String fileName) {
        BitmapDrawable bmpMeizi = null;
        try {
            bmpMeizi = new BitmapDrawable(mContext.getAssets().open(fileName));//"pic_meizi.jpg"
            Bitmap mBitmap = bmpMeizi.getBitmap();
            return mBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过资源ID获取Bitmap
     * @param res
     * @param resId
     * @return
     */
    public static Bitmap getBitmapFromResource(Resources res, int resId) {
        return BitmapFactory.decodeResource(res, resId);
    }

    /**
     * 通过文件路径来获取Bitmap
     * @param pathName
     * @return
     */
    public static Bitmap getBitmapFromFile(String pathName) {
        return BitmapFactory.decodeFile(pathName);
    }

    /**
     * 通过字节数组来获取Bitmap
     * @param b
     * @return
     */
    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 通过输入流InputStream来获取Bitmap
     * @param inputStream
     * @return
     */
    public static Bitmap getBitmapFromStream(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }
}
