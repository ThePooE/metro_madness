package com.unimelb.swen30006.metromadness.tracks;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.trains.Train;

public class Track {
    static final float DRAW_RADIUS=10f;
    static final int LINE_WIDTH=6;
    Point2D.Float startPos;
    Point2D.Float endPos;
    Color trackColour;
    boolean occupied;

    public Track(Point2D.Float start, Point2D.Float end, Color trackCol){
        this.startPos = start;
        this.endPos = end;
        this.trackColour = trackCol;
        this.occupied = false;
    }

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

    @Override
    public String toString() {
        return "Track [startPos=" + startPos + ", endPos=" + endPos + ", trackColour=" + trackColour + ", occupied="
                + occupied + "]";
    }

    /**
     * Train leaving this track
     * @param t     current Train
     */
    public void leave(Train t){
        this.occupied = false;
    }
}
