package LightsOut.util;

import com.badlogic.gdx.graphics.Color;

public class ColorUtil {
    public static final Color RED = new Color(1, 0, 0, 1);
    public static final Color VERMILION = new Color(1, 0.25f, 0, 1);
    public static final Color ORANGE = new Color(1, 0.5f, 0, 1);
    public static final Color AMBER = new Color(1, 0.75f, 0, 1);
    public static final Color YELLOW = new Color(1, 1, 0, 1);
    public static final Color LIME = new Color(0.75f, 1, 0, 1);
    public static final Color CHARTREUSE = new Color(0.5f, 1, 0, 1);
    public static final Color HARLEQUIN = new Color(0.25f, 1, 0, 1);
    public static final Color GREEN =  new Color(0, 1, 0, 1);
    public static final Color ERIN = new Color(0, 1, 0.25f, 1);
    public static final Color SPRING_GREEN = new Color(0, 1, 0.5f, 1);
    public static final Color AQUAMARINE = new Color(0, 1, 0.75f, 1);
    public static final Color CYAN =  new Color(0, 1, 1, 1);
    public static final Color CAPRI = new Color(0, 0.75f, 1, 1);
    public static final Color AZURE = new Color(0, 0.5f, 1, 1);
    public static final Color CERULEAN = new Color(0, 0.25f, 1, 1);
    public static final Color BLUE = new Color(0, 0, 1, 1);
    public static final Color INDIGO = new Color(0.25f, 0, 1, 1);
    public static final Color VIOLET = new Color(0.5f, 0, 1, 1);
    public static final Color PURPLE = new Color(0.25f, 0, 1, 1);
    public static final Color MAGENTA = new Color(1, 0, 1, 1);
    public static final Color CERISE = new Color(1, 0, 0.75f, 1);
    public static final Color ROSE = new Color(1, 0, 0.5f, 1);
    public static final Color CRIMSON = new Color(1, 0, 0.25f, 1);
    public static final Color WHITE = new Color(1, 1, 1, 1);
    public static final Color QUARTER_GRAY =  new Color(0.75f, 0.75f, 0.75f, 1);
    public static final Color HALF_GRAY =  new Color(0.5f, 0.5f, 0.5f, 1);
    public static final Color THREE_QUARTER_GRAY =  new Color(0.25f, 0.25f, 0.25f, 1);
    public static final Color BLACK = new Color(0, 0, 0, 1);
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public static Color mix(Color c1, Color c2) {
        return c1.cpy().lerp(c2, 0.5f);
    }

    public static Color lighten(Color c) {
        return c.cpy().lerp(Color.WHITE, 0.25f);
    }

    public static Color darken(Color c) {
        return c.cpy().lerp(Color.BLACK, 0.25f);
    }

    public static Color pastel(Color c) {
        return colorFromHSL(getHue(c), getSat(c), 0.8f, c.a);
    }

    private static float getHue(Color c) {
        float max = c.r;
        float min = c.r;
        if (c.g > max) {
            max = c.g;
        }
        if (c.b > max) {
            max = c.b;
        }
        if (c.g < min) {
            min = c.g;
        }
        if (c.b < min) {
            min = c.b;
        }
        float delta = max - min;
        if (delta == 0) {
            return 0;
        }
        if (c.g >= c.b) {
            return (float) Math.toDegrees(Math.acos((c.r - c.g/2 - c.b/2)/Math.sqrt(c.r*c.r + c.g*c.g + c.b*c.b - c.r*c.g - c.r*c.b - c.g*c.b)));
        } else {
            return 360 - (float) Math.toDegrees(Math.acos((c.r - c.g/2 - c.b/2)/Math.sqrt(c.r*c.r + c.g*c.g + c.b*c.b - c.r*c.g - c.r*c.b - c.g*c.b)));
        }
    }

    private static float getSat(Color c) {
        float max = c.r;
        float min = c.r;
        if (c.g > max) {
            max = c.g;
        }
        if (c.b > max) {
            max = c.b;
        }
        if (c.g < min) {
            min = c.g;
        }
        if (c.b < min) {
            min = c.b;
        }
        float delta = max - min;
        if (delta == 0) {
            return 0;
        }
        float lightness = (max + min)/2f;
        return delta / (1 - Math.abs(2*lightness - 1));
    }

    public static Color colorFromHSL(float hue, float sat, float light, float alpha) {
        float d = sat * (1 - Math.abs(2*light - 1));
        float x = d * (1 - Math.abs(((hue/60f)%2) - 1));
        float m = light - d/2f;
        if (0 <= hue && hue < 60) {
            return new Color(d+m, x+m, m, alpha);
        } else if (60 <= hue && hue < 120) {
            return new Color(x+m, d+m, m, alpha);
        } else if (120 <= hue && hue < 180) {
            return new Color(m, d+m, x+m, alpha);
        } else if (180 <= hue && hue < 240) {
            return new Color(m, x+m, d+m, alpha);
        } else if (240 <= hue && hue < 300) {
            return new Color(x+m, m, d+m, alpha);
        } else {
            return new Color(d+m, m, x+m, alpha);
        }
    }
}
