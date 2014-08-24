package com.ironalloygames.ld30;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ironalloygames.ld30.world.World;

public class HyperspaceWall extends Actor {
	public HyperspaceWall() {
		angle = MathUtils.random(MathUtils.PI2);
		hp = 8;
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(32 / 2 / LD30.METER_SCALE, 237 / 2 / LD30.METER_SCALE);

		body.createFixture(ps, 16);
	}

	@Override
	public void render() {
		super.render();

		drawDefault("wall");
	}
}
