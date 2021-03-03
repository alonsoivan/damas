package com.ivnygema.damas;

import com.badlogic.gdx.Game;
import com.ivnygema.damas.models.AdsController;
import com.ivnygema.damas.screens.SplashScreen;

public class Aplication extends Game {

	public static AdsController adService;

	@Override
	public void create () {
		this.setScreen(new SplashScreen(this));
		//this.setScreen(new WinScreen("tu"));;
	}

	public Aplication(AdsController adService){
		this.adService = adService;}

	public Aplication(){};

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
	}
}
