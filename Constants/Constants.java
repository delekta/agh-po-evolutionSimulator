package Constants;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static Constants.Constants.initializeVariables;

public class Constants {
    private Constants(){

    }

    public static final String TITLE = "Evolution Simulator";
    public static final int BOARD_WIDTH = 700;
    public static final int BOARD_HEIGHT = 700;

    // Values Read From JSON
    public static int NUMBER_OF_TILES_X;
    public static int NUMBER_OF_TILES_Y;
    public static int NUMBER_OF_MAPS;
    // Speed Of The Application
    public static int GAME_SPEED;
    public static int START_ENERGY;
    public static int NUMBER_OF_ANIMALS;
    public static int MOVE_ENERGY;
    public static int PLANT_ENERGY;
    public static double JUNGLE_RATIO;

    public static int FIRST_RANK;
    public static int SECOND_RANK;
    public static int THIRD_RANK;
    public static int FOURTH_RANK;

    // URLS
    static final String snakesOnGrassPath = "src/Graphics/snakesOnGrass/";
    static final String snakesOnJunglePath = "src/Graphics/snakesOnJungle/";
    static final String grassesPath = "src/Graphics/grasses/";
    static final String eggsPath = "src/Graphics/eggs/";

    public static final String GRASS_TILE_URL = grassesPath + "grassTile.png";
    public static final String JUNGLE_TILE_URL = grassesPath + "jungleTile.png";
    public static final String WHITE_SNAKE_GRASS_URL = snakesOnGrassPath + "whiteSnakeOnGrass.png";
    public static final String BLUE_SNAKE_GRASS_URL = snakesOnGrassPath + "blueSnakeOnGrass.png";
    public static final String RED_SNAKE_GRASS_URL = snakesOnGrassPath + "redSnakeOnGrass.png";
    public static final String GOLD_SNAKE_GRASS_URL = snakesOnGrassPath + "goldSnakeOnGrass.png";
    public static final String WHITE_SNAKE_JUNGLE_URL = snakesOnJunglePath + "whiteSnakeOnJungle.png";
    public static final String BLUE_SNAKE_JUNGLE_URL = snakesOnJunglePath + "blueSnakeOnJungle.png";
    public static final String RED_SNAKE_JUNGLE_URL = snakesOnJunglePath + "redSnakeOnJungle.png";
    public static final String GOLD_SNAKE_JUNGLE_URL = snakesOnJunglePath + "goldSnakeOnJungle.png";
    public static final String EGG_GRASS_URL = eggsPath + "eggOnGrass.png";
    public static final String EGG_JUNGLE_URL = eggsPath + "eggOnJungle.png";

    public static void getValueFromJSON() throws FileNotFoundException {
        FileReader reader = new FileReader("src/startValues.json");
        JsonParser jsonParser = new JsonParser();
        Gson gson = new Gson();
        StartValue startValue = gson.fromJson(jsonParser.parse(reader), StartValue.class);
        initializeVariables(startValue);
    }

    public static void initializeVariables(StartValue startValue){
        NUMBER_OF_TILES_X = startValue.numberOfTilesX;
        NUMBER_OF_TILES_Y = startValue.numberOfTilesY;
        NUMBER_OF_MAPS = startValue.numberOfMaps;
        GAME_SPEED = startValue.gameSpeed;
        START_ENERGY = startValue.startEnergy;
        NUMBER_OF_ANIMALS = startValue.numberOfAnimals;
        MOVE_ENERGY = startValue.moveEnergy;
        PLANT_ENERGY = startValue.plantEnergy;
        JUNGLE_RATIO = startValue.jungleRatio;
        FIRST_RANK = START_ENERGY / 2;
        SECOND_RANK = START_ENERGY;
        THIRD_RANK = (int) (1.5 * START_ENERGY);
        FOURTH_RANK = 2 * START_ENERGY;
    }

    private class StartValue{
        int numberOfTilesX;
        int numberOfTilesY;
        int numberOfMaps;
        int startEnergy;
        int numberOfAnimals;
        int moveEnergy;
        int plantEnergy;
        double jungleRatio;
        int gameSpeed;
    }
}
