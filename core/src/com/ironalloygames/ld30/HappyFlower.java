package com.ironalloygames.ld30;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.ironalloygames.ld30.world.World;

public class HappyFlower extends Actor {
	public HappyFlower() {
		hp = 1;
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		CircleShape cs = new CircleShape();
		cs.setRadius(48 / 2 / LD30.METER_SCALE);

		body.createFixture(cs, 1);
	}

	@Override
	public void render() {
		super.render();

		drawDefault("happy_flower");
	}
}
