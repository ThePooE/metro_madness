package com.unimelb.swen30006.metromadness.tracks;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.trains.Train;

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
 */

public class Track {

    static final float DRAW_RADIUS=10f;
    static final int LINE_WIDTH=6;

    Point2D.Float startPos;
    Point2D.Float endPos;

    Color trackColour;
    boolean occupied;

    /**
     * Constructor for Track
     * @param start         Track's starting position
     * @param end           Track's ending position
     * @param trackCol      Color of the particular Track
     */
    public Track(Point2D.Float start, Point2D.Float end, Color trackCol){
        this.startPos = start;
        this.endPos = end;
        this.trackColour = trackCol;
        this.occupied = false;
    }

    /**
     * Render the Track as rectangles
     * @param renderer      ShapeRenderer to render the Track
     */
    public void render(ShapeRenderer renderer){
        renderer.rectLine(startPos.x, startPos.y, endPos.x, endPos.y, LINE_WIDTH);
    }

    /**
     * Check if the track is empty and can be entered by a train
     * @param forward   direction of train's movement on a line
     * @return
     */
    public boolean canEnter(boolean forward){
        return !this.occupied;
    }

    /**
     * Train entering and occupying this track
     * @param t     current Train
     */
    public void enter(Train t){
        this.occupied = true;
    }

    /**
     * Converts Track's information into String
     * @return      Track's info
     */
    @Override
    public String toString() {
        return "Track [startPos=" + startPos
                + ", endPos=" + endPos
                + ", trackColour=" + trackColour
                + ", occupied=" + occupied + "]";
    }

    /**
     * Train leaving this track
     * @param t     current Train
     */
    public void leave(Train t){
        this.occupied = false;
    }
}
