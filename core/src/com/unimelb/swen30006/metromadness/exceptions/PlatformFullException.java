package com.unimelb.swen30006.metromadness.exceptions;

/**
 * Nate Bhurinat W. (@natebwangsut | nate.bwangsut@gmail.com)
 * https://github.com/natebwangsut
 */

public class PlatformFullException extends Exception {
    public PlatformFullException() {
        super("Error: All platforms are occupied.");
    }
}
