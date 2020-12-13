package Classes;

import View.GameMainFrame;

import java.awt.*;

public class App {
     public static void main(String[] args){

        EventQueue.invokeLater( () -> {
            new GameMainFrame();
        });
    }
}
