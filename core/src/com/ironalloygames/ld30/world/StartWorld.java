package com.ironalloygames.ld30.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ironalloygames.ld30.Asteroid;
import com.ironalloygames.ld30.CapitalTranslationPoint;
import com.ironalloygames.ld30.Gem;
import com.ironalloygames.ld30.LD30;
import com.ironalloygames.ld30.Mothership;
import com.ironalloygames.ld30.PlayerMiniShip;

public class StartWorld extends World {

	public StartWorld() {
		Mothership ms = new Mothership();

		CapitalTranslationPoint pt = new CapitalTranslationPoint();
		pt.setPosition(new Vector2(0, 0));
		pt.lifespan = 180;

		ms.immuneTranslationPoint = pt;
		ms.lastTranslationPoint = pt;

		addActor(pt);
		addActor(ms);

		for (int i = 0; i < 10; i++) {
			Gem g = new Gem();
			g.setPosition(new Vector2(MathUtils.random(-RADIUS, RADIUS), MathUtils.random(-RADIUS, RADIUS)));
			addActor(g);

			int c = MathUtils.random(2, 5);

			for (int j = 0; j < c; j++) {
				Asteroid ast = new Asteroid();
				ast.setPosition(g.getPosition().add(MathUtils.random(-4, 4), MathUtils.random(-4, 4)));
				addActor(ast);
			}
		}
	}

	@Override
	public void renderBackground() {
		super.renderBackground();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	protected void update() {
		super.update();

		if (LD30.pc == null) {
			LD30.respawnTimer--;

			if (LD30.respawnTimer <= 0) {
				PlayerMiniShip pc = new PlayerMiniShip();
				LD30.pc = pc;
				pc.setPosition(new Vector2(0, 90));
				addActor(pc);
			}
		}
	}
}
