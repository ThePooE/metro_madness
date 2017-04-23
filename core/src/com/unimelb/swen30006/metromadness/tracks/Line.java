package com.unimelb.swen30006.metromadness.tracks;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.exceptions.StationNotFoundException;
import com.unimelb.swen30006.metromadness.exceptions.TrackNotFoundException;
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

    /**
     * Adds the stations of this Line to the stored variable 'stations'
     * If the station is also a Cargo Station, add those stations to
     * the stored variable 'cargoStations' (for easier searching in other functions)
     * @param s         current station
     * @param two_way   true if the station has a dual track, otherwise false
     */
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

         // Check if it is a Cargo Station then store it
        if(s instanceof CargoStation){
        	this.cargoStations.add(s);
        }
    }

    @Override
    public String toString() {
        return "Line [lineColour=" + lineColour + ", trackColour=" + trackColour + ", name=" + name + "]";
    }


    /**
     * Checks if the station is at an end of the line
     * @param s     current station to be checked
     * @return      true if it is at an end, otherwise false
     * @throws Exception
     */
    public boolean endOfLine(Station s) throws Exception{
        if(this.stations.contains(s)){
            int index = this.stations.indexOf(s);
            return (index==0 || index==this.stations.size()-1);
        } else {
            // Default behaviour
            throw new StationNotFoundException();
        }
    }


    /**
     * Looks for the next track for the train to move into
     * @param currentStation    current station that the train is in
     * @param forward           the direction of train movement on its line
     * @return                  the Track to move into
     * @throws Exception
     */
    public Track nextTrack(Station currentStation, boolean forward) throws Exception {
        if(this.stations.contains(currentStation)){
            // Determine the track index
            int curIndex = this.stations.lastIndexOf(currentStation);

            // Increment to retrieve
            if(!forward){ curIndex -=1;}

            // Check index is within range
            if((curIndex < 0) || (curIndex > this.tracks.size()-1)){
                throw new TrackNotFoundException();
            } else {
                return this.tracks.get(curIndex);
            }

        } else {
            // Default behaviour
            throw new TrackNotFoundException();
        }
    }


    /**
     * Looks for the next station for the train to travel to
     * @param s             current station that the train is in
     * @param forward       the direction or train movement on its line
     * @return              the next Station to travel to
     * @throws Exception
     */
    public Station nextStation(Station s, boolean forward) throws Exception{
        if(this.stations.contains(s)){
            int curIndex = this.stations.lastIndexOf(s);
            if(forward){ curIndex+=1;}else{ curIndex -=1;}

            // Check index is within range
            if((curIndex < 0) || (curIndex > this.stations.size()-1)){
                throw new StationNotFoundException();
            } else {
                return this.stations.get(curIndex);
            }
        } else {
            // Default behaviour
            throw new StationNotFoundException();
        }
    }


    /**
     * Looks for the next cargo station for the train to travel to
     * (in the case of a Cargo Train)
     * @param s             current (Cargo) station that the train is in
     * @param forward       the direction or train movement on its line
     * @return              the next (Cargo) Station to travel to
     * @throws Exception
     */
    public Station nextCargoStation(Station s, boolean forward) throws Exception{
        if(this.cargoStations.contains(s)){
            int curIndex = this.cargoStations.lastIndexOf(s);
            if(forward){ curIndex+=1;}else{ curIndex -=1;}

            // Check index is within range
            if((curIndex < 0) || (curIndex > this.cargoStations.size()-1)){
                throw new StationNotFoundException();
            } else {
                return this.cargoStations.get(curIndex);
            }
        } else {
            throw new StationNotFoundException();
        }
    }


    /**
     * Checks if there are more than one Cargo Stations on a particular line
     * @return      true if true, otherwise false
     */
    public boolean notSingleCargoStation(){
        return this.cargoStations.size() >= MULTIPLE_STATIONS;
    }


    /**
     * Sets the colour of this train line and pass it to the Track renderer
     * @param renderer
     */
    public void render(ShapeRenderer renderer){
        // Set the color to our line
        renderer.setColor(trackColour);

        // Draw all the track sections
        for(Track t: this.tracks){
            t.render(renderer);
        }
    }

}
