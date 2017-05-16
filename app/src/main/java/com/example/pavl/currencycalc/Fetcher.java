package com.example.pavl.currencycalc;

import com.example.pavl.currencycalc.model.Currency;
import com.example.pavl.currencycalc.model.CurrencyList;
import com.example.pavl.currencycalc.model.CustomMatcher;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public final class Fetcher extends AsyncTask<String, Void, Exception> {

    private final static String TAG = Fetcher.class.getCanonicalName();
    private final static int bufferSize = 1024; // intermediate buffer size
    private final static boolean reloadCache = true; // for debug purposes

    private String url;
    private File file;
    private File tempFile;
    private List<Listener> listeners = new ArrayList<>();
    private volatile CurrencyList currencyList;

    Fetcher(Context context, String fileName, String url) {
        this.url = url;
        this.file = new File(context.getCacheDir(), fileName);
        this.tempFile = new File(context.getCacheDir(), "temp.xml");
    }

    public void registerListener(Listener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(Listener listener) {
        listeners.remove(listener);
    }

    public void fetch(){
        if (reloadCache) {
            if (getStatus() == Status.PENDING) {
                execute(url);
            }
        } else {
            try {
                currencyList = load();
            }
            catch (Exception e) {
                if (e == null) {
                    notify(currencyList);
                }else {
                    notify(e);
                }
            }
        }
    }

    public CurrencyList getCurrencyList() {
        return currencyList;
    }

    private CurrencyList load() throws Exception {
        CurrencyList list = new CurrencyList();

        if (file.exists()) {
            Serializer serializer = new Persister(new CustomMatcher());
            list = serializer.read(CurrencyList.class, file);
        }
        for (Currency c : list.getCurrencies()) {
            Log.d(TAG, c.getCharCode() + " - " + c.getRubbles());
        }

        return list;
    }

    private void notify (CurrencyList list) {
        for (Listener listener: listeners) {
            listener.onDataFetched(list);
        }
    }

    private void notify (Exception e) {
        for (Listener listener: listeners) {
            listener.onFetchError(e);
        }
    }

    @Override
    protected Exception doInBackground(String... urls) {
        InputStream input = null;
        OutputStream output = null;

        try {
            URL url = new URL(urls[0]);
            byte data[] = new byte[bufferSize];
            int count;

            Log.d(TAG, "downloading XML " + url.toString());

            URLConnection connection = url.openConnection();
            connection.connect();

            input = new BufferedInputStream(url.openStream(), bufferSize);
            output = new FileOutputStream(tempFile);

            while ((count = input.read(data)) != -1) {
                // writing data to file
                output.write(data, 0, count);
            }

            tempFile.renameTo(file);

            currencyList = load();
        } catch (Exception e) {
            Log.e(TAG, "failed to fetch XML: " + e.getMessage());
            return e;
        } finally {
            // with-resources does it better
            try {
                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
            } catch (Exception e) {}
        }

        return null;
    }

    @Override
    protected void onPostExecute(Exception e) {
        if (e == null) {
            notify(currencyList);
        }else {
            notify(e);
        }
    }

    public interface Listener {
        void onDataFetched(CurrencyList list);
        void onFetchError(Exception e);
    }
}
