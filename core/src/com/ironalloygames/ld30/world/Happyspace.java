package com.ironalloygames.ld30.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ironalloygames.ld30.Actor;
import com.ironalloygames.ld30.HappyFlower;
import com.ironalloygames.ld30.HappyOrb;

public class Happyspace extends World {

	public boolean aggroed = false;

	public Happyspace() {
		for (int i = 0; i < 20; i++) {
			HappyFlower g = new HappyFlower();
			g.setPosition(new Vector2(MathUtils.random(-RADIUS, RADIUS), MathUtils.random(-RADIUS, RADIUS)));
			addActor(g);
		}
		for (int i = 0; i < 10; i++) {
			HappyOrb g = new HappyOrb();
			g.setPosition(new Vector2(MathUtils.random(-RADIUS, RADIUS), MathUtils.random(-RADIUS, RADIUS)));
			addActor(g);
		}
	}

	@Override
	public Color getColor() {
		return new Color(0, 1, 0, 1);
	}

	@Override
	public void renderBackground() {
		super.renderBackground();

		Gdx.gl.glClearColor(0, .45f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	protected void transferred(Actor a) {
		super.transferred(a);

		if (a instanceof HappyOrb || a instanceof HappyFlower)
			aggroed = true;
	}

}
