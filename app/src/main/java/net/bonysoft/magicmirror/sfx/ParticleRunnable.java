package net.bonysoft.magicmirror.sfx;

import android.widget.ImageView;

public final class ParticleRunnable implements Runnable {

    private final ImageView particleview;
    private final ParticlesLayout particlesLayout;
    private final ParticleEffect effect;

    public ParticleRunnable(ParticlesLayout particlesLayout, ImageView particleViews, ParticleEffect effect) {
        this.particleview = particleViews;
        this.particlesLayout = particlesLayout;
        this.effect = effect;
    }

    @Override
    public void run() {

        effect.animateParticle(particleview, particlesLayout.getWidth(), particlesLayout.getHeight());
    }

}
