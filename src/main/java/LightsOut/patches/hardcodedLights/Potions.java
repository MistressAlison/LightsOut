package LightsOut.patches.hardcodedLights;

import LightsOut.patches.CustomLightPatches;
import LightsOut.util.CustomLightData;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.widepotions.potions.WidePotion;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtNewMethod;

public class Potions {
    @SpirePatch2(clz = WidePotion.class, method = SpirePatch.CONSTRUCTOR, requiredModId = "widepotions")
    public static class WidePotionTest {
        @SpireRawPatch
        public static void patch(CtBehavior ctBehavior) throws CannotCompileException {
            CtClass ctClass = ctBehavior.getDeclaringClass();
            ctClass.addMethod(CtNewMethod.make("public float[] _lightsOutGetXYRI() {return LightsOut.patches.RenderPatches.WidePotionTest.wideXYRI(potion, posX, posY);}", ctClass));
            ctClass.addMethod(CtNewMethod.make("public com.badlogic.gdx.graphics.Color[] _lightsOutGetColor() {return LightsOut.patches.RenderPatches.WidePotionTest.wideColor(potion);}", ctClass));
        }

        public static float[] wideXYRI(AbstractPotion basePotion, float x, float y) {
            CustomLightData data = CustomLightPatches.customLights.get(basePotion.getClass());
            if (data != null) {
                float[] xyri = data.getXYRIData(basePotion);
                float[] ret = new float[xyri.length*2];
                for (int i = 0 ; i < xyri.length ; i++) {
                    if (i % 4 == 0) {
                        //X
                        ret[i] = x;
                        ret[i + xyri.length] = x + 64f * Settings.scale;
                    } else if (i % 4 == 1) {
                        //Y
                        ret[i] = y;
                        ret[i + xyri.length] = y;
                    } else if (i % 4 == 2) {
                        //R
                        ret[i] = xyri[i] * 1.5f;
                        ret[i + xyri.length] = xyri[i] * 1.5f;
                    } else {
                        //I
                        ret[i] = xyri[i] * 1.25f;
                        ret[i + xyri.length] = xyri[i] * 1.25f;
                    }
                }
                return ret;
            }
            return new float[0];
        }

        public static Color[] wideColor(AbstractPotion basePotion) {
            CustomLightData data = CustomLightPatches.customLights.get(basePotion.getClass());
            if (data != null) {
                Color[] color = data.getColorData(basePotion);
                Color[] ret = new Color[color.length*2];
                for (int i = 0 ; i < color.length ; i++) {
                    ret[i] = color[i];
                    ret[i + color.length] = color[i];
                }
                return ret;
            }
            return new Color[0];
        }
    }
}
