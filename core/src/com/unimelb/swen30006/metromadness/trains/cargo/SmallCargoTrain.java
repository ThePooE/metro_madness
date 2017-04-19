package com.unimelb.swen30006.metromadness.trains.cargo;

import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.CargoTrain;

/**
 * Nate Bhurinat W. (@natebwangsut | nate.bwangsut@gmail.com)
 * https://github.com/natebwangsut
 */

public class SmallCargoTrain extends CargoTrain {
    public SmallCargoTrain(Line trainLine, Station start, boolean forward, String name) {
        super(trainLine, start, forward, name);
    }
}
