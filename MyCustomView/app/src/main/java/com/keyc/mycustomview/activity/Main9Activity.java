package com.keyc.mycustomview.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.keyc.mycustomview.R;
import com.keyc.mycustomview.utils.GlideCircleTransform;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class Main9Activity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvImg;
    private Button mBtImg;
    private byte[] mBytes;
    private String mBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9);

        mIvImg = (ImageView) findViewById(R.id.iv_img);
        mBtImg = (Button) findViewById(R.id.btn_img);
        mBtImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_img:
                selectPicture();
                break;
        }
    }

    /**
     * 选择图片
     */
    private void selectPicture() {
        /**
         * create()方法参数一是上下文，在activity中传activity.this，在fragment中传fragment.this。参数二为请求码，用于结果回调onActivityResult中判断
         * selectPicture()方法参数分别为 是否裁剪、裁剪后图片的宽(单位px)、裁剪后图片的高、宽比例、高比例。都不传则默认为裁剪，宽200，高200，宽高比例为1：1。
         */
        PictureSelector
                .create(Main9Activity.this, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(false, 200, 200, 1, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
//                mIvImg.setImageBitmap(ImageUtils.getBitmap(picturePath));
                //压缩图片
                getResult(picturePath);
            }
        }
    }

    /**
     * 获取图片
     * @param path
     */
    private void getResult(final String path) {
        Luban.with(this)
                .load(path)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
//                .setTargetDir(getPath())                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        FileInputStream inputFile = null;
                        try {
                            inputFile = new FileInputStream(file);
                            mBytes = new byte[(int)file.length()];
                            inputFile.read(mBytes);
                            inputFile.close();
                            mBase64 = "data:image/png;base64," + Base64.encodeToString(mBytes, Base64.NO_WRAP);
                            Log.i("TAG", "onActivityResult: " + mBase64);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Glide.with(Main9Activity.this)
                                .load(path)
                                .placeholder(R.drawable.family_avatar)
                                .error(R.drawable.family_avatar)
                                .centerCrop()
                                .transform(new GlideCircleTransform(Main9Activity.this))
                                .dontAnimate()
                                .into(mIvImg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        Toast.makeText(Main9Activity.this, "图片压缩失败，请重新选择图片", Toast.LENGTH_SHORT).show();
                    }
                }).launch();    //启动压缩
    }

}
