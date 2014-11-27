package com.example.wanderxx.spotifytest;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Wanderxx on 11/23/14.
 */
public final class LongOperation extends AsyncTask<String, Void, String> {

    private Activity activity;

    LongOperation(Activity act){
        this.activity=act;
    }

    @Override
    protected String doInBackground(String... params) {
        String res="";
        try {
            res=ShowMeta(params[0]);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("Exception", e.toString());
            e.printStackTrace();
        }
        return res;
    }
    @Override
    protected void onPostExecute(String result) {
        TextView txt=(TextView)activity.findViewById(R.id.txtMeta);
        txt.setText(result);
    }
    @Override
    protected void onPreExecute() {
    }
    @Override
    protected void onProgressUpdate(Void... values) {
    }

    private String ShowMeta(String uri){
        String res="";
        String request="http://ws.spotify.com/lookup/1/.json?uri=" +uri;
        Log.d("uri",request);
        StringBuilder sb = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(request);
        try {

            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                inputStream.close();
            } else {
                System.out.println("Failed to download file");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        String str=sb.toString(); // Json string
        // System.out.println(str);
        try {
            JSONObject obj = new JSONObject(str);
            obj=obj.getJSONObject("track");
            String album=obj.getJSONObject("album").getString("name");
            Log.d("Album Name: ",album);
            String name=obj.getString("name");
            Log.d("song Name: ",name);
            String artist=obj.getJSONArray("artists").getJSONObject(0).getString("name");
            Log.d("art Name: ",artist);
            res=" Song: "+ name +"\n Album: "+album + "\n Singer: " + artist;

        }catch(Exception ex){
            Log.e("", ex.toString());
        }
        return res;
    }
}
