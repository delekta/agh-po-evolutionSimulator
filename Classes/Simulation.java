package Classes;

import java.util.Timer;
import java.util.TimerTask;

public class Simulation {
    public static void main(String[] args) {

        Map map = new Map(Constants.BOARD_HEIGHT, Constants.BOARD_WIDTH, Constants.START_ENERGY,
                        Constants.MOVE_ENERGY, Constants.PLANT_ENERGY, Constants.JUNGLE_RATIO);

        map.place(new Animal(map, new Vector2d(15, 5)));
        map.place(new Animal(map, new Vector2d(15, 6)));
        map.place(new Animal(map, new Vector2d(15, 7)));
        map.place(new Animal(map, new Vector2d(14, 5)));
//        map.place(new Animal(map, new Vector2d(14, 6)));
//        map.place(new Animal(map, new Vector2d(14, 7)));
//        map.place(new Animal(map, new Vector2d(16, 5)));
//        map.place(new Animal(map, new Vector2d(16, 6)));
//        map.place(new Animal(map, new Vector2d(16, 7)));
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
