package com.ironalloygames.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ironalloygames.ld30.world.World;

public class Gem extends Actor {

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(33 / 2 / LD30.METER_SCALE, 33 / 2 / LD30.METER_SCALE);

		body.createFixture(ps, 1);
	}

	@Override
	public boolean isInvulnerable() {
		return true;
	}

	@Override
	public void render() {
		super.render();

		drawDefault("gem", 90, Color.CYAN);
	}
}
