package com.example.pavl.currencycalc.background;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class NetworkHandler extends HandlerThread {
    private final static String URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private final static String FILE = "rates.xml";
    private final static String TAG = NetworkHandler.class.getSimpleName();
    private static volatile NetworkHandler networkHandler;
    private final Context context;
    private final OkHttpClient client;
    private final EventHandler eventHandler;
    private Handler handler;

    private NetworkHandler(Context context) {
        super(NetworkHandler.class.getSimpleName());
        this.context = context;
        this.client = new OkHttpClient();
        this.eventHandler = EventHandler.getInstance();
    }

    public static NetworkHandler getInstance(Context context) {
        if (networkHandler == null) {
            synchronized (NetworkHandler.class) {
                if (networkHandler == null) {
                    networkHandler = new NetworkHandler(context);
                    networkHandler.start();
                }
            }
        }
        return networkHandler;
    }

    public static File getFile(Context context) {
        return new File(context.getCacheDir(), FILE);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        handler = new Handler(getLooper());
    }

    public void fetch() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onFetch();
            }
        });
    }

    private void onFetch() {
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
            eventHandler.notifyDataUpdated();
        } catch (Exception e) {
            Log.e(TAG, "failed to fetch data: " + e.getMessage());
            eventHandler.notifyError(e);
        } finally {
            try {
                output.close();
            } catch (Exception e) {
            }
        }
    }
}
