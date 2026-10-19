import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Board extends MineSweeper {
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
            updateBombs(lstBombsLocation, pnlMain.getComponents());
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
            Point location = ((GameButton) c).getPosition();
            int currrentPosition = location.x * COLUMNS + location.y;
            int bombLocation = r.nextInt(TOTAL);
            if (bombLocation == currrentPosition) {
                ((GameButton) c).setBomb(true);
                lstBombsLocation.add(((GameButton) c).getPosition());
                return;
            }
        }
    }

    private void updateBombCount(GameButton btn, Component[] components) {
        Point[] points = getSurroundings(btn.getPosition());

        for (Point p : points) {
            GameButton b = getButtonAt(components, p);
            if (b != null && b.isBomb()) {
                btn.setBombCount(btn.getBombCount() + 1);
            }
        }
        btn.setText(btn.getBombCount() + "");
    }

    private void updateSurroundingZeros(Point currentPoint) {
        Point[] points = getSurroundings(currentPoint);

        for (Point p : points) {
            GameButton b = getButtonAt(pnlMain.getComponents(), p);
            if (b != null && b.getBombCount() == 0 && b.getState() != State.Clicked && b.getState() != State.Marked
                    && b.isBomb() == false) {
                b.setState(State.Clicked);
                updateSurroundingZeros(b.getPosition());
                b.updateUI();
            }
            if (b != null && b.getBombCount() > 0 && b.getState() != State.Clicked && b.getState() != State.Marked
                    && b.isBomb() == false) {
                b.setEnabled(false);
                b.setState(State.Clicked);
                b.updateUI();
            }
        }
    }

    private Point[] getSurroundings(Point cPoint) {
        int cX = (int) cPoint.getX();
        int cY = (int) cPoint.getY();
        Point[] points = { new Point(cX - 1, cY - 1), new Point(cX - 1, cY), new Point(cX - 1, cY + 1),
                new Point(cX, cY - 1), new Point(cX, cY + 1),
                new Point(cX + 1, cY - 1), new Point(cX + 1, cY), new Point(cX + 1, cY + 1) };
        return points;
    }

    private GameButton getButtonAt(Component[] components, Point position) {
        for (Component btn : components) {
            if ((((GameButton) btn).getPosition().equals(position))) {
                return (GameButton) btn;
            }
        }
        return null;
    }

    private boolean checkGameState() {
        boolean isWin = false;
        for (Component c : pnlMain.getComponents()) {
            GameButton b = (GameButton) c;
            if (b.getState() != State.Clicked) {
                if (b.isBomb()) {
                    isWin = true;
                } else {
                    return false;
                }
            }
        }
        if (isWin) {
            state = GameState.Finished;
            for (Component c : pnlMain.getComponents()) {
                GameButton b = (GameButton) c;
                if (b.isBomb()) {
                    b.setState(State.Marked);
                }
                b.setEnabled(false);
            }
            lblBombCount.setText("0");
            JOptionPane.showMessageDialog(this, "You win!", "Congrats",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        return isWin;
    }

    private void blastBombs() {
        int blastCount = 0;
        for (Component c : pnlMain.getComponents()) {
            ((GameButton) c).setEnabled(false);
            ((GameButton) c).transferFocus();
            if (((GameButton) c).isBomb() && ((GameButton) c).getState() != State.Marked) {
                ((GameButton) c).setState(State.Clicked);
                ((GameButton) c).updateUI();
                blastCount++;
            }
            if (((GameButton) c).isBomb() == false && ((GameButton) c).getState() == State.Marked) {
                ((GameButton) c).setState(State.WrongMarked);
            }
        }
        lblBombCount.setText("" + blastCount);
        lblBombCount.updateUI();
        state = GameState.Finished;
        JOptionPane.showMessageDialog(this, "You loose the game :(", "Game Over", JOptionPane.ERROR_MESSAGE, null);
        for (Component c : pnlMain.getComponents()) {
            GameButton b = (GameButton) c;
            b.setEnabled(false);
        }
    }
}
