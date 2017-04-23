package com.unimelb.swen30006.metromadness.exceptions;

/**
 * Nate Bhurinat W. (@natebwangsut | nate.bwangsut@gmail.com)
 * https://github.com/natebwangsut
 */

public class TrackNotFoundException extends Exception {
    public TrackNotFoundException() {
        super("Error: Track not found.");
    }
}
