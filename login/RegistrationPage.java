package login;

import javax.swing.*;

import loginexceptions.EmptyUsernamePasswordException;
import loginexceptions.InvalidPasswordException;
import loginexceptions.InvalidUsernameException;
import internationalization.LanguagePanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


public class RegistrationPage extends JFrame {
    private static final String CREDENTIALS_DIRECTORY = "login/logincsv/";
    private static final String CREDENTIALS_FILE = "login/credentials.csv";
    private JPasswordField password;
    private JTextField username;
    private JLabel labelPassword, labelUsername, info, logInLabel, welcomeMessage;
    private JButton buttonSignUp;
    private JPanel headerPanel, centralPanel, footerPanel, logInPanel;
    private GridBagConstraints gbcMain, gbcHeader, gbcCentral;
    private Locale currentLocale;
    private ResourceBundle rb;

    RegistrationPage() {
        currentLocale = new Locale(LanguagePanel.getLanguage());
        rb = ResourceBundle.getBundle("internationalization/messages", currentLocale);

        this.setTitle(rb.getString("registrationTitle"));
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

        buttonSignUp = new JButton(rb.getString("signupText"));
        buttonSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewUser(username, password);
            }
        });

        buttonSignUp.setPreferredSize(new Dimension(20, 40));
        footerPanel.add(buttonSignUp);

        logInPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        info = new JLabel(rb.getString("alreadyHaveAccount"));
        logInPanel.add(info);
        logInLabel = new JLabel(rb.getString("loginText"));
        logInLabel.setForeground(Color.BLUE);
        logInLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logInLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openRegisterWindow();
            }
        });
        logInPanel.add(logInLabel);
        footerPanel.add(logInPanel);

        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        this.add(headerPanel, gbcMain);

        gbcMain.gridy = 1;
        this.add(centralPanel, gbcMain);

        gbcMain.gridy = 2;
        this.add(footerPanel, gbcMain);

        this.setVisible(true);
    }

    private void openRegisterWindow() {
        LogInPage newLogInWindow = new LogInPage();
        newLogInWindow.setVisible(true);
        this.dispose();
    }

    private void createNewUser(JTextField username, JPasswordField password){
        try{
            String enteredUsername = username.getText();
            String enteredPassword = new String(password.getPassword()); // getPassword() method returns char array.
            Map<String, String> credentials = readCredentials(CREDENTIALS_FILE);

            if (enteredUsername.length() == 0 && enteredPassword.length() == 0){
                throw new EmptyUsernamePasswordException(rb.getString("emptyUsernamePassword"));
            }

            else if (enteredUsername.length() < 6 || enteredUsername.contains(" ")){
                throw new InvalidUsernameException(rb.getString("invalidUsername"));
            }

            else if (credentials.containsKey(enteredUsername)){
                throw new InvalidUsernameException(rb.getString("usernameTaken"));
            }

            else if (!isValidPassword(enteredPassword)){
                throw new InvalidPasswordException(rb.getString("invalidPassword"));
            }

            else{
                credentials.put(enteredUsername, enteredPassword);
                writeCredentials(CREDENTIALS_FILE, credentials);
                createDatabaseFile(enteredUsername);

                JOptionPane.showMessageDialog(RegistrationPage.this, rb.getString("accountCreated"), rb.getString("successTitle"), JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch (EmptyUsernamePasswordException e){
            JOptionPane.showMessageDialog(RegistrationPage.this, e.getMessage(), rb.getString("errorTitle"), JOptionPane.ERROR_MESSAGE);
        }
        catch (InvalidUsernameException e){
            JOptionPane.showMessageDialog(RegistrationPage.this, e.getMessage(), rb.getString("errorTitle"), JOptionPane.ERROR_MESSAGE);
        }
        catch (InvalidPasswordException e){
            JOptionPane.showMessageDialog(RegistrationPage.this, e.getMessage(), rb.getString("errorTitle"), JOptionPane.ERROR_MESSAGE);
        }
        finally{
            username.setText("");
            password.setText("");
        }
    }

    private boolean isValidPassword(String enteredPassword){
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialCharacter = false;
        boolean isLong = false;
        boolean hasNoSpace = true;
        String specialCharacters = "~`!@#$%^&*()_+-={}[]\\|:;\"'?/<>,.";

        if (enteredPassword.length() >= 8){
            isLong = true;
        }

        for (char c : enteredPassword.toCharArray()){
            if (Character.isUpperCase(c)){
                hasUppercase = true;
            }
            else if (Character.isLowerCase(c)){
                hasLowercase = true;
            }
            else if (Character.isDigit(c)){
                hasDigit = true;
            }
            else if (specialCharacters.contains(String.valueOf(c))){
                hasSpecialCharacter = true;
            }
            else if (Character.isWhitespace(c)){
                hasNoSpace = false;
            }
        }
        return hasUppercase && hasLowercase && hasDigit && hasSpecialCharacter && isLong && hasNoSpace;
    }

    public static Map<String, String> readCredentials(String filename){
        Map<String, String> credentials = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                credentials.put(user[0], user[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return credentials;
    }

    private void createDatabaseFile(String username) {
        String userDatabaseFile = CREDENTIALS_DIRECTORY + username + ".csv";
        File file = new File(userDatabaseFile);
        try {
            if (file.createNewFile()) {
                System.out.println("Database file created: " + file.getName());
            } else {
                System.out.println("Database file already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeCredentials(String filename, Map<String, String> credentials) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String username : credentials.keySet()) {
                String password = credentials.get(username);
                writer.write(username + "," + password);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
