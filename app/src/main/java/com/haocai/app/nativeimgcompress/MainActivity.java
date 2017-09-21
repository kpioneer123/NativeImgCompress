package com.haocai.app.nativeimgcompress;

import android.os.Bundle;

/**
 * Created by Xionghu on 2017/9/20.
 * Desc:
 */

import java.io.File;
import java.io.IOException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.haocai.app.nativeimgcompress.utils.NativeUtil;


public class MainActivity extends Activity {
    public static final int REQUEST_PICK_IMAGE = 10011;
    public static final int REQUEST_KITKAT_PICK_IMAGE = 10012;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pickFromGallery(View v) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    REQUEST_PICK_IMAGE);
            Log.e("======", "========13图片为空======");
        } else {
            Log.e("======", "========14图片为空======");
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_KITKAT_PICK_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("======", "========1图片为空======");
        if (resultCode == Activity.RESULT_OK) {
            Log.e("======", "========2图片为空======");
            switch (requestCode) {

                case REQUEST_PICK_IMAGE:
                    if (data != null) {
                        Uri uri = data.getData();
                        compressImage(uri);
                    } else {
                        Log.e("======", "========图片为空======");
                    }
                    break;
                case REQUEST_KITKAT_PICK_IMAGE:
                    if (data != null) {
                        Uri uri = ensureUriPermission(this, data);
                        compressImage(uri);
                    } else {
                        Log.e("======", "====-----==图片为空======");
                    }
                    break;
            }
        }
    }

    @SuppressWarnings("ResourceType")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Uri ensureUriPermission(Context context, Intent intent) {
        Uri uri = intent.getData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final int takeFlags = intent.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
            context.getContentResolver().takePersistableUriPermission(uri, takeFlags);
        }
        return uri;
    }


    public void compressImage(Uri uri) {
        Log.e("===compressImage===", "====开始====uri==" + uri.getPath());
        try {
            File saveFile = new File(getExternalCacheDir(), "终极压缩.jpg");
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            Log.e("===compressImage===", "====开始==压缩==saveFile==" + saveFile.getAbsolutePath());
            NativeUtil.compressBitmap(bitmap, saveFile.getAbsolutePath());
            Log.e("===compressImage===", "====完成==压缩==saveFile==" + saveFile.getAbsolutePath());


            File saveFile1 = new File(getExternalCacheDir(), "质量压缩.jpg");
            NativeUtil.compressImageToFile(bitmap,saveFile1);


            File saveFile2 = new File(getExternalCacheDir(), "尺寸压缩.jpg");
            NativeUtil.compressBitmapToFile(bitmap,saveFile2);

            Log.e("===compressImage===", "====uri==" + uri.toString());

            File saveFile3 = new File(getExternalCacheDir(), "采样率压缩.jpg");

            File f = new File("/storage/sdcard0/DCIM/Camera/IMG_20161130_200251.jpg");
            if(f.exists()){
                NativeUtil.compressBitmap(f.getAbsolutePath(),saveFile3);
            }else{
                Log.e("===compressImage===", "采样率压缩找不到这个代码里面写死的图片哦~~~~");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
