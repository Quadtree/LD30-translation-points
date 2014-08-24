package com.ironalloygames.ld30;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class EnemyMiniShip extends MiniShip {
	int pointCreationCooldown = 0;

	int startDelay = 140;

	int timeWithoutEngine = 0;

	public void approach(Actor a) {
		if (a.world == this.world) {
			moveTowardsPoint(a.getPosition());
			System.out.print("Closing on target... " + a.getPosition());
		} else {
			// we need to planeshift!
			TranslationPoint existing = world.findTranslationPointTo(a.world);
			System.out.println("Need to planeshift!");

			if (existing != null) {
				System.out.println("Approaching translation point " + existing.getPosition());
				moveTowardsPoint(existing.getPosition());
			} else {
				// okay, can we create one?
				if (pointCreationCooldown <= 0) {
					world.addTranslationPoint(getPosition(), a.world, 7);
					System.out.println("Creating translation point");
				} else {
					// wait till it comes up...
					randomWalk();
				}
			}
		}
	}

	@Override
	protected float getEnginePower() {
		return super.getEnginePower();
	}

	public void moveTowardsPoint(Vector2 vec) {
		dest = vec.cpy();

		if (turn == 0) {
			thrust = 1;
		} else {
			thrust = -1;
		}
	}

	public void randomWalk() {
		if (dest.dst(position) < 40 * 40) {
			dest = position.cpy().add(MathUtils.random(-200, 200), MathUtils.random(-200, 200));
		}
		thrust = 1;
	}

	@Override
	public void update() {
		super.update();

		if (startDelay > 0) {
			startDelay--;
			return;
		}

		pointCreationCooldown--;

		// check if the engine is still on the mothership
		if (LD30.mothershipEngine == null && LD30.mothership.hasEngine) {
			approach(LD30.mothership);
		}
	}
}
