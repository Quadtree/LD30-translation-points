package com.ironalloygames.ld30;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class LD30 extends ApplicationAdapter {
	public static Assets a;
	public static SpriteBatch batch;
	public static OrthographicCamera cam;
	public static final float METER_SCALE = 5f;
	public static PlayerMiniShip pc;

	World currentWorld;

	Texture img;

	float r = 0;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		a = new Assets();

		pc = new PlayerMiniShip();
		pc.setPosition(new Vector2(40, 40));

		currentWorld = new StartWorld();
		currentWorld.addActor(pc);
		currentWorld.addActor(new MiniShip());

		cam = new OrthographicCamera(1024 / METER_SCALE, 768 / METER_SCALE);
	}

	@Override
	public void render() {

		currentWorld.updateIfNeeded();

		currentWorld.renderBackground();

		// System.out.println("PP " + cam.position);
		cam.position.x = pc.getPosition().x;
		cam.position.y = pc.getPosition().y;
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		// batch.draw(a.getSprite("mini_ship"), 0, 0, .5f, .5f, 1, 1, 32, 32,
		// r);
		// r++;
		// batch.draw(a.getSprite("mini_ship"), 0, 0);

		currentWorld.render();

		batch.end();
	}
}
