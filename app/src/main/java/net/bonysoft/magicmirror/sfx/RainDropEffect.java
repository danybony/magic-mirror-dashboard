package net.bonysoft.magicmirror.sfx;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.novoda.notils.logger.simple.Log;

import java.util.Random;

public class RainDropEffect implements ParticleEffect {

    private static final long FALL_DURATION = 250L;

    private static final Random RANDOM = new Random(SystemClock.currentThreadTimeMillis());
    private final Interpolator INTERPOLATOR = new LinearInterpolator();

    @Override
    public void animateParticle(View particleView, int parentWidth, int parentHeight) {
        float x = RANDOM.nextFloat() * parentWidth;
        Log.d("x: " + x);
        particleView.setX(x);
        ObjectAnimator tossUp = ObjectAnimator.ofFloat(particleView, "y", 0, parentHeight);
        tossUp.setDuration(FALL_DURATION);
        tossUp.setInterpolator(INTERPOLATOR);
        tossUp.setRepeatMode(ValueAnimator.RESTART);
        tossUp.setRepeatCount(ValueAnimator.INFINITE);
        tossUp.start();
    }

    @Override
    public int delayInBetween() {
        return 1000 + RANDOM.nextInt(1000);
    }
}
