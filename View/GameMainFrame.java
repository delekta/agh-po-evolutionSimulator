package View;

import Classes.Map;
import Constants.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class  GameMainFrame extends JFrame {
    int sizeOfTile;
    int xOverflow;
    int yOverflow;
    private Map map;
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
        this.map.placeNAnimalsOnMap(Constants.NUMBER_OF_ANIMALS);

        this.gamePanel = new GamePanel(this.sizeOfTile, this.xOverflow, this.yOverflow, this.map);
        this.timer = new Timer(Constants.GAME_SPEED, new GameLoop(gamePanel));
        this.statisticsPanel = new StatisticsPanel(this.yOverflow, this.timer, this.gamePanel, this.map);
    }

    private void initializeLayout() throws IOException {


        this.add(statisticsPanel, BorderLayout.LINE_START);
        this.add(gamePanel, BorderLayout.LINE_END);
        setTitle(Constants.TITLE);
        this.timer.start();



        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

    }
}
