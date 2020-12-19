package ImageFactory;
import Constants.Constants;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class ImageFactory {
    BufferedImage WHITE_SNAKE_ON_JUNGLE;
    BufferedImage BLUE_SNAKE_ON_JUNGLE;
    BufferedImage RED_SNAKE_ON_JUNGLE;
    BufferedImage GOLD_SNAKE_ON_JUNGLE;
    BufferedImage WHITE_SNAKE_ON_GRASS;
    BufferedImage BLUE_SNAKE_ON_GRASS;
    BufferedImage RED_SNAKE_ON_GRASS;
    BufferedImage GOLD_SNAKE_ON_GRASS;
    BufferedImage GRASS_TILE;
    BufferedImage JUNGLE_TILE;
    BufferedImage EGG_GRASS;
    BufferedImage EGG_JUNGLE;

    public ImageFactory() throws IOException {
        WHITE_SNAKE_ON_JUNGLE = ImageIO.read(new File(Constants.WHITE_SNAKE_JUNGLE_URL));
        BLUE_SNAKE_ON_JUNGLE = ImageIO.read(new File(Constants.BLUE_SNAKE_JUNGLE_URL));
        RED_SNAKE_ON_JUNGLE = ImageIO.read(new File(Constants.RED_SNAKE_JUNGLE_URL));
        GOLD_SNAKE_ON_JUNGLE = ImageIO.read(new File(Constants.GOLD_SNAKE_JUNGLE_URL));
        WHITE_SNAKE_ON_GRASS = ImageIO.read(new File(Constants.WHITE_SNAKE_GRASS_URL));
        BLUE_SNAKE_ON_GRASS = ImageIO.read(new File(Constants.BLUE_SNAKE_GRASS_URL));
        RED_SNAKE_ON_GRASS = ImageIO.read(new File(Constants.RED_SNAKE_GRASS_URL));
        GOLD_SNAKE_ON_GRASS = ImageIO.read(new File(Constants.GOLD_SNAKE_GRASS_URL));
        GRASS_TILE = ImageIO.read(new File(Constants.GRASS_TILE_URL));
        JUNGLE_TILE = ImageIO.read(new File(Constants.JUNGLE_TILE_URL));
        EGG_GRASS = ImageIO.read(new File(Constants.EGG_GRASS_URL));
        EGG_JUNGLE = ImageIO.read(new File(Constants.EGG_JUNGLE_URL));
    }

    public BufferedImage getWHITE_SNAKE_ON_JUNGLE() {
        return WHITE_SNAKE_ON_JUNGLE;
    }

    public BufferedImage getBLUE_SNAKE_ON_JUNGLE() {
        return BLUE_SNAKE_ON_JUNGLE;
    }

    public BufferedImage getRED_SNAKE_ON_JUNGLE() {
        return RED_SNAKE_ON_JUNGLE;
    }

    public BufferedImage getGOLD_SNAKE_ON_JUNGLE() {
        return GOLD_SNAKE_ON_JUNGLE;
    }

    public BufferedImage getWHITE_SNAKE_ON_GRASS() {
        return WHITE_SNAKE_ON_GRASS;
    }

    public BufferedImage getBLUE_SNAKE_ON_GRASS() {
        return BLUE_SNAKE_ON_GRASS;
    }

    public BufferedImage getRED_SNAKE_ON_GRASS() {
        return RED_SNAKE_ON_GRASS;
    }

    public BufferedImage getGOLD_SNAKE_ON_GRASS() {
        return GOLD_SNAKE_ON_GRASS;
    }

    public BufferedImage getGRASS_TILE() {
        return GRASS_TILE;
    }

    public BufferedImage getJUNGLE_TILE() {
        return JUNGLE_TILE;
    }

    public BufferedImage getEGG_GRASS() {
        return EGG_GRASS;
    }

    public BufferedImage getEGG_JUNGLE() {
        return EGG_JUNGLE;
    }
}
