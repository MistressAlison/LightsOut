package LightsOut.util;

import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;
import java.util.List;

public interface CustomLightData {
    float[] getXYRIData(Object paramObject);

    Color[] getColorData(Object paramObject);

    default List<LightData> getLightData(Object o) {
        ArrayList<LightData> data = new ArrayList<>();
        float[] xyri = getXYRIData(o);
        Color[] c = getColorData(o);
        for (int i = 0; i < c.length; i++)
            data.add(new LightData(xyri[4 * i], xyri[4 * i + 1], xyri[4 * i + 2], xyri[4 * i + 3], c[i]));
        return data;
    }
}
