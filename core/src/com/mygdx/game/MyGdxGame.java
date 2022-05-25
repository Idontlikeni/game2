package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.*;

import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private SpriteBatch batch2;
	private OrthographicCamera camera;
	private Texture img;
	private Sound dropSound;
	private Sound[] shootSounds;
	private Sound[] explosionSounds;
	private Music rainMusic;
	private Array<Rectangle> raindrops;
	private Array<Asteroid> asteroids;
	private Array<Bullet> bullets;
	private Array<ColorButton> buttons;
	private long lastDropTime;
	private Texture dropImage;
	private Texture bucketImage;
	private Texture shd;
	private ShapeRenderer shapeRenderer;
	private ShaderProgram shader;
	private boolean touchFlag;
	private boolean clickFlag;
	private boolean charged;
	private float angle;
	private float temp;
	private BitmapFont font; //or use alex answer to use custom font
	private String currentColor = "white";
	private float dist;
	private int lives;
	private Viewport viewport;
	private Stage stage;
	private int score;
	private int a;
	public void endGame(){

	}

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		//viewport = new FillViewport(240, 400, camera);


		shd = new Texture(Gdx.files.internal("shader_back3.png"));

		ShaderProgram.pedantic = false;
		shader = new ShaderProgram(
				Gdx.files.internal("shaders/default.vert"),
				Gdx.files.internal("shaders/shader.frag"));
		if (!shader.isCompiled()) {
			System.err.println(shader.getLog());
			System.exit(0);
		}

		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		batch2 = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("font2.fnt")); //or use alex answer to use custom font
		batch.setShader(shader);

		shootSounds = new Sound[]{Gdx.audio.newSound(Gdx.files.internal("Laser_Shoot18.wav")),
									Gdx.audio.newSound(Gdx.files.internal("Laser_Shoot21.wav")),
									Gdx.audio.newSound(Gdx.files.internal("Laser_Shoot25.wav"))};
		explosionSounds = new Sound[]{Gdx.audio.newSound(Gdx.files.internal("Laser_Shoot18.wav")),
				Gdx.audio.newSound(Gdx.files.internal("Laser_Shoot21.wav")),
				Gdx.audio.newSound(Gdx.files.internal("Laser_Shoot25.wav"))};
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("resonance.mp3"));

		a = 0;
		lives = 3;
		score = 0;
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
		if(lives <= 0){
			lives = 3;
			asteroids.clear();
			bullets.clear();
			currentColor = "white";
			score = 0;
		}
		ScreenUtils.clear(0, 0, 0, 1);

		camera.update();
		//viewport.update(480, 800);
		float cx = (float)Math.cos(angle) * 100 + 240;
		float cy = (float)Math.sin(angle) * 100 + 250;
		float bx = (float)Math.cos(angle) * dist * 1.5f + 240;
		float by = (float)Math.sin(angle) * dist * 1.5f + 250;
		batch.setProjectionMatrix(camera.combined);
		batch2.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);

		shader.begin(); // bind shader
		shader.setUniformf("u_resolution", new Vector2(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		a = (int)TimeUtils.millis();
		shader.setUniformf("u_time", ((float)a) / 1000);
		//shader.setUniformf("u_time", 1.0f);
		shader.end(); //unbind shader

/*		shader.begin(); // bind shader
//set u_resolution
		//shader.setUniformf("u_resolution", new Vector2(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
		shader.setUniformf("u_time", 1.0f);
		shader.end(); //unbind shader*/

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		for(Bullet bullet: bullets) {
			switch (bullet.getColor()) {
				case "green":
					shapeRenderer.setColor(1, 1, 1, 1);
					break;
				case "blue":
					shapeRenderer.setColor((float) (85.0/255.0), 1, 1, 1);
					break;
				case "red":
					shapeRenderer.setColor(1, (float) (85.0/255.0), 1, 1);
					break;
				case "white":
					shapeRenderer.setColor(1, 1, 1, 1);
					break;
			}
			shapeRenderer.circle(bullet.x, bullet.y, 20);
		}
		for(Asteroid asteroid: asteroids) {
			switch (asteroid.getColor()) {
				case "green":
					shapeRenderer.setColor(1, 1, 1, 1);
					break;
				case "blue":
					shapeRenderer.setColor((float) (85.0/255.0), 1, 1, 1);
					break;
				case "red":
					shapeRenderer.setColor(1, (float) (85.0/255.0), 1, 1);
					break;
				case "white":
					shapeRenderer.setColor(1, 1, 1, 1);
					break;
			}
			shapeRenderer.circle(asteroid.x, asteroid.y, asteroid.radius);

			switch (asteroid.chcolor) {
				case "green":
					shapeRenderer.setColor(1, 1, 1, 1);
					break;
				case "blue":
					shapeRenderer.setColor((float) (85.0/255.0), 1, 1, 1);
					break;
				case "red":
					shapeRenderer.setColor(1, (float) (85.0/255.0), 1, 1);
					break;
				case "white":
					shapeRenderer.setColor(1, 1, 1, 1);
					break;
			}
			shapeRenderer.circle(asteroid.x, asteroid.y, asteroid.radius / asteroid.change * asteroid.chng);

		}
		for(ColorButton button: buttons) {
			shapeRenderer.setColor(1, 1, 1, 1);
			shapeRenderer.rect(button.x - 2, button.y - 2, button.width + 4, button.height + 4);

			switch (button.getColor()) {
				case "green":
					shapeRenderer.setColor(1, 1, 1, 1);
					break;
				case "blue":
					shapeRenderer.setColor((float) (85.0/255.0), 1, 1, 1);
					break;
				case "red":
					shapeRenderer.setColor(1, (float) (85.0/255.0), 1, 1);
					break;
				case "white":
					shapeRenderer.setColor(1, 1, 1, 1);
					break;
				/*case "green":
					shapeRenderer.setColor(0, 1, 0, 1);
					*//*shapeRenderer.setColor(0, 0.8f, 0.1f, 1);
					shapeRenderer.rect(button.x, button.y, button.width, button.height / 4);*//*
					break;
				case "blue":
					shapeRenderer.setColor(0, 0, 1, 1);
					*//*shapeRenderer.setColor(0.1f, 0, 0.8f, 1);
					shapeRenderer.rect(button.x, button.y, button.width, button.height / 4);*//*
					break;
				case "red":
					shapeRenderer.setColor(1, 0, 0, 1);

					*//*shapeRenderer.setColor(0.8f, 0, 0.1f, 1);
					shapeRenderer.rect(button.x, button.y, button.width, button.height / 4);*//*
					break;*/
			}
			shapeRenderer.rect(button.x, button.y, button.width, button.height);
		}

		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.line(240, 250, bx, by);

		switch (currentColor) {
			case "green":
				shapeRenderer.setColor(1, 1, 1, 1);
				break;
			case "blue":
				shapeRenderer.setColor((float) (85.0/255.0), 1, 1, 1);
				break;
			case "red":
				shapeRenderer.setColor(1, (float) (85.0/255.0), 1, 1);
				break;
			case "white":
				shapeRenderer.setColor(1, 1, 1, 1);
				break;
		}
		shapeRenderer.circle(240, 250, 60);
		shapeRenderer.setColor((int)((4 - lives) * 0.5f), (int)(lives * 0.5f), 0, 1);
		//shapeRenderer.setColor(1 , 1, 0, 1);
		shapeRenderer.rect(15, 780, lives * 100, 10);
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.rect(0, 180, 480, 10);
		shapeRenderer.end();
		batch.begin();
		batch.draw(shd, 0, 0, 480, 800);
		//font.draw(batch, Integer.toString(score), 220, 500);
		batch.end();

		batch2.begin();
		font.draw(batch2, Integer.toString(score), 210, 500);
		batch2.end();

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
					shootSounds[MathUtils.random(0, 2)].play();
				}
			}
			touchFlag = false;
		}

		//if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();
		if(TimeUtils.millis() - lastDropTime > MathUtils.random(1200, 6000)) spawnAsteroid();

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
			if(asteroid.y - asteroid.radius <= 200) {
				iter.remove();
				lives--;
			}
			/*if(raindrop.overlaps(bucket)) {
				dropSound.play();
				iter.remove();
			}*/
		}

		for (Iterator<Bullet> iter = bullets.iterator(); iter.hasNext(); ) {
			Bullet bullet = iter.next();
			boolean[] bool = bullet.move(asteroids);
			if(bool[0]){
				iter.remove();
				explosionSounds[MathUtils.random(0, 2)].play();
			}
			if(bool[1])score++;
			if(bullet.y + bullet.circle.radius < 0 || bullet.y + bullet.circle.radius > 800 || bullet.x + bullet.circle.radius < 0 || bullet.x + bullet.circle.radius > 480) iter.remove();

		}
	}
	
	@Override
	public void dispose () {
		shapeRenderer.dispose();
		shader.dispose();
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}

	private void spawnAsteroid() {
		String[] arr = {"red", "green", "blue"};
		Asteroid asteroid = new Asteroid(MathUtils.random(0, 480-64), 800, (float)MathUtils.random(15, 60), arr[MathUtils.random(0, 2)], (float)MathUtils.random(80, 150), MathUtils.random(300, 1000) > 850 - 2 * score);
		asteroids.add(asteroid);
		lastDropTime = TimeUtils.millis();
	}
}
