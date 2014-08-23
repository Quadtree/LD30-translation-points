package com.ironalloygames.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PlayerMiniShip extends MiniShip implements InputProcessor {

	Vector2 currentMousePos = new Vector2();

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
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

		Vector3 v3 = new Vector3((float) screenX / Gdx.graphics.getWidth() * 2 - 1, (float) (Gdx.graphics.getHeight() - screenY) / Gdx.graphics.getHeight() * 2 - 1, 0);

		v3.mul(LD30.cam.invProjectionView);

		currentMousePos.x = v3.x;
		currentMousePos.y = v3.y;

		System.out.println(currentMousePos);
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

		dest = currentMousePos.cpy();
	}

}
