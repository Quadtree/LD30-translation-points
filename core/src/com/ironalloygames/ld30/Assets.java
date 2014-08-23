package com.ironalloygames.ld30;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
	AssetManager mgr;

	TextureAtlas pack;

	HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

	public Assets() {
		mgr = new AssetManager();
	}

	public Sprite getSprite(String name) {
		if (pack == null) {
			mgr.load("pack.atlas", TextureAtlas.class);
			mgr.finishLoading();
			pack = mgr.get("pack.atlas");
		}

		if (!sprites.containsKey(name)) {
			sprites.put(name, pack.createSprite(name));
		}

		return sprites.get(name);
	}
}
