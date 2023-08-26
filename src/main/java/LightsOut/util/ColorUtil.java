package LightsOut.util;

import com.badlogic.gdx.graphics.Color;

public class ColorUtil {
    // https://en.wikipedia.org/wiki/Tertiary_color#Tertiary-,_quaternary-,_and_quinary-_terms
    public static final Color RED = new Color(0xFF0000FF);
    public static final Color SCARLET = new Color(0xFF2000FF);
    public static final Color VERMILION = new Color(0xFF4000FF);
    public static final Color PERSIMMON = new Color(0xFF6000FF);
    public static final Color ORANGE = new Color(0xFF8000FF);
    public static final Color ORANGE_PEEL = new Color(0xFFA000FF);
    public static final Color AMBER = new Color(0xFFC000FF);
    public static final Color GOLDEN_YELLOW = new Color(0xFFE000FF);
    public static final Color YELLOW = new Color(0xFFFF00FF);
    public static final Color LEMON = new Color(0xE0FF00FF);
    public static final Color LIME = new Color(0xC0FF00FF);
    public static final Color SPRING_BUG = new Color(0xA0FF00FF);
    public static final Color CHARTREUSE = new Color(0x80FF00FF);
    public static final Color BRIGHT_GREEN = new Color(0x60FF00FF);
    public static final Color HARLEQUIN = new Color(0x40FF00FF);
    public static final Color NEON_GREEN = new Color(0x20FF00FF);
    public static final Color GREEN =  new Color(0x00FF00FF);
    public static final Color JADE = new Color(0x00FF20FF);
    public static final Color ERIN = new Color(0x00FF40FF);
    public static final Color EMERALD = new Color(0x00FF60FF);
    public static final Color SPRING_GREEN = new Color(0x00FF80FF);
    public static final Color MINT = new Color(0x00FFA0FF);
    public static final Color AQUAMARINE = new Color(0x00FFC0FF);
    public static final Color TURQUOISE = new Color(0x00FFE0FF);
    public static final Color CYAN =  new Color(0x00FFFFFF);
    public static final Color SKY_BLUE = new Color(0x00E0FFFF);
    public static final Color CAPRI = new Color(0x00C0FFFF);
    public static final Color CORNFLOWER = new Color(0x00A0FFFF);
    public static final Color AZURE = new Color(0x0080FFFF);
    public static final Color COBALT = new Color(0x0060FFFF);
    public static final Color CERULEAN = new Color(0x40FFFF);
    public static final Color SAPPHIRE = new Color(0x0020FFFF);
    public static final Color BLUE = new Color(0x0000FFFF);
    public static final Color IRIS = new Color(0x2000FFFF);
    public static final Color INDIGO = new Color(0x4000FFFF);
    public static final Color VERONICA = new Color(0x6000FFFF);
    public static final Color VIOLET = new Color(0x8000FFFF);
    public static final Color AMETHYST = new Color(0xA000FFFF);
    public static final Color PURPLE = new Color(0xC000FFFF);
    public static final Color PHLOX = new Color(0xE000FFFF);
    public static final Color MAGENTA = new Color(0xFF00FFFF);
    public static final Color FUCHSIA = new Color(0xFF00E0FF);
    public static final Color CERISE = new Color(0xFF00C0FF);
    public static final Color DEEP_PINK = new Color(0xFF00A0FF);
    public static final Color ROSE = new Color(0xFF0080FF);
    public static final Color RASPBERRY = new Color(0xFF0060FF);
    public static final Color CRIMSON = new Color(0xFF0040FF);
    public static final Color AMARANTH = new Color(0xFF0020FF);

    public static final Color WHITE = new Color(1, 1, 1, 1);
    public static final Color LIGHT_GRAY =  new Color(0.75f, 0.75f, 0.75f, 1);
    public static final Color GRAY =  new Color(0.5f, 0.5f, 0.5f, 1);
    public static final Color DARK_GRAY =  new Color(0.25f, 0.25f, 0.25f, 1);
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
