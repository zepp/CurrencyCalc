package com.example.pavl.currencycalc.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Service extends IntentService {
    public final static String DATA_UPDATED = Service.class.getCanonicalName() + ".DATA_UPDATED";
    public final static String SERVICE_ERROR = Service.class.getCanonicalName() + ".ERROR";
    public final static String ERROR_MESSAGE = "error-message";

    private final static String URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private final static String FILE = "rates.xml";
    private final static String TAG = Service.class.getSimpleName();
    private final LocalBroadcastManager manager;
    private final OkHttpClient client;

    public Service() {
        super(Service.class.getCanonicalName());
        this.manager = LocalBroadcastManager.getInstance(this);
        this.client = new OkHttpClient();
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
            manager.sendBroadcast(new Intent(DATA_UPDATED));
        } catch (Exception e) {
            Log.e(TAG, "failed to fetch data: " + e.getMessage());
            Intent result = new Intent(SERVICE_ERROR);
            result.putExtra(ERROR_MESSAGE, e.getMessage());
            manager.sendBroadcast(result);
        } finally {
            try {
                output.close();
            } catch (Exception e) {
            }
        }
    }
}
