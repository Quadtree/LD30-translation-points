package com.ironalloygames.ld30;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ironalloygames.ld30.world.World;

public class Mothership extends Actor {
	public Mothership() {
		angle = MathUtils.PI / 2;
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(350 / 2 / LD30.METER_SCALE, 314 / 2 / LD30.METER_SCALE, new Vector2(35 / LD30.METER_SCALE, 0), 0);

		body.createFixture(ps, 0);
	}

	@Override
	protected BodyType getBodyType() {
		return BodyType.StaticBody;
	}

	@Override
	public boolean isInvulnerable() {
		return true;
	}

	@Override
	public boolean isTranslatable() {
		return false;
	}

	@Override
	public void render() {
		super.render();

		this.drawDefault("mothership1");
	}

}
