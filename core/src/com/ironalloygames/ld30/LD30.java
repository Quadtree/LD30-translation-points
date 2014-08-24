package com.ironalloygames.ld30;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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

	public static int respawnTimer = 360;
	public static ShapeRenderer sr;

	public static ArrayList<World> worlds = new ArrayList<World>();
	World currentWorld;

	Texture img;

	float r = 0;

	ArrayList<Vector2> spaceDust = new ArrayList<Vector2>();

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		sr = new ShapeRenderer();

		a = new Assets();

		// pc = new PlayerMiniShip();
		// pc.setPosition(new Vector2(40, 40));

		worlds.add(new StartWorld());
		worlds.add(new FireWorld());
		worlds.add(new Happyspace());
		worlds.add(new Hyperspace());
		worlds.add(new Subspace());
		worlds.add(new WaterWorld());
		worlds.add(new NegativeWorld());

		currentWorld = worlds.get(0);
		// currentWorld.addActor(pc);

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
		cam.position.y = 50;
		cam.update();

		createSpaceDust();
	}

	public void createSpaceDust() {
		for (int i = 0; i < 100; i++) {
			if (spaceDust.size() - 1 < i) {
				spaceDust.add(new Vector2(0, 0));
			}
			spaceDust.get(i).set(cam.position.x + MathUtils.random(-200, 200), cam.position.x + MathUtils.random(-200, 200));
		}
	}

	@Override
	public void render() {

		for (World w : worlds) {
			w.updateIfNeeded();
		}

		if (pc != null && pc.world != null)
			currentWorld = pc.world;

		currentWorld.renderBackground();

		if (pc != null) {
			cam.position.x = pc.getPosition().x;
			cam.position.y = pc.getPosition().y;
			cam.update();
		}
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		Sprite spaceDustSprite = a.getSprite("space_dust");
		Color c = currentWorld.getColor();
		c.a = 0.25f;
		batch.setColor(c);
		Vector2 pcVel = new Vector2(0, 0);

		if (pc != null)
			pcVel = pc.velocity.cpy();

		for (Vector2 v2 : spaceDust) {
			if (Math.abs(v2.x - cam.position.x) > 200 || Math.abs(v2.y - cam.position.y) > 200) {
				if (MathUtils.randomBoolean()) {
					v2.x = MathUtils.random(-200, 200) + cam.position.x;
					v2.y = MathUtils.random(0, 1) * 400 - 200 + cam.position.y;
				} else {
					v2.x = MathUtils.random(0, 1) * 400 - 200 + cam.position.x;
					v2.y = MathUtils.random(-200, 200) + cam.position.y;
				}
			}
			batch.draw(spaceDustSprite, v2.x, v2.y, .5f, .5f, 1, 1, Math.max(pcVel.len() / 16, 1), 1, pcVel.angle());
		}

		currentWorld.render();

		batch.end();

		if (pc != null)
			pc.renderUI();
	}
}
