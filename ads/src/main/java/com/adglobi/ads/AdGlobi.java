package com.adglobi.ads;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;

public class AdGlobi implements GetNetworkResult,GetImageResult{

    public  final long INTERVAL=15000;
    public static Timer mTimer=null; // timer handling
    static private WeakReference<Activity> mContext;
    static AdLoadingListener mAdLoadingListener;
    static Credentials mCredentials;
    static ArrayList<AdModel> ads;
    static Context applicationContext;
    static Integer currentAdIndex = 0;
    static ImageLoader imageLoader;
    static AdValidator adValidator = null;

    public void initialize(Activity containingActivity, Credentials credentials){
        //we will store weak reference to activity , to avoid memory leak
        mContext = new WeakReference<Activity>(containingActivity);
        ads = new ArrayList<>();
        //credentials are necessary to get ads
        mCredentials = credentials;

        // store application context for some general usage
        applicationContext = mContext.get().getApplicationContext();
        if(adValidator == null)
            adValidator = new AdValidator();
        if(adValidator.getApiCountryCode().isEmpty() || adValidator.isLocationUpdateRequired(applicationContext)){
            adValidator.getUserLocationInfoFromApiLevel2();
        }
//        if(adValidator.getGpsCity() == ""){
//            getLocationViaGps();
//        }

        String countryCode = AdValidator.getDeviceCountryLevel1(applicationContext);

        Log.d("israr","isp: "+adValidator.getSimNetworkISP() +" "+adValidator.getApiISP() +" "+ adValidator.getGpsCity());

        //start a timer to load next ad after 1 minute
        if(mTimer == null) {
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new AdScheduler(), INTERVAL, INTERVAL);// schedule task
        }else{
            mTimer.cancel();
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new AdScheduler(), INTERVAL, INTERVAL);// schedule task
        }
    }
    public static Context getApplicationContext(){
        return applicationContext;
    }

    public static void getLocationViaGps(){
        //third level 3, using gps
        if(!AdValidator.checkLocationPermission(mContext.get())){
            //no permission, request location permission
            adValidator.requestLocationPermission(mContext.get());
        }else{
            adValidator.getLocationLevel3(mContext.get());
        }
    }
    public void loadNativeAd(AdLoadingListener adLoadingListener){
        mAdLoadingListener = adLoadingListener;
        NetworkHandler networkHandler = new NetworkHandler(this);
        HashMap<String,String> urlParameters = new HashMap<>();
        urlParameters.put("key",Credentials.getKey());
        urlParameters.put("aid",Credentials.getAid());
        urlParameters.put("mid",Credentials.getMid());
        urlParameters.put("authorized","1");
        String parameters = Utilities.getParamsOfRequest(urlParameters);
        networkHandler.doGetRequestForJson(Constants.OFFERS_URL,parameters);
    }

    @Override
    public void onRequestCompleted(String result) {
        Utilities.parseJson(result,ads);
        Log.d("israr",result);
        Log.d("israr","size: "+ads.size());
        if(adValidator.getApiCountryCode().isEmpty()){
            try {
                Thread.sleep(350);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(ads.size() > 0){
            Iterator<AdModel> iter = ads.iterator();
            while(iter.hasNext()) {
                AdModel ad = iter.next();
                //ad.setCountry_allow("pk");
                if(adValidator.isAdAllowed(ad)){
                    Log.d("israr","not removing ad :"+ ad.getOs_allow() + " "+ AdValidator.getOSType());
                }else{
                    Log.d("israr","removing ad :"+ ad.getOs_allow() + " "+ adValidator.getOSType());
                    iter.remove();
                }
            }
        }
        if(ads.size() >0){
            imageLoader = new ImageLoader(this);
            imageLoader.getBitmap(ads.get(currentAdIndex).getCreatives().get("300x250"));
        }else{
            Log.d("israr","timer cancelled");
            mTimer.cancel();
            mContext.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdLoadingListener.onAddLoadingFailed("No Ad Found");
                }
            });
        }
    }
    @Override
    public void onImageLoaded(Bitmap bitmap) {
        Log.d("israr","next1 ad called"+ currentAdIndex + ads.size());
        if(ads.size()>0){
        }else{
            Log.d("israr","timer cancelled");
            mTimer.cancel();
        }

        UnifiedAd adGlobiUnfiedAd = new UnifiedAd(ads.get(currentAdIndex),bitmap);
        synchronized(currentAdIndex) {
            currentAdIndex = currentAdIndex+1;
            if(currentAdIndex == ads.size()){
                currentAdIndex = 0;
            }
        }

        mContext.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mContext !=null) {
                    mAdLoadingListener.onAdLoaded(adGlobiUnfiedAd);
                }else{
                    Log.d("israr","timer cancelled");
                    mTimer.cancel();
                }
            }
        });
    }
    public void loadNextScheduledAd(){
        if(ads ==null){
            return;
        }
        if(mContext.get() == null || mContext.get().isDestroyed() || mContext.get().isFinishing()){
            Log.d("israr","scheduler cancelled");
            mTimer.cancel();
            return;
        }

        Log.d("israr","next ad called"+ currentAdIndex + ads.size());
//        if(currentAdIndex == ads.size()){
//            synchronized(currentAdIndex) {
//                currentAdIndex = 0;
//            }
//        }
        if(currentAdIndex < ads.size() ) {
            imageLoader.getBitmap(ads.get(currentAdIndex).getCreatives().get("300x250"));
        }else{
            //mTimer.cancel();
            if(mContext.get() == null || mContext.get().isDestroyed() || mContext.get().isFinishing()){
                Log.d("israr","scheduler cancelled");
                mTimer.cancel();
            }
        }
    }

    public void release() {
        Log.d("israr","scheduler stoped");
        mTimer.cancel();
    }
}
