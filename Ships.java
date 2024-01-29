package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

// Class representing a collection of ships in the Battle Ship game
class Ships {
    private final int CELL_SIZE;
    private ArrayList<Ship> ships = new ArrayList<>(); // Array to store ships
    private final int[] PATTERN = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1}; // Pattern for ships
    private Random random;
    private boolean hide;

    // Constructor for initializing ships with random positions
    Ships(int fieldSize, int cellSize, boolean hide) {
        random = new Random();
        for (int i = 0; i < PATTERN.length; i++) {
            Ship ship;
            do {
                int x = random.nextInt(fieldSize);
                int y = random.nextInt(fieldSize);
                int position = random.nextInt(2);
                ship = new Ship(x, y, PATTERN[i], position);
            } while (ship.isOutOfField(0, fieldSize - 1) || isOverlayOrTouch(ship));
            ships.add(ship);
        }
        CELL_SIZE = cellSize;
        this.hide = hide;
    }

    // Check if a ship overlays or touches another ship in the collection
    boolean isOverlayOrTouch(Ship ctrlShip) {
        for (Ship ship : ships)
            if (ship.isOverlayOrTouch(ctrlShip))
                return true;
        return false;
    }

    // Check if any ship in the collection is hit at the specified coordinates
    boolean checkHit(int x, int y) {
        for (Ship ship : ships)
            if (ship.checkHit(x, y))
                return true;
        return false;
    }

    // Check if there are any surviving ships in the collection
    boolean checkSurvivors() {
        for (Ship ship : ships)
            if (ship.isAlive())
                return true;
        return false;
    }

    // Check if a specific ship in the collection is destroyed
    boolean isShipDestroyed(Ship ship) {
        for (Cell cell : ship.getCells())
            if (!cell.isDestroyed())
                return false;
        return true;
    }
    boolean isCellAroundDestroyedShip(int x, int y) {
        for (Ship ship : ships) {
            if (isShipDestroyed(ship) && ship.isCellAroundDestroyedShip(new Cell(x, y))) {
                return true;
            }
        }
        return false;
    }


    // Check if a specific cell in any ship is protected
    boolean isCellProtected(int x, int y) {
        for (Ship ship : ships) {
            Cell cell = new Cell(x, y);
            if (ship.isProtected(cell)) {
                return true;
            }
        }
        return false;
    }

    // Paint the ships on the graphics context, considering hiding destroyed ships
    void paint(Graphics g, boolean hideDestroyed, boolean destroyed) {
        for (Ship ship : ships) {
            // Check if the ship is destroyed
            if (isShipDestroyed(ship)) {
                // Draw small gray squares around the destroyed ship
                for (Cell cell : ship.getCells()) {
                    int x = cell.getX();
                    int y = cell.getY();

                    // Draw small gray square around the destroyed cell
                    if (cell.isDestroyed() && destroyed) {
                        drawGraySquare(g, x, y);
                    }
                }
            }

            // Set color to red for the entire destroyed ship
            g.setColor(Color.red);
            ship.paint(g, CELL_SIZE, hideDestroyed, destroyed);
        }
    }

    // Draw a small gray square around a specified cell
    private void drawGraySquare(Graphics g, int x, int y) {
        // Draw small gray square around the specified cell
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                int centerX = i * CELL_SIZE + CELL_SIZE / 2 - 3;
                int centerY = j * CELL_SIZE + CELL_SIZE / 2 - 3;
                g.setColor(Color.gray);
                g.fillRect(centerX, centerY, 8, 8);
            }
        }
    }
}
