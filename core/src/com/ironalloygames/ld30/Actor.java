package com.ironalloygames.ld30;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class Actor {
	Body body;
	public TranslationPoint lastTranslationPoint;
	Vector2 position;

	public World world;

	public Actor() {
		position = new Vector2(0, 0);
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
			return 0;
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
	}
}
