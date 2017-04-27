package com.unimelb.swen30006.metromadness.trains;

import com.unimelb.swen30006.metromadness.exceptions.TrainCargoFullException;
import com.unimelb.swen30006.metromadness.exceptions.TrainPassengerFullException;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.Passenger.Cargo;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

import java.util.ArrayList;

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

public class CargoTrain extends Train {

    /* Cargo Systems */
    private ArrayList<Passenger.Cargo> cargo;
    private int weightCapacity;
    private int passengerCapacity;

    /**
     * Constructor of CargoTrain
     * @param trainLine     Line which the Train will run
     * @param start         Station that the Train start
     * @param forward       Which way the train will wun
     * @param name          Name of this train
     * @param capacity      Capacity (passengers) of the train
     * @param weight        Capacity (cargo) of the train
     */
    public CargoTrain(Line trainLine, Station start, boolean forward, String name, int capacity, int weight) {
        super(trainLine, start, forward, name);
        this.cargo = new ArrayList<Cargo>();
        this.passengerCapacity = capacity;
        this.weightCapacity = weight;
    }


    /**
     * Try to add Passengers into Train
     * @param p     Passengers to enter the Train
     * @throws Exception
     */
    @Override
    public void embark(Passenger p) throws Exception {

        // Check passenger full
        if(this.passengers.size() +1 > this.passengerCapacity){
            throw new TrainPassengerFullException();
        }

        // Check weight full
        int weight=0;
        for (Passenger.Cargo pc : cargo) {
            weight += pc.getWeight();
        }
        if (p.getCargo().getWeight() + weight > this.weightCapacity) {
            throw new TrainCargoFullException();
        }

        this.passengers.add(p);
    }
}
