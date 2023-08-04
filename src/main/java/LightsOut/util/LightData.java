package LightsOut.util;

import com.badlogic.gdx.graphics.Color;

public class LightData {
    public float x;

    public float y;

    public float radius;

    public float intensity;

    public Color color;

    public LightData(float x, float y, float radius, Color color) {
        this(x, y, radius, 0.1F, color);
    }

    public LightData(float x, float y, float radius, float intensity, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.intensity = intensity;
        this.color = color;
    }
}
