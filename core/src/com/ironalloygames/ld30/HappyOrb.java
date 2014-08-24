package com.ironalloygames.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.ironalloygames.ld30.world.World;

public class HappyOrb extends Actor {
	public HappyOrb() {
		hp = 2;
	}

	@Override
	public void destroyed() {
		super.destroyed();

		Gem g = new Gem();
		g.setPosition(position);
		world.addActor(g);
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		CircleShape cs = new CircleShape();
		cs.setRadius(96 / 2 / LD30.METER_SCALE);

		body.createFixture(cs, 1);
	}

	@Override
	public void render() {
		super.render();

		drawDefault("happy_orb");
		drawDefault("gem", 90, Color.CYAN);
	}
}
