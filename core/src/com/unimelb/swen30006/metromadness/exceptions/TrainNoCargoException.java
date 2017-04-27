package com.unimelb.swen30006.metromadness.exceptions;

/**
 * [SWEN30006] Software Modelling and Design
 * Semester 1, 2017
 * Project Part B - Metro Madness
 *
 * Group 107:
 * Nate Wangsutthitham [755399]
 * Kolatat Thangkasemvathana [780631]
 * Khai Mei Chin [755332]
 *
 * Exception if the Passengers with Cargo tries to enter invalid Train.
 */

public class TrainNoCargoException extends Exception {
    public TrainNoCargoException() {
        super("Error: Passengers with Cargo tries to enter Train that cannot contain Cargo.");
    }
}
