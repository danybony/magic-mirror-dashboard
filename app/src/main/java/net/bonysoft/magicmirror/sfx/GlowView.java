package net.bonysoft.magicmirror.sfx;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.view.View;

public class GlowView extends View {

    private static final int TRANSITION_DURATION = 700;

    private final SparseArrayCompat<ColorDrawable> colors = new SparseArrayCompat<>();

    public GlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void transitionToColor(@ColorRes int colorRes) {
        // TODO timeout if too fast
        Drawable previousBackground = getPreviousDrawableSafely();
        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{
                previousBackground,
                getOrCreateColorDrawableFor(colorRes)
        });
        setBackground(transitionDrawable);
        transitionDrawable.startTransition(TRANSITION_DURATION);
    }

    private ColorDrawable getOrCreateColorDrawableFor(@ColorRes int colorRes) {
        ColorDrawable colorDrawable = colors.get(colorRes);
        if (colorDrawable == null) {
            colorDrawable = new ColorDrawable(getColor(colorRes));
            colors.put(colorRes, colorDrawable);
        }
        return colorDrawable;
    }

    private Drawable getPreviousDrawableSafely() {
        Drawable previousDrawable = getBackground();
        if (previousDrawable == null) {
            int transparentRes = getColor(android.R.color.transparent);
            return new ColorDrawable(transparentRes);
        }
        return previousDrawable;
    }

    @ColorInt
    private int getColor(@ColorRes int colorRes) {
        return getResources().getColor(colorRes);
    }
}
