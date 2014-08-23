package com.ironalloygames.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;

public class MiniShip extends Actor {

	protected Vector2 dest;

	protected int strafe = 0;
	protected int thrust = 0;

	protected int turn = 0;

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		CircleShape cs = new CircleShape();
		cs.setRadius(37f / LD30.METER_SCALE / 2);

		body.createFixture(cs, 1);
	}

	protected float getEnginePower() {
		return 90;
	}

	@Override
	public void render() {
		super.render();
		LD30.batch.setColor(Color.WHITE);
		LD30.batch.draw(LD30.a.getSprite("mini_ship"), getPosition().x, getPosition().y, .5f, .5f, 1, 1, 37f / LD30.METER_SCALE, 37f / LD30.METER_SCALE, body.getAngle() * (180 / MathUtils.PI) - 90);
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
