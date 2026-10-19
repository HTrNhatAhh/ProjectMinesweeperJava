import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MineSweeper extends JPanel implements AWTEventListener, ActionListener {

    private static final int MAX_BOMB_COUNT = 10;
    private int ROWS = 9, COLUMNS = 9, TOTAL = ROWS * COLUMNS;
    private JPanel pnlMain = new JPanel(new GridLayout(ROWS, COLUMNS));
    private JLabel lblBombCount = new JLabel(MAX_BOMB_COUNT + "");
    private JLabel lblTimer = new JLabel("0");
    private boolean isColorCheatOn = false;
    private JButton btnReset = new JButton("Reset");

    private GameState state = GameState.NotStarted;

    public MineSweeper() {
        setLayout(new BorderLayout());
        add(pnlMain, BorderLayout.CENTER);
        createButtons();
        addControlPanel();
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this, "<html>Author : Justin Cowles Cowpwethwaite <br>Version : 1.0</html>",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void restartGame() {
        state = GameState.NotStarted;
        lblTimer.setText("0");
        pnlMain.removeAll();
        createButtons();
        pnlMain.updateUI();
        lblBombCount.setText("" + MAX_BOMB_COUNT);
        lblBombCount.updateUI();
    }

    private void addControlPanel() {
        JPanel pnlTimer = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        pnlTimer.add(lblTimer);

        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnReset.setToolTipText("<HTML>Press <B>F2</B> to reset the current game</HTML>");
        pnl.add(lblBombCount);
        pnl.add(btnReset);
        JPanel pnlN = new JPanel(new GridLayout(1, 3));

        pnlN.add(pnl);
        pnlN.add(pnlTimer);
        add(pnlN, BorderLayout.NORTH);
        btnReset.addActionListener(this);
    }

    private boolean isBomb() {
        Random r = new Random();
        return r.nextInt(ROWS) == 1;
    }

    public static void main(String[] args) {
        JFrame fr = new JFrame("MineSweeper");
        fr.setLayout(new BorderLayout());
        fr.add(new MineSweeper());
        fr.setResizable(false);
        fr.setSize(250, 350);
        fr.setLocationRelativeTo(null);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setVisible(true);
    }

    public void eventDispatched(AWTEvent event) {
        if (KeyEvent.class.isInstance(event) && ((KeyEvent) (event)).getID() == KeyEvent.KEY_RELEASED) {
            if (((KeyEvent) (event)).getKeyCode() == KeyEvent.VK_F1) {
                showAbout();
            }
            if (((KeyEvent) (event)).getKeyCode() == KeyEvent.VK_F2) {
                restartGame();
            }
            if (((KeyEvent) (event)).getKeyCode() == KeyEvent.VK_F3) {
                isColorCheatOn = !isColorCheatOn;
                if (state == GameState.Playing) {
                    pnlMain.updateUI();
                }
            }

            if (((KeyEvent) (event)).getKeyCode() == KeyEvent.VK_F12) {
                for (Component c : pnlMain.getComponents()) {
                    GameButton b = (GameButton) c;
                    if (b.isBomb() == false) {
                        b.setState(State.Clicked);
                    } else {
                        b.setState(State.Marked);
                    }
                    b.setEnabled(false);
                }
                checkGameState();
            }
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnReset) {
            restartGame();
        }
    }
}