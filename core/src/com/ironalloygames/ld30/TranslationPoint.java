package com.ironalloygames.ld30;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ironalloygames.ld30.world.World;

public class TranslationPoint extends Actor {

	public static final float GRAB_RANGE = 12;
	private static final float GRAVITY_FORCE = 20;
	private static final int GRAVITY_RANGE = 60;
	private static final float JUMP_RANGE = .2f;

	public World destination;

	Set<Actor> inGravityWell = new HashSet<Actor>();
	public int lifespan = Integer.MAX_VALUE;

	public TranslationPoint otherEnd;

	public TranslationPoint(World destination) {
		this.destination = destination;
	}

	@Override
	public void beginContact(Actor other) {
		super.beginContact(other);

		if (other.isTranslatable())
			inGravityWell.add(other);

		/*
		 * if (other.isTranslatable() && other.immuneTranslationPoint != this) {
		 * other.lastTranslationPoint = this.otherEnd;
		 * other.immuneTranslationPoint = this.otherEnd;
		 * other.world.transferActor(other, destination,
		 * otherEnd.getPosition()); }
		 */
	}

	@Override
	public void endContact(Actor other) {
		super.endContact(other);

		inGravityWell.remove(other);

		if (other.immuneTranslationPoint == this)
			other.immuneTranslationPoint = null;
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		CircleShape cs = new CircleShape();
		cs.setRadius(GRAVITY_RANGE);

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
	public boolean keep() {
		return super.keep() && lifespan >= 0;

	}

	@Override
	public void render() {
		super.render();
		Color c = destination.getColor();

		if (lifespan < 60) {
			c.a = lifespan / 60f;
		}

		Sprite raySprite = LD30.a.getSprite("tp_ray");
		LD30.batch.setColor(c);
		for (int i = 0; i < 8; i++) {
			LD30.batch.draw(raySprite, getPosition().x, getPosition().y, .5f, .5f, 1, 1, 128f / LD30.METER_SCALE * MathUtils.random(), 128f / LD30.METER_SCALE * MathUtils.random(), MathUtils.random(0, 360));
		}
	}

	@Override
	public void update() {
		super.update();

		for (Actor a : inGravityWell) {
			if (a.body != null && a.immuneTranslationPoint != this) {
				Vector2 delta = getPosition().sub(a.body.getPosition());
				float dist2 = delta.len2();
				float force = ((GRAVITY_RANGE * GRAVITY_RANGE) - delta.len2()) / (GRAVITY_RANGE * GRAVITY_RANGE);
				delta.nor();

				if (dist2 < (GRAB_RANGE * GRAB_RANGE)) {
					Vector2 vel = a.body.getLinearVelocity();

					a.body.setLinearVelocity(delta.cpy().scl(vel.len()));

					float pt = (float) Math.sqrt(dist2) / GRAB_RANGE;
					a.transPointScale = pt;

					if (dist2 < JUMP_RANGE * JUMP_RANGE || Math.sqrt(dist2) < vel.len() / 60) {
						a.lastTranslationPoint = this.otherEnd;
						a.immuneTranslationPoint = this.otherEnd;
						a.world.transferActor(a, destination, otherEnd.getPosition());
					}
				}

				if (a.lastTranslationPoint != this)
					a.body.applyLinearImpulse(delta.scl(force).scl(GRAVITY_FORCE), a.body.getWorldCenter(), true);
			}
		}

		lifespan--;
	}

}
