package com.ivnygema.damas;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.ivnygema.damas.models.AdsController;

public class AndroidLauncher extends AndroidApplication implements AdsController {

	private InterstitialAd interstitialAd;

	public static AdView adView;

	RelativeLayout layout;

	private final int SHOW_ADS = 1;
	private  final int HIDE_ADS = 0;

	Handler handler;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock = true;
		initialize(new Aplication(), config);

		interstitialAd = new InterstitialAd(this);
		//interstitialAd.setAdUnitId("ca-app-pub-1626440771794937/8079042015");
		interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); // test


		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				loadInterstitialAd();
				super.onAdClosed();
				//GameScreen.restart();
			}

			@Override
			public void onAdFailedToLoad(LoadAdError loadAdError) {
				super.onAdFailedToLoad(loadAdError);
				//GameScreen.restart();
			}
		});

		loadInterstitialAd();

		View gameView = initializeForView(new Aplication(this), config);


		adView = new AdView(this);
		adView.setVisibility(View.VISIBLE);
		//adView.setBackgroundColor(0xff000000);
		//adView.setAdSize(AdSize.BANNER);
		//adView.setAdSize(AdSize.FLUID);
		adView.setAdSize(AdSize.SMART_BANNER);
		//adView.setAdSize(AdSize.FULL_BANNER);


		adView.setAdUnitId("ca-app-pub-1626440771794937/8079042015");
		//adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111"); // test

		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {
			}
		});

		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

		//adView.setVisibility(View.GONE);

		layout = new RelativeLayout(this);
		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layout.addView(adView, params);

		setContentView(layout);

		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what){
					case SHOW_ADS:
						adView.setVisibility(View.VISIBLE);
						break;
					case (HIDE_ADS):
						adView.setVisibility(View.GONE);
						break;
				}
			}
		};
	}

	@Override
	public void loadInterstitialAd() {
		interstitialAd.loadAd(new AdRequest.Builder().build());
	}

	@Override
	public void showInterstitialAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(interstitialAd.isLoaded()) {
					interstitialAd.show();
				}
				else loadInterstitialAd();
			}
		});
	}


	@Override
	public void showAds(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}

	@Override
	public int getBannerHeight() {
		return adView.getHeight();
	}


}
