package com.unimelb.swen30006.metromadness.exceptions;

/**
 * Nate Bhurinat W. (@natebwangsut | nate.bwangsut@gmail.com)
 * https://github.com/natebwangsut
 */

public class TrainCargoFullException extends Exception {
    public TrainCargoFullException() {
        super("Error: Train's cargo capacity reached. Cannot take any more passengers' cargo");
    }
}
