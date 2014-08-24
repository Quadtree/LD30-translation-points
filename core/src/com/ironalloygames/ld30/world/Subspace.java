package com.ironalloygames.ld30.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ironalloygames.ld30.Actor;

public class Subspace extends World {

	Vector2 wind = new Vector2(0, 0);

	public Subspace() {
		randomWind();
	}

	@Override
	public Color getColor() {
		return new Color(0.5f, 0.5f, 0.5f, 1);
	}

	private void randomWind() {
		wind.x = MathUtils.random(-10, 10);
		wind.y = MathUtils.random(-10, 10);
	}

	@Override
	public void renderBackground() {
		super.renderBackground();

		Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	protected void update() {
		super.update();

		if (MathUtils.random(600) == 0) {
			randomWind();
		}

		for (Actor a : actors) {
			a.addWind(wind);
		}
	}

}
