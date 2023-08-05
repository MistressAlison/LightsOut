package LightsOut.patches;

import LightsOut.util.CustomLightData;
import LightsOut.util.LightData;
import LightsOut.util.ShaderLogic;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.widepotions.potions.WidePotion;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
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
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.scenes.TheCityScene;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import com.megacrit.cardcrawl.vfx.scene.LogoFlameEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtNewMethod;

public class RenderPatches {
    @SpirePatch2(clz = SmallLaserEffect.class, method = "render")
    public static class AddLightsSmallLaser {
        @SpirePostfixPatch
        public static void add(AbstractGameEffect __instance, Color ___color, float ___sX, float ___sY, float ___dX, float ___dY) {
            Vector2 step = new Vector2(___dX - ___sX, ___dY - ___sY);
            for (int i = 0; i < 10; i++) {
                ShaderLogic.lightsToRender.add(new LightData(___sX + i * step.x / 10.0F, ___sY + i * step.y / 10.0F, ShaderLogic.TORCH_RADIUS, 0.5F, ___color));
            }
        }
    }

    @SpirePatch2(clz = FlashAtkImgEffect.class, method = "render")
    public static class AddLightsFireDamage {
        private static final Color FIRE_COLOR = new Color(1.0f, 0.8f, 0.3f, 1.0f);

        @SpirePostfixPatch
        public static void add(FlashAtkImgEffect __instance, float ___x, float ___y) {
            if (__instance.img == ImageMaster.ATK_FIRE) {
                ShaderLogic.lightsToRender.add(new LightData(___x, ___y, ShaderLogic.TORCH_RADIUS, 1.0F, FIRE_COLOR));
            }
        }
    }

    @SpirePatch2(clz = LogoFlameEffect.class, method = "render", paramtypez = {SpriteBatch.class, float.class, float.class})
    public static class AddLightsMainMenu {
        private static final Color TITLE_COLOR = new Color(0.7F, 0.8F, 1.0F, 1.0F);

        @SpirePostfixPatch
        public static void add(AbstractGameEffect __instance, float x, float y, float ___offsetX, float ___offsetY) {
            if (shouldRenderFire())
                ShaderLogic.lightsToRender.add(new LightData(
                        x + 15.0F * Settings.scale + ___offsetX, y + ___offsetY,
                        ShaderLogic.TORCH_RADIUS * 2.0F, 0.08F, TITLE_COLOR));
        }

        private static boolean shouldRenderFire() {
            return !CardCrawlGame.isInARun() &&
                    CardCrawlGame.mainMenuScreen.screen != MainMenuScreen.CurScreen.DOOR_UNLOCK &&
                    (CardCrawlGame.mainMenuScreen.screen != MainMenuScreen.CurScreen.CHAR_SELECT || CardCrawlGame.mainMenuScreen.charSelectScreen.options.stream().noneMatch(o -> o.selected)) &&
                    !BaseMod.modSettingsUp;
        }
    }

    @SpirePatch2(clz = TheCityScene.class, method = "renderCombatRoomFg")
    public static class AddLightsCityFG {
        private static final Color FG2_COLOR = new Color(1.0F, 0.8F, 0.2F, 1.0F);

        @SpirePostfixPatch
        public static void plz(TheCityScene __instance, boolean ___renderFg2) {
            if (!___renderFg2) {
                ShaderLogic.lightsToRender.add(new LightData(1848.0F * Settings.xScale, 314.0F * Settings.yScale, 250.0F, 1.0F, FG2_COLOR));
            }

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
                if (___renderMgGlow) {
                    ShaderLogic.lightsToRender.add(new LightData(199.0F * Settings.xScale, 421.0F * Settings.yScale, 400.0F, ___whiteColor.a, MG_COLOR));
                }
            }
        }
    }

    @SpirePatch2(clz = AbstractRelic.class, method = "renderInTopPanel")
    @SpirePatch2(clz = AbstractRelic.class, method = "render", paramtypez = {SpriteBatch.class})
    @SpirePatch2(clz = AbstractRelic.class, method = "render", paramtypez = {SpriteBatch.class, boolean.class, Color.class})
    @SpirePatch2(clz = AbstractRelic.class, method = "renderWithoutAmount")
    public static class AddLightsToRelics {
        @SpirePostfixPatch
        public static void add(AbstractRelic __instance) {
            CustomLightPatches.processCustomLights(__instance);
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
            if (__instance.selected) {
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
    }

    @SpirePatch2(clz = AbstractCard.class, method = "renderImage")
    public static class AddLightsToCards {
        @SpirePostfixPatch
        public static void plz(AbstractCard __instance) {
            CustomLightPatches.processCustomLights(__instance);
        }
    }

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

    @SpirePatch2(clz = AbstractPotion.class, method = "render")
    @SpirePatch2(clz = AbstractPotion.class, method = "shopRender")
    @SpirePatch2(clz = AbstractPotion.class, method = "labRender")
    public static class AddLightsToPotions {
        @SpirePostfixPatch
        public static void plz(AbstractPotion __instance) {
            CustomLightPatches.processCustomLights(__instance);
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "render")
    public static class CustomLightPlayer {
        @SpirePostfixPatch
        public static void plz(AbstractPlayer __instance) {
            CustomLightPatches.processCustomLights(__instance);
            if (((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof MonsterRoom) && !__instance.isDead) {
                for (AbstractOrb o : __instance.orbs) {
                    CustomLightPatches.processCustomLights(o);
                }
            }
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "render")
    public static class CustomLightDungeon {
        @SpirePostfixPatch
        public static void plz(AbstractDungeon __instance) {
            for (AbstractGameEffect e : AbstractDungeon.effectList) {
                CustomLightPatches.processCustomLights(e);
            }
            for (AbstractGameEffect e : AbstractDungeon.topLevelEffects) {
                CustomLightPatches.processCustomLights(e);
            }
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
