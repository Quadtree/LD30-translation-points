package com.ironalloygames.ld30;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
		Sprite raySprite = LD30.a.getSprite("tp_ray");
		LD30.batch.setColor(destination.getColor());
		for (int i = 0; i < 8; i++) {
			LD30.batch.draw(raySprite, getPosition().x, getPosition().y, .5f, .5f, 1, 1, 128f / LD30.METER_SCALE * MathUtils.random(), 128f / LD30.METER_SCALE * MathUtils.random(), MathUtils.random(0, 360));
		}
	}

}
