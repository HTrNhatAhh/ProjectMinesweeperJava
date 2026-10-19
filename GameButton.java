import java.awt.Color;
import java.awt.Point;
import javax.swing.JButton;

public class GameButton extends JButton {
    private boolean isBomb = false;
    private Point position = null;
    private int bombCount = 0;
    private State state = State.Initial;

    public void setState(State state) {
        this.state = state;
        if (getBombCount() == 0 && !isBomb) {
            setEnabled(false);
        }
    }

    public State getState() {
        return state;
    }

    public int getBombCount() {
        return bombCount;
    }

    public void setBombCount(int bombCount) {
        this.bombCount = bombCount;
    }

    public GameButton(Point position) {
        setPosition(position);
        setText(position.toString());
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean isBomb) {
        this.isBomb = isBomb;
    }

    @Override
    public String getText() {
        if (state == State.Initial) {
            return "";
        }
        if (state == State.Marked) {
            return "\u00B6";
        }
        if (state == State.Clicked) {
            if (isBomb) {
                return "<html><font size='16'><b>*</b></font></html>";
            } else {
                if (getBombCount() > 0)
                    return getBombCount() + "";
                else
                    return "";
            }
        }
        if (state == State.WrongMarked) {
            return "X";
        }
        return super.getText();
    }

    @Override
    public Color getBackground() {
        if (state == State.Clicked && isBomb) {
            return Color.RED;
        }
        if (isBomb) {
            return Color.MAGENTA;
        }
        if (state == State.Clicked && getBombCount() > 0) {
            return Color.GREEN;
        }
        if (isEnabled()) {
            return Color.YELLOW.brighter();
        }
        return super.getBackground();
    }
}
