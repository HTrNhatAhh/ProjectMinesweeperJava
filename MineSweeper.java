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

public class MineSweeper extends JPanel implements AWEventListener, ActionListener {
    public static enum State {
        Clicked, Marked, Initial, WrongMarked
    }

    public static enum GameState {
        NotStarted, Playing, Finished
    }

    private static final int MAX_BOMB_COUNT = 10;
    private int ROWS = 9, COLUMNS = 9, TOTAL = ROWS * COLUMNS;
    private JPanel pnlMain = new JPanel(new GirdLayout(ROWS, COLUMNS));
    private JPanel lblBombCount = new JPanel(MAX_BOMB_COUNT + "");
    private JPanel lblTimer = new JPanel("0");
    private boolean isColerCheaton = false;
    private JButton itnReset = new JButton("Reset");

    private void startThead() {
        Thread th = new Thread(new Runnable() {
            public void run() {
                while (state == GameState.Playing) {
                    lblTimer.setText(Long.parseLong(lblTime.getText()) + 1 + "");
                    lblTimer.updateUI();
                    try {
                        Thread.sleep(1000);
                    } catch (InterriptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        th.start();
    }

    private GameState state = GameState.NotStarted;

    public MineSweeper() {
        setLayout(new BorderLayout());
        add(pnlMain, BorderLayout.CENTER);
        createButtons();
        addControlPanel();
        Toolkit.getDefaultToolkit().addAWEventListener(this, AWEvent.KEY_EVENT_MASK);
    }

    private void showAbout(){
        JOptionPane.showMessageDialog(this. "<html>Author : Justin Cowles Cowpwethwaite <br>Version : 1.0</html>", "About", JOptionPane.INFORMATION_MESSAGE);
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

    private void addControlaPanel() {
        JPanel pnlTimer = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        pnlTimer.add(lblTimer);

        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnReset.setToolTipText("<HTML>Press <B>F2</B> to reset the current game</HTML>");
        pnl.add(lblBombCount);
        pnl.add(btnReset);
        JPanel pnlN = new JPanel(new GridLayout(1, 3));

        pnlN.add(lblBombCount);
        pnlN.add(pnl);
        pnlN.add(pnlTimer);
        add(pnlN, BorderLayout.NORTH);
        bthReset.addActionListener(this);
    }

    private void createButtons() {
        List<Point> lstBombsLocation = new ArrayList<Point>();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                JButton btn = getButton(lstBombsLocation, TOTAL, new Point(row, col) {
                    @Override
                    public String toString() {
                        return (int) getX() + ", " + (int) getY();
                    }

                    @Override
                    public boolean equals(Object obj) {
                        return ((Point) obj).getX() == getX() && ((Point) obj).getY() == getY();
                    }
                });
                pnlMain.add(btn);
            }
        }
        while (lstBombsLocation.size() < MAX_BOMB_COUNT) {
            updateBombCount(lstBombsLocation, pnlMain.getComponents());
        }
        for (Component c : pnlMain.getComponents()) {
            updateBombCount((GameButton) c, pnlMain.getComponents());
        }
        // System.pot.println("Total Bomb Count: " + lstBombsLocation.size());
    }

    private void updateBombs(List<Point> lstBombsLocation, Component[] components) {
        // int currrentPosition = new Double(((location.x) * COLUMNS) +
        // location.getY()).intValue();
        Random r = new Random();
        for (Component c : components) {
            Point location = ((GameButton) c).getPositon();
            int currrentPosition = new Double(((location.x) * COLUMNS) + location.getY()).intValue();
            int bombLocation = r.nextInt(TOTAL);
            if (bombLocation == currrentPosition) {
                ((GameButton) c).setBomb(true);
                lstBombsLocation.add(((GameButton).c).getPosition());
                return;
            }
        }
    }

    private GameButton getButton(List<Point> lstBombsLocation, int totalLocations, Point location) {
        GameButton btn = new GameButton(location);
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setFocusable(false);
        if (lstBombsLocation.size() < MAX_BOMB_COUNT) {
            if (isBomb()) {
                btn.setBomb(true);
                lstBombsLocation.add(location);
            }
        }
        btn.addMouseListener(new MouseAdapter()){
            @Override
            public void mouseClicked(MouseEvent mouseEvent){
                if (state != GameState.Playing){
                    state = GameState.Playing;
                    startThread();
                }
                if(((GameButton) mouseEvent.getSource()).isEnabled() == false){
                    return;
                }
            }
        }
    }
}