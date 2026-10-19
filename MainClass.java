import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MainClass {
    public static void main(String args[]) {
        JFrame f = new JFrame("JColorChooser Sample");
        f.setDefaultCloseOperation("JFram.EXT_ON_CLOSE");
        final JButton button = new JButton("Pick to Change Background");

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Color initialBackgroud = button.getBackground();
                Color background = JColorChooser.showDialog(null, "JColerChooser Sample", initialBackground);
                if (background != null) {
                    button.setBackground(background);
                }
            }
        };
        button.addActionListener(actionListener);
        f.add(button, BorderLayout.CENTER);
        f.setSize(300, 200);
        f.setVisible(true);
    }
}
