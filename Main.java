package Mandel_Julia;

import javax.swing.*;
import java.awt.*;

/**
 * Created by BradWilliams on 11/30/16.
 */
public class Main {

    public static void main(String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();

        JFrame frame = new JFrame("MandelBrot");
        frame.setResizable(false);

        JuliaSet js = new JuliaSet(-2.0,1.0,-1.0,1.0,60);

        JFrame juliaFrame = new JFrame("JuliaSet");

        juliaFrame.getContentPane().add(js);
        juliaFrame.setSize(700, 600);
        juliaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        juliaFrame.setLocation((int)(rect.getWidth()/2),(int)(rect.getHeight()/2-juliaFrame.getHeight()/2));
        juliaFrame.setVisible(true);

        frame.getContentPane().add(new MainMandelBrot(-2.0,1.0,-1.0,1.0,500,js));
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation((int)(rect.getWidth()/2-frame.getWidth()),(int)(rect.getHeight()/2-frame.getHeight()/2));
        frame.setVisible(true);

        JOptionPane.showMessageDialog(frame,
                "Click anywhere in Mandelbrot set to zoom \n\nPress 'b' to reset",
                "Instructions",
                JOptionPane.INFORMATION_MESSAGE);


    }
}
