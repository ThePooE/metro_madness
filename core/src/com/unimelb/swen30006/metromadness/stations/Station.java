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

    protected static final int PLATFORMS=2;

    protected Point2D.Float position;
    protected static final float RADIUS=10;
    protected static final int NUM_CIRCLE_STATMENTS=100;
    protected static final int MAX_LINES=3;
    protected String name;
    protected ArrayList<Line> lines;
    protected ArrayList<Train> trains;
    protected static final float DEPARTURE_TIME = 2;
    protected PassengerRouter router;

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

    public void render(ShapeRenderer renderer){

        // Render the circle
        float radius = RADIUS;
        radius = renderRings(renderer, radius);

        // Calculate the percentage
        float t = this.trains.size()/(float)PLATFORMS;
        Color c = Color.WHITE.cpy().lerp(Color.DARK_GRAY, t);
        renderer.setColor(c);
        renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);
    }

    public float renderRings(ShapeRenderer renderer, float radius) {
        for(int i=0; (i<this.lines.size() && i<MAX_LINES); i++){
            Line l = this.lines.get(i);
            renderer.setColor(l.getLineColor());
            renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);
            radius = radius-2;
        }
        return radius;
    }

    public void renderName(SpriteBatch b, BitmapFont header, boolean showStation){
        if(showStation){
            b.begin();
            header.getData().setScale(1f);
            header.draw(b, this.name, this.position.x+10, this.position.y+10);
            b.end();
        }
    }

    public void renderWaiting(SpriteBatch b, BitmapFont header, boolean waitingShow){
        // To be overwritten by stations that are in use (eg ActiveStation and CargoStation)
        // Only show passengers in those stations
    }

    public void enter(Train t) throws Exception {
        if(trains.size() >= PLATFORMS){
            throw new PlatformFullException();
        } else {
            this.trains.add(t);
        }
    }

    public void depart(Train t) throws Exception {
        if(this.trains.contains(t)){
            this.trains.remove(t);
        } else {
            throw new TrainNotFoundException();
        }
    }

    public boolean canEnter(Line l) throws Exception {
        return trains.size() < PLATFORMS;
    }

    public boolean compatible(Train t) throws Exception {
        return true;
    }

    // Returns departure time in seconds
    public float getDepartureTime() {
        return DEPARTURE_TIME;
    }

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
