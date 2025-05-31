package AfterLogin;

import login.LogInPage;
import usermanagement.UserManagementPanel;
import GeneralDatabase.AdminDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import internationalization.LanguagePanel;
import java.util.Locale;
import java.util.ResourceBundle;

public class AdminPanel extends JPanel {
    private Locale currentLocale;
    private ResourceBundle rb;

    public AdminPanel() {
        currentLocale = new Locale(LanguagePanel.getLanguage());
        rb = ResourceBundle.getBundle("internationalization/messages", currentLocale);

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JPanel buttonPanel = createButtonPanel();
        this.add(buttonPanel, BorderLayout.CENTER);

        JButton logoutButton = new JButton(rb.getString("logoutText"));
        logoutButton.setPreferredSize(new Dimension(200, 30));
        logoutButton.addActionListener(this::logout);

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.add(logoutButton);
        this.add(logoutPanel, BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton generalLibraryButton = new JButton(rb.getString("generalLibrary"));
        generalLibraryButton.addActionListener(e -> switchToPanel(new AdminDatabase()));

        JButton editUsersButton = new JButton(rb.getString("editUsersButton"));
        editUsersButton.addActionListener(e -> switchToPanel(new UserManagementPanel()));

        panel.add(generalLibraryButton);
        panel.add(editUsersButton);
        return panel;
    }

    private void switchToPanel(JPanel newPanel) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        frame.add(newPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void logout(ActionEvent e) {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        currentFrame.dispose();

        JFrame loginFrame = new JFrame(rb.getString("loginTitle"));
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(550, 400);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.add(new LogInPage());
        loginFrame.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("adminPanelTitle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.add(new AdminPanel());
        frame.setVisible(true);
    }
}
