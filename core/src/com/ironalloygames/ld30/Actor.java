package com.ironalloygames.ld30;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.ironalloygames.ld30.world.World;

public class Actor {
	float angle;
	Body body;
	public TranslationPoint immuneTranslationPoint;
	public TranslationPoint lastTranslationPoint;
	Vector2 position;
	public float transPointScale = 1;

	Vector2 velocity;

	public World world;

	public Actor() {
		position = new Vector2(0, 0);
		velocity = new Vector2(0, 0);
	}

	public void beginContact(Actor other) {

	}

	public void destroyed() {
	}

	public void endContact(Actor other) {

	}

	public void enteringWorld(World world) {
		this.world = world;

		BodyDef bd = new BodyDef();
		bd.type = BodyDef.BodyType.DynamicBody;
		bd.position.set(position);
		bd.linearVelocity.set(velocity);
		bd.angle = angle;

		body = world.physicsWorld.createBody(bd);
		body.setUserData(this);
	}

	public void exitingWorld(World world) {
		if (body != null) {
			world.physicsWorld.destroyBody(body);
			body = null;
		}
		world = null;
	}

	public float getAngle() {
		if (body != null) {
			return body.getAngle();
		} else {
			return angle;
		}
	}

	public Vector2 getPosition() {
		if (position != null)
			return position.cpy();
		else
			return new Vector2(0, 0);
	}

	public boolean isTranslatable() {
		return true;
	}

	public boolean keep() {
		return true;
	}

	public void render() {
	}

	public void setPosition(Vector2 pos) {
		this.position = pos.cpy();
		if (body != null)
			body.setTransform(pos.cpy(), getAngle());
	}

	public void update() {
		position = body.getPosition().cpy();
		velocity = body.getLinearVelocity().cpy();
		angle = body.getAngle();

		if (immuneTranslationPoint != null) {
			float dist = immuneTranslationPoint.position.dst(position);

			if (dist < TranslationPoint.GRAB_RANGE) {
				this.transPointScale = (dist / TranslationPoint.GRAB_RANGE);
			} else {
				immuneTranslationPoint = null;
			}
		}
	}
}
