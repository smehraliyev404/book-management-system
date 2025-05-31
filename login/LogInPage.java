package login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import AfterLogin.AdminPanel;
import GeneralDatabase.Panel;
import internationalization.LanguagePanel;
import loginexceptions.InvalidCredentials;

public class LogInPage extends JFrame {
    private static final String CREDENTIALS_FILE = "login/credentials.csv";
    private JPasswordField password;
    private static JTextField username;
    private JLabel labelPassword, labelUsername, welcomeMessage, info, registerLabel;
    private JButton buttonLogIn;
    private JPanel headerPanel, centralPanel, footerPanel, registerPanel;
    private GridBagConstraints gbcMain, gbcHeader, gbcCentral;
    private Locale currentLocale;
    private ResourceBundle rb;
    private static String userFileName;

    public static String getUserFileName() {
        return "login/logincsv/" + username.getText() + ".csv";
    }

    public LogInPage() {
        currentLocale = new Locale(LanguagePanel.getLanguage());
        rb = ResourceBundle.getBundle("internationalization/messages", currentLocale);

        this.setTitle(rb.getString("loginTitle"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setSize(550, 400);
        this.setLocationRelativeTo(null);

        gbcMain = new GridBagConstraints();
        gbcHeader = new GridBagConstraints();
        gbcCentral = new GridBagConstraints();

        headerPanel = new JPanel();
        headerPanel.setLayout(new GridBagLayout());
        centralPanel = new JPanel();
        centralPanel.setLayout(new GridBagLayout());
        footerPanel = new JPanel();
        footerPanel.setLayout(new GridLayout(2, 1));

        gbcMain.weighty = 1;

        welcomeMessage = new JLabel(rb.getString("welcomeMessage"));
        welcomeMessage.setFont(new Font("Arial", Font.BOLD, 18));
        gbcHeader.insets = new Insets(40, 0, 0, 0);
        headerPanel.add(welcomeMessage, gbcHeader);

        labelUsername = new JLabel(rb.getString("username") + ": ");
        labelUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        gbcCentral.insets = new Insets(0, 0, 5, 0);
        centralPanel.add(labelUsername, gbcCentral);

        gbcCentral.gridx = 1;
        username = new JTextField(20);
        username.setPreferredSize(new Dimension(20, 40));
        centralPanel.add(username, gbcCentral);

        gbcCentral.gridx = 0;
        gbcCentral.gridy = 1;
        labelPassword = new JLabel(rb.getString("password") + ": ");
        labelPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        centralPanel.add(labelPassword, gbcCentral);

        gbcCentral.gridx = 1;
        password = new JPasswordField(20);
        password.setPreferredSize(new Dimension(20, 40));
        centralPanel.add(password, gbcCentral);

        buttonLogIn = new JButton(rb.getString("loginText"));
        buttonLogIn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonLogIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateAndProcessLogin();
            }
        });
        JButton languageButton = new JButton(rb.getString("languageButton"));
        languageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLanguagePanel();
            }
        });

        footerPanel.add(languageButton);
        buttonLogIn.setPreferredSize(new Dimension(20, 40));
        footerPanel.add(buttonLogIn);

        registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        info = new JLabel(rb.getString("noAccountText"));
        registerPanel.add(info);

        registerLabel = new JLabel(rb.getString("registerText"));
        registerLabel.setForeground(Color.BLUE);
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openRegisterWindow(LogInPage.this);
            }
        });
        registerPanel.add(registerLabel);
        footerPanel.add(registerPanel);

        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        this.add(headerPanel, gbcMain);

        gbcMain.gridy = 1;
        this.add(centralPanel, gbcMain);

        gbcMain.gridy = 2;
        this.add(footerPanel, gbcMain);

        this.setVisible(true);
    }

    private void openSecondPanel() {
        this.dispose();
        JFrame frame = new JFrame(rb.getString("generalLibrary"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.add(new Panel());
        frame.setVisible(true);
    }

    private void openAdminPanel() {
        this.dispose();
        JFrame frame = new JFrame(rb.getString("adminPanelTitle"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.add(new AdminPanel());
        frame.setVisible(true);
    }

    private void openLanguagePanel() {
        LanguagePanel languagePanel = new LanguagePanel();
        JFrame languageFrame = new JFrame(rb.getString("languageFrame"));
        languageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        languageFrame.add(languagePanel);
        languageFrame.pack();
        languageFrame.setLocationRelativeTo(this);
        languageFrame.setVisible(true);

        dispose();
    }

    private void openRegisterWindow(JFrame logInPage) {
        RegistrationPage newRegisterWindow = new RegistrationPage();
        newRegisterWindow.setVisible(true);
        logInPage.dispose();
    }

    private void validateAndProcessLogin(){
        try {
            String enteredUsername = username.getText();
            String enteredPassword = new String(password.getPassword());
            Map<String, String> credentials = RegistrationPage.readCredentials(CREDENTIALS_FILE);

            if (enteredUsername.equals("admin") && enteredPassword.equals("admin")) {
                openAdminPanel();
            } else if (!(credentials.containsKey(enteredUsername) && credentials.get(enteredUsername).equals(enteredPassword))) {
                throw new InvalidCredentials(rb.getString("invalidCredentials"));
            } else {
                openSecondPanel();
            }
        } catch (InvalidCredentials ex) {
            JOptionPane.showMessageDialog(LogInPage.this, ex.getMessage(), rb.getString("errorTitle"), JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new LogInPage();
    }
}
