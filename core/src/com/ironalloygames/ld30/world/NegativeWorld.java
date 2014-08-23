package com.ironalloygames.ld30.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.ironalloygames.ld30.Actor;

public class NegativeWorld extends World {

	@Override
	public Color getColor() {
		return new Color(0, 0.2f, 0.2f, 1);
	}

	@Override
	public void renderBackground() {
		super.renderBackground();

		Gdx.gl.glClearColor(0, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	protected void update() {
		super.update();

		for (Actor a : actors) {
			a.takeDamage(1 / 30f / 60f);
		}
	}

}
