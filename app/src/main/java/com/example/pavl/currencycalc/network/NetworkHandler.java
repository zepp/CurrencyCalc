package com.example.pavl.currencycalc.network;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class NetworkHandler {
    private final static String URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private final static String FILE = "rates.xml";
    private final static String TAG = NetworkHandler.class.getSimpleName();
    private final Context context;
    private final OkHttpClient client;

    public NetworkHandler(Context context) {
        this.context = context;
        this.client = new OkHttpClient();
    }

    public static File getFile(Context context) {
        return new File(context.getCacheDir(), FILE);
    }

    public void fetch() {
        Request request = new Request.Builder().url(URL).build();
        OutputStream output = null;
        File file = new File(context.getCacheDir(), FILE + ".new");

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
            file.renameTo(getFile(context));
        } catch (Exception e) {
            Log.e(TAG, "failed to fetch data: " + e.getMessage());
        } finally {
            try {
                output.close();
            } catch (Exception e) {
            }
        }
    }
}
