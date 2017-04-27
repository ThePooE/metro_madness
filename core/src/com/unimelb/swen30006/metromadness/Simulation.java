package com.unimelb.swen30006.metromadness;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

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

    /**
     * Render the Simulation.
     * @param renderer      Renderer to be use on each seperate class
     * @param b             Batch to draw the font on
     * @param font          Font that use to render on the simulation
     * @param station       Boolean value to show the station or not
     * @param passenger     Boolean value to show the passengers or not
     * @param waiting       Boolean value to show the waiting passengers or not
     * @param train         Boolean value to show the trains or not
     */
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
            if (train) {t.renderName(b, font, train);}
            if (passenger) {t.renderPassengers(b, font, passenger);}
        }

        for(Station s: this.stations){
            s.render(renderer);
            if (station) {s.renderName(b, font, station);}
            if (waiting) {s.renderWaiting(b, font, waiting);}
        }
    }
}
