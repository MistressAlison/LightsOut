package LightsOut.patches;


import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.megacrit.cardcrawl.characters.Defect;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.beyond.Exploder;
import com.megacrit.cardcrawl.monsters.beyond.SnakeDagger;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.vfx.*;
import com.megacrit.cardcrawl.vfx.campfire.CampfireBurningEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireEndingBurningEffect;
import com.megacrit.cardcrawl.vfx.combat.*;
import com.megacrit.cardcrawl.vfx.scene.*;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;
import javassist.*;

import java.util.ArrayList;

public class PatchFactory {
    public static final ArrayList<PatchData> PATCHES = new ArrayList<>();
    public static final String SCALE = "*com.megacrit.cardcrawl.core.Settings.scale";
    public static final String IMAGE = "com.megacrit.cardcrawl.helpers.ImageMaster";
    public static class PatchData {
        public Class<?> classToPatch;
        public String xyriMethod;
        public String colorMethod;

        public PatchData(Class<?> classToPatch, String xyri, String color) {
            this.classToPatch = classToPatch;
            this.xyriMethod = makeXYRIMethod(xyri);
            this.colorMethod = makeColorMethod(color);
        }
    }

    public static String makeXYRIMethod(String xyri) {
        return "public float[] _lightsOutGetXYRI() { return new float[]{"+xyri+"};}";
    }

    public static String makeColorMethod(String color) {
        return "public com.badlogic.gdx.graphics.Color[] _lightsOutGetColor() { return new com.badlogic.gdx.graphics.Color[]{"+color+"};}";
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

    public static String boneToXYRI(String bone, float r, float i) {
        return "skeleton.getX() + skeleton.findBone(\""+bone+"\").getWorldX(), skeleton.getY() + skeleton.findBone(\""+bone+"\").getWorldY(), "+r+SCALE+", "+i;
    }

    public static String bonesToXYRI(String[] bones, float[] r, float[] i) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int index = 0 ; index < bones.length ; index++) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(boneToXYRI(bones[index], r[index], i[index]));
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
        PATCHES.add(new PatchData(clazz, boneToXYRI(bone, r, i), colorToString(c)));
    }

    public static void addEntity(Class<? extends AbstractCreature> clazz, String[] bones, float[] r, float[] i, Color[] c) {
        PATCHES.add(new PatchData(clazz, bonesToXYRI(bones, r, i), colorToString(c)));
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
            addPotion(FirePotion.class, 250f, 1.2f);
            addPotion(BottledMiracle.class, 100f, 1.0f);
            addPotion(EntropicBrew.class, 100f, 1.0f);
            addPotion(DuplicationPotion.class, 100f, 1.0f);
            addPotion(DistilledChaosPotion.class, 100f, 1.0f);
            addPotion(FairyPotion.class, 100f, 1.0f);

            //Relics
            addRelic(BurningBlood.class, 100f, 0.8f, new Color(1.0f, 0.5f, 0.2f, 1.0f));
            addRelic(Lantern.class, 300f, 2.0f, new Color(1.0F, 0.96F, 0.87F, 1.0F));
            addRelic(NuclearBattery.class, 300f, 2.0f, new Color(0.7F, 0.7F, 1.0F, 1.0F));
            addRelic(FusionHammer.class, 150f, 0.8f, new Color(1.0F, 0.4F, 0.8F, 1.0F));
            addRelic(HolyWater.class, 50f, 0.5f, new Color(1.0F, 0.6F, 0.9F, 1.0F));
            addRelic(BottledFlame.class, 200f, 1.5f, new Color(1.0F, 0.6F, 0.2F, 1.0F));
            addRelic(BottledLightning.class, 200f, 2.0f, new Color(1.0F, 1.0F, 0.0F, 1.0F));
            addRelic(BlueCandle.class, 200f, 0.8f, new Color(0.4F, 0.0F, 1.0F, 1.0F));

            //Orbs
            addOrb(Lightning.class, 200f, 1.2f, new Color(1.0f, 1.0f, 0.6f, 1.0f));
            addSimpleVFX(LightningOrbPassiveEffect.class, 200f, 0.1f);
            addSimpleVFX(LightningOrbActivateEffect.class, 200f, 0.1f);
            addOrb(Frost.class, 100f, 0.5f, Color.CYAN);
            addOrb(Plasma.class, 300f, 1.5f, new Color(0.7F, 0.7F, 1.0F, 1.0F));
            addSimpleVFX(PlasmaOrbPassiveEffect.class, 200f, 0.1f);
            addSimpleVFX(PlasmaOrbActivateParticle.class, 200f, 0.1f);
            addOrb(Dark.class, 100f, 0.5f, Color.PURPLE);

            //Cards

            //Powers

            //Players
            addSimpleVFX(IroncladVictoryFlameEffect.class, 200f, 0.1f);
            addEntity(Defect.class, "Hips_sphere", 200f, 0.3f, Color.CYAN);

            //Monsters
            addEntity(Exploder.class, "inside", 200f, 1.2f, new Color(1.0f, 1.0f, 0.5f, 1.0f));
            addEntity(SnakeDagger.class, "root", 100f, 0.8f, new Color(0.5f, 1.0f, 0.5f, 1.0f));
            //Repto dagger

            //Effects
            addSimpleVFX(FastSmokeParticle.class, 300f, 2f);
            addSimpleVFX(LightningEffect.class, 2000f, 2f);
            addSimpleVFX(MindblastEffect.class, 2000f, 2f);
            addSimpleVFX(LaserBeamEffect.class, 2000f, 2f);

            addSimpleVFX(GhostlyFireEffect.class, 400f, 0.03f);
            addSimpleVFX(GhostlyWeakFireEffect.class, 400f, 0.03f);

            addSimpleVFX(StanceAuraEffect.class, 300f, 0.1f);

            addSimpleVFX(TorchParticleSEffect.class, 200f, 0.1f);
            addSimpleVFX(TorchParticleMEffect.class, 200f, 0.1f);
            addSimpleVFX(TorchParticleLEffect.class, 200f, 0.1f);
            addSimpleVFX(TorchParticleXLEffect.class, 200f, 0.1f);
            addSimpleVFX(StaffFireEffect.class, 200f, 0.1f);
            addSimpleVFX(GlowyFireEyesEffect.class, 200f, 0.1f);
            addSimpleVFX(FireBurstParticleEffect.class, 200f, 0.1f);
            addSimpleVFX(TorchHeadFireEffect.class, 200f, 0.1f);
            addSimpleVFX(GiantFireEffect.class, 200f, 0.1f);
            addSimpleVFX(LightRayFlyOutEffect.class, 200f, 0.1f);
            //TODO campfire broken
            addSimpleVFX(CampfireBurningEffect.class, 200f, 0.1f);
            addSimpleVFX(CampfireEndingBurningEffect.class, 200f, 0.1f);
            addSimpleVFX(FlameParticleEffect.class, 200f, 0.1f);
            addSimpleVFX(RedFireBurstParticleEffect.class, 200f, 0.1f);
            addSimpleVFX(SlowFireParticleEffect.class, 200f, 0.1f);
            addSimpleVFX(RoomShineEffect.class, 200f, 0.1f);
            addSimpleVFX(RoomShineEffect2.class, 200f, 0.1f);
            addCustom(LightFlareParticleEffect.class, "pos.x, pos.y, 200f"+SCALE+", 0.1f", "color");
            addCustom(AwakenedEyeParticle.class, "x + "+IMAGE+".ROOM_SHINE_2.packedWidth / 2.0F, y + "+IMAGE+".ROOM_SHINE_2.packedHeight / 2.0F, 200f"+SCALE+", 0.1f", "color");

            addSimpleVFX(MiracleEffect.class, 200f, 1.0f);
            addSimpleVFX(LightBulbEffect.class, 200f, 1.0f);

            addSimpleVFX(GlowRelicParticle.class, 50f, 0.05f);

            addSimpleVFX(BossChestShineEffect.class, 50f, 0.5f);
            addSimpleVFX(FireFlyEffect.class, 50f, 0.5f);
            addSimpleVFX(ShinySparkleEffect.class, 50f, 0.5f);
            addSimpleVFX(ShineSparkleEffect.class, 50f, 0.5f);
            addSimpleVFX(RarePotionParticleEffect.class, 50f, 0.5f);
            addSimpleVFX(UncommonPotionParticleEffect.class, 50f, 0.5f);
            addSimpleVFX(ImpactSparkEffect.class, 50f, 0.5f);

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
