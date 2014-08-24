package com.ironalloygames.ld30;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ironalloygames.ld30.world.Happyspace;
import com.ironalloygames.ld30.world.World;

public class HappyFlower extends Actor {

	public final static float WEAPON_RANGE = 150;

	int shotCooldown = 0;

	public HappyFlower() {
		hp = 1;
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		CircleShape cs = new CircleShape();
		cs.setRadius(48 / 2 / LD30.METER_SCALE);

		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 1;
		fd.filter.groupIndex = -7;

		body.createFixture(fd);
	}

	@Override
	public void render() {
		super.render();

		drawDefault("happy_flower");
	}

	@Override
	public void update() {
		super.update();

		shotCooldown--;

		if (originalWorld instanceof Happyspace && ((Happyspace) originalWorld).aggroed) {
			if (LD30.pc != null && LD30.pc.world == world && LD30.pc.position.dst2(position) < WEAPON_RANGE * WEAPON_RANGE) {
				if (shotCooldown <= 0) {
					Bolt.shoot(this, getPosition(), LD30.pc.position.cpy().sub(position).angleRad(), 320, 0.15f);
					shotCooldown = 60;
				}
			}
		}
	}
}
