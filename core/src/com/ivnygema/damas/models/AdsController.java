package com.ivnygema.damas.models;

public interface AdsController {
    public void loadInterstitialAd();
    public void showInterstitialAd();
    public void showAds(boolean show);
    public int getBannerHeight();
}
