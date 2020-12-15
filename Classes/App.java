package Classes;

import Constants.Constants;
import View.GameMainFrame;

import java.awt.*;
import java.io.IOException;

public class App {
     public static void main(String[] args){
        EventQueue.invokeLater( () -> {
            try {
                Constants.getValueFromJSON();
                for(int i = 0; i < Constants.NUMBER_OF_MAPS; i++){
                    new GameMainFrame();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
