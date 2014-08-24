package com.ironalloygames.ld30;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.ironalloygames.ld30.world.World;

public class Actor {
	class Dialogue {
		Vector2 location;
		String text;
		int ttl;
	}

	float angle;
	float angularVelocity;
	Body body;
	boolean collisionGroupSetup = false;
	ArrayList<Dialogue> currentDialogue = new ArrayList<Dialogue>();
	float hp = Float.MAX_VALUE;

	public TranslationPoint immuneTranslationPoint;

	Boolean isDefaultSensor = null;

	public TranslationPoint lastTranslationPoint;
	public boolean oldCollidable = true;

	public World originalWorld;

	Vector2 position;

	public float transPointScale = 1;

	Vector2 velocity;

	public World world;

	public Actor() {
		position = new Vector2(0, 0);
		velocity = new Vector2(0, 0);
	}

	public void addDialogue(String text, Vector2 location, int ttl) {
		Dialogue d = new Dialogue();
		d.text = text;
		d.location = location;
		d.ttl = ttl;
		currentDialogue.add(d);
	}

	public void addWind(Vector2 wind) {
		if (body != null) {
			body.applyLinearImpulse(wind, body.getWorldCenter(), true);
		}
	}

	public void beginContact(Actor other, Fixture localFixture) {

	}

	public void destroyed() {
	}

	public void drawDefault(String graphic) {
		this.drawDefault(graphic, 90, originalWorld.getColor());
	}

	public void drawDefault(String graphic, int angleOffset, Color color) {
		Sprite s = LD30.a.getSprite(graphic);
		LD30.batch.setColor(color);
		LD30.batch.draw(s, getPosition().x, getPosition().y, .5f, .5f, 1, 1, s.getWidth() / LD30.METER_SCALE * transPointScale, s.getHeight() / LD30.METER_SCALE * transPointScale, body.getAngle() * (180 / MathUtils.PI)
				- angleOffset);
	}

	public void endContact(Actor other, Fixture localFixture) {

	}

	public void enteringWorld(World world) {
		this.world = world;

		BodyDef bd = new BodyDef();
		bd.type = getBodyType();
		bd.position.set(position);
		bd.linearVelocity.set(velocity);
		bd.angle = angle;
		bd.angularVelocity = angularVelocity;

		body = world.physicsWorld.createBody(bd);
		body.setUserData(this);

		if (originalWorld == null)
			originalWorld = world;

		oldCollidable = true;
		collisionGroupSetup = false;
	}

	public void exitingWorld(World world) {
		if (body != null) {
			world.physicsWorld.destroyBody(body);
			body = null;
		}
		world = null;
	}

	protected void explosion(int fragments) {
		for (int i = 0; i < fragments; i++) {
			Fragment f = new Fragment();
			f.setPosition(position);
			f.velocity.set(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
			f.originalWorld = this.originalWorld;
			world.addActor(f);
		}
	}

	public float getAngle() {
		if (body != null) {
			return body.getAngle();
		} else {
			return angle;
		}
	}

	protected BodyType getBodyType() {
		return BodyDef.BodyType.DynamicBody;
	}

	public float getMaxHP() {
		return Float.MAX_VALUE;
	}

	public Vector2 getPosition() {
		if (position != null)
			return position.cpy();
		else
			return new Vector2(0, 0);
	}

	public boolean isInvulnerable() {
		return false;
	}

	public boolean isTranslatable() {
		return true;
	}

	public boolean keep() {
		return hp > 0;
	}

	public void render() {
	}

	public void renderDialogue() {
		for (Dialogue d : currentDialogue) {
			TextBounds bd = LD30.a.getFont(16).getBounds(d.text);

			Vector2 pos = body.getWorldPoint(d.location).cpy();

			Vector3 v3 = new Vector3(pos.x, pos.y, 0);

			v3.mul(LD30.cam.combined);

			// System.out.println(v3);

			// LD30.sr.setColor(0, 0, 0, 0.5f);
			// LD30.sr.rect(v3.x * 1024 / 2 - bd.width / 2, v3.y * 768 / 2 -
			// bd.height / 2, bd.width, bd.height);

			LD30.a.getFont(16).setColor(1, 1, 0, 1);
			LD30.a.getFont(16).draw(LD30.batch, d.text, v3.x * 1024 / 2 - bd.width / 2, v3.y * 768 / 2 - bd.height / 2);
		}
	}

	public void setAngle(float angle) {
		if (body != null) {
			body.setTransform(getPosition(), angle);
		}
		this.angle = angle;
	}

	public void setCollidable(boolean collidable) {
		if (collidable != oldCollidable) {
			for (Fixture f : body.getFixtureList()) {
				Filter flt = f.getFilterData();

				if (collidable)
					flt.maskBits = 3;
				else
					flt.maskBits = 2;

				f.setFilterData(flt);
			}
		}

		oldCollidable = collidable;
	}

	public void setPosition(Vector2 pos) {
		this.position = pos.cpy();
		if (body != null)
			body.setTransform(pos.cpy(), getAngle());
	}

	void setupCollisionGroups() {
		for (Fixture f : body.getFixtureList()) {
			Filter flt = f.getFilterData();
			if (f.isSensor()) {
				flt.categoryBits = 2;
				flt.maskBits = 3;
			} else {
				flt.categoryBits = 1;
				flt.maskBits = 3;
			}
			f.setFilterData(flt);
		}
	}

	public void takeDamage(float damage) {
		hp -= damage;
	}

	public void update() {

		for (int i = 0; i < currentDialogue.size(); i++) {
			if (currentDialogue.get(i).ttl > 0)
				currentDialogue.get(i).ttl--;
			else
				currentDialogue.remove(i--);
		}

		if (body == null)
			return;

		if (!collisionGroupSetup) {
			setupCollisionGroups();
			collisionGroupSetup = true;
		}

		position = body.getPosition().cpy();

		if (world.fixPosition(position))
			body.setTransform(position.cpy(), getAngle());

		velocity = body.getLinearVelocity().cpy();
		body.setLinearVelocity(velocity.cpy().scl(world.getDragCoeff()));
		angle = body.getAngle();
		angularVelocity = body.getAngularVelocity();

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
