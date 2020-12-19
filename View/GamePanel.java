package View;
import ImageFactory.ImageFactory;
import Objects.Animal;
import Objects.Grass;
import Objects.Map;
import Objects.Vector2d;
import Constants.Constants;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static javax.swing.JOptionPane.showMessageDialog;

public class GamePanel extends JPanel implements MouseListener {
    private Map map;
    private Timer timer;
    private GameMainFrame gameMainFrame;
    private int sizeOfTile;
    private int xOverflow;
    private int yOverflow;
    private boolean markDominant = false;
    private Animal trackedAnimal = null;
    private boolean isFindAnimalToTrackModeOn = false;
    private boolean isAnimalTracked = false;
    private boolean isDeadAnnounced = false;
    private int numberOfChildrenWhenTrackingStarted = 0;
    private int dayOfTrackedAnimalDeath = 0;
    private ImageFactory images;

    public GamePanel(int sizeOfTile, int xOverflow, int yOverflow, Map map, GameMainFrame gameMainFrame, Timer timer) throws IOException {
        initializeVariables(sizeOfTile, xOverflow, yOverflow, map, gameMainFrame, timer);
        initializeLayout();
    }

     private void initializeVariables(int sizeOfTile, int xOverflow, int yOverflow, Map map, GameMainFrame gameMainFrame, Timer timer) throws IOException {
        images = new ImageFactory();
        this.sizeOfTile = sizeOfTile;
        this.xOverflow = xOverflow;
        this.yOverflow = yOverflow;
        this.map = map;
        this.gameMainFrame = gameMainFrame;
        this.timer = timer;
        addMouseListener(this);
    }

    private void initializeLayout() {
        setPreferredSize(new Dimension(Constants.BOARD_WIDTH - xOverflow, Constants.BOARD_HEIGHT - yOverflow));
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        BufferedImage[][] tileGrid = new BufferedImage[Constants.NUMBER_OF_TILES_Y][Constants.NUMBER_OF_TILES_X];
            for(int y = 0; y < Constants.NUMBER_OF_TILES_Y; y++){
                for(int x = 0; x < Constants.NUMBER_OF_TILES_X; x++){
                    if(!map.isOnJungle(x, y)){
                        if(!map.isOccupied(new Vector2d(x, y))){
                            tileGrid[y][x] = images.getGRASS_TILE();
                        }
                        else if(map.objectAt(new Vector2d(x, y)) instanceof Grass){
                            tileGrid[y][x] = images.getEGG_GRASS();
                        }else{
                            Animal animal = (Animal) map.objectAt(new Vector2d(x, y));
                            if(animal.getEnergy() < Constants.FIRST_RANK){
                                tileGrid[y][x] = images.getWHITE_SNAKE_ON_GRASS();
                            }
                            else if(animal.getEnergy() < Constants.SECOND_RANK){
                                tileGrid[y][x] = images.getBLUE_SNAKE_ON_GRASS();
                            }
                            else if(animal.getEnergy() < Constants.THIRD_RANK){
                                tileGrid[y][x] = images.getRED_SNAKE_ON_GRASS();
                            }
                            else{
                                tileGrid[y][x] = images.getGOLD_SNAKE_ON_GRASS();
                            }
                        }

                    }
                    else{
                        if(!map.isOccupied(new Vector2d(x, y))){
                            tileGrid[y][x] = images.getJUNGLE_TILE();
                        }
                        else if(map.objectAt(new Vector2d(x, y)) instanceof Grass){
                            tileGrid[y][x] = images.getEGG_JUNGLE();
                        }
                        else{
                            Animal animal = (Animal) map.objectAt(new Vector2d(x, y));
                            if(animal.getEnergy() < Constants.FIRST_RANK){
                                tileGrid[y][x] = images.getWHITE_SNAKE_ON_JUNGLE();
                            }
                            else if(animal.getEnergy() < Constants.SECOND_RANK){
                                tileGrid[y][x] = images.getBLUE_SNAKE_ON_JUNGLE();
                            }
                            else if(animal.getEnergy() < Constants.THIRD_RANK){
                                tileGrid[y][x] = images.getRED_SNAKE_ON_JUNGLE();
                            }
                            else{
                                tileGrid[y][x] = images.getGOLD_SNAKE_ON_JUNGLE();
                            }
                        }
                    }
                }
            }
        for(int y = 0; y < Constants.NUMBER_OF_TILES_Y; y++){
            for(int x = 0; x < Constants.NUMBER_OF_TILES_X; x++) {
                g.drawImage(tileGrid[y][x], x * sizeOfTile, y * sizeOfTile, sizeOfTile, sizeOfTile, null);
            }
        }
        if(markDominant){
            markDominantGenotype(g);
        }
        if(this.trackedAnimal != null && isAnimalTracked && !this.trackedAnimal.isDead()){
            markTrackedAnimal(g);
        }
    }

    public void markDominantGenotype(Graphics g){
        ArrayList<ArrayList<Animal>> animalsCopy = new ArrayList<>(this.map.getAnimalHashMap().values());

        for (ArrayList<Animal> animaList : animalsCopy) {
            ArrayList<Animal> animalListCopy = new ArrayList<>(animaList);
            for (Animal animal : animalListCopy) {
                if(animal.getDominantGenotypes().contains(map.getDominantGenotype())){
                    Graphics2D gr = (Graphics2D)g;
                    gr.setColor(Color.RED);
                    int x = animal.getPosition().x;
                    int y = animal.getPosition().y;
                    Shape redRing = createRingShape(x*sizeOfTile + sizeOfTile/2, y * sizeOfTile + sizeOfTile/2, sizeOfTile/2, sizeOfTile/12);
                    gr.fill(redRing);
                }
            }
        }
    }

    public void markTrackedAnimal(Graphics g){
        if(trackedAnimal != null){
            Graphics2D gr = (Graphics2D)g;
            int x = trackedAnimal.getPosition().x;
            int y = trackedAnimal.getPosition().y;
            Shape goldRing = createRingShape(x*sizeOfTile + sizeOfTile/2, y * sizeOfTile + sizeOfTile/2, sizeOfTile/2 - sizeOfTile/12, sizeOfTile/12);
            gr.setColor(Color.YELLOW);
            gr.fill(goldRing);
        }
    }

    public void doOneLoop() {
        update();
        repaint(); // paintComponent method is going to be called
        if(this.trackedAnimal != null && isAnimalTracked && this.trackedAnimal.isDead() && !this.isDeadAnnounced){
            this.dayOfTrackedAnimalDeath = map.getDay();
            this.isDeadAnnounced = true;
            this.timer.stop();
            this.gameMainFrame.statisticsPanel.timerStop();
            showMessageDialog(null, "Tracked animal died on " + map.getDay() + " day [*]");
        }
        if(this.isAnimalTracked()){
            gameMainFrame.statisticsPanel.updateTrackedAnimalStats();
        }
    }

    private void update() {
        map.nextDay();
    }

    // Toggle Functions
    public void toggleDominantGenotype(){
        this.markDominant = !this.markDominant;
    }

    public void toggleIsFindAnimalToTrackModeOn(){
        this.isFindAnimalToTrackModeOn = !this.isFindAnimalToTrackModeOn;
    }

    public void toggleIsAnimalTracked(){
        this.isAnimalTracked = !this.isAnimalTracked;
    }

    // Getters
    public boolean isFindAnimalToTrackModeOn() {
        return isFindAnimalToTrackModeOn;
    }

    public boolean isAnimalTracked() {
        return isAnimalTracked;
    }

    public boolean isDominantGenotypeMarked() {
        return markDominant;
    }

    public Animal getTrackedAnimal() {
        return trackedAnimal;
    }

    public int getNumberOfChildrenWhenTrackingStarted() {
        return numberOfChildrenWhenTrackingStarted;
    }

    public int getDayOfTrackedAnimalDeath() {
        return dayOfTrackedAnimalDeath;
    }

    // Setters
    public void setTrackedAnimal(Animal trackedAnimal) {
        this.trackedAnimal = trackedAnimal;
    }

    public void setFindAnimalToTrackModeOn(boolean trackAnimalModeOn) {
        isFindAnimalToTrackModeOn = trackAnimalModeOn;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!this.timer.isRunning() && !isFindAnimalToTrackModeOn){
            int x = e.getX() / sizeOfTile;
            int y = e.getY() / sizeOfTile;
            Object animal = map.objectAt(new Vector2d(x, y));
            if(animal instanceof Animal){
                StringBuilder genotype = new StringBuilder();
                int[] genotypeArray = ((Animal) animal).getGenes();
                for(int gen: genotypeArray){
                    genotype.append(" ").append(gen);
                }
                showMessageDialog(null, "Animal genotype: " + genotype);
            }
        }
        else if(!this.timer.isRunning() && isFindAnimalToTrackModeOn()){
            int x = e.getX() / sizeOfTile;
            int y = e.getY() / sizeOfTile;
            Object animal = map.objectAt(new Vector2d(x, y));
            if(animal instanceof Animal){
                this.isDeadAnnounced = false;
                this.numberOfChildrenWhenTrackingStarted = ((Animal) animal).getNumberOfChildren();
                ((Animal) animal).setIsOffspringOfTrackedAnimal(true);
                setTrackedAnimal((Animal)animal);
                toggleIsAnimalTracked();
                this.gameMainFrame.statisticsPanel.updateTrackedAnimalStats();
                repaint();
                toggleIsFindAnimalToTrackModeOn(); //setting false
            }
        }
    }

    // Function Used To Mark Genotype And Tracked Animal
    private static Shape createRingShape(
            double centerX, double centerY, double outerRadius, double thickness)
    {
        Ellipse2D outer = new Ellipse2D.Double(
                centerX - outerRadius,
                centerY - outerRadius,
                outerRadius + outerRadius,
                outerRadius + outerRadius);
        Ellipse2D inner = new Ellipse2D.Double(
                centerX - outerRadius + thickness,
                centerY - outerRadius + thickness,
                outerRadius + outerRadius - thickness - thickness,
                outerRadius + outerRadius - thickness - thickness);
        Area area = new Area(outer);
        area.subtract(new Area(inner));
        return area;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
