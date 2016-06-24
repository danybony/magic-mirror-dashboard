package net.bonysoft.magicmirror.sfx;

import android.widget.ImageView;

import java.util.List;

public class ParticleEffectRunner {

    private final ParticleEffect effect;
    private final List<ImageView> particleViews;
    private final ParticlesLayout particlesLayout;

    public ParticleEffectRunner(ParticleEffect effect, List<ImageView> particleViews, ParticlesLayout particlesLayout) {
        this.effect = effect;
        this.particleViews = particleViews;
        this.particlesLayout = particlesLayout;
    }

    public void run() {
        moveParticlesOutsideOfScreen(particleViews);
        for (int i = 0; i < particleViews.size(); i++) {
            ParticleRunnable particleRunnable = new ParticleRunnable(particlesLayout, particleViews.get(i), effect);
            int totalDelay = (i * effect.delayInBetween());
            particlesLayout.postDelayed(particleRunnable, totalDelay);
        }

    }

    private void moveParticlesOutsideOfScreen(List<ImageView> particleViews) {
        for (ImageView particleView : particleViews) {
            particleView.setY(-particleView.getHeight());
        }
    }
}
