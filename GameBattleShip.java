package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.ImageIcon;

class GameBattleShip extends JFrame {

    final String TITLE_OF_PROGRAM = "Battle Ship";
    static final int FIELD_SIZE = 10;
    final int AI_PANEL_SIZE = 400;
    final int AI_CELL_SIZE = AI_PANEL_SIZE / FIELD_SIZE;
    final int HUMAN_PANEL_SIZE = AI_PANEL_SIZE / 2;
    final int HUMAN_CELL_SIZE = HUMAN_PANEL_SIZE / FIELD_SIZE;
    final String BTN_INIT = "New game";
    final String BTN_EXIT = "Exit";
    final String BTN_MENU = "Menu";
    private static final String BEST_TIME_FILE = "all_players_best_time.txt";

    final String AI_WON = "AI WON!";
    final int MOUSE_BUTTON_LEFT = 1; // for mouse listener
    final int MOUSE_BUTTON_RIGHT = 3;
    private long bestTime = Long.MAX_VALUE; // Initialize with a large value

    private long startTime;
    private long endTime;
    String playerName = "";

    JTextArea board; // for logging
    Canvas leftPanel, humanPanel; // for game fields
    Ships aiShips, humanShips; // set of human's and AI ships
    Shots humanShots, aiShots; // set of shots from human and AI
    Random random;
    boolean gameOver;
    JButton menuButton;

    GameBattleShip() {
        setTitle(TITLE_OF_PROGRAM);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        ImageIcon icon = new ImageIcon("free-icon-ship-870119.png");
        setIconImage(icon.getImage());

        // Initialize the best time file
        initBestTimeFile();

        leftPanel = new Canvas(); // panel for AI ships
        leftPanel.setPreferredSize(new Dimension(AI_PANEL_SIZE, AI_PANEL_SIZE));
        leftPanel.setBackground(Color.white);
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
        leftPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseRelease(e);
            }
        });

        humanPanel = new Canvas(); // panel for human ships
        humanPanel.setPreferredSize(new Dimension(HUMAN_PANEL_SIZE, HUMAN_PANEL_SIZE));
        humanPanel.setBackground(Color.white);
        humanPanel.setBorder(BorderFactory.createLineBorder(Color.blue));

        JButton init = new JButton(BTN_INIT); // init button
        init.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!confirmAction("New Game")) {
                    return;
                }
                getPlayerName();
                init();
                leftPanel.repaint();
                humanPanel.repaint();
            }
        });

        init.setMaximumSize(new Dimension(Integer.MAX_VALUE, init.getMinimumSize().height));

        JButton exit = new JButton(BTN_EXIT); // exit game button
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!confirmAction("Exit")) {
                    return;
                }
                System.exit(0);
            }
        });
        exit.setMaximumSize(new Dimension(Integer.MAX_VALUE, exit.getMinimumSize().height));

        menuButton = new JButton(BTN_MENU); // menu button
        menuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!confirmAction("Menu")) {
                    return;
                }
                showMainMenu();
            }
        });
        menuButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, menuButton.getMinimumSize().height));

        board = new JTextArea(); // scoreboard
        board.setEditable(false);
        JScrollPane scroll = new JScrollPane(board);

        JPanel buttonPanel = new JPanel(); // panel for button
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(init);
        buttonPanel.add(exit);
        buttonPanel.add(menuButton);

        JPanel rightPanel = new JPanel();         // panel for human ships,
        rightPanel.setLayout(new BorderLayout()); // scoreboard and buttons

        rightPanel.add(humanPanel, BorderLayout.NORTH);
        rightPanel.add(scroll, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        add(leftPanel);
        add(rightPanel);
        pack();
        setLocationRelativeTo(null); // to the center
        init();
        setVisible(true); // Moved setVisible to the end
    }
    private void initBestTimeFile() {
        try {
            File file = new File(BEST_TIME_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean confirmAction(String action) {
        if (gameOver || confirmDialog("Are you sure you want to " + action + "?")) {
            return true;
        }
        return false;
    }

    private boolean confirmDialog(String message) {
        int result = JOptionPane.showConfirmDialog(this, message, "Confirm Action", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
    private void handleMouseRelease(MouseEvent e) {
        int x = e.getX() / AI_CELL_SIZE;
        int y = e.getY() / AI_CELL_SIZE;

        if (e.getButton() == MOUSE_BUTTON_LEFT && !gameOver) {
            if (!humanShots.hitSamePlace(x, y) && !aiShips.isCellAroundDestroyedShip(x, y)) {
                humanShots.add(x, y, true);

                if (aiShips.checkHit(x, y)) {
                    if (!aiShips.checkSurvivors()) {
                        endTime = System.currentTimeMillis();
                        long totalTime = endTime - startTime;
                        long seconds = totalTime / 1000;

                        if (!playerName.equals("Player")) {
                            if (seconds < bestTime) {
                                bestTime = seconds;
                                saveBestTimeToFile(bestTime);
                                board.append("\n" + playerName + " won in " + seconds + " seconds! New record!");
                            } else {
                                board.append("\n" + playerName + " won in " + seconds + " seconds! Best time: " + bestTime + " seconds");
                            }
                        } else {
                            board.append("\nYou won!");
                        }
                        gameOver = true;
                    }
                } else if (!aiShips.isCellProtected(x, y)) {
                    shootsAI();
                }

                leftPanel.repaint();
                humanPanel.repaint();
                board.setCaretPosition(board.getText().length());
            }
        }

        if (e.getButton() == MOUSE_BUTTON_RIGHT) {
            Shot label = humanShots.getLabel(x, y);
            if (label != null) {
                humanShots.removeLabel(label);
            } else {
                humanShots.add(x, y, false);
            }
            leftPanel.repaint();
        }
    }

    // Method to show the main menu and close the game frame
    private void showMainMenu() {
        MainMenu mainMenu = new MainMenu();
        mainMenu.setVisible(true);
        dispose(); // Close the game frame
    }

    // Method to initialize the game
    void init() {
        if (playerName.isEmpty()) {
            getPlayerName();
        }

        aiShips = new Ships(FIELD_SIZE, AI_CELL_SIZE, true);
        humanShips = new Ships(FIELD_SIZE, HUMAN_CELL_SIZE, false);
        aiShots = new Shots(HUMAN_CELL_SIZE);
        humanShots = new Shots(AI_CELL_SIZE);
        board.setText(BTN_INIT);
        gameOver = false;
        random = new Random();
        startTime = System.currentTimeMillis(); // Set the start time
        readBestTimeFromFile();

        leftPanel.repaint();
    }

    private void getPlayerName() {
        while (true) {
            // Create a custom input dialog
            JPanel panel = new JPanel();
            JLabel label = new JLabel("Enter your name:");
            JTextField textField = new JTextField(10);
            panel.add(label);
            panel.add(textField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Registration", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                playerName = textField.getText();

                if (playerName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid name.", "Invalid Name", JOptionPane.WARNING_MESSAGE);
                } else {
                    break;
                }
            } else {
                playerName = "Player";
                JOptionPane.showMessageDialog(this, "Default name 'Player' will be used.", "Default Name", JOptionPane.INFORMATION_MESSAGE);
                break; // Exit the loop
            }
        }
    }


    void shootsAI() {
        if (gameOver) {
            return;
        }

        int x, y;
        do {
            x = random.nextInt(FIELD_SIZE);
            y = random.nextInt(FIELD_SIZE);
        } while (aiShots.hitSamePlace(x, y) || aiShips.isCellProtected(x, y));

        aiShots.add(x, y, true);
        if (!humanShips.checkHit(x, y)) {
            board.append("\n" + (x + 1) + ":" + (y + 1) + " AI missed.");
            // AI missed, now let the player take the next turn
        } else {
            // Bot hit the target
            board.append("\n" + (x + 1) + ":" + (y + 1) + " AI hit the target.");

            if (!humanShips.checkSurvivors()) {
                endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                long seconds = totalTime / 1000;

                if (seconds < bestTime) {
                    bestTime = seconds;
                    saveBestTimeToFile(bestTime);
                }
                board.append("\n"+ AI_WON);
                gameOver = true;
                return; // Return to avoid the recursive call
            }
        }
        leftPanel.repaint();

        if (gameOver) {
            board.setCaretPosition(board.getText().length());
        }
    }


    private void saveBestTimeToFile(long time) {
        try {
            List<String> records = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(BEST_TIME_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    records.add(line);
                }
            }
            records.add(playerName + ": " + Long.toString(time));

            records.sort(Comparator.comparingLong(record -> Long.parseLong(record.split(": ")[1])));

            try (FileWriter writer = new FileWriter(BEST_TIME_FILE)) {
                for (String record : records) {
                    writer.write(record + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readBestTimeFromFile() {
        try {
            File file = new File("all_players_best_time.txt");
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith(playerName)) {
                        // Extract the time for the specific player
                        bestTime = Long.parseLong(line.split(": ")[1]);
                    }
                }
                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class representing the canvas for painting
    class Canvas extends JPanel { // for painting
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            int cellSize = (int) getSize().getWidth() / FIELD_SIZE;

            // Draw gray squares around the ships
            g.setColor(Color.lightGray);
            for (int i = 1; i < FIELD_SIZE; i++) {
                g.drawLine(0, i * cellSize, FIELD_SIZE * cellSize, i * cellSize);
                g.drawLine(i * cellSize, 0, i * cellSize, FIELD_SIZE * cellSize);
            }

            // Draw the ships and shots
            if (cellSize == AI_CELL_SIZE) {
                humanShots.paint(g);
                aiShips.paint(g, true, true); // Pass 'true' to indicate hiding the destroyed ships and 'true' for destroyed
            } else {
                aiShots.paint(g);
                humanShips.paint(g, false, true); // Pass 'false' to indicate not hiding the destroyed ships and 'false' for destroyed
            }
        }
    }
}