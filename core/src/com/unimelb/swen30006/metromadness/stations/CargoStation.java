package com.unimelb.swen30006.metromadness.stations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

import java.util.ArrayList;

/**
 * Nate Bhurinat W. (@natebwangsut | nate.bwangsut@gmail.com)
 * https://github.com/natebwangsut
 */

public class CargoStation extends ActiveStation {


    public PassengerGenerator g;
    public ArrayList<Passenger> waiting;
    public float maxVolume;

    public CargoStation(float x, float y, PassengerRouter router, String name, float maxPax) {
        super(x, y, router, name, maxPax);
        this.waiting = new ArrayList<Passenger>();
        this.g = new PassengerGenerator(this, this.lines, maxPax);
        this.maxVolume = maxPax;
    }

    @Override
    public boolean compatible(Train t) throws Exception {
        return true;
    }

    @Override
    public void render(ShapeRenderer renderer){
        // Show a station as a rings of lines
        float radius = RADIUS;
        radius = renderRings(renderer, radius);

        // Calculate the percentage
        float t = this.trains.size()/(float)PLATFORMS;
        Color c = Color.ORANGE.cpy().lerp(Color.DARK_GRAY, t);

        // Show RED if there is passenger in station
        if(this.waiting.size() > 0){
            c = Color.RED;
        }

        renderer.setColor(c);
        renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);
    }
}
