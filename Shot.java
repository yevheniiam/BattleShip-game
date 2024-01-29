package org.example;

import java.awt.Color;
import java.awt.Graphics;

// Class representing a shot in the Battle Ship game
class Shot {
    private int x, y;
    private boolean shot;

    // Constructor for creating a shot with specified coordinates and shot status
    Shot(int x, int y, boolean shot) {
        this.x = x;
        this.y = y;
        this.shot = shot;
    }

    // Get the x-coordinate of the shot
    int getX() {
        return x;
    }

    // Get the y-coordinate of the shot
    int getY() {
        return y;
    }

    // Check if the shot has been fired
    boolean isShot() {
        return shot;
    }

    // Paint the shot on the graphics context with a specified cell size
    void paint(Graphics g, int cellSize) {
        g.setColor(Color.gray);
        if (shot)
            g.fillRect(x * cellSize + cellSize / 2 - 3, y * cellSize + cellSize / 2 - 3, 8, 8);
        else
            g.drawRect(x * cellSize + cellSize / 2 - 3, y * cellSize + cellSize / 2 - 3, 8, 8);
    }
}
