package Classes;

import Constants.Constants;

import java.util.Timer;
import java.util.TimerTask;

public class Simulation {
    public static void main(String[] args) {

        Map map = new Map(Constants.NUMBER_OF_TILES_Y, Constants.NUMBER_OF_TILES_X, Constants.START_ENERGY,
                        Constants.MOVE_ENERGY, Constants.PLANT_ENERGY, Constants.JUNGLE_RATIO);

        map.placeNAnimalsOnMap(Constants.NUMBER_OF_ANIMALS);
        System.out.println(map.toString());
        simulate(map);
    }

    public static void simulate(Map map){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                map.nextDay();
                System.out.println(map);
            }
        }, 0, 1000);//wait 0 ms before doing the action and do it evry 1000ms (1second)

//        timer.cancel();//stop the timer
    }
}
