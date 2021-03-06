package com.unimelb.swen30006.metromadness.trains.passenger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.PassengerTrain;

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

public class SmallPassengerTrain extends PassengerTrain {

    // Different capacity between trains
    private static int PASSENGER_CAPACITY = 10;

    /**
     * Constructor for SmallPassengerTrain class
     * @param trainLine     Line which the Train will run
     * @param start         Station that the Train start
     * @param forward       Which way the train will wun
     * @param name          Name of this train
     */
    public SmallPassengerTrain(Line trainLine, Station start, boolean forward, String name) {
        super(trainLine, start, forward, name, PASSENGER_CAPACITY);
    }


    /**
     * Render algorithm for SmallPassengerTrain class
     * @param renderer      ShapeRenderer to render the Train
     */
    @Override
    public void render(ShapeRenderer renderer){
        if(!this.inStation()){

            // Train gets slightly bigger when there are more passengers
            float percentage = this.getPassengers().size()/10f;
            float size = TRAIN_WIDTH* (1+percentage);

            Color col = this.getForward() ? FORWARD_COLOUR : BACKWARD_COLOUR;
            renderer.setColor(col.cpy().lerp(Color.MAROON, percentage));
            renderer.circle(this.getPos().x, this.getPos().y, size);
        }
    }

}
