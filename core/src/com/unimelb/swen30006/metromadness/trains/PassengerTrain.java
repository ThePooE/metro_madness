package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Nate Bhurinat W. (@natebwangsut | nate.bwangsut@gmail.com)
 * https://github.com/natebwangsut
 */

public class PassengerTrain extends Train {

    private static Logger logger = LogManager.getLogger();
    private static int maxPassenger;

//    Train Rendering Configs
//    public static final int     MAX_TRIPS       = 4;
//    public static final Color   FORWARD_COLOUR  = Color.ORANGE;
//    public static final Color   BACKWARD_COLOUR = Color.VIOLET;
//    public static final float   TRAIN_WIDTH     = 4;
//    public static final float   TRAIN_LENGTH    = 6;
//    public static final float   TRAIN_SPEED     = 50f;

    @Override
    public void embark(Passenger p) throws Exception {
        int numPassengers=this.passengers.size();
        if (numPassengers +1 > this.maxPassenger) {
            throw new Exception();
        }
        this.passengers.add(p);
    }
    
    
    public PassengerTrain(Line trainLine, Station start, boolean forward, String name, int passenger) {
        super(trainLine, start, forward, name);
        this.maxPassenger = passenger;
    }
}
