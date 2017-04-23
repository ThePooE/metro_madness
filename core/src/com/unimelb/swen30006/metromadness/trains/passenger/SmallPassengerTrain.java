package com.unimelb.swen30006.metromadness.trains.passenger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.PassengerTrain;

public class SmallPassengerTrain extends PassengerTrain {

    // Different capacity between trains
    private static int PASSENGER_CAPACITY = 10;

    public SmallPassengerTrain(Line trainLine, Station start, boolean forward, String name) {
        super(trainLine, start, forward, name, PASSENGER_CAPACITY);
    }

    @Override
    public void render(ShapeRenderer renderer){
        if(!this.inStation()){
            
            // Train gets slightly bigger when there are more passengers
            float percentage = this.passengers.size()/10f;
            float size = TRAIN_WIDTH* (1+percentage);
            
            Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
            renderer.setColor(col.cpy().lerp(Color.MAROON, percentage));
            renderer.circle(this.pos.x, this.pos.y, size);
        }
    }

}
