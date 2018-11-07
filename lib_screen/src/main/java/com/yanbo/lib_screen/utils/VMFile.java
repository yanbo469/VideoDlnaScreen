package com.yanbo.lib_screen.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.yanbo.lib_screen.VApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * 描述：
 *
 * @author Yanbo
 * @date 2018/11/6
 */
public class VMFile {


    /**
     * 判断目录是否存在
     *
     * @param path 目录路径
     */
    public static boolean isDirExists(String path) {
        File dir = new File(path);
        return dir.exists();
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     */
    public static boolean isFileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 创建目录，多层目录会递归创建
     */
    public static boolean createDirectory(String path) {
        File dir = new File(path);
        if (!isDirExists(path)) {
            return dir.mkdirs();
        }
        return true;
    }

    /**
     * 创建新文件
     */
    public static boolean createFile(String filepath) {
        boolean isSuccess = false;
        File file = new File(filepath);
        // 判断文件上层目录是否存在，不存在则首先创建目录
        if (!isDirExists(file.getParent())) {
            createDirectory(file.getParent());
        }
        if (!file.isFile()) {
            try {
                isSuccess = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    /**
     * 复制文件
     *
     * @param srcPath 源文件地址
     * @param filepath2 目标文件地址
     * @return 返回复制结果
     */
    public static boolean copyFile(String srcPath, String filepath2) {
        File file1 = new File(srcPath);
        if (!file1.exists()) {
            LogUtils.e("","源文件不存在，无法完成复制");
            return false;
        }
        File file2 = new File(filepath2);
        LogUtils.i("",file2.getParent());
        if (!isDirExists(file2.getParent())) {
            createDirectory(file2.getParent());
        }
        try {
            InputStream inputStream = new FileInputStream(file1);
            FileOutputStream outputStream = new FileOutputStream(filepath2);
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
            }
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            LogUtils.e("拷贝文件出错：" , e);
            return false;
        }
        return true;
    }

    /**
     * 读取文件到 Bitmap
     */
    public static Bitmap fileToBitmap(String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(filepath);
            return bitmap;
        }
        return null;
    }

    /**
     * 读取文件到drawable
     *
     * @param filepath 文件路径
     * @return 返回Drawable资源
     */
    public static Drawable fileToDrawable(String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
            Drawable drawable = Drawable.createFromPath(filepath);
            return drawable;
        }
        return null;
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String filepath) {
        LogUtils.i("删除文件：" , filepath);
        File file = new File(filepath);
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * 删除文件集合
     *
     * @param paths 文件路径集合
     */
    public static void deleteFiles(List<String> paths) {
        for (String path : paths) {
            deleteFile(path);
        }
    }

    /**
     * 格式化文件字节大小
     */
    public static String formatSize(long size) {
        BigDecimal result;
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            result = new BigDecimal(Double.toString(kiloByte));
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            result = new BigDecimal(Double.toString(megaByte));
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraByte = gigaByte / 1024;
        if (teraByte < 1) {
            result = new BigDecimal(Double.toString(gigaByte));
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        result = new BigDecimal(Double.toString(teraByte));
        return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 递归实现遍历文件夹大小
     *
     * @param fileDir 要计算的文件夹
     */
    public static long getFolderSize(File fileDir) {
        long size = 0;
        if (!fileDir.exists()) {
            return size;
        }
        File[] fileList = fileDir.listFiles();
        for (File file : fileList) {
            if (file.isDirectory()) {
                size += getFolderSize(file);
            } else {
                size += file.length();
            }
        }
        return size;
    }

    /**
     * 递归删除文件夹内的文件
     *
     * @param path 需要操作的路径
     * @param deleteThisPath 删除自己
     */
    public static void deleteFolderFile(String path, boolean deleteThisPath) {
        if (path == "" || path == null) {
            return;
        }
        File fileSrc = new File(path);
        if (fileSrc.isDirectory()) {
            File[] files = fileSrc.listFiles();
            for (File file : files) {
                deleteFolderFile(file.getAbsolutePath(), true);
            }
        }
        if (deleteThisPath) {
            if (!fileSrc.isDirectory()) {
                fileSrc.delete();
            }
        }
    }

    /**
     * 根据文件路径解析文件名，不包含扩展类型
     */
    public static String parseResourceId(String path) {
        String result = null;
        if (path != null && path.length() > 0) {
            int index = path.lastIndexOf("/");
            String fileName = path.substring(index + 1);
            result = fileName.substring(0, fileName.lastIndexOf("."));
        }
        return result;
    }

    /**
     * 判断sdcard是否被挂载
     */
    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取Android系统的一些默认路径
     * 不常用：
     * Environment.getDataDirectory().getPath()             : /data
     * Environment.getDownloadCacheDirectory().getPath()    : /cache
     * Environment.getRootDirectory().getPath()             : /system
     *
     * 常用：
     * Environment.getExternalStorageDirectory().getPath()  : /mnt/sdcard (storage/emulated/0)
     * Context.getCacheDir().getPath()                      : /data/data/packagename/cache
     * Context.getExternalCacheDir().getPath()              : /mnt/sdcard/Android/data/packagename/cache
     * Context.getFilesDir().getPath()                      : /data/data/packagename/files
     * Context.getObbDir().getPath()                        : /mnt/sdcard/Android/obb/packagename
     * Context.getPackageName()                             : packagename
     * Context.getPackageCodePath()                         : /data/app/packagename-1.apk
     * Context.getPackageResourcePath()                     : /data/app/packagename-1.apk
     */
    /**
     * Root 目录，一般不常用
     *
     * String rootCache = Environment.getDownloadCacheDirectory().getPath();
     * String rootData = Environment.getDataDirectory().getPath();
     * String rootSystem = Environment.getRootDirectory().getPath();
     *
     * SDCard 目录
     * Environment.getExternalStorageDirectory().getPath();
     * 当前 app 在 root 下的缓存目录
     * VMTools.getContext().getCacheDir().getPath();
     * 当前 app 在 SDCard 下的缓存目录
     * VMTools.getContext().getExternalCacheDir().getPath();
     * 当前 app 在 root 下的 files 目录
     * VMTools.getContext().getFilesDir().getPath();
     * VMTools.getContext().getFilesDir().getPath();
     * 当前 app 在 SDCard 下的 obb 目录，一般是apk包过大要分出资源包，游戏用的比较多
     * VMTools.getContext().getObbDir().getPath();
     * 获取当前 app 包名
     * VMTools.getContext().getPackageName();
     * 获取当前 app 代码路径
     * VMTools.getContext().getPackageCodePath();
     * 获取当前 app 资源路径
     * VMTools.getContext().getPackageResourcePath();
     *
     * 获取常用目录的方法，参数是需要获取的目录类型，可以是download，camera
     * Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
     * return null;
     */

    /**
     * 获取 /sdcard (/storage/emulated/0) 目录
     *
     * @return 返回得到的路径
     */
    public static String getSDCard() {
        return Environment.getExternalStorageDirectory().getPath() + "/";
    }

    /**
     * 获取 /data/data/packagename/cache 目录
     *
     * @return 返回得到的路径
     */
    public static String getCacheFromData() {
        return VApplication.getContext().getCacheDir().getPath() + "/";
    }

    /**
     * 获取 /sdcard/Android/data/packagename/cache 目录
     *
     * @return 返回得到的路径
     */
    public static String getCacheFromSDCard() {
        return VApplication.getContext().getExternalCacheDir().getPath() + "/";
    }

    /**
     * 获取/data/data/packagename/files 目录
     *
     * @return 返回得到的路径
     */
    public static String getFilesFromData() {
        return VApplication.getContext().getFilesDir().getPath() + "/";
    }

    /**
     * 获取 /sdcard/Android/data/packagename/files 目录
     *
     * @return 返回得到的路径
     */
    public static String getFilesFromSDCard() {
        return VApplication.getContext().getExternalFilesDir("").getAbsolutePath() + "/";
    }

    /**
     * 获取 /sdcard/Android/obb/packagename 目录
     *
     * @return 返回得到的路径
     */
    public static String getOBB() {
        return VApplication.getContext().getObbDir().getAbsolutePath() + "/";
    }

    /**
     * 获取设备默认的相册目录
     *
     * @return 返回得到的路径
     */
    public static String getDCIM() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .getAbsolutePath() + "/";
    }

    /**
     * 获取设备默认的下载目录
     *
     * @return 返回得到的路径
     */
    public static String getDownload() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath() + "/";
    }

    /**
     * 获取设备默认的音乐目录
     *
     * @return 返回得到的路径
     */
    public static String getMusic() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                .getAbsolutePath() + "/";
    }

    /**
     * 获取设备默认的电影目录
     *
     * @return 返回得到的路径
     */
    public static String getMovies() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                .getAbsolutePath() + "/";
    }

    /**
     * 获取设备默认的图片目录
     */
    public static String getPictures() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath() + "/";
    }

    /**
     * 获取 packagename 目录
     *
     * @return 返回得到的路径
     */
    public static String getPackageName() {
        return VApplication.getContext().getPackageName();
    }

    /**
     * 获取 /data/app/packagename-1.apk 目录
     *
     * @return 返回得到的路径
     */
    public static String getPackageCode() {
        return VApplication.getContext().getPackageCodePath();
    }

    /**
     * 获取 /data/app/packagename-1.apk 目录
     *
     * @return 返回得到的路径
     */
    public static String getPackageResource() {
        return VApplication.getContext().getPackageResourcePath();
    }

    /**
     * 根据 Uri 获取文件的真实路径，这个是网上的方法，用的还是比较多的，可以参考，
     * 不过在选择google相册的图片的时候，如果本地不存在图片会出现问题
     *
     * @param uri 包含文件信息的 Uri
     * @return 返回文件真实路径
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Uri uri) {

        // 判断当前系统 API 4.4（19）及以上
        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(VApplication.getContext(), uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long
                        .valueOf(id));

                return getDataColumn(VApplication.getContext(), contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(VApplication.getContext(), contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            // Return the remote address
            // 这里先判断是否是通过 Google 相册 选择的图片，同时这个图片不存在于本地
            if (isGooglePhotosUri(uri)) {
                //                return null;
                return uri.getLastPathSegment();
            }
            return getDataColumn(VApplication.getContext(), uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // File
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver()
                    .query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 这里我修改了下，我在最新的5.1上选择的一个在 Google相册里的一张图片时，这个 uri.getAuthority() 的值有所改变
     * com.google.android.apps.photos.contentprovider，之前的结尾是content，我测试的为contentprovider
     *
     * @param uri 需要判断的 Uri
     * @return 判断这个 Uri 是否是通过 Google 相册 选择的
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
