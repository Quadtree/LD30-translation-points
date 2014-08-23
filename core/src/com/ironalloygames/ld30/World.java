package com.ironalloygames.ld30;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public abstract class World implements ContactListener {
	class Transfer {
		Actor a;
		World d;
		Vector2 p;
	}

	ArrayList<Actor> actorAddQueue = new ArrayList<Actor>();

	ArrayList<Actor> actors = new ArrayList<Actor>();

	public long milisDone;

	public com.badlogic.gdx.physics.box2d.World physicsWorld;
	ArrayList<Transfer> transferQueue = new ArrayList<World.Transfer>();

	public World worldAbove;

	public World worldBelow;

	public World() {
		physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
		physicsWorld.setContactListener(this);
		milisDone = System.currentTimeMillis();
	}

	public void addActor(Actor a) {
		actorAddQueue.add(a);
	}

	public void addTranslationPoint(Vector2 pos, World destination) {
		TranslationPoint localEnd = new TranslationPoint(destination);
		TranslationPoint remoteEnd = new TranslationPoint(this);
		localEnd.otherEnd = remoteEnd;
		remoteEnd.otherEnd = localEnd;

		localEnd.setPosition(pos);
		remoteEnd.setPosition(pos.cpy().scl(1f / getScale()).scl(destination.getScale()));

		addActor(localEnd);
		destination.addActor(remoteEnd);
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

	public Color getColor() {
		return new Color(1, 1, 1, 1);
	}

	public float getScale() {
		return 1;
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

	public void transferActor(Actor a, World dest, Vector2 newPos) {
		Transfer t = new Transfer();
		t.a = a;
		t.d = dest;
		t.p = newPos;
		transferQueue.add(t);
	}

	protected void update() {

		while (!actorAddQueue.isEmpty()) {
			actorAddQueue.get(0).enteringWorld(this);
			actors.add(actorAddQueue.get(0));
			actorAddQueue.remove(0);
		}

		while (!transferQueue.isEmpty()) {
			transferQueue.get(0).a.exitingWorld(this);
			transferQueue.get(0).a.setPosition(transferQueue.get(0).p);
			transferQueue.get(0).d.addActor(transferQueue.get(0).a);
			actors.remove(transferQueue.get(0).a);
			transferQueue.remove(0);
		}

		physicsWorld.step(0.016f, 1, 1);

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
