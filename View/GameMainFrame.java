package View;

import Classes.Map;
import Constants.Constants;

import javax.swing.*;
import java.io.IOException;

public class  GameMainFrame extends JFrame {


    public GameMainFrame() throws IOException {
        initializeLayout();
    }

    private void initializeLayout() throws IOException {
        add(new GamePanel());
        setTitle(Constants.TITLE);


        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

    }
}
