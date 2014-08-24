package com.ironalloygames.ld30;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.ironalloygames.ld30.world.World;

public class HunterKiller extends Actor {
	public HunterKiller() {
		hp = 2;
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		CircleShape cs2 = new CircleShape();
		cs2.setRadius(5);

		body.createFixture(cs2, 1);
	}

	@Override
	public void render() {
		super.render();

		drawDefault("star1");
	}
}
