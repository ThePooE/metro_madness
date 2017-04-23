package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.exceptions.TrainCargoFullException;
import com.unimelb.swen30006.metromadness.exceptions.TrainPassengerFullException;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.Passenger.Cargo;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Nate Bhurinat W. (@natebwangsut | nate.bwangsut@gmail.com)
 * https://github.com/natebwangsut
 */

public class CargoTrain extends Train {

    private static Logger logger = LogManager.getLogger();

//    Train Rendering Configs
//    public static final int     MAX_TRIPS       = 4;
//    public static final Color   FORWARD_COLOUR  = Color.ORANGE;
//    public static final Color   BACKWARD_COLOUR = Color.VIOLET;
//    public static final float   TRAIN_WIDTH     = 4;
//    public static final float   TRAIN_LENGTH    = 6;
//    public static final float   TRAIN_SPEED     = 50f;

    // Cargo
    private ArrayList<Passenger.Cargo> cargo;
    private int weightCapacity;
    private int passengerCapacity;

    public CargoTrain(Line trainLine, Station start, boolean forward, String name, int capacity, int weight) {
        super(trainLine, start, forward, name);
        this.cargo = new ArrayList<Cargo>();
        this.passengerCapacity = capacity;
        this.weightCapacity = weight;
    }

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
