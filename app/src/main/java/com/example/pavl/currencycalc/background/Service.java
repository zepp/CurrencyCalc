package com.example.pavl.currencycalc.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Service extends IntentService {
    private final static String URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private final static String FILE = "rates.xml";
    private final static String TAG = Service.class.getSimpleName();
    private final OkHttpClient client;
    private final EventHandler handler;

    public Service() {
        super(Service.class.getCanonicalName());
        this.client = new OkHttpClient();
        this.handler = EventHandler.getInstance();
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, Service.class);
        return intent;
    }

    public static File getFile(Context context) {
        return new File(context.getCacheDir(), FILE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Request request = new Request.Builder().url(URL).build();
        OutputStream output = null;
        File file = new File(getCacheDir(), FILE + ".new");

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
            file.renameTo(getFile(getBaseContext()));
            handler.notifyDataUpdated();
        } catch (Exception e) {
            Log.e(TAG, "failed to fetch data: " + e.getMessage());
            handler.notifyError(e);
        } finally {
            try {
                output.close();
            } catch (Exception e) {
            }
        }
    }
}
