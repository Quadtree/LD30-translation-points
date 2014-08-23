package com.ironalloygames.ld30;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class TranslationPoint extends Actor {

	public World destination;
	public TranslationPoint otherEnd;

	public TranslationPoint(World destination) {
		this.destination = destination;
	}

	@Override
	public void beginContact(Actor other) {
		super.beginContact(other);

		if (other.isTranslatable() && other.immuneTranslationPoint != this) {
			other.lastTranslationPoint = this.otherEnd;
			other.immuneTranslationPoint = this.otherEnd;
			other.world.transferActor(other, destination, otherEnd.getPosition());
		}
	}

	@Override
	public void endContact(Actor other) {
		super.endContact(other);

		if (other.immuneTranslationPoint == this)
			other.immuneTranslationPoint = null;
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		CircleShape cs = new CircleShape();
		cs.setRadius(1);

		FixtureDef fd = new FixtureDef();
		fd.isSensor = true;
		fd.shape = cs;
		fd.density = 0;

		body.createFixture(fd);
	}

	@Override
	public boolean isTranslatable() {
		return false;
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
