package Classes;

import Constants.Constants;
import View.GameMainFrame;

import java.awt.*;
import java.io.IOException;

public class App {
     public static void main(String[] args){
        EventQueue.invokeLater( () -> {
            try {
                new GameMainFrame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
