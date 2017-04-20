package com.unimelb.swen30006.metromadness.stations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.CargoTrain;
import com.unimelb.swen30006.metromadness.trains.Train;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Nate Bhurinat W. (@natebwangsut | nate.bwangsut@gmail.com)
 * https://github.com/natebwangsut
 */

public class CargoStation extends ActiveStation {

    private static Logger logger = LogManager.getLogger();

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

    public void render(ShapeRenderer renderer){
        float radius = RADIUS;
        for(int i=0; (i<this.lines.size() && i<MAX_LINES); i++){
            Line l = this.lines.get(i);
            renderer.setColor(l.lineColour);
            renderer.rect(this.position.x, this.position.y, radius*2, radius*2);
            //renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);
            radius = radius - 1;
        }

        // Calculate the percentage
        float t = this.trains.size()/(float)PLATFORMS;
        Color c = Color.WHITE.cpy().lerp(Color.DARK_GRAY, t);
        if(this.waiting.size() > 0){
            c = Color.RED;
        }

        renderer.setColor(c);
        renderer.rect(this.position.x+1, this.position.y+1, radius*2, radius*2);
        //renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);
    }

}
