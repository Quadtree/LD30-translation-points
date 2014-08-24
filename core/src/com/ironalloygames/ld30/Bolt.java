package com.ironalloygames.ld30;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ironalloygames.ld30.world.World;

public class Bolt extends Actor {

	public static void shoot(Actor firer, Vector2 pos, float angle, float speed, float damage) {
		Bolt b = new Bolt();
		b.setPosition(pos);
		b.setAngle(angle);
		b.velocity.set(MathUtils.cos(angle) * speed, MathUtils.sin(angle) * speed);
		b.damage = damage;
		b.originalWorld = firer.originalWorld;
		b.world = firer.world;
		firer.world.addActor(b);

		if (LD30.currentWorld == firer.world)
			LD30.a.getSound("shoot").play();
	}

	float damage = 0.25f;

	int lifespan = 120;

	@Override
	public void beginContact(Actor other, Fixture localFixture) {
		super.beginContact(other, localFixture);

		if (!other.isInvulnerable()) {
			other.takeDamage(damage);
			this.hp = -1;

			if (LD30.currentWorld == world)
				LD30.a.getSound("hit").play();
		}
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		CircleShape cs = new CircleShape();
		cs.setRadius(0.1f);

		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 1;
		fd.filter.groupIndex = -7;

		body.createFixture(fd);
	}

	@Override
	public float getDragModifier() {
		return 0.02f;
	}

	@Override
	public boolean isInvulnerable() {
		return true;
	}

	@Override
	public void render() {
		super.render();

		drawDefault("bolt", 0, originalWorld.getColor());
	}

	@Override
	public void update() {
		super.update();

		lifespan--;

		if (lifespan <= 0)
			hp = -1;
	}
}
