package com.ironalloygames.ld30.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.ironalloygames.ld30.LD30;

public class HtmlLauncher extends GwtApplication {

	@Override
	public ApplicationListener getApplicationListener() {
		return new LD30();
	}

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(1024, 768);
	}
}