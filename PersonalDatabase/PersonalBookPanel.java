package PersonalDatabase;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import gui.EditBookWindow;
import internationalization.LanguagePanel;
import login.LogInPage;
import sortingfiltering.TableSorting;

import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class PersonalBookPanel extends JPanel {
    private DefaultTableModel model;
    private String userCsvFile;

    private TableRowSorter<DefaultTableModel> sorter;
    private Locale currentLocale;
    private static ResourceBundle rb;
    private TableSorting sorting;

    public PersonalBookPanel(String userCsvFile) {
        currentLocale = new Locale(LanguagePanel.getLanguage());
        rb = ResourceBundle.getBundle("internationalization/messages", currentLocale);
        this.userCsvFile = userCsvFile;

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Allow editing for all columns except for Title, Author, Rating, and Status
                if (column == 3) { // Review column
                    // Allow editing if the current value is "No Review"
                    return getValueAt(row, column).toString().equals(rb.getString("noReview"));
                } else {
                    return column != 0 && column != 1 && column != 2 && column != 4;
                }
            }
        };

        model.addColumn(rb.getString("title"));
        model.addColumn(rb.getString("author"));
        model.addColumn(rb.getString("rating"));
        model.addColumn(rb.getString("review"));
        model.addColumn(rb.getString("status"));
        model.addColumn(rb.getString("timeSpent"));
        model.addColumn(rb.getString("startDate"));
        model.addColumn(rb.getString("endDate"));
        model.addColumn(rb.getString("userRating"));
        model.addColumn(rb.getString("userReview"));

        loadData(LogInPage.getUserFileName()); // Load data when the panel is created

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);
        table.getTableHeader().setForeground(Color.BLACK);

        // Add lines between rows and columns
        table.setShowGrid(true);
        table.setGridColor(Color.GRAY);

        table.getTableHeader().setReorderingAllowed(false);
        sorter = new TableRowSorter<>(model);
        sorting = new TableSorting(table, sorter);
        table.setRowSorter(sorter);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Search box and button
        JTextField searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(searchField.getText());
            }
        });

        JButton searchButton = new JButton(rb.getString("search"));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search(searchField.getText());
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Add Edit button
        JButton editButton = new JButton(rb.getString("edit"));
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String title = (String) model.getValueAt(selectedRow, 0);
                String author = (String) model.getValueAt(selectedRow, 1);
                String ratingStr = (String) model.getValueAt(selectedRow, 2);
                String review = (String) model.getValueAt(selectedRow, 3);

                // Parse the rating string into an integer
                int rating = 0;
                if (!ratingStr.equals("No Rating")) {
                    try {
                        rating = Integer.parseInt(ratingStr);
                    } catch (NumberFormatException ex) {
                        // Handle the case where the rating string cannot be parsed into an integer
                        System.err.println("Error parsing rating: " + ex.getMessage());
                        // You may choose to provide a default value or handle the error differently
                    }
                }

                EditBookWindow editWindow = new EditBookWindow(title, author, ratingStr, review, review); // Modified here
                editWindow.setVisible(true);
            }
        });

        // Add Remove button
        JButton removeButton = new JButton(rb.getString("remove"));
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    model.removeRow(selectedRow);
                    saveToCSV(LogInPage.getUserFileName()); // Save changes to CSV after removal
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.SOUTH);
    }

    private void search(String query) {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(query, 0, 1); // Searching in title and author columns
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    private void loadData(String userCsvFile) {
        try (Scanner scanner = new Scanner(new File(userCsvFile))) {

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                String[] newData = new String[model.getColumnCount()];
                for (int i = 0; i < data.length; i++) {
                    if (data[i].equals("null")) {
                        newData[i] = ""; // Replace "null" with an empty string
                    } else {
                        newData[i] = data[i];
                    }
                }
                // Ensure all columns have data
                for (int i = data.length; i < model.getColumnCount(); i++) {
                    newData[i] = "";
                }
                // Check if author field is empty, replace with "Unknown"
                if (newData.length > 1 && newData[1].isEmpty()) {
                    newData[1] = rb.getString("unknown");
                }
                // Check if rating field is empty, replace with "No Rating"
                if (newData.length > 2 && newData[2].isEmpty()) {
                    newData[2] = rb.getString("noRating");
                }
                // Check if review field is empty, replace with "No Review"
                if (newData.length > 3 && newData[3].isEmpty()) {
                    newData[3] = rb.getString("noReview");
                }
                // Check if status field contains text other than "Completed", "Ongoing", and "Not Started",
                // replace with an empty string
                if (newData.length > 4 && !newData[4].equals(rb.getString("completed")) && !newData[4].equals(rb.getString("ongoing")) && !newData[4].equals(rb.getString("notStarted"))) {
                    newData[4] = "";
                }
                model.addRow(newData);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveToCSV(String userCsvFile) {
        System.out.println(userCsvFile);
        try (FileWriter writer = new FileWriter(new File(userCsvFile))) { // Overwrite mode
            for (int row = 0; row < model.getRowCount(); row++) {
                StringBuilder line = new StringBuilder();
                for (int col = 0; col < model.getColumnCount(); col++) {
                    line.append(model.getValueAt(row, col));
                    if (col < model.getColumnCount() - 1) {
                        line.append(",");
                    }
                }
                writer.write(line.toString() + "\n");
            }
            JOptionPane.showMessageDialog(this, rb.getString("saveSuccessful") + userCsvFile, rb.getString("saveSuccessfulTitle"), JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, rb.getString("saveError") + userCsvFile, rb.getString("saveErrorTitle"), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String userCsvFile = LogInPage.getUserFileName(); // Specify the CSV file directly

        JFrame frame = new JFrame(rb.getString("personalLibrary"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.add(new PersonalBookPanel(userCsvFile));
        frame.setVisible(true);
    }

    public void addBook(String title, String author, String rating, String review) {
        String[] rowData = {title, author, rating.isEmpty() ? rb.getString("noRating") : rating, review.isEmpty() ? rb.getString("noReview") : review,
                "", "", "", "", "", ""}; // Added empty strings for additional columns
        model.addRow(rowData);
        saveToCSV(LogInPage.getUserFileName());
    }
}
