package LightsOut.patches;

import LightsOut.LightsOutMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.FlameAnimationEffect;

import java.util.ArrayList;

public class RenderPatches {
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

    @SpirePatch2(clz = AbstractCard.class, method = "renderImage")
    public static class AddLightsToCards {
        @SpirePostfixPatch
        public static void plz(AbstractCard __instance) {
            CustomLightPatches.processCustomLights(__instance);
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
                for (AbstractPower p : __instance.powers) {
                    CustomLightPatches.processCustomLights(p);
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
                if (!m.isDeadOrEscaped() && !AbstractDungeon.isScreenUp) {
                    CustomLightPatches.processCustomLights(m);
                    for (AbstractPower p : m.powers) {
                        CustomLightPatches.processCustomLights(p);
                    }
                    if (LightsOutMod.glowingIntents) {
                        for (AbstractGameEffect e : ReflectionHacks.<ArrayList<AbstractGameEffect>>getPrivate(m, AbstractMonster.class, "intentVfx")) {
                            CustomLightPatches.processCustomLights(e);
                        }
                    }
                }
            }
        }
    }

    @SpirePatch2(clz = CampfireUI.class, method = "renderFire")
    public static class CampfireLights {
        @SpirePostfixPatch
        public static void getLights(CampfireUI __instance, ArrayList<AbstractGameEffect> ___flameEffect) {
            for (AbstractGameEffect e : ___flameEffect) {
                CustomLightPatches.processCustomLights(e);
            }
        }
    }

    @SpirePatch2(clz = MapRoomNode.class, method = "renderEmeraldVfx")
    public static class EmeraldBuffLight {
        @SpirePostfixPatch
        public static void lights(ArrayList<FlameAnimationEffect> ___fEffects) {
            for (FlameAnimationEffect e : ___fEffects) {
                CustomLightPatches.processCustomLights(e);
            }
        }
    }

    @SpirePatch2(clz = EventRoom.class, method = "render")
    public static class EventLights {
        @SpirePostfixPatch
        public static void lights(EventRoom __instance) {
            if (__instance.event != null && __instance.event.waitTimer == 0f && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE) {
                CustomLightPatches.processCustomLights(__instance.event);
            }
        }
    }
}
