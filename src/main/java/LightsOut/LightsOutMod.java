package LightsOut;

import LightsOut.patches.CustomLightPatches;
import LightsOut.ui.ModTorch;
import LightsOut.util.ShaderLogic;
import LightsOut.util.TextureLoader;
import basemod.*;
import basemod.helpers.ScreenPostProcessorManager;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.ScreenPostProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

@SpireInitializer
public class LightsOutMod implements EditStringsSubscriber, PostInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger(LightsOutMod.class.getName());

    private static String modID;

    public static SpireConfig LOConfig;

    public static String FILE_NAME = "LightsOutConfig";

    public static final String ENABLE_MOD = "enableMod";

    public static boolean modEnabled = true;

    public static final String TORCH_MODE = "torchMode";

    public static boolean torchMode = false;

    public static final String MOUSE_RADIUS = "mouseRadium";

    public static int mouseRadius = 360;

    public static final String TORCH_MODE_DECAY = "torchModeDecay";

    public static int torchModeDecay = 5;

    public static UIStrings uiStrings;

    public static String[] TEXT;

    public static String[] EXTRA_TEXT;

    private static final String MODNAME = "Lights Out";

    private static final String AUTHOR = "Mistress Autumn";

    private static final String DESCRIPTION = "Turns off the Spire.";

    public static final String BADGE_IMAGE = "LightsOutResources/images/Badge.png";

    public LightsOutMod() {
        logger.info("Subscribe to BaseMod hooks");
        BaseMod.subscribe(this);
        setModID("LightsOut");
        logger.info("Done subscribing");
        logger.info("Adding mod settings");
        Properties LODefaultSettings = new Properties();
        LODefaultSettings.setProperty("enableMod", Boolean.toString(modEnabled));
        LODefaultSettings.setProperty("torchMode", Boolean.toString(torchMode));
        LODefaultSettings.setProperty("mouseRadium", String.valueOf(mouseRadius));
        LODefaultSettings.setProperty("torchModeDecay", String.valueOf(torchModeDecay));
        try {
            LOConfig = new SpireConfig(modID, FILE_NAME, LODefaultSettings);
            modEnabled = LOConfig.getBool("enableMod");
            torchMode = LOConfig.getBool("torchMode");
            mouseRadius = LOConfig.getInt("mouseRadium");
            torchModeDecay = LOConfig.getInt("torchModeDecay");
        } catch (IOException e) {
            logger.error("Lights Out SpireConfig initialization failed:");
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
    }

    public static void setModID(String ID) {
        modID = ID;
    }

    public static String getModID() {
        return modID;
    }

    public static void initialize() {
        LightsOutMod lightsOutMod = new LightsOutMod();
    }

    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        uiStrings = CardCrawlGame.languagePack.getUIString(makeID("ModConfigs"));
        EXTRA_TEXT = uiStrings.EXTRA_TEXT;
        TEXT = uiStrings.TEXT;
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        ModPanel settingsPanel = new ModPanel();
        float currentYposition = 740.0F;
        float sliderOffset = 50.0F + FontHelper.getWidth(FontHelper.charDescFont, TEXT[3], 1.0F / Settings.scale);
        float spacingY = 55.0F;
        ModLabeledToggleButton enableModsButton = new ModLabeledToggleButton(TEXT[0], 360.0F, currentYposition - 10.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, LOConfig.getBool("enableMod"), settingsPanel, label -> {

        },button -> {
            LOConfig.setBool("enableMod", button.enabled);
            modEnabled = button.enabled;
            if (modEnabled) {
                CardCrawlGame.sound.playA("ATTACK_FIRE", 0.4F);
            } else {
                CardCrawlGame.sound.play("SCENE_TORCH_EXTINGUISH");
            }
            try {
                LOConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        currentYposition -= spacingY;
        ModLabeledToggleButton torchModeButton = new ModLabeledToggleButton(TEXT[1], 360.0F, currentYposition - 10.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, LOConfig.getBool("torchMode"), settingsPanel, label -> {

        },button -> {
            LOConfig.setBool("torchMode", button.enabled);
            torchMode = button.enabled;
            try {
                LOConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        currentYposition -= spacingY;
        ModLabel radLabel = new ModLabel(TEXT[2], 400.0F, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, modLabel -> {

        });
        ModMinMaxSlider radSlider = new ModMinMaxSlider("", 400.0F + sliderOffset, currentYposition + 7.0F, 180.0F, 540.0F, LOConfig.getInt("mouseRadium"), "%.0f", settingsPanel, slider -> {
            LOConfig.setInt("mouseRadium", Math.round(slider.getValue()));
            mouseRadius = Math.round(slider.getValue());
            try {
                LOConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        currentYposition -= spacingY;
        ModLabel decayLabel = new ModLabel(TEXT[3], 400.0F, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, modLabel -> {

        });
        ModMinMaxSlider decaySlider = new ModMinMaxSlider("", 400.0F + sliderOffset, currentYposition + 7.0F, 0.0F, 10.0F, LOConfig.getInt("torchModeDecay"), "%.0f", settingsPanel, slider -> {
            LOConfig.setInt("torchModeDecay", Math.round(slider.getValue()));
            torchModeDecay = Math.round(slider.getValue());
            try {
                LOConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        currentYposition -= spacingY;
        settingsPanel.addUIElement((IUIElement)enableModsButton);
        settingsPanel.addUIElement((IUIElement)torchModeButton);
        settingsPanel.addUIElement((IUIElement)radLabel);
        settingsPanel.addUIElement((IUIElement)radSlider);
        settingsPanel.addUIElement((IUIElement)decayLabel);
        settingsPanel.addUIElement((IUIElement)decaySlider);
        settingsPanel.addUIElement((IUIElement)new ModTorch(325.0F, 740.0F));
        BaseMod.registerModBadge(badgeTexture, "Lights Out", "Mistress Autumn", "Turns off the Spire.", settingsPanel);
        logger.info("Done loading badge Image and mod options");
        ShaderLogic shaderLogic = new ShaderLogic();
        ScreenPostProcessorManager.addPostProcessor((ScreenPostProcessor)shaderLogic);
        logger.info("Scanning Custom Lighting");
        long time = -System.currentTimeMillis();
        CustomLightPatches.findCustomLights();
        time += System.currentTimeMillis();
        logger.info("Custom Lights Loaded. Processing Time: " + ((float)time / 1000.0F) + "s");
    }

    private String loadLocalizationIfAvailable(String fileName) {
        if (!Gdx.files.internal(getModID() + "Resources/localization/" + Settings.language.toString().toLowerCase() + "/" + fileName).exists()) {
            logger.info("Language: " + Settings.language.toString().toLowerCase() + ", not currently supported for " + fileName + ".");
            return "eng/" + fileName;
        }
        logger.info("Loaded Language: " + Settings.language.toString().toLowerCase() + ", for " + fileName + ".");
        return Settings.language.toString().toLowerCase() + "/" + fileName;
    }

    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/" + loadLocalizationIfAvailable("UI-Strings.json"));
        logger.info("Done editing strings");
    }

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
