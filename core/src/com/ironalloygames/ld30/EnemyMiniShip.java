package com.ironalloygames.ld30;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ironalloygames.ld30.world.NegativeWorld;
import com.ironalloygames.ld30.world.StartWorld;
import com.ironalloygames.ld30.world.World;

public class EnemyMiniShip extends MiniShip {
	public static final float WEAPON_RANGE = 150;

	int lastThreat = 0;

	int pointCreationCooldown = 0;

	int shotCooldown = 0;

	int startDelay = 140;

	int timeWithoutEngine = 1000;

	Vector2 tractorBeamTarget = null;

	public EnemyMiniShip() {
		LD30.enemyMiniShip = this;
	}

	public boolean approach(Actor a) {
		if (a.world == this.world) {

			// System.out.print("Closing on target... " + a.getPosition());
			return moveTowardsPoint(a.getPosition());
		} else {
			// we need to planeshift!
			TranslationPoint existing = world.findTranslationPointTo(position, a.world);
			// System.out.println("Need to planeshift!");

			if (existing != null) {
				// System.out.println("Approaching translation point " +
				// existing.getPosition());
				moveTowardsPoint(existing.getPosition());
			} else {
				// okay, can we create one?
				if (pointCreationCooldown <= 0) {
					createPointAtLoc(a.world);
					// System.out.println("Creating translation point");
				} else {
					// wait till it comes up...
					randomWalk();
					// System.out.println("Random walk");
				}
			}
		}

		return false;
	}

	private boolean createPointAtLoc(World dest) {
		if (pointCreationCooldown <= 0) {
			world.addTranslationPoint(getPosition(), dest, 280);
			pointCreationCooldown = 1200;
			return true;
		}
		return false;
	}

	private void flee() {
		createPointAtLoc(LD30.worlds.get(MathUtils.random(1, LD30.worlds.size() - 2)));

		TranslationPoint pt = world.findTranslationPointTo(getPosition(), null);

		if (pt != null)
			moveTowardsPoint(pt.position);
	}

	@Override
	protected short getColGroup() {
		return -7;
	}

	@Override
	protected float getEnginePower() {
		return super.getEnginePower();
	}

	public boolean moveTowardsPoint(Vector2 vec) {
		dest = vec.cpy();

		if (turn == 0) {
			thrust = 1;
		} else {
			thrust = -1;
		}

		return position.dst2(vec) < 100 * 100;
	}

	public void randomWalk() {
		if (dest.dst(position) < 40 * 40) {
			dest = position.cpy().add(MathUtils.random(-200, 200), MathUtils.random(-200, 200));
		}
		thrust = 1;
	}

	@Override
	public void render() {
		super.render();

		if (tractorBeamTarget != null) {
			Vector2 d = tractorBeamTarget.cpy().sub(position);
			LD30.batch.draw(LD30.a.getSprite("ray"), getPosition().x, getPosition().y, 0, .5f, 1, 1, position.dst(tractorBeamTarget), 128f / LD30.METER_SCALE, d.angle());
		}
	}

	private boolean tow(Actor a) {
		if (a.body == null)
			return false;

		float range = position.dst(a.position);

		Vector2 impulse = new Vector2(0, 0);

		if (range > 20) {
			impulse = position.cpy().sub(a.position);
			impulse.nor();
			impulse.scl(2000);
			a.body.applyLinearImpulse(impulse, a.body.getWorldCenter(), true);
			return false;
		}

		return true;
	}

	@Override
	public void update() {
		super.update();

		tractorBeamTarget = null;

		if (startDelay > 0) {
			startDelay--;
			return;
		}

		if (world instanceof StartWorld || world instanceof NegativeWorld)
			lastThreat = 30;

		timeWithoutEngine++;
		pointCreationCooldown--;
		lastThreat--;
		thrust = -1;
		shotCooldown--;

		if (LD30.pc != null && LD30.pc.world == this.world && LD30.pc.position.dst2(position) < WEAPON_RANGE * WEAPON_RANGE) {
			lastThreat = 240;

			if (shotCooldown <= 0) {
				Bolt.shoot(this, getPosition(), LD30.pc.position.cpy().sub(position).angleRad(), 400, 0.05f);
				shotCooldown = 30;
			}
		}

		// check if the engine is still on the mothership
		if (LD30.mothershipEngine == null && LD30.mothership.hasEngine) {
			if (approach(LD30.mothership)) {
				LD30.mothership.detachEngine();
			}
		} else if (LD30.mothershipEngine != null) {
			if (LD30.mothershipEngine.world == world && LD30.mothershipEngine.position.dst2(position) < 100 * 100) {
				tractorBeamTarget = LD30.mothershipEngine.getPosition();

				if (tow(LD30.mothershipEngine)) {
					timeWithoutEngine = 0;
				}

				// we have it now lets get out of here
				if (timeWithoutEngine < 20 && lastThreat > 0) {
					flee();
				}
			} else if (timeWithoutEngine > 60) {
				approach(LD30.mothershipEngine);
			}
		}
	}
}
