package com.gmail.gpolomicz.bggxml;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadXML extends AsyncTask<String, Void, String> {
    private static final String TAG = "GPDEB";

    private OnDataAvailable callback;
    private String baseURL;
    private Integer id;

    interface OnDataAvailable {
        void onDataAvailable (String result);
        void onDataAvailable (String result, Integer id);
    }

    DownloadXML(OnDataAvailable callback, String baseURL) {
        this.callback = callback;
        this.baseURL = baseURL;
    }

    DownloadXML(OnDataAvailable callback, String baseURL, Integer id) {
        this.callback = callback;
        this.baseURL = baseURL;
        this.id = id;
    }

    @Override
    protected String doInBackground(String... strings) {
        String rssFeed = downloadXML(baseURL+strings[0]);

        if (rssFeed == null) {
            Log.e(TAG, "doInBackground: Error downloading");
        }
        return rssFeed;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(callback != null) {
            if(id == null) {
                callback.onDataAvailable(s);
            } else {
                callback.onDataAvailable(s, id);
            }
        }
    }

    private String downloadXML(String urlPath) {

        StringBuilder xmlResult = new StringBuilder();
        try {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                xmlResult.append(line);
                xmlResult.append("\n");
            }
            reader.close();

            return xmlResult.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "downloadXML: IO Exeption reading data " + e.getMessage());
        }
        return null;
    }
}
