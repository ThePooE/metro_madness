package com.unimelb.swen30006.metromadness.stations;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unimelb.swen30006.metromadness.exceptions.PlatformFullException;
import com.unimelb.swen30006.metromadness.trains.CargoTrain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.trains.Train;

public class ActiveStation extends Station {
    // Logger
    private static Logger logger = LogManager.getLogger();

    private PassengerGenerator g;
    private ArrayList<Passenger> waiting;
    private float maxVolume;

    public ActiveStation(float x, float y, PassengerRouter router, String name, float maxPax) {
        super(x, y, router, name);
        this.waiting = new ArrayList<Passenger>();
        this.g = new PassengerGenerator(this, this.lines, maxPax);
        this.maxVolume = maxPax;
    }

    @Override
    // Only PassengerTrains can stop at ActiveStations
    public boolean compatible(Train t) throws Exception {
        return !(t instanceof CargoTrain);
    }

    @Override
    // Generate passengers when a train has entered the station
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
                    logger.info("Passenger "+p.getID()+" carrying "+p.getCargo().getWeight() +" kg embarking at "+this.name+" heading to "+p.getDestination().name);
                    t.embark(p);
                } catch(Exception e){
                    this.waiting.add(p);
                }
            }
        }
    }

    
    /**
     * Embarking passengers onto train
     * @param t     Train for passengers to get on
     */
    public void addWaitingPassengers(Train t){
        Iterator<Passenger> pIter = this.waiting.iterator();
        while(pIter.hasNext()){
            Passenger p = pIter.next();
            try {
                logger.info("Passenger " + p.getID() + " carrying " + p.getCargo().getWeight()
                        + " kg cargo embarking at " + this.name + " heading to "+p.getDestination().name);
                t.embark(p);
                pIter.remove();
            } catch (Exception e){
                // Do nothing, already waiting
                break;
            }
        }
    }

    
    @Override
    public void render(ShapeRenderer renderer){
        // Show a station as a rings of lines
        float radius = RADIUS;
        radius = renderRings(renderer, radius);

        // Calculate the percentage
        float t = this.trains.size()/(float)PLATFORMS;
        Color c = Color.WHITE.cpy().lerp(Color.DARK_GRAY, t);
        if(this.waiting.size() > 0){
            c = Color.RED;
        }

        renderer.setColor(c);
        renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);
    }

    @Override
    public void renderWaiting(SpriteBatch b, BitmapFont header, boolean waiting){
        if(waiting){
            b.begin();
            header.getData().setScale(1f);
            header.draw(
                    b,
                    Integer.toString(this.waiting.size()),
                    this.position.x-10,
                    this.position.y-10);
            b.end();
        }
    }
}
