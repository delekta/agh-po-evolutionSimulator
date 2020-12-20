package View;
import Objects.Map;
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
    GamePanel gamePanel;
    private Map map;
    Timer timer;
    private int yOverflow;
    boolean isStarted;
    boolean isStopped;
    JButton startBtn;
    JButton stopBtn;
    JButton nextDayBtn;
    JButton saveStats;
    JButton markDominantGenotype;
    JButton trackAnimal;
    StopActionListener stopActionListener;
    StartActionListener startActionListener;
    NextDayActionListener nextDayActionListener;
    SaveStatsActionListener saveStatsActionListener;
    MarkDominantGenotypeActionListener markDominantGenotypeActionListener;
    TrackAnimalActionListener trackAnimalActionListener;
    JLabel mapStatisticsTitle;
    JLabel dayInfo;
    JLabel numberOfAnimalsInfo;
    JLabel numberOfGrassesInfo;
    JLabel dominantGenotypesInfo;
    JLabel averageEnergyInfo;
    JLabel averageAgeOfDeathsInfo;
    JLabel averageNumberOfChildrenInfo;
    JLabel trackedAnimalStatisticsTitle;
    JLabel numberOfChildren;
    JLabel numberOfOffspring;
    JLabel deathDate;

    public StatisticsPanel(int yOverflow, Timer timer, GamePanel gamePanel, Map map, GameMainFrame gameMainFrame){
        initializeVariables(yOverflow, timer, gamePanel, map, gameMainFrame);
        initializeLayout();
    }

    private void initializeVariables(int yOverflow, Timer timer, GamePanel gamePanel, Map map, GameMainFrame gameMainFrame){
        this.yOverflow = yOverflow;
        this.timer = timer;
        this.isStarted = false;
        this.isStopped = true;
        this.gameMainFrame = gameMainFrame;
        this.gamePanel = gamePanel;
        this.map = map;

        stopActionListener = new StopActionListener();
        startActionListener = new StartActionListener();
        nextDayActionListener = new NextDayActionListener();
        saveStatsActionListener = new SaveStatsActionListener();
        markDominantGenotypeActionListener = new MarkDominantGenotypeActionListener();
        trackAnimalActionListener = new TrackAnimalActionListener();

        stopBtn = new JButton("STOP");
        startBtn = new JButton("START");
        nextDayBtn = new JButton("NEXT DAY");
        saveStats = new JButton("SAVE STATS");
        markDominantGenotype = new JButton("<html><center> MARK DOMINANT GENOTYPE</center></html>");
        markDominantGenotype.setHorizontalAlignment(SwingConstants.CENTER);
        trackAnimal = new JButton("TRACK ANIMAL");

        mapStatisticsTitle = new JLabel("MAP STATS");
        mapStatisticsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        dayInfo = new JLabel("  Day: " + map.getDay());
        numberOfAnimalsInfo = new JLabel("  Number of animals: " + map.getNumberOfAnimals());
        numberOfGrassesInfo = new JLabel("  Number of eggs: " + map.getNumberOfGrasses());
        dominantGenotypesInfo = new JLabel("  Dominant genotype: " + map.getDominantGenotype());
        averageEnergyInfo = new JLabel("  Average animal energy: " + map.getAverageEnergy());
        averageAgeOfDeathsInfo = new JLabel("  Average age of deaths: " + map.getAverageAgeOfDeaths());
        averageNumberOfChildrenInfo = new JLabel("  Average number of children: " + map.getAverageNumberOfChildren());
        trackedAnimalStatisticsTitle = new JLabel("");
        numberOfChildren = new JLabel("");
        numberOfOffspring = new JLabel("");
        deathDate = new JLabel("");

        Font titleFont = new Font("Courier", Font.BOLD,22);
        Font font = new Font("Courier", Font.BOLD,16);
        mapStatisticsTitle.setFont(titleFont);
        dayInfo.setFont(font);
        numberOfAnimalsInfo.setFont(font);
        numberOfGrassesInfo.setFont(font);
        dominantGenotypesInfo.setFont(font);
        averageEnergyInfo.setFont(font);
        averageAgeOfDeathsInfo.setFont(font);
        averageNumberOfChildrenInfo.setFont(font);
        trackedAnimalStatisticsTitle.setFont(titleFont);
        numberOfChildren.setFont(font);
        numberOfOffspring.setFont(font);
        deathDate.setFont(font);
    }

    private void initializeLayout() {
        setPreferredSize(new Dimension(50 + Constants.BOARD_WIDTH / 2, Constants.BOARD_HEIGHT - yOverflow));
        setLayout(new GridLayout(0,1));

        JPanel mapStats = new JPanel();
        JPanel trackedAnimal = new JPanel();
        JPanel buttons = new JPanel();

        mapStats.setLayout(new GridLayout(0,1));
        trackedAnimal.setLayout(new GridLayout(0,1));
        GridLayout layout = new GridLayout(0,2);
        layout.setHgap(5);
        layout.setVgap(5);
        buttons.setLayout(layout);

        stopBtn.setEnabled(!isStopped);
        saveStats.setEnabled(!isStopped);
        markDominantGenotype.setEnabled(!isStopped);

        mapStats.add(mapStatisticsTitle);
        mapStats.add(dayInfo);
        mapStats.add(numberOfAnimalsInfo);
        mapStats.add(numberOfGrassesInfo);
        mapStats.add(dominantGenotypesInfo);
        mapStats.add(averageEnergyInfo);
        mapStats.add(averageAgeOfDeathsInfo);
        mapStats.add(averageNumberOfChildrenInfo);

        trackedAnimal.add(trackedAnimalStatisticsTitle);
        trackedAnimal.add(numberOfChildren);
        trackedAnimal.add(numberOfOffspring);
        trackedAnimal.add(deathDate);

        buttons.add(startBtn);
        buttons.add(stopBtn);
        buttons.add(nextDayBtn);
        buttons.add(markDominantGenotype);
        buttons.add(saveStats);
        buttons.add(trackAnimal);

        this.add(mapStats);
        this.add(trackedAnimal);
        this.add(buttons);

        stopBtn.addActionListener(stopActionListener);
        startBtn.addActionListener(startActionListener);
        nextDayBtn.addActionListener(nextDayActionListener);
        saveStats.addActionListener(saveStatsActionListener);
        markDominantGenotype.addActionListener(markDominantGenotypeActionListener);
        trackAnimal.addActionListener(trackAnimalActionListener);
    }

    private class StopActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae) {
            if(isStarted){
                timer.stop();
                timerStop();
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
                trackAnimal.setEnabled(isStopped);
                markDominantGenotype.setEnabled(!isStopped);
            }

        }
    }

    private class NextDayActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae) {
            if(isStopped){
                gamePanel.doOneLoop();
                updateMapStats();
                saveStats.setEnabled(isStopped);
                markDominantGenotype.setEnabled(isStopped);
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
                String userHomeFolder = System.getProperty("user.home");
                userHomeFolder += "/Desktop/";
                FileWriter writer = new FileWriter(userHomeFolder + "EvolutionStats.txt");
                writer.write(jsonString);
                writer.close();
                showMessageDialog(null, "Stats saved on Desktop!");
            } catch (IOException e2) {
                System.out.println("exception " + e2.getMessage());
                e2.printStackTrace();
            }
        }
    }

    private class MarkDominantGenotypeActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae) {
            gamePanel.toggleDominantGenotype();
            gamePanel.repaint();
            if(gamePanel.isDominantGenotypeMarked()){
                markDominantGenotype.setText("<html><center>UNMARK DOMINANT GENOTYPE</center></html>");
            }
            else{
                markDominantGenotype.setText("<html><center>MARK DOMINANT GENOTYPE</center></html>");
            }
        }
    }

    private class TrackAnimalActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae) {
            if(isStopped && !gamePanel.isAnimalTracked()){
                showMessageDialog(null, "Click animal to track!");
                gamePanel.toggleIsFindAnimalToTrackModeOn();
            }
            if(!gamePanel.isAnimalTracked()){
                trackAnimal.setText("UNTRACK ANIMAL");
            }else{
                map.removeOffspringOfTrackedAnimal();
                resetTrackedAnimalStats();
                gamePanel.setFindAnimalToTrackModeOn(false);
                gamePanel.toggleIsAnimalTracked();
                gamePanel.setTrackedAnimal(null);
                gamePanel.repaint();
                trackAnimal.setText("TRACK ANIMAL");
            }
        }
    }

    public void timerStop(){
        isStarted = false;
        isStopped = true;
        stopBtn.setEnabled(isStarted);
        startBtn.setEnabled(isStopped);
        nextDayBtn.setEnabled(isStopped);
        saveStats.setEnabled(isStopped);
        trackAnimal.setEnabled(isStopped);
        markDominantGenotype.setEnabled(isStopped);
    }

    public static class StatsToSave{
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

    public void updateMapStats(){
        dayInfo.setText("  Day: " + map.getDay());
        numberOfAnimalsInfo.setText("  Number of animals: " + map.getNumberOfAnimals());
        numberOfGrassesInfo.setText("  Number of eggs: " + map.getNumberOfGrasses());
        dominantGenotypesInfo.setText("  Dominant genotype: " + map.getDominantGenotype());
        averageEnergyInfo.setText("  Average animal energy: " + map.getAverageEnergy());
        averageAgeOfDeathsInfo.setText("  Average age of deaths: " + map.getAverageAgeOfDeaths());
        averageNumberOfChildrenInfo.setText("  Average number of children: " + map.getAverageNumberOfChildren());
    }

    public void disableButtons(){
        startBtn.setEnabled(false);
        stopBtn.setEnabled(false);
        nextDayBtn.setEnabled(false);
        saveStats.setEnabled(true);
        markDominantGenotype.setEnabled(false);
        trackAnimal.setEnabled(false);
    }

    public void updateTrackedAnimalStats(){
        trackedAnimalStatisticsTitle.setText("TRACKED ANIMAL STATS");
        trackedAnimalStatisticsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        int numberNewChildren = gamePanel.getTrackedAnimal().getNumberOfChildren() - gamePanel.getNumberOfChildrenWhenTrackingStarted();
        numberOfChildren.setText("  Number of new children: " + numberNewChildren);
        numberOfOffspring.setText("  Number of new offspring: " + map.getNumberOfOffspringOfTrackedAnimal());
        if(!gamePanel.getTrackedAnimal().isDead()){
            deathDate.setText("");
        }
        else{
            deathDate.setText("  Animal died on " + gamePanel.getDayOfTrackedAnimalDeath() + " day");
        }
    }

    public void resetTrackedAnimalStats(){
        trackedAnimalStatisticsTitle.setText("");
        numberOfChildren.setText("");
        numberOfOffspring.setText("");
        deathDate.setText("");
    }


}
