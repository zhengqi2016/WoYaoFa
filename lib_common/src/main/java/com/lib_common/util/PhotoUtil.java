package com.lib_common.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.lib_common.BaseConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoUtil {
    /**
     * 调用相机的请求码
     */
    public static final int REQUESTCODE_CAMERA = 20;
    /**
     * 调用图库的请求码
     */
    public static final int REQUESTCODE_PIC = 21;
    private static String fileName;
    private static String path;
    private static String orientation;

    /**
     * 设置照片路径
     *
     * @param activity
     * @return
     */
    public static File setPhotoFilePath(Activity activity) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory()
                    .getPath() + BaseConfig.DEFAULT_DIR_NAME;
        } else {
            path = activity.getCacheDir().getParent()
                    + BaseConfig.DEFAULT_DIR_NAME;
        }
        fileName = "hemiao"
                + DateUtil.datetimeToString(new Date(), "yyyyMMddHHmmss")
                + ".jpg";
        return FileUtil.initFile(path, fileName);
    }

    public static void callCamera(Activity activity) {
        Uri u = Uri.fromFile(setPhotoFilePath(activity));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
        activity.startActivityForResult(intent, REQUESTCODE_CAMERA);
    }

    /**
     * 调用图库功能
     *
     * @param activity
     */
    public static void callPic(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        activity.startActivityForResult(intent, REQUESTCODE_PIC);
    }

    /**
     * 照相回调函数 图片大于1080的已经压缩并保存到SD卡的
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @param activity
     * @return
     */
    public static String OnPhotoResult(Activity activity, int requestCode,
                                       int resultCode, Intent data) {
        String url = null;
        if (requestCode == REQUESTCODE_CAMERA
                && resultCode == Activity.RESULT_OK) {
            url = path + "/" + fileName;
        } else if (requestCode == REQUESTCODE_PIC
                && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // 照片的原始资源地址
                Uri originalUri = data.getData();
                url = getAbsoluteImagePath(activity, originalUri);
            }
        }
        return url;
    }

    /**
     * @param fPath
     * @return
     */
    public static InputStream uploadCompressBitmap(Activity act, String fPath) {
        Bitmap bitmap = loadCompressBitmap(fPath, DensityUtil.getScreenW(act));
        ByteArrayOutputStream output = new ByteArrayOutputStream();// 初始化一个流对象
        bitmap.compress(CompressFormat.JPEG, 100, output);
        bitmap = null;
        System.gc();
        return new ByteArrayInputStream(output.toByteArray());
    }

    /**
     * @param path
     * @param ratio 目标宽度 像素
     * @return
     */
    public static Bitmap loadCompressBitmap(String path, int ratio) {
        // 对图片进行压缩
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(path, options);
        // 计算缩放比
        int be = Math
                .round(options.outHeight / (float) ratio > options.outWidth
                        / (float) ratio ? options.outHeight / (float) ratio
                        : options.outWidth / (float) ratio);// 配置图片分辨率
        be = be <= 0 ? 1 : be;
        options.inSampleSize = be;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    /**
     * 获取文件的绝对路径
     *
     * @param activity
     * @param uri      文件的uri
     * @return 路径
     */
    public static String getAbsoluteImagePath(Activity activity, Uri uri) {
        // can post image
        ContentResolver cr = activity.getContentResolver();
        if (uri == null) {
            return null;
        }
        Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
        String filePath = null;
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路
            orientation = cursor
                    .getString(cursor.getColumnIndex("orientation"));// 获取旋转的角度
        }
        return filePath;
    }

    // <<<<<<<<<<<<<<<<<>>>>>>>>>>>>>><<<<<<<<<<<>>>>>>><><><><><><><><><<><><><><><>

    /**
     * 根据图片路径压缩图片 并返回压缩有的图片路径
     *
     * @param fPath
     * @return
     */
    public static String compressedImage(String fPath) {
        Bitmap bitmap = null;
        Bitmap rotatedBitmap = null;
        String url = null;
        try {
            bitmap = loadCompressBitmap(fPath, 480);
            rotatedBitmap = rotateBitmapByDegree(bitmap, getBitmapDegree(fPath));// 把三星照片摆正
            ByteArrayOutputStream output = new ByteArrayOutputStream();// 初始化一个流对象
            rotatedBitmap.compress(CompressFormat.JPEG, 100, output);
            FileOutputStream fileOutputStream = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            fileName = df.format(new Date()) + ".jpg";
            url = path + fileName;
            fileOutputStream = new FileOutputStream(url);
            fileOutputStream.write(output.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            ToastUtil.printErr(e);
        } catch (IOException e) {
            ToastUtil.printErr(e);
        } finally {
            if (!rotatedBitmap.isRecycled()) {
                rotatedBitmap.recycle();
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        return url;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            ToastUtil.printErr(e);
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
}
