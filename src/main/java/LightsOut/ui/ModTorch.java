package LightsOut.ui;

import LightsOut.LightsOutMod;
import basemod.IUIElement;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.scene.LightFlareLEffect;
import com.megacrit.cardcrawl.vfx.scene.TorchParticleLEffect;

public class ModTorch implements IUIElement {
    public float x;
    public float y;
    private final Color color;
    private float particleTimer1;
    private static TextureAtlas.AtlasRegion img;

    public ModTorch(float x, float y) {
        this.x = x * Settings.scale;
        this.y = y * Settings.scale;
        this.color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
        if (img == null) {
            img = ImageMaster.vfxAtlas.findRegion("env/torch");
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(img, this.x - (img.packedWidth / 2f), this.y - (img.packedHeight / 2f) - 24.0F * Settings.yScale, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, 1.0F, 1.0F, 0.0F);
    }

    public void update() {
        if (LightsOutMod.modEnabled) {
            this.particleTimer1 -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer1 < 0.0F) {
                this.particleTimer1 = 0.1F;
                LightsOutMod.managedEffects.add(new TorchParticleLEffect(this.x, this.y + 14.0F * Settings.scale));
                LightsOutMod.managedEffects.add(new LightFlareLEffect(this.x, this.y + 14.0F * Settings.scale));
            }
        }
    }

    public int renderLayer() {
        return 2;
    }

    public int updateOrder() {
        return 0;
    }

    public void set(float xPos, float yPos) {
        this.x = xPos * Settings.scale;
        this.y = yPos * Settings.scale;
    }

    public void setX(float xPos) {
        this.x = xPos * Settings.scale;
    }

    public void setY(float yPos) {
        this.y = yPos * Settings.scale;
    }

    public float getX() {
        return this.x / Settings.scale;
    }

    public float getY() {
        return this.y / Settings.scale;
    }
}
