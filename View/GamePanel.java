package View;

import Classes.Animal;
import Classes.Grass;
import Classes.Map;
import Classes.Vector2d;
import Constants.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel {
    private Timer timer;
    private Map map;
    int sizeOfTile;
    int xOverflow;
    int yOverflow;

    BufferedImage image1 = ImageIO.read(new File(Constants.GOLD_SNAKE_GRASS_URL));
    BufferedImage image2 = ImageIO.read(new File(Constants.WHITE_SNAKE_JUNGLE_URL));
    BufferedImage image3 = ImageIO.read(new File(Constants.GRASS_TILE_URL));
    BufferedImage image4 = ImageIO.read(new File(Constants.EGG_JUNGLE_URL));

    public GamePanel() throws IOException {
        initializeLayout();
        initializeVariables();


    }

     private void initializeVariables() {
        // after every Constants.GAME_SPEED call GameLoop ActionPerformed
         sizeOfTile = (int) (Math.min(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT) / Math.max(Constants.NUMBER_OF_TILES_X, Constants.NUMBER_OF_TILES_Y));
         xOverflow = Constants.BOARD_WIDTH - sizeOfTile * Constants.NUMBER_OF_TILES_X;
         yOverflow = Constants.BOARD_HEIGHT - sizeOfTile * Constants.NUMBER_OF_TILES_Y;

        this.map = new Map(Constants.NUMBER_OF_TILES_Y, Constants.NUMBER_OF_TILES_X, Constants.START_ENERGY,
                Constants.MOVE_ENERGY, Constants.PLANT_ENERGY, Constants.JUNGLE_RATIO);
         this.map.placeNAnimalsOnMap(Constants.NUMBER_OF_ANIMALS);
        this.timer = new Timer(Constants.GAME_SPEED, new GameLoop(this));
    }

    private void initializeLayout() {
        setPreferredSize(new Dimension(Constants.BOARD_WIDTH - xOverflow, Constants.BOARD_HEIGHT - yOverflow));
        this.timer = new Timer(Constants.GAME_SPEED, new GameLoop(this));
        this.timer.start();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        BufferedImage[][] tileGrid = new BufferedImage[Constants.NUMBER_OF_TILES_Y][Constants.NUMBER_OF_TILES_X];
        try {
            for(int y = 0; y < Constants.NUMBER_OF_TILES_Y; y++){
                for(int x = 0; x < Constants.NUMBER_OF_TILES_X; x++){
                    if(!map.isOnJungle(x, y)){
                        if(!map.isOccupied(new Vector2d(x, y))){
                            tileGrid[y][x] = ImageIO.read(new File(Constants.GRASS_TILE_URL));
                        }
                        else if(map.objectAt(new Vector2d(x, y)) instanceof Grass){
                            tileGrid[y][x] = ImageIO.read(new File(Constants.EGG_GRASS_URL));
                        }else{
                            Animal animal = (Animal) map.objectAt(new Vector2d(x, y));
                            if(animal.getEnergy() < Constants.FIRST_RANK){
                                tileGrid[y][x] = ImageIO.read(new File(Constants.WHITE_SNAKE_GRASS_URL));
                            }
                            else if(animal.getEnergy() < Constants.SECOND_RANK){
                                tileGrid[y][x] = ImageIO.read(new File(Constants.BLUE_SNAKE_GRASS_URL));
                            }
                            else if(animal.getEnergy() < Constants.THIRD_RANK){
                                tileGrid[y][x] = ImageIO.read(new File(Constants.RED_SNAKE_GRASS_URL));
                            }
                            else{
                                tileGrid[y][x] = ImageIO.read(new File(Constants.GOLD_SNAKE_GRASS_URL));
                            }
                        }

                    }
                    else{
                        if(!map.isOccupied(new Vector2d(x, y))){
                            tileGrid[y][x] = ImageIO.read(new File(Constants.JUNGLE_TILE_URL));
                        }
                        else if(map.objectAt(new Vector2d(x, y)) instanceof Grass){
                            tileGrid[y][x] = ImageIO.read(new File(Constants.EGG_JUNGLE_URL));
                        }
                        else{
                            Animal animal = (Animal) map.objectAt(new Vector2d(x, y));
                            if(animal.getEnergy() < Constants.FIRST_RANK){
                                tileGrid[y][x] = ImageIO.read(new File(Constants.WHITE_SNAKE_JUNGLE_URL));
                            }
                            else if(animal.getEnergy() < Constants.SECOND_RANK){
                                tileGrid[y][x] = ImageIO.read(new File(Constants.BLUE_SNAKE_JUNGLE_URL));
                            }
                            else if(animal.getEnergy() < Constants.THIRD_RANK){
                                tileGrid[y][x] = ImageIO.read(new File(Constants.RED_SNAKE_JUNGLE_URL));
                            }
                            else{
                                tileGrid[y][x] = ImageIO.read(new File(Constants.GOLD_SNAKE_JUNGLE_URL));
                            }
                        }
                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        for(int y = 0; y < Constants.NUMBER_OF_TILES_Y; y++){
            for(int x = 0; x < Constants.NUMBER_OF_TILES_X; x++) {
                g.drawImage(tileGrid[y][x], x * sizeOfTile, y * sizeOfTile, sizeOfTile, sizeOfTile, null);
            }
        }
    }

    public void doOneLoop() {
        update();
        repaint(); // paintComponent method is going to be called
    }

    private void update() {
        map.nextDay();
    }


}
