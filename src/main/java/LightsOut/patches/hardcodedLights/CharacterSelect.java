package LightsOut.patches.hardcodedLights;

import LightsOut.patches.CustomLightPatches;
import LightsOut.util.LightData;
import LightsOut.util.ShaderLogic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;

public class CharacterSelect {
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
}
