package Constants;

public class Constants {
    private Constants(){

    }

    public static final String TITLE = "Evolution Simulator";
    public static final int BOARD_WIDTH = 700;
    public static final int BOARD_HEIGHT = 700;

    public static final int NUMBER_OF_TILES_X = 10;
    public static final int NUMBER_OF_TILES_Y = 10;



    public static final int START_ENERGY = 50;
    public static final int NUMBER_OF_ANIMALS = 6;
    public static final int MOVE_ENERGY = 1;
    public static final int PLANT_ENERGY = 10;
    public static final double JUNGLE_RATIO = 0.4;

    public static final int FIRST_RANK = 25;
    public static final int SECOND_RANK = 50;
    public static final int THIRD_RANK = 75;
    public static final int FOURTH_RANK = 100;

    // Speed Of The Application
    public static final int GAME_SPEED = 500;

    static String snakesOnGrassPath = "/Users/kamildelekta/Documents/Programming/Obiektowka/EvolutionGenerator/src/Graphics/snakesOnGrass/";
    static String snakesOnJunglePath = "/Users/kamildelekta/Documents/Programming/Obiektowka/EvolutionGenerator/src/Graphics/snakesOnJungle/";
    static String grassesPath = "/Users/kamildelekta/Documents/Programming/Obiektowka/EvolutionGenerator/src/Graphics/grasses/";
    static String eggsPath = "/Users/kamildelekta/Documents/Programming/Obiektowka/EvolutionGenerator/src/Graphics/eggs/";

    // URLS
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
}
