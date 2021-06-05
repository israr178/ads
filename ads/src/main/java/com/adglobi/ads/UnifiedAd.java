package com.adglobi.ads;

import android.graphics.Bitmap;

public class UnifiedAd {
    AdModel adInfo;
    Bitmap adImage;

    public UnifiedAd(AdModel adInfo, Bitmap adImage) {
        this.adInfo = adInfo;
        this.adImage = adImage;
    }

    public AdModel getAdInfo() {
        return adInfo;
    }

    public void setAdInfo(AdModel adInfo) {
        this.adInfo = adInfo;
    }

    public Bitmap getAdImage() {
        return adImage;
    }

    public void setAdImage(Bitmap adImage) {
        this.adImage = adImage;
    }
}
