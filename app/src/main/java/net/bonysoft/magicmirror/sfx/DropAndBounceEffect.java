package net.bonysoft.magicmirror.sfx;

import android.animation.ObjectAnimator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import java.util.List;

public class DropAndBounceEffect implements ParticleEffect {

    private static final long PARTICLE_DURATION = 1000L;

    private final BounceInterpolator INTERPOLATOR = new BounceInterpolator();

    @Override
    public void animateParticles(List<ImageView> particleViews, int parentWidth, int parentHeight) {
        for (ImageView particleView : particleViews) {
            int topOfScreen = 0;
            ObjectAnimator tossUp = ObjectAnimator.ofFloat(particleView, "y", topOfScreen, parentHeight);
            tossUp.setDuration(PARTICLE_DURATION);
            tossUp.setInterpolator(INTERPOLATOR);
            tossUp.start();
        }
    }
}
