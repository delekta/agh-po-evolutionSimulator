package Simulation;
import Constants.Constants;
import View.GameMainFrame;
import java.io.IOException;

public class Simulation {
     public static void main(String[] args) throws IOException {
         Constants.getValueFromJSON();
         for(int i = 0; i < Constants.NUMBER_OF_MAPS; i++) {
                 try {
                     new GameMainFrame();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
         }
    }
}
