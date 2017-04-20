package com.unimelb.swen30006.metromadness.exceptions;

/**
 * Nate Bhurinat W. (@natebwangsut | nate.bwangsut@gmail.com)
 * https://github.com/natebwangsut
 */

public class TrainPassengerFullException extends Exception {
    public TrainPassengerFullException() {
        super("Error: Train's capacity reached. Cannot take any more passenger.");
    }
}
