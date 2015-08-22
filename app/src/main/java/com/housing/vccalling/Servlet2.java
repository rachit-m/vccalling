package com.housing.vccalling;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 22-08-2015.
 */
public class Servlet2 extends AsyncTask<Pair<Context, String>, Void, String>{

private Context context;

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        context = params[0].first;
        String name = params[0].second;

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://vccalling.appspot.com/hello"); // 10.0.2.2 is localhost's IP address in Android emulator

        try {
            // Add name data to request
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("name", name));
           // nameValuePairs.add(new BasicNameValuePair("api_key", "236c7aef0b5c57a7fd27fcbb29a84335a718c574"));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                Log.d("Backend", "Success");
                return EntityUtils.toString(response.getEntity());
            }
            Log.d("Backend","Error");
            return "Error: " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase();

        } catch (ClientProtocolException e) {
            Log.d("Backend","Not functioning");
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}