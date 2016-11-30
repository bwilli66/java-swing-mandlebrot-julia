package Mandel_Julia;

/**
 * Created by BradWilliams on 10/26/16.
 */

import ComplexNumber.ComplexNumber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MainMandelBrot extends JComponent {

    public double rMin, rMax, iMin, iMax;
    public int maxIters;
    public JuliaSet juliaSetFrame;

    static int count = 0;

    public MainMandelBrot(double rMin, double rMax, double iMin, double iMax, int maxIters, JuliaSet jsf){

        this.rMin = rMin;
        this.rMax = rMax;

        this.iMin = iMin;
        this.iMax = iMax;

        this.maxIters = maxIters;

        this.juliaSetFrame = jsf;

        this.setFocusable(true);

        this.addMouseListener(new Zoomlistener(this));

        this.addKeyListener(new ResetKeyListener(this));

        this.addMouseMotionListener(new MouseMovementListener(this));

    }
    public void paint(Graphics g) {

        int width = getSize().width;
        int height = getSize().height;



        int[] data = new int[width * height];
        int i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                /*uses bitwise shifts and ORs
                *
                * red = 00000000 00000000 11111111
                *                 <--------
                * red << 16: 11111111 00000000 00000000
                *
                * green = 00000000 00000000 11111111
                *                           <---
                * green << 8: 00000000 11111111 00000000
                *
                * (red << 16) | (green << 8): 11111111 11111111 00000000
                *
                * blue = 00000000 00000000 11111111
                *
                * (red << 16) | (green << 8) | blue: 11111111 11111111 11111111
                 */

                // Gray scale
//                int rgbTemp = this.mandelBrot(x, y, height, width, this.maxIters, this.rMin, this.rMax, this.iMin, this.iMax);
//
//                data[i] = (rgbTemp << 16) |
//                            (rgbTemp << 8) |
//                            rgbTemp;

                // With color
                int [] rgbTemp = convertItersToColor(this.maxIters,
                        this.mandelBrot(x, y, height, width, this.maxIters, this.rMin, this.rMax, this.iMin, this.iMax));

                data[i] = (rgbTemp[0] << 16) |
                        (rgbTemp[1] << 8) |
                        rgbTemp[2];
                i++;
            }
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, width, height, data, 0, width);

        //only do this once
//        if(this.count >= 0) {
//            try {
//
//                ImageIO.write(image, "png", new File("mandelBrotJava" + this.count + ".png"));
//                System.out.println("PNG created");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        g.drawImage(image, 0, 0, this);

        this.count++;
    }



    public int[] convertItersToColor(int maxIters, int value){

        int[] RGB = new int[3];

        float ratio = (float)value/(float)maxIters * 8;

        if(value == maxIters){
            RGB[0] = 0; // red
            RGB[1] = 0; // green
            RGB[2] = 0; // blue
            return RGB;
        }

        Color color = Color.getHSBColor(ratio, 1.0f, 1.0f);

        RGB[0] = color.getRed(); // red
        RGB[1] = color.getGreen(); // green
        RGB[2] = color.getBlue(); // blue

        return RGB;
    }

    public class Zoomlistener implements MouseListener {

        MainMandelBrot parent;

        public Zoomlistener(MainMandelBrot parent){
            this.parent = parent;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            System.out.println("Cursor " + x + "," + y);//these co-ords are relative to the component

            // find half are of current square

            double newWidth = (parent.rMax - parent.rMin) * 0.5;
            double newHeight = (parent.iMax - parent.iMin) * 0.5;

            //System.out.println("new complex plane range" + newWidth + "," + newHeight);

            // current point on complex plane:
            double real = parent.remapX(x,parent.getWidth(), parent.rMin, parent.rMax);
            double imag = parent.remapY(y,parent.getHeight(), parent.iMin, parent.iMax);

            //System.out.println("current point on complex plane: " + real + ", " + imag + "i");

            parent.rMin = real - newWidth/2.0;
            parent.rMax = real + newWidth/2.0;
            parent.iMin = imag - newHeight/2.0;
            parent.iMax = imag + newHeight/2.0;

            //System.out.println("After math: realMin:" + parent.rMin + " realMax:" + parent.rMax + " imagMin:" + parent.iMin + " imagMax:" + parent.iMax);
            parent.repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }

    public class ResetKeyListener implements KeyListener {

        MainMandelBrot parent;

        public ResetKeyListener(MainMandelBrot parent){
            this.parent = parent;
        }

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {

            if (e.getKeyChar() == 'b'){

                //System.out.println("b pressed");
                parent.rMin = -2.0;
                parent.rMax = 1;
                parent.iMin = -1.0;
                parent.iMax = 1;

                parent.repaint();
            }
            else
                return;

        }
    }

    public class MouseMovementListener implements MouseListener, MouseMotionListener {

        MainMandelBrot parent;
        public MouseMovementListener(MainMandelBrot parent){
            this.parent = parent;
        }

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();


            juliaSetFrame.cReal = remapX(x,parent.getWidth(),parent.rMin,parent.rMax);
            juliaSetFrame.cImag = remapY(y,parent.getHeight(),parent.iMin,parent.iMax);;

            juliaSetFrame.repaint();
            //System.out.println(x + ", " + y);
        }

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }

    public static double remapX(int pixel, int width, double rMin, double rMax){
        //System.out.println(pixel);


        //  z = z+c


        double real_min = rMin;
        double real_max = rMax;
        double oldRange;
        double newRange;
        double newValue;

        oldRange = (double) width;
        newRange = real_max - real_min;

        newValue = real_min + (newRange/oldRange) * pixel;


        //System.out.println(newValue + ",");

        return newValue; //real part of complex number at a given pixel
    }

    public static double remapY(int pixel, int height, double iMin, double iMax){
        //System.out.println(pixel);

        double imag_min = iMin;
        double imag_max = iMax;
        double oldRange;
        double newRange;
        double newValue;

        oldRange = (double) height;
        newRange = imag_max - imag_min;

        newValue = imag_max - (newRange/oldRange) * pixel;

        //System.out.println(newValue);

        return newValue; //real part of complex number at a given pixel
    }

    public int mandelBrot(int pixelX, int pixelY, int height, int width, int maxIters, double rMin, double rMax, double iMin, double iMax){

        ComplexNumber c = new ComplexNumber(remapX(pixelX,width, rMin, rMax),remapY(pixelY,height, iMin, iMax));
        ComplexNumber z = new ComplexNumber(0.0, 0.0);

        //Julia Set
//        ComplexNumber z = new ComplexNumber(remapX(pixelX,width,rMin, rMax),remapY(pixelY,height, iMin, iMax));
//        ComplexNumber c = new ComplexNumber(-0.70176,-0.3842);

        double zRealPartTemp;
        int i = 0;

//        if (this.count < 20)
//            System.out.println(c.toString());


        while(z.mag() < 2.0 && i < maxIters){

            z = (z.mult(z)).add(c);

            i++;
        }

        return i;
    }

}

