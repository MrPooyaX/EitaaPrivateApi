package com.example.eitaaprivateapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HelperHttp {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType TEXT = MediaType.parse("text/stream");
    public static String URL = "https://dev3.eitaa.com/eitaa/index.php";
    private static int connectionTimeout = 60000;
    private static final OkHttpClient httpClient;
    private String url;

    public static interface Result {
        void onResponse(byte[] response);

        void onFailure(String str);
    }
    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        long j = connectionTimeout;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        httpClient = builder.connectTimeout(j, timeUnit).readTimeout(connectionTimeout, timeUnit).writeTimeout(connectionTimeout, timeUnit).build();
    }

    public HelperHttp(String ip_address, int port, String url) {
        this.url = (port == 443 ? "https" : "http").concat("://").concat(ip_address == null ? "armita.eitaa.com" : ip_address).concat(":").concat(String.valueOf(port)).concat(url);
    }

    public void send(byte[] request,Result result) {
        Request build;
        try {
            build = new Request.Builder().url(this.url).post(RequestBody.create(TEXT, request)).build();
        } catch (Exception e) {
            build = new Request.Builder().url("https://armita.eitaa.com/eitaa/index.php").post(RequestBody.create(TEXT, request)).build();
        }
        try {
            httpClient.newCall(build).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    result.onFailure("err");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] byteArray = toByteArray(response.body().byteStream());
                    result.onResponse(byteArray);
                }
            });
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[4096];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
            } else {
                return byteArrayOutputStream.toByteArray();
            }
        }
    }
}