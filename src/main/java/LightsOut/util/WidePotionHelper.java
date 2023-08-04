package LightsOut.util;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.widepotions.potions.WidePotion;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class WidePotionHelper {
    public static void checkWide(AbstractPotion p) {
        if (p instanceof WidePotion) {
            WidePotion wp = (WidePotion)p;
            if ((wp.getPotion()).p_effect == AbstractPotion.PotionEffect.RAINBOW) {
                ShaderLogic.lightsToRender.add(new LightData(wp.posX, wp.posY, 225.0F * Settings.scale, 1.25F, (wp.getPotion()).liquidColor));
                ShaderLogic.lightsToRender.add(new LightData(wp.posX + 64.0F * Settings.scale, wp.posY, 225.0F * Settings.scale, 1.25F, (wp.getPotion()).liquidColor));
            } else if (wp.getPotion() instanceof com.megacrit.cardcrawl.potions.FirePotion) {
                ShaderLogic.lightsToRender.add(new LightData(wp.posX, wp.posY, 250.0F * Settings.scale, 1.5F, new Color(1.0F, 0.8F, 0.1F, 1.0F)));
                ShaderLogic.lightsToRender.add(new LightData(wp.posX + 64.0F * Settings.scale, wp.posY, 250.0F * Settings.scale, 1.5F, new Color(1.0F, 0.8F, 0.1F, 1.0F)));
            } else if (wp.getPotion() instanceof com.megacrit.cardcrawl.potions.BottledMiracle) {
                ShaderLogic.lightsToRender.add(new LightData(wp.posX, wp.posY, 200.0F * Settings.scale, 1.25F, Color.YELLOW));
                ShaderLogic.lightsToRender.add(new LightData(wp.posX + 64.0F * Settings.scale, wp.posY, 200.0F * Settings.scale, 0.75F, Color.YELLOW));
            }
        }
    }
}
