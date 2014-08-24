package com.ironalloygames.ld30.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ironalloygames.ld30.Shark;

public class WaterWorld extends World {

	public WaterWorld() {
		for (int i = 0; i < 20; i++) {
			Shark g = new Shark();
			g.setPosition(new Vector2(MathUtils.random(-RADIUS, RADIUS), MathUtils.random(-RADIUS, RADIUS)));
			addActor(g);
		}
	}

	@Override
	public Color getColor() {
		return new Color(0, 0, 1, 1);
	}

	@Override
	public float getDragCoeff() {
		return 0.9f;
	}

	@Override
	public void renderBackground() {
		super.renderBackground();

		Gdx.gl.glClearColor(0, 0, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

}
