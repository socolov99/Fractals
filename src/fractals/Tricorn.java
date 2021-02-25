package fractals;

import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator {

    public static final int MAX_ITERATIONS = 2000;

    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    @Override
    public int numIterations(double x, double y) {
        int count = 0;

        double real = 0;
        double imagine = 0;
        double z_multiplyZ = 0;

        while (count < MAX_ITERATIONS && z_multiplyZ < 4) {
            count++;
            double newReal = real * real - imagine * imagine + x;
            double newImagine = -2 * real * imagine + y;
            z_multiplyZ = newReal * newReal + newImagine * newImagine;
            real = newReal;
            imagine = newImagine;
        }

        return count < MAX_ITERATIONS ? count : -1;
    }

    public static String getString() {
        return "Tricorn";
    }
}
