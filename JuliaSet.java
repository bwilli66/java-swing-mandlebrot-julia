package Mandel_Julia;

import ComplexNumber.ComplexNumber;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by BradWilliams on 10/27/16.
 */
public class JuliaSet extends JComponent {

    public double rMin, rMax, iMin, iMax, cReal, cImag;
    public int maxIters;

    static int count = 0;


    public JuliaSet(double rMin, double rMax, double iMin, double iMax, int maxIters){

        this.rMin = rMin;
        this.rMax = rMax;

        this.iMin = iMin;
        this.iMax = iMax;

        this.maxIters = maxIters;
    }

    public void paint(Graphics g) {

        int width = getSize().width;
        int height = getSize().height;
        //System.out.println("Right before mandleBrot created:" + this.rMin + " " + this.rMax + " " + this.iMin + " " + this.iMax);


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
//                int rgbTemp = this.mandelBrot(x, y, height, width, this.maxIters, this.rMin, this.rMax, this.iMin, this.iMax, this.cReal, this.cImag);
//
//                data[i] = (rgbTemp << 16) |
//                            (rgbTemp << 8) |
//                            rgbTemp;

                // With color
                int [] rgbTemp = this.convertItersToColor(this.maxIters,
                        this.mandelBrot(x, y, height, width, this.maxIters, this.rMin + 0.5f, this.rMax + 0.5f, this.iMin, this.iMax, this.cReal, this.cImag));

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

    public int mandelBrot(int pixelX, int pixelY, int height, int width, int maxIters, double rMin, double rMax, double iMin, double iMax, double cReal, double cImag){


        //System.out.println(cImag + ", " + cReal);
        //Julia Set
        ComplexNumber z = new ComplexNumber(MainMandelBrot.remapX(pixelX,width,rMin, rMax), MainMandelBrot.remapY(pixelY,height, iMin, iMax));
        ComplexNumber c = new ComplexNumber(cReal,cImag);

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

    public int[] convertItersToColor(int maxIters, int value){

        int[] RGB = new int[3];

        float ratio = (float)value/(float)maxIters;

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

}
