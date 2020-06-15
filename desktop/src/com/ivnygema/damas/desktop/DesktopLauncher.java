package com.ivnygema.damas.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ivnygema.damas.Aplication;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width = 540;
		//config.height = 960;

		config.width = 512;
		config.height = 896;

		new LwjglApplication(new Aplication(), config);
	}
}
