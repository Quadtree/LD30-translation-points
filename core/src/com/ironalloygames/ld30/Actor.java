package com.ironalloygames.ld30;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.ironalloygames.ld30.world.World;

public class Actor {
	float angle;
	float angularVelocity;
	Body body;
	float hp = Float.MAX_VALUE;
	public TranslationPoint immuneTranslationPoint;
	public TranslationPoint lastTranslationPoint;
	public World originalWorld;

	Vector2 position;

	public float transPointScale = 1;

	Vector2 velocity;
	public World world;

	public Actor() {
		position = new Vector2(0, 0);
		velocity = new Vector2(0, 0);
	}

	public void beginContact(Actor other, Fixture localFixture) {

	}

	public void destroyed() {
	}

	public void drawDefault(String graphic) {
		Sprite s = LD30.a.getSprite(graphic);
		LD30.batch.setColor(originalWorld.getColor());
		LD30.batch.draw(s, getPosition().x, getPosition().y, .5f, .5f, 1, 1, s.getWidth() / LD30.METER_SCALE * transPointScale, s.getHeight() / LD30.METER_SCALE * transPointScale, body.getAngle() * (180 / MathUtils.PI) - 90);
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

	public void setAngle(float angle) {
		if (body != null) {
			body.setTransform(getPosition(), angle);
		}
		this.angle = angle;
	}

	public void setPosition(Vector2 pos) {
		this.position = pos.cpy();
		if (body != null)
			body.setTransform(pos.cpy(), getAngle());
	}

	public void takeDamage(float damage) {
		hp -= damage;
	}

	public void update() {
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
