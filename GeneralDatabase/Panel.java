package GeneralDatabase;

import PersonalDatabase.PersonalBookPanel;
import internationalization.LanguagePanel;
import login.LogInPage;
import sortingfiltering.TableSorting;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Panel extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private List<Book> books;
    private PersonalBookPanel personalBookPanel;
    private JComboBox<String> databaseSelector;
    private JScrollPane scrollPane;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;
    private JButton addButton;
    private Locale currentLocale;
    private static ResourceBundle rb;
    private TableSorting sorting;

    public Panel() {
        currentLocale = new Locale(LanguagePanel.getLanguage());
        rb = ResourceBundle.getBundle("internationalization/messages", currentLocale);

        model = new DefaultTableModel();
        model.addColumn(rb.getString("title"));
        model.addColumn(rb.getString("author"));
        model.addColumn(rb.getString("rating"));
        model.addColumn(rb.getString("review"));

        try {
            books = ReadCsv.readCSV("brodsky.csv");
        } catch (IOException e) {
            e.printStackTrace();
            books = new ArrayList<>();
        }

        for (Book book : books) {
            String author = book.getAuthor().isEmpty() ? rb.getString("unknown") : book.getAuthor();
            for (String title : book.getTitles()) {
                title = title.isEmpty() ? rb.getString("unknown") : title;
                model.addRow(new Object[]{title, author, rb.getString("noRating"), rb.getString("noReview"), rb.getString("add")});
            }
        }

        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3;
            }
        };
        table.getTableHeader().setReorderingAllowed(false);
        sorter = new TableRowSorter<>(model);
        sorting = new TableSorting(table, sorter);
        table.setRowSorter(sorter);

        personalBookPanel = new PersonalBookPanel(getName());
        scrollPane = new JScrollPane(table);

        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                search(searchField.getText());
            }
            
            public void removeUpdate(DocumentEvent e) {
                search(searchField.getText());
            }
            
            public void changedUpdate(DocumentEvent e) {
                search(searchField.getText());
            }
            
            private void search(String str) {
                RowFilter<DefaultTableModel, Object> rf = null;
                try {
                    rf = RowFilter.regexFilter("(?i)" + str);
                } catch (java.util.regex.PatternSyntaxException e) {
                    return;
                }
                sorter.setRowFilter(rf);
            }
            
        });

        databaseSelector = new JComboBox<>(new String[]{rb.getString("generalLibrary"), rb.getString("personalLibrary"), rb.getString("logoutText")});
        databaseSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDatabase = (String) databaseSelector.getSelectedItem();
                if (selectedDatabase.equals(rb.getString("generalLibrary"))) {
                    remove(personalBookPanel);
                    remove(addButton);
                    add(scrollPane, BorderLayout.CENTER);
                    add(addButton, BorderLayout.SOUTH); // Add the button back
                    table.setEnabled(true);
                } 
                else if (selectedDatabase.equals(rb.getString("logoutText"))){
                    Container container = getParent();
                    while (!(container instanceof JFrame) && container != null) {
                        container = container.getParent();
                    }
                    if (container instanceof JFrame) {
                        JFrame topLevelFrame = (JFrame) container;
                        topLevelFrame.dispose();
                        new LogInPage();
                    }
                }
                else {
                    remove(scrollPane);
                    remove(addButton);
                    add(personalBookPanel, BorderLayout.CENTER);
                    table.setEnabled(false);
                }
                revalidate();
                repaint();
            }
        });

        addButton = new JButton(rb.getString("add"));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String title = (String) table.getValueAt(selectedRow, 0);
                    String author = (String) table.getValueAt(selectedRow, 1);
                    
                    String filePath = LogInPage.getUserFileName(); // Assuming this retrieves user's file name
                    System.out.println(filePath);
                    if (filePath != null && !filePath.isEmpty()) {
                        try (FileWriter writer = new FileWriter(filePath, true)) {
                            writer.append(title + "," + author + ",,\n");
                            writer.flush();
                            personalBookPanel.addBook(title, author, "", ""); // Add the book to PersonalBookPanel
                            JOptionPane.showMessageDialog(personalBookPanel, rb.getString("bookAdded") + ": " + title);
                        } catch (IOException ex) {
                            ex.printStackTrace(); // Handle file I/O errors appropriately
                        }
                    } else {
                        JOptionPane.showMessageDialog(addButton, rb.getString("userFileNameNotFound"));
                    }
                } else {
                    JOptionPane.showMessageDialog(addButton, rb.getString("selectBookToAdd"));
                }
            }
        });

        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);
        table.getTableHeader().setForeground(Color.BLACK);

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel(rb.getString("search") + ":"));
        searchPanel.add(searchField);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(databaseSelector);
        topPanel.add(searchPanel);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(topPanel, BorderLayout.NORTH); // Adding topPanel instead of databaseSelector and searchPanel
        add(scrollPane, BorderLayout.CENTER);
        add(addButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(rb.getString("generalLibrary"));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.add(new Panel());
            frame.setVisible(true);
        });
    }
}
