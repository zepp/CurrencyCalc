package com.example.pavl.currencycalc;

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

public final class Fetcher {

    private final String TAG = Fetcher.class.getCanonicalName();
    private final static int bufferSize = 1024;
    private final boolean reloadCache = true; // for debug purposes

    private String url;
    private File file;
    private OnUpdateListener listener;

    private class URLDownloader extends AsyncTask<String, Void, List<Currency>>
    {
        @Override
        protected List<Currency> doInBackground(String ... urls)
        {
            try
            {
                URL url = new URL(urls[0]);
                byte data[] = new byte[bufferSize];
                int count;

                Log.d(TAG, "downloading XML " + url.toString());

                URLConnection connection = url.openConnection();
                connection.connect();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                OutputStream output = new FileOutputStream(file);


                while ((count = input.read(data)) != -1)
                {
                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
            }
            catch (Exception e) {
                Log.e(TAG, "failed to download XML " + e.getMessage());
            }
            if (file.exists())
                return load();
            else
                return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<Currency> result)
        {
            if (listener != null)
                listener.onUpdated(result);
        }
    }

    private List<Currency> load()
    {
        CurrencyList curList;

        try {
            Serializer serializer = new Persister(new CustomMatcher());
            curList = serializer.read(CurrencyList.class, file);
        } catch (Exception e) {
            Log.e(TAG, "failed to load currency exchange rates from XML file:" + e.getMessage());
            return new ArrayList<>();
        }
        for(Currency c : curList.getCurrencies())
            Log.d(TAG, c.getCharCode() + " - " + c.getRubbles());
        return curList.getCurrencies();
    }

    public void get()
    {
        if (reloadCache)
            new URLDownloader().execute(url);
        else {
            if (listener != null)
                listener.onUpdated(load());
        }
    }

    public void setListener(OnUpdateListener listener)
    {
        this.listener = listener;
    }

    Fetcher(Context context, String fileName, String url)
    {
        this.url = url;
        this.file = new File(context.getCacheDir(), fileName);
    }

    public interface OnUpdateListener
    {
        void onUpdated (List<Currency> currencyList);
    };
}
