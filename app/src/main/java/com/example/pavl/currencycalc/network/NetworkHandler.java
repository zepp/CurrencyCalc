package com.example.pavl.currencycalc.network;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class NetworkHandler {
    private final static String URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private final Context context;
    private final OkHttpClient client;

    public NetworkHandler(Context context) {
        this.context = context;
        this.client = new OkHttpClient();
    }

    public File fetch() throws IOException {
        Request request = new Request.Builder().url(URL).build();
        OutputStream output = null;
        File file = new File(context.getCacheDir(), UUID.randomUUID().toString());

        try {
            output = new FileOutputStream(file);
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                throw new RuntimeException("server error: " + response.message());
            }
            String contentType = response.headers().get("content-type");
            if (!(contentType.contains("text/xml") || contentType.contains("application/xml"))) {
                throw new RuntimeException("data type " + contentType + " is unsupported");
            }
            output.write(response.body().bytes());
            output.flush();
            output.close();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
            }
        }

        return file;
    }
}
