package com.ironalloygames.ld30;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ironalloygames.ld30.world.World;

public class MiniShip extends Actor {

	protected Vector2 dest;

	float maxShield = 0;
	float shield = 0;

	protected int strafe = 0;

	protected int thrust = 0;
	protected int turn = 0;

	public MiniShip() {
		hp = 1;
	}

	@Override
	public void destroyed() {
		super.destroyed();
		this.explosion(10);
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		CircleShape cs = new CircleShape();
		cs.setRadius(37f / LD30.METER_SCALE / 2);

		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 1;
		fd.filter.groupIndex = getColGroup();

		body.createFixture(fd);
	}

	protected short getColGroup() {
		return 0;
	}

	protected float getEnginePower() {
		return 90;
	}

	@Override
	public void render() {
		super.render();
		drawDefault("mini_ship");
	}

	@Override
	public void takeDamage(float damage) {

		if (shield > 0) {
			float absorb = Math.max(shield, damage);
			damage -= absorb;
			shield -= absorb;
		}

		super.takeDamage(damage);
	}

	@Override
	public void update() {
		super.update();

		if (dest != null) {
			Vector2 leftPoint = getPosition().add(MathUtils.cos(getAngle() + 0.15f), MathUtils.sin(getAngle() + 0.15f));
			Vector2 centerPoint = getPosition().add(MathUtils.cos(getAngle()) * 1.0f, MathUtils.sin(getAngle()) * 1.0f);
			Vector2 rightPoint = getPosition().add(MathUtils.cos(getAngle() - 0.15f), MathUtils.sin(getAngle() - 0.15f));

			float leftPointDist = leftPoint.dst2(dest);
			float centerPointDist = centerPoint.dst2(dest);
			float rightPointDist = rightPoint.dst2(dest);

			turn = 0;
			if (leftPointDist < centerPointDist && leftPointDist < rightPointDist)
				turn = 1;
			if (rightPointDist < centerPointDist && rightPointDist < leftPointDist)
				turn = -1;
		}

		if (body != null) {
			body.setSleepingAllowed(false);
			body.setAngularVelocity(turn * 6);

			body.applyLinearImpulse(new Vector2(MathUtils.cos(getAngle() - MathUtils.PI / 2) * getEnginePower() * strafe, MathUtils.sin(getAngle() - MathUtils.PI / 2) * getEnginePower() * strafe), body.getWorldCenter(), true);

			if (thrust == 1) {
				body.applyLinearImpulse(new Vector2(MathUtils.cos(getAngle()) * getEnginePower(), MathUtils.sin(getAngle()) * getEnginePower()), body.getWorldCenter(), true);
			} else if (thrust == -1) {
				Vector2 curVel = body.getLinearVelocity().cpy();
				float pwr = Math.min(curVel.len() * body.getMass(), getEnginePower());
				curVel.nor();

				body.applyLinearImpulse(curVel.scl(-pwr), body.getWorldCenter(), true);

			}
		}
	}

}
