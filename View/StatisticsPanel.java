package View;

import Classes.Map;
import Constants.Constants;
import com.google.gson.Gson;

import javax.swing.*;
import static javax.swing.JOptionPane.showMessageDialog;
import java.awt.*;

import java.awt.event.ActionListener;
import  java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;

public class StatisticsPanel extends JPanel {
    GameMainFrame gameMainFrame;
    private Map map;
    Timer timer;
    int yOverflow;
    boolean isStarted;
    boolean isStopped;

    JButton startBtn;
    JButton stopBtn;
    JButton nextDayBtn;
    JButton saveStats;

    StopActionListener stopActionListener;
    StartActionListener startActionListener;
    NextDayActionListener nextDayActionListener;
    SaveStatsActionListener saveStatsActionListener;

    GamePanel gamePanel;

    JLabel dayInfo;
    JLabel numberOfAnimalsInfo;
    JLabel numberOfGrassesInfo;
    JLabel dominantGenotypesInfo;
    JLabel averageEnergyInfo;
    JLabel averageAgeOfDeathsInfo;
    JLabel averageNumberOfChildrenInfo;



    public StatisticsPanel(int yOverflow, Timer timer, GamePanel gamePanel, Map map, GameMainFrame gameMainFrame){
        initializeVariables(yOverflow, timer, gamePanel, map, gameMainFrame);
        initializeLayout();
    }

    private void initializeVariables(int yOverflow, Timer timer, GamePanel gamePanel, Map map, GameMainFrame gameMainFrame){
        this.yOverflow = yOverflow;
        this.timer = timer;
        this.isStarted = true;
        this.isStopped = false;
        this.gameMainFrame = gameMainFrame;

        stopActionListener = new StopActionListener();
        startActionListener = new StartActionListener();
        nextDayActionListener = new NextDayActionListener();
        saveStatsActionListener = new SaveStatsActionListener();

        this.gamePanel = gamePanel;
        this.map = map;

        stopBtn = new JButton("STOP");
        startBtn = new JButton("START");
        nextDayBtn = new JButton("NEXT DAY");
        saveStats = new JButton("SAVE STATS");

        dayInfo = new JLabel("  Day: " + map.getDay());
        numberOfAnimalsInfo = new JLabel("  Number of animals: " + map.getNumberOfAnimals());
        numberOfGrassesInfo = new JLabel("  Number of grasses: " + map.getNumberOfGrasses());
        dominantGenotypesInfo = new JLabel("  Dominant genotype: " + map.getDominantGenotype());
        averageEnergyInfo = new JLabel("  Average animal energy: " + map.getAverageEnergy());
        averageAgeOfDeathsInfo = new JLabel("  Average age of deaths: " + map.getAverageAgeOfDeaths());
        averageNumberOfChildrenInfo = new JLabel("  Average number of children: " + map.getAverageNumberOfChildren());
    }

    private void initializeLayout() {
        setPreferredSize(new Dimension(Constants.BOARD_WIDTH / 2, Constants.BOARD_HEIGHT - yOverflow));
        setLayout(new GridLayout(0,1));

        startBtn.setEnabled(isStopped);
        nextDayBtn.setEnabled(isStopped);
        saveStats.setEnabled(isStopped);

        this.add(dayInfo);
        this.add(numberOfAnimalsInfo);
        this.add(numberOfGrassesInfo);
        this.add(dominantGenotypesInfo);
        this.add(averageEnergyInfo);
        this.add(averageAgeOfDeathsInfo);
        this.add(averageNumberOfChildrenInfo);

        this.add(stopBtn);
        this.add(startBtn);
        this.add(nextDayBtn);
        this.add(saveStats);

        stopBtn.addActionListener(stopActionListener);
        startBtn.addActionListener(startActionListener);
        nextDayBtn.addActionListener(nextDayActionListener);
        saveStats.addActionListener(saveStatsActionListener);
    }

    private class StopActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            if(isStarted){
                timer.stop();
                isStarted = false;
                isStopped = true;
                stopBtn.setEnabled(isStarted);
                startBtn.setEnabled(isStopped);
                nextDayBtn.setEnabled(isStopped);
                saveStats.setEnabled(isStopped);
            }

        }
    }
    
    private class StartActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            if(isStopped){
                timer.start();
                isStarted = true;
                isStopped = false;
                stopBtn.setEnabled(isStarted);
                startBtn.setEnabled(isStopped);
                nextDayBtn.setEnabled(isStopped);
                saveStats.setEnabled(isStopped);
            }

        }
    }
    private class NextDayActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            if(isStopped){
                gamePanel.doOneLoop();
                updateStats();
            }

        }
    }

    private class SaveStatsActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Gson gson = new Gson();

            StatsToSave statsToSave = new StatsToSave(Constants.NUMBER_OF_TILES_X, Constants.NUMBER_OF_TILES_Y,
                    Constants.START_ENERGY, Constants.NUMBER_OF_ANIMALS, Constants.MOVE_ENERGY, Constants.PLANT_ENERGY,
                    Constants.JUNGLE_RATIO, map.getDay(), map.getNumberOfAnimals(), map.getNumberOfGrasses(), map.getDominantGenotype(),
                    map.getAverageEnergy(), map.getAverageAgeOfDeaths(), map.getAverageNumberOfChildren());

            try {
                String jsonString = gson.toJson(statsToSave);

                // Na macu zapisuje dane na pulpit
                String userHomeFolder = System.getProperty("user.home");
                userHomeFolder += "/Desktop/";

                FileWriter writer = new FileWriter(userHomeFolder + "EvolutionStats.json");
                writer.write(jsonString);
                writer.close();
                showMessageDialog(null, "Stats saved on Desktop (work on the mac)!");
            } catch (IOException e2) {
                System.out.println("exception " + e2.getMessage());
                e2.printStackTrace();
            }


        }
    }

    public class StatsToSave{
        // Start Values
        int numberOfTilesX;
        int numberOfTilesY;
        int startAnimalEnergy;
        int numberOfStartAnimals;
        int moveEnergy;
        int plantEnergy;
        double jungleRatio;

        // Current Map Stats
        int day;
        int numberOfAnimals;
        int numberOfGrasses;
        int dominantGenotype;
        double averageEnergy;
        double averageAgeOfDeaths;
        double averageNumberOfChildren;

        public StatsToSave(int numberOfTilesX, int numberOfTilesY, int startAnimalEnergy, int numberOfStartAnimals,
                           int moveEnergy, int plantEnergy, double jungleRatio, int day, int numberOfAnimals,
                           int numberOfGrasses, int dominantGenotype, double averageEnergy, double averageAgeOfDeaths,
                           double averageNumberOfChildren){
            this.numberOfTilesX = numberOfTilesX;
            this.numberOfTilesY = numberOfTilesY;
            this.startAnimalEnergy = startAnimalEnergy;
            this.numberOfStartAnimals = numberOfStartAnimals;
            this.moveEnergy = moveEnergy;
            this.plantEnergy = plantEnergy;
            this.jungleRatio = jungleRatio;
            this.day = day;
            this.numberOfAnimals = numberOfAnimals;
            this.numberOfGrasses = numberOfGrasses;
            this.dominantGenotype = dominantGenotype;
            this.averageEnergy = averageEnergy;
            this.averageAgeOfDeaths = averageAgeOfDeaths;
            this.averageNumberOfChildren = averageNumberOfChildren;
        }
    }

    public void updateStats(){
        dayInfo.setText("  Day: " + map.getDay());
        numberOfAnimalsInfo.setText("  Number of animals: " + map.getNumberOfAnimals());
        numberOfGrassesInfo.setText("  Number of grasses: " + map.getNumberOfGrasses());
        dominantGenotypesInfo.setText("  Dominant genotype: " + map.getDominantGenotype());
        averageEnergyInfo.setText("  Average animal energy: " + map.getAverageEnergy());
        averageAgeOfDeathsInfo.setText("  Average age of deaths: " + map.getAverageAgeOfDeaths());
        averageNumberOfChildrenInfo.setText("  Average number of children: " + map.getAverageNumberOfChildren());
    }


}
