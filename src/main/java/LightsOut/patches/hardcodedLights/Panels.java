package LightsOut.patches.hardcodedLights;

import LightsOut.LightsOutMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.options.OptionsPanel;
import com.megacrit.cardcrawl.vfx.scene.LightFlareLEffect;
import com.megacrit.cardcrawl.vfx.scene.TorchParticleLEffect;

public class Panels {
    @SpirePatch2(clz = OptionsPanel.class, method = "render")
    public static class VisibleOptions {
        private static float particleTimer;
        private static TextureAtlas.AtlasRegion img;
        @SpirePostfixPatch
        public static void lights(SpriteBatch sb) {
            if (LightsOutMod.modEnabled) {
                if (img == null) {
                    img = ImageMaster.vfxAtlas.findRegion("env/torch");
                }
                float x = Settings.WIDTH/2f;
                float y = Settings.HEIGHT/2f - 50f * Settings.scale;
                sb.draw(img, x - (img.packedWidth / 2f), y - (img.packedHeight / 2f) - 24.0F * Settings.yScale, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, 1.0F, 1.0F, 0.0F);
                particleTimer -= Gdx.graphics.getDeltaTime();
                if (particleTimer < 0.0F) {
                    particleTimer = 0.1F;
                    LightsOutMod.managedEffects.add(new TorchParticleLEffect(x, y + 14.0F * Settings.scale));
                    LightsOutMod.managedEffects.add(new LightFlareLEffect(x, y + 14.0F * Settings.scale));
                }
            }
        }
    }
}
