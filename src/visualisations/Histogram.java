package visualisations;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Class that defines the drawing of a histogram. This class extends Vue.
 *
 * @author Thierry JIAO - Samy BENCHAREF - Thanh Huy LE - Milo THIBAUD - Lucas BERENGUER
 */
public class Histogram extends Vue {

    private static final long serialVersionUID = 1917L;
    private final Point2D.Float[] coordonnees;
    private float yMax = 0;
    private float yMin = 0;

    public Histogram(ArrayList<Float> values, String name) {

        super(name);

        // Instanced the array list for x and y
        ArrayList<Float> x = new ArrayList();
        ArrayList<Float> y = new ArrayList();

        int xPosition = Vue.getXPosition();
        int yPosition = Vue.getYPosition();
        setLocation(xPosition, yPosition);
        this.coordonnees = new Point2D.Float[values.size()];
        yMax = 0;
        yMin = 0;

        // Count the occurrence
        float occur;
        for (int i = 0; i < values.size(); i++) {
            occur = 0.00f;
            for (Float valeur : values) {
                if (valeur.equals(values.get(i))) {
                    occur = occur + 1;
                }
            }
            x.add(values.get(i));
            y.add(occur / values.size());
        }

        // Add the points to the graph
        for (int i = 0; i < x.size(); i++) {
            if (y.get(i) > yMax) {
                yMax = y.get(i);
            }
            if (y.get(i) < yMin) {
                yMin = y.get(i);
            }
            coordonnees[i] = new Point2D.Float(x.get(i), y.get(i));
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        int largeur = values.size() + 10;
        if (largeur > 1000) {
            largeur = 1000;
        }
        setSize(largeur, 200);
        setVisible(true);
        repaint();
    }

    public void paint() {
        paint(getGraphics());
    }


    public void paint(Graphics g) {
        if (g == null) {
            return;
        }
        // effacement total
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.green);


        int x0Axe = 500;
        float deltaX = getContentPane().getWidth() - (2 * x0Axe);

        int y0Axe = 0;
        float deltaY = getContentPane().getHeight() - (2 * y0Axe);


        if ((yMax > 0) && (yMin <= 0)) {
            y0Axe += (int) (deltaY * (yMax / (yMax - yMin)));
        } else if ((yMax > 0) && (yMin > 0)) {
            y0Axe += deltaY;
        } else if (yMax <= 0) {
            y0Axe += 0;
        }
        getContentPane().getGraphics().drawLine(x0Axe, y0Axe, x0Axe + (int) deltaX + x0Axe, y0Axe);
        getContentPane().getGraphics().drawLine(x0Axe + (int) deltaX + x0Axe - 5, y0Axe - 5, x0Axe + (int) deltaX + x0Axe, y0Axe);
        getContentPane().getGraphics().drawLine(x0Axe + (int) deltaX + x0Axe - 5, y0Axe + 5, x0Axe + (int) deltaX + x0Axe, y0Axe);

        getContentPane().getGraphics().drawLine(x0Axe, y0Axe, x0Axe, y0Axe - (int) deltaY - y0Axe);
        getContentPane().getGraphics().drawLine(x0Axe + 5, 5, x0Axe, 0);
        getContentPane().getGraphics().drawLine(x0Axe - 5, 5, x0Axe, 0);


        // tracer la courbe

        float dx = deltaX / (float) coordonnees[coordonnees.length - 1].getX();
        float dy = 0.0f;
        if ((yMax >= 0) && (yMin <= 0)) {
            dy = deltaY / (yMax - yMin);
        } else if (yMin > 0) {
            dy = deltaY / yMax;
        } else if (yMax < 0) {
            dy = -(deltaY / yMin);
        }


        for (int i = 1; i < coordonnees.length; i++) {
            int x1 = (int) (coordonnees[i - 1].getX() * dx);
            int x2 = (int) (coordonnees[i].getX() * dx);
            int y1 = (int) (coordonnees[i - 1].getY() * dy);
            int y2 = (int) (coordonnees[i].getY() * dy);
            getContentPane().getGraphics().drawLine(x0Axe + x1, y0Axe - y1, x0Axe + x2, y0Axe - y2);
        }


    }

}
