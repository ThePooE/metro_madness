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

package com.unimelb.swen30006.metromadness;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

public class Simulation {

    private ArrayList<Station> stations;
    private ArrayList<Line> lines;
    private ArrayList<Train> trains;

    public Simulation(String fileName){
        // Create a map reader and read in the file
        MapReader m = new MapReader(fileName);
        m.process();

        // Create a list of lines
        this.lines = new ArrayList<Line>();
        this.lines.addAll(m.getLines());

        // Create a list of stations
        this.stations = new ArrayList<Station>();
        this.stations.addAll(m.getStations());

        // Create a list of trains
        this.trains = new ArrayList<Train>();
        this.trains.addAll(m.getTrains());
    }


    // Update all the trains in the simulation
    public void update(){
        // Update all the trains
        for(Train t: this.trains){
            t.update(Gdx.graphics.getDeltaTime());
        }
    }

    public void render(
            ShapeRenderer renderer,
            SpriteBatch b,
            BitmapFont font,
            boolean station,
            boolean passenger,
            boolean waiting,
            boolean train) {

        for(Line l: this.lines){
            l.render(renderer);
        }

        for(Train t: this.trains){
            t.render(renderer);
            t.renderName(b, font, train);
            t.renderPassengers(b, font, passenger);
        }

        for(Station s: this.stations){
            s.render(renderer);
            s.renderName(b, font, station);
            s.renderWaiting(b, font, waiting);
        }
    }
}
