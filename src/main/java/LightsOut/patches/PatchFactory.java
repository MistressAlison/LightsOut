package LightsOut.patches;


import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.megacrit.cardcrawl.characters.Defect;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.EnergyPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import javassist.*;

import java.util.ArrayList;

public class PatchFactory {
    public static final ArrayList<PatchData> PATCHES = new ArrayList<>();
    public static final String SCALE = "*com.megacrit.cardcrawl.core.Settings.scale";
    public static class PatchData {
        public Class<?> classToPatch;
        public String xyriMethod;
        public String colorMethod;

        public PatchData(Class classToPatch, String xyri, String color) {
            this.classToPatch = classToPatch;
            this.xyriMethod = makeXYRIMethod(xyri);
            this.colorMethod = makeColorMethod(color);
        }
    }

    public static String makeXYRIMethod(String xyri) {
        return "public float[] _lightsOutGetXYRI() { return new float[]{"+xyri+"};}";
    }

    public static String makeColorMethod(String color) {
        return "public com.badlogic.gdx.graphics.Color[] _lightsOutGetColor() { return new com.badlogic.gdx.graphics.Color[] {"+color+"};}";
    }

    public static String colorToString(Color... colors) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Color c : colors) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append("new com.badlogic.gdx.graphics.Color(").append(Color.rgba8888(c)).append(")");
        }
        return sb.toString();
    }

    public static void addPotion(Class<? extends AbstractPotion> clazz, float r, float i) {
        PATCHES.add(new PatchData(clazz, "posX, posY, "+r+SCALE+", "+i, "liquidColor"));
    }

    public static void addRelic(Class<? extends AbstractRelic> clazz, float r, float i, Color c) {
        PATCHES.add(new PatchData(clazz, "currentX, currentY, "+r+SCALE+", "+i, colorToString(c)));
    }

    public static void addOrb(Class<? extends AbstractOrb> clazz, float r, float i, Color c) {
        PATCHES.add(new PatchData(clazz, "cX, cY, "+r+SCALE+", "+i, colorToString(c)));
    }

    public static void addEntity(Class<? extends AbstractCreature> clazz, String bone, float r, float i, Color c) {
        PATCHES.add(new PatchData(clazz, "skeleton.getX() + skeleton.findBone(\""+bone+"\").getWorldX(), skeleton.getY() + skeleton.findBone(\""+bone+"\").getWorldY(), "+r+SCALE+", "+i, colorToString(c)));
    }

    public static void addSimpleVFX(Class<? extends AbstractGameEffect> clazz, float r, float i) {
        PATCHES.add(new PatchData(clazz, "x, y, "+r+SCALE+", "+i, "color"));
    }

    public static void addColoredVFX(Class<? extends AbstractGameEffect> clazz, float r, float i, Color c) {
        PATCHES.add(new PatchData(clazz, "x, y, "+r+SCALE+", "+i, colorToString(c)));
    }

    public static void addCard() {

    }

    public static void addCustom(Class<?> clazz, String xyri, Color... colors) {
        addCustom(clazz, xyri, colorToString(colors));
    }

    public static void addCustom(Class<?> clazz, String xyri, String color) {
        PATCHES.add(new PatchData(clazz, xyri, color));
    }

    @SpirePatch(clz = CardCrawlGame.class, method = SpirePatch.CONSTRUCTOR)
    public static class AbstractCardDynamicPatch {
        @SpireRawPatch
        public static void patch(CtBehavior ctBehavior) throws NotFoundException {
            //Potions
            addPotion(EnergyPotion.class, 100f, 1.0f);

            //Relics

            //Orbs

            //Cards

            //Players
            addEntity(Defect.class, "Hips_sphere", 20f, 2f, Color.CYAN);
            //Monsters

            //Effects

            for (PatchData data : PATCHES) {
                CtClass ctClass = ctBehavior.getDeclaringClass().getClassPool().get(data.classToPatch.getName());
                try {
                    ctClass.addMethod(CtNewMethod.make(data.xyriMethod, ctClass));
                    ctClass.addMethod(CtNewMethod.make(data.colorMethod, ctClass));
                } catch (CannotCompileException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
