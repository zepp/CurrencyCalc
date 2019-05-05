package com.example.pavl.currencycalc.network;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class NetworkHandler {
    private final static String URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private final OkHttpClient client;
    private final File cacheDir;

    public NetworkHandler(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        cacheDir = context.getCacheDir();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.readTimeout(5, TimeUnit.SECONDS);
        client = builder.build();
    }

    public File fetch() throws IOException {
        Request request = new Request.Builder().url(URL).build();
        File file = new File(cacheDir, UUID.randomUUID().toString() + ".xml");

        try (OutputStream output = new FileOutputStream(file)){
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String contentType = response.headers().get("content-type");
                if (contentType.contains("text/xml") || contentType.contains("application/xml")) {
                    output.write(response.body().bytes());
                    output.flush();
                } else {
                    throw new RuntimeException("data type " + contentType + " is unsupported");
                }
            } else {
                throw new RuntimeException("server error: " + response.message());
            }
        }

        return file;
    }
}
