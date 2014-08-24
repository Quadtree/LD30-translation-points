package com.ironalloygames.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ironalloygames.ld30.world.WaterWorld;
import com.ironalloygames.ld30.world.World;

public class Shark extends Actor {

	boolean hasGem = false;

	public Shark() {
		hasGem = MathUtils.randomBoolean(0.25f);
		hp = 0.4f;
	}

	@Override
	public void beginContact(Actor other, Fixture localFixture) {
		super.beginContact(other, localFixture);

		other.takeDamage(0.3f);
	}

	@Override
	public void destroyed() {
		super.destroyed();

		if (hasGem) {
			Gem g = new Gem();
			g.setPosition(position);
			world.addActor(g);
		}
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(92 / 2 / LD30.METER_SCALE, 49 / 2 / LD30.METER_SCALE);

		body.createFixture(ps, 4);
	}

	@Override
	public boolean isInvulnerable() {
		return true;
	}

	@Override
	public void render() {
		super.render();

		drawDefault("shark");

		if (hasGem)
			drawDefault("gem", 90, Color.CYAN);
	}

	@Override
	public void update() {
		super.update();

		if (LD30.pc != null && LD30.pc.world == world && LD30.pc.position.dst2(position) < 400 * 400 && body != null && world instanceof WaterWorld) {
			Vector2 delta = LD30.pc.position.cpy().sub(position);
			delta.nor();
			delta.scl(800);

			body.applyLinearImpulse(delta, body.getWorldPoint(new Vector2(7, 0)), true);
		}
	}
}
