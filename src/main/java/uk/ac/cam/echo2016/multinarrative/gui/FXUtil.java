package uk.ac.cam.echo2016.multinarrative.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.paint.Color;

public class FXUtil {

    private FXUtil() {
    }

    /**
     * Converts a colour to it's RGB hex code
     * 
     * @param c
     * @return
     */
    public static String colorToHex(Color c) {
        String r = Integer.toHexString((int) (c.getRed() * 255));
        String g = Integer.toHexString((int) (c.getGreen() * 255));
        String b = Integer.toHexString((int) (c.getBlue() * 255));
        return "#" + (r.length() == 1 ? "0" + r : r) + (g.length() == 1 ? "0" + g : g)
                + (b.length() == 1 ? "0" + b : b);
    }

    public static DoubleBinding degreesAngle(DoubleBinding x, DoubleBinding y) {
        return Bindings.createDoubleBinding(
                () -> (x.get() < 0 ? -180 : 0) + Math.atan(y.get() / x.get()) * 180 / Math.PI, x, y);
    }

}
