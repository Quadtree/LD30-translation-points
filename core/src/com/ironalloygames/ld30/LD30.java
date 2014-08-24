package com.ironalloygames.ld30;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
	public static World currentWorld;
	public static EnemyMiniShip enemyMiniShip = null;
	public static final float METER_SCALE = 5f;

	public static Mothership mothership = null;
	public static MothershipEngine mothershipEngine = null;

	public static boolean needToCreateSpaceDust = false;

	public static PlayerMiniShip pc;
	public static int respawnTimer = 0;

	static ArrayList<Vector2> spaceDust = new ArrayList<Vector2>();
	public static ShapeRenderer sr;

	public static OrthographicCamera uiCamera;

	public static boolean victoryDialogEverShown = false;

	static boolean victoryDialogShown = false;

	public static ArrayList<World> worlds = new ArrayList<World>();

	public static boolean anyKeyPressed() {
		if (victoryDialogShown) {
			victoryDialogShown = false;
			return true;
		}

		return false;
	}

	public static void createSpaceDust() {
		System.out.println("Creating space dust");
		for (int i = 0; i < 100; i++) {
			if (spaceDust.size() - 1 < i) {
				spaceDust.add(new Vector2(0, 0));
			}
			spaceDust.get(i).set(cam.position.x + MathUtils.random(-200, 200), cam.position.x + MathUtils.random(-200, 200));
		}
	}

	Music currentlyPlayingMusic = null;

	int currentMusicWorld = -1;

	Texture img;

	int noMusicTime = 0;

	float r = 0;

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

		uiCamera = new OrthographicCamera(1024, 768);

		createSpaceDust();

		a.getSound("gem");
		a.getSound("miniship_die");
		a.getSound("shoot");
		a.getSound("translation-point");
		a.getSound("cant");
		a.getSound("hit");
	};

	@Override
	public void render() {

		int currentWorldIndex = worlds.indexOf(currentWorld);

		if (currentWorldIndex != currentMusicWorld && currentlyPlayingMusic != null) {
			// System.out.println("Musical dissonance detected" +
			// currentWorldIndex + " " + currentMusicWorld);
			currentlyPlayingMusic.setVolume(currentlyPlayingMusic.getVolume() - 0.01f);

			if (currentlyPlayingMusic.getVolume() < 0.01f) {
				currentlyPlayingMusic.stop();
				currentlyPlayingMusic = null;
			}
		}

		if (currentlyPlayingMusic == null) {
			noMusicTime++;
		} else {
			noMusicTime = 0;
		}

		if (noMusicTime > 180) {
			currentlyPlayingMusic = a.getMusic("world" + currentWorldIndex);

			if (currentlyPlayingMusic != null) {
				currentlyPlayingMusic.setVolume(0.25f);
				currentlyPlayingMusic.play();
				currentMusicWorld = currentWorldIndex;
			}
		}

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

		if (needToCreateSpaceDust) {
			createSpaceDust();
			needToCreateSpaceDust = false;
		}

		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		Sprite spaceDustSprite = a.getSprite("space_dust");
		Color c = currentWorld.getColor();
		c.a = 0.5f;
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

		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();

		sr.setProjectionMatrix(uiCamera.combined);
		sr.begin(ShapeType.Filled);

		currentWorld.renderDialogue();

		sr.end();
		batch.end();

		if (pc != null)
			pc.renderUI();

		batch.begin();

		if (victoryDialogShown) {
			batch.setColor(Color.WHITE);
			batch.draw(a.getTexture("victory_dialog"), -512 / 2, -384 / 2);
		}

		batch.end();

		if (!victoryDialogEverShown) {
			if (LD30.mothership.hasEngine && !LD30.enemyMiniShip.keep()) {
				victoryDialogShown = true;
				victoryDialogEverShown = true;
			}
		}

		a.update();
	}
}
