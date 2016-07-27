package com.code.wing.core.util;

/**
 * Created by wing on 15/3/22.
 */

import android.content.Context;
import android.os.Environment;
import android.text.format.DateUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    /**外部路径*/
    private static File path;
    /**内容路径*/
    private static File appPath;
    private final static String CACHE_DIR_NAME = File.separator + "cache";

    /**
     * 获取保存文件位置根路径
     *
     * @param context
     * @param isOutPath 网络相关的请不要使用true!!!!!,其它可考虑是否保存在app外(沙盒)
     * @return File()
     */
    public static File getSaveRootPath(Context context, boolean isOutPath) {

        if (isOutPath) {
            if (path == null) {
                if (hasSDCard()) { // SD card
                    path = new File(getSDCardPath() + File.separator + "BondWithMe");
                    path.mkdir();
                } else {
                    path = Environment.getDataDirectory();
                }
            }
            return path;
        } else {
            if (appPath == null) {
                appPath = context.getFilesDir();
            }
            return appPath;
        }
    }

    /**
     * 获取异常保存路径
     * @param context
     * @return
     */
    public static String getSaveCrashPath(Context context) {
        String date = DateUtils.formatDateTime(context, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);
        return getCrashRootPath(context) + File.separator + date + ".log";
    }

    /**
     * 获取Crash文件保存位置根路径
     *
     * @param context
     * @return
     */
    private static File getCrashRootPath(Context context) {
        File file = new File(getSaveRootPath(context, true) + File.separator + "crash");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File[] getCrashFiles(Context context) {
        return getCrashRootPath(context).listFiles();
    }

    /**
     * 清除异常文件日志
     * @param context
     */
    public static void clearCrashFiles(Context context) {
        File[] cacheFiles = getCrashFiles(context);
        if (cacheFiles != null) {
            for (File file : cacheFiles) {
                file.delete();
            }
        }
    }

    /**
     * 获取全局缓存目录路径
     *
     * @param context
     * @return
     */
    public static String getCacheFilePath(Context context, boolean isOut) {
        File f = getSaveRootPath(context, isOut);

        f = new File(f.getAbsolutePath() + CACHE_DIR_NAME);
        if (!f.exists()) {
            f.mkdirs();
        }

        return f.getAbsolutePath();
    }

    /**
     * 判断是否有sd卡
     *
     * @return
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取sd卡路径
     *
     * @return
     */
    private static String getSDCardPath() {
        File path = Environment.getExternalStorageDirectory();
        return path.getAbsolutePath();
    }

    /***
     * clear the app cache create when photo handling,not app all cache
     *
     * @param context
     */
    public static void clearCache(Context context) {
        clearCache(context,true);
        clearCache(context,false);
    }

    /**
     * clear the app cache create when photo handling or downloading,not app all cache
     * @param context
     * @param isOut 是否程序外(一般下载为程序内,图片裁剪等处理在程序外)
     */
    public static void clearCache(Context context,boolean isOut) {
        File fileRoot = new File(getCacheFilePath(context,isOut));
        if (fileRoot != null) {
            File[] cacheFiles = fileRoot.listFiles();
            for (File file : cacheFiles) {
                file.delete();
            }
        }
    }

    /**
     * get all files path of assets by parent path
     *
     * @param context
     * @param path    parent path
     * @return
     */
    public static List<String> getAllFilePathsFromAssets(Context context, final String path) {
        List<String> filePaths = new ArrayList<>();
        String[] fileNames = null;
        try {
            fileNames = context.getAssets().list(path);
            if (fileNames != null) {
                for (String fileName : fileNames) {
                    filePaths.add(fileName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePaths;
    }


    /**
     * 获取临时文件名称
     * @param context
     * @return
     * @throws IOException
     */
    private static String getTempFilename(Context context) throws IOException {
        File outputFile = File.createTempFile("image", "tmp", getSaveRootPath(context, true));
        return outputFile.getAbsolutePath();
    }

}