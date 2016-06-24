package net.bonysoft.magicmirror.sfx;

import android.widget.ImageView;

import java.util.List;

public final class ParticleRunnable implements Runnable {

    private final List<ImageView> particleviews;
    private final ParticlesLayout particlesLayout;
    private final ParticleEffect effect;

    public ParticleRunnable(ParticlesLayout particlesLayout, List<ImageView> particleViews, ParticleEffect effect) {
        this.particleviews = particleViews;
        this.particlesLayout = particlesLayout;
        this.effect = effect;
    }

    @Override
    public void run() {
        effect.animateParticles(
                particleviews,
                particlesLayout.getWidth(),
                particlesLayout.getHeight()
        );
    }

}
