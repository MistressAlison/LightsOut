package LightsOut.util;

import LightsOut.LightsOutMod;
import basemod.interfaces.ScreenPostProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ShaderLogic implements ScreenPostProcessor {
    public static final float TORCH_RADIUS = 200.0F * Settings.scale;

    public static final float SHINE_RADIUS = 50.0F * Settings.scale;

    public static final ShaderProgram sp = new ShaderProgram(SpriteBatch.createDefaultShader().getVertexShaderSource(), Gdx.files.internal("LightsOutResources/shaders/flashlight4.frag").readString(String.valueOf(StandardCharsets.UTF_8)));

    public static final ArrayList<LightData> lightsToRender = new ArrayList<>();

    public static float time;

    public void postProcess(SpriteBatch sb, TextureRegion textureRegion, OrthographicCamera orthographicCamera) {
        time += Gdx.graphics.getRawDeltaTime();
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(1, 0);
        if (LightsOutMod.modEnabled) {
            ShaderProgram back = sb.getShader();
            sb.setShader(sp);
            if (!CardCrawlGame.isPopupOpen) {
                int size = Math.min(256, lightsToRender.size());
                float[] xyri = new float[size * 4];
                float[] rgba = new float[size * 4];
                for (int i = 0; i < size; i++) {
                    xyri[4 * i] = ((LightData)lightsToRender.get(i)).x;
                    xyri[4 * i + 1] = ((LightData)lightsToRender.get(i)).y;
                    xyri[4 * i + 2] = ((LightData)lightsToRender.get(i)).radius;
                    xyri[4 * i + 3] = ((LightData)lightsToRender.get(i)).intensity;
                    rgba[4 * i] = ((LightData)lightsToRender.get(i)).color.r;
                    rgba[4 * i + 1] = ((LightData)lightsToRender.get(i)).color.g;
                    rgba[4 * i + 2] = ((LightData)lightsToRender.get(i)).color.b;
                    rgba[4 * i + 3] = ((LightData)lightsToRender.get(i)).color.a;
                }
                sp.setUniformi("u_lightObjects", size);
                sp.setUniform4fv("u_objectXYRI[0]", xyri, 0, size * 4);
                sp.setUniform4fv("u_objectColor[0]", rgba, 0, size * 4);
            } else {
                sp.setUniformi("u_lightObjects", 0);
            }
            sp.setUniformf("x_time", time);
            sp.setUniformf("u_mouse", InputHelper.mX, InputHelper.mY);
            sp.setUniformf("m_scale", LightsOutMod.mouseRadius);
            sp.setUniformi("m_mode", LightsOutMod.torchMode ? 1 : 0);
            sp.setUniformf("m_decay", 1.0F + LightsOutMod.torchModeDecay / 10.0F);
            sb.draw(textureRegion, 0.0F, 0.0F);
            sb.setShader(back);
        } else {
            sb.draw(textureRegion, 0.0F, 0.0F);
        }
        lightsToRender.clear();
    }
}