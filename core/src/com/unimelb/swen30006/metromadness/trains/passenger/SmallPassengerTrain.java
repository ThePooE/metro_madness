package com.unimelb.swen30006.metromadness.trains.passenger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.PassengerTrain;

public class SmallPassengerTrain extends PassengerTrain {

	private static int MAX_PASSENGER = 10;
	
    public SmallPassengerTrain(Line trainLine, Station start, boolean forward, String name) {
        super(trainLine, start, forward, name, MAX_PASSENGER);
    }

   

    @Override
    public void render(ShapeRenderer renderer){
        if(!this.inStation()){
            Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
            float percentage = this.passengers.size()/10f;
            renderer.setColor(col.cpy().lerp(Color.MAROON, percentage));
            // We also get slightly bigger with passengers
            renderer.circle(this.pos.x, this.pos.y, TRAIN_WIDTH*(1+percentage));
        }
    }

}
