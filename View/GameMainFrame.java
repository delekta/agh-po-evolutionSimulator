package View;
import Objects.Map;
import Constants.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static javax.swing.JOptionPane.showMessageDialog;

public class  GameMainFrame extends JFrame implements ActionListener {
    Map map;
    int sizeOfTile;
    int xOverflow;
    int yOverflow;
    Timer timer;
    StatisticsPanel statisticsPanel;
    GamePanel gamePanel;

    public GameMainFrame() throws IOException {
        initializeVariables();
        initializeLayout();

    }

    private void initializeVariables() throws IOException{
        this.sizeOfTile = (int) ((Math.min(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT) / Math.max(Constants.NUMBER_OF_TILES_X, Constants.NUMBER_OF_TILES_Y)));
        this.xOverflow = Constants.BOARD_WIDTH - sizeOfTile * Constants.NUMBER_OF_TILES_X;
        this.yOverflow = Constants.BOARD_HEIGHT - sizeOfTile * Constants.NUMBER_OF_TILES_Y;
        this.map = new Map(Constants.NUMBER_OF_TILES_Y, Constants.NUMBER_OF_TILES_X, Constants.START_ENERGY, Constants.MOVE_ENERGY, Constants.PLANT_ENERGY, Constants.JUNGLE_RATIO);
        map.placeNAnimalsOnMap(Constants.NUMBER_OF_ANIMALS);
        this.timer = new Timer(Constants.GAME_SPEED, this);
        this.gamePanel = new GamePanel(this.sizeOfTile, this.xOverflow, this.yOverflow, map, this, timer);
        this.statisticsPanel = new StatisticsPanel(this.yOverflow, this.timer, this.gamePanel, map, this);
    }

    private void initializeLayout() throws IOException {
        this.add(statisticsPanel, BorderLayout.LINE_START);
        this.add(gamePanel, BorderLayout.LINE_END);
        setTitle(Constants.TITLE);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gamePanel.doOneLoop();
        if(map.areAllAnimalsDead()){
            timer.stop();
            showMessageDialog(null, "All animals are dead!");
            statisticsPanel.disableButtons();
        }
        else {
            statisticsPanel.updateMapStats();
        }
    }
}
