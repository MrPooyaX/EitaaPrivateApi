package com.example.eitaaprivateapi;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;

import androidx.annotation.RequiresApi;
import androidx.core.graphics.ColorUtils;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

/**
 * image filter types
 * suffixes:
 * f - image is wallpaper
 * isc - ignore cache for small images
 * b - need blur image
 * g - autoplay
 * lastframe - return lastframe for Lottie animation
 * lastreactframe - return lastframe for Lottie animation + some scale ReactionLastFrame magic
 * firstframe - return firstframe for Lottie or Video animation
 * ignoreOrientation - do not extract EXIF orientation and do not apply it to an imagereceiver
 * exif — check exif contents of invert/orientation
 */
public class ImageLoader {
    private static ThreadLocal<byte[]> bytesLocal = new ThreadLocal<>();

    public static Bitmap getStrippedPhotoBitmap(byte[] photoBytes, String filter) {
        int len = photoBytes.length - 3 + Bitmaps.header.length + Bitmaps.footer.length;
        byte[] bytes = bytesLocal.get();
        byte[] data = bytes != null && bytes.length >= len ? bytes : null;
        if (data == null) {
            bytes = data = new byte[len];
            bytesLocal.set(bytes);
        }
        System.arraycopy(Bitmaps.header, 0, data, 0, Bitmaps.header.length);
        System.arraycopy(photoBytes, 3, data, Bitmaps.header.length, photoBytes.length - 3);
        System.arraycopy(Bitmaps.footer, 0, data, Bitmaps.header.length + photoBytes.length - 3, Bitmaps.footer.length);

        data[164] = photoBytes[1];
        data[166] = photoBytes[2];

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, len);
        if (bitmap != null && !TextUtils.isEmpty(filter) && filter.contains("b")) {
            // Utilities.blurBitmap(bitmap, 3, 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
        }
        return bitmap;
    }
}