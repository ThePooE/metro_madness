package com.unimelb.swen30006.metromadness;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

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
    BitmapFont header;
    
    // Default settings
    boolean stationShow = true;
    boolean passengerShow = true;
    boolean waitingShow = false;

    // Default settings for showing names in simulation
    boolean stationShow = true;
    boolean passengerShow = true;
    boolean waitingShow = false;
    boolean trainShow = false;

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = viewport_width;
        camera.viewportHeight = viewport_width * (float)height/width;
        camera.update();
    }

    @Override
    public void create () {
        // Create the simulation
        sim = new Simulation("filename");

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
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Gotham-Book.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 12;
        smaller = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        FreeTypeFontGenerator headlineGen = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Gotham-Bold.ttf"));
        FreeTypeFontParameter headlineParam = new FreeTypeFontParameter();
        headlineParam.size = 40;
        header = headlineGen.generateFont(headlineParam); // font size 12 pixels
        headlineGen.dispose(); // don't forget to dispose to avoid memory leaks!

        // Setup fonts
        smaller.setColor(Color.GRAY);
        header.setColor(Color.BLACK);
    }

    @Override
    public void render () {
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
<<<<<<< HEAD
        sim.render(shapeRenderer, b, smaller, stationShow, passengerShow, waitingShow, trainShow);
=======
        sim.render(shapeRenderer, b, smaller, stationShow, passengerShow, waitingShow);
>>>>>>> 1f03d74... Extra Functions
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
        if (Gdx.input.isKeyPressed(Input.Keys.E)){
            Gdx.app.exit();
        }

        /* Zoom In-Out */
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.zoom += 0.1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)){
            camera.zoom -= 0.1;
        }

        /* Camera Directions */
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            camera.translate(-3f, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            camera.translate(3f, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            camera.translate(0, -3f, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            camera.translate(0, 3f, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
        	// Show/Hide station names
        	if(this.stationShow){
        		this.stationShow = false;
        	} else {
        		this.stationShow = true;
        	}
        	
        }
        if(Gdx.input.isKeyPressed(Input.Keys.P)){
        	// Show/Hide passengers on train
        	if(this.passengerShow){
        		this.passengerShow = false;
        	} else {
        		this.passengerShow = true;
        	}
        		
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
        	// Show/Hide passengers waiting at station
        	if(this.waitingShow){
        		this.waitingShow = false;
        	} else {
        		this.waitingShow = true;
        	}
        }

        /* Show or hide names */
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            this.stationShow = !this.stationShow;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.P)){
            this.passengerShow = !this.passengerShow;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            this.waitingShow = !this.waitingShow;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.T)){
            this.trainShow = !this.trainShow;
        }


        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, WORLD_WIDTH/camera.viewportWidth);
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, WORLD_WIDTH - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, WORLD_HEIGHT - effectiveViewportHeight / 2f);
<<<<<<< HEAD

=======
        
>>>>>>> 1f03d74... Extra Functions
    }

}
