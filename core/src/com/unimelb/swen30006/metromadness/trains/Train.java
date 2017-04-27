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

public class Train {

    // Logger
    static Logger logger = LogManager.getLogger();

    // The state that a train can be in
    enum State {
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
    String name;

    // The line that this is traveling on
    Line trainLine;
    int stationsOnLine;

    // Passenger Information
    ArrayList<Passenger> passengers;
    float departureTimer;

    // Station and track and position information
    Station station;
    Station destination;
    Station next;
    Track track;
    Point2D.Float pos;

    // Direction and direction
    boolean forward;
    State state;

    // State variables
    int numTrips;
    boolean disembarked;

    State previousState = null;

    /**
     * Constructor for Train class
     * @param trainLine     Line which the Train will run
     * @param start         Station that the Train start
     * @param forward       Which way the train will wun
     * @param name          Name of this train
     */
    public Train(Line trainLine, Station start, boolean forward, String name){
        this.trainLine = trainLine;
        this.stationsOnLine = trainLine.getStations().size();
        this.station = start;
        this.destination = null;
        this.next = null;
        this.state = State.FROM_DEPOT;
        this.forward = forward;
        this.passengers = new ArrayList<Passenger>();
        this.name = name;
    }

    public boolean getForward(){
        return this.forward;
    }

    public Point2D.Float getPos(){
        return this.pos;
    }

    public ArrayList<Passenger> getPassengers(){
        return this.passengers;
    }

    /**
     * Update the Train which will in terms update passengers
     * @param delta     How long has the time changed
     */
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
                // Case at the start of simulation when the Train is starting from its depot station
                if (hasChanged) {
                    logger.info(this.name + "(" + this.getClass().getSimpleName()
                            +") is travelling from the depot: " + this.station.getName() + " Station...");
                }

                // We have our station initialized we just need to retrieve the next track, enter the
                // current station officially and mark as in station
                try {
                    if (this.station.canEnter()) {
                        this.station.enter(this);
                        this.pos = (Point2D.Float) this.station.getPosition().clone();
                        this.state = State.IN_STATION;
                        this.disembarked = false;
                        logger.info(this.name + "(" + this.getClass().getSimpleName()
                                +") is in " + this.station.getName() + " Station.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case IN_STATION:
                // Case when train is currently in a station
                if (hasChanged) {
                    logger.info(this.name + "(" + this.getClass().getSimpleName()
                            +") is in " + this.station.getName() + " Station.");
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
                        // We are ready to depart, find if the train has a valid destination to travel to
                        // (i.e for a Cargo Train, there is another Cargo Station on Line to travel to)
                        // If there is a valid destination, find the next track and wait until we can enter
                        try {

                            // Check if we are at either ends of the Line,
                            // If so, we need to change direction of Train
                            boolean endOfLine = this.trainLine.endOfLine(this.station);
                            if (endOfLine) {
                                this.forward = !this.forward;
                            }

                            // For the case of a Cargo Train
                            if(this instanceof CargoTrain){

                                // Switch direction if there are no more Cargo Stations in the current direction we are going
                                if(this.trainLine.earlyEndOfCargoLine(this.station)){
                                    this.forward = !this.forward;
                                }

                                // Check if there is a valid destination for this Cargo Train to travel to
                                destination = this.trainLine.nextCargoStation(this.station, this.forward);

                               // If there is a valid destination, find the next immediate station
                                //on this line to travel towards
                                // Note: This next station may not be a Cargo Station
                                if(destination != null){
                                    next = this.trainLine.nextStation(this.station, this.forward);
                                }
                            }

                            // For the case of a Passenger Train
                            else {
                                destination = this.trainLine.nextStation(this.station, this.forward);
                                next = destination;
                            }

                            // If there is a valid destination, find the next track
                            // Otherwise, stay in station
                            if(next != null){
                                this.track = this.trainLine.nextTrack(this.station, this.forward);
                                this.state = State.READY_DEPART;
                            }

                           break;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case READY_DEPART:
                // Case when a train is ready to leave this station
                if (hasChanged) {
                    logger.info(this.name + "(" + this.getClass().getSimpleName()
                            +") is ready to depart for " + this.next.getName() + " Station!");
                }

                // When ready to depart, check that the track is clear and if
                // so, then occupy it if possible.
                if (this.track.canEnter(this.forward)) {
                    try {

                        // When track is clear, leave the station
                        this.station.depart(this);
                        this.station = this.next;
                        this.track.enter(this);
                        this.state = State.ON_ROUTE;


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case ON_ROUTE:
                // Case where train is on the journey
                if (hasChanged) {
                    logger.info(this.name + "(" + this.getClass().getSimpleName()
                            +") enroute to " + this.station.getName() + " Station!");
                }

                // Checkout if we have reached the new station
                // Nate: Simulation smoothness
                // Distance position switched from 10 to 5
                if (this.pos.distance(this.station.getPosition()) < 5) {
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
                // Case where a train is waiting to enter a station
                if (hasChanged) {
                    logger.info(this.name + "(" + this.getClass().getSimpleName()
                            +") is awaiting entry " + this.station.getName() + " Station..!");
                }

                // Waiting to enter, we need to check the station has room and if so
                // then we need to enter, otherwise we just wait
                try {
                    if (this.station.canEnter() && this.station.compatible(this)) {
                        this.track.leave(this);
                        this.pos = (Point2D.Float) this.station.getPosition().clone();
                        this.station.enter(this);
                        this.state = State.IN_STATION;
                        this.disembarked = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case SKIPPING_STATION:
                // Case when a Cargo Station is skipping an Active Station
                if (hasChanged) {
                    logger.info(this.name + "(" + this.getClass().getSimpleName()
                            +") is skipping " + this.station.getName() + " Station..!");
                }

                this.track.leave(this);
                this.pos = (Point2D.Float) this.station.getPosition().clone();

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
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * Move the Train accodingly
     * @param delta     Time elapsed since last frame has been rendered
     */
    public void move(float delta){
        // Work out where we're going
        float angle = angleAlongLine(
                this.pos.x,
                this.pos.y,
                this.station.getPosition().x,
                this.station.getPosition().y);
        float newX = this.pos.x + (float)(Math.cos(angle) * delta * TRAIN_SPEED);
        float newY = this.pos.y + (float)(Math.sin(angle) * delta * TRAIN_SPEED);
        this.pos.setLocation(newX, newY);
    }

    /**
     * Default behaviour for passengers to enter Train,
     * will always throw Exception
     * @param p     Passengers to enter the Train
     * @throws Exception
     */
    public void embark(Passenger p) throws Exception {
        throw new Exception();
    }

    /**
     * Default behaviour for passengers to exit Train
     * @return      ArrayList of Passengers after the Passengers have disembarked
     */
    public ArrayList<Passenger> disembark(){
        ArrayList<Passenger> disembarking = new ArrayList<Passenger>();
        Iterator<Passenger> iterator = this.passengers.iterator();
        while(iterator.hasNext()){
            Passenger p = iterator.next();
            if(this.station.shouldLeave(p)){
                logger.info("Passenger " + p.getID()
                        + " is disembarking at " + this.station.getName()
                        + ", travel time = " + p.getTravelTime());
                disembarking.add(p);
                iterator.remove();
            }
        }
        return disembarking;
    }

    /**
     * Converts Train information into String
     * @return      Train's info
     */
    @Override
    public String toString() {
        return "Train [line=" + this.trainLine.getName()
                + ", departureTimer=" + departureTimer
                + ", pos=" + pos
                + ", forward=" + forward
                + ", state=" + state
                + ", numTrips=" + numTrips
                + ", disembarked=" + disembarked + "]";
    }

    public boolean inStation(){
        return (this.state == State.IN_STATION || this.state == State.READY_DEPART);
    }

    /**
     * Return the angle which the train will be using to change its position
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public float angleAlongLine(float x1, float y1, float x2, float y2){
        return (float) Math.atan2((y2-y1),(x2-x1));
    }

    /**
     * Default render algorithm for Train class
     * @param renderer      The renderer use to render into Simulation
     */
    public void render(ShapeRenderer renderer){
        if(!this.inStation()){
            Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
            renderer.setColor(col);
            renderer.circle(this.pos.x, this.pos.y, TRAIN_WIDTH);
        }
    }

    /**
     * Renders the number of passengers currently on train
     * @param b             SpriteBatch
     * @param font          font used to render the text
     * @param passenger     toggle between show/hide the number of passengers
     */
    public void renderPassengers(SpriteBatch b, BitmapFont font, boolean passenger){
        if(!this.inStation() && passenger){
            b.begin();
            font.getData().setScale(1f);
            font.draw(b, Integer.toString(this.passengers.size()), this.pos.x+10, this.pos.y+10);
            b.end();
        }
    }

    /**
     * Renders the name of this train
     * @param b         SpriteBatch
     * @param font      font used to render the text
     * @param train     toggle between show/hide the train name
     */
    public void renderName(SpriteBatch b, BitmapFont font, boolean train){
        if(train){
            b.begin();
            font.getData().setScale(1f);
            font.draw(b, this.name, this.pos.x-10, this.pos.y-10);
            b.end();
        }
    }
}
