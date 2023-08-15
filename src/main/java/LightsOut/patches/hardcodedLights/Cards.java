package LightsOut.patches.hardcodedLights;

public class Cards {

    // TODO use new energy orb render logic so it actually lines up
    /*@SpirePatch2(clz = AbstractCard.class, method = "renderSmallEnergy")
    public static class EnergyIconLight {
        private static final Color IC = new Color(1.0F, 0.4F, 0.1F, 1.0F);
        private static final Color DEFECT = Color.SKY;
        private static final Color SILENT = Color.CHARTREUSE;
        private static final Color WATCHER = Color.PURPLE;
        @SpirePostfixPatch
        public static void plz(AbstractCard __instance, TextureAtlas.AtlasRegion region, float x, float y) {
            Color c = Color.WHITE;
            switch (__instance.color) {
                case RED:
                    c = IC;
                    break;
                case GREEN:
                    c = SILENT;
                    break;
                case BLUE:
                    c = DEFECT;
                    break;
                case PURPLE:
                    c = WATCHER;
                    break;
                case COLORLESS:
                case CURSE:
                    break;
                default:
                    for (AbstractPlayer p : BaseMod.getModdedCharacters()) {
                        if (p.getCardColor().equals(__instance.color)) {
                            c = p.getCardTrailColor();
                            break;
                        }
                    }
            }
            ShaderLogic.lightsToRender.add(new LightData(
                    __instance.current_x + x * Settings.scale * __instance.drawScale + region.offsetX * Settings.scale + region.getRegionWidth()/2f * Settings.scale,
                    __instance.current_y + y * Settings.scale * __instance.drawScale + region.offsetY * Settings.scale + region.getRegionHeight()/2f * Settings.scale,
                    10f, 1f, c));
        }
    }*/
}
