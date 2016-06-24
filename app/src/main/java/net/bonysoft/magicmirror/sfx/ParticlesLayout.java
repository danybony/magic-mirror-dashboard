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

public class ParticlesLayout extends FrameLayout {

    private static final long REPEAT_DURATION = 1000;
    private final BounceInterpolator INTERPOLATOR = new BounceInterpolator();

    private final List<ImageView> particleviews = new ArrayList<>(50);

    public ParticlesLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initialise() {
        int count = 50;
        Drawable drawable = getResources().getDrawable(android.R.mipmap.sym_def_app_icon);
        for (int i = 0; i < count; i++) {
            ImageView particleView = new ImageView(getContext());
            particleView.setImageDrawable(drawable);
            addView(particleView);
            particleviews.add(particleView);
        }
    }

    public void startParticles() {
        postDelayed(playParticlesRunnable, REPEAT_DURATION);
    }

    private final Runnable playParticlesRunnable = new Runnable() {
        @Override
        public void run() {
            for (ImageView particleView : particleviews) {
                animateFall(particleView);
            }
        }
    };

    private void animateFall(ImageView particleView) {
        int topOfScreen = 0;
        ObjectAnimator tossUp = ObjectAnimator.ofFloat(particleView, "y", topOfScreen, getHeight());
        long tossDuration = 1000L;
        tossUp.setDuration(tossDuration);
        tossUp.setInterpolator(INTERPOLATOR);
        tossUp.start();
    }

}
