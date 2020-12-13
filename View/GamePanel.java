package View;

import Classes.Constants;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private Timer timer;

    public GamePanel(){
        initializeLayout();
    }

    private void initializeLayout() {
        setPreferredSize(new Dimension(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT));
        this.timer = new Timer(Constants.GAME_SPEED, new GameLoop(this));
    }

    public void doOneLoop() {

    }
}
