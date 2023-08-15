package LightsOut.patches.hardcodedLights;

import LightsOut.LightsOutMod;
import LightsOut.patches.CustomLightPatches;
import LightsOut.util.CustomLightData;
import LightsOut.util.LightData;
import LightsOut.util.ShaderLogic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import com.megacrit.cardcrawl.vfx.scene.LightFlareLEffect;
import com.megacrit.cardcrawl.vfx.scene.TorchParticleLEffect;
import javassist.CtBehavior;

public class Relics {
    // TODO make work with multiple lights
    @SpirePatch2(clz = SingleRelicViewPopup.class, method = "renderRelicImage")
    public static class SRVLights {
        @SpirePostfixPatch
        public static void plz(SingleRelicViewPopup __instance, AbstractRelic ___relic) {
            CustomLightData data = CustomLightPatches.customLights.get(___relic.getClass());
            if (data != null) {
                ShaderLogic.lightsToRender.add(new LightData(Settings.WIDTH/2f, Settings.HEIGHT/2f, data.getLightData(___relic).get(0).radius*2f, data.getLightData(___relic).get(0).intensity, data.getLightData(___relic).get(0).color));
            }
        }
    }


    @SpirePatch2(clz = AbstractRelic.class, method = "renderTip")
    public static class RelicViewScreenLights {
        private static float particleTimer;
        private static TextureAtlas.AtlasRegion img;
        @SpireInsertPatch(locator = Locator.class)
        public static void lights(SpriteBatch sb) {
            if (LightsOutMod.modEnabled) {
                if (img == null) {
                    img = ImageMaster.vfxAtlas.findRegion("env/torch");
                }
                float x = 180.0F * Settings.scale;
                float y = Settings.HEIGHT * 0.7F;
                sb.draw(img, x - (img.packedWidth / 2f), y - (img.packedHeight / 2f) - 24.0F * Settings.yScale, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, 1.0F, 1.0F, 0.0F);
                particleTimer -= Gdx.graphics.getDeltaTime();
                if (particleTimer < 0.0F) {
                    particleTimer = 0.1F;
                    LightsOutMod.managedEffects.add(new TorchParticleLEffect(x, y + 14.0F * Settings.scale));
                    LightsOutMod.managedEffects.add(new LightFlareLEffect(x, y + 14.0F * Settings.scale));
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(TipHelper.class, "queuePowerTips");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }
}
