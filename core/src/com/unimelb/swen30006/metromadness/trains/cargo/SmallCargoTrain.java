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

package com.unimelb.swen30006.metromadness.trains.cargo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.CargoTrain;

/**
 * Nate Bhurinat W. (@natebwangsut | nate.bwangsut@gmail.com)
 * https://github.com/natebwangsut
 */

public class SmallCargoTrain extends CargoTrain {

    // Different capacity between trains
    private static int PASSENGER_CAPACITY   = 10;
    private static int WEIGHT_CAPACITY      = 200;

    public SmallCargoTrain(Line trainLine, Station start, boolean forward, String name) {
        super(trainLine, start, forward, name, PASSENGER_CAPACITY, WEIGHT_CAPACITY);
    }
    
    /**
     * Render Cargo Trains as square rather than circle for Passenger Trains
     * @param renderer
     */
    @Override
    public void render(ShapeRenderer renderer){
        
        // Train gets slightly bigger when there are more passengers
        float percentage = this.getPassengers().size()/10f;
        float size = TRAIN_WIDTH*(1+percentage);
        
        if(!this.inStation()){
            Color col = this.getForward() ? FORWARD_COLOUR : BACKWARD_COLOUR;
            renderer.setColor(col);
            renderer.rect(this.getPos().x, this.getPos().y, size, size);
        }
    }
}
