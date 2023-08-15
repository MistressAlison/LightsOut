package LightsOut.patches.hardcodedLights;

import LightsOut.util.LightData;
import LightsOut.util.ShaderLogic;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.scenes.TheCityScene;

public class Backgrounds {
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
}
