package com.ironalloygames.ld30;

import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ironalloygames.ld30.world.World;

public class Asteroid extends Actor {
	public Asteroid() {
		hp = 1.5f;
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(48 / 2 / LD30.METER_SCALE, 61 / 2 / LD30.METER_SCALE);

		body.createFixture(ps, 4);
	}

	@Override
	public void render() {
		super.render();

		drawDefault("asteroid");
	}

}
