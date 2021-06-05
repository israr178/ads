package com.adglobi.ads;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkHandler {
    GetNetworkResult networkResultListener;
    public NetworkHandler(GetNetworkResult networkResultListener) {
        this.networkResultListener = networkResultListener;
    }

    public void doGetRequestForJson(String url, String parameters){
        GetAsyncTask obj = new GetAsyncTask();
        obj.execute(url,parameters);
    }

    public void doPostRequest(String url, String parameters){
        PostAsyncTask obj = new PostAsyncTask();
        obj.execute(url,parameters);
    }
    private class GetAsyncTask{
        public void execute(String url, String parameters){
            onPreExecute();
            doInBackground(url,parameters);
        }
        public void onPreExecute(){

        }
        public void doInBackground(final String toUrl, String parameters){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection httpURLConnection=null;
                    InputStream is=null;
                    BufferedReader br=null;
                    String ToRequestUrl = toUrl;
                    StringBuilder outputResult = new StringBuilder("");
                    try {
                        if(parameters !=null && !parameters.isEmpty()){
                            ToRequestUrl = ToRequestUrl + "?" + parameters;
                        }
                        URL url = new URL(ToRequestUrl);
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        // con.connect();
                        httpURLConnection.setDoInput(true);
                        if (httpURLConnection.getResponseCode() == 200) {
                            is = httpURLConnection.getInputStream();
                            br = new BufferedReader(new InputStreamReader(is));
                            String line = null;
                            while ((line = br.readLine()) != null) {
                                outputResult.append(line);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        if(httpURLConnection!=null) {
                            httpURLConnection.disconnect();
                        }
                        if (is !=null){
                            try {
                                is.close();
                                br.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    onPostExecute(outputResult.toString());

                }
            }).start();
        }
        public void onPostExecute(String result){
            networkResultListener.onRequestCompleted(result);

        }

    }

    private class PostAsyncTask{
        public void execute(String url, String parameters){
            onPreExecute();
            doInBackground(url,parameters);
        }
        public void onPreExecute(){
        }
        public void doInBackground(final String toUrl, String parameters){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpsURLConnection httpURLConnection=null;
                    OutputStream outputStream=null;
                    BufferedWriter bufferedWriter=null;
                    StringBuilder outputResult = new StringBuilder("");
                    try {
                        URL url = new URL(toUrl);
                        httpURLConnection= (HttpsURLConnection) url.openConnection();
                        httpURLConnection.setReadTimeout(20000);
                        httpURLConnection.setConnectTimeout(20000);
                        httpURLConnection.setRequestMethod("POST");

                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setDoOutput(true);

                        //going to upload parameters to Server
                        outputStream = httpURLConnection.getOutputStream();
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                        bufferedWriter.write(parameters);

                        bufferedWriter.flush();
                        bufferedWriter.close();
                        outputStream.close();

                        // now reading response from server
                        InputStream is;
                        if (httpURLConnection.getResponseCode() == 200) {

                            is = httpURLConnection.getInputStream();
                            BufferedReader br = new BufferedReader(new InputStreamReader(is));
                            String line = null;
                            while ((line = br.readLine()) != null) {
                                outputResult.append(line);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        if(httpURLConnection!=null) {
                            httpURLConnection.disconnect();
                        }
                        if (outputStream !=null){
                            try {
                                outputStream.close();
                                bufferedWriter.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    onPostExecute(outputResult.toString());

                }
            }).start();
        }
        public void onPostExecute(String result){
            networkResultListener.onRequestCompleted(result);
        }

    }


}
