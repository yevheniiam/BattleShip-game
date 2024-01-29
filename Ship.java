package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

// Class representing a Ship in the Battle Ship game
class Ship {
    private ArrayList<Cell> cells = new ArrayList<>();
    private Set<Cell> protectedCells = new HashSet<>();

    // Constructor for creating a ship with specified parameters
    Ship(int x, int y, int length, int position) {
        for (int i = 0; i < length; i++)
            cells.add(
                    new Cell(x + i * ((position == 1) ? 0 : 1),
                            y + i * ((position == 1) ? 1 : 0)));
    }

    // Check if the ship is out of the game field boundaries
    boolean isOutOfField(int bottom, int top) {
        for (Cell cell : cells)
            if (cell.getX() < bottom || cell.getX() > top ||
                    cell.getY() < bottom || cell.getY() > top)
                return true;
        return false;
    }

    boolean isCellAroundDestroyedShip(Cell ctrlCell) {
        for (Cell cell : cells) {
            for (int dx = -1; dx < 2; dx++) {
                for (int dy = -1; dy < 2; dy++) {
                    int newX = cell.getX() + dx;
                    int newY = cell.getY() + dy;
                    if (newX == ctrlCell.getX() && newY == ctrlCell.getY()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Check if a specific cell is protected
    boolean isProtected(Cell cell) {
        return protectedCells.contains(cell);
    }

    // Check if the ship overlays or touches another ship
    boolean isOverlayOrTouch(Ship ctrlShip) {
        for (Cell cell : cells)
            if (ctrlShip.isOverlayOrTouchCell(cell))
                return true;
        return false;
    }

    // Check if a specific cell overlays or touches the ship
    boolean isOverlayOrTouchCell(Cell ctrlCell) {
        for (Cell cell : cells)
            for (int dx = -1; dx < 2; dx++)
                for (int dy = -1; dy < 2; dy++)
                    if (ctrlCell.getX() == cell.getX() + dx &&
                            ctrlCell.getY() == cell.getY() + dy)
                        return true;
        return false;
    }

    // Get the cells of the ship
    ArrayList<Cell> getCells() {
        return cells;
    }

    // Check if the ship is hit at the specified coordinates
    boolean checkHit(int x, int y) {
        for (Cell cell : cells)
            if (cell.checkHit(x, y))
                return true;
        return false;
    }

    // Check if the ship is still alive (has undestroyed cells)
    boolean isAlive() {
        for (Cell cell : cells)
            if (!cell.isDestroyed())
                return true;
        return false;
    }

    // Paint the ship on the graphics context
    void paint(Graphics g, int cellSize, boolean hide, boolean destroyed) {
        for (Cell cell : cells) {
            int cellX = cell.getX();
            int cellY = cell.getY();
            Color cellColor = cell.getColor();

            // Draw the cell if not hidden or if hidden and either destroyed or not destroyed
            if (!hide || (hide && cell.isDestroyed() && destroyed)) {
                g.setColor(cellColor);
                g.fill3DRect(cellX * cellSize + 1, cellY * cellSize + 1, cellSize - 2, cellSize - 2, true);
            }
        }
    }
}
