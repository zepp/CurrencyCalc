package com.example.pavl.currencycalc.network;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;


public final class NetworkHandler {
    private final static String TAG = NetworkHandler.class.getSimpleName();
    private final static int TIMEOUT = 5000;
    private final static String FETCH_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private final File cacheDir;

    public NetworkHandler(Context context) {
        cacheDir = context.getCacheDir();
    }

    public File fetch() throws IOException {
        URL url = new URL(FETCH_URL);
        HttpURLConnection connection = null;
        File file = new File(cacheDir, UUID.randomUUID().toString() + ".xml");
        char[] buffer = new char[1024];

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new RuntimeException("HTTP error: " + connection.getResponseMessage());
            }
            ContentType content =  ContentType.newContentType(connection.getHeaderField("content-type"));
            Log.d(TAG, content.toString());
            if (!content.isType("application/xml")) {
                throw new RuntimeException("HTTP content unsupported: " + content.type);
            }
            try (OutputStream outputStream = new FileOutputStream(file);
                 InputStreamReader reader = new InputStreamReader(connection.getInputStream(), content.charset);
                 OutputStreamWriter writer = new OutputStreamWriter(outputStream, content.charset)) {
                for (int bytes = reader.read(buffer); bytes > 0; bytes = reader.read(buffer)) {
                    writer.write(buffer, 0, bytes);
                }
                writer.flush();
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return file;
    }

    private static class ContentType{
        final String type;
        final String charset;

        static ContentType newContentType(String value) {
            String[] content = value.split(";", 2);
            String type = content[0].trim();
            String charset = content[1].split("=", 2)[1].trim();
            return new ContentType(type, charset);
        }

        ContentType(String type, String charset) {
            this.type = type;
            this.charset = charset;
        }

        boolean isType(String value) {
            return type.equals(value);
        }

        @Override
        public String toString() {
            return "ContentType{" +
                    "type='" + type + '\'' +
                    ", charset='" + charset + '\'' +
                    '}';
        }
    }
}
