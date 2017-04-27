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
 * Cannot find a particular train
 */

public class TrainNotFoundException extends Exception{
    public TrainNotFoundException() {
        super("Error: Cannot found the Train.");
    }
}
