package com.ironalloygames.ld30;

import com.badlogic.gdx.math.MathUtils;

public class TranslationPoint extends Actor {

	public World destination;
	public TranslationPoint otherEnd;

	public TranslationPoint(World destination) {
		this.destination = destination;
	}

	@Override
	public void beginContact(Actor other) {
		// TODO Auto-generated method stub
		super.beginContact(other);
	}

	@Override
	public void endContact(Actor other) {
		// TODO Auto-generated method stub
		super.endContact(other);
	}

	@Override
	public void render() {
		super.render();

		LD30.batch.draw(LD30.a.getSprite("tp_ray"), getPosition().x, getPosition().y, .5f, .5f, 1, 1, 128f / LD30.METER_SCALE, 128f / LD30.METER_SCALE, body.getAngle() * (180 / MathUtils.PI) - 90);
	}

}
