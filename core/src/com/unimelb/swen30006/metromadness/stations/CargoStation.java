package com.unimelb.swen30006.metromadness.stations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.exceptions.PlatformFullException;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.trains.Train;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Nate Bhurinat W. (@natebwangsut | nate.bwangsut@gmail.com)
 * https://github.com/natebwangsut
 */

public class CargoStation extends ActiveStation {

    // Logger
    private static Logger logger = LogManager.getLogger();

    private PassengerGenerator g;
    private ArrayList<Passenger> waiting;
    private float maxVolume;

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

    @Override
    public void enter(Train t) throws Exception {
        if(trains.size() >= PLATFORMS){
            throw new PlatformFullException();
        } else {
            // Add the train
            this.trains.add(t);
            // Add the waiting passengers
            addWaitingPassengers(t);
            // Do not add new passengers if there are too many already
            if (this.waiting.size() > maxVolume){
                return;
            }

            // Add the new passenger
            Passenger[] ps = this.g.generatePassengers();
            for(Passenger p: ps){
                try {
                    if(p==null){
                        return;
                    }
                    logger.info("Passenger "+p.id+" carrying "+p.getCargo().getWeight() +" kg embarking at "+this.name+" heading to "+p.getDestination().name);
                    t.embark(p);
                } catch(Exception e){
                    this.waiting.add(p);
                }
            }
        }
    }
}
