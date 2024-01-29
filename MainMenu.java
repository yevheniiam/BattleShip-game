package org.example;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
public class MainMenu extends JFrame {
    private JButton startGameButton;
    private JButton viewRecordsButton;

    public MainMenu() {
        setTitle("Battle Ship Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        // icon to the title bar
        ImageIcon icon = new ImageIcon("free-icon-ship-870119.png");
        setIconImage(icon.getImage());

        // Create a custom font for the title
        Font titleFont = new Font("Arial", Font.BOLD, 40);

        // Add a label with the game name
        JLabel titleLabel = new JLabel("Battle Ship", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.BLUE);
        add(titleLabel, BorderLayout.NORTH);

        // Add a smaller ship icon above the buttons
        ImageIcon smallIcon = new ImageIcon(icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
        JLabel shipIconLabel = new JLabel(smallIcon);
        shipIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(shipIconLabel, BorderLayout.CENTER);

        // Set up the buttons
        startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(e -> {
            dispose();
            new GameBattleShip();
        });

        viewRecordsButton = new JButton("View Records");
        viewRecordsButton.addActionListener(e -> {
            showRecordsTable();
        });

        // Customize the appearance of the buttons
        startGameButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewRecordsButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add buttons to a panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonPanel.add(startGameButton);
        buttonPanel.add(viewRecordsButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void showRecordsTable() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("all_players_best_time.txt"));
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Player Name", "Record (seconds)"}, 0);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                String playerName = parts[0];
                String recordTime = parts[1];
                tableModel.addRow(new Object[]{playerName, recordTime});
            }

            JTable recordsTable = new JTable(tableModel);
            JTextField searchField = new JTextField();
            searchField.setFont(new Font("Arial", Font.PLAIN, 14));
            searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));
            searchField.addActionListener(e -> searchPlayer(recordsTable, searchField.getText()));

            JPanel searchPanel = new JPanel();
            searchPanel.setLayout(new BorderLayout());
            searchPanel.add(searchField, BorderLayout.CENTER);

            JPanel recordsPanel = new JPanel(new BorderLayout());
            recordsPanel.add(searchPanel, BorderLayout.NORTH);
            recordsPanel.add(new JScrollPane(recordsTable), BorderLayout.CENTER);

            JOptionPane.showMessageDialog(this, recordsPanel, "Records", JOptionPane.INFORMATION_MESSAGE);

            // Return to the main menu
            setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchPlayer(JTable recordsTable, String playerName) {
        for (int i = 0; i < recordsTable.getRowCount(); i++) {
            if (recordsTable.getValueAt(i, 0).equals(playerName)) {
                recordsTable.setRowSelectionInterval(i, i);
                recordsTable.scrollRectToVisible(recordsTable.getCellRect(i, 0, true));
                break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}
