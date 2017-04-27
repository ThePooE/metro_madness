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
 * Exception if the platform is full and a train tries to enter.
 */

public class PlatformFullException extends Exception {
    public PlatformFullException() {
        super("Error: All platforms are occupied.");
    }
}
