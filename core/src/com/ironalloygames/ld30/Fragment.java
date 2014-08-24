package com.ironalloygames.ld30;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ironalloygames.ld30.world.World;

public class Fragment extends Actor {

	public int lifespan;

	public Fragment() {
		lifespan = MathUtils.random(180, 600);
		angle = MathUtils.random(0, MathUtils.PI2);
		angularVelocity = MathUtils.random(80);
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		CircleShape cs = new CircleShape();
		cs.setRadius(1 / LD30.METER_SCALE);

		FixtureDef fd = new FixtureDef();
		fd.density = 0.3f;
		fd.shape = cs;
		fd.filter.groupIndex = -6;

		body.createFixture(fd);
	}

	@Override
	public boolean isInvulnerable() {
		return true;
	}

	@Override
	public boolean keep() {
		return super.keep() && lifespan > 0;
	}

	@Override
	public void render() {
		super.render();

		drawDefault("fragment");
	}

	@Override
	public void update() {
		super.update();

		lifespan--;
	}
}
