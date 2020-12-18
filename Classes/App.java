package Classes;

import Constants.Constants;
import View.GameMainFrame;

import java.awt.*;
import java.io.IOException;

public class App {
     public static void main(String[] args) throws IOException {
         Constants.getValueFromJSON();
         for(int i = 0; i < Constants.NUMBER_OF_MAPS; i++) {
                 try {
                     GameMainFrame gameMainFrame = new GameMainFrame();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
         }
    }
}
