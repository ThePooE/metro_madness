package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Random;

import com.unimelb.swen30006.metromadness.stations.ActiveStation;
import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class PassengerGenerator {

    // Random number generator
    static final private Random random = new Random(30006);

    // Passenger id generator
    static private int idGen = 1;


    // The station that passengers are getting on
    public Station s;

    // The line they are travelling on
    public ArrayList<Line> lines;

    // The max volume
    public float maxVolume;

    public PassengerGenerator(Station s, ArrayList<Line> lines, float max){
        this.s = s;
        this.lines = lines;
        this.maxVolume = max;
    }

    public Passenger[] generatePassengers(){
        int count = random.nextInt(4)+1;
        Passenger[] passengers = new Passenger[count];

        // Check is current station is a Cargo Station
        boolean atCargoStation = s instanceof CargoStation;

        System.out.println("At station: " + this.s.name + " is a Cargo station?: " + Boolean.toString(atCargoStation));
        for(int i=0; i<count; i++){

            passengers[i] = generatePassenger(random, atCargoStation);
        }
        return passengers;
    }



    public Passenger generatePassenger(Random random, boolean atCargoStation){

        // Pick a random line that the station is part of
        // Note: a station could be part of more than one line
        Line l = this.lines.get(random.nextInt(this.lines.size()));
        ArrayList<Station> stationList;

        // Check if current station is a Cargo Station or Active Station
        if(atCargoStation){
            if(!l.notSingleCargoStation()){
                return null;
            }
            stationList = l.cargoStations;
        } else {
            stationList = l.stations;
        }

        int current_station = stationList.indexOf(this.s);
        boolean forward = random.nextBoolean();

        // If we are the end of the line then set our direction forward or backward
        if(current_station == 0){
            forward = true;
        } else if (current_station == stationList.size()-1){
            forward = false;
        }

        // Find the station
        int index = 0;

        if (forward) {
            index = random.nextInt(stationList.size() - 1 - current_station) + current_station + 1;
        } else {
            index = current_station - 1 - random.nextInt(current_station);
        }
        Station s = stationList.get(index);

        return this.s.generatePassenger(idGen++, random, s);
    }
}
