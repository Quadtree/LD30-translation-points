package com.ironalloygames.ld30.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.ironalloygames.ld30.EnemyMiniShip;

public class Hyperspace extends World {

	public Hyperspace() {
		EnemyMiniShip e = new EnemyMiniShip();
		e.setPosition(new Vector2(-50, 100));
		addActor(e);
	}

	@Override
	public Color getColor() {
		return new Color(0.8f, 0, 1, 1);
	}

	@Override
	public void renderBackground() {
		super.renderBackground();

		Gdx.gl.glClearColor(0.25f, 0, 0.25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

}
