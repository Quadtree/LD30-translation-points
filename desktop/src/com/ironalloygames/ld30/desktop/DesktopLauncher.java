package com.ironalloygames.ld30.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ironalloygames.ld30.LD30;

public class DesktopLauncher {
	public static void main(String[] arg) {

		// TexturePacker.process("../rawassets", "../core/assets", "pack");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 768;
		new LwjglApplication(new LD30(), config);
	}
}
