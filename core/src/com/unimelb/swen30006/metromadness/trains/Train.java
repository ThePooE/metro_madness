package com.unimelb.swen30006.metromadness.trains;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.tracks.Track;

public class Train {

    // Logger
    private static Logger logger = LogManager.getLogger();

    // The state that a train can be in
    public enum State {
        IN_STATION,
        READY_DEPART,
        ON_ROUTE,
        WAITING_ENTRY,
        SKIPPING_STATION,
        FROM_DEPOT
    }

    // Constants
    public static final int     MAX_TRIPS       = 4;

    public static final Color   FORWARD_COLOUR  = Color.ORANGE;
    public static final Color   BACKWARD_COLOUR = Color.VIOLET;

    public static final float   TRAIN_WIDTH     = 4;
    public static final float   TRAIN_LENGTH    = 6;
    public static final float   TRAIN_SPEED     = 50f;

    // The train's name
    public String name;

    // The line that this is traveling on
    public Line trainLine;
    public int stationsOnLine;

    // Passenger Information
    public ArrayList<Passenger> passengers;
    public float departureTimer;

    // Station and track and position information
    public Station station;
    public Track track;
    public Point2D.Float pos;
    public Station nextStation;

    // Direction and direction
    public boolean forward;
    public State state;

    // State variables
    public int numTrips;
    public boolean disembarked;


    public State previousState = null;

    /**
     * Constructor
     */
    public Train(Line trainLine, Station start, boolean forward, String name){
        this.trainLine = trainLine;
        this.stationsOnLine = trainLine.stations.size();
        this.station = start;
        this.state = State.FROM_DEPOT;
        this.forward = forward;
        this.passengers = new ArrayList<Passenger>();
        this.name = name;
    }

    public void update(float delta) {
        // Update all passengers
        for (Passenger p : this.passengers) {
            p.update(delta);
        }
        boolean hasChanged = false;
        if (previousState == null || previousState != this.state) {
            previousState = this.state;
            hasChanged = true;
        }

        // Update the state
        switch (this.state) {
            case FROM_DEPOT:

                if (hasChanged) {
                    logger.info(this.name + "(" + this.getClass().getSimpleName()
                            +") is travelling from the depot: " + this.station.name + " Station...");
                }

                // We have our station initialized we just need to retrieve the next track, enter the
                // current station officially and mark as in station
                try {
                    if (this.station.canEnter(this.trainLine)) {
                        this.station.enter(this);
                        this.pos = (Point2D.Float) this.station.position.clone();
                        this.state = State.IN_STATION;
                        this.disembarked = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case IN_STATION:
                if (hasChanged) {
                    logger.info(this.name + "(" + this.getClass().getSimpleName()
                            +") is in " + this.station.name + " Station.");
                }

                // When in station we want to disembark passengers
                // and wait 10 seconds for incoming passengers
                if (!this.disembarked) {
                    this.disembark();
                    this.departureTimer = this.station.getDepartureTime();
                    this.disembarked = true;
                } else {
                    // Count down if departure timer.
                    if (this.departureTimer > 0) {
                        this.departureTimer -= delta;
                    } else {
                        // We are ready to depart, find the next track and wait until we can enter
                        try {
                            boolean endOfLine = this.trainLine.endOfLine(this.station);
                            if (endOfLine) {
                                this.forward = !this.forward;
                            }
                            this.track = this.trainLine.nextTrack(this.station, this.forward);
                            this.state = State.READY_DEPART;
                            break;
                        } catch (Exception e) {
                            // Massive error.
                            return;
                        }
                    }
                }
                break;
            case READY_DEPART:
                if (hasChanged) {
                    logger.info(this.name + "(" + this.getClass().getSimpleName()
                            +") is ready to depart from " + this.station.name + " Station!");
                }

                // When ready to depart, check that the track is clear and if
                // so, then occupy it if possible.
                if (this.track.canEnter(this.forward)) {
                    try {
                        /** // Find the next
                        Station next = this.trainLine.nextStation(this.station, this.forward);
                        // Depart our current station
                        this.station.depart(this);
                        this.station = next;*/

                        Station next = null;
                        Station destination = null;

                        // Cargo Trains looking for next Cargo Station on line
                        if(this instanceof CargoTrain){
                            
                            if(this.trainLine.earlyEndOfCargoLine(this.station)){
                                this.forward = !this.forward;
                            }
                            
                            destination = this.trainLine.nextCargoStation(this.station, this.forward);
                            
                            if(destination != null){
                                next = this.trainLine.nextStation(this.station, this.forward);
                            }
                        }
                        // Passenger Train looking for next station on line
                        else {
                            next = this.trainLine.nextStation(this.station, this.forward);
                        }

                        if(next != null){
                            this.station.depart(this);
                            this.station = next;
                            this.track.enter(this);
                            this.state = State.ON_ROUTE;
                        }

                        // If a Cargo Train could not find another Cargo Station on line, stay in station
                        else {
                            this.state = State.IN_STATION;
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                }
                break;
            case ON_ROUTE:
                if (hasChanged) {
                    logger.info(this.name + "(" + this.getClass().getSimpleName()
                            +") enroute to " + this.station.name + " Station!");
                }

                // Checkout if we have reached the new station
                // Nate: Simulation smoothness
                if (this.pos.distance(this.station.position) < 5) {
                    this.state = State.WAITING_ENTRY;
                    try {
                        if (!this.station.compatible(this)) {
                            this.state = State.SKIPPING_STATION;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    move(delta);
                }
                break;
            case WAITING_ENTRY:
                if (hasChanged) {
                    logger.info(this.name + "(" + this.getClass().getSimpleName()
                            +") is awaiting entry " + this.station.name + " Station..!");
                }

                // Waiting to enter, we need to check the station has room and if so
                // then we need to enter, otherwise we just wait
                try {
                    if (this.station.canEnter(this.trainLine) && this.station.compatible(this)) {
                        this.track.leave(this);
                        this.pos = (Point2D.Float) this.station.position.clone();
                        this.station.enter(this);
                        this.state = State.IN_STATION;
                        this.disembarked = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SKIPPING_STATION:
                if (hasChanged) {
                    logger.info(this.name + "(" + this.getClass().getSimpleName()
                            +") is skipping " + this.station.name + " Station..!");
                }

                this.track.leave(this);
                this.pos = (Point2D.Float) this.station.position.clone();

                try {
                    boolean endOfLine = this.trainLine.endOfLine(this.station);
                    if (endOfLine) {
                        this.forward = !this.forward;
                    }
                    // Find the next track
                    this.track = this.trainLine.nextTrack(this.station, this.forward);
                    this.track.enter(this);

                    // Find the next station
                    this.station = this.trainLine.nextStation(this.station, this.forward);
                    this.state = State.ON_ROUTE;
                } catch (Exception e) {
                    // Error
                    return;
                }
                break;
        }
    }

    public void move(float delta){
        // Work out where we're going
        float angle = angleAlongLine(
                this.pos.x,
                this.pos.y,
                this.station.position.x,
                this.station.position.y);
        float newX = this.pos.x + (float)(Math.cos(angle) * delta * TRAIN_SPEED);
        float newY = this.pos.y + (float)(Math.sin(angle) * delta * TRAIN_SPEED);
        this.pos.setLocation(newX, newY);
    }

    public void embark(Passenger p) throws Exception {
        throw new Exception();
    }

    public ArrayList<Passenger> disembark(){
        ArrayList<Passenger> disembarking = new ArrayList<Passenger>();
        Iterator<Passenger> iterator = this.passengers.iterator();
        while(iterator.hasNext()){
            Passenger p = iterator.next();
            if(this.station.shouldLeave(p)){
                logger.info("Passenger " + p.id + " is disembarking at " + this.station.name
                        + ", travel time = " + p.travelTime);
                disembarking.add(p);
                iterator.remove();
            }
        }
        return disembarking;
    }

    @Override
    public String toString() {
        return "Train [line=" + this.trainLine.name +", departureTimer=" + departureTimer
                + ", pos=" + pos + ", forward=" + forward + ", state=" + state
                + ", numTrips=" + numTrips + ", disembarked=" + disembarked + "]";
    }

    public boolean inStation(){
        return (this.state == State.IN_STATION || this.state == State.READY_DEPART);
    }

    public float angleAlongLine(float x1, float y1, float x2, float y2){
        return (float) Math.atan2((y2-y1),(x2-x1));
    }

    public void render(ShapeRenderer renderer){
        if(!this.inStation()){
            Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
            renderer.setColor(col);
            renderer.circle(this.pos.x, this.pos.y, TRAIN_WIDTH);
        }
    }

    public void renderPassengers(SpriteBatch b, BitmapFont header, boolean passengerShow){
        if(!this.inStation() && passengerShow){
            b.begin();
            header.getData().setScale(1f);
            header.draw(b, Integer.toString(this.passengers.size()), this.pos.x+10, this.pos.y+10);
            b.end();
        }
    }

    public void renderName(SpriteBatch b, BitmapFont header, boolean train){
        if(train){
            b.begin();
            header.getData().setScale(1f);
            header.draw(b, this.name, this.pos.x-10, this.pos.y-10);
            b.end();
        }
    }
}
