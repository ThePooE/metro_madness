package com.unimelb.swen30006.metromadness;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

/**
 * [SWEN30006] Software Modelling and Design
 * Semester 1, 2017
 * Project Part B - Metro Madness
 *
 * Group 107:
 * Nate Wangsutthitham [755399]
 * Kolatat Thangkasemvathana [780631]
 * Khai Mei Chin [755332]
 *
 */

public class MetroMadness extends ApplicationAdapter {

    // The width of the world in unitless dimensions
    static final int WORLD_WIDTH = 1200;
    static final int WORLD_HEIGHT = 1200;

    // Viewport state
    int VIEWPORT_WIDTH=200;
    float viewport_width;

    // Data for simluation, rendering and camera.
    Simulation sim;
    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;

    // Font
    BitmapFont smaller;
    BitmapFont display;
    BitmapFont header;

    // Default settings for showing names in simulation
    boolean stationShow = true;
    boolean passengerShow = true;
    boolean waitingShow = false;
    boolean trainShow = false;

    double inputDelay = 0.2; // seconds
    static float delay = 0;

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = viewport_width;
        camera.viewportHeight = viewport_width * (float)height/width;
        camera.update();
    }

    @Override
    public void create() {
        // Create the simulation
        String fileName = "maps/melbourne_cargo.xml";
        //String fileName = "maps/melbourne.xml";
        //String fileName = "maps/world.xml";
        sim = new Simulation(fileName);

        // Setup our 2D Camera
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        viewport_width = VIEWPORT_WIDTH;
        camera = new OrthographicCamera(viewport_width, viewport_width * (h / w));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        // Create our shape renderer
        shapeRenderer = new ShapeRenderer();

        // Create our font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/London-Tube.ttf"));
        //FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/London-Tube.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 16;
        smaller = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        FreeTypeFontGenerator headlineGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/London-Tube.ttf"));
        //FreeTypeFontGenerator headlineGen = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/London-Tube.ttf"));
        FreeTypeFontParameter headlineParam = new FreeTypeFontParameter();
        headlineParam.size = 50;
        header = headlineGen.generateFont(headlineParam); // font size 50 pixels
        headlineGen.dispose(); // don't forget to dispose to avoid memory leaks!

        // Setup fonts
        smaller.setColor(Color.DARK_GRAY);
        header.setColor(Color.BLACK);
    }

    @Override
    public void render() {
        // Clear the graphics to white
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle user input
        handleInput();

        // Update the simulation and camera
        camera.update();
        sim.update();

        // Render the simulation
        SpriteBatch b = new SpriteBatch();
        shapeRenderer.setProjectionMatrix(camera.combined);
        b.setProjectionMatrix(camera.combined);

        // Render all filled shapes
        shapeRenderer.begin(ShapeType.Filled);
        sim.render(shapeRenderer, b, smaller, stationShow, passengerShow, waitingShow, trainShow);
        shapeRenderer.end();

        // Begin preparations to render text
        SpriteBatch batch = new SpriteBatch();
        batch.begin();

        // Render Header
        header.getData().setScale(0.5f);
        header.draw(batch, "metro madness.", 10, Gdx.graphics.getHeight()-10);
        batch.end();
    }

    private void handleInput() {

        /* Exit Program */
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            Gdx.app.exit();
        }

        /* Zoom In-Out */
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.1;
        }

        /* Camera Directions */
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-3f, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(3f, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -3f, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 3f, 0);
        }

        /* Show or hide names */
        delay += Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (delay >= inputDelay) {
                this.stationShow = !this.stationShow;
                delay = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            if (delay >= inputDelay) {
                this.passengerShow = !this.passengerShow;
                delay = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (delay >= inputDelay) {
                this.waitingShow = !this.waitingShow;
                delay = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.T)) {
            if (delay >= inputDelay) {
                this.trainShow = !this.trainShow;
                delay = 0;
            }
        }

        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, WORLD_WIDTH / camera.viewportWidth);
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(
                camera.position.x,
                effectiveViewportWidth / 2f,
                WORLD_WIDTH - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y,
                effectiveViewportHeight / 2f,
                WORLD_HEIGHT - effectiveViewportHeight / 2f);
    }
}
