package com.ivnygema.damas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.ivnygema.damas.managers.ResourceManager;
import com.ivnygema.damas.screens.MainScreen;

public class Aplication extends Game {

	@Override
	public void create () {

		// Lanza la carga de recursos
		ResourceManager.loadAllResources();

		((Game) Gdx.app.getApplicationListener()).setScreen(new MainScreen());
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
	}
}
