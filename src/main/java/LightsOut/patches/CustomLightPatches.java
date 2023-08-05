package LightsOut.patches;

import LightsOut.LightsOutMod;
import LightsOut.util.CustomLightData;
import LightsOut.util.ShaderLogic;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.clapper.util.classutil.*;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomLightPatches {
    private static final String GET_XYRI_METHOD = "_lightsOutGetXYRI";
    private static final String GET_COLOR_METHOD = "_lightsOutGetColor";
    private static final String GET_BG_XYRI_METHOD = "_lightsOutGetCharSelectXYRI";
    private static final String GET_BG_COLOR_METHOD = "_lightsOutGetCharSelectColor";
    public static final HashMap<Class<?>, CustomLightData> customLights = new HashMap<>();
    public static final HashMap<Class<?>, CustomLightData> customCharSelectLights = new HashMap<>();

    public static void processCustomLights(Object o) {
        if (LightsOutMod.modEnabled) {
            CustomLightData data = customLights.get(o.getClass());
            if (data != null)
                ShaderLogic.lightsToRender.addAll(data.getLightData(o));
        }
    }

    public static void processCustomCharacterSelectLights(Object o) {
        if (LightsOutMod.modEnabled) {
            CustomLightData data = customCharSelectLights.get(o.getClass());
            if (data != null)
                ShaderLogic.lightsToRender.addAll(data.getLightData(o));
        }
    }

    public static void findCustomLights() {
        ClassFinder finder = new ClassFinder();
        finder.add(new File(Loader.STS_JAR));
        for (ModInfo modInfo : Loader.MODINFOS) {
            if (modInfo.jarURL != null)
                try {
                    finder.add(new File(modInfo.jarURL.toURI()));
                } catch (URISyntaxException ignored) {}
        }
        AndClassFilter andClassFilter = new AndClassFilter(
                new NotClassFilter(new InterfaceOnlyClassFilter()),
                new ClassModifiersClassFilter(Modifier.PUBLIC),
                new OrClassFilter(
                        new SubclassClassFilter(AbstractGameEffect.class),
                        new SubclassClassFilter(AbstractPlayer.class),
                        new SubclassClassFilter(AbstractMonster.class),
                        new SubclassClassFilter(AbstractOrb.class),
                        new SubclassClassFilter(AbstractRelic.class),
                        new SubclassClassFilter(AbstractPotion.class),
                        new SubclassClassFilter(AbstractCard.class)));

        ArrayList<ClassInfo> foundClasses = new ArrayList<>();
        finder.findClasses(foundClasses, andClassFilter);
        for (ClassInfo classInfo : foundClasses) {
            try {
                CtClass ctClass = Loader.getClassPool().getCtClass(classInfo.getClassName());
                for (CtMethod ctm : ctClass.getMethods()) {
                    if (ctm.getName().equals(GET_COLOR_METHOD)) {
                        try {
                            Class<?> clazz = Class.forName(classInfo.getClassName());
                            customLights.put(clazz, makeData(clazz, GET_XYRI_METHOD, GET_COLOR_METHOD));
                        } catch (Exception ignored) {}
                    } else if (ctm.getName().equals(GET_BG_COLOR_METHOD)) {
                        try {
                            Class<?> clazz = Class.forName(classInfo.getClassName());
                            customCharSelectLights.put(clazz, makeData(clazz, GET_BG_XYRI_METHOD, GET_BG_COLOR_METHOD));
                        } catch (Exception ignored) {}
                    }
                }
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static CustomLightData makeData(Class<?> clazz, String xyri, String color) throws NoSuchMethodException {
        final Method getXYRI = clazz.getMethod(xyri);
        final Method getColor = clazz.getMethod(color);
        return new CustomLightData() {
            public float[] getXYRIData(Object o) {
                try {
                    return (float[])getXYRI.invoke(o, new Object[0]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            public Color[] getColorData(Object o) {
                try {
                    return (Color[])getColor.invoke(o, new Object[0]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
