package com.ironalloygames.ld30;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ironalloygames.ld30.world.World;

public class MothershipEngine extends Actor {

	public MothershipEngine() {
		angle = MathUtils.PI / 2;
		LD30.mothershipEngine = this;
	}

	@Override
	public void destroyed() {
		super.destroyed();
		LD30.mothershipEngine = null;
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		CircleShape cs = new CircleShape();
		cs.setRadius(72 / LD30.METER_SCALE);

		FixtureDef fd = new FixtureDef();
		fd.density = 2f;
		fd.shape = cs;
		fd.filter.groupIndex = -5;

		body.createFixture(fd);
	}

	@Override
	public boolean isInvulnerable() {
		return true;
	}

	@Override
	public void render() {
		super.render();

		this.drawDefault("mothership2");
	}

}
