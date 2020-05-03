package lines.Iline;

import javafx.scene.layout.Pane;

public interface iVehicle{
    void draw(Pane mapCanvas);
    void erase(Pane mapCanvas);
    void ride();
    void move(int x, int y);
    void setForward(boolean setTo);
    int getStartPosition();
    boolean getForward();
}