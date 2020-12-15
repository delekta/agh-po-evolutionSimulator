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
    private Map map;
    int sizeOfTile;
    int xOverflow;
    int yOverflow;

    BufferedImage WHITE_SNAKE_ON_JUNGLE = ImageIO.read(new File(Constants.WHITE_SNAKE_JUNGLE_URL));
    BufferedImage BLUE_SNAKE_ON_JUNGLE = ImageIO.read(new File(Constants.BLUE_SNAKE_JUNGLE_URL));
    BufferedImage RED_SNAKE_ON_JUNGLE = ImageIO.read(new File(Constants.RED_SNAKE_JUNGLE_URL));
    BufferedImage GOLD_SNAKE_ON_JUNGLE = ImageIO.read(new File(Constants.GOLD_SNAKE_JUNGLE_URL));

    BufferedImage WHITE_SNAKE_ON_GRASS = ImageIO.read(new File(Constants.WHITE_SNAKE_GRASS_URL));
    BufferedImage BLUE_SNAKE_ON_GRASS = ImageIO.read(new File(Constants.BLUE_SNAKE_GRASS_URL));
    BufferedImage RED_SNAKE_ON_GRASS = ImageIO.read(new File(Constants.RED_SNAKE_GRASS_URL));
    BufferedImage GOLD_SNAKE_ON_GRASS = ImageIO.read(new File(Constants.GOLD_SNAKE_GRASS_URL));

    BufferedImage GRASS_TILE = ImageIO.read(new File(Constants.GRASS_TILE_URL));
    BufferedImage JUNGLE_TILE = ImageIO.read(new File(Constants.JUNGLE_TILE_URL));

    BufferedImage EGG_GRASS = ImageIO.read(new File(Constants.EGG_GRASS_URL));
    BufferedImage EGG_JUNGLE = ImageIO.read(new File(Constants.EGG_JUNGLE_URL));

    public GamePanel(int sizeOfTile, int xOverflow, int yOverflow) throws IOException {
        this.sizeOfTile = sizeOfTile;
        this.xOverflow = xOverflow;
        this.yOverflow = yOverflow;
        initializeVariables();
        initializeLayout();
    }

     private void initializeVariables() {
        this.map = new Map(Constants.NUMBER_OF_TILES_Y, Constants.NUMBER_OF_TILES_X, Constants.START_ENERGY,
                Constants.MOVE_ENERGY, Constants.PLANT_ENERGY, Constants.JUNGLE_RATIO);
         this.map.placeNAnimalsOnMap(Constants.NUMBER_OF_ANIMALS);
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
                            tileGrid[y][x] = GRASS_TILE;
                        }
                        else if(map.objectAt(new Vector2d(x, y)) instanceof Grass){
                            tileGrid[y][x] = EGG_GRASS;
                        }else{
                            Animal animal = (Animal) map.objectAt(new Vector2d(x, y));
                            if(animal.getEnergy() < Constants.FIRST_RANK){
                                tileGrid[y][x] = WHITE_SNAKE_ON_GRASS;
                            }
                            else if(animal.getEnergy() < Constants.SECOND_RANK){
                                tileGrid[y][x] = BLUE_SNAKE_ON_GRASS;
                            }
                            else if(animal.getEnergy() < Constants.THIRD_RANK){
                                tileGrid[y][x] = RED_SNAKE_ON_GRASS;
                            }
                            else{
                                tileGrid[y][x] = GOLD_SNAKE_ON_GRASS;
                            }
                        }

                    }
                    else{
                        if(!map.isOccupied(new Vector2d(x, y))){
                            tileGrid[y][x] = JUNGLE_TILE;
                        }
                        else if(map.objectAt(new Vector2d(x, y)) instanceof Grass){
                            tileGrid[y][x] = EGG_JUNGLE;
                        }
                        else{
                            Animal animal = (Animal) map.objectAt(new Vector2d(x, y));
                            if(animal.getEnergy() < Constants.FIRST_RANK){
                                tileGrid[y][x] = WHITE_SNAKE_ON_JUNGLE;
                            }
                            else if(animal.getEnergy() < Constants.SECOND_RANK){
                                tileGrid[y][x] = BLUE_SNAKE_ON_JUNGLE;
                            }
                            else if(animal.getEnergy() < Constants.THIRD_RANK){
                                tileGrid[y][x] = RED_SNAKE_ON_JUNGLE;
                            }
                            else{
                                tileGrid[y][x] = GOLD_SNAKE_ON_JUNGLE;
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
    }

    public void doOneLoop() {
        update();
        repaint(); // paintComponent method is going to be called
    }

    private void update() {
        map.nextDay();
    }


}
