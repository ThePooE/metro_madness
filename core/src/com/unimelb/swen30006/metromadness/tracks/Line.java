package com.unimelb.swen30006.metromadness.tracks;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger.Cargo;
import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;

public class Line {

    // The colour of this line
    public Color lineColour;
    public Color trackColour;

    // The name of this line
    public String name;
    // The stations on this line
    public ArrayList<Station> stations;

    // The cargo stations on this line
    public ArrayList<Station> cargoStations;

    // The tracks on this line between stations
    public ArrayList<Track> tracks;

    // Constant variable for checking if there is more than one station in array
    private static int MULTIPLE_STATIONS = 2;

    // Create a line
    public Line(Color stationColour, Color lineColour, String name){
        // Set the line colour
        this.lineColour = stationColour;
        this.trackColour = lineColour;
        this.name = name;

        // Create the data structures
        this.stations = new ArrayList<Station>();
        this.cargoStations = new ArrayList<Station>();
        this.tracks = new ArrayList<Track>();
    }


    public void addStation(Station s, Boolean two_way){
        // We need to build the track if this is adding to existing stations
        if(this.stations.size() > 0){
            // Get the last station
            Station last = this.stations.get(this.stations.size()-1);

            // Generate a new track
            Track t;
            if(two_way){
                t = new DualTrack(last.position, s.position, this.trackColour);
            } else {
                t = new Track(last.position, s.position, this.trackColour);
            }
            this.tracks.add(t);
        }

        // Add the station
        s.registerLine(this);
        this.stations.add(s);

         // Check if it is a Cargo Station
        if(s instanceof CargoStation){
        	this.cargoStations.add(s);
        }
    }

    @Override
    public String toString() {
        return "Line [lineColour=" + lineColour + ", trackColour=" + trackColour + ", name=" + name + "]";
    }


    public boolean endOfLine(Station s) throws Exception{
        if(this.stations.contains(s)){
            int index = this.stations.indexOf(s);
            return (index==0 || index==this.stations.size()-1);
        } else {
            throw new Exception();
        }
    }



    public Track nextTrack(Station currentStation, boolean forward) throws Exception {
        if(this.stations.contains(currentStation)){
            // Determine the track index
            int curIndex = this.stations.lastIndexOf(currentStation);
            // Increment to retrieve
            if(!forward){ curIndex -=1;}

            // Check index is within range
            if((curIndex < 0) || (curIndex > this.tracks.size()-1)){
                throw new Exception();
            } else {
                return this.tracks.get(curIndex);
            }

        } else {
            throw new Exception();
        }
    }

    public Station nextStation(Station s, boolean forward) throws Exception{
        if(this.stations.contains(s)){
            int curIndex = this.stations.lastIndexOf(s);
            if(forward){ curIndex+=1;}else{ curIndex -=1;}

            // Check index is within range
            if((curIndex < 0) || (curIndex > this.stations.size()-1)){
                throw new Exception();
            } else {
                return this.stations.get(curIndex);
            }
        } else {
            throw new Exception();
        }
    }

    public Station nextCargoStation(Station s, boolean forward) throws Exception{
        if(this.cargoStations.contains(s)){
            int curIndex = this.cargoStations.lastIndexOf(s);
            if(forward){ curIndex+=1;}else{ curIndex -=1;}

            // Check index is within range
            if((curIndex < 0) || (curIndex > this.cargoStations.size()-1)){
                throw new Exception();
            } else {
                return this.cargoStations.get(curIndex);
            }
        } else {
            throw new Exception();
        }
    }

    public boolean notSingleCargoStation(){
        return this.cargoStations.size() >= MULTIPLE_STATIONS;
    }

    public void render(ShapeRenderer renderer){
        // Set the color to our line
        renderer.setColor(trackColour);

        // Draw all the track sections
        for(Track t: this.tracks){
            t.render(renderer);
        }
    }

}
