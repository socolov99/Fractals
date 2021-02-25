import fractals.FractalGenerator;
import fractals.Mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    private int displaySize;
    private JImageDisplay imageDisplay;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double range;
    private JButton resetButton;
    private int rowsRemaining;

    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(800);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }

    public FractalExplorer(int displaySize) {
        this.displaySize = displaySize;
        fractalGenerator = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractalGenerator.getInitialRange(range);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        imageDisplay = new JImageDisplay(displaySize, displaySize);
        frame.getContentPane().add(imageDisplay, BorderLayout.CENTER);

        FractalHandler handler = new FractalHandler();
        resetButton = new JButton("Reset Display");
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(handler);
        frame.getContentPane().add(resetButton, BorderLayout.SOUTH);

        frame.getContentPane().addMouseListener(new MouseHandler());

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private class FractalHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();

            if (cmd.equals("reset")) {
                range = new Rectangle2D.Double();
                fractalGenerator.getInitialRange(range);

                drawFractal();
            }
        }
    }

    private class MouseHandler extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {

            if (rowsRemaining > 0) {
                return;
            }

            double xCord = FractalGenerator.getCoord(range.x, range.x + range.width,
                    displaySize, e.getX());

            double yCord = FractalGenerator.getCoord(range.y, range.y + range.height,
                    displaySize, e.getY());

            fractalGenerator.recenterAndZoomRange(range, xCord, yCord, 0.5);

            drawFractal();
        }
    }

    private void drawFractal() {
        enableUI(false);
        for (int y = 0; y < displaySize; y++) {
            double yCord = FractalGenerator.getCoord(range.y, range.y + range.height,
                    displaySize, y);
            for (int x = 0; x < displaySize; x++) {
                double xCord = FractalGenerator.getCoord(range.x, range.x + range.width,
                        displaySize, x);

                int numIterates = fractalGenerator.numIterations(xCord, yCord);
                int rgbColor;

                if (numIterates >= 0) {
                    float hue = 0.7f + (float) numIterates / 200f;
                    rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                } else {
                    rgbColor = 0;
                }
                imageDisplay.drawPixel(x, y, rgbColor);
            }
        }
        imageDisplay.repaint();
        if (rowsRemaining-- < 1) {
            enableUI(true);
        }
    }

    private void enableUI(boolean val) {
        resetButton.setEnabled(val);
    }
}
