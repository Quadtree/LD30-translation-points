package com.ironalloygames.ld30;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
	AssetManager mgr;

	TextureAtlas pack;

	HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

	public Assets() {
		mgr = new AssetManager();
	}

	public BitmapFont getFont(int pt) {
		String filename = "main" + pt + ".fnt";

		if (!mgr.isLoaded(filename)) {
			mgr.load(filename, BitmapFont.class);
			mgr.finishLoading();
		}

		return mgr.get(filename, BitmapFont.class);
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

	public Texture getTexture(String name) {
		String filename = name + ".png";

		if (!mgr.isLoaded(filename)) {
			mgr.load(filename, Texture.class);
			mgr.finishLoading();
		}

		return mgr.get(filename, Texture.class);
	}
}
