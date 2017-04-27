package com.unimelb.swen30006.metromadness.trains;

import com.unimelb.swen30006.metromadness.exceptions.TrainPassengerFullException;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

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

public class PassengerTrain extends Train {

    private int capacity;

    public PassengerTrain(Line trainLine, Station start, boolean forward, String name, int capacity) {
        super(trainLine, start, forward, name);
        this.capacity = capacity;
    }

    @Override
    public void embark(Passenger p) throws Exception {
        if(this.passengers.size() +1 > this.capacity){
            throw new TrainPassengerFullException();
        }
        this.passengers.add(p);
    }
}
