package com.mygdx.game.desktop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.UI;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "VocabularyTrainer";
		config.width = 1920;
		config.height = 1000;
		config.resizable = false;
		config.forceExit = false;
		ApplicationAdapter ui = new UI();
		new LwjglApplication(ui, config);
	}
}