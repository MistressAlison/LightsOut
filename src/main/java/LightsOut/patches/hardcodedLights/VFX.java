package LightsOut.patches.hardcodedLights;

import LightsOut.util.LightData;
import LightsOut.util.ShaderLogic;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;
import com.megacrit.cardcrawl.vfx.scene.LogoFlameEffect;

public class VFX {
    @SpirePatch2(clz = SweepingBeamEffect.class, method = "render")
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
                        ShaderLogic.TORCH_RADIUS * 2.0F, 0.06F, TITLE_COLOR));
        }

        private static boolean shouldRenderFire() {
            return !CardCrawlGame.isInARun() &&
                    CardCrawlGame.mainMenuScreen.screen != MainMenuScreen.CurScreen.DOOR_UNLOCK &&
                    CardCrawlGame.mainMenuScreen.screen != MainMenuScreen.CurScreen.SETTINGS &&
                    CardCrawlGame.mainMenuScreen.screen != MainMenuScreen.CurScreen.RELIC_VIEW &&
                    CardCrawlGame.mainMenuScreen.screen != MainMenuScreen.CurScreen.POTION_VIEW &&
                    (CardCrawlGame.mainMenuScreen.screen != MainMenuScreen.CurScreen.CHAR_SELECT || CardCrawlGame.mainMenuScreen.charSelectScreen.options.stream().noneMatch(o -> o.selected)) &&
                    !BaseMod.modSettingsUp;
        }
    }
}
