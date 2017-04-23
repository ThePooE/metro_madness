package com.unimelb.swen30006.metromadness.trains.passenger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.PassengerTrain;

public class BigPassengerTrain extends PassengerTrain {

    // Different capacity between trains
    private static int PASSENGER_CAPACITY=80;

    public BigPassengerTrain(Line trainLine, Station start, boolean forward, String name) {
        super(trainLine, start, forward, name, PASSENGER_CAPACITY);
    }

    @Override
    public void render(ShapeRenderer renderer){
        if(!this.inStation()){

            // Train gets slightly bigger when there are more passengers
            float percentage = this.passengers.size()/20f;
            float size = TRAIN_WIDTH*(1+percentage);
            
            Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
            renderer.setColor(col.cpy().lerp(Color.LIGHT_GRAY, percentage));
            renderer.circle(this.pos.x, this.pos.y, size);
        }
    }
}

  