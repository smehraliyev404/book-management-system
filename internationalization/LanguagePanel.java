package internationalization;

import javax.swing.*;

import login.LogInPage;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

public class LanguagePanel extends JPanel implements ActionListener {

  private JButton englishButton, azerbaijaniButton;
  private static String language;

  public static String getLanguage() {
    return language;
}

public LanguagePanel() {
    setLayout(new FlowLayout());

    englishButton = new JButton("English");
    azerbaijaniButton = new JButton("Azərbaycan Dili");

    englishButton.addActionListener(this);
    azerbaijaniButton.addActionListener(this);

    add(englishButton);
    add(azerbaijaniButton);

    language = Locale.getDefault().getLanguage();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == englishButton) {
      language = "en";
    } else if (e.getSource() == azerbaijaniButton) {
      language = "az";
    }

    LogInPage loginPage = new LogInPage();
    loginPage.setVisible(true);

    ((JFrame) getTopLevelAncestor()).dispose();
  }

}
