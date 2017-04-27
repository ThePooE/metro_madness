package com.unimelb.swen30006.metromadness.exceptions;

/**
 * Nate Bhurinat W. (@natebwangsut | nate.bwangsut@gmail.com)
 * https://github.com/natebwangsut
 */

public class TrainNoCargoException extends Exception {
    public TrainNoCargoException() {
        super("Error: Passengers with Cargo tries to enter Train that cannot contain Cargo.");
    }
}
