package com.adglobi.ads;

public interface AdLoadingListener {

    public void onAdLoaded(UnifiedAd adGlobiUnifiedAd);
    public void onAddLoadingFailed(String errorReason);
}
