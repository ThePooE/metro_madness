/**
 * SWEN30006 Software Modelling and Design
 * Semester 1, 2017
 * Project Part B - Metro Madness
 * 
 * Group 107
 * Members:
 * Nate Wangsutthitham
 * Kolatat Thangkasemvathana
 * Khai Mei Chin
 *  
 */

package com.unimelb.swen30006.metromadness.stations;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.exceptions.PlatformFullException;
import com.unimelb.swen30006.metromadness.exceptions.TrainNotFoundException;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

public class Station {

    static final int PLATFORMS=2;

    Point2D.Float position;
    static final float RADIUS=10;
    static final int NUM_CIRCLE_STATMENTS=100;
    static final int MAX_LINES=3;
    String name;
    ArrayList<Line> lines;
    ArrayList<Train> trains;
    static final float DEPARTURE_TIME = 2;
    PassengerRouter router;

    public Station(float x, float y, PassengerRouter router, String name){
        this.name = name;
        this.router = router;
        this.position = new Point2D.Float(x,y);
        this.lines = new ArrayList<Line>();
        this.trains = new ArrayList<Train>();
    }

    public void registerLine(Line l){
        this.lines.add(l);
    }
    
    public Point2D.Float getPosition(){
        return this.position;
    }
    
    public String getName(){
        return this.name;
    }
    
    // Returns departure time in seconds
    public float getDepartureTime() {
        return DEPARTURE_TIME;
    }

    /**
     * Renders the station
     * @param renderer      ShapeRenderer
     */
    public void render(ShapeRenderer renderer){

        // Render the circle
        float radius = RADIUS;
        radius = renderRings(renderer, radius);

        // Calculate the percentage
        // If there are no train in station, the circle will be white
        // If there is a train(s) in station, the circle will be 
        // an increasing darker shade of gray
        // E.g if there is one train in station, circle = light gray
        //     if two trains in station, circle = dark gray
        float t = this.trains.size()/(float)PLATFORMS;
        Color c = Color.WHITE.cpy().lerp(Color.DARK_GRAY, t);
        renderer.setColor(c);
        renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);
    }

    
    /**
     * Renders the circles that represents this station on map
     * @param renderer      ShapeRenderer
     * @param radius        radius of the circle
     * @return              the modified radius
     */
    public float renderRings(ShapeRenderer renderer, float radius) {
        
        // The number of circles drawn can represent the number of lines
        // that this station is part of (up to three lines can be visualised on map)
        for(int i=0; (i<this.lines.size() && i<MAX_LINES); i++){
            Line l = this.lines.get(i);
            renderer.setColor(l.getLineColor());
            renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);
            radius = radius-2;
        }
        return radius;
    }

    
    /**
     * Renders the name of this station on map
     * @param b             SpriteBatch
     * @param header        font used for rendered text
     * @param showStation   toggle to show/hide station name on map
     */
    public void renderName(SpriteBatch b, BitmapFont header, boolean showStation){
        if(showStation){
            b.begin();
            header.getData().setScale(1f);
            header.draw(b, this.name, this.position.x+10, this.position.y+10);
            b.end();
        }
    }

    
    /**
     * Renders the number of passengers waiting at the station
     * @param b             SpriteBatch
     * @param header        font used to render the text
     * @param waitingShow   toggle value to show/hide the number
     */
    public void renderWaiting(SpriteBatch b, BitmapFont header, boolean waitingShow){
        // To be overwritten by stations that are in use (eg ActiveStation and CargoStation)
        // Only show passengers in those stations
    }

    
    /**
     * Entry of a train into this station 
     * To be overriden in child classes
     * @param t     Train to enter this station
     * @throws Exception
     */
    public void enter(Train t) throws Exception {
        if(trains.size() >= PLATFORMS){
            throw new PlatformFullException();
        } else {
            this.trains.add(t);
        }
    }

    /**
     * Departure of a train from this station
     * @param t     Train to leave this station
     * @throws Exception
     */
    public void depart(Train t) throws Exception {
        if(this.trains.contains(t)){
            this.trains.remove(t);
        } else {
            throw new TrainNotFoundException();
        }
    }

    /**
     * Checker to see if this station has any free platform
     * @return      true if there is an unoccupied platform, otherwise false
     * @throws Exception
     */
    public boolean canEnter() throws Exception {
        return trains.size() < PLATFORMS;
    }

    /** 
     * Checker to see if a train is compatible with this type of station
     * (to be overriden in child classes)
     * @param t     Train to check for compatibility
     * @return      true if Train can enter this station, otherwise false
     * @throws Exception
     */
    public boolean compatible(Train t) throws Exception {
        return true;
    }

    
    /**
     * Checker to see if a particular passenger should disembark at this station
     * @param p     Passenger to be checked
     * @return
     */
    public boolean shouldLeave(Passenger p) {
        return this.router.shouldLeave(this, p);
    }

    @Override
    public String toString() {
        return "Station [position=" + position
                + ", name=" + name
                + ", trains=" + trains.size()
                + ", router=" + router + "]";
    }

    
    public Passenger generatePassenger(int id, Random random, Station s) {
        return new Passenger(id, random, this, s);
    }
}
