package com.ironalloygames.ld30.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ironalloygames.ld30.Star;

public class FireWorld extends World {

	public FireWorld() {
		for (int i = 0; i < 100; i++) {
			Star s = new Star();

			float dir = MathUtils.random(MathUtils.PI2);
			float range = MathUtils.random(World.RADIUS);

			s.setPosition(new Vector2(MathUtils.cos(dir) * range, MathUtils.sin(dir) * range));
			s.setAngle(MathUtils.random(MathUtils.PI2));

			this.addActor(s);
		}
	}

	@Override
	public Color getColor() {
		return new Color(0.8f, .5f, 0, 1);
	}

	@Override
	public void renderBackground() {
		super.renderBackground();

		Gdx.gl.glClearColor(.4f, .15f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
