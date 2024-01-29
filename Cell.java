package org.example;

import java.awt.Color;

// Class representing a single cell in the Battle Ship game
class Cell {
    private final Color RED = Color.red;
    private int x, y;
    private Color color;

    // Constructor for creating a cell with specified coordinates
    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        color = Color.gray; // Default color for an undestroyed cell
    }

    // Get the color of the cell
    Color getColor() {
        return color;
    }

    // Check if the cell is destroyed (hit)
    boolean isDestroyed() {
        return color == RED; // Check if the cell is destroyed based on its color
    }

    // Get the x-coordinate of the cell
    int getX() {
        return x;
    }

    // Get the y-coordinate of the cell
    int getY() {
        return y;
    }

    // Check if the cell is hit at the specified coordinates
    boolean checkHit(int x, int y) {
        if (this.x == x && this.y == y) {
            color = RED; // Change color to red if the cell is hit
            return true;
        }
        return false;
    }
}
