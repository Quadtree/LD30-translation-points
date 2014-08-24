package com.ironalloygames.ld30.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ironalloygames.ld30.EnemyMiniShip;
import com.ironalloygames.ld30.HunterKiller;
import com.ironalloygames.ld30.HyperspaceWall;

public class Hyperspace extends World {

	public Hyperspace() {
		EnemyMiniShip e = new EnemyMiniShip();
		e.setPosition(new Vector2(-50, 100));
		addActor(e);

		for (int i = 0; i < 50; i++) {
			HyperspaceWall g = new HyperspaceWall();
			g.setPosition(new Vector2(MathUtils.random(-RADIUS, RADIUS), MathUtils.random(-RADIUS, RADIUS)));
			addActor(g);
		}
		for (int i = 0; i < 3; i++) {
			HunterKiller g = new HunterKiller();
			g.setPosition(new Vector2(MathUtils.random(-RADIUS, RADIUS), MathUtils.random(-RADIUS, RADIUS)));
			addActor(g);
		}
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
