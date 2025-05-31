import javax.swing.JFrame;

import internationalization.LanguagePanel;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Language Selection/Dil Seçimi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);
        frame.setLocationRelativeTo(null);
        frame.add(new LanguagePanel());
        frame.setVisible(true);
    }
}
