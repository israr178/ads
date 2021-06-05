package com.adglobi.ads;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.TimerTask;

public class AdScheduler extends TimerTask{
    private  Handler mHandler;
    AdGlobi adGlobi = new AdGlobi();

        @Override
        public void run() {
            Log.d("israr","scheduler ad called");
            mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                   adGlobi.loadNextScheduledAd();
                }
            });
        }
}
