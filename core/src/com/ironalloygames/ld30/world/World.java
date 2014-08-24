package com.ironalloygames.ld30.world;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ironalloygames.ld30.Actor;
import com.ironalloygames.ld30.LD30;
import com.ironalloygames.ld30.TranslationPoint;

public abstract class World implements ContactListener {
	class Transfer {
		Actor a;
		World d;
		Vector2 p;
	}

	public static float RADIUS = 250;

	ArrayList<Actor> actorAddQueue = new ArrayList<Actor>();

	ArrayList<Actor> actors = new ArrayList<Actor>();

	public long milisDone;

	public com.badlogic.gdx.physics.box2d.World physicsWorld;

	public final Vector2 pivot = new Vector2(0, 0);

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

	public void addTranslationPoint(Vector2 pos, World destination, int lifespan) {
		TranslationPoint localEnd = new TranslationPoint(destination);
		TranslationPoint remoteEnd = new TranslationPoint(this);
		localEnd.otherEnd = remoteEnd;
		remoteEnd.otherEnd = localEnd;

		localEnd.setPosition(pos);
		remoteEnd.setPosition(pos.cpy().scl(1f / getScale()).scl(destination.getScale()));

		addActor(localEnd);
		destination.addActor(remoteEnd);

		localEnd.lifespan = lifespan;
		remoteEnd.lifespan = lifespan;
	}

	@Override
	public void beginContact(Contact contact) {
		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();

		if (o1 instanceof Actor && o2 instanceof Actor) {
			((Actor) o1).beginContact((Actor) o2, contact.getFixtureA());
			((Actor) o2).beginContact((Actor) o1, contact.getFixtureB());
		}
	}

	@Override
	public void endContact(Contact contact) {
		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();

		if (o1 instanceof Actor && o2 instanceof Actor) {
			((Actor) o1).endContact((Actor) o2, contact.getFixtureA());
			((Actor) o2).endContact((Actor) o1, contact.getFixtureB());
		}
	}

	public TranslationPoint findTranslationPointTo(Vector2 pos, World otherWorld) {

		TranslationPoint bestPoint = null;
		float bestDist2 = Float.MAX_VALUE;

		for (Actor a : actors) {
			if (a instanceof TranslationPoint && (((TranslationPoint) a).destination == otherWorld || otherWorld == null)) {
				float dist2 = pos.dst2(a.getPosition());

				if (dist2 < bestDist2) {
					bestDist2 = dist2;
					bestPoint = (TranslationPoint) a;
				}
			}
		}

		return bestPoint;
	}

	public boolean fixPosition(Vector2 pos) {

		boolean fixed = false;

		if (pos.x > pivot.x + RADIUS) {
			pos.add(-RADIUS * 2, 0);
			fixed = true;
		}

		if (pos.x < pivot.x - RADIUS) {
			pos.add(RADIUS * 2, 0);
			fixed = true;
		}

		if (pos.y > pivot.y + RADIUS) {
			pos.add(0, -RADIUS * 2);
			fixed = true;
		}

		if (pos.y < pivot.y - RADIUS) {
			pos.add(0, RADIUS * 2);
			fixed = true;
		}

		return fixed;
	}

	public Color getColor() {
		return new Color(1, 1, 1, 1);
	}

	public float getDragCoeff() {
		return 0.99f;
	}

	protected int getNumSpaceDust() {
		return 20;
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

	public void renderDialogue() {
		for (Actor a : actors) {
			a.renderDialogue();
		}
	}

	public void transferActor(Actor a, World dest, Vector2 newPos) {
		Transfer t = new Transfer();
		t.a = a;
		t.d = dest;
		t.p = newPos;
		transferQueue.add(t);
	}

	protected void transferred(Actor a) {

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

			if (transferQueue.get(0).a == LD30.pc)
				LD30.needToCreateSpaceDust = true;

			transferred(transferQueue.get(0).a);

			actors.remove(transferQueue.get(0).a);
			transferQueue.remove(0);
		}

		physicsWorld.step(0.016f, 1, 1);

		for (Actor a : actors)
			a.transPointScale = 1;

		for (int i = 0; i < actors.size(); i++) {
			if (actors.get(i).keep()) {
				actors.get(i).update();
			} else {
				actors.get(i).exitingWorld(this);
				actors.get(i).destroyed();
				actors.remove(i--);
			}
		}

		for (Actor a : actors) {
			a.setCollidable(a.transPointScale > 0.99f);
		}

	}

	public void updateIfNeeded() {
		while (milisDone < System.currentTimeMillis()) {
			update();
			milisDone += 16;
		}
	}
}
