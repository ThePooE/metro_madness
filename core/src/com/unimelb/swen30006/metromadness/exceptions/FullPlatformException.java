package com.unimelb.swen30006.metromadness.exceptions;

/**
 * Nate Bhurinat W. (@natebwangsut | nate.bwangsut@gmail.com)
 * https://github.com/natebwangsut
 */

public class FullPlatformException extends Exception {
    public FullPlatformException() {
        super("Error: All platforms are occupied.");
    }
}
