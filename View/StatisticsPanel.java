package View;

import Constants.Constants;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionListener;
import  java.awt.event.ActionEvent;

public class StatisticsPanel extends JPanel {
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


    public StatisticsPanel(int yOverflow, Timer timer, GamePanel gamePanel){
        this.yOverflow = yOverflow;
        this.timer = timer;
        this.isStarted = true;
        this.isStopped = false;
        stopActionListener = new StopActionListener();
        startActionListener = new StartActionListener();
        nextDayActionListener = new NextDayActionListener();
        this.gamePanel = gamePanel;

        initializeLayout();
    }

    private void initializeLayout() {
        setPreferredSize(new Dimension(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT - yOverflow));
        stopBtn = new JButton("STOP");
        startBtn = new JButton("START");
        nextDayBtn = new JButton("NEXT DAY");

        startBtn.setEnabled(isStopped);
        nextDayBtn.setEnabled(isStopped);

        this.add(stopBtn);
        this.add(startBtn);
        this.add(nextDayBtn);

        stopBtn.addActionListener(stopActionListener);
        startBtn.addActionListener(startActionListener);
        nextDayBtn.addActionListener(nextDayActionListener);

        //Adding Action Listener Methods
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


}
