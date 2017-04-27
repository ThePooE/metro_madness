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
 * Passenger tries to enter the train when the cargo space is not enough to facilitate
 */

public class TrainCargoFullException extends Exception {
    public TrainCargoFullException() {
        super("Error: Train's cargo capacity reached. Cannot take any more passengers' cargo");
    }
}
