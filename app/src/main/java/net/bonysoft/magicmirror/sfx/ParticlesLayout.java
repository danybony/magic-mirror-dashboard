package net.bonysoft.magicmirror.sfx;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ParticlesLayout extends FrameLayout {

    private static final int PARTICLE_COUNT = 5;

    private final List<ImageView> particleViews = new ArrayList<>(PARTICLE_COUNT);

    public ParticlesLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initialise() {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            ImageView particleView = new ImageView(getContext());
            particleView.setLayoutParams(new ActionBar.LayoutParams(50, 50));
            addView(particleView);
            particleViews.add(particleView);
        }
    }

    public void startParticles(Particle particle) {
        applyDrawableFrom(particle);
        ParticleEffectRunner runner = new ParticleEffectRunner(particle.getEffect(), particleViews, this);
        runner.run();
    }

    private void applyDrawableFrom(Particle particle) {
        Drawable drawable = getResources().getDrawable(particle.drawableResId());
        for (ImageView particleView : particleViews) {
            particleView.setImageDrawable(drawable);
        }
    }

}
