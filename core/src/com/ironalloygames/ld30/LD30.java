package com.ironalloygames.ld30;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ironalloygames.ld30.world.FireWorld;
import com.ironalloygames.ld30.world.StartWorld;
import com.ironalloygames.ld30.world.World;

public class LD30 extends ApplicationAdapter {
	public static Assets a;
	public static SpriteBatch batch;
	public static OrthographicCamera cam;
	public static final float METER_SCALE = 5f;
	public static PlayerMiniShip pc;

	World currentWorld;
	Texture img;

	float r = 0;

	ArrayList<World> worlds = new ArrayList<World>();

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

		FireWorld fireWorld = new FireWorld();
		currentWorld.worldAbove = fireWorld;
		fireWorld.worldBelow = currentWorld;

		worlds.add(currentWorld);
		worlds.add(fireWorld);

		currentWorld.addTranslationPoint(new Vector2(60, 60), fireWorld, Integer.MAX_VALUE);

		cam = new OrthographicCamera(1024 / METER_SCALE, 768 / METER_SCALE);
	}

	@Override
	public void render() {

		for (World w : worlds) {
			w.updateIfNeeded();
		}

		currentWorld = pc.world;

		currentWorld.renderBackground();

		cam.position.x = pc.getPosition().x;
		cam.position.y = pc.getPosition().y;
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		currentWorld.render();

		batch.end();
	}
}
