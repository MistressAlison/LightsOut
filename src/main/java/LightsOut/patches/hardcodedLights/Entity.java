package LightsOut.patches.hardcodedLights;

import LightsOut.util.ColorUtil;
import LightsOut.util.LightData;
import LightsOut.util.ShaderLogic;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BobEffect;

public class Entity {
    @SpirePatch2(clz = AbstractMonster.class, method = "renderIntent")
    public static class IntentLight {
        @SpirePostfixPatch
        public static void lights(AbstractMonster __instance, BobEffect ___bobEffect) {
            Color c = Color.WHITE;
            switch (__instance.intent) {
                case ATTACK:
                case ATTACK_DEFEND:
                case ESCAPE:
                    c = Color.RED;
                    break;
                case BUFF:
                case ATTACK_BUFF:
                case DEFEND_BUFF:
                case DEFEND:
                    c = ColorUtil.AZURE;
                    break;
                case DEBUFF:
                case ATTACK_DEBUFF:
                case DEFEND_DEBUFF:
                    c = Color.CHARTREUSE;
                    break;
                case STRONG_DEBUFF:
                case MAGIC:
                    c = Color.PURPLE;
                    break;
                case DEBUG:
                case NONE:
                    c = ColorUtil.TRANSPARENT;
                    break;
                case SLEEP:
                case STUN:
                case UNKNOWN:
                    c = Color.YELLOW;
                    break;
                default:
                    break;
            }
            ShaderLogic.lightsToRender.add(new LightData(__instance.intentHb.cX, __instance.intentHb.cY + ___bobEffect.y, 50f * Settings.scale, 1.0f, c));
        }
    }
}
