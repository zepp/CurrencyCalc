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
import java.util.List;

public abstract class Converter {

    private final boolean reloadCache = true;
    private final String TAG = Converter.class.getCanonicalName();

    private Context context;
    private String url;
    private File file;

    private CurrencyList curList;

    private class URLDownloader extends AsyncTask<String, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(String ...urls)
        {
            try
            {
                URL url = new URL(urls[0]);
                byte data[] = new byte[1024];
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
            catch (Exception e)
            {
                Log.e(TAG, "failed to download XML " + e.getMessage());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            if (result || file.exists()) {
                load();
                onUpdated(true);
            }
            onUpdated(result);
        }
    }

    private void load()
    {
        try {
            Serializer serializer = new Persister(new CustomMatcher());
            curList = serializer.read(CurrencyList.class, file);
        } catch (Exception e) {
            Log.e(TAG, "failed to load currency exchange rates from XML file:" + e.getMessage());
        }
        for(String code : curList.getCharCodes())
        {
            Log.d(TAG, code + " - " + curList.getRubbles(code));
        }
    }

    public abstract void onUpdated(boolean result);

    public final void update()
    {
        if (file.exists() && !reloadCache) {
            load();
            onUpdated(true);
        } else {
            new URLDownloader().execute(url);
        }
    }

    public final List<String> charCodes()
    {
        List<String> codes = curList.getCharCodes();
        java.util.Collections.sort(codes);
        return codes;
    }

    public final double process(String from, String to, double amount)
    {
        return amount * (curList.getRubbles(from)/curList.getRubbles(to));
    }

    Converter (Context context, String fileName, String url)
    {
        this.context = context;
        this.url = url;
        this.file = new File(context.getCacheDir(), fileName);
    }
}
