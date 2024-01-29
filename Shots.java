package org.example;

import java.awt.Graphics;
import java.util.ArrayList;

// Class representing a collection of shots in the Battle Ship game
class Shots {
    private final int CELL_SIZE;
    private ArrayList<Shot> shots;

    // Constructor for initializing shots with a specified cell size
    Shots(int cellSize) {
        CELL_SIZE = cellSize;
        shots = new ArrayList<>();
    }

    // Add a shot at the specified coordinates
    void add(int x, int y, boolean shot) {
        shots.add(new Shot(x, y, shot));
    }

    // Check if a shot has been fired at the same place
    boolean hitSamePlace(int x, int y) {
        for (Shot shot : shots)
            if (shot.getX() == x && shot.getY() == y && shot.isShot())
                return true;
        return false;
    }

    // Get a shot label at the specified coordinates
    Shot getLabel(int x, int y) {
        for (Shot label : shots)
            if (label.getX() == x && label.getY() == y && (!label.isShot()))
                return label;
        return null;
    }

    // Remove a shot label
    void removeLabel(Shot label) {
        shots.remove(label);
    }

    // Paint all the shots on the graphics context
    void paint(Graphics g) {
        for (Shot shot : shots)
            shot.paint(g, CELL_SIZE);
    }
}
