package com.adglobi.ads;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class Utilities {
    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }
        return isConnected;
    }
    public static String getParamsOfRequest(HashMap<String, String> params){
        Uri.Builder builder = new Uri.Builder();
        for(Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        String encodedParameter = builder.build().getEncodedQuery();
        return encodedParameter;
    }
    public static String getCurrentTimeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date()); //-prints-> 2015-01-22 03:23:26Z
    }
    public static String getCurrentTime() {
        //date output format
        SimpleDateFormat sdf  = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }
    public static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }


    public static void parseJson(String jsonString, ArrayList<AdModel> list){
        try {
            JSONObject jsonObject2,jsonObject3;
            JSONObject jsonObject = new JSONObject(jsonString);
            String response = jsonObject.getString("response");
            if(response.equals("200")){
                jsonObject2 = jsonObject.getJSONObject("data");
                Iterator<String> iterator = jsonObject2.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    Log.d("israr","key: "+key);
                    jsonObject3 = jsonObject2.getJSONObject(key);
                    parseSingleOffer(jsonObject3,list);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void parseSingleOffer(JSONObject offerObject,ArrayList<AdModel> list){
        AdModel ad = new AdModel();
        try {
            ad.setOfferid(offerObject.getString("offerid"));

            ad.setOfferid(offerObject.getString("offerid"));
            ad.setName(offerObject.getString("name"));
            ad.setLogo(offerObject.getString("logo"));
            ad.setStatus(offerObject.getString("status"));
            if(ad.getStatus().equalsIgnoreCase("expired") || ad.getStatus().equalsIgnoreCase("finished")){
                return;
            }

            ad.setCategory(offerObject.getString("category"));
            ad.setCurrency(offerObject.getString("currency"));
            ad.setPrice(offerObject.getString("price"));
            ad.setModel(offerObject.getString("model"));
            ad.setDate_start(offerObject.getString("date_start"));
            ad.setDate_end(offerObject.getString("date_end"));
            ad.setPreview_url(offerObject.getString("preview_url"));
            ad.setOffer_terms(offerObject.getString("offer_terms"));
            ad.setOffer_kpi(offerObject.getString("offer_kpi"));
            ad.setCountry_allow(offerObject.getString("country_allow"));
            ad.setCountry_block(offerObject.getString("country_block"));
            ad.setCity_allow(offerObject.getString("city_allow"));
            ad.setCity_block(offerObject.getString("city_block"));
            ad.setOs_allow(offerObject.getString("os_allow"));
            ad.setOs_block(offerObject.getString("os_block"));
            ad.setDevice_allow(offerObject.getString("device_allow"));
            ad.setDevice_block(offerObject.getString("device_block"));
            ad.setIsp_allow(offerObject.getString("isp_allow"));
            ad.setIsp_block(offerObject.getString("isp_block"));
            ad.setCapping_budget_period(offerObject.getString("capping_budget_period"));
            ad.setCapping_budget(offerObject.getString("capping_budget"));
            ad.setCapping_conversion_period(offerObject.getString("capping_conversion_period"));
            ad.setCapping_conversion(offerObject.getString("capping_conversion"));

            if(ad.getCapping_conversion().equalsIgnoreCase("0") && ad.getCapping_budget().equalsIgnoreCase("0")){
                return;
            }

            ad.setClick_url(offerObject.getString("click_url"));
            ad.setImpression_url(offerObject.getString("impression_url"));
            ad.setAuthorized(offerObject.getString("authorized"));
            if(offerObject.has("creatives") && !offerObject.isNull("creatives") && !offerObject.get("creatives").toString().isEmpty()){
                JSONObject creatives = offerObject.getJSONObject("creatives");
                if(creatives != null){
                    Iterator<String> iterator = creatives.keys();
                    Hashtable<String,String> creativesHash = new Hashtable<>();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        JSONArray creativesImages = creatives.getJSONArray(key);
                        creativesHash.put(key,creativesImages.getString(0));
                    }
                    ad.setCreatives(creativesHash);
                }
                list.add(ad);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void saveDataSharedPreference(Context context, String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getDataSharedPreference(Context context,String key){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPreferences.getString(key, "");
        return value;
    }
}
