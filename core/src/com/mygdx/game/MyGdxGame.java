package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Texture img;
	private Sound dropSound;
	private Music rainMusic;
	private Array<Rectangle> raindrops;
	private Array<Asteroid> asteroids;
	private Array<Bullet> bullets;
	private Array<ColorButton> buttons;
	private long lastDropTime;
	private Texture dropImage;
	private Texture bucketImage;
	private ShapeRenderer shapeRenderer;
	private boolean touchFlag;
	private boolean clickFlag;
	private boolean charged;
	private float angle;
	private float temp;
	private String currentColor = "white";
	private float dist;
	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		dropImage = new Texture(Gdx.files.internal("drop-1.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket-1.png"));

		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		touchFlag = false;
		clickFlag = false;
		charged = false;
		// start the playback of the background music immediately
		rainMusic.setLooping(true);
		rainMusic.play();

		raindrops = new Array<Rectangle>();
		bullets = new Array<Bullet>();
		asteroids = new Array<Asteroid>();
		buttons = new Array<ColorButton>();
		buttons.add(new ColorButton(15, 15, 140, 80, "green"));
		buttons.add(new ColorButton(170, 15, 140, 80, "red"));
		buttons.add(new ColorButton(325, 15, 140, 80, "blue"));
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		float cx = (float)Math.cos(angle) * 100 + 240;
		float cy = (float)Math.sin(angle) * 100 + 250;
		float bx = (float)Math.cos(angle) * dist * 1.5f + 240;
		float by = (float)Math.sin(angle) * dist * 1.5f + 250;
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		switch (currentColor) {
			case "green":
				shapeRenderer.setColor(0, 1, 0, 1);
				break;
			case "blue":
				shapeRenderer.setColor(0, 0, 1, 1);
				break;
			case "red":
				shapeRenderer.setColor(1, 0, 0, 1);
				break;
		}
		shapeRenderer.circle(240, 250, 60);
		for(Bullet bullet: bullets) {
			switch (bullet.getColor()) {
				case "green":
					shapeRenderer.setColor(0, 1, 0, 1);
					break;
				case "blue":
					shapeRenderer.setColor(0, 0, 1, 1);
					break;
				case "red":
					shapeRenderer.setColor(1, 0, 0, 1);
					break;
			}
			shapeRenderer.circle(bullet.x, bullet.y, 20);
		}
		for(Asteroid asteroid: asteroids) {
			switch (asteroid.getColor()) {
				case "green":
					shapeRenderer.setColor(0, 1, 0, 1);
					break;
				case "blue":
					shapeRenderer.setColor(0, 0, 1, 1);
					break;
				case "red":
					shapeRenderer.setColor(1, 0, 0, 1);
					break;
			}
			shapeRenderer.circle(asteroid.x, asteroid.y, asteroid.radius);
		}
		for(ColorButton button: buttons) {
			shapeRenderer.setColor(1, 1, 1, 1);
			shapeRenderer.rect(button.x - 2, button.y - 2, button.width + 4, button.height + 4);

			switch (button.getColor()) {
				case "green":
					shapeRenderer.setColor(0, 1, 0, 1);
					shapeRenderer.rect(button.x, button.y, button.width, button.height);
					shapeRenderer.setColor(0, 0.8f, 0.1f, 1);
					shapeRenderer.rect(button.x, button.y, button.width, button.height / 4);
					break;
				case "blue":
					shapeRenderer.setColor(0, 0, 1, 1);
					shapeRenderer.rect(button.x, button.y, button.width, button.height);
					shapeRenderer.setColor(0.1f, 0, 0.8f, 1);
					shapeRenderer.rect(button.x, button.y, button.width, button.height / 4);
					break;
				case "red":
					shapeRenderer.setColor(1, 0, 0, 1);
					shapeRenderer.rect(button.x, button.y, button.width, button.height);
					shapeRenderer.setColor(0.8f, 0, 0.1f, 1);
					shapeRenderer.rect(button.x, button.y, button.width, button.height / 4);
					break;
			}

		}
		shapeRenderer.line(240, 250, bx, by);
		shapeRenderer.end();
		batch.begin();
		for(Rectangle raindrop: raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}

		batch.end();

		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			dist = (float) Math.sqrt((touchPos.x - 240) * (touchPos.x - 240) + (touchPos.y - 250) * (touchPos.y - 250));
			if(!touchFlag){
				for (ColorButton button : buttons) {
					if(button.rectangle.contains(touchPos.x, touchPos.y)){
						currentColor = button.color;
						String a, b;
					}
				}
				charged = dist <= 60;
			}
			touchFlag = true;
			if(charged)	angle = (float) (Math.atan2(touchPos.y - 250, touchPos.x - 240) + Math.PI);
		}else{
			if(touchFlag){
				if(charged) {
					bullets.add(new Bullet(cx, cy, angle, currentColor, dist * 2));
				}
			}
			touchFlag = false;
		}

		//if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();
		if(TimeUtils.millis() - lastDropTime > MathUtils.random(1500, 7000)) spawnAsteroid();

		/*for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0) iter.remove();
			if(raindrop.overlaps(bucket)) {
				dropSound.play();
				iter.remove();
			}
		}*/

		for (Iterator<Asteroid> iter = asteroids.iterator(); iter.hasNext(); ) {
			Asteroid asteroid = iter.next();
			asteroid.move();
			if(asteroid.y + 64 < 0) {
				iter.remove();
			}
			/*if(raindrop.overlaps(bucket)) {
				dropSound.play();
				iter.remove();
			}*/
		}

		for (Iterator<Bullet> iter = bullets.iterator(); iter.hasNext(); ) {
			Bullet bullet = iter.next();
			if(bullet.move(asteroids)) iter.remove();
			if(bullet.y + 64 < 0) iter.remove();
		}
	}
	
	@Override
	public void dispose () {
		shapeRenderer.dispose();
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}

	private void spawnAsteroid() {
		String[] arr = {"red", "green", "blue"};
		Asteroid asteroid = new Asteroid(MathUtils.random(0, 480-64), 800, (float)MathUtils.random(15, 60), arr[MathUtils.random(0, 2)], (float)MathUtils.random(80, 150));
		asteroids.add(asteroid);
		lastDropTime = TimeUtils.millis();
	}
}
