package com.ironalloygames.ld30;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class Actor {
	Body body;
	Vector2 position;
	World world;

	public void beginContact(Actor other) {

	}

	public void destroyed() {
	}

	public void endContact(Actor other) {

	}

	public void enteringWorld(World world) {
		this.world = world;

		BodyDef bd = new BodyDef();
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

	public Vector2 getPosition() {
		return position.cpy();
	}

	public boolean keep() {
		return true;
	}

	public void render() {
	}

	public void setPosition(Vector2 pos) {
		this.position = pos.cpy();
	}

	public void update() {
	}
}
