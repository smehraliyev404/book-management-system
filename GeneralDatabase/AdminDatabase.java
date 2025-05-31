package GeneralDatabase;

import login.LogInPage;
import sortingfiltering.TableSorting;
import usermanagement.UserManagementPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import internationalization.LanguagePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminDatabase extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private List<Book> books;
    private JScrollPane scrollPane;
    private JButton removeButton;
    private JButton editButton;
    private JButton addButton;
    private JTextField searchField;
    private JButton searchButton;
    private String filename = "general.csv";
    private TableRowSorter<DefaultTableModel> sorter;
    private TableSorting sorting;
    private Locale currentLocale;
    private static ResourceBundle rb;

    public AdminDatabase() {
        currentLocale = new Locale(LanguagePanel.getLanguage());
        rb = ResourceBundle.getBundle("internationalization/messages", currentLocale);

        model = new DefaultTableModel();
        model.addColumn(rb.getString("title"));
        model.addColumn(rb.getString("author"));
        model.addColumn(rb.getString("rating"));
        model.addColumn(rb.getString("review"));

        try {
            books = ReadCsv.readCSV(filename);
            populateTable();
        } catch (Exception e) {
            e.printStackTrace();
            books = new ArrayList<>();
        }

        table = new JTable(model);
        scrollPane = new JScrollPane(table);

        removeButton = new JButton(rb.getString("remove"));
        removeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
                saveToCSV(filename);
            } else {
                JOptionPane.showMessageDialog(removeButton, rb.getString("removeButton"));
            }
        });

        editButton = new JButton(rb.getString("edit"));
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String title = JOptionPane.showInputDialog(AdminDatabase.this, rb.getString("enterNewTitle"));
                String author = JOptionPane.showInputDialog(AdminDatabase.this, rb.getString("enterNewAuthor"));
                String rating = JOptionPane.showInputDialog(AdminDatabase.this, rb.getString("enterNewRating"));
                String review = JOptionPane.showInputDialog(AdminDatabase.this, rb.getString("enterNewReview"));                

                title = title.isEmpty() ? rb.getString("unknown") : title;
                author = author.isEmpty() ? rb.getString("unknown") : author;
                rating = rating.isEmpty() ? rb.getString("noRating") : rating;
                review = review.isEmpty() ? rb.getString("noReview") : review;

                model.setValueAt(title, selectedRow, 0);
                model.setValueAt(author, selectedRow, 1);
                model.setValueAt(rating, selectedRow, 2);
                model.setValueAt(review, selectedRow, 3);
                saveToCSV(filename);
            } else {
                JOptionPane.showMessageDialog(editButton, rb.getString("selectBookToEdit"));
            }
        });

        addButton = new JButton(rb.getString("add"));
        addButton.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(AdminDatabase.this, rb.getString("enterNewTitle"));
            String author = JOptionPane.showInputDialog(AdminDatabase.this, rb.getString("enterNewAuthor"));
            String rating = JOptionPane.showInputDialog(AdminDatabase.this, rb.getString("enterNewRating"));
            String review = JOptionPane.showInputDialog(AdminDatabase.this, rb.getString("enterNewReview"));   

            title = title.isEmpty() ? rb.getString("unknown") : title;
            author = author.isEmpty() ? rb.getString("unknown") : author;
            rating = rating.isEmpty() ? rb.getString("noRating") : rating;
            review = review.isEmpty() ? rb.getString("noReview") : review;

            model.addRow(new Object[]{title, author, rating, review});
            saveToCSV(filename);
        });

        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> search());


        JComboBox<String> selector = new JComboBox<>(new String[]{rb.getString("generalLibrary"), rb.getString("editUsersButton"), rb.getString("logoutText")});
        selector.addActionListener(e -> {
            String selectedOption = Objects.requireNonNull(selector.getSelectedItem()).toString();
            switch (selectedOption) {
                case "General Library":
                    break;

                case "Ümumi Kitabxana":
                    break;

                case "Edit Users":
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(new UserManagementPanel());
                    frame.revalidate();
                    frame.repaint();
                    break;

                case "İstifadəçiləri Redaktə Et":
                    JFrame frameAz = (JFrame) SwingUtilities.getWindowAncestor(this);
                    frameAz.getContentPane().removeAll();
                    frameAz.getContentPane().add(new UserManagementPanel());
                    frameAz.revalidate();
                    frameAz.repaint();
                    break;    

                case "Logout":
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    topFrame.dispose();
                    LogInPage loginPage = new LogInPage();
                    loginPage.setVisible(true);
                    break;

                case "Hesabdan çıxış":
                    JFrame topFrameAz = (JFrame) SwingUtilities.getWindowAncestor(this);
                    topFrameAz.dispose();
                    LogInPage loginPageAz = new LogInPage();
                    loginPageAz.setVisible(true);
                    break;
            }
        });

        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);
        table.getTableHeader().setForeground(Color.BLACK);

        table.getTableHeader().setReorderingAllowed(false);
        sorter = new TableRowSorter<>(model);
        sorting = new TableSorting(table, sorter);
        table.setRowSorter(sorter);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectorPanel.add(selector);
        add(selectorPanel, BorderLayout.NORTH);
    }

    private void populateTable() {
        for (Book book : books) {
            String author = book.getAuthor().isEmpty() ? rb.getString("unknown") : book.getAuthor();
            for (String title : book.getTitles()) {
                title = title.isEmpty() ? rb.getString("unknown") : title;
                model.addRow(new Object[]{title, author, rb.getString("noRating"), rb.getString("noReview")});
            }
        }
    }

    private void saveToCSV(String filename) {
    }

    private void search() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            resetSearch();
        } else {
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                String title = ((String) model.getValueAt(i, 0)).toLowerCase();
                String author = ((String) model.getValueAt(i, 1)).toLowerCase();
                if (!title.contains(query) && !author.contains(query)) {
                    model.removeRow(i);
                }
            }
        }
    }

    private void resetSearch() {
        // Restore the original state of the table
        model.setRowCount(0);
        populateTable();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(rb.getString("adminPanelTitle"));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.add(new AdminDatabase());
            frame.setVisible(true);
        });
    }
}