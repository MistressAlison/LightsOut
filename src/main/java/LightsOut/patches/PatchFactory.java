package LightsOut.patches;


import LightsOut.LightsOutMod;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.SeeingRed;
import com.megacrit.cardcrawl.characters.Defect;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.beyond.MindBloom;
import com.megacrit.cardcrawl.events.beyond.MoaiHead;
import com.megacrit.cardcrawl.events.beyond.SecretPortal;
import com.megacrit.cardcrawl.events.beyond.TombRedMask;
import com.megacrit.cardcrawl.events.city.*;
import com.megacrit.cardcrawl.events.exordium.*;
import com.megacrit.cardcrawl.events.shrines.*;
import com.megacrit.cardcrawl.monsters.beyond.Exploder;
import com.megacrit.cardcrawl.monsters.beyond.OrbWalker;
import com.megacrit.cardcrawl.monsters.beyond.SnakeDagger;
import com.megacrit.cardcrawl.monsters.beyond.WrithingMass;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import com.megacrit.cardcrawl.powers.EchoPower;
import com.megacrit.cardcrawl.powers.WraithFormPower;
import com.megacrit.cardcrawl.powers.watcher.DevaPower;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.vfx.*;
import com.megacrit.cardcrawl.vfx.campfire.CampfireBurningEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireEndingBurningEffect;
import com.megacrit.cardcrawl.vfx.combat.*;
import com.megacrit.cardcrawl.vfx.scene.*;
import com.megacrit.cardcrawl.vfx.stance.CalmParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.DivinityParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;
import com.megacrit.cardcrawl.vfx.stance.WrathParticleEffect;
import javassist.*;

import java.util.ArrayList;

import static LightsOut.util.ColorUtil.*;

public class PatchFactory {
    public static final ArrayList<PatchData> PATCHES = new ArrayList<>();
    public static final String SCALE = "*com.megacrit.cardcrawl.core.Settings.scale";
    public static final String WIDTH = "com.megacrit.cardcrawl.core.Settings.WIDTH";
    public static final String HEIGHT = "com.megacrit.cardcrawl.core.Settings.HEIGHT";
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

    public static String boneToXYRI(String[] bones, float[] r, float[] i) {
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

    public static String cardToXYRI(float dx, float dy, float r, float i) {
        float dist = (float) Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));
        float atan = (float) Math.toDegrees(Math.atan(dx/dy));
        return "current_x + "+dist+SCALE+"*drawScale * com.badlogic.gdx.math.MathUtils.sinDeg((float)("+atan+" - angle)), current_y + "+dist+SCALE+"*drawScale * com.badlogic.gdx.math.MathUtils.cosDeg((float)("+atan+" - angle)),"+r+SCALE+"*drawScale, "+i;
    }

    public static String cardToXYRI(float[] dx, float[] dy, float[] r, float[] i) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int index = 0 ; index < dx.length ; index++) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(cardToXYRI(dx[index], dy[index], r[index], i[index]));
        }
        return sb.toString();
    }

    public static String eventToXYTI(float x, float y, float r, float i) {
        return x+SCALE+", "+y+SCALE+", "+r+SCALE+", "+i;
    }

    public static String eventToXYRI(float[] dx, float[] dy, float[] r, float[] i) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int index = 0 ; index < dx.length ; index++) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(eventToXYTI(dx[index], dy[index], r[index], i[index]));
        }
        return sb.toString();
    }

    public static String eventToXYRI(float[] flatXYRI) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int index = 0 ; index < flatXYRI.length ; index++) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(flatXYRI[index]);
            if (index % 4 != 3) {
                sb.append(SCALE);
            }
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
        PATCHES.add(new PatchData(clazz, boneToXYRI(bones, r, i), colorToString(c)));
    }

    public static void addSimpleVFX(Class<? extends AbstractGameEffect> clazz, float r, float i) {
        PATCHES.add(new PatchData(clazz, "x, y, "+r+SCALE+", "+i, "color"));
    }

    public static void addColoredVFX(Class<? extends AbstractGameEffect> clazz, float r, float i, Color c) {
        PATCHES.add(new PatchData(clazz, "x, y, "+r+SCALE+", "+i, colorToString(c)));
    }

    public static void addCard(Class<? extends AbstractCard> clazz, float dx, float dy, float r, float i, Color c) {
        PATCHES.add(new PatchData(clazz, cardToXYRI(dx, dy, r, i), colorToString(c)));
    }

    public static void addCard(Class<? extends AbstractCard> clazz, float[] dx, float[] dy, float[] r, float[] i, Color... c) {
        PATCHES.add(new PatchData(clazz, cardToXYRI(dx, dy, r, i), colorToString(c)));
    }

    public static void addEvent(Class<? extends AbstractEvent> clazz, float x, float y, float r, float i, Color c) {
        PATCHES.add(new PatchData(clazz, eventToXYTI(x, y, r, i), colorToString(c)));
    }

    public static void addEvent(Class<? extends AbstractEvent> clazz, float[] x, float[] y, float[] r, float[] i, Color... c) {
        PATCHES.add(new PatchData(clazz, eventToXYRI(x, y, r, i), colorToString(c)));
    }

    public static void addEvent(Class<? extends AbstractEvent> clazz, float[] flatXYRI, Color... c) {
        PATCHES.add(new PatchData(clazz, eventToXYRI(flatXYRI), colorToString(c)));
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
            addPotion(FirePotion.class, 100f, 1.0f);
            addPotion(BottledMiracle.class, 100f, 1.0f);
            addPotion(EntropicBrew.class, 100f, 1.0f);
            addPotion(DuplicationPotion.class, 100f, 1.0f);
            addPotion(DistilledChaosPotion.class, 100f, 1.0f);
            addCustom(FairyPotion.class, "posX, posY, "+50f+SCALE+", "+1.0, Color.LIME);
            addCustom(GhostInAJar.class, "posX, posY, "+50f+SCALE+", "+1.0, Color.PURPLE);

            //Relics
            addRelic(BurningBlood.class, 100f, 0.8f, new Color(1.0f, 0.5f, 0.2f, 1.0f));
            addRelic(Lantern.class, 300f, 1.5f, new Color(1.0F, 0.96F, 0.87F, 1.0F));
            addRelic(NuclearBattery.class, 300f, 1.5f, new Color(0.7F, 0.7F, 1.0F, 1.0F));
            addRelic(FusionHammer.class, 150f, 0.8f, new Color(1.0F, 0.4F, 0.8F, 1.0F));
            addRelic(HolyWater.class, 50f, 0.5f, new Color(1.0F, 0.6F, 0.9F, 1.0F));
            addRelic(BottledFlame.class, 200f, 1.25f, new Color(1.0F, 0.6F, 0.2F, 1.0F));
            addRelic(BottledLightning.class, 200f, 1.5f, new Color(1.0F, 1.0F, 0.0F, 1.0F));
            addRelic(BlueCandle.class, 200f, 0.8f, new Color(0.4F, 0.0F, 1.0F, 1.0F));
            addRelic(MutagenicStrength.class, 100f, 0.5f, new Color(1.0f, 0.5f, 0.2f, 1.0f));
            addRelic(Ectoplasm.class, 100f, 0.4f, Color.LIME);

            //Orbs
            addOrb(Lightning.class, 200f, 1.2f, new Color(1.0f, 1.0f, 0.6f, 1.0f));
            addSimpleVFX(LightningOrbPassiveEffect.class, 200f, 0.1f);
            addSimpleVFX(LightningOrbActivateEffect.class, 200f, 0.1f);
            addOrb(Frost.class, 100f, 0.5f, Color.CYAN);
            addSimpleVFX(FrostOrbPassiveEffect.class, 100f, 0.1f);
            addCustom(FrostOrbActivateParticle.class, "cX, cY, 100f"+SCALE+", 0.3f", "color");
            addOrb(Plasma.class, 300f, 1.5f, new Color(0.7F, 0.7F, 1.0F, 1.0F));
            addSimpleVFX(PlasmaOrbPassiveEffect.class, 200f, 0.1f);
            addSimpleVFX(PlasmaOrbActivateParticle.class, 200f, 0.1f);
            addOrb(Dark.class, 100f, 0.5f, Color.PURPLE);
            addSimpleVFX(DarkOrbPassiveEffect.class, 100f, 0.1f);
            addSimpleVFX(DarkOrbActivateParticle.class, 100f, 0.1f);

            //Cards
            addCard(SeeingRed.class, new float[]{-50f, 50f}, new float[]{80f, 80f}, new float[]{100f, 100f}, new float[]{0.5f, 0.5f}, Color.RED, Color.RED);

            //Powers
            addCustom(DemonFormPower.class, "owner.hb.cX, owner.hb.cY, (100f+10*amount)"+SCALE+",0.5f+0.05*amount", Color.RED);
            addCustom(WraithFormPower.class, "owner.hb.cX, owner.hb.cY, (100f+10*amount)"+SCALE+",0.5f+0.05*amount", Color.GREEN);
            addCustom(EchoPower.class, "owner.hb.cX, owner.hb.cY, (100f+10*amount)"+SCALE+",0.5f+0.05*amount", Color.BLUE);
            addCustom(DevaPower.class, "owner.hb.cX, owner.hb.cY, (100f+10*amount)"+SCALE+",0.5f+0.05*amount", Color.PURPLE);

            //Players
            addSimpleVFX(IroncladVictoryFlameEffect.class, 200f, 0.1f);
            addEntity(Defect.class, "Hips_sphere", 200f, 0.3f, Color.CYAN);

            //Monsters
            addEntity(Exploder.class, "inside", 200f, 1.2f, new Color(1.0f, 1.0f, 0.5f, 1.0f));
            addEntity(SnakeDagger.class, "root", 100f, 0.8f, new Color(0.5f, 1.0f, 0.5f, 1.0f));
            addEntity(OrbWalker.class, "orb_main", 200f, 1.2f, new Color(0.2f, 0.7f, 1.0f, 1.0f));
            addEntity(Sentry.class, "jewel", 50f, 0.5f, new Color(0.2f, 0.7f, 1.0f, 1.0f));
            addEntity(WrithingMass.class, "Eye", 50f, 0.7f, new Color(1.0f, 0.2f, 0.2f, 1.0f));

            //Events
            addEvent(AccursedBlacksmith.class, new float[]{207, 297, 250, 1.2f,   527, 443, 100, 0.3f}, Color.ORANGE, CHARTREUSE);
            addEvent(BackToBasics.class, 402, 444, 200, 1, mix(ORANGE, RED));
            addEvent(Beggar.class, new float[]{632, 661, 50f, 0.4f,   674, 434, 40f, 0.4f,   712, 610, 100f, 0.4f}, ORANGE, ORANGE, ORANGE);
            addEvent(Bonfire.class, new float[]{437, 252, 250, 1.2f,   285, 542, 50, 0.9f,   404, 576, 20, 0.8f,   575, 616, 50, 0.8f,   696, 441, 50, 0.9f}, mix(YELLOW, Color.ORANGE), Color.PURPLE, Color.PURPLE, Color.PURPLE, Color.PURPLE);
            addEvent(Colosseum.class, 476, 706, 250, 1.f, pastel(YELLOW));
            addEvent(Cleric.class, new float[]{656, 320, 250, 0.8f,   250, 323, 250, 0.8f}, CYAN, mix(CHARTREUSE, Color.YELLOW));
            addEvent(CursedTome.class, 528, 503, 150, 1.0f, Color.PINK);
            addEvent(DrugDealer.class, 411, 462, 50f, 1.3f, CYAN);
            addEvent(Duplicator.class, 437, 604, 150, 0.8f, CYAN);
            addEvent(FaceTrader.class, new float[]{457, 664, 50, 0.7f,   363, 529, 50, 0.7f,   544, 519, 50, 0.7f}, Color.PURPLE, Color.PURPLE, Color.PURPLE);
            addEvent(ForgottenAltar.class, 475, 511, 250f, 0.7f, Color.PURPLE);
            addEvent(FountainOfCurseRemoval.class, new float[]{452, 546, 50, 1f,   454, 281, 150, 1.2f}, CYAN, CYAN);
            addEvent(Ghosts.class, 440, 374, 250, 1.2f, mix(ORANGE, Color.YELLOW));
            addEvent(GoldenIdolEvent.class, 468, 615, 250, 1.5f, Color.GOLD);
            addEvent(GoldShrine.class, 458, 527, 200, 0.3f, Color.GOLD);
            addEvent(GoldenWing.class, new float[]{507, 478, 100f, 0.4f,   606, 211, 20f, 1.0f,   548, 307, 30f, 1.2f}, Color.GOLD, pastel(Color.GOLD), pastel(Color.GOLD));
            addEvent(KnowingSkull.class, 475, 542, 250, 1.5f, CHARTREUSE);
            addEvent(LivingWall.class, new float[]{705, 345, 50, 1.2f,   550, 564, 100, 1.2f,   432, 555, 100, 1.2f,   222, 466, 100, 1.2f,   276, 438, 50, 1.2f}, Color.GOLD, Color.GOLD, Color.GOLD, Color.GOLD, Color.GOLD);
            addEvent(MindBloom.class, 459, 389, 500, 1.4f, mix(Color.RED, Color.ORANGE));
            addEvent(Nest.class, 442, 489, 300f, 1.4f, pastel(YELLOW));
            addEvent(PurificationShrine.class, 444, 575, 150, 0.7f, ORANGE);
            addEvent(ScrapOoze.class, new float[]{288, 308, 250, 1.2f,   684, 564, 25, 1.2f}, ORANGE, WHITE);
            addEvent(SecretPortal.class, new float[]{416, 486, 400f, 0.6f,   416, 486, 150f, 1.0f}, CYAN, ORANGE);
            addEvent(ShiningLight.class, 580, 614, 600, 1.2f, mix(WHITE, pastel(YELLOW)));
            //addEvent(TheJoust.class, 716, 583, 100, 0.3f, Color.GRAY);
            addEvent(TheLibrary.class, 641, 710, 200, 1.0f, mix(WHITE, CYAN));
            addEvent(TheMausoleum.class, 447, 576, 300, 1.2f, ORANGE);
            addEvent(MoaiHead.class, new float[]{465, 274, 250, 1.2f,   541, 678, 30f, 1.2f,   413, 674, 15f, 1.2f}, ORANGE, YELLOW, YELLOW);
            addEvent(TombRedMask.class, 359, 477, 200, 1.2f, YELLOW);
            addEvent(Transmogrifier.class, 465, 576, 150, 0.7f, Color.PURPLE);
            addEvent(UpgradeShrine.class, new float[]{398, 597, 100, 1.2f,   517, 677, 100, 1.2f,   547, 579, 100, 1.2f}, ORANGE, ORANGE, ORANGE);
            addEvent(Vampires.class, 321, 568, 150, 0.4f, Color.RED);
            addEvent(WeMeetAgain.class, new float[]{498, 564, 25, 1.2f,   431, 566, 25, 1.2f}, YELLOW, YELLOW);
            addEvent(GoopPuddle.class, new float[]{605, 198, 200f, 0.6f,   329, 470, 150f, 0.5f,   682, 565, 100f, 0.4f,   517, 673, 50f, 0.4f}, Color.PURPLE, Color.PURPLE, Color.PURPLE, Color.PURPLE);

            //Effects
            addSimpleVFX(LightningEffect.class, 2000f, 2f);
            addSimpleVFX(MindblastEffect.class, 2000f, 2f);
            addSimpleVFX(LaserBeamEffect.class, 2000f, 2f);

            addSimpleVFX(GhostlyFireEffect.class, 400f, 0.03f);
            addSimpleVFX(GhostlyWeakFireEffect.class, 400f, 0.03f);

            addSimpleVFX(StanceAuraEffect.class, 300f, 0.1f);
            addCustom(WrathParticleEffect.class, "x, y+vY, 50f"+SCALE+", 0.1f", "color");
            addSimpleVFX(CalmParticleEffect.class, 50f, 0.1f);
            addCustom(DivinityParticleEffect.class, "x, y+vY, 50f"+SCALE+", 0.1f", "color");

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
            addSimpleVFX(CampfireBurningEffect.class, 200f, 0.1f);
            addSimpleVFX(CampfireEndingBurningEffect.class, 200f, 0.1f);
            addSimpleVFX(FlameParticleEffect.class, 200f, 0.1f);
            addSimpleVFX(RedFireBurstParticleEffect.class, 200f, 0.1f);
            addSimpleVFX(SlowFireParticleEffect.class, 200f, 0.1f);
            addSimpleVFX(RoomShineEffect.class, 200f, 0.1f);
            addSimpleVFX(RoomShineEffect2.class, 200f, 0.1f);
            addCustom(LightFlareParticleEffect.class, "pos.x, pos.y, 200f"+SCALE+", 0.1f", "color");
            addCustom(AwakenedEyeParticle.class, "x + "+IMAGE+".ROOM_SHINE_2.packedWidth / 2.0F, y + "+IMAGE+".ROOM_SHINE_2.packedHeight / 2.0F, 200f"+SCALE+", 0.1f", "color");
            addCustom(UpgradeHammerImprintEffect.class, "x, y, 200f"+SCALE+", 0.1f", Color.YELLOW);

            addSimpleVFX(FastSmokeParticle.class, 400f, 0.15f);

            addSimpleVFX(MiracleEffect.class, 200f, 1.0f);
            addSimpleVFX(LightBulbEffect.class, 200f, 1.0f);
            addSimpleVFX(VerticalImpactEffect.class, 200f, 1.0f);
            addCustom(AnimatedSlashEffect.class, "x - 32"+SCALE+", y, 200f"+SCALE+", 0.8f, x + 32"+SCALE+", y, 200f"+SCALE+", 0.8f", "color, color2");
            addCustom(ViceCrushEffect.class, "x, y, 200f"+SCALE+", 1.0f, x2, y, 200f"+SCALE+", 1.0", "color, color");

            addSimpleVFX(GlowRelicParticle.class, 50f, 0.05f);
            addCustom(FlameAnimationEffect.class, "nodeHb.cX, nodeHb.cY, 75f"+SCALE+", 0.25f", Color.SCARLET);

            addSimpleVFX(UpgradeShineParticleEffect.class, 50f, 0.5f);
            addSimpleVFX(BossChestShineEffect.class, 50f, 0.5f);
            addCustom(FireFlyEffect.class, "x, y, 120f"+SCALE+"*color.a, 0.5f, x, y, 500f"+SCALE+"*color.a, 0.1f", "color, color");
            addSimpleVFX(ShinySparkleEffect.class, 50f, 0.5f);
            addSimpleVFX(ShineSparkleEffect.class, 50f, 0.5f);
            addSimpleVFX(ImpactSparkEffect.class, 50f, 0.5f);
            addSimpleVFX(DamageImpactLineEffect.class, 50f, 0.1f);
            addSimpleVFX(BlockImpactLineEffect.class, 50f, 0.1f);

            addCustom(UncommonPotionParticleEffect.class, "oX + (hb == null ? x : hb.cX) + img.packedWidth / 2.0F, oY + (hb == null ? y : hb.cY) + img.packedHeight / 2.0F, 40f"+SCALE+",0.25f", "color");
            addCustom(RarePotionParticleEffect.class, "oX + (hb == null ? x : hb.cX) + img.packedWidth / 2.0F, oY + (hb == null ? y : hb.cY) + img.packedHeight / 2.0F, 40f"+SCALE+",0.25f", "color");

            addCustom(BorderFlashEffect.class,
                    "0, 0, 400f"+SCALE+", 0.7f*color.a, " +
                            "0, "+HEIGHT+"/2f, 400f"+SCALE+", 0.7f*color.a, " +
                            "0, "+HEIGHT+", 400f"+SCALE+", 0.7f*color.a, " +
                            WIDTH+"/3f, "+HEIGHT+", 400f"+SCALE+", 0.7f*color.a, " +
                            WIDTH+"*2/3f, "+HEIGHT+", 400f"+SCALE+", 0.7f*color.a, " +
                            WIDTH+", "+HEIGHT+", 400f"+SCALE+", 0.7f*color.a, " +
                            WIDTH+", "+HEIGHT+"/2f, 400f"+SCALE+", 0.7f*color.a, " +
                            WIDTH+", 0, 400f"+SCALE+", 0.7f*color.a, " +
                            WIDTH+"*2/3f, 0, 400f"+SCALE+", 0.7f*color.a, " +
                            WIDTH+"/3f, 0, 400f"+SCALE+", 0.7f*color.a",
                    "color, color, color, color, color, color, color, color, color, color");
            addCustom(BorderLongFlashEffect.class,
                    "0, 0, 400f"+SCALE+", 0.7f*color.a, " +
                            "0, "+HEIGHT+"/2f, 400f"+SCALE+", 0.7f*color.a, " +
                            "0, "+HEIGHT+", 400f"+SCALE+", 0.7f*color.a, " +
                            WIDTH+"/3f, "+HEIGHT+", 400f"+SCALE+", 0.7f*color.a, " +
                            WIDTH+"*2/3f, "+HEIGHT+", 400f"+SCALE+", 0.7f*color.a, " +
                            WIDTH+", "+HEIGHT+", 400f"+SCALE+", 0.7f*color.a, " +
                            WIDTH+", "+HEIGHT+"/2f, 400f"+SCALE+", 0.7f*color.a, " +
                            WIDTH+", 0, 400f"+SCALE+", 0.7f*color.a, " +
                            WIDTH+"*2/3f, 0, 400f"+SCALE+", 0.7f*color.a, " +
                            WIDTH+"/3f, 0, 400f"+SCALE+", 0.7f*color.a",
                    "color, color, color, color, color, color, color, color, color, color");


            for (PatchData data : PATCHES) {
                CtClass ctClass = ctBehavior.getDeclaringClass().getClassPool().get(data.classToPatch.getName());
                try {
                    ctClass.addMethod(CtNewMethod.make(data.xyriMethod, ctClass));
                    ctClass.addMethod(CtNewMethod.make(data.colorMethod, ctClass));
                } catch (CannotCompileException e) {
                    LightsOutMod.logger.warn("Failed to patch class: "+data.classToPatch);
                    e.printStackTrace();
                }
            }
        }
    }
}
