package com.ironalloygames.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PlayerMiniShip extends MiniShip implements InputProcessor {

	boolean braking = false;

	Vector2 currentMousePos = new Vector2();
	Vector2 currentMouseScreenPos = new Vector2();
	boolean strafeLeft = false;

	boolean strafeRight = false;
	boolean thrusting = false;

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public boolean keyDown(int keycode) {

		if (keycode == Keys.UP || keycode == Keys.W)
			thrusting = true;

		if (keycode == Keys.DOWN || keycode == Keys.S)
			braking = true;

		if (keycode == Keys.LEFT || keycode == Keys.A)
			strafeLeft = true;

		if (keycode == Keys.RIGHT || keycode == Keys.D)
			strafeRight = true;

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
	}

	private void updateMousePos() {
		Vector3 v3 = new Vector3(currentMouseScreenPos.x / Gdx.graphics.getWidth() * 2 - 1, (Gdx.graphics.getHeight() - currentMouseScreenPos.y) / Gdx.graphics.getHeight() * 2 - 1, 0);

		v3.mul(LD30.cam.invProjectionView);

		currentMousePos.x = v3.x;
		currentMousePos.y = v3.y;

		// System.out.println(currentMousePos);
	}

}
