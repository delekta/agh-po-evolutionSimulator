package View;

import Classes.Map;
import Constants.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.awt.event.ActionListener;
import  java.awt.event.ActionEvent;

public class StatisticsPanel extends JPanel {
    private static Map map;
    Timer timer;
    int yOverflow;
    boolean isStarted;
    boolean isStopped;

    JButton startBtn;
    JButton stopBtn;
    JButton nextDayBtn;

    StopActionListener stopActionListener;
    StartActionListener startActionListener;
    NextDayActionListener nextDayActionListener;

    GamePanel gamePanel;

    static JLabel dayInfo;
    static JLabel numberOfAnimalsInfo;
    static JLabel numberOfGrassesInfo;
    static JLabel dominantGenotypesInfo;
    static JLabel averageEnergyInfo;
    static JLabel averageAgeOfDeathsInfo;
    static JLabel averageNumberOfChildrenInfo;



    public StatisticsPanel(int yOverflow, Timer timer, GamePanel gamePanel, Map map){
        initializeVariables(yOverflow, timer, gamePanel, map);
        initializeLayout();
    }

    private void initializeVariables(int yOverflow, Timer timer, GamePanel gamePanel, Map map){
        this.yOverflow = yOverflow;
        this.timer = timer;
        this.isStarted = true;
        this.isStopped = false;
        stopActionListener = new StopActionListener();
        startActionListener = new StartActionListener();
        nextDayActionListener = new NextDayActionListener();
        this.gamePanel = gamePanel;
        this.map = map;

        stopBtn = new JButton("STOP");
        startBtn = new JButton("START");
        nextDayBtn = new JButton("NEXT DAY");

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

        stopBtn.addActionListener(stopActionListener);
        startBtn.addActionListener(startActionListener);
        nextDayBtn.addActionListener(nextDayActionListener);
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
            }

        }
    }
    private class NextDayActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            if(isStopped){
                gamePanel.doOneLoop();
            }

        }
    }

    public static void updateStats(){
        dayInfo.setText("  Day: " + map.getDay());
        numberOfAnimalsInfo.setText("  Number of animals: " + map.getNumberOfAnimals());
        numberOfGrassesInfo.setText("  Number of grasses: " + map.getNumberOfGrasses());
        dominantGenotypesInfo.setText("  Dominant genotype: " + map.getDominantGenotype());
        averageEnergyInfo.setText("  Average animal energy: " + map.getAverageEnergy());
        averageAgeOfDeathsInfo.setText("  Average age of deaths: " + map.getAverageAgeOfDeaths());
        averageNumberOfChildrenInfo.setText("  Average number of children: " + map.getAverageNumberOfChildren());
    }


}
