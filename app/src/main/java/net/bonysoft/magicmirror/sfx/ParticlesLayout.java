package net.bonysoft.magicmirror.sfx;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import net.bonysoft.magicmirror.R;

public class ParticlesLayout extends FrameLayout {

    private static final long REPEAT_DURATION = 1000;
    private final BounceInterpolator INTERPOLATOR = new BounceInterpolator();

    public ParticlesLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void startParticles() {
        postDelayed(playParticlesRunnable, REPEAT_DURATION);
    }

    private void startInternal() {
        int particleCount = 1;
        final List<ImageView> particleViews = createParticles(particleCount);
        for (ImageView particleView : particleViews) {
            addView(particleView);
            animateFall(particleView);
        }
    }

    private List<ImageView> createParticles(int count) {
        List<ImageView> particleHolder = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            ImageView particleView = new ImageView(getContext());
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_face);
            particleView.setImageDrawable(drawable);
            particleHolder.add(particleView);
        }
        return particleHolder;
    }

    private void animateFall(ImageView ball) {
        int topOfScreen = 0;
        ObjectAnimator tossUp = ObjectAnimator.ofFloat(ball, "y", topOfScreen, getHeight());
        long tossDuration = 1000L;
        tossUp.setDuration(tossDuration);
        tossUp.setInterpolator(INTERPOLATOR);
        tossUp.start();
    }

    private final Runnable playParticlesRunnable = new Runnable() {
        @Override
        public void run() {
            startInternal();
        }
    };

}
