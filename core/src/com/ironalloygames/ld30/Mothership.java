package com.ironalloygames.ld30;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ironalloygames.ld30.world.World;

public class Mothership extends Actor {

	public boolean hasEngine = true;

	int startTime = 0;

	public Mothership() {
		angle = MathUtils.PI / 2;
		LD30.mothership = this;
	}

	public void detachEngine() {
		hasEngine = false;

		MothershipEngine me = new MothershipEngine();
		me.setPosition(getPosition().add(0, -175 / LD30.METER_SCALE));
		me.lastTranslationPoint = this.lastTranslationPoint;
		me.immuneTranslationPoint = this.immuneTranslationPoint;

		world.addActor(me);
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(425 / 2 / LD30.METER_SCALE, 314 / 2 / LD30.METER_SCALE, new Vector2(0, 0), 0);

		FixtureDef fd = new FixtureDef();
		fd.filter.groupIndex = -5;
		fd.shape = ps;
		fd.density = 0;

		body.createFixture(fd);
	}

	@Override
	protected BodyType getBodyType() {
		return BodyType.KinematicBody;
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

		this.drawDefault("mothership" + (hasEngine ? "3" : "1"));
	}

	@Override
	public void update() {
		super.update();

		if (startTime < 140) {
			body.setLinearVelocity(0, 15);
		} else {
			body.setLinearVelocity(0, 0);
		}

		startTime++;
	}

}
