package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.Passenger.Cargo;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import sun.rmi.runtime.Log;

import java.awt.geom.Point2D;
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
    private int maxWeight;
	private int maxPassengers;

    public CargoTrain(Line trainLine, Station start, boolean forward, String name, int weight, int passenger) {
        super(trainLine, start, forward, name);
        this.cargo = new ArrayList<Cargo>();
        this.maxWeight = weight;
        this.maxPassengers = passenger;
    }

    @Override
    public void embark(Passenger p) throws Exception {
        int weight=0;
        int numPassengers=this.passengers.size();
        for (Passenger.Cargo pc : cargo) {
            weight += pc.getWeight();
            numPassengers++;
        }
        if (p.getCargo().getWeight() + weight > this.maxWeight && numPassengers +1 > this.maxPassengers) {
            throw new Exception();
        }
        this.passengers.add(p);
    }

    @Override
    public void render(ShapeRenderer renderer){
        if(!this.inStation()){
            Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
            renderer.setColor(col);
            renderer.rect(this.pos.x, this.pos.y, TRAIN_WIDTH, TRAIN_WIDTH);
        }
    }
}
