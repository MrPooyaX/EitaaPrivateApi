package com.example.eitaaprivateapi;

import android.content.pm.PackageInfo;
import android.os.Build;
import android.util.Log;

import com.example.eitaaprivateapi.tgnet.EitaaTLRPC;
import com.example.eitaaprivateapi.tgnet.TLRPC;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;


public class Utils {
    public static SecureRandom random = new SecureRandom();
    public static Pattern pattern = Pattern.compile("[\\-0-9]+");
    private static byte[] decompressBuffer;
    private static ByteArrayOutputStreamExpand decompressStream;
    public static Integer parseInt(CharSequence value) {
        int i = 0;
        if (value == null) {
            return 0;
        }
        try {
            Matcher matcher = pattern.matcher(value);
            if (matcher.find()) {
                i = Integer.parseInt(matcher.group(0));
            }
        } catch (Exception unused) {
        }
        return Integer.valueOf(i);
    }
    public static long getNextRandomId() {
        long val = 0;
        while (val == 0) {
            val = random.nextLong();
        }
        return val;
    }
    public static byte[] decompress(byte[] data) {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        try {
            if (decompressBuffer == null) {
                decompressBuffer = new byte[16384];
                decompressStream = new ByteArrayOutputStreamExpand(16384);
            }
            decompressStream.reset();
            GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream, 16384);
            while (true) {
                int read = gZIPInputStream.read(decompressBuffer);
                if (read != -1) {
                    decompressStream.write(decompressBuffer, 0, read);
                } else {
                    try {
                        break;
                    } catch (Exception e) {
                    }
                }
            }
            gZIPInputStream.close();
            try {
                byteArrayInputStream.close();
            } catch (Exception e2) {
            }

        } catch (IOException e3) {
            Log.i("myapp", e3.toString());
        }
        return decompressStream.toByteArray();
    }

    public static EitaaTLRPC.TL_AppInfo getAppInfo() {
        EitaaTLRPC.TL_AppInfo tl_appInfo = new EitaaTLRPC.TL_AppInfo();
        tl_appInfo.build_version = Application.BUILD_VERSION;
        try {
            String localeString = "en_US";
            tl_appInfo.lang_code = localeString;
            if (localeString.length() == 0) {
                tl_appInfo.lang_code = "en";
            }
            tl_appInfo.device_model = Build.MANUFACTURER + Build.MODEL;
            PackageInfo packageInfo = Application.applicationContext.getPackageManager().getPackageInfo(Application.applicationContext.getPackageName(), 0);
            //tLRPC$TL_AppInfo.app_version = packageInfo.versionName + " (" + packageInfo.versionCode + ")";
            tl_appInfo.app_version = "6.3.4 (24541)";
            StringBuilder sb = new StringBuilder();
            sb.append("SDK ");
            sb.append(Build.VERSION.SDK_INT);
            tl_appInfo.system_version = sb.toString();
        } catch (Exception e) {
            tl_appInfo.lang_code = "en";
            tl_appInfo.device_model = "Android unknown";
            tl_appInfo.app_version = "App version unknown";
            tl_appInfo.system_version = "SDK " + Build.VERSION.SDK_INT;
        }
        String str = tl_appInfo.lang_code;
        if (str == null || str.length() == 0) {
            tl_appInfo.lang_code = "en";
        }
        String str2 = tl_appInfo.device_model;
        if (str2 == null || str2.length() == 0) {
            tl_appInfo.device_model = "Android unknown";
        }
        String str3 = tl_appInfo.app_version;
        if (str3 == null || str3.length() == 0) {
            tl_appInfo.app_version = "App version unknown";
        }
        String str4 = tl_appInfo.system_version;
        if (str4 == null || str4.length() == 0) {
            tl_appInfo.system_version = "SDK Unknown";
        }
        tl_appInfo.sign = "3082034930820231a00302010202041facbdeb300d06092a864886f70d01010b05003055310b3009060355040613024952310c300a06035504081303516f6d310c300a06035504071303516f6d310e300c060355040a13054569746161310b3009060355040b13024954310d300b0603550403130454616861301e170d3138303130343135333131325a170d3432313232393135333131325a3055310b3009060355040613024952310c300a06035504081303516f6d310c300a06035504071303516f6d310e300c060355040a13054569746161310b3009060355040b13024954310d300b060355040313045461686130820122300d06092a864886f70d01010105000382010f003082010a028201010096278b0ea19a1389528e7666cb669c2d96c01175a219e2be3271cfabb75f6d0751c6d5a4115d202fe0f3a507a919561db2a85a31d04ea3642b823305bbbdc606d0aa3a7371a6e6cc92ab467753266518aab5dd06e8ad2506d78b12185f2d09dad635dd33fc3402580f0a1f4c672735d76d0decddff412c2573b22b44b7a35249cb00a5f43ac03fcf71eda951273a2409b9c5963dcadce383b86a8fe87d6a3039280077976cd4149870ef367124c824df5f18c4b7a4f11c8a841176110d1c876512c3147e2b283a002055f71bd2cfb0f117b3c5f5bcf6080c21cdb4c318c96de60769be658ed95fd5c69c696f22f002edbd0da7eae53b20a3af82df5cf44011770203010001a321301f301d0603551d0e041604144a9ebe0649605884b3dd8b7ce5df24058cfc614a300d06092a864886f70d01010b0500038201010081318251c9d034d4e52fbe98ed0a0066e9053cda67195ec3d426fd23eb3f32ec13abd0baf1b52b37279078fc453f6d39ee8cbcdd4cef4e1c07e8ad44f39f0f81a9b2d226154dd9fa4c4d0c6b757f4ec596dfc6775bbd7dd9e26ab24e06cab312b80c60187a6909421b9cfa3fee82410cc7f6dc7ad06fc2cbbe27bc6fa5d132f0729ef000a083857abb12849ba7b8448fb787e2811a1b28e16d4f252f9c200ff9cb68b2f287bb3c63d472c4ade364f00d724823765b70c4ec55da9a65113540ac1f82e9a75d5296e7772272a8da559bdf544a722bcb94ed889a971919419268e1f1d62d1fdf6913bc750db1060097ae0ab6ce0bfe3f420b53873b0e0b09aa16eb";
        return tl_appInfo;
    }

}
