package com.ironalloygames.ld30;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public abstract class World implements ContactListener {
	ArrayList<Actor> actorAddQueue = new ArrayList<Actor>();

	ArrayList<Actor> actors = new ArrayList<Actor>();

	public long milisDone;
	public com.badlogic.gdx.physics.box2d.World physicsWorld;

	public World() {
		physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0,
				0), true);
		milisDone = System.currentTimeMillis();
	}

	public void addActor(Actor a) {
		actorAddQueue.add(a);
	}

	@Override
	public void beginContact(Contact contact) {
		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();

		if (o1 instanceof Actor && o2 instanceof Actor) {
			((Actor) o1).beginContact((Actor) o2);
			((Actor) o2).beginContact((Actor) o1);
		}
	}

	@Override
	public void endContact(Contact contact) {
		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();

		if (o1 instanceof Actor && o2 instanceof Actor) {
			((Actor) o1).endContact((Actor) o2);
			((Actor) o2).endContact((Actor) o1);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	public void render() {
		for (Actor a : actors) {
			a.render();
		}
	}

	public void renderBackground() {

	}

	protected void update() {

		while (!actorAddQueue.isEmpty()) {
			actorAddQueue.get(0).enteringWorld(this);
			actors.add(actorAddQueue.get(0));
			actorAddQueue.remove(0);
		}

		for (int i = 0; i < actors.size(); i++) {
			if (actors.get(i).keep()) {
				actors.get(i).update();
			} else {
				actors.get(i).exitingWorld(this);
				actors.get(i).destroyed();
				actors.remove(i--);
			}
		}
	}

	public void updateIfNeeded() {
		while (milisDone < System.currentTimeMillis()) {
			update();
			milisDone += 16;
		}
	}
}
