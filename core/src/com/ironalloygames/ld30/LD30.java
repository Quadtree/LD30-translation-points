package com.ironalloygames.ld30;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.ironalloygames.ld30.world.FireWorld;
import com.ironalloygames.ld30.world.Happyspace;
import com.ironalloygames.ld30.world.Hyperspace;
import com.ironalloygames.ld30.world.NegativeWorld;
import com.ironalloygames.ld30.world.StartWorld;
import com.ironalloygames.ld30.world.Subspace;
import com.ironalloygames.ld30.world.WaterWorld;
import com.ironalloygames.ld30.world.World;

public class LD30 extends ApplicationAdapter {
	public static Assets a;
	public static SpriteBatch batch;
	public static OrthographicCamera cam;
	public static final float METER_SCALE = 5f;

	public static Mothership mothership = null;
	public static MothershipEngine mothershipEngine = null;

	public static PlayerMiniShip pc;

	public static ShapeRenderer sr;

	World currentWorld;
	Texture img;

	float r = 0;

	ArrayList<World> worlds = new ArrayList<World>();

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		sr = new ShapeRenderer();

		a = new Assets();

		pc = new PlayerMiniShip();
		pc.setPosition(new Vector2(40, 40));

		worlds.add(new StartWorld());
		worlds.add(new FireWorld());
		worlds.add(new Happyspace());
		worlds.add(new Hyperspace());
		worlds.add(new Subspace());
		worlds.add(new WaterWorld());
		worlds.add(new NegativeWorld());

		currentWorld = worlds.get(0);
		currentWorld.addActor(pc);

		for (int i = 0; i < worlds.size(); i++) {
			if (i < worlds.size() - 1) {
				worlds.get(i).worldAbove = worlds.get(i + 1);
			} else {
				worlds.get(i).worldAbove = worlds.get(0);
			}

			if (i > 0) {
				worlds.get(i).worldBelow = worlds.get(i - 1);
			} else {
				worlds.get(i).worldBelow = worlds.get(worlds.size() - 1);
			}
		}

		cam = new OrthographicCamera(1024 / METER_SCALE, 768 / METER_SCALE);
	}

	@Override
	public void render() {

		for (World w : worlds) {
			w.updateIfNeeded();
		}

		if (pc != null)
			currentWorld = pc.world;

		currentWorld.renderBackground();

		if (pc != null) {
			cam.position.x = pc.getPosition().x;
			cam.position.y = pc.getPosition().y;
			cam.update();
		}
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		currentWorld.render();

		batch.end();

		if (pc != null)
			pc.renderUI();
	}
}
