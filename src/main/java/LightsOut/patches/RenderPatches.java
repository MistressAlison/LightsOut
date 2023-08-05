package LightsOut.patches;

import LightsOut.util.LightData;
import LightsOut.util.ShaderLogic;
import LightsOut.util.WidePotionHelper;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.scenes.TheCityScene;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.AwakenedEyeParticle;
import com.megacrit.cardcrawl.vfx.BossChestShineEffect;
import com.megacrit.cardcrawl.vfx.FireBurstParticleEffect;
import com.megacrit.cardcrawl.vfx.GhostlyFireEffect;
import com.megacrit.cardcrawl.vfx.GhostlyWeakFireEffect;
import com.megacrit.cardcrawl.vfx.GlowRelicParticle;
import com.megacrit.cardcrawl.vfx.GlowyFireEyesEffect;
import com.megacrit.cardcrawl.vfx.RarePotionParticleEffect;
import com.megacrit.cardcrawl.vfx.RoomShineEffect;
import com.megacrit.cardcrawl.vfx.RoomShineEffect2;
import com.megacrit.cardcrawl.vfx.StaffFireEffect;
import com.megacrit.cardcrawl.vfx.TorchHeadFireEffect;
import com.megacrit.cardcrawl.vfx.UncommonPotionParticleEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireBurningEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireEndingBurningEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.GiantFireEffect;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;
import com.megacrit.cardcrawl.vfx.combat.LightBulbEffect;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.LightRayFlyOutEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbPassiveEffect;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import com.megacrit.cardcrawl.vfx.combat.MiracleEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateParticle;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbPassiveEffect;
import com.megacrit.cardcrawl.vfx.combat.RedFireBurstParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import com.megacrit.cardcrawl.vfx.scene.FireFlyEffect;
import com.megacrit.cardcrawl.vfx.scene.IroncladVictoryFlameEffect;
import com.megacrit.cardcrawl.vfx.scene.LogoFlameEffect;
import com.megacrit.cardcrawl.vfx.scene.ShinySparkleEffect;
import com.megacrit.cardcrawl.vfx.scene.SlowFireParticleEffect;
import com.megacrit.cardcrawl.vfx.scene.TorchParticleLEffect;
import com.megacrit.cardcrawl.vfx.scene.TorchParticleMEffect;
import com.megacrit.cardcrawl.vfx.scene.TorchParticleSEffect;
import com.megacrit.cardcrawl.vfx.scene.TorchParticleXLEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;

public class RenderPatches {
    @SpirePatches2({@SpirePatch2(clz = GhostlyFireEffect.class, method = "render"), @SpirePatch2(clz = GhostlyWeakFireEffect.class, method = "render")})
    public static class AddLightsHexaghost {
        @SpirePostfixPatch
        public static void add(AbstractGameEffect __instance) {
            Color c = (Color)ReflectionHacks.getPrivate(__instance, AbstractGameEffect.class, "color");
            if (c == null)
                throw new RuntimeException(__instance.getClass().getName());
            ShaderLogic.lightsToRender.add(new LightData((
                    (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "x")).floatValue(), (
                    (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "y")).floatValue(), ShaderLogic.TORCH_RADIUS * 2.0F, 0.03F, c));
        }
    }

    @SpirePatches2({@SpirePatch2(clz = TorchParticleSEffect.class, method = "render"), @SpirePatch2(clz = TorchParticleMEffect.class, method = "render"), @SpirePatch2(clz = TorchParticleLEffect.class, method = "render"), @SpirePatch2(clz = TorchParticleXLEffect.class, method = "render"), @SpirePatch2(clz = StaffFireEffect.class, method = "render"), @SpirePatch2(clz = GlowyFireEyesEffect.class, method = "render"), @SpirePatch2(clz = FireBurstParticleEffect.class, method = "render"), @SpirePatch2(clz = TorchHeadFireEffect.class, method = "render"), @SpirePatch2(clz = GiantFireEffect.class, method = "render"), @SpirePatch2(clz = LightRayFlyOutEffect.class, method = "render"), @SpirePatch2(clz = CampfireBurningEffect.class, method = "render"), @SpirePatch2(clz = CampfireEndingBurningEffect.class, method = "render"), @SpirePatch2(clz = FlameParticleEffect.class, method = "render"), @SpirePatch2(clz = LightningOrbPassiveEffect.class, method = "render"), @SpirePatch2(clz = LightningOrbActivateEffect.class, method = "render"), @SpirePatch2(clz = PlasmaOrbActivateParticle.class, method = "render"), @SpirePatch2(clz = PlasmaOrbPassiveEffect.class, method = "render"), @SpirePatch2(clz = RedFireBurstParticleEffect.class, method = "render"), @SpirePatch2(clz = IroncladVictoryFlameEffect.class, method = "render"), @SpirePatch2(clz = SlowFireParticleEffect.class, method = "render"), @SpirePatch2(clz = StanceAuraEffect.class, method = "render"), @SpirePatch2(clz = RoomShineEffect.class, method = "render"), @SpirePatch2(clz = RoomShineEffect2.class, method = "render")})
    public static class AddLightsGenericLow {
        @SpirePostfixPatch
        public static void add(AbstractGameEffect __instance) {
            Color c = (Color)ReflectionHacks.getPrivate(__instance, AbstractGameEffect.class, "color");
            if (c == null)
                throw new RuntimeException(__instance.getClass().getName() + " failed to get Color");
            ShaderLogic.lightsToRender.add(new LightData((
                    (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "x")).floatValue(), (
                    (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "y")).floatValue(), ShaderLogic.TORCH_RADIUS, 0.1F, c));
        }
    }

    @SpirePatches2({@SpirePatch2(clz = MiracleEffect.class, method = "render"), @SpirePatch2(clz = LightBulbEffect.class, method = "render")})
    public static class AddLightsGenericHigh {
        @SpirePostfixPatch
        public static void add(AbstractGameEffect __instance) {
            Color c = (Color)ReflectionHacks.getPrivate(__instance, AbstractGameEffect.class, "color");
            if (c == null)
                throw new RuntimeException(__instance.getClass().getName());
            ShaderLogic.lightsToRender.add(new LightData((
                    (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "x")).floatValue(), (
                    (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "y")).floatValue(), ShaderLogic.TORCH_RADIUS, 1.0F, c));
        }
    }

    @SpirePatch2(clz = GlowRelicParticle.class, method = "render")
    public static class AddLightsShineLow {
        @SpirePostfixPatch
        public static void add(AbstractGameEffect __instance) {
            Color c = (Color)ReflectionHacks.getPrivate(__instance, AbstractGameEffect.class, "color");
            ShaderLogic.lightsToRender.add(new LightData((
                    (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "x")).floatValue(), (
                    (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "y")).floatValue(), ShaderLogic.SHINE_RADIUS, 0.05F, c));
        }
    }

    @SpirePatches2({@SpirePatch2(clz = BossChestShineEffect.class, method = "render"), @SpirePatch2(clz = FireFlyEffect.class, method = "render"), @SpirePatch2(clz = ShinySparkleEffect.class, method = "render"), @SpirePatch2(clz = RarePotionParticleEffect.class, method = "render"), @SpirePatch2(clz = UncommonPotionParticleEffect.class, method = "render")})
    public static class AddLightsShine {
        @SpirePostfixPatch
        public static void add(AbstractGameEffect __instance) {
            Color c = (Color)ReflectionHacks.getPrivate(__instance, AbstractGameEffect.class, "color");
            ShaderLogic.lightsToRender.add(new LightData((
                    (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "x")).floatValue(), (
                    (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "y")).floatValue(), ShaderLogic.SHINE_RADIUS, 0.5F, c));
        }
    }

    @SpirePatch2(clz = AwakenedEyeParticle.class, method = "render")
    public static class AddLightsAwakenedEyes {
        @SpirePostfixPatch
        public static void add(AbstractGameEffect __instance) {
            Color c = (Color)ReflectionHacks.getPrivate(__instance, AbstractGameEffect.class, "color");
            if (c == null)
                throw new RuntimeException(__instance.getClass().getName());
            ShaderLogic.lightsToRender.add(new LightData((
                    (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "x")).floatValue() + ImageMaster.ROOM_SHINE_2.packedWidth / 2.0F, (
                    (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "y")).floatValue() + ImageMaster.ROOM_SHINE_2.packedHeight / 2.0F, ShaderLogic.TORCH_RADIUS, 0.1F, c));
        }
    }

    @SpirePatches2({@SpirePatch2(clz = LightningEffect.class, method = "render"), @SpirePatch2(clz = MindblastEffect.class, method = "render"), @SpirePatch2(clz = LaserBeamEffect.class, method = "render")})
    public static class AddLightsFullBright {
        @SpirePostfixPatch
        public static void add(AbstractGameEffect __instance) {
            Color c = (Color)ReflectionHacks.getPrivate(__instance, AbstractGameEffect.class, "color");
            if (c == null)
                throw new RuntimeException(__instance.getClass().getName());
            ShaderLogic.lightsToRender.add(new LightData((
                    (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "x")).floatValue(), (
                    (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "y")).floatValue(), ShaderLogic.TORCH_RADIUS * 100.0F, 2.0F, c));
        }
    }

    @SpirePatch2(clz = SmallLaserEffect.class, method = "render")
    public static class AddLightsSmallLaser {
        @SpirePostfixPatch
        public static void add(AbstractGameEffect __instance) {
            Color c = (Color)ReflectionHacks.getPrivate(__instance, AbstractGameEffect.class, "color");
            float startX = ((Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "sX")).floatValue();
            float endX = ((Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "dX")).floatValue();
            float startY = ((Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "sY")).floatValue();
            float endY = ((Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "dY")).floatValue();
            Vector2 step = new Vector2(endX - startX, endY - startY);
            for (int i = 0; i < 10; i++)
                ShaderLogic.lightsToRender.add(new LightData(startX + i * step.x / 10.0F, startY + i * step.y / 10.0F, ShaderLogic.TORCH_RADIUS, 0.5F, c));
        }
    }

    @SpirePatch2(clz = LightFlareParticleEffect.class, method = "render")
    public static class AddLightsFlareParticle {
        @SpirePostfixPatch
        public static void add(LightFlareParticleEffect __instance) {
            Vector2 vec = (Vector2)ReflectionHacks.getPrivate(__instance, LightFlareParticleEffect.class, "pos");
            Color c = (Color)ReflectionHacks.getPrivate(__instance, AbstractGameEffect.class, "color");
            ShaderLogic.lightsToRender.add(new LightData(vec.x, vec.y, ShaderLogic.TORCH_RADIUS, 0.1F, c));
        }
    }

    @SpirePatch2(clz = FlashAtkImgEffect.class, method = "render")
    public static class AddLightsFireDamage {
        @SpirePostfixPatch
        public static void add(FlashAtkImgEffect __instance) {
            if (__instance.img == ImageMaster.ATK_FIRE) {
                Color c = Color.RED;
                ShaderLogic.lightsToRender.add(new LightData((
                        (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "x")).floatValue(), (
                        (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "y")).floatValue(), ShaderLogic.TORCH_RADIUS, 1.0F, c));
            }
        }
    }

    @SpirePatch2(clz = LogoFlameEffect.class, method = "render", paramtypez = {SpriteBatch.class, float.class, float.class})
    public static class AddLightsMainMenu {
        private static final Color TITLE_COLOR = new Color(0.7F, 0.8F, 1.0F, 1.0F);

        @SpirePostfixPatch
        public static void add(AbstractGameEffect __instance, float x, float y) {
            if (shouldRenderFire())
                ShaderLogic.lightsToRender.add(new LightData(x + 15.0F * Settings.scale + (
                        (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "offsetX")).floatValue(), y + (
                        (Float)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "offsetY")).floatValue(), ShaderLogic.TORCH_RADIUS * 2.0F, 0.08F, TITLE_COLOR));
        }

        private static boolean shouldRenderFire() {
            return (!CardCrawlGame.isInARun() && CardCrawlGame.mainMenuScreen.screen != MainMenuScreen.CurScreen.DOOR_UNLOCK && (CardCrawlGame.mainMenuScreen.screen != MainMenuScreen.CurScreen.CHAR_SELECT ||

                    !CardCrawlGame.mainMenuScreen.charSelectScreen.options.stream().anyMatch(o -> o.selected)) && !BaseMod.modSettingsUp);
        }
    }

    @SpirePatch2(clz = TheCityScene.class, method = "renderCombatRoomFg")
    public static class AddLightsCityFG {
        private static final Color FG2_COLOR = new Color(1.0F, 0.8F, 0.2F, 1.0F);

        @SpirePostfixPatch
        public static void plz(TheCityScene __instance, boolean ___renderFg2) {
            if (!___renderFg2)
                ShaderLogic.lightsToRender.add(new LightData(1848.0F * Settings.xScale, 314.0F * Settings.yScale, 250.0F, 1.0F, FG2_COLOR));
        }
    }

    @SpirePatch2(clz = TheCityScene.class, method = "renderCombatRoomBg")
    public static class AddLightsCityBG {
        private static final Color BG_COLOR = new Color(1.0F, 0.5F, 0.2F, 1.0F);

        private static final Color MG_COLOR = new Color(1.0F, 1.0F, 0.2F, 1.0F);

        @SpirePostfixPatch
        public static void plz(TheCityScene __instance, boolean ___renderMgAlt, boolean ___renderMgGlow, Color ___whiteColor) {
            ShaderLogic.lightsToRender.add(new LightData(Settings.WIDTH / 2.0F, (Settings.HEIGHT * 3) / 5.0F, Settings.HEIGHT, 0.05F, BG_COLOR));
            if (!___renderMgAlt) {
                ShaderLogic.lightsToRender.add(new LightData(199.0F * Settings.xScale, 421.0F * Settings.yScale, 300.0F, 1.2F, MG_COLOR));
                if (___renderMgGlow)
                    ShaderLogic.lightsToRender.add(new LightData(199.0F * Settings.xScale, 421.0F * Settings.yScale, 400.0F, ___whiteColor.a, MG_COLOR));
            }
        }
    }

    @SpirePatches2({@SpirePatch2(clz = AbstractRelic.class, method = "renderInTopPanel"), @SpirePatch2(clz = AbstractRelic.class, method = "render", paramtypez = {SpriteBatch.class}), @SpirePatch2(clz = AbstractRelic.class, method = "render", paramtypez = {SpriteBatch.class, boolean.class, Color.class}), @SpirePatch2(clz = AbstractRelic.class, method = "renderWithoutAmount")})
    public static class AddLightsToRelics {
        private static final Color LANTERN_COLOR = new Color(1.0F, 0.96F, 0.87F, 1.0F);

        private static final Color FUSION_HAMMER_COLOR = new Color(1.0F, 0.4F, 0.8F, 1.0F);

        private static final Color HOLY_WATER_COLOR = new Color(1.0F, 0.6F, 0.9F, 1.0F);

        private static final Color NUCLEAR_BATTERY_COLOR = new Color(0.7F, 0.7F, 1.0F, 1.0F);

        private static final Color BOTTLE_FLAME_COLOR = new Color(1.0F, 0.6F, 0.2F, 1.0F);

        private static final Color BOTTLE_LIGHTNING_COLOR = new Color(1.0F, 1.0F, 0.0F, 1.0F);

        private static final Color BLUE_CANDLE_COLOR = new Color(0.4F, 0.0F, 1.0F, 1.0F);

        @SpirePostfixPatch
        public static void add(AbstractRelic __instance) {
            if (__instance instanceof com.megacrit.cardcrawl.relics.Lantern) {
                ShaderLogic.lightsToRender.add(new LightData(__instance.currentX, __instance.currentY, ShaderLogic.TORCH_RADIUS, 2.5F, LANTERN_COLOR));
            } else if (__instance instanceof com.megacrit.cardcrawl.relics.FusionHammer) {
                ShaderLogic.lightsToRender.add(new LightData(__instance.currentX, __instance.currentY, ShaderLogic.TORCH_RADIUS, 1.0F, FUSION_HAMMER_COLOR));
            } else if (__instance instanceof com.megacrit.cardcrawl.relics.HolyWater) {
                ShaderLogic.lightsToRender.add(new LightData(__instance.currentX, __instance.currentY, ShaderLogic.TORCH_RADIUS, 1.0F, HOLY_WATER_COLOR));
            } else if (__instance instanceof com.megacrit.cardcrawl.relics.NuclearBattery) {
                ShaderLogic.lightsToRender.add(new LightData(__instance.currentX, __instance.currentY, ShaderLogic.TORCH_RADIUS, 2.0F, NUCLEAR_BATTERY_COLOR));
            } else if (__instance instanceof com.megacrit.cardcrawl.relics.BottledFlame) {
                ShaderLogic.lightsToRender.add(new LightData(__instance.currentX, __instance.currentY, ShaderLogic.TORCH_RADIUS, 1.5F, BOTTLE_FLAME_COLOR));
            } else if (__instance instanceof com.megacrit.cardcrawl.relics.BottledLightning) {
                ShaderLogic.lightsToRender.add(new LightData(__instance.currentX, __instance.currentY, ShaderLogic.TORCH_RADIUS, 1.8F, BOTTLE_LIGHTNING_COLOR));
            } else if (__instance instanceof com.megacrit.cardcrawl.relics.BlueCandle) {
                ShaderLogic.lightsToRender.add(new LightData(__instance.currentX, __instance.currentY, ShaderLogic.TORCH_RADIUS, 1.0F, BLUE_CANDLE_COLOR));
            } else {
                CustomLightPatches.processCustomLights(__instance);
            }
        }
    }

    @SpirePatches2({@SpirePatch2(clz = Lightning.class, method = "render"), @SpirePatch2(clz = Plasma.class, method = "render"), @SpirePatch2(clz = Dark.class, method = "render")})
    public static class AddLightsToOrbs {
        private static final Color LIGHTNING_COLOR = Color.YELLOW.cpy();

        private static final Color PLASMA_COLOR = new Color(0.7F, 0.7F, 1.0F, 1.0F);

        private static final Color DARK_COLOR = new Color(0.5F, 1.0F, 0.5F, 1.0F);

        @SpirePostfixPatch
        public static void add(AbstractOrb __instance) {
            if (__instance instanceof Lightning) {
                ShaderLogic.lightsToRender.add(new LightData(__instance.cX, __instance.cY, ShaderLogic.TORCH_RADIUS, 1.5F, LIGHTNING_COLOR));
            } else if (__instance instanceof Plasma) {
                ShaderLogic.lightsToRender.add(new LightData(__instance.cX, __instance.cY, ShaderLogic.TORCH_RADIUS, 1.8F, PLASMA_COLOR));
            } else if (__instance instanceof Dark) {
                ShaderLogic.lightsToRender.add(new LightData(__instance.cX, __instance.cY, ShaderLogic.TORCH_RADIUS, -1.0F, DARK_COLOR));
            }
        }
    }

    @SpirePatch2(clz = CharacterOption.class, method = "render")
    public static class AddLightsToCharSelect {
        private static final float EYE_X = 1350.0F * Settings.xScale;

        private static final float EYE_Y = 634.0F * Settings.yScale;

        private static final float MAX_X = 10.0F * Settings.xScale;

        private static final float MAX_Y = 5.0F * Settings.yScale;

        @SpirePostfixPatch
        public static void add(CharacterOption __instance) {
            if (__instance.selected)
                if (__instance.c.chosenClass == AbstractPlayer.PlayerClass.IRONCLAD) {
                    Vector2 dir = new Vector2(InputHelper.mX - EYE_X, InputHelper.mY - EYE_Y);
                    float len = Math.min(1.0F, dir.len() / 500.0F);
                    dir.nor();
                    float dx = dir.x * len * MAX_X;
                    float dy = dir.y * len * MAX_Y;
                    ShaderLogic.lightsToRender.add(new LightData(EYE_X + dx, EYE_Y + dy, ShaderLogic.SHINE_RADIUS / 2.0F, 1.5F, Color.RED));
                    ShaderLogic.lightsToRender.add(new LightData(103.0F * Settings.xScale, 774.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS, 0.25F, Color.RED));
                    ShaderLogic.lightsToRender.add(new LightData(298.0F * Settings.xScale, 933.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS, 0.25F, Color.RED));
                    ShaderLogic.lightsToRender.add(new LightData(417.0F * Settings.xScale, 644.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS, 0.5F, Color.RED));
                    ShaderLogic.lightsToRender.add(new LightData(389.0F * Settings.xScale, 548.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS, 0.25F, Color.RED));
                    ShaderLogic.lightsToRender.add(new LightData(185.0F * Settings.xScale, 496.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS, 0.125F, Color.RED));
                    ShaderLogic.lightsToRender.add(new LightData(280.0F * Settings.xScale, 285.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS, 0.25F, Color.RED));
                    ShaderLogic.lightsToRender.add(new LightData(1062.0F * Settings.xScale, 99.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS, 0.5F, Color.RED));
                    ShaderLogic.lightsToRender.add(new LightData(925.0F * Settings.xScale, 304.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS, 0.5F, Color.RED));
                    ShaderLogic.lightsToRender.add(new LightData(804.0F * Settings.xScale, 1035.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS, 0.5F, Color.RED));
                    ShaderLogic.lightsToRender.add(new LightData(821.0F * Settings.xScale, 461.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS, 0.125F, Color.RED));
                    ShaderLogic.lightsToRender.add(new LightData(727.0F * Settings.xScale, 579.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS, 0.125F, Color.RED));
                    ShaderLogic.lightsToRender.add(new LightData(821.0F * Settings.xScale, 679.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS, 0.25F, Color.RED));
                    ShaderLogic.lightsToRender.add(new LightData(760.0F * Settings.xScale, 829.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS, 0.25F, Color.RED));
                    ShaderLogic.lightsToRender.add(new LightData(599.0F * Settings.xScale, 802.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS, 0.25F, Color.RED));
                } else if (__instance.c.chosenClass == AbstractPlayer.PlayerClass.THE_SILENT) {
                    ShaderLogic.lightsToRender.add(new LightData(1515.0F * Settings.xScale, 650.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS / 4.0F, 1.5F, Color.GREEN));
                    ShaderLogic.lightsToRender.add(new LightData(1790.0F * Settings.xScale, 650.0F * Settings.yScale, ShaderLogic.SHINE_RADIUS / 4.0F, 1.5F, Color.GREEN));
                } else if (__instance.c.chosenClass == AbstractPlayer.PlayerClass.DEFECT) {
                    ShaderLogic.lightsToRender.add(new LightData(1065.0F * Settings.xScale, 575.0F * Settings.yScale, ShaderLogic.TORCH_RADIUS * 3.0F, 1.5F, Color.YELLOW));
                } else if (__instance.c.chosenClass == AbstractPlayer.PlayerClass.WATCHER) {
                    ShaderLogic.lightsToRender.add(new LightData(1720.0F * Settings.xScale, 885.0F * Settings.yScale, ShaderLogic.TORCH_RADIUS * 3.0F, 1.5F, Color.PURPLE));
                } else {
                    CustomLightPatches.processCustomCharacterSelectLights(__instance.c);
                }
        }
    }

    @SpirePatch2(clz = AbstractCard.class, method = "renderImage")
    public static class AddLightsToCards {
        @SpirePostfixPatch
        public static void plz(AbstractCard __instance) {
            CustomLightPatches.processCustomLights(__instance);
        }
    }

    @SpirePatches2({@SpirePatch2(clz = AbstractPotion.class, method = "render"), @SpirePatch2(clz = AbstractPotion.class, method = "shopRender"), @SpirePatch2(clz = AbstractPotion.class, method = "labRender")})
    public static class AddLightsToPotions {
        private static final boolean wideLoaded = Loader.isModLoaded("widepotions");

        @SpirePostfixPatch
        public static void plz(AbstractPotion __instance) {
            if (wideLoaded)
                WidePotionHelper.checkWide(__instance);
            if (__instance.p_effect == AbstractPotion.PotionEffect.RAINBOW) {
                ShaderLogic.lightsToRender.add(new LightData(__instance.posX, __instance.posY, 125.0F * Settings.scale, 1.0F, __instance.liquidColor));
            } else if (__instance instanceof com.megacrit.cardcrawl.potions.FirePotion) {
                ShaderLogic.lightsToRender.add(new LightData(__instance.posX, __instance.posY, 150.0F * Settings.scale, 1.3F, new Color(1.0F, 0.8F, 0.1F, 1.0F)));
            } else if (__instance instanceof com.megacrit.cardcrawl.potions.BottledMiracle) {
                ShaderLogic.lightsToRender.add(new LightData(__instance.posX, __instance.posY, 100.0F * Settings.scale, 0.6F, Color.YELLOW));
            } else {
                CustomLightPatches.processCustomLights(__instance);
            }
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "render")
    public static class CustomLightPlayer {
        @SpirePostfixPatch
        public static void plz(AbstractPlayer __instance) {
            CustomLightPatches.processCustomLights(__instance);
            if (((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoom) && !__instance.isDead)
                for (AbstractOrb o : __instance.orbs)
                    CustomLightPatches.processCustomLights(o);
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "render")
    public static class CustomLightDungeon {
        @SpirePostfixPatch
        public static void plz(AbstractDungeon __instance) {
            for (AbstractGameEffect e : AbstractDungeon.effectList)
                CustomLightPatches.processCustomLights(e);
            for (AbstractGameEffect e : AbstractDungeon.topLevelEffects)
                CustomLightPatches.processCustomLights(e);
        }
    }

    @SpirePatch2(clz = MonsterGroup.class, method = "render")
    public static class CustomLightEntity {
        @SpirePostfixPatch
        public static void plz(MonsterGroup __instance) {
            for (AbstractMonster m : __instance.monsters) {
                if (!m.isDeadOrEscaped()) {
                    CustomLightPatches.processCustomLights(m);
                }
            }
        }
    }
}
