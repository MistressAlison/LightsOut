package LightsOut.patches.hardcodedLights;

import LightsOut.LightsOutMod;
import LightsOut.util.LightData;
import LightsOut.util.ShaderLogic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.Legend;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import com.megacrit.cardcrawl.vfx.scene.LightFlareLEffect;
import com.megacrit.cardcrawl.vfx.scene.TorchParticleLEffect;

public class Map {
    @SpirePatch2(clz = Legend.class, method = "render")
    public static class LegendLights {
        private static float particleTimer;
        private static TextureAtlas.AtlasRegion img;
        @SpirePostfixPatch
        public static void lights(SpriteBatch sb) {
            if (LightsOutMod.modEnabled && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
                if (img == null) {
                    img = ImageMaster.vfxAtlas.findRegion("env/torch");
                }
                float x = Legend.X + 150f * Settings.scale;
                float y = Legend.Y;
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

    @SpirePatch2(clz = MapRoomNode.class, method = "render")
    public static class VisibleMapPlz {
        private static final float SPACING_X = Settings.xScale * 64.0F * 2;
        private static final float OFFSET_X = 560.0F * Settings.xScale;
        private static final float OFFSET_Y = 180.0F * Settings.scale;
        @SpirePostfixPatch
        public static void lights(MapRoomNode __instance, SpriteBatch sb) {
            if (LightsOutMod.modEnabled && LightsOutMod.glowingMap) {
                Color c = Color.LIGHT_GRAY;
                float rad = 50f*Settings.scale;
                float i = 0.5f;
                if (LightsOutMod.colorfulMap) {
                    Texture t = __instance.room.getMapImg();
                    if (t == ImageMaster.MAP_NODE_ENEMY) {
                        c = Color.SCARLET;
                    } else if (t == ImageMaster.MAP_NODE_ELITE) {
                        c = Color.PURPLE;
                        i += 0.2f;
                        rad *= 1.2f;
                    } else if (t == ImageMaster.MAP_NODE_EVENT) {
                        c = Color.CYAN;
                    } else if (t == ImageMaster.MAP_NODE_MERCHANT) {
                        c = Color.GREEN;
                    } else if (t == ImageMaster.MAP_NODE_TREASURE) {
                        c = Color.GOLD;
                    } else if (t == ImageMaster.MAP_NODE_REST) {
                        c = Color.ORANGE;
                    }
                }
                ShaderLogic.lightsToRender.add(new LightData(__instance.x * SPACING_X + OFFSET_X + __instance.offsetX, __instance.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY + __instance.offsetY, rad, i, c));
            }
        }
    }

    @SpirePatch2(clz = DungeonMap.class, method = "renderBossIcon")
    public static class GlowyBoss {
        private static final float BOSS_OFFSET_Y = 1416.0F * Settings.scale;
        private static final float BOSS_W = 512.0F * Settings.scale;
        @SpirePostfixPatch
        public static void lights(float ___mapOffsetY) {
            if (LightsOutMod.modEnabled && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP && LightsOutMod.glowingMap) {
                ShaderLogic.lightsToRender.add(new LightData(Settings.WIDTH/2f, DungeonMapScreen.offsetY + ___mapOffsetY + BOSS_OFFSET_Y + BOSS_W/2f, 256f*Settings.scale, 0.8f, LightsOutMod.colorfulMap ? Color.RED : Color.WHITE));
            }
        }
    }

    // TODO not enough light slots, lol
    /*@SpirePatch2(clz = MapDot.class, method = "render")
    public static class VisibleDots {
        private static final float OFFSET_Y = 172.0F * Settings.scale;
        @SpirePostfixPatch
        public static void lights(float ___x, float ___y) {
            ShaderLogic.lightsToRender.add(new LightData(___x, ___y + DungeonMapScreen.offsetY + OFFSET_Y, 10f*Settings.scale, .4f, Color.GRAY));
        }
    }*/
}
