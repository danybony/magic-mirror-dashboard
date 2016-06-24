package net.bonysoft.magicmirror.sfx;

import android.support.annotation.DrawableRes;

public class Particle {

    @DrawableRes
    private final int drawableResId;
    private final ParticleEffect effect;

    public Particle(@DrawableRes int drawableRes, ParticleEffect effect) {
        this.drawableResId = drawableRes;
        this.effect = effect;
    }

    @DrawableRes
    public int drawableResId() {
        return drawableResId;
    }

    public ParticleEffect getEffect() {
        return effect;
    }
}
