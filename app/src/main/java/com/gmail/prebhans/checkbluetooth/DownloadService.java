package com.gmail.prebhans.checkbluetooth;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends IntentService {

    private static final String TAG = "DownloadService";

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    //private static final String TAG = "DownloadService";

    public DownloadService() {
        super(DownloadService.class.getName());
        Log.i(TAG, "DownloadService Started!");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "onHandleIntent Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String url = intent.getStringExtra("url");

        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {
                String[] results = downloadData(url);

				/* Sending result back to activity */
                if (null != results && results.length > 0) {
                    bundle.putStringArray("result", results);
                    receiver.send(STATUS_FINISHED, bundle);
                }
            } catch (Exception e) {

				/* Sending error message back to activity */
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        }
        Log.i(TAG, "onHandleIntent Stopping!");
        this.stopSelf();
    }

    private String[] downloadData(String requestUrl) throws IOException,
            DownloadException {
        Log.i(TAG, "downloadData Started!");
        InputStream inputStream = null;

        HttpURLConnection urlConnection = null;

		/* forming th java.net.URL object */
        URL url = new URL(requestUrl);

        urlConnection = (HttpURLConnection) url.openConnection();

		/* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");

		/* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");

		/* for Get request */
        urlConnection.setRequestMethod("GET");

        int statusCode = urlConnection.getResponseCode();

		/* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(
                    urlConnection.getInputStream());

            String response = convertInputStreamToString(inputStream);

            String[] results = parseResultJsonNew(response);

            return results;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }

    private String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        Log.i(TAG, "convertInputStreamToString Started!");

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

		/* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    private String[] parseResultJsonOld(String result) {


        String[] blogTitles = null;
        try {
            JSONObject response = new JSONObject(result);

            JSONArray posts = response.optJSONArray("posts");

            blogTitles = new String[posts.length()];

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");

                blogTitles[i] = title;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return blogTitles;
    }

    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super("Failed to fetch data!!");
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private String[] parseResultJsonNew(String result) {
        Log.i(TAG, "parseResultJsonNew Started!");
        int firstCharIndex;
        int lastCharIndex;
        String localResult = result;
        String startHex = "";
        String company = "";
        //String[] returnResult;
        // check result for first [ and ]
        localResult.trim();
        if (localResult.indexOf("[") != -1) {
            firstCharIndex = localResult.indexOf("[");
            if (localResult.indexOf("]") != -1) {
                lastCharIndex = localResult.indexOf("]");
                localResult = localResult.substring(firstCharIndex, lastCharIndex + 1);
            }
        } else {
            localResult = "NoNameFound";

        }

        Log.i(TAG, "parseResultJsonNew A" + " " + localResult);

        if (!localResult.equals("NoNameFound")) {
            // #TODO strip JSON localReturnResultFromSendUrl, find company name
            try {
                JSONArray jsonArray = new JSONArray(localResult);
                JSONObject jObj;
                jObj = jsonArray.getJSONObject(0);
                startHex = jObj.getString("startHex");
                company = jObj.getString("company");
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }

        String[] returnResult = {"StartHex", startHex, "Company", company};

        //Log.i(TAG, "parseResultJsonNew B" + " " + returnResult);

        return returnResult;
    }

}