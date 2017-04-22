package com.unimelb.swen30006.metromadness.trains.passenger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.PassengerTrain;

/**********************************************************************************
 * Nate Bhurinat W. (@ThePooE | nate.bwangsut@gmail.com)
 * https://github.com/ThePooE
 * on 20/04/2017
 **********************************************************************************/
public class LargePassengerTrain extends PassengerTrain {

    /* This train can have vary size */
	private static int MAX_PASSENGER = 80;

    public LargePassengerTrain(Line trainLine, Station start, boolean forward, String name) {
        super(trainLine, start, forward, name, MAX_PASSENGER);
    }

    

    @Override
    public void render(ShapeRenderer renderer){
        if(!this.inStation()){
            Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
            float percentage = this.passengers.size()/15f;
            renderer.setColor(col.cpy().lerp(Color.MAROON, percentage));
            renderer.circle(this.pos.x, this.pos.y, TRAIN_WIDTH*(1+percentage));
        }
    }
}
