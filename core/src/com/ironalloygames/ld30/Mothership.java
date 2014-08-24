package com.ironalloygames.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ironalloygames.ld30.world.World;

public class Mothership extends Actor {

	int firstMessageTimer = 0;

	boolean firstSpawnPossible = false;

	public int gems = 0;

	public boolean hasEngine = true;

	int heyDialogTimer = -100000000;

	int startTime = 0;

	public Mothership() {
		angle = MathUtils.PI / 2;
		LD30.mothership = this;

	}

	public void detachEngine() {
		hasEngine = false;

		MothershipEngine me = new MothershipEngine();
		me.setPosition(getPosition().add(0, -175 / LD30.METER_SCALE));
		me.lastTranslationPoint = this.lastTranslationPoint;
		me.immuneTranslationPoint = this.immuneTranslationPoint;

		world.addActor(me);

		if (heyDialogTimer < 0)
			heyDialogTimer = 0;
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(425 / 2 / LD30.METER_SCALE, 314 / 2 / LD30.METER_SCALE, new Vector2(0, 0), 0);

		FixtureDef fd = new FixtureDef();
		fd.filter.groupIndex = -5;
		fd.shape = ps;
		fd.density = 0;

		body.createFixture(fd);
	}

	@Override
	protected BodyType getBodyType() {
		return BodyType.KinematicBody;
	}

	@Override
	public boolean isInvulnerable() {
		return true;
	}

	@Override
	public boolean isTranslatable() {
		return false;
	}

	@Override
	public void render() {
		super.render();

		this.drawDefault("mothership" + (hasEngine ? "3" : "1"));

		if (LD30.pc != null && LD30.pc.world == world && LD30.pc.position.dst2(position) < 200 * 200 && LD30.pc.hp < LD30.pc.getMaxHP()) {
			Vector2 d = LD30.pc.position.cpy().sub(position);
			LD30.batch.setColor(Color.GREEN);
			LD30.batch.draw(LD30.a.getSprite("ray"), getPosition().x, getPosition().y, 0, .5f, 1, 1, d.len(), 64f / LD30.METER_SCALE, d.angle());
		}
	}

	@Override
	public void update() {
		super.update();

		firstMessageTimer++;
		heyDialogTimer++;

		if (firstMessageTimer == 40)
			this.addDialogue("Excellent, this dimension looks ideal for gem mining", new Vector2(40, 0), 240);

		if (firstMessageTimer == 180)
			this.addDialogue("You've said it", new Vector2(35, 0), 120);

		if (heyDialogTimer == 50)
			this.addDialogue("Hey!!!", new Vector2(30, 0), 120);

		if (heyDialogTimer == 250)
			this.addDialogue("All stations, an unknown ship has stolen a ship component", new Vector2(25, 0), 240);

		if (heyDialogTimer == 350)
			this.addDialogue("Please report anything missing", new Vector2(20, 0), 200);

		if (heyDialogTimer == 400)
			this.addDialogue("Tow beam, nothing missing", new Vector2(40, -50), 200);

		if (heyDialogTimer == 410)
			this.addDialogue("Gem processing, nothing missing", new Vector2(30, 50), 200);

		if (heyDialogTimer == 550)
			this.addDialogue("Engine?", new Vector2(40, 0), 120);

		if (heyDialogTimer == 650)
			this.addDialogue("Engine? Damnit!", new Vector2(35, 0), 120);

		if (heyDialogTimer == 800) {
			this.addDialogue("Exploration ship, please see if you can retrieve our engine.", new Vector2(30, 0), 220);
			firstSpawnPossible = true;
		}

		if (heyDialogTimer == 820)
			this.addDialogue("And gems!!! ... If you want upgrades", new Vector2(25, 0), 220);

		if (startTime < 140) {
			body.setLinearVelocity(0, 15);
		} else {
			body.setLinearVelocity(0, 0);
		}

		startTime++;

		LD30.respawnTimer--;

		if (LD30.pc == null && (firstSpawnPossible || true)) {

			if (LD30.respawnTimer <= 0) {
				PlayerMiniShip pc = new PlayerMiniShip();
				LD30.pc = pc;
				pc.setPosition(new Vector2(0, 90));
				world.addActor(pc);
			}
		}

		if (LD30.pc != null && LD30.pc.world == world && LD30.pc.position.dst2(position) < 200 * 200 && LD30.pc.hp < LD30.pc.getMaxHP()) {
			LD30.pc.hp = Math.min(LD30.pc.hp + 0.005f, LD30.pc.getMaxHP());

		}
	}

}
