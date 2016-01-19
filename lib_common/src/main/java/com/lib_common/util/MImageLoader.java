package com.lib_common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;

import com.lib_common.R;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author Administrator 图片加载类，在程序中只实例一次
 */
public class MImageLoader {
    private static DisplayImageOptions options;
    private static ImageLoaderConfiguration config;
    private static ImageLoader imageLoader;
    private static Builder builder;
    public static final int DEFAULT_ANGLE = 20;
    public static final int DEFAULT_IMAGE = R.mipmap.default_icon;
    public static final int DEFAULT_LOADING_IMAGE = R.mipmap.default_icon;
    public static final String rootPath = Environment
            .getExternalStorageDirectory().getPath();

    /**
     * 获取ImageLoader
     *
     * @param context
     * @return
     */
    public static ImageLoader getInstance(Context context) {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
            config = new ImageLoaderConfiguration.Builder(context)
                    .memoryCacheExtraOptions(480, 800)
                            // default = device screen dimensions
                            // .discCacheExtraOptions(480, 800, null)
                            // .connectTimeout (3000)
                            // , readTimeout (30 s)超时时间
                    .imageDownloader(
                            new BaseImageDownloader(context, 3 * 1000,
                                    15 * 1000))
                            // .diskCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                    .threadPoolSize(5)
                    .threadPriority(Thread.NORM_PRIORITY - 1)
                    .tasksProcessingOrder(QueueProcessingType.FIFO)
                            // .denyCacheImageMultipleSizesInMemory()//关闭不同尺寸的相同图片的缓存
                    .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                    .memoryCacheSize(2 * 1024 * 1024)
                    .memoryCacheSizePercentage(13) // default
                            // .discCache(new UnlimitedDiscCache(cacheDir))// default
                    .discCacheSize(50 * 1024 * 1024) // 缓冲大小
                    .discCacheFileCount(200) // 缓冲文件数目
                    .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                    .imageDownloader(new BaseImageDownloader(context)) // default
                    .imageDecoder(new BaseImageDecoder(true)) // default
                    .defaultDisplayImageOptions(
                            DisplayImageOptions.createSimple()) // default
                    .writeDebugLogs().build();
            imageLoader.init(config);
        }
        return imageLoader;
    }

    public static void display(Context context, String url,
                               ImageView imageView, boolean isReset, int defaultImage, int angle,
                               ImageLoadingListener listener) {
        if (imageView == null) {
            return;
        }
        if (url == null) {
            url = "";
        }
        if (defaultImage == 0) {
            defaultImage = DEFAULT_IMAGE;
        }
        if (url.startsWith("http://")) {
            options = new Builder().delayBeforeLoading(0)
                    .cacheInMemory(false)
                    .cacheOnDisc(true)
                    .resetViewBeforeLoading(isReset)
                    .showImageOnFail(defaultImage)
                            // 不是图片文件 显示图片
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .bitmapConfig(Bitmap.Config.RGB_565).handler(new Handler())
                    .displayer(new RoundedBitmapDisplayer(angle)) // default
                    .build();
        } else if (url.contains(rootPath)) {
            options = new Builder().delayBeforeLoading(0)
                    .cacheInMemory(false).cacheOnDisc(false)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .resetViewBeforeLoading(isReset)
                    .showImageOnFail(defaultImage)
                            // 不是图片文件 显示图片
                    .bitmapConfig(Bitmap.Config.RGB_565).handler(new Handler())
                    .displayer(new RoundedBitmapDisplayer(angle)) // default
                    .build();
            url = "file://" + url;
        } else {
            options = new Builder().delayBeforeLoading(0)
                    .cacheInMemory(false).cacheOnDisc(false)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .resetViewBeforeLoading(isReset)
                    .showImageOnFail(defaultImage)
                            // 不是图片文件 显示图片
                    .bitmapConfig(Bitmap.Config.RGB_565).handler(new Handler())
                    .displayer(new RoundedBitmapDisplayer(angle)) // default
                    .build();
            url = "drawable://" + url;
        }
        getInstance(context).displayImage(url, imageView, options, listener);
    }

    public static void display(Context context, String url,
                               ImageView imageView, ImageLoadingListener listener) {
        if (url == null) {
            url = "";
        }
        if (url.startsWith("http://")) {
        } else if (url.contains(rootPath)) {
            url = "file://" + url;
        } else {
            url = "drawable://" + url;
        }
        getInstance(context).displayImage(url, imageView, listener);
    }

    // <><><><><><><><><><<><>< 不带监听
    // ><><><><>><><><><><<><><><><><><><><>><><><><><<><><><><>

    public static void display(Context context, String url,
                               ImageView imageView, DisplayImageOptions options) {
        getInstance(context).displayImage(url, imageView, options);
    }

    /**
     * @param context
     * @param url
     * @param imageView
     * @param defaultImage        加载失败的默认图片
     * @param defaultLoadingImage 加载时的默认图片
     * @param isCacheDisc         网络图片是否缓存
     * @param angle               角度
     * @param bitmapConfig        图片像素模式 Bitmap.Config.RGB_565、Bitmap.Config.ARGB_8888
     */
    public static void display(Context context, String url,
                               ImageView imageView, boolean isCacheDisc, int defaultImage,
                               int defaultLoadingImage, Boolean isResetImage, int angle,
                               Bitmap.Config bitmapConfig) {
        if (imageView == null) {
            return;
        }
        if (defaultImage == 0) {
            defaultImage = DEFAULT_IMAGE;
        }
        if (defaultLoadingImage == 0) {
            defaultLoadingImage = DEFAULT_LOADING_IMAGE;
        }
        if (url == null) {
            url = "" + defaultImage;
        }
        builder = new Builder()
                .showImageOnLoading(defaultLoadingImage)
                        // image在加载过程中，显示的图片
                .showImageOnFail(defaultImage)
                        // 不是图片文件 显示图片
                .resetViewBeforeLoading(isResetImage).delayBeforeLoading(0)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)// 图片显示模式
                .cacheInMemory(false) // default
                .bitmapConfig(bitmapConfig); // default
        if (angle != 0) {
            builder.displayer(new RoundedBitmapDisplayer(angle));
        }
        if (url.startsWith("http://")) {
            builder.cacheOnDisk(isCacheDisc);
        } else if (url.contains(rootPath)) {
            builder.cacheOnDisk(false);
            url = "file://" + url;
        } else {
            builder.cacheOnDisk(false);
            url = "drawable://" + url;
        }
        options = builder.build();
        display(context, url, imageView, options);
    }

    /**
     * @param context
     * @param url
     * @param imageView
     * @param defaultImage        加载失败的默认图片
     * @param defaultLoadingImage 加载时的默认图片
     * @param isCacheDisc         网络图片是否缓存
     * @param angle               角度
     */
    public static void display(Context context, String url,
                               ImageView imageView, boolean isCacheDisc, int defaultImage,
                               int defaultLoadingImage, Boolean isResetImage, int angle) {
        display(context, url, imageView, isCacheDisc, defaultImage,
                defaultLoadingImage, isResetImage, angle, Bitmap.Config.RGB_565);
    }

    /**
     * @param context
     * @param url
     * @param isResetImage
     * @param angle        角度
     */
    public static void display(Context context, String url,
                               ImageView imageView, Boolean isResetImage, int angle) {
        display(context, url, imageView, true, 0, 0, isResetImage, angle,
                Bitmap.Config.RGB_565);
    }

    public static void display(Context context, String url,
                               ImageView imageView, int defaultImage, Boolean isResetImage,
                               int angle) {
        display(context, url, imageView, true, defaultImage, 0, isResetImage,
                angle, Bitmap.Config.RGB_565);
    }

    public static void display(Context context, String url,
                               ImageView imageView, int defaultImage, int defaultLoadingImage,
                               Boolean isResetImage, int angle) {
        display(context, url, imageView, true, defaultImage,
                defaultLoadingImage, isResetImage, angle, Bitmap.Config.RGB_565);
    }

    /**
     * @param context
     * @param url
     * @param imageView
     */
    public static void displayWithDefaultOptions(Context context, String url,
                                                 ImageView imageView) {
        display(context, url, imageView, true, 0, 0, false, 0,
                Bitmap.Config.RGB_565);
    }

    /**
     * @param context
     * @param url
     * @param imageView
     */
    public static void display(Context context, String url,
                               ImageView imageView, Bitmap.Config bitmapConfig) {
        display(context, url, imageView, true, 0, 0, false, 0, bitmapConfig);
    }

    // //////////////////////////////////清缓存///////////////////////////////////////////////

    /**
     * 清除某张图片的缓存
     *
     * @param context
     * @param url
     */
    public static void cleanTheCacheOnDisk(Context context, String url) {
        if (url != null && !url.trim().equals("")) {
            getInstance(context).getDiskCache().remove(url);
        }
    }
}
