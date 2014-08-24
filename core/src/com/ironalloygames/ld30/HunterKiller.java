package com.ironalloygames.ld30;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ironalloygames.ld30.world.World;

public class HunterKiller extends Actor {
	public static float WEAPON_RANGE = 200;
	int shotCooldown = 0;

	public HunterKiller() {
		hp = 2;
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		CircleShape cs2 = new CircleShape();
		cs2.setRadius(5);

		FixtureDef fd = new FixtureDef();
		fd.shape = cs2;
		fd.filter.groupIndex = -7;
		fd.density = 1;

		body.createFixture(fd);
	}

	@Override
	public void render() {
		super.render();

		drawDefault("star1");
	}

	@Override
	public void update() {
		super.update();

		shotCooldown--;

		if (LD30.pc != null && LD30.pc.world == world && LD30.pc.position.dst2(position) < WEAPON_RANGE * WEAPON_RANGE) {
			if (shotCooldown <= 0) {
				Bolt.shoot(this, getPosition(), LD30.pc.position.cpy().sub(position).angleRad(), 600, 0.5f);
				shotCooldown = 90;
			}
		}
	}
}
