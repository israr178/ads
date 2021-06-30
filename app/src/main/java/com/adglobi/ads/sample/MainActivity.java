package com.adglobi.ads.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;


import com.adglobi.ads.AdGlobi;
import com.adglobi.ads.AdLoadingListener;
import com.adglobi.ads.Credentials;
import com.adglobi.ads.UnifiedAd;
import com.adglobi.templates.AdInteractionListener;
import com.adglobi.templates.AdTemplateView;

public class MainActivity extends AppCompatActivity implements AdLoadingListener, AdInteractionListener {

    RelativeLayout rl;
    AdTemplateView nativeTemplateView;
    AdGlobi adGlobi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AdTemplateView adView = (AdTemplateView)this.getLayoutInflater().inflate(R.layout.ad_template_view,null);

        rl = findViewById(R.id.main);
        nativeTemplateView = new AdTemplateView(this,null);
        nativeTemplateView.setAdInteractionListener(this);
        rl.addView(nativeTemplateView);
        nativeTemplateView.setVisibility(View.GONE);
        adGlobi = new AdGlobi();
        adGlobi.initialize(this,new Credentials("27b23a0e6ed42629b269b8dc53866604","146579","5132"));
        adGlobi.loadNativeAd(this);
    }

    @Override
    public void onAdLoaded(UnifiedAd adGlobiUnifiedAd) {
        Log.d("israr","ad is loaded");
        nativeTemplateView.setVisibility(View.VISIBLE);
        nativeTemplateView.setNativeAd(adGlobiUnifiedAd);
    }

    @Override
    public void onAddLoadingFailed(String errorReason) {
        Log.d("israr",errorReason);
        nativeTemplateView.setVisibility(View.GONE);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 99) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("israr", "permission granted");
                adGlobi.getLocationViaGps();
            } else {
                Log.d("israr", "permission denied");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //adGlobi.release();
    }

    @Override
    public void onAdCloseClicked() {
        adGlobi.loadNextScheduledAd();
    }
}