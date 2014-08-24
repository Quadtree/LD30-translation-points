package com.ironalloygames.ld30;

import java.util.HashSet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ironalloygames.ld30.world.World;

public class Star extends Actor {

	public static final float BURN_POWER = 1 / 60f / 5f;
	public static final float BURN_RANGE = 50;

	Fixture burnSensor;

	HashSet<Actor> inBurnRange = new HashSet<Actor>();

	@Override
	public void beginContact(Actor other, Fixture localFixture) {
		super.beginContact(other, localFixture);

		if (localFixture == burnSensor && !(other instanceof Star) && !other.isInvulnerable()) {

			inBurnRange.add(other);
		}
	}

	@Override
	public void endContact(Actor other, Fixture localFixture) {
		super.endContact(other, localFixture);

		if (localFixture == burnSensor) {
			inBurnRange.remove(other);
		}
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		FixtureDef fd = new FixtureDef();
		fd.density = 0;
		fd.isSensor = true;

		CircleShape cs1 = new CircleShape();
		cs1.setRadius(BURN_RANGE);
		fd.shape = cs1;

		burnSensor = body.createFixture(fd);

		CircleShape cs2 = new CircleShape();
		body.createFixture(cs2, 0.2f);
	}

	@Override
	public void render() {
		super.render();

		drawDefault("star1");

		for (Actor a : inBurnRange) {
			float ang = a.getPosition().sub(position).angleRad();
			Color c = originalWorld.getColor();
			c.a = MathUtils.clamp((BURN_RANGE - a.getPosition().dst(position)) / (BURN_RANGE / 3), 0, 1);
			LD30.batch.setColor(c);

			// System.out.println((BURN_RANGE - a.getPosition().dst(position)) /
			// (BURN_RANGE / 3));
			LD30.batch.draw(LD30.a.getSprite("tp_ray"), getPosition().x, getPosition().y, .5f, .5f, 1, 1, BURN_RANGE * 2, 128f / LD30.METER_SCALE, ang * MathUtils.radiansToDegrees);
		}
	}

	@Override
	public void update() {
		super.update();

		for (Actor a : inBurnRange) {
			a.takeDamage(BURN_POWER * (1 - (a.position.dst(position) / BURN_RANGE)));
		}
	}

}
