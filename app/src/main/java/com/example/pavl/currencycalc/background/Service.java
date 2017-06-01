package com.example.pavl.currencycalc.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class Service extends IntentService {
    public final static String DATA_UPDATED = Service.class.getCanonicalName() + ".DATA_UPDATED";
    public final static String FILE = "rates.xml";
    public final static String URL = "http://www.cbr.ru/scripts/XML_daily.asp";

    private final static String TAG = Service.class.getSimpleName();
    private final static int bufferSize = 1024; // intermediate buffer size
    private final LocalBroadcastManager manager;

    public Service() {
        super(Service.class.getCanonicalName());
        this.manager = LocalBroadcastManager.getInstance(this);
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
        InputStream input = null;
        OutputStream output = null;
        byte data[] = new byte[bufferSize];
        int count;
        File tempFile = new File(getCacheDir(), FILE + ".new");
        File file = new File(getCacheDir(), FILE);

        try {
            Log.d(TAG, "downloading " + URL);
            URL url = new URL(URL);

            URLConnection connection = url.openConnection();
            connection.connect();

            input = new BufferedInputStream(url.openStream(), bufferSize);
            output = new FileOutputStream(tempFile);

            while ((count = input.read(data)) != -1) {
                // writing data to FILE
                output.write(data, 0, count);
            }

            tempFile.renameTo(file);
        } catch (Exception e) {
            Log.e(TAG, "failed to fetch XML: " + e.getMessage());
            return;
        } finally {
            try {
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
            } catch (Exception e) {
            }
        }
        manager.sendBroadcast(new Intent(DATA_UPDATED));
    }
}
