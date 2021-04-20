/*******************************************************************************
 * Copyright (c) 2015 LG Electronics. All Rights Reserved. This software is the 
 * confidential and proprietary information of LG Electronics. You shall not
 * disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with LG Electronics.
 *******************************************************************************/
package br.ufc.great.syssu.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Almada on 02/06/2015.
 */
public class Connection {

    private static final String TAG = "Connection";

    private static String sServer = "";
    private static HttpClient sHttpclient;
    private static HttpPost sHttpPost;

    private Context mContext;

    public Connection(Context context) {
        this.mContext = context;
        sHttpclient = new DefaultHttpClient();
    }

    public void setURL(String url) {
        sServer = url;
    }

    public String getURL() {
        if (!sServer.equals("")) {
            return sServer;
        }

        return "";
    }

    public void send(String local, String msg) {
        System.out.println(sServer + local);
        new HttpAsyncTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, sServer + local, msg);
    }

    public String getResponse(String local, String msg) {
        System.out.println(sServer + local);
        String result = "";

        try {
            result = new HttpAsyncTask().execute(sServer + local, msg).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return post(urls[0], urls[1]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("")) {
                Log.d(TAG, "Data Sent! ");
                Log.d(TAG, "Response: " + result);
            } else {
                Log.d(TAG, "Data not sent! ");
            }
        }
    }

    private static String post(String url, String jsonObject) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 3. set json to StringEntity
            StringEntity se = new StringEntity(jsonObject);

            sHttpPost = new HttpPost(url);
            // 4. set httpPost Entity
            sHttpPost.setEntity(se);

            // 5. Set some headers to inform server about the type of the content
            sHttpPost.setHeader("Accept", "application/json");
            sHttpPost.setHeader("Content-type", "application/json");

            // 6. Execute POST request to the given URL
            HttpResponse httpResponse = sHttpclient.execute(sHttpPost);

            // 7. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent(); //Colocar resposta do servidor

            // 8. convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "";
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        inputStream.close();
        return result;
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(
        		mContext.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(TAG, "Wifi on !!");
            return true;
        }
        else {
            Log.d(TAG, "WiFi off !!");
            return false;
        }
    }
}
