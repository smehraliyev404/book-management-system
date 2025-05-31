package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import internationalization.LanguagePanel;
import login.LogInPage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class EditBookWindow extends JDialog {
    private JTextField titleField;
    private JTextField authorField;
    private JComboBox<Integer> ratingComboBox;
    private JTextArea reviewArea;
    private JComboBox<String> statusComboBox;
    private JTextField timeSpentField;
    private JTextField startDateField;
    private JTextField endDateField;
    private boolean confirmed;
    private String userFileName; // User's database file name
    private Locale currentLocale;
    private static ResourceBundle rb;

    public EditBookWindow(String title, String author, String rating, String review, String userFileName) {
        currentLocale = new Locale(LanguagePanel.getLanguage());
        rb = ResourceBundle.getBundle("internationalization/messages", currentLocale);
        this.userFileName = userFileName; // Assign user's database file name

        setTitle(rb.getString("editBookTitle"));
        setModal(true);
        setLayout(new BorderLayout());
        setSize(500, 600);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel(rb.getString("title") + ":");
        panel.add(titleLabel, gbc);
        gbc.gridx++;
        titleField = new JTextField(20);
        titleField.setText(title);
        titleField.setEditable(false);
        panel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel authorLabel = new JLabel(rb.getString("author") + ":");
        panel.add(authorLabel, gbc);
        gbc.gridx++;
        authorField = new JTextField(20);
        authorField.setText(author);
        authorField.setEditable(false);
        panel.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel ratingLabel = new JLabel(rb.getString("rating") + ":");
        panel.add(ratingLabel, gbc);
        gbc.gridx++;
        Integer[] ratings = new Integer[]{0, 1, 2, 3, 4, 5}; // Include 0 as an option when there is no rating
        ratingComboBox = new JComboBox<>(ratings);
        if (!rating.equals(rb.getString("noRating"))) {
            ratingComboBox.setSelectedItem(Integer.parseInt(rating)); // Set selected item based on the provided rating
        }
        panel.add(ratingComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel reviewLabel = new JLabel(rb.getString("review") + ":");
        panel.add(reviewLabel, gbc);
        gbc.gridx++;
        reviewArea = new JTextArea(5, 20);
        JScrollPane reviewScrollPane = new JScrollPane(reviewArea);
        reviewArea.setText(review);
        panel.add(reviewScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel statusLabel = new JLabel(rb.getString("status") + ":");
        panel.add(statusLabel, gbc);
        gbc.gridx++;
        statusComboBox = new JComboBox<>(new String[]{rb.getString("ongoing"), rb.getString("completed"), rb.getString("notStarted")});
        statusComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rb.getString("completed").equals(statusComboBox.getSelectedItem())) {
                    endDateField.setText(getCurrentDateTime());
                    // Calculate time spent when book is completed
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date startDate = dateFormat.parse(startDateField.getText());
                        Date endDate = new Date();
                        long timeDifference = endDate.getTime() - startDate.getTime();
                        long minutesSpent = timeDifference / (60 * 1000); // Convert milliseconds to minutes
                        timeSpentField.setText(String.valueOf(minutesSpent));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    endDateField.setText("");
                    timeSpentField.setText(""); // Clear time spent field for other statuses
                }
                if (rb.getString("notStarted").equals(statusComboBox.getSelectedItem())) {
                    startDateField.setText(""); // Clear the start date field
                    startDateField.setEditable(false); // Make it non-editable
                } else {
                    startDateField.setEditable(true); // Make it editable for other statuses
                    startDateField.setText(getCurrentDateTime()); // Set the current date and time
                }
            }
        });

        panel.add(statusComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel timeSpentLabel = new JLabel(rb.getString("timeSpentLabel"));
        panel.add(timeSpentLabel, gbc);
        gbc.gridx++;
        timeSpentField = new JTextField(10);
        panel.add(timeSpentField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel startDateLabel = new JLabel(rb.getString("startDateLabel"));
        panel.add(startDateLabel, gbc);
        gbc.gridx++;
        startDateField = new JTextField(20);
        startDateField.setEditable(false); // Make the field non-editable
        startDateField.setText(getCurrentDateTime()); // Set the current date and time
        panel.add(startDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel endDateLabel = new JLabel(rb.getString("endDateLabel"));
        panel.add(endDateLabel, gbc);
        gbc.gridx++;
        endDateField = new JTextField(20);
        endDateField.setEditable(false); // Make the field non-editable
        panel.add(endDateField, gbc);

        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton(rb.getString("save"));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = true;

                // Write data to the user's database file
                try (PrintWriter writer = new PrintWriter(new FileWriter(userFileName, true))) {
                    // Write the data in a format suitable for your table structure
                    writer.println(getTitleField() + "," + getAuthorField() + "," + getRating() + "," +
                            getReview() + "," + getStatus() + "," + getTimeSpent() + "," +
                            getStartDate() + "," + getEndDate());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Handle the exception appropriately
                }

                dispose();
            }
        });
        JButton cancelButton = new JButton(rb.getString("cancelButton"));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Define the date format
        return dateFormat.format(new Date()); // Format the current date and time
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getTitleField() {
        return titleField.getText();
    }

    public String getAuthorField() {
        return authorField.getText();
    }

    public Integer getRating() {
        String ratingString = ratingComboBox.getSelectedItem().toString();
        if (rb.getString("noRating").equals(ratingString)) {
            return null; // or any default value you want to use for "No Rating"
        } else {
            return Integer.parseInt(ratingString);
        }
    }
    

    public String getReview() {
        return reviewArea.getText();
    }

    public String getStatus() {
        return (String) statusComboBox.getSelectedItem();
    }
    public int getTimeSpent() {
        String timeSpentText = timeSpentField.getText();
        if (timeSpentText.isEmpty()) {
            return 0; // or any default value you want to use when the field is empty
        } else {
            return Integer.parseInt(timeSpentText);
        }
    }
    

    public String getStartDate() {
        return startDateField.getText();
    }

    public String getEndDate() {
        return endDateField.getText();
    }

    public static void main(String[] args) {
        // Test the EditBookWindow
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String userFileName = LogInPage.getUserFileName(); // Retrieve user's database file name
                EditBookWindow editWindow = new EditBookWindow(rb.getString("title"), rb.getString("author"), rb.getString("noRating"), rb.getString("sampleReview"), userFileName);
                editWindow.setVisible(true);
            }
        });
    }
}
