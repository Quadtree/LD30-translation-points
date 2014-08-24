package com.ironalloygames.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ironalloygames.ld30.world.World;

public class PlayerMiniShip extends MiniShip implements InputProcessor {

	public static final int GEM_COST = 4;

	boolean braking = false;
	Vector2 currentMousePos = new Vector2();

	Vector2 currentMouseScreenPos = new Vector2();
	boolean inPurchaseRange = false;

	boolean strafeLeft = false;

	boolean strafeRight = false;

	boolean thrusting = false;

	OrthographicCamera uiCamera = new OrthographicCamera(1024, 768);

	public PlayerMiniShip() {
		LD30.pc = this;
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void destroyed() {
		super.destroyed();

		LD30.pc = null;
		if (Gdx.input.getInputProcessor() == this)
			Gdx.input.setInputProcessor(null);

		LD30.respawnTimer = 240;
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);
	}

	@Override
	public boolean keyDown(int keycode) {

		if (LD30.anyKeyPressed())
			return false;

		if (keycode == Keys.UP || keycode == Keys.W)
			thrusting = true;

		if (keycode == Keys.DOWN || keycode == Keys.S)
			braking = true;

		if (keycode == Keys.LEFT || keycode == Keys.A)
			strafeLeft = true;

		if (keycode == Keys.RIGHT || keycode == Keys.D)
			strafeRight = true;

		if (inPurchaseRange && LD30.mothership != null && LD30.mothership.gems >= GEM_COST) {
			if (keycode == Keys.NUM_1) {
				hasShieldUpgrade = true;
				LD30.mothership.gems -= GEM_COST;
			}
			if (keycode == Keys.NUM_2) {
				hasArmorUpgrade = true;
				LD30.mothership.gems -= GEM_COST;
			}
			if (keycode == Keys.NUM_3) {
				hasAgilityUpgrade = true;
				LD30.mothership.gems -= GEM_COST;
			}
		}

		// the IWIN button. remove before release!
		if (keycode == Keys.BACKSPACE) {
			LD30.enemyMiniShip.hp = -1;
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		if (keycode == Keys.UP || keycode == Keys.W)
			thrusting = false;

		if (keycode == Keys.DOWN || keycode == Keys.S)
			braking = false;

		if (keycode == Keys.LEFT || keycode == Keys.A)
			strafeLeft = false;

		if (keycode == Keys.RIGHT || keycode == Keys.D)
			strafeRight = false;

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		setMousePos(screenX, screenY);
		return false;
	}

	private void renderRadar(Actor other, String text) {
		if (other != null && other.world == world) {
			Vector2 delta = other.position.cpy().sub(position);
			float angle = delta.angleRad();

			if (delta.len2() > 110 * 110) {

				String txt = text + " " + (int) delta.len() + "m";

				TextBounds tb = LD30.a.getFont(16).getBounds(txt);
				LD30.a.getFont(16).draw(LD30.batch, txt, MathUtils.cos(angle) * 200 - tb.width / 2, MathUtils.sin(angle) * 200 - tb.height / 2);
			}
		}
	}

	public void renderUI() {
		LD30.sr.setProjectionMatrix(uiCamera.combined);
		LD30.sr.begin(ShapeType.Filled);

		float hpBarWidth = this.hp * 100;
		float shieldBarWidth = this.shield * 100;

		LD30.sr.setColor(Color.RED);
		LD30.sr.rect(25 - 1024 / 2, 768 / 2 - 50, hpBarWidth, 25);
		LD30.sr.setColor(Color.BLUE);
		LD30.sr.rect(25 - 1024 / 2 + hpBarWidth, 768 / 2 - 50, shieldBarWidth, 25);

		LD30.sr.end();

		LD30.batch.setProjectionMatrix(uiCamera.combined);
		LD30.batch.begin();

		renderRadar(LD30.mothership, "Mothership");
		renderRadar(LD30.mothershipEngine, "Mothership Engine");

		for (int i = 0; i < LD30.mothership.gems; i++) {
			LD30.batch.setColor(Color.CYAN);
			LD30.batch.draw(LD30.a.getSprite("gem"), 25 - 1024 / 2 + 40 * i, 768 / 2 - 100);
		}

		if (inPurchaseRange) {
			LD30.a.getFont(16).setColor(Color.WHITE);
			LD30.a.getFont(16).drawMultiLine(
					LD30.batch,
					"Upgrades (all cost " + GEM_COST + " gems):\n\n" + (!hasShieldUpgrade ? "Press 1 to buy Shields\n" : "Shield upgrade already purchased\n")
					+ (!hasArmorUpgrade ? "Press 2 to buy +200% armor\n" : "Armor upgrade already purchased\n") + (!hasAgilityUpgrade ? "Press 3 to buy +50% agility\n" : "Agility upgrade already purchased\n"),
					25 - 1024 / 2, 768 / 2 - 300);
		}

		LD30.batch.end();
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	private void setMousePos(int screenX, int screenY) {

		currentMouseScreenPos.x = screenX;
		currentMouseScreenPos.y = screenY;

		updateMousePos();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		setMousePos(screenX, screenY);

		if (button == Buttons.LEFT && world.worldAbove != null) {
			world.addTranslationPoint(currentMousePos, world.worldAbove, 600);
		}

		if (button == Buttons.RIGHT && world.worldBelow != null) {
			world.addTranslationPoint(currentMousePos, world.worldBelow, 600);
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		setMousePos(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		setMousePos(screenX, screenY);
		return false;
	}

	@Override
	public void update() {
		super.update();

		inPurchaseRange = LD30.mothership.world == world && LD30.mothership.position.dst2(position) < 200 * 200;

		if (thrusting && !braking) {
			thrust = 1;
		} else if (braking) {
			thrust = -1;
		} else {
			thrust = 0;
		}

		strafe = 0;

		if (strafeLeft && !strafeRight)
			strafe = -1;
		if (!strafeLeft && strafeRight)
			strafe = 1;

		updateMousePos();
		dest = currentMousePos.cpy();

		world.pivot.set(position);
	}

	private void updateMousePos() {
		Vector3 v3 = new Vector3(currentMouseScreenPos.x / Gdx.graphics.getWidth() * 2 - 1, (Gdx.graphics.getHeight() - currentMouseScreenPos.y) / Gdx.graphics.getHeight() * 2 - 1, 0);

		v3.mul(LD30.cam.invProjectionView);

		currentMousePos.x = v3.x;
		currentMousePos.y = v3.y;

		// System.out.println(currentMousePos);
	}

}
