package usermanagement;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Objects;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.table.DefaultTableModel;

import GeneralDatabase.AdminDatabase;
import internationalization.LanguagePanel;
import login.LogInPage;

public class UserManagementPanel extends JPanel {
    private JTable userTable;
    private File credentialsFile = new File("login/credentials.csv");
    private Locale currentLocale;
    private ResourceBundle rb;

    public UserManagementPanel() {
        currentLocale = new Locale(LanguagePanel.getLanguage());
        rb = ResourceBundle.getBundle("internationalization/messages", currentLocale);

        setLayout(new BorderLayout());
        modelUsers();

        // JComboBox selector
        JComboBox<String> selector = new JComboBox<>(new String[]{"Edit Users", "General Library", "Logout"});
        selector.addActionListener(e -> {
            String selectedOption = Objects.requireNonNull(selector.getSelectedItem()).toString();
            switch (selectedOption) {
                case "General Library":
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(new AdminDatabase());
                    frame.revalidate();
                    frame.repaint();
                    break;

                case "Edit Users":
                    // No need to do anything here since we are already in the UserManagementPanel
                    break;

                case "Logout":
                    Container container = getParent();
                    while (!(container instanceof JFrame) && container != null) {
                        container = container.getParent();
                    }
                    if (container instanceof JFrame) {
                        JFrame topLevelFrame = (JFrame) container;
                        topLevelFrame.dispose();
                        new LogInPage();
                    }
                    break;
            }
        });

        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectorPanel.add(selector);
        add(selectorPanel, BorderLayout.NORTH);
    }

    private void modelUsers() {
        String[] columnNames = {rb.getString("username"), rb.getString("password")};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try (BufferedReader br = new BufferedReader(new FileReader(credentialsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) {
                    tableModel.addRow(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton deleteButton = new JButton(rb.getString("deleteUser"));
        deleteButton.addActionListener(e -> deleteSelectedUser());
        add(deleteButton, BorderLayout.SOUTH);
    }

    private void deleteSelectedUser() {
        int row = userTable.getSelectedRow();
        if (row >= 0) {
            ((DefaultTableModel) userTable.getModel()).removeRow(row);
            saveChangesToFile();
        }
    }

    private void saveChangesToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(credentialsFile, false))) {
            DefaultTableModel model = (DefaultTableModel) userTable.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                Object username = model.getValueAt(i, 0);
                Object password = model.getValueAt(i, 1);
                pw.println(username + "," + password);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, rb.getString("errorUpdatingFile"), rb.getString("errorTitle"), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}